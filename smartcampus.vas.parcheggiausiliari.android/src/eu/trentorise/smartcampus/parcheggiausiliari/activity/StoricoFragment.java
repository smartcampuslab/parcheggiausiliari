package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.osmdroid.bonuspack.cachemanager.CacheManager;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.model.StreetLog;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class StoricoFragment extends Fragment {
	private ListView lv;
	private GeoObject obj;
	private TextView tv;

	public StoricoFragment(GeoObject obj) {
		this.obj = obj;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_storico, container,
				false);
		lv = (ListView) rootView.findViewById(R.id.listView1);
		tv = (TextView) rootView.findViewById(R.id.txtNoData);
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		ArrayList<LogObject> result = new ArrayList<LogObject>();
		if (Parking.class.isInstance(obj)) {
			List<ParkingLog> list = new AusiliariHelper(getActivity())
					.getStoricoPark((Parking) obj);
			if (!list.isEmpty()) {
				tv.setVisibility(View.GONE);
				for (ParkingLog p : list) {
					result.add(p);
				}
			}
		} else {
			List<StreetLog> list = new AusiliariHelper(getActivity())
					.getStoricoStreet((Street) obj);
			if (!list.isEmpty()) {
				tv.setVisibility(View.GONE);
				for (StreetLog p : list) {
					result.add(p);
				}
			}
		}

		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getActivity(),
				result);
		lv.setAdapter(adapter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public static class MySimpleArrayAdapter extends
			ArrayAdapter<LogObject> {
		private final Context context;
		private final ArrayList<LogObject> values;

		public MySimpleArrayAdapter(Context context, ArrayList<LogObject> values) {
			super(context, R.layout.storicorow,values);
			this.context = context;
			this.values = values;
		}

		public MySimpleArrayAdapter(Context context, LogObject[] values) {
			super(context, R.layout.storicorow,values);
			this.context = context;
			this.values = new ArrayList<LogObject>(Arrays.asList(values));
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
			Date d = new Date(values.get(position).getTime());
			textView.setText(values.get(position).getAuthor()+" - ore "+String.format("%02d", d.getHours())+":"+String.format("%02d", d.getMinutes())+" - " +String.format("%02d", d.getDate())+"/"+String.format("%02d", (d.getMonth()+1))+"/"+(d.getYear()+1900));
			if(values.get(position) instanceof ParkingLog)
			{
				rowView.findViewById(R.id.txtStreet).setVisibility(View.GONE);
				Parking p = (Parking) values.get(position).getValue();
				valFree.setText(""+p.getSlotsOccupiedOnTotal());
				valWork.setText(""+p.getSlotsUnavailable());
			}
			else{
				valPay 	= (TextView) rowView.findViewById(R.id.valuePay);
				valTime = (TextView) rowView.findViewById(R.id.valueTime);
				Street s = (Street) values.get(position).getValue();
				valFree.setText(""+s.getSlotsOccupiedOnFree());
				valWork.setText(""+s.getSlotsUnavailable());
				valTime.setText(""+s.getSlotsOccupiedOnTimed());
				valPay.setText(""+s.getSlotsOccupiedOnPaying());
			}

			return rowView;
		}
	}
}
