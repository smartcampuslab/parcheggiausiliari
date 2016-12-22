package eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObjectExtended;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;

public class StoricoAdapter extends ArrayAdapter<LogObject> {
	private final Context context;
	private final List<LogObjectExtended> values = new ArrayList<LogObjectExtended>();

	public StoricoAdapter(Context context, List<LogObject> values) {
		super(context, R.layout.storicorow, values);
		this.context = context;
		for (LogObject l : values) {
			LogObjectExtended le = new LogObjectExtended();
			if (l instanceof ParkingLog) {
				le.setParkingData(LogObjectExtended.mapToPark(l.getValue()));
				le.extractOldValues("park");
			} else {
				le.setStreetData(LogObjectExtended.mapToStreet(l.getValue()));
				le.extractOldValues("street");
			}
			
			this.values.add(le);
		}
		// this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.storicorow, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.storicotitle);
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
		}
		return rowView;
	}
}
