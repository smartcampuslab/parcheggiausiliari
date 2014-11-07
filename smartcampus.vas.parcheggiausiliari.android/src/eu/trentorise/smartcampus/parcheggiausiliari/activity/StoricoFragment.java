package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters.StoricoAdapter;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoParkingInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoStreetInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.model.StreetLog;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class StoricoFragment extends Fragment implements
		UpdateStoricoStreetInterface, UpdateStoricoParkingInterface {
	private ListView lv;
	private GeoObject obj;
	private TextView tv;
	private StoricoAdapter storicoAdapter;
	private List<LogObject> storico = new ArrayList<LogObject>();

	public StoricoFragment(GeoObject obj) {
		this.obj = obj;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		// Make sure that we are currently visible
		if (this.isVisible()) {
			refreshParkingList();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_storico, container,
				false);
		lv = (ListView) rootView.findViewById(R.id.listView1);
		tv = (TextView) rootView.findViewById(R.id.txtNoData);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		refreshParkingList();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void refreshParkingList() {
		// ArrayList<LogObject> result = new ArrayList<LogObject>();
		// if (Parking.class.isInstance(obj)) {
		// List<ParkingLog> list = AusiliariHelper
		// .getStoricoPark((Parking) obj);
		// if (!list.isEmpty()) {
		// tv.setVisibility(View.GONE);
		// for (ParkingLog p : list) {
		// result.add(p);
		// }
		// }
		// } else {
		// List<StreetLog> list = AusiliariHelper
		// .getStoricoStreet((Street) obj);
		// if (!list.isEmpty()) {
		// tv.setVisibility(View.GONE);
		// for (StreetLog p : list) {
		// result.add(p);
		// }
		// }
		// }
		//
		// StoricoAdapter adapter = new StoricoAdapter(getActivity(),
		// result);
		// lv.setAdapter(adapter);
		storicoAdapter = new StoricoAdapter(getActivity(), storico);
		lv.setAdapter(storicoAdapter);
		if (Parking.class.isInstance(obj)) {
			AusiliariHelper.getStoricoParkingProcessor(this, getActivity(),
					(Parking) obj);
		} else if (Street.class.isInstance(obj)) {
			AusiliariHelper.getStoricoStreetProcessor(this, getActivity(),
					(Street) obj);

		}
	}

	@Override
	public void addStoricoStreet(List<StreetLog> storico) {

		tv.setVisibility(View.GONE);
		for (StreetLog p : storico) {
			this.storico.add(p);
		}
		// this.storico = storico;
		storicoAdapter.clear();
		storicoAdapter.addAll(storico);
		storicoAdapter.notifyDataSetChanged();
	}

	@Override
	public void addStoricoPark(List<ParkingLog> storico) {
		tv.setVisibility(View.GONE);
		for (ParkingLog p : storico) {
			this.storico.add(p);
		}
//		this.storico = storico;
		storicoAdapter.clear();
		storicoAdapter.addAll(storico);
		storicoAdapter.notifyDataSetChanged();
	}
}
