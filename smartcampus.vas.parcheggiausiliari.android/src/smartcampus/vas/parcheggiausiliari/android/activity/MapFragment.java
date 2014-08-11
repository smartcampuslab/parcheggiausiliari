package smartcampus.vas.parcheggiausiliari.android.activity;

import java.util.ArrayList;

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
import smartcampus.vas.parcheggiausiliari.android.model.Parking;
import smartcampus.vas.parcheggiausiliari.android.util.AusiliariHelper;
import smartcampus.vas.parcheggiausiliari.android.util.GPSTracker;
import smartcampus.vas.parcheggiausiliari.android.util.MyPolyline;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

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
	//	btnParkings.setBackgroundColor(getResources().getColor(R.color.button_normal));
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
	//	btnStreets.setBackgroundColor(getResources().getColor(R.color.button_normal));
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
		
		
		/*MyPolyline a = new MyPolyline(getActivity()) ;
		ArrayList<GeoPoint> list = new ArrayList<GeoPoint>();
		list.add(new GeoPoint(
				new AusiliariHelper(getActivity()).getParklist()[0]
						.getPosition()[0], new AusiliariHelper(getActivity())
						.getParklist()[0].getPosition()[1]));
		list.add(new GeoPoint(46.07, 11.15));
		list.add(new GeoPoint(46.078, 11.15));
		list.add(new GeoPoint(46.07, 11.154));
		a.setPoints(list);
		map.getOverlays().add(a);
		map.invalidate();*/
		debugLinee(map);
		return rootView;
	}

	protected void showPopup(View anchorView, ParkingMarker arg1) {
		DialogFragment df = PopupFragment.newInstance(arg1.getmParking(),
				arg1.getSnippet());
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

	
	
	private void debugLinee(MapView mv){
		ArrayList<GeoPoint> a = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> b = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> c = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> d = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> e = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> f = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> g = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> h = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> i = new ArrayList<GeoPoint>();
		ArrayList<GeoPoint> j = new ArrayList<GeoPoint>();
				
		
		a.add(new GeoPoint(46.091802, 11.115442));
		a.add(new GeoPoint(46.085313, 11.118746));
		b.add(new GeoPoint(46.083468, 11.122695));
		b.add(new GeoPoint(46.085194, 11.122780));
		b.add(new GeoPoint(46.086385, 11.121622));
		c.add(new GeoPoint(46.086385, 11.121622));
		c.add(new GeoPoint(46.086147, 11.121064));
		d.add(new GeoPoint(46.087546, 11.124969));
		d.add(new GeoPoint(	46.085581, 11.125141));
		d.add(new GeoPoint(46.080819, 11.126342));
		e.add(new GeoPoint(	46.092844, 11.114541));
		e.add(new GeoPoint(46.092606, 11.112824));
		e.add(new GeoPoint(46.091445, 11.113167));
		f.add(new GeoPoint(46.094123, 11.120635));
		f.add(new GeoPoint(46.093915, 11.117931));
		f.add(new GeoPoint(46.092457, 11.118746));
		g.add(new GeoPoint(46.087510, 11.124942));
		g.add(new GeoPoint(46.091365, 11.122239));
		g.add(new GeoPoint(46.094906, 11.121359));
		
		h.add(new GeoPoint(46.095546, 11.120114));
		h.add(new GeoPoint(46.094088, 11.120715));
		h.add(new GeoPoint(46.091127, 11.120758));
		
		i.add(new GeoPoint(46.092912, 11.120694));
		i.add(new GeoPoint(46.092034, 11.117110));
		i.add(new GeoPoint(46.090517, 11.118183));
		
		j.add(new GeoPoint(46.094862, 11.110373));
		j.add(new GeoPoint(46.094549, 11.109638));
		j.add(new GeoPoint(46.092831, 11.110845));
		j.add(new GeoPoint(46.092942, 11.111327));
		j.add(new GeoPoint(46.092392, 11.111644));
		j.add(new GeoPoint(46.092332, 11.111258));
		j.add(new GeoPoint(46.090116, 11.113147));
		
		MyPolyline pa = new MyPolyline(getActivity());
		pa.setPoints(a);
		
		MyPolyline pb = new MyPolyline(getActivity());
		pb.setPoints(b);
		
		MyPolyline pc = new MyPolyline(getActivity());
		pc.setPoints(c);
		
		MyPolyline pd = new MyPolyline(getActivity());
		pd.setPoints(d);
		
		MyPolyline pe = new MyPolyline(getActivity());
		pe.setPoints(e);
		
		MyPolyline pf = new MyPolyline(getActivity());
		pf.setPoints(f);
		
		MyPolyline pg = new MyPolyline(getActivity());
		pg.setPoints(g);
		
		MyPolyline ph = new MyPolyline(getActivity());
		ph.setPoints(h);
		
		MyPolyline pi = new MyPolyline(getActivity());
		pi.setPoints(i);
		
		MyPolyline pj = new MyPolyline(getActivity());
		pj.setPoints(j);		
		
		MyPolyline pk = MyPolyline.decode(getActivity(),"qshxGatwbAqZdXw`@f^VaBtRsQxFwGzDmP|@eDxBcAr@nAg@lB}BHqDkCqCoJkDyN" );
		
		FolderOverlay fo = new FolderOverlay(mv.getContext());
		fo.add(pa);
		fo.add(pb);
		fo.add(pc);
		fo.add(pd);
		fo.add(pe);
		fo.add(pf);
		fo.add(pg);
		fo.add(ph);
		fo.add(pi);
		fo.add(pj);
		fo.add(pk);
		mv.getOverlays().add(fo);
		
		
	}
}
