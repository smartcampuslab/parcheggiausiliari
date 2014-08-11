package smartcampus.vas.parcheggiausiliari.android.activity;

import java.util.ArrayList;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import smartcampus.vas.parcheggiausiliari.android.R;
import smartcampus.vas.parcheggiausiliari.android.model.Parking;
import smartcampus.vas.parcheggiausiliari.android.util.AusiliariHelper;
import smartcampus.vas.parcheggiausiliari.android.util.GPSTracker;
import smartcampus.vas.parcheggiausiliari.android.util.LongPressOverlay;
import smartcampus.vas.parcheggiausiliari.android.util.OSMGeocoding;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MapFragment extends Fragment {

	private MapView map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);
		Button btnParkings = (Button) rootView.findViewById(R.id.btnParking);
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

		Button btnStreets = (Button) rootView.findViewById(R.id.btnVie);
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
		map.setMinZoomLevel(3);
		MyLocationNewOverlay myLoc = new MyLocationNewOverlay(getActivity(),
				new CustomLocationProvider(getActivity()), map);
		myLoc.enableMyLocation();
		GPSTracker pos = new GPSTracker(getActivity());
		map.getController().setZoom(18);
		center(new GeoPoint(pos.getLatitude(), pos.getLongitude()));
		// map.addMarkerOnLongClick();

		/*map.getOverlays().add(new LongPressOverlay(getActivity()) {
			@Override
			public void onLongPressGesture(MotionEvent event) {
				Log.e("", "ASDF");
				IGeoPoint point = map.getProjection().fromPixels((int)event.getX(),
						(int)event.getY());
				Toast.makeText(getActivity(),OSMGeocoding
						.FromPointToAddress(
								new GeoPoint(point.getLatitudeE6(),
										point.getLongitudeE6()),
								getActivity()).get(0).getAddressLine(0) , Toast.LENGTH_LONG).show();
				showPopup(
						map,
						"VIA",
						OSMGeocoding
								.FromPointToAddress(
										new GeoPoint(point.getLatitudeE6(),
												point.getLongitudeE6()),
										getActivity()).get(0).getAddressLine(0));
			}
		});*/

		ArrayList<ParkingMarker> items = new ArrayList<ParkingMarker>();
		for (Parking mPark : new AusiliariHelper(getActivity()).getParklist()) {
			ParkingMarker item = new ParkingMarker(
					"14 / 07 / 2014 alle 17.16 \n da Mario Rossi", mPark);
			item.setMarker(getResources().getDrawable(
					R.drawable.marker_poi_generic));
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
		/*PathOverlay a = new PathOverlay(Color.RED, getActivity()){
			@Override
			public boolean onLongPress(MotionEvent e, MapView mapView) {
				// TODO Auto-generated method stub
				Log.d("DEBUG","Pressed road");
				return super.onLongPress(e, mapView);
			}
		};
		a.addPoint(new GeoPoint(new AusiliariHelper(getActivity()).getParklist()[0].getPosition()[0],new AusiliariHelper(getActivity()).getParklist()[0].getPosition()[1]));
		a.addPoint(new GeoPoint(46.07,11.15));*/
		Polyline a = new Polyline(getActivity()){
			@Override
			public boolean onLongPress(MotionEvent e, MapView mapView) {
				// TODO Auto-generated method stub
				Log.d("DEBUG","Path Pressed");
				Toast.makeText(getActivity(), "PATH PRESSED", 1).show();
				return super.onLongPress(e, mapView);
			}
		};
		ArrayList<GeoPoint> list = new ArrayList<GeoPoint>();
		list.add(new GeoPoint(new AusiliariHelper(getActivity()).getParklist()[0].getPosition()[0],new AusiliariHelper(getActivity()).getParklist()[0].getPosition()[1]));
		list.add(new GeoPoint(46.07,11.15));
		list.add(new GeoPoint(46.078,11.15));
		list.add(new GeoPoint(46.07,11.154));
		a.setPoints(list);
		map.getOverlays().add(a);
		
		
		return rootView;
	}

	protected void showPopup(View anchorView, ParkingMarker arg1) {
		DialogFragment df = PopupFragment.newInstance(arg1.getmParking(),arg1.getSnippet());
		df.show(getFragmentManager(), getTag());
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void center(GeoPoint geopoint) {
		map.getController().animateTo(geopoint);
	}

	private class CustomLocationProvider extends GpsMyLocationProvider {

		public CustomLocationProvider(Context context) {
			super(context);
		}

	}

	private class ParkingMarker extends OverlayItem {

		private Parking mParking;

		public ParkingMarker(String aSnippet, Parking parking) {
			super(parking.getName(), aSnippet, new GeoPoint(
					parking.getPosition()[0], parking.getPosition()[1]));
			mParking = parking;
		}

		public Parking getmParking() {
			return mParking;
		}
	}

}
