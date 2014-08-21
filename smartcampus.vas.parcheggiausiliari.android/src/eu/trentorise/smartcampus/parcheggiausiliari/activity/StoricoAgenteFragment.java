package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import eu.trentorise.smartcampus.parcheggiausiliari.model.LastChange;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class StoricoAgenteFragment extends Fragment {
	private ListView lv;
	private TextView tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_storico, container,
				false);
		//setRetainInstance(true);
		lv = (ListView) rootView.findViewById(R.id.listView1);
		tv = (TextView) rootView.findViewById(R.id.txtNoData);
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		ArrayList<Map> result = new ArrayList<Map>();
		List<Map> list = new AusiliariHelper(getActivity())
				.getStoricoAgente();
		if (!list.isEmpty()) {
			for (Map lc : list) {
				result.add(lc);
			}
			tv.setVisibility(View.GONE);
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

	public static class MySimpleArrayAdapter extends ArrayAdapter<Map> {
		private final Context context;
		private final ArrayList<Map> values;

		public MySimpleArrayAdapter(Context context, ArrayList<Map> values) {
			super(context, R.layout.storicorow, values);
			this.context = context;
			this.values = values;
		}

		public MySimpleArrayAdapter(Context context, Map[] values) {
			super(context, R.layout.storicorow, values);
			this.context = context;
			this.values = new ArrayList<Map>(Arrays.asList(values));
		}

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
			
			Date d = new Date((Long) values.get(position).get("time"));
			textView.setText(((Map) values.get(position).get("value")).get("name")
					+ " - ore " + String.format("%02d", d.getHours()) + ":"
					+ String.format("%02d", d.getMinutes()) + " - "
					+ String.format("%02d", d.getDate()) + "/"
					+ String.format("%02d", (d.getMonth() + 1)) + "/"
					+ (d.getYear() + 1900));

			if (((Map) values.get(position).get("value")).containsKey("slotsTotal")) {
				rowView.findViewById(R.id.txtStreet).setVisibility(View.GONE);
				Parking p = populateParking((Map) values.get(position).get("value"));
				valFree.setText("" + p.getSlotsOccupiedOnTotal());
				valWork.setText("" + p.getSlotsUnavailable());
			} else {
				rowView.findViewById(R.id.txtStreet).setVisibility(View.VISIBLE);
				valPay = (TextView) rowView.findViewById(R.id.valuePay);
				valTime = (TextView) rowView.findViewById(R.id.valueTime);
				Street s = populateStreet((Map) values.get(position).get("value"));
				valFree.setText("" + s.getSlotsOccupiedOnFree());
				valWork.setText("" + s.getSlotsUnavailable());
				valTime.setText("" + s.getSlotsOccupiedOnTimed());
				valPay.setText("" + s.getSlotsOccupiedOnPaying());
			}
			return rowView;
		}
		public Parking populateParking(Map m){
			Parking p = new Parking();
			p.setSlotsTotal((Integer) m.get("slotsTotal"));
			p.setSlotsOccupiedOnTotal((Integer) m.get("slotsOccupiedOnTotal"));
			p.setName((String) m.get("name"));
			p.setSlotsUnavailable((Integer) m.get("slotsUnavailable"));
			return p;
		}
		
		public Street populateStreet(Map m){
			Street s = new Street();
			s.setSlotsFree((Integer) m.get("slotsFree"));
			s.setSlotsOccupiedOnFree((Integer) m.get("slotsOccupiedOnFree"));
			s.setName((String) m.get("name"));
			s.setSlotsUnavailable((Integer) m.get("slotsUnavailable"));
			s.setSlotsOccupiedOnPaying((Integer) m.get("slotsOccupiedOnPaying"));
			s.setSlotsPaying((Integer) m.get("slotsPaying"));
			s.setSlotsOccupiedOnTimed((Integer) m.get("slotsOccupiedOnTimed"));
			s.setSlotsTimed((Integer) m.get("slotsTimed"));
			return s;
		}
	}
}
