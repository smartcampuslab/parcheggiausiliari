package eu.trentorise.smartcampus.parcheggiausiliari.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogObjectExtended extends LogObject {

	/**
	 * parcheggi totali totali/occupati in struttura
	 */
	private int slotsTotal;
	private int slotsOccupiedOnTotal;

	/**
	 * parcheggi gratuiti totali/occupati
	 */
	private int slotsFree;
	private int slotsOccupiedOnFree;
	/**
	 * parcheggi liberi totali/occupati
	 */
	private int slotsUnavailable;
	/**
	 * parcheggi a pagamento totali/occupati
	 */
	private int slotsPaying;
	private int slotsOccupiedOnPaying;
	/**
	 * parcheggi a disco orario totali/occupati
	 */
	private int slotsTimed;
	private int slotsOccupiedOnTimed;

	private Street streetData;
	private Parking parkingData;


	public Street getStreetData() {
		return streetData;
	}

	public void setStreetData(Street streetData) {
		this.streetData = streetData;
	}

	public Parking getParkingData() {
		return parkingData;
	}

	public void setParkingData(Parking parkingData) {
		this.parkingData = parkingData;
	}

	public int getSlotsTotal() {
		return slotsTotal;
	}

	public void setSlotsTotal(int slotsTotal) {
		this.slotsTotal = slotsTotal;
	}

	public int getSlotsOccupiedOnTotal() {
		return slotsOccupiedOnTotal;
	}

	public void setSlotsOccupiedOnTotal(int slotsOccupiedOnTotal) {
		this.slotsOccupiedOnTotal = slotsOccupiedOnTotal;
	}

	public int getSlotsFree() {
		return slotsFree;
	}

	public void setSlotsFree(int slotsFree) {
		this.slotsFree = slotsFree;
	}

	public int getSlotsOccupiedOnFree() {
		return slotsOccupiedOnFree;
	}

	public void setSlotsOccupiedOnFree(int slotsOccupiedOnFree) {
		this.slotsOccupiedOnFree = slotsOccupiedOnFree;
	}

	public int getSlotsUnavailable() {
		return slotsUnavailable;
	}

	public void setSlotsUnavailable(int slotsUnavailable) {
		this.slotsUnavailable = slotsUnavailable;
	}

	public int getSlotsPaying() {
		return slotsPaying;
	}

	public void setSlotsPaying(int slotsPaying) {
		this.slotsPaying = slotsPaying;
	}

	public int getSlotsOccupiedOnPaying() {
		return slotsOccupiedOnPaying;
	}

	public void setSlotsOccupiedOnPaying(int slotsOccupiedOnPaying) {
		this.slotsOccupiedOnPaying = slotsOccupiedOnPaying;
	}

	public int getSlotsTimed() {
		return slotsTimed;
	}

	public void setSlotsTimed(int slotsTimed) {
		this.slotsTimed = slotsTimed;
	}

	public int getSlotsOccupiedOnTimed() {
		return slotsOccupiedOnTimed;
	}

	public void setSlotsOccupiedOnTimed(int slotsOccupiedOnTimed) {
		this.slotsOccupiedOnTimed = slotsOccupiedOnTimed;
	}

	public static Parking mapToPark(Map<String, Object> value) {
		Parking returnPark = new Parking();
		returnPark.setAgency((value.get("agency")!=null)?(String) value.get("agency"):"");
		returnPark.setAuthor((value.get("author")!=null)?(String) value.get("author"):"");
		returnPark.setId((value.get("id")!=null)?(String) value.get("id"):"");
		returnPark.setName((value.get("name")!=null)?(String) value.get("name"):"");
		returnPark.setUpdateTime((value.get("updateTime")!=null)?(Long) value.get("updateTime"):0);
		if (value.get("slotsConfiguration")!=null)
		{
			returnPark.setSlotsTotal((Integer) value.get("slotsTotal"));
			returnPark.setSlotsConfiguration(convertVehicleSlot((ArrayList<HashMap<String, Object>>) value.get("slotsConfiguration")));
		} 
		returnPark.setChannel( (value.get("channel") != null) ?(Integer) value.get("channel") : 0);
		return returnPark;
	}

	private static double[] convertPosition(List<Double> coords) {
		double[] ret = new double[coords.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = coords.get(i).doubleValue();
		}
		return ret;
	}
	private static List<VehicleSlot> convertVehicleSlot(ArrayList<HashMap<String, Object>> array) {
		List<VehicleSlot> ret = new ArrayList<VehicleSlot>();
		for (int i = 0; i < array.size(); i++) {
			VehicleSlot vs = new VehicleSlot();
			vs.setCarSharingSlotNumber((Integer) array.get(i).get("carSharingSlotNumber"));
			vs.setCarSharingSlotOccupied((Integer) array.get(i).get("carSharingSlotOccupied"));
			vs.setFreeParkSlotNumber((Integer) array.get(i).get("freeParkSlotNumber"));
			vs.setFreeParkSlotOccupied((Integer) array.get(i).get("freeParkSlotOccupied"));
			vs.setFreeParkSlotSignNumber((Integer) array.get(i).get("freeParkSlotSignNumber"));
			vs.setFreeParkSlotSignOccupied((Integer) array.get(i).get("freeParkSlotSignOccupied"));
			vs.setHandicappedSlotNumber((Integer) array.get(i).get("handicappedSlotNumber"));
			vs.setHandicappedSlotOccupied((Integer) array.get(i).get("handicappedSlotOccupied"));
			vs.setLoadingUnloadingSlotNumber((Integer) array.get(i).get("loadingUnloadingSlotNumber"));
			vs.setLoadingUnloadingSlotOccupied((Integer) array.get(i).get("loadingUnloadingSlotOccupied"));
			vs.setPaidSlotNumber((Integer) array.get(i).get("paidSlotNumber"));
			vs.setPaidSlotOccupied((Integer) array.get(i).get("paidSlotOccupied"));
			vs.setPinkSlotNumber((Integer) array.get(i).get("pinkSlotNumber"));
			vs.setPinkSlotOccupied((Integer) array.get(i).get("pinkSlotOccupied"));
			vs.setRechargeableSlotNumber((Integer) array.get(i).get("rechargeableSlotNumber"));
			vs.setRechargeableSlotOccupied((Integer) array.get(i).get("rechargeableSlotOccupied"));
			vs.setReservedSlotNumber((Integer) array.get(i).get("reservedSlotNumber"));
			vs.setReservedSlotOccupied((Integer) array.get(i).get("reservedSlotOccupied"));
			vs.setSlotNumber((Integer) array.get(i).get("slotNumber"));
			vs.setSlotOccupied((Integer) array.get(i).get("slotOccupied"));
			vs.setTimedParkSlotNumber((Integer) array.get(i).get("timedParkSlotNumber"));
			vs.setTimedParkSlotOccupied((Integer) array.get(i).get("timedParkSlotOccupied"));
			vs.setUnusuableSlotNumber((Integer) array.get(i).get("unusuableSlotNumber"));
			vs.setVehicleType((String) array.get(i).get("vehicleType"));
			vs.setVehicleTypeActive((Boolean) array.get(i).get("vehicleTypeActive"));
			ret.add(vs);
		}
		return ret;
	}

	public static Street mapToStreet(Map<String, Object> value) {
		Street returnStreet = new Street();
		returnStreet.setAgency((value.get("agency")!=null)?(String) value.get("agency"):"");
		returnStreet.setAuthor((value.get("author")!=null)?(String) value.get("author"):"");
		returnStreet.setId((value.get("id")!=null)?(String) value.get("id"):"");
		returnStreet.setName((value.get("name")!=null)?(String) value.get("name"):"");
		returnStreet.setUpdateTime((value.get("updateTime")!=null)?(Long) value.get("updateTime"):0);

		if (value.get("slotsConfiguration")!=null)
		{
			//returnStreet.setSlotsTotal((Integer) value.get("slotsTotal"));
			returnStreet.setSlotsConfiguration(convertVehicleSlot((ArrayList<HashMap<String, Object>>) value.get("slotsConfiguration")));
//			returnStreet.setSlotsConfiguration((List<VehicleSlot>) value
//					.get("slotsConfiguration"));
		} else {
			//for rovereto I need a intermediate cast
			List<VehicleSlot> lv = new ArrayList<VehicleSlot>();
			VehicleSlot vs = new VehicleSlot();
			vs.setVehicleType("Car");
			vs.setFreeParkSlotNumber((value.get("slotsFree")!=null)?(Integer) value.get("slotsFree"):0);
			vs.setFreeParkSlotSignNumber((value.get("slotsFreeSigned")!=null)?(Integer) value.get("slotsFreeSigned"):0);
			vs.setPaidSlotNumber((value.get("slotsPaying")!=null)?(Integer) value.get("slotsPaying"):0);
			vs.setReservedSlotNumber((value.get("slotsReserved")!=null)?(Integer) value.get("slotsReserved"):0);
			vs.setHandicappedSlotNumber((value.get("slotsHandicapped")!=null)?(Integer) value.get("slotsHandicapped"):0);
			vs.setTimedParkSlotNumber((value.get("slotsTimed")!=null)?(Integer) value.get("slotsTimed"):0);
			vs.setFreeParkSlotOccupied((value.get("slotsOccupiedOnFree")!=null)?(Integer) value.get("slotsOccupiedOnFree"):0);
			vs.setFreeParkSlotSignOccupied((value.get("slotsOccupiedOnFreeSigned")!=null)?(Integer) value.get("slotsOccupiedOnFreeSigned"):0);
			vs.setPaidSlotOccupied((value.get("slotsOccupiedOnPaying")!=null)?(Integer) value.get("slotsOccupiedOnPaying"):0);
			vs.setReservedSlotOccupied((value.get("slotsOccupiedOnReserved")!=null)?(Integer) value.get("slotsOccupiedOnReserved"):0);
			vs.setHandicappedSlotOccupied((value.get("slotsOccupiedOnHandicapped")!=null)?(Integer) value.get("slotsOccupiedOnHandicapped"):0);
			vs.setTimedParkSlotOccupied((value.get("slotsOccupiedOnTimed")!=null)?(Integer) value.get("slotsOccupiedOnTimed"):0);
			vs.setUnusuableSlotNumber((value.get("slotsUnavailable")!=null)?(Integer) value.get("slotsUnavailable"):0);
			lv.add(vs);
			returnStreet.setSlotsConfiguration(lv);
		}

		returnStreet.setChannel((value.get("channel")!=null)?(Integer) value.get("channel"):0);
		return returnStreet;
	}

	public void extractOldValues(String type) {
		 
		
		if ("park".compareTo(type) == 0) {
			// add park data from slot conf
			for (VehicleSlot vs : parkingData.getSlotsConfiguration()) {
				slotsTotal += vs.getSlotNumber();
				if (vs.getSlotOccupied() != null) {
					slotsOccupiedOnTotal += vs.getSlotOccupied();
				} else {
					slotsOccupiedOnTotal += ((vs.getCarSharingSlotOccupied() != null) ? vs
							.getCarSharingSlotOccupied() : 0)
							+ ((vs.getFreeParkSlotOccupied() != null) ? vs
									.getFreeParkSlotOccupied() : 0)
							+ ((vs.getFreeParkSlotSignOccupied() != null) ? vs
									.getFreeParkSlotSignOccupied() : 0)
							+ ((vs.getHandicappedSlotOccupied() != null) ? vs
									.getHandicappedSlotOccupied() : 0)
							+ ((vs.getLoadingUnloadingSlotOccupied() != null) ? vs
									.getLoadingUnloadingSlotOccupied() : 0)
							+ ((vs.getPaidSlotOccupied() != null) ? vs
									.getPaidSlotOccupied() : 0)
							+ ((vs.getPinkSlotOccupied() != null) ? vs
									.getPinkSlotOccupied() : 0)
							+ ((vs.getRechargeableSlotOccupied() != null) ? vs
									.getRechargeableSlotOccupied() : 0)
							+ ((vs.getReservedSlotOccupied() != null) ? vs
									.getReservedSlotOccupied() : 0)
							+ ((vs.getTimedParkSlotOccupied() != null) ? vs
									.getTimedParkSlotOccupied() : 0);
				}
				if (vs.getUnusuableSlotNumber() != null
						&& vs.getUnusuableSlotNumber() > 0) {
					slotsUnavailable += vs.getUnusuableSlotNumber();
				}
			}

		} else {

			// add street data from slot conf
			for (VehicleSlot vs : streetData.getSlotsConfiguration()) {
				// valFree.setText("" + s.getSlotsOccupiedOnFree());
				// valWork.setText("" + s.getSlotsUnavailable());
				// valTime.setText("" + s.getSlotsOccupiedOnTimed());
				// valPay.setText("" + s.getSlotsOccupiedOnPaying
				slotsOccupiedOnFree += ((vs.getFreeParkSlotOccupied() != null) ? vs
						.getFreeParkSlotOccupied() : 0)
						+ ((vs.getFreeParkSlotSignOccupied() != null) ? vs
								.getFreeParkSlotSignOccupied() : 0);
				if (vs.getUnusuableSlotNumber() != null
						&& vs.getUnusuableSlotNumber() > 0) {
					slotsUnavailable += vs.getUnusuableSlotNumber();
				}
				slotsOccupiedOnTimed+=((vs.getTimedParkSlotOccupied() != null) ? vs
						.getTimedParkSlotOccupied() : 0);
				slotsOccupiedOnPaying+=((vs.getPaidSlotOccupied() != null) ? vs
						.getPaidSlotOccupied() : 0);
				
			}


		}
	}
}
