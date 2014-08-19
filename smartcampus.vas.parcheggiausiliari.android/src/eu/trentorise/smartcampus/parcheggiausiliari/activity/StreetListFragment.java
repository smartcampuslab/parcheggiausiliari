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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class StreetListFragment extends Fragment {

	private ListView list;
	private SearchView mSearchView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				((MySimpleArrayAdapter) list.getAdapter()).getFilter().filter(
						arg0);
				return true;
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_streetlist,
				container, false);
		// setRetainInstance(true);
		list = (ListView) rootView.findViewById(R.id.listStreets);
		list.setAdapter(new MySimpleArrayAdapter(getActivity(),
				new AusiliariHelper(getActivity()).getStreetlist()));
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
				// Log.d("DEBUG", "Passed");
			}
		});
		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public static class MySimpleArrayAdapter extends ArrayAdapter<Street> {
		private Filter filter;
		private final Context context;
		private List<Street> items = new ArrayList<Street>();
		private List<Street> filtered = new ArrayList<Street>();

		public MySimpleArrayAdapter(Context context, List<Street> values) {
			super(context, R.layout.rowlayout, values);
			this.context = context;
			this.filtered.addAll(values);
			this.items.addAll(values);
			this.filter = new MyFilter();
		}

		public MySimpleArrayAdapter(Context context, Street[] values) {
			super(context, R.layout.rowlayout, values);
			this.context = context;
			this.filtered = new ArrayList<Street>(Arrays.asList(values));
			this.items.addAll(this.filtered);
			this.filter = new MyFilter();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.txt1);
			textView.setText(filtered.get(position).getName());

			return rowView;
		}

		@Override
		public Filter getFilter() {
			if (filter == null)
				filter = new MyFilter();
			return filter;
		}

		private class MyFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// NOTE: this function is *always* called from a background
				// thread, and
				// not the UI thread.
				filtered.clear();
				filtered.addAll(items);
				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					ArrayList<Street> filt = new ArrayList<Street>();
					ArrayList<Street> lItems = new ArrayList<Street>();
					synchronized (this) {
						lItems.addAll(filtered);
					}
					for (int i = 0, l = lItems.size(); i < l; i++) {
						Street m = lItems.get(i);
						if (m.getName().toLowerCase().contains(constraint))
							filt.add(m);
					}
					result.count = filt.size();
					result.values = filt;
				} else {

					filtered.clear();
					filtered.addAll(items);
					result.count = filtered.size();
					result.values = filtered;
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// NOTE: this function is *always* called from the UI thread.
				filtered = (ArrayList<Street>) results.values;
				ArrayList<Street> temp = new ArrayList<Street>();
				temp.addAll(filtered);
				notifyDataSetChanged();
				clear();
				for (int i = 0, l = temp.size(); i < l; i++) {
					add(temp.get(i));
				}

				notifyDataSetInvalidated();
			}

		}
	}
}
