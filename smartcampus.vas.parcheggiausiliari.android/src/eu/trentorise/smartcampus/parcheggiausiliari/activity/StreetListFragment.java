package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.ParkListFragment.MySimpleArrayAdapter;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class StreetListFragment extends Fragment {

	private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_streetlist,
				container, false);
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
								new DetailsFragment((GeoObject) list.getAdapter()
										.getItem(arg2))).addToBackStack(null)
						.commit();
				//Log.d("DEBUG", "Passed");
			}
		});
		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
