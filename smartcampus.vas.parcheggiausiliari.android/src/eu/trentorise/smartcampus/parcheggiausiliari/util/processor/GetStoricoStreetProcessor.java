package eu.trentorise.smartcampus.parcheggiausiliari.util.processor;

import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoStreetInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.model.StreetLog;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class GetStoricoStreetProcessor extends
		AsyncTask<Object, Void, List<StreetLog>> {
	UpdateStoricoStreetInterface updateStoricoStreetInterface;
	Activity activity;
	Street street;
	ProgressDialog pd;

	public GetStoricoStreetProcessor(Activity activity) {
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

	protected List<StreetLog> doInBackground(Object... params) {
		// get Parkings
		this.updateStoricoStreetInterface = (UpdateStoricoStreetInterface) params[0];
		this.street = (Street) params[1];
		return AusiliariHelper.getStoricoStreet(street);
	}

	protected void onPostExecute(List<StreetLog> storico) {
		TextView tv = (TextView) activity.findViewById(R.id.txtNoData);

		if (storico != null && storico.size() > 0) {
			if (tv != null) {
				tv.setVisibility(View.GONE);
			}
			updateStoricoStreetInterface.addStoricoStreet(storico);

		} else {
			// enable layout message "empty list"
			if (tv != null) {
				tv.setVisibility(View.VISIBLE);
			}
		}
		if (pd.isShowing())
			pd.dismiss();
	}
}
