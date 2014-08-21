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
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;
import eu.trentorise.smartcampus.parcheggiausiliari.util.GPSTracker;
import eu.trentorise.smartcampus.parcheggiausiliari.util.MyPolyline;
import eu.trentorise.smartcampus.parcheggiausiliari.util.SinglePopup;
import eu.trentorise.smartcampus.parcheggiausiliari.views.ClearableAutoCompleteTextView;
import eu.trentorise.smartcampus.parcheggiausiliari.views.ClearableAutoCompleteTextView.OnClearListener;

public class MapFragment extends Fragment implements SinglePopup {

	private boolean opened = false;
	private MapView map;
	private List<GeoObject> list = new ArrayList<GeoObject>();
	private ImageView searchIcon;
	private ClearableAutoCompleteTextView searchBox;
	private View v;
	private List<Street> streets;
	private List<Parking> parks;
	private View vNew;

	// ClearableAutoCompleteTextView search;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar(); // you can use ABS or the non-bc
										// ActionBar
		v = actionBar.getCustomView();
		actionBar.setDisplayOptions(
				 ActionBar.DISPLAY_SHOW_CUSTOM |ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_HOME_AS_UP); // what's
													// mainly
													// important
													// here
													// is
													// DISPLAY_SHOW_CUSTOM.
													// the
													// rest
													// is
													// optional
		//LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate the view that we created before
		vNew = inflater.inflate(R.layout.actionbar_search, null);
		// the view that contains the search "magnifier" icon
		searchIcon = (ImageView) vNew.findViewById(R.id.search_icon);
		// the view that contains the new clearable autocomplete text view
		searchBox = (ClearableAutoCompleteTextView) vNew
				.findViewById(R.id.search_box);

		// start with the text view hidden in the action bar
		searchBox.setVisibility(View.GONE);

		streets = new AusiliariHelper(getActivity()).getStreetlist();
		parks = new AusiliariHelper(getActivity()).getParklist();
		list.addAll(parks);
		list.addAll(streets);

		searchBox.setAdapter(new MySimpleArrayAdapter(getActivity(), list));
		searchIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleSearch(false);
			}
		});

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
		actionBar.setCustomView(vNew);
		super.onCreate(savedInstanceState);
	}

	private void toggleSearch(boolean reset) {
		if (reset) {
			// hide search box and show search icon
			searchBox.setText("");
			searchBox.setVisibility(View.GONE);
			//LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			//params.gravity = Gravity.RIGHT;
			//((View)searchBox.getParent()).setLayoutParams(params);
			searchIcon.setVisibility(View.VISIBLE);
			// hide the keyboard
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
		} else {
			// hide search icon and show search box
			searchIcon.setVisibility(View.GONE);
			searchBox.setVisibility(View.VISIBLE);
			//LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			//params.gravity = Gravity.RIGHT;
			//((View)searchBox.getParent()).setLayoutParams(params);
			searchBox.requestFocus();
			// show the keyboard
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		searchIcon.setVisibility(View.VISIBLE);
		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);
		Button btnParkings = (Button) rootView.findViewById(R.id.btnParking);
		Button btnStreets = (Button) rootView.findViewById(R.id.btnVie);
		// btnStreets.setBackgroundColor(getResources().getColor(R.color.button_normal));
		btnStreets.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.enter, R.anim.exit);
				ft.replace(R.id.container, new StreetListFragment(),
						getString(R.string.streetlist_fragment))
						.addToBackStack(null)// Start the animated transition.
						.commit();
			}
		});

		map = (MapView) rootView.findViewById(R.id.mapview);
		map.setTileSource(TileSourceFactory.MAPQUESTOSM);
		map.setMultiTouchControls(true);
		map.setMinZoomLevel(10);
		MyLocationNewOverlay myLoc = new MyLocationNewOverlay(getActivity(),
				new CustomLocationProvider(getActivity()), map);
		myLoc.enableMyLocation();
		map.getController().setZoom(18);
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
								// TODO Auto-generated method stub
								return false;
							}

							@Override
							public boolean onItemSingleTapUp(int arg0,
									ParkingMarker arg1) {
								showPopup(map, arg1);
								return true;
							}
						}, map.getResourceProxy()));
		map.getOverlays().add(myLoc);
		Button myLocButton = (Button) rootView.findViewById(R.id.btMyLocation);
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
		GPSTracker pos = new GPSTracker(getActivity());
		center(new GeoPoint(pos.getLatitude(), pos.getLongitude()));
		center(new GeoPoint(pos.getLatitude(), pos.getLongitude()));
		// btnParkings.setBackgroundColor(getResources().getColor(R.color.button_normal));
		btnParkings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.enter, R.anim.exit);
				ft.replace(R.id.container, new ParkListFragment(),
						getString(R.string.parklist_fragment))
						.addToBackStack(null)
						// Start the animated transition.
						.commit();
			}
		});
		addLinee(streets);
		return rootView;
	}

	protected void showPopup(View anchorView, ParkingMarker arg1) {
		DialogFragment df = new PopupFragment(arg1.getmParking());
		df.show(getFragmentManager(), getTag());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void center(GeoPoint geopoint) {
		map.getController().animateTo(geopoint);
		map.getController().animateTo(geopoint);
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

	private void addLinee(List<Street> data) {
		FolderOverlay fo = new FolderOverlay(map.getContext());
		for (Street street : data) {
			MyPolyline mp = MyPolyline.decode(getActivity(),
					street.getPolyline(), street, this);
			fo.add(mp);
		}

		map.getOverlays().add(fo);
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
		// TODO Auto-generated method stub
		super.onResume();
		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();
		actionBar.setCustomView(vNew);
	}

	@Override
	public void onPause() {
		super.onPause();
		// TODO Auto-generated method stub
		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar(); // you can use ABS or the non-bc
										// ActionBar
		actionBar.setCustomView(v);
	}

	public static class MySimpleArrayAdapter extends ArrayAdapter<GeoObject> {
		private Filter filter;
		private final Context context;
		private List<GeoObject> items = new ArrayList<GeoObject>();
		private List<GeoObject> filtered = new ArrayList<GeoObject>();

		public MySimpleArrayAdapter(Context context, List<GeoObject> values) {
			super(context, R.layout.search_item, values);
			this.context = context;
			this.filtered.addAll(values);
			this.items.addAll(values);
			this.filter = new MyFilter();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater
					.inflate(R.layout.search_item, parent, false);
			TextView textView = (TextView) rowView
					.findViewById(R.id.search_item);
			if (position < filtered.size())
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
				filtered.clear();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					constraint = constraint.toString().toLowerCase();
					ArrayList<GeoObject> filt = new ArrayList<GeoObject>();
					ArrayList<GeoObject> lItems = new ArrayList<GeoObject>();
					synchronized (this) {
						lItems.addAll(items);
					}
					for (int i = 0, l = lItems.size(); i < l; i++) {
						GeoObject m = lItems.get(i);
						if (m.getName().toLowerCase().contains(constraint))
							filt.add(m);
					}
					result.count = filt.size();
					result.values = filt;
				} else {

					filtered.clear();
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
				filtered = (ArrayList<GeoObject>) results.values;
				ArrayList<GeoObject> temp = new ArrayList<GeoObject>();
				temp.addAll(filtered);
				clear();
				for (int i = 0, l = temp.size(); i < l; i++) {
					add(temp.get(i));
				}
				notifyDataSetChanged();
				notifyDataSetInvalidated();
			}

		}
	}
}
