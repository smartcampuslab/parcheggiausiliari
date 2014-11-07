package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters.StreetListAdapter;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStreetListInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class StreetListFragment extends Fragment implements UpdateStreetListInterface{

	private ListView list;
	private SearchView mSearchView;
	private List<Street> streets = new ArrayList<Street>();
	private StreetListAdapter streetsAdapter;

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
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				((StreetListAdapter) list.getAdapter()).getFilter()
						.filter(arg0);
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
		return rootView;
	}

	@Override
	public void onStart() {
		
//		super.onStart();
//		parksAdapter = new ParkListAdapter(getActivity(), parks);
//		list = (ListView) getActivity().findViewById(R.id.listParkings);
//		list.setAdapter(parksAdapter);
//		list.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//
//				getActivity()
//						.getSupportFragmentManager()
//						.beginTransaction()
//						.replace(
//								getId(),// R.id.container,
//								new DetailsFragment((GeoObject) list
//										.getAdapter().getItem(arg2)))
//						.addToBackStack(null).commit();
//			}
//		});
//		AusiliariHelper.getParklistProcessor(this, getActivity());
		
		
		
		super.onStart();
		streetsAdapter = new StreetListAdapter(getActivity(), streets);
		list = (ListView) getActivity().findViewById(R.id.listStreets);
		list.setAdapter(streetsAdapter);
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
		AusiliariHelper.getStreetProcessor(this, getActivity());
		//disable layout message "empty list"
//		TextView tv = (TextView) getActivity().findViewById(R.id.streets_empty_text);
//		tv.setVisibility(View.GONE);
//	} else {
//		//enable layout message "empty list"
//		TextView tv = (TextView) getActivity().findViewById(R.id.streets_empty_text);
//		tv.setVisibility(View.VISIBLE);
//	}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void addStreets(List<Street> streets) {
		this.streets = streets;
		streetsAdapter.clear();
		streetsAdapter.addAll(streets);
		streetsAdapter.updateCollections(streets);
		streetsAdapter.notifyDataSetChanged();
		
	}

	
}
