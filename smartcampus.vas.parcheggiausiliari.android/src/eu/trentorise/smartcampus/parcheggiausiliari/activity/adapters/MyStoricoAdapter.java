package eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObjectExtended;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;

/**
 * Adapter used for populating the {@link ListView} by converting the {@link Map} recieved (because of the mixed types of Data) into a {@link Parking} or a {@link Street}
 * @author Michele Armellini
 *
 */
public class MyStoricoAdapter extends ArrayAdapter<LogObject> {
	private final Context context;
	private final List<LogObjectExtended> values = new ArrayList<LogObjectExtended>();

	public MyStoricoAdapter(Context context, List<LogObject> values) {
		super(context, R.layout.storicorow, values);
		this.context = context;
		for (LogObject l : values) {
			LogObjectExtended le = new LogObjectExtended();
			if (l.getType().contains("Parking")) {
				le.setParkingData(LogObjectExtended.mapToPark(l.getValue()));
				le.extractOldValues("park");
			} else {
				le.setStreetData(LogObjectExtended.mapToStreet(l.getValue()));
				le.extractOldValues("street");
			}
			
			this.values.add(le);
		}
		//this.values = values;
	}
//public void updateValues(List<LogObject> values){
//	
//}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.storicorow, parent, false);
		TextView textView = (TextView) rowView
				.findViewById(R.id.storicotitle);
		TextView valFree = (TextView) rowView.findViewById(R.id.valueFree);
		TextView valWork = (TextView) rowView.findViewById(R.id.valueWork);
		TextView valPay;
		TextView valTime;
		if (!values.isEmpty()) {
			if (values.get(position).getParkingData() != null) {
				rowView.findViewById(R.id.txtStreet).setVisibility(View.GONE);
				LogObjectExtended p = (LogObjectExtended) values.get(position);
				Date d = new Date(p.getParkingData().getUpdateTime());
				textView.setText(((p.getParkingData().getChannel()==1) ? p.getParkingData().getAuthor(): context.getString(R.string.system_author))+ " - ore "
						
						+ String.format("%02d", d.getHours()) + ":"
						+ String.format("%02d", d.getMinutes()) + " - "
						+ String.format("%02d", d.getDate()) + "/"
						+ String.format("%02d", (d.getMonth() + 1)) + "/"
						+ (d.getYear() + 1900));
				 valFree.setText("" + p.getSlotsOccupiedOnTotal());
				 valWork.setText("" + p.getSlotsUnavailable());
			} else {
				valPay = (TextView) rowView.findViewById(R.id.valuePay);
				valTime = (TextView) rowView.findViewById(R.id.valueTime);
				LogObjectExtended s = (LogObjectExtended) values.get(position);
				Date d = new Date(s.getStreetData().getUpdateTime());
				textView.setText(((s.getStreetData().getChannel()==1) ? s.getStreetData().getAuthor(): context.getString(R.string.system_author))+  " - ore "
						+ String.format("%02d", d.getHours()) + ":"
						+ String.format("%02d", d.getMinutes()) + " - "
						+ String.format("%02d", d.getDate()) + "/"
						+ String.format("%02d", (d.getMonth() + 1)) + "/"
						+ (d.getYear() + 1900));
				 valFree.setText("" + s.getSlotsOccupiedOnFree());
				 valWork.setText("" + s.getSlotsUnavailable());
				 valTime.setText("" + s.getSlotsOccupiedOnTimed());
				 valPay.setText("" + s.getSlotsOccupiedOnPaying());
			}
//		Date d = new Date((Long) values.get(position).get("time"));
//		textView.setText(((Map) values.get(position).get("value"))
//				.get("name")
//				+ " - ore "
//				+ String.format("%02d", d.getHours())
//				+ ":"
//				+ String.format("%02d", d.getMinutes())
//				+ " - "
//				+ String.format("%02d", d.getDate())
//				+ "/"
//				+ String.format("%02d", (d.getMonth() + 1))
//				+ "/"
//				+ (d.getYear() + 1900));
//
//		if (((Map) values.get(position).get("value"))
//				.containsKey("slotsTotal")) {
//			rowView.findViewById(R.id.txtStreet).setVisibility(View.GONE);
//			Parking p = populateParking((Map) values.get(position).get(
//					"value"));
////TODO		calculate using the array of slotconfigurations
//			//valFree.setText("" + p.getSlotsTotal()- 5);
//			//valWork.setText("" + p.getSlotsUnavailable());
//		} else {
//			rowView.findViewById(R.id.txtStreet)
//					.setVisibility(View.VISIBLE);
//			valPay = (TextView) rowView.findViewById(R.id.valuePay);
//			valTime = (TextView) rowView.findViewById(R.id.valueTime);
//			Street s = populateStreet((Map) values.get(position).get(
//					"value"));
//			//TODO		calculate using the array of slotconfigurations
//
////			valFree.setText("" + s.getSlotsOccupiedOnFree());
////			valWork.setText("" + s.getSlotsUnavailable());
////			valTime.setText("" + s.getSlotsOccupiedOnTimed());
////			valPay.setText("" + s.getSlotsOccupiedOnPaying());
//		}
		}
		return rowView;
	}

	/**
	 * Method to convert a {@link Map} into a {@link Parking}
	 * @param m map to convert
	 * @return the Parking with values obtained from the map
	 */
	public Parking populateParking(Map m) {
		Parking p = new Parking();
		p.setSlotsTotal((Integer) m.get("slotsTotal"));
		//TODO		calculate using the array of slotconfigurations
//		p.setSlotsOccupiedOnTotal((Integer) m.get("slotsOccupiedOnTotal"));
//		p.setSlotsUnavailable((Integer) m.get("slotsUnavailable"));
		return p;
	}

	/**
	 * Method to convert a {@link Map} into a {@link Street}
	 * @param m map to convert
	 * @return the Parking with values obtained from the map
	 */
	public Street populateStreet(Map m) {
		Street s = new Street();
		//TODO		calculate using the array of slotconfigurations

//		s.setSlotsFree((Integer) m.get("slotsFree"));
//		s.setSlotsOccupiedOnFree((Integer) m.get("slotsOccupiedOnFree"));
//		s.setSlotsUnavailable((Integer) m.get("slotsUnavailable"));
//		s.setSlotsOccupiedOnPaying((Integer) m.get("slotsOccupiedOnPaying"));
//		s.setSlotsPaying((Integer) m.get("slotsPaying"));
//		s.setSlotsOccupiedOnTimed((Integer) m.get("slotsOccupiedOnTimed"));
//		s.setSlotsTimed((Integer) m.get("slotsTimed"));
		return s;
	}
}