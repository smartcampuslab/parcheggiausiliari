package eu.trentorise.smartcampus.parcheggiausiliari.model;

import java.util.Comparator;
import java.util.List;


public class Street extends GeoObject implements Comparator<Street>{
	private static final long serialVersionUID = 387771948768252561L;

	//private int slotsTotal;
	
	private List<VehicleSlot> slotsConfiguration;

	private String polyline;
	
	private LastChange lastChange;
	private int channel;
	
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}
	
//	public int getSlotsTotal() {
//		return slotsTotal;
//	}

//
//	public void setSlotsTotal(int slotsTotal) {
//		this.slotsTotal = slotsTotal;
//	}


	public List<VehicleSlot> getSlotsConfiguration() {
		return slotsConfiguration;
	}


	public void setSlotsConfiguration(List<VehicleSlot> slotsConfiguration) {
		this.slotsConfiguration = slotsConfiguration;
	}


	public String getPolyline() {
		return polyline;
	}


	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}


	public LastChange getLastChange() {
		return lastChange;
	}


	public void setLastChange(LastChange lastChange) {
		this.lastChange = lastChange;
	}


	@Override
	public int compare(Street lhs, Street rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}
	
	
}
