package eu.trentorise.smartcampus.parcheggiausiliari.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.geo.Circle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.network.RemoteException;
import eu.trentorise.smartcampus.parcheggiausiliari.data.GeoStorage;
import eu.trentorise.smartcampus.parcheggiausiliari.data.LogMongoStorage;
import eu.trentorise.smartcampus.parcheggiausiliari.model.KMLData;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LastChange;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.model.StreetLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ViaBean;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ViaBean.PointBean;
import eu.trentorise.smartcampus.presentation.common.exception.DataException;
import eu.trentorise.smartcampus.presentation.common.exception.NotFoundException;

@Service
public class DataService {

	@Autowired 
	private LogMongoStorage logMongoStorage;
	@Autowired
	private GeoStorage geoStorage;

	@Value("${parking.sources}")
	private String parkingSources;
	@Value("${parking.agencies}")
	private String parkingAgencies;

	@Value("${street.sources}")
	private String streetURLs;
	@Value("${street.agencies}")
	private String streetAgencies;
	
	@PostConstruct
	private void initData() throws IOException, DataException {
		String[] agencies = parkingAgencies.split(",");
		String[] refs = parkingSources.split(",");
		
		if (refs.length != agencies.length) throw new IOException("number of agencies does not match number of sources");

		for (int i = 0; i < refs.length; i++) {
			String agency = agencies[i];

			ClassPathResource res = new ClassPathResource(refs[i]);
			List<KMLData> data = KMLHelper.readData(res.getInputStream());
			for (KMLData item : data) {
				Parking p = new Parking();
				p.setId("parking@"+agency+"@"+item.getId());
				p.setName(item.getName());
				p.setAgency(agency);
				p.setSlotsTotal(item.getTotal());
				p.setPosition(new double[]{item.getLat(),item.getLon()});
				saveOrUpdateParking(p);
			}
		}
	}
	
	@Scheduled(fixedRate = 24*60*60*1000)
	private void updateStreets() throws SecurityException, RemoteException, IOException, DataException {
		String[] agencies = streetAgencies.split(",");
		String[] refs = streetURLs.split(",");
		
		if (refs.length != agencies.length) throw new IOException("number of agencies does not match number of sources");

		for (int i = 0; i < refs.length; i++) {
			String agency = agencies[i];
			String urlString = refs[i];
			if (ResourceUtils.isUrl(urlString)) {
				URL url = ResourceUtils.getURL(urlString);
				InputStream is = url.openStream();
				List<ViaBean> vie = JsonUtils.toObjectList(IOUtils.toString(is), ViaBean.class);
				for (ViaBean via : vie) {
					Street street = new Street();
					street.setId("street@"+agency+"@"+via.getId());
					street.setAreaId(via.getAreaId());
					street.setAgency(agency);
					street.setName(via.getStreetReference());
					street.setPolyline(PolylineEncoder.encode(via.getGeometry().getPoints()));
					PointBean start = via.getGeometry().getPoints().get(0);
					street.setPosition(new double[]{start.getLat(),start.getLng()});
					if (via.getFreeParkSlotNumber() != null) {
						street.setSlotsFree(via.getFreeParkSlotNumber());
					}
					if (via.getPaidSlotNumber() != null) {
						street.setSlotsPaying(via.getPaidSlotNumber());
					} else if (via.getSlotNumber() != null) {
						street.setSlotsPaying(via.getSlotNumber());
					}
					if (via.getTimedParkSlotNumber() != null){
						street.setSlotsTimed(via.getTimedParkSlotNumber());
					}
					//street.setSlotsUnavailable(via.getReservedSlotNumber());
					saveOrUpdateStreet(street);
				}
			}

		}
	}
	
	public List<Parking> getParkings(String agency) throws DataException {
		return geoStorage.searchObjects(Parking.class, (Circle)null, Collections.<String,Object>singletonMap("agency", agency));
	}
	public List<Street> getStreets(String agency) throws DataException {
		return geoStorage.searchObjects(Street.class, (Circle)null, Collections.<String,Object>singletonMap("agency", agency));
	}
	
	public List<Parking> getParkings(String agency, double lat, double lon, double radius) throws DataException {
		return geoStorage.searchObjects(Parking.class, new Circle(lat, lon, radius), Collections.<String,Object>singletonMap("agency", agency));
	}
	public List<Street> getStreets(String agency, double lat, double lon, double radius) throws DataException {
		return geoStorage.searchObjects(Street.class, new Circle(lat, lon, radius), Collections.<String,Object>singletonMap("agency", agency));
	}
	
	
	public void saveOrUpdateStreet(Street s) throws DataException {
		try {
			Street old = geoStorage.getObjectByIdAndAgency(Street.class, s.getId(), s.getAgency());
			old.setAgency(s.getAgency());
			old.setSlotsFree(s.getSlotsFree());
			old.setSlotsPaying(s.getSlotsPaying());
			old.setSlotsTimed(s.getSlotsTimed());
			old.setName(s.getName());
			old.setPolyline(s.getPolyline());
			old.setPosition(s.getPosition());
			old.setDescription(s.getDescription());
			geoStorage.storeObject(old);
		} catch (NotFoundException e) {
			geoStorage.storeObject(s);
		}
	}
	public void saveOrUpdateParking(Parking p) throws DataException {
		try {
			Parking old = geoStorage.getObjectByIdAndAgency(Parking.class, p.getId(), p.getAgency());
			old.setName(p.getName());
			old.setAgency(p.getAgency());
			old.setPosition(p.getPosition());
			old.setDescription(p.getDescription());
			old.setSlotsTotal(p.getSlotsTotal());
			geoStorage.storeObject(old);
		} catch (NotFoundException e) {
			geoStorage.storeObject(p);
		}
	}
	
	public void updateStreetData(Street s, String agencyId, String authorId) throws DataException, NotFoundException {
		Street old = geoStorage.getObjectByIdAndAgency(Street.class, s.getId(), agencyId);
		old.setSlotsFree(s.getSlotsFree());
		old.setSlotsOccupiedOnFree(s.getSlotsOccupiedOnFree());
		old.setSlotsPaying(s.getSlotsPaying());
		old.setSlotsOccupiedOnPaying(s.getSlotsOccupiedOnPaying());
		old.setSlotsTimed(s.getSlotsTimed());
		old.setSlotsUnavailable(s.getSlotsUnavailable());
		StreetLog sl = new StreetLog();
		sl.setAuthor(authorId);
		sl.setTime(System.currentTimeMillis());
		sl.setValue(old);
		logMongoStorage.storeLog(sl);
		LastChange lc = new LastChange();
		lc.setAuthor(authorId);
		lc.setTime(sl.getTime());
		old.setLastChange(lc);
		geoStorage.storeObject(old);
	}
	
	public void updateParkingData(Parking object, String agencyId, String authorId) throws DataException, NotFoundException {
		Parking old = geoStorage.getObjectByIdAndAgency(Parking.class, object.getId(), agencyId);
		old.setSlotsOccupiedOnTotal(object.getSlotsOccupiedOnTotal());
		old.setSlotsTotal(object.getSlotsTotal());
		old.setSlotsUnavailable(object.getSlotsUnavailable());

		ParkingLog sl = new ParkingLog();
		sl.setAuthor(authorId);
		sl.setTime(System.currentTimeMillis());
		sl.setValue(old);
		logMongoStorage.storeLog(sl);
		LastChange lc = new LastChange();
		lc.setAuthor(authorId);
		lc.setTime(sl.getTime());
		old.setLastChange(lc);
		geoStorage.storeObject(old);
	}

}
