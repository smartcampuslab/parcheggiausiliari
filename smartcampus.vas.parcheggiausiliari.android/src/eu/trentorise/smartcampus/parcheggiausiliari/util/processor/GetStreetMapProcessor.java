package eu.trentorise.smartcampus.parcheggiausiliari.util.processor;

import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activity.AddGeoPoints;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class GetStreetMapProcessor extends
		AsyncTask<Object, Void, List<Street>> {
	AddGeoPoints addGeoPointInterface;
	Activity activity;
	ProgressDialog pd;

	public GetStreetMapProcessor(Activity activity) {
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

	protected List<Street> doInBackground(Object... params) {
		// get Parkings
		this.addGeoPointInterface = (AddGeoPoints) params[0];
		return AusiliariHelper.getStreetlist();
	}

	protected void onPostExecute(List<Street> streets) {
		if (streets != null && streets.size() > 0) {
			addGeoPointInterface.addStreets(streets);
		}
		if (pd.isShowing())
			pd.dismiss();
	}
}
