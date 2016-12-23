package eu.trentorise.smartcampus.parcheggiausiliari.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.AddGeoPoints;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.SendSignalProcessor;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateParkListInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateSegnalaInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoAgenteInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoParkingInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoStreetInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStreetListInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.model.StreetLog;
import eu.trentorise.smartcampus.parcheggiausiliari.util.constants.Parcheggi_Services;
import eu.trentorise.smartcampus.parcheggiausiliari.util.processor.GetParkingListProcessor;
import eu.trentorise.smartcampus.parcheggiausiliari.util.processor.GetParkingMapProcessor;
import eu.trentorise.smartcampus.parcheggiausiliari.util.processor.GetStoricoAgenteProcessor;
import eu.trentorise.smartcampus.parcheggiausiliari.util.processor.GetStoricoParkingProcessor;
import eu.trentorise.smartcampus.parcheggiausiliari.util.processor.GetStoricoStreetProcessor;
import eu.trentorise.smartcampus.parcheggiausiliari.util.processor.GetStreetMapProcessor;
import eu.trentorise.smartcampus.parcheggiausiliari.util.processor.GetStreetsListProcessor;

/**
 * Class containing all methods which make use of the Services listed in the
 * interface {@link Parcheggi_Services}
 * 
 * @author Michele Armellini
 * 
 */
public class AusiliariHelper {
	private static Context mContext;
	private static AusiliariHelper instance = null;

	protected AusiliariHelper(Context mContext) {
		AusiliariHelper.mContext = mContext;
	}

	public static void init(Context mContext) {
		if (instance == null) {
			instance = new AusiliariHelper(mContext);
		}
	}

	public static AusiliariHelper getInstance() {
		if (instance == null && mContext != null) {
			AusiliariHelper.init(mContext);
		}
		return instance;
	}

	public static void sendDataProcessor(GeoObject obj, Activity activity,
			UpdateSegnalaInterface updateSegnalaInterface) {
		SendSignalProcessor ast = new SendSignalProcessor(activity);
		ast.execute(obj, updateSegnalaInterface);
	}

	public static void getStoricoStreetProcessor(
			UpdateStoricoStreetInterface updateStoricoStreetInterface,
			Activity activity, Street street) {
		GetStoricoStreetProcessor asa = new GetStoricoStreetProcessor(activity);
		asa.execute(updateStoricoStreetInterface, street);
	}

	public static void getStoricoParkingProcessor(
			UpdateStoricoParkingInterface updateStoricoParkingInterface,
			Activity activity, Parking park) {
		GetStoricoParkingProcessor asa = new GetStoricoParkingProcessor(
				activity);
		asa.execute(updateStoricoParkingInterface, park);
	}

	public static List<LogObject> getStorico(GeoObject obj) {
		String request = null;
		List<LogObject> list = null;
		try {
			if (obj instanceof Parking) {
				request = RemoteConnector.getJSON(
						Parcheggi_Services.HOST,
						mContext.getResources().getString(R.string.park)
								+ mContext.getResources().getString(
										R.string.applocation)
								+ Parcheggi_Services.PARKLOGLIST + "?"
								+ "type=parking"
								+ Parcheggi_Services.LOGELEMENTS + "&id="
								+ obj.getId(), mContext.getResources()
								.getString(R.string.token));
			} else {
				request = RemoteConnector.getJSON(
						Parcheggi_Services.HOST,
						mContext.getResources().getString(R.string.park)
								+ mContext.getResources().getString(
										R.string.applocation)
								+ Parcheggi_Services.STREETLOGLIST + "?"
								+ "type=parking"
								+ Parcheggi_Services.STREETLOGLIST + "&id="
								+ obj.getId(), mContext.getResources()
								.getString(R.string.token));
			}
			list = JsonUtils.toObjectList(request, LogObject.class);
			// convert valueString to value
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return list; //return null list and managed in that case the message
		}
		addValueString(list);
		return list;
	}

	public static List<ParkingLog> getStoricoPark(Parking obj) {
		String request = null;
		List<ParkingLog> list = null;
		try {
			request = RemoteConnector.getJSON(Parcheggi_Services.HOST, mContext
					.getResources().getString(R.string.park)
					+ mContext.getResources().getString(R.string.applocation)
					+ Parcheggi_Services.PARKLOGLIST
					+ "?"
					+ "type=parking"
					+ Parcheggi_Services.LOGELEMENTS + "&id=" + obj.getId(),
					mContext.getResources().getString(R.string.token));
			list = JsonUtils.toObjectList(request, ParkingLog.class);
			// convert valueString to value
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addValueStringPark(list);
		return list;
	}

	public static List<StreetLog> getStoricoStreet(Street obj) {
		String request = null;
		List<StreetLog> list = null;
		try {
			request = RemoteConnector.getJSON(Parcheggi_Services.HOST, mContext
					.getResources().getString(R.string.park)
					+ mContext.getResources().getString(R.string.applocation)
					+ Parcheggi_Services.STREETLOGLIST
					+ "?"
					+ "type=parking"
					+ Parcheggi_Services.STREETLOGLIST + "&id=" + obj.getId(),
					mContext.getResources().getString(R.string.token));
			list = JsonUtils.toObjectList(request, StreetLog.class);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addValueStringStreet(list);
		return list;

	}

	public static List<LogObject> getStoricoAgente() {
		String request = null;
		try {
			request = RemoteConnector.getJSON(Parcheggi_Services.HOST, mContext
					.getResources().getString(R.string.park)
					+ mContext.getResources().getString(R.string.applocation)
					+ Parcheggi_Services.AUSLOGLIST
					+ "?author="
					+ new AusiliariHelper(mContext).getUsername()
					+ Parcheggi_Services.LOGELEMENTS, mContext.getResources()
					.getString(R.string.token));

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// convert valueString into value
		List<LogObject> list = JsonUtils.toObjectList(request, LogObject.class);
		addValueString(list);
		return list;

	}

	private static String cleanStringForJSON(String value) {
		String corrected = "";
		if (value != null) {
			// corrected = value.replaceAll("\n\t|NumberLong|(|)", "");
			value = value.replaceAll("\n", "");
			value = value.replaceAll("\t", "");
			value = value.replaceAll("\\(", "");
			value = value.replaceAll("\\)", "");
			value = value.replaceAll("NumberLong", "");
		}
		return value;
	}

	private static void addValueStringPark(List<ParkingLog> list) {
		if (list != null) {
			for (ParkingLog map : list) {
				if (map.getValueString() != null) {
					Parking park = new Parking();
					Map tmpValue = JsonUtils
							.toObject(cleanStringForJSON(map.getValueString()),
									Map.class);
					map.setValue(JsonUtils.toObject(
							cleanStringForJSON(map.getValueString()), Map.class));
				}
			}
		}

	}

	private static void addValueStringStreet(List<StreetLog> list) {
		if (list != null) {
			for (StreetLog map : list) {
				if (map.getValueString() != null) {
					Street park = new Street();
					Map tmpValue = JsonUtils
							.toObject(cleanStringForJSON(map.getValueString()),
									Map.class);
					map.setValue(JsonUtils.toObject(
							cleanStringForJSON(map.getValueString()), Map.class));
				}
			}
		}

	}

	private static void addValueString(List<LogObject> list) {
		if (list != null) {
			for (LogObject map : list) {
				String value = cleanStringForJSON((String) map.getValueString());
				if (value != null) {
					if (map.getType().contains("Parking")) {
						Parking park = new Parking();
						Map tmpValue = JsonUtils.toObject(
								cleanStringForJSON(map.getValueString()),
								Map.class);
						map.setValue(JsonUtils.toObject(
								cleanStringForJSON(map.getValueString()),
								Map.class));
					} else {
						Street park = new Street();
						Map tmpValue = JsonUtils.toObject(
								cleanStringForJSON(map.getValueString()),
								Map.class);
						map.setValue(JsonUtils.toObject(
								cleanStringForJSON(map.getValueString()),
								Map.class));
					}
					// map.put("value", JsonUtils.toObjectList(value,
					// Map.class));
				}
			}
		}

	}

	public static void getStoricoProcessor(
			UpdateStoricoAgenteInterface updateStoricoAgenteInterface,
			Activity activity) {
		GetStoricoAgenteProcessor asa = new GetStoricoAgenteProcessor(activity);
		asa.execute(updateStoricoAgenteInterface);
	}

	public static List<Street> getStreetlist() {
		String request = null;
		List<Street> array = null;

		try {
			request = RemoteConnector.getJSON(Parcheggi_Services.HOST, mContext
					.getResources().getString(R.string.park)
					+ mContext.getResources().getString(R.string.applocation)
					+ Parcheggi_Services.STREETLIST, mContext.getResources()
					.getString(R.string.token));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (request != null) {
			array = JsonUtils.toObjectList(request, Street.class);
			Collections.sort(array, new Comparator<Street>() {
				public int compare(Street obj1, Street obj2) {
					return obj1.getName().compareTo(obj2.getName());
				}
			});
		}
		return array;
	}

	public static List<Parking> getParklist() {
		String request = null;
		try {
			request = RemoteConnector.getJSON(Parcheggi_Services.HOST, mContext
					.getResources().getString(R.string.park)
					+ mContext.getResources().getString(R.string.applocation)
					+ Parcheggi_Services.PARKLIST, mContext.getResources()
					.getString(R.string.token));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JsonUtils.toObjectList(request, Parking.class);
	}

	public static void getParklistProcessor(
			UpdateParkListInterface updateParkListInterface, Activity activity) {

		GetParkingListProcessor ast = new GetParkingListProcessor(activity);
		ast.execute(updateParkListInterface);
	}

	public static void getParklistProcessorMap(FragmentActivity activity,
			AddGeoPoints agpInterface) {
		GetParkingMapProcessor ast = new GetParkingMapProcessor(activity);
		ast.execute(agpInterface);
	}

	public static void getStreetProcessor(
			UpdateStreetListInterface updateStreetListInterface,
			Activity activity) {

		GetStreetsListProcessor ast = new GetStreetsListProcessor(activity);
		ast.execute(updateStreetListInterface);
	}

	public static void getStreetProcessorMap(FragmentActivity activity,
			AddGeoPoints agpInterface) {
		GetStreetMapProcessor ast = new GetStreetMapProcessor(activity);
		ast.execute(agpInterface);
	}

	public static String getUsername() {
		return ((Activity) mContext).getSharedPreferences("Login", 0)
				.getString("User", null);
	}

}