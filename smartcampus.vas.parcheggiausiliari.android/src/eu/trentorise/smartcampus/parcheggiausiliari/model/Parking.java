package eu.trentorise.smartcampus.parcheggiausiliari.model;

import java.util.List;


public class Parking extends GeoObject {
	private static final long serialVersionUID = 8076535734041609036L;

//	private int slotsTotal;
//	private int slotsOccupiedOnTotal;
//	private int slotsUnavailable;
//	private LastChange lastChange;
	private int slotsTotal;
	private List<VehicleSlot> slotsConfiguration;
	private int channel;
	
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}
	
	public int getSlotsTotal() {
		return slotsTotal;
	}
	public void setSlotsTotal(int slotsTotal) {
		this.slotsTotal = slotsTotal;
	}
	public List<VehicleSlot> getSlotsConfiguration() {
		return slotsConfiguration;
	}
	public void setSlotsConfiguration(List<VehicleSlot> slotsConfiguration) {
		this.slotsConfiguration = slotsConfiguration;
	}
			
}
