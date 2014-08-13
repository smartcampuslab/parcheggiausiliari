package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class ParkListFragment extends Fragment {

	private ListView list;
	private SearchView mSearchView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

	    menu.clear();
	    getActivity().getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				if (arg0.length() != 0) {
					 Log.d("DEBUG","--->" + arg0);
					 ((MySimpleArrayAdapter) list.getAdapter()).getFilter().filter(arg0);
						return true;
			        }
			        return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				if (arg0.length() != 0) {
					 Log.d("DEBUG","--->" + arg0);
					 ((MySimpleArrayAdapter) list.getAdapter()).getFilter().filter(arg0);
						return true;
			        }
			        return false;
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_parklist, container,
				false);
		list = (ListView) rootView.findViewById(R.id.listParkings);
		list.setAdapter(new MySimpleArrayAdapter(getActivity(),
				new AusiliariHelper(getActivity()).getParklist()));
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(
								R.id.container,
								new DetailsFragment((GeoObject) list
										.getAdapter().getItem(arg2)))
						.addToBackStack(null).commit();
				Log.d("DEBUG", "Passed");
			}
		});
		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public static class MySimpleArrayAdapter extends ArrayAdapter<Parking> {
		private final Context context;
		private final List<Parking> values;

		public MySimpleArrayAdapter(Context context, List<Parking> values) {
			super(context, R.layout.rowlayout, values);
			this.context = context;
			this.values = values;
		}

		public MySimpleArrayAdapter(Context context, Parking[] values) {
			super(context, R.layout.rowlayout, values);
			this.context = context;
			this.values = new ArrayList<Parking>(Arrays.asList(values));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.txt1);
			textView.setText(values.get(position).getName());

			return rowView;
		}
	}

}
