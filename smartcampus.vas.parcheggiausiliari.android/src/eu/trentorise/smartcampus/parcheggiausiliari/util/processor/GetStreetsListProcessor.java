package eu.trentorise.smartcampus.parcheggiausiliari.util.processor;

import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStreetListInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class GetStreetsListProcessor extends
		AsyncTask<Object, Void, List<Street>> {
	UpdateStreetListInterface updateStreetListInterface;
	Activity activity;
	ProgressDialog pd;

	public GetStreetsListProcessor(Activity activity) {
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
		this.updateStreetListInterface = (UpdateStreetListInterface) params[0];
		return AusiliariHelper.getStreetlist();
	}

	protected void onPostExecute(List<Street> streets) {
		TextView tv = (TextView) activity.findViewById(R.id.streets_empty_text);

		if (streets != null && streets.size() > 0) {
			if (tv != null) {
				tv.setVisibility(View.GONE);
			}
			updateStreetListInterface.addStreets(streets);

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
