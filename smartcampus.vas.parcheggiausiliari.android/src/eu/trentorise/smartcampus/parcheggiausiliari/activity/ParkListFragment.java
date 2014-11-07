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
import eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters.ParkListAdapter;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateParkListInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class ParkListFragment extends Fragment implements
		UpdateParkListInterface {

	private ListView list;
	private SearchView mSearchView;
	private ParkListAdapter parksAdapter;
	private List<Parking> parks = new ArrayList<Parking>();

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
				((ParkListAdapter) list.getAdapter()).getFilter().filter(arg0);
				return true;
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_parklist, container,
				false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		parksAdapter = new ParkListAdapter(getActivity(), parks);
		list = (ListView) getActivity().findViewById(R.id.listParkings);
		list.setAdapter(parksAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(
								getId(),// R.id.container,
								new DetailsFragment((GeoObject) list
										.getAdapter().getItem(arg2)))
						.addToBackStack(null).commit();
			}
		});
		AusiliariHelper.getParklistProcessor(this, getActivity());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void addParkings(List<Parking> parks) {
		this.parks = parks;
		parksAdapter.clear();
		parksAdapter.addAll(parks);
		parksAdapter.updateCollections(parks);
		parksAdapter.notifyDataSetChanged();

	}

}
