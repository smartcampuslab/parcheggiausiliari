package eu.trentorise.smartcampus.parcheggiausiliari.util.processor;

import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.AddGeoPoints;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class GetParkingMapProcessor extends
		AsyncTask<Object, Void, List<Parking>> {
	AddGeoPoints addGeoPointInterface;
	Activity activity;
	ProgressDialog pd;

	public GetParkingMapProcessor(Activity activity) {
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
		this.addGeoPointInterface = (AddGeoPoints) params[0];
		return AusiliariHelper.getParklist();
	}

	protected void onPostExecute(List<Parking> parks) {
		if (parks != null && parks.size() > 0) {
			addGeoPointInterface.addgeopoints(parks);

		}
		if (pd.isShowing())
			pd.dismiss();
	}
}
