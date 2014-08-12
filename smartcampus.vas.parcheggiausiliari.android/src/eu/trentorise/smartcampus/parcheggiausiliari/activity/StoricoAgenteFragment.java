package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class StoricoAgenteFragment extends Fragment {
	ListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_storico, container,
				false);
		lv = (ListView) rootView.findViewById(R.id.listView1);
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		ArrayList<String> result = new ArrayList<String>();
		List<ParkingLog> list = new AusiliariHelper(getActivity())
				.getStoricoAgente();
		if (list != null)
			for (ParkingLog lc : list) {
				result.add("da " + lc.getAuthor() + " il " + lc.getTime());
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

	public static class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> values;

		public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
			super(context, R.layout.rowlayout, values);
			this.context = context;
			this.values = values;
		}

		public MySimpleArrayAdapter(Context context, String[] values) {
			super(context, R.layout.rowlayout, values);
			this.context = context;
			this.values = new ArrayList<String>(Arrays.asList(values));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.txt1);
			textView.setText(values.get(position));

			return rowView;
		}
	}
}
