package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters.MyStoricoAdapter;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters.StoricoAdapter;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoAgenteInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class StoricoAgenteFragment extends Fragment implements
		UpdateStoricoAgenteInterface {
	private ListView lv;
	private TextView tv;
	private List<LogObject> storico = new ArrayList<LogObject>();
	private MyStoricoAdapter myStoricoAdapter;

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
//		myStoricoAdapter = new MyStoricoAdapter(getActivity(), storico);
//		lv.setAdapter(myStoricoAdapter);
//
//		//lv.setAdapter(myStoricoAdapter);
//		AusiliariHelper.getStoricoProcessor(this, getActivity());
		myStoricoAdapter = new MyStoricoAdapter(getActivity(), storico);
		lv.setAdapter(myStoricoAdapter);
		AusiliariHelper.getStoricoProcessor(this, getActivity());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
@Override
public void refreshLog() {
	refreshLogList();
	
}
	@Override
	public void addStorico(List<LogObject> storico) {
		//this.storico = storico;
//		myStoricoAdapter = new MyStoricoAdapter(getActivity(), storico);
		for (LogObject p : storico) {
			this.storico.add(p);
		}
		myStoricoAdapter.clear();
		myStoricoAdapter.addAll(storico);
		myStoricoAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		// Make sure that we are currently visible
		if (this.isVisible()) {
			refreshLogList();
		}
	}
	
	private void refreshLogList() {

		myStoricoAdapter = new MyStoricoAdapter(getActivity(), storico);
		lv.setAdapter(myStoricoAdapter);

		
	}
}
