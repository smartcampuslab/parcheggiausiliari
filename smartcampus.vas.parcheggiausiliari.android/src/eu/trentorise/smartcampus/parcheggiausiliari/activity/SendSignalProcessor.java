package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import eu.trentorise.smartcampus.parcheggiausiliari.activityinterface.UpdateSegnalaInterface;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;
import eu.trentorise.smartcampus.parcheggiausiliari.util.JsonUtils;
import eu.trentorise.smartcampus.parcheggiausiliari.util.RemoteConnector;
import eu.trentorise.smartcampus.parcheggiausiliari.util.RemoteException;
import eu.trentorise.smartcampus.parcheggiausiliari.util.constants.Parcheggi_Services;

public class SendSignalProcessor extends AsyncTask<Object, Void, Boolean> {
	Activity activity;
	ProgressDialog pd;
	UpdateSegnalaInterface updateSegnalaInterface;
	public SendSignalProcessor(	Activity activity) {
	this.activity=activity;
}
	@Override
	protected Boolean doInBackground(Object... params) {
		updateSegnalaInterface = (UpdateSegnalaInterface) params[1];
		try {
			if (Parking.class.isInstance(params[0])) {
				RemoteConnector.postJSON(
						Parcheggi_Services.HOST,
						activity.getResources().getString(
								R.string.park)
								+ activity.getResources().getString(
										R.string.applocation)
								+ Parcheggi_Services.UPDATEPARK
								+ ((Parking)params[0]).getId(), JsonUtils
								.toJSON(params[0]), activity.getResources()
								.getString(R.string.token));
			} else {
				RemoteConnector.postJSON(
						Parcheggi_Services.HOST,
						activity.getResources().getString(
								R.string.park)
								+ activity.getResources().getString(
										R.string.applocation)
								+ Parcheggi_Services.UPDATESTREET
								+ ((Street)params[0]).getId(), JsonUtils
								.toJSON(params[0]), activity.getResources()
								.getString(R.string.token));
			}
			return true;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new ProgressDialog(activity);
		pd.setTitle(activity.getString(R.string.dialog_loading));
		pd.setMessage(activity.getString(R.string.dialog_waiting));
		pd.show();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		updateSegnalaInterface.signal(result);
		if (pd.isShowing())
			pd.dismiss();
	}
}

//public class SendSignalProcessor extends
//		AsyncTask<Object, Void, List<Parking>> {
//	UpdateParkListInterface updateParkListInterface;
//	Activity activity;
//	ProgressDialog pd;
//
//	public SendSignalProcessor(	Activity activity) {
//		this.activity=activity;
//	}
//	@Override
//	protected void onPreExecute() {
//		// TODO Auto-generated method stub
//		super.onPreExecute();
//		pd = new ProgressDialog(activity);
//		pd.setTitle(activity.getString(R.string.dialog_loading));
//		pd.setMessage(activity.getString(R.string.dialog_waiting));
//		pd.show();
//	}
//
//
//
//	protected List<Parking> doInBackground(Object... params) {
//		// get Parkings
//		this.updateParkListInterface = (UpdateParkListInterface) params[0];
//		return AusiliariHelper.getParklist();
//	}
//
//	protected void onPostExecute(List<Parking> parks) {
//		TextView tv = (TextView) activity.findViewById(R.id.parks_empty_text);
//		ListView lv = (ListView) activity.findViewById(R.id.listParkings);
//		if (parks != null && parks.size() > 0) {
//			if (tv != null) {
//				lv.setVisibility(View.VISIBLE);
//				tv.setVisibility(View.GONE);
//			}
//			updateParkListInterface.addParkings(parks);
//			// adapter.addAll(parks);
//			// adapter.notifyDataSetChanged();
//			// disable layout message "empty list"
//
//		} else {
//			// enable layout message "empty list"
//			if (tv != null) {
//				lv.setVisibility(View.GONE);
//				tv.setVisibility(View.VISIBLE);
//			}
//		}
//		if (pd.isShowing())
//			pd.dismiss();
//	}
//}
