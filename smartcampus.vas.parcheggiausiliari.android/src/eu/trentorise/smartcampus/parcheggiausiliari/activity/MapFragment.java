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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;
import eu.trentorise.smartcampus.parcheggiausiliari.util.GPSTracker;
import eu.trentorise.smartcampus.parcheggiausiliari.util.MyPolyline;
import eu.trentorise.smartcampus.parcheggiausiliari.util.SinglePopup;

public class MapFragment extends Fragment implements SinglePopup {

	private boolean opened = false;
	private MapView map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
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
		for (Parking mPark : new AusiliariHelper(getActivity()).getParklist()) {
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

		addLinee(new AusiliariHelper(getActivity()).getStreetlist());
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
}
