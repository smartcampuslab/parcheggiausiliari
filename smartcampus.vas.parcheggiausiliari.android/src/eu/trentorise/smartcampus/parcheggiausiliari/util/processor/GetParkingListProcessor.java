package eu.trentorise.smartcampus.parcheggiausiliari.util.processor;

import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateParkListInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class GetParkingListProcessor extends
		AsyncTask<Object, Void, List<Parking>> {
	UpdateParkListInterface updateParkListInterface;
	Activity activity;
	ProgressDialog pd;

	public GetParkingListProcessor(	Activity activity) {
		this.activity=activity;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd = new ProgressDialog(activity);
		pd.setTitle(activity.getString(R.string.dialog_loading));
		pd.setMessage(activity.getString(R.string.dialog_waiting));
		pd.show();
	}



	protected List<Parking> doInBackground(Object... params) {
		// get Parkings
		this.updateParkListInterface = (UpdateParkListInterface) params[0];
		return AusiliariHelper.getParklist();
	}

	protected void onPostExecute(List<Parking> parks) {
		TextView tv = (TextView) activity.findViewById(R.id.parks_empty_text);
		ListView lv = (ListView) activity.findViewById(R.id.listParkings);
		if (parks != null && parks.size() > 0) {
			if (tv != null) {
				lv.setVisibility(View.VISIBLE);
				tv.setVisibility(View.GONE);
			}
			updateParkListInterface.addParkings(parks);
			// adapter.addAll(parks);
			// adapter.notifyDataSetChanged();
			// disable layout message "empty list"

		} else {
			// enable layout message "empty list"
			if (tv != null) {
				lv.setVisibility(View.GONE);
				tv.setVisibility(View.VISIBLE);
			}
		}
		if (pd.isShowing())
			pd.dismiss();
	}
}
