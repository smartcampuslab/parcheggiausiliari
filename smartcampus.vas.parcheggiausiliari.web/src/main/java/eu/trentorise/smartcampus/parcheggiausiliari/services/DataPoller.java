package eu.trentorise.smartcampus.parcheggiausiliari.services;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

@Service
@SuppressWarnings("all")
public class DataPoller {

	// - per stazioni:
	// https://tn.smartcampuslab.it/bikesharing/stations/rovereto
	// - per log della stazione:
	// https://tn.smartcampuslab.it/bikesharing/stations/rovereto/{stationId}/reports

	private static final Logger logger = Logger.getLogger("DataPoller");

	@Autowired
	private OrionService orionService;

	private Client client;

	@PostConstruct
	private void init() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		client = Client.create(clientConfig);
	}

	@Scheduled(fixedRate = 2 * 60 * 1000)
	public void retrieveBikes() {
		logger.info("retriving bike data...");
		ClientResponse res = client
				.resource(
						"https://tn.smartcampuslab.it/bikesharing/stations/rovereto")
				.accept("application/json").get(ClientResponse.class);

		if (res.getStatus() == 200) {
			Map<String, Object> e = res.getEntity(Map.class);

			List<Map<String, Object>> d = (List<Map<String, Object>>) e
					.get("data");
			for (Map<String, Object> dd : d) {
				orionService.insertBike(orionService.bykeType, dd);
			}
		}

		logger.info("BIKE orion DONE");
	}

	public void retrieveBikeReports() {

	}

	public void retrieveTnParks() {

	}
}
