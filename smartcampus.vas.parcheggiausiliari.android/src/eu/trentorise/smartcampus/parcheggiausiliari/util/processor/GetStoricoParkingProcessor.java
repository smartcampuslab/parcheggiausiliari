package eu.trentorise.smartcampus.parcheggiausiliari.util.processor;

import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoParkingInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class GetStoricoParkingProcessor extends
		AsyncTask<Object, Void, List<ParkingLog>> {
	UpdateStoricoParkingInterface updateStoricoParkingInterface;
	Activity activity;
	Parking park;
	ProgressDialog pd;

	public GetStoricoParkingProcessor(Activity activity) {
		this.activity = activity;
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

	protected List<ParkingLog> doInBackground(Object... params) {
		// get Parkings
		this.updateStoricoParkingInterface = (UpdateStoricoParkingInterface) params[0];
		this.park = (Parking) params[1];
		return AusiliariHelper.getStoricoPark(this.park);
	}

	protected void onPostExecute(List<ParkingLog> storico) {
		TextView tv = (TextView) activity.findViewById(R.id.txtNoData);

		if (storico != null && storico.size() > 0) {
			if (tv != null) {
				tv.setVisibility(View.GONE);
			}
			updateStoricoParkingInterface.addStoricoPark(storico);

		} else {
			// enable layout message "empty list"
			if (tv != null) {
				tv.setVisibility(View.VISIBLE);
			}
		}
		if (pd.isShowing())
			pd.dismiss();
		updateStoricoParkingInterface.refreshLog();
	}
}
