package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters.GeoObjectAdapter;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;
import eu.trentorise.smartcampus.parcheggiausiliari.util.GPSTracker;
import eu.trentorise.smartcampus.parcheggiausiliari.util.MyPolyline;
import eu.trentorise.smartcampus.parcheggiausiliari.util.SinglePopup;
import eu.trentorise.smartcampus.parcheggiausiliari.views.ClearableAutoCompleteTextView;
import eu.trentorise.smartcampus.parcheggiausiliari.views.ClearableAutoCompleteTextView.OnClearListener;
//import android.widget.Filter.FilterResults;

/**
 * Fragment containing the map. It implements {@link SinglePopup} to make sure
 * only one {@link PopupFragment popup} will show
 * 
 * @author Michele Armellini
 * 
 */
public class MapFragment extends Fragment implements SinglePopup, AddGeoPoints {

	private boolean opened = false;
	private MapView map;
	private List<GeoObject> list = new ArrayList<GeoObject>();
	private ImageView searchIcon;
	private ClearableAutoCompleteTextView searchBox;
	private View v;
	private List<Street> streets;
	private List<Parking> parks;
	private View vNew;
	private GeoObjectAdapter streetFiltered;
	
	@Override
	public void onStart() {
		super.onStart();
		/*
		 *  ***Setting up the custom layout of the actionbar and implementing
		 * search functionality***
		 */

		setupActionBar();

		/* ***Populating view*** */

		Button btnParkings = (Button) getActivity().findViewById(R.id.btnParking);
		Button btnStreets = (Button) getActivity().findViewById(R.id.btnVie);
		btnStreets.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.enter, R.anim.exit);
				ft.replace(R.id.container, new StreetListFragment(),
						getString(R.string.streetlist_fragment))
						.addToBackStack(null).commit();
			}
		});

		/* ***Map Settings*** */
		map = (MapView) getActivity().findViewById(R.id.mapview);
		map.setTileSource(TileSourceFactory.MAPQUESTOSM);
		map.setMultiTouchControls(true);
		map.setMinZoomLevel(10);
		MyLocationNewOverlay myLoc = new MyLocationNewOverlay(getActivity(),
				new CustomLocationProvider(getActivity()), map);
		myLoc.enableMyLocation();
		map.getOverlays().add(myLoc);
		map.getController().setZoom(Integer.valueOf(getResources().getString((R.string.zoom_level))));
		String center = getResources().getString(R.string.appcenter);
		String[] coords = center.split(",");
		map.getController().animateTo(new GeoPoint(Double.parseDouble(coords[0]),Double.parseDouble(coords[1])));


		

		/* ***Setting up "go to my location" button*** */

		Button myLocButton = (Button) getActivity().findViewById(R.id.btMyLocation);
		myLocButton.setBackgroundResource(R.drawable.ic_menu_mylocation);
		myLocButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				GPSTracker pos = new GPSTracker(getActivity());
				map.getController().animateTo(
						new GeoPoint(pos.getLatitude(), pos.getLongitude()));
				map.getController().setZoom(18);
				map.getController().animateTo(
						new GeoPoint(pos.getLatitude(), pos.getLongitude()));
			}
		});

		btnParkings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.enter, R.anim.exit);
				ft.replace(R.id.container, new ParkListFragment(),
						getString(R.string.parklist_fragment))
						.addToBackStack(null).commit();
			}
		});
//		addLinee(streets);
	}
	private void setupActionBar() {
		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();
		v = actionBar.getCustomView();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_HOME_AS_UP);
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vNew = inflater.inflate(R.layout.actionbar_search, null);
		searchIcon = (ImageView) vNew.findViewById(R.id.search_icon);
		searchBox = (ClearableAutoCompleteTextView) vNew
				.findViewById(R.id.search_box);
		searchBox.setVisibility(View.GONE);

		/* Non static calls are needed in order for the Helper to have the context and show the ProgressDialog correctly */
		AusiliariHelper.getParklistProcessorMap(getActivity(), this);
		AusiliariHelper.getStreetProcessorMap(getActivity(), this);

		searchIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleSearch(false);
			}
		});

	
		actionBar.setCustomView(vNew);
		
		searchIcon.setVisibility(View.VISIBLE);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	/**
	 * method to toggle between showing the searchBar and the search icon
	 * 
	 * @param reset
	 *            true -> searchBar visible , false -> searchBar not visible
	 */
	private void toggleSearch(boolean reset) {
		if (reset) {
			searchBox.setText("");
			searchBox.setVisibility(View.GONE);
			searchIcon.setVisibility(View.VISIBLE);
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
		} else {
			searchIcon.setVisibility(View.GONE);
			searchBox.setVisibility(View.VISIBLE);
			searchBox.requestFocus();
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);
		
		return rootView;
	}

	protected void showPopup(View anchorView, ParkingMarker arg1) {
		DialogFragment df = new PopupFragment(arg1.getmParking());
		df.show(getFragmentManager(), getTag());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private class CustomLocationProvider extends GpsMyLocationProvider {
		public CustomLocationProvider(Context context) {
			super(context);
		}

	}

	private class ParkingMarker extends OverlayItem {
		private Parking mParking;

		public ParkingMarker(Parking parking) {
			super(parking.getName(), "", new GeoPoint(parking.getPosition()[0],
					parking.getPosition()[1]));
			mParking = parking;
		}

		public Parking getmParking() {
			return mParking;
		}
	}

	/**
	 * method which adds polylines to the map by decoding them from every
	 * {@link Street} object passed
	 * 
	 * @param data
	 *            List of {@link Street} to decode from
	 */
	private void addLinee(List<Street> data) {
		FolderOverlay fo = new FolderOverlay(map.getContext());
		for (Street street : data) {
			MyPolyline mp = MyPolyline.decode(getActivity(),
					street.getPolyline(), street, this);
			fo.add(mp);
		}
		list.clear();
		list.addAll(data);

		streetFiltered = new GeoObjectAdapter(getActivity(), list);
		searchBox.setAdapter(streetFiltered);
		streetFiltered.notifyDataSetChanged();
		searchBox.setOnClearListener(new OnClearListener() {

			@Override
			public void onClear() {
				toggleSearch(true);
			}
		});

		searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.enter, R.anim.exit);
				ft.replace(
						R.id.container,
						new DetailsFragment((GeoObject) parent
								.getItemAtPosition(position)), null)
						.addToBackStack(null)// Start the animated transition.
						.commit();
				toggleSearch(true);
				searchIcon.setVisibility(View.GONE);
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
			}

		});
		map.getOverlays().add(fo);
		map.invalidate();
	}

	@Override
	public void openPopup(GeoObject obj) {
		// TODO Auto-generated method stub
		if (!opened) {
			opened = true;
			DialogFragment df = new PopupFragment(obj) {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					opened = false;
					super.onDismiss(dialog);
				}
			};
			df.show(getActivity().getSupportFragmentManager(), "");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();
		actionBar.setCustomView(vNew);
	}

	
	@Override
	public void onPause() {
		super.onPause();
		/* ***Restore "original" actionBar*** */
		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();
		actionBar.setCustomView(v);
	}
	@Override
	public void addgeopoints(List<Parking> parks) {
		ArrayList<ParkingMarker> items = new ArrayList<ParkingMarker>();
		for (Parking mPark : parks) {
			ParkingMarker item = new ParkingMarker(mPark);
			item.setMarker(getResources().getDrawable(
					R.drawable.marker_parcheggio));
			items.add(item);
		}

		map.getOverlays().add(
				new ItemizedOverlayWithFocus<ParkingMarker>(items,
						new OnItemGestureListener<ParkingMarker>() {

							@Override
							public boolean onItemLongPress(int arg0,
									ParkingMarker arg1) {
								return false;
							}

							@Override
							public boolean onItemSingleTapUp(int arg0,
									ParkingMarker arg1) {
								showPopup(map, arg1);
								return true;
							}
						}, map.getResourceProxy()));		
	}
	@Override
	public void addStreets(List<Street> streets) {
		addLinee(streets);
	}

	

	
}
