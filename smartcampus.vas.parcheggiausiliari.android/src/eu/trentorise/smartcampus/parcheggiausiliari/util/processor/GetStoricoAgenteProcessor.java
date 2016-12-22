package eu.trentorise.smartcampus.parcheggiausiliari.util.processor;

import java.util.List;
import java.util.Map;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateStoricoAgenteInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;

public class GetStoricoAgenteProcessor extends
		AsyncTask<Object, Void, List<LogObject>> {
	UpdateStoricoAgenteInterface updateStoricoAgenteInterface;
	Activity activity;
	ProgressDialog pd;

	public GetStoricoAgenteProcessor(Activity activity) {
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

	protected List<LogObject> doInBackground(Object... params) {
		// get Parkings
		this.updateStoricoAgenteInterface = (UpdateStoricoAgenteInterface) params[0];
		return AusiliariHelper.getStoricoAgente();
	}

	protected void onPostExecute(List<LogObject> storico) {
		TextView tv = (TextView) activity.findViewById(R.id.txtNoData);

		if (storico != null && storico.size() > 0) {
			if (tv != null) {
				tv.setVisibility(View.GONE);
			}
			updateStoricoAgenteInterface.addStorico(storico);

		} else {
			// enable layout message "empty list"
			if (tv != null) {
				tv.setVisibility(View.VISIBLE);
			}
		}
		if (pd.isShowing())
			pd.dismiss();
		updateStoricoAgenteInterface.refreshLog();
	}
}
