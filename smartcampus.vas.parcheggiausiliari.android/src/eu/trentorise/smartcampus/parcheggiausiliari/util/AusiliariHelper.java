package eu.trentorise.smartcampus.parcheggiausiliari.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.model.StreetLog;
import eu.trentorise.smartcampus.parcheggiausiliari.util.constants.Parcheggi_Services;

public class AusiliariHelper implements Parcheggi_Services {
	private static Context mContext;

	public AusiliariHelper(Context ctx ) {
		this.mContext = ctx;
	}

	public void sendData(GeoObject obj) {
		SetDataTask ast = new SetDataTask();
		ast.execute(obj);
	}

	public static List<ParkingLog> getStoricoPark(Parking obj) {
		List<ParkingLog> toRtn = new ArrayList<ParkingLog>();
		try {
			GetParkingStoricoTask ast = new GetParkingStoricoTask();
			ast.execute(obj);
			toRtn = ast.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toRtn;
	}

	public static List<StreetLog> getStoricoStreet(Street obj) {
		List<StreetLog> toRtn = new ArrayList<StreetLog>();
		try {
			GetStreetStoricoTask ast = new GetStreetStoricoTask();
			ast.execute(obj);
			toRtn = ast.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toRtn;
	}

	public static List<ParkingLog> getStoricoAgente() {
		List<ParkingLog> toRtn = new ArrayList<ParkingLog>();
		try {
			GetStoricoAgenteTask ast = new GetStoricoAgenteTask();
			ast.execute();
			toRtn = ast.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toRtn;
	}

	private static class SetDataTask extends AsyncTask<GeoObject, Void, Void> {
		ProgressDialog pd;

		@Override
		protected Void doInBackground(GeoObject... params) {
			try {
				if (Parking.class.isInstance(params[0])) {
					RemoteConnector.postJSON(HOST,"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
							UPDATEPARK + params[0].getId() + "/" + new AusiliariHelper(mContext).getUsername(),
							JsonUtils.toJSON(params[0]), mContext
									.getResources().getString(R.string.token));
				} else {
					RemoteConnector.postJSON(HOST,"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
							UPDATESTREET + params[0].getId() + "/" + new AusiliariHelper(mContext).getUsername(),
							JsonUtils.toJSON(params[0]), mContext
									.getResources().getString(R.string.token));
				}
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
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(mContext);
			pd.setTitle("Sending Data");
			pd.setMessage("Attendere");
			pd.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

	private static class GetStreetStoricoTask extends
			AsyncTask<GeoObject, Void, List<StreetLog>> {
		ProgressDialog pd;

		@Override
		protected List<StreetLog> doInBackground(GeoObject... params) {
			String request = null;
			List<StreetLog> list = null;
			try {
				Log.d("DEBUG", HOST +"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
						STREETLOGLIST + params[0].getId());
				request = RemoteConnector.getJSON(HOST,"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
						STREETLOGLIST + params[0].getId(), mContext.getResources()
								.getString(R.string.token));
				list = JsonUtils.toObjectList(request, StreetLog.class);

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return list;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(mContext);
			pd.setTitle("Downloading Data");
			pd.show();
		}

		@Override
		protected void onPostExecute(List<StreetLog> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

	private static class GetParkingStoricoTask extends
			AsyncTask<GeoObject, Void, List<ParkingLog>> {
		ProgressDialog pd;

		@Override
		protected List<ParkingLog> doInBackground(GeoObject... params) {
			String request = null;
			List<ParkingLog> list = null;
			try {
				Log.d("DEBUG", HOST +"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
						PARKLOGLIST + params[0].getId());
				request = RemoteConnector.getJSON(HOST,"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
						PARKLOGLIST + params[0].getId(), mContext.getResources()
								.getString(R.string.token));
				list = JsonUtils.toObjectList(request, ParkingLog.class);

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return list;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(mContext);
			pd.setTitle("Downloading Data");
			pd.show();
		}

		@Override
		protected void onPostExecute(List<ParkingLog> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

	private static class GetStoricoAgenteTask extends
			AsyncTask<Void, Void, List<ParkingLog>> {
		ProgressDialog pd;

		@Override
		protected List<ParkingLog> doInBackground(Void... params) {
			String request = null;
			try {
				Log.d("DEBUG", HOST +"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
						AUSLOGLIST + new AusiliariHelper(mContext).getUsername());
				request = RemoteConnector.getJSON(HOST,"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+ AUSLOGLIST + new AusiliariHelper(mContext).getUsername(),
						mContext.getResources().getString(R.string.token));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return JsonUtils.toObjectList(request, ParkingLog.class);

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(mContext);
			pd.setTitle("Downloading Data");
			pd.show();
		}

		@Override
		protected void onPostExecute(List<ParkingLog> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

	public static List<Parking> getParklist() {
		List<Parking> array = null;
		try {
			GetParkingTask ast = new GetParkingTask();
			ast.execute();
			array = ast.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}

	public static List<Street> getStreetlist() {
		List<Street> array = null;
		try {
			GetStreetsTask ast = new GetStreetsTask();
			ast.execute();
			array = ast.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}

	private static class GetStreetsTask extends
			AsyncTask<Void, Void, List<Street>> {
		ProgressDialog pd;

		@Override
		protected List<Street> doInBackground(Void... params) {
			String request = null;
			try {
				Log.d("DEBUG", HOST +"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
						STREETLIST);
				request = RemoteConnector.getJSON(HOST,"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+ STREETLIST, mContext
						.getResources().getString(R.string.token));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return JsonUtils.toObjectList(request, Street.class);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(mContext);
			pd.setTitle("Downloading Data");
			pd.show();
		}

		@Override
		protected void onPostExecute(List<Street> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

	private static class GetParkingTask extends
			AsyncTask<Void, Void, List<Parking>> {
		ProgressDialog pd;

		@Override
		protected List<Parking> doInBackground(Void... params) {
			String request = null;
			try {
				Log.d("DEBUG", HOST +"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+
						PARKLIST);
				request = RemoteConnector.getJSON(HOST,"parcheggiausiliari/"+mContext.getResources().getString(R.string.applocation)+ PARKLIST, mContext
						.getResources().getString(R.string.token));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return JsonUtils.toObjectList(request, Parking.class);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(mContext);
			pd.setTitle("Downloading Data");
			pd.show();
		}

		@Override
		protected void onPostExecute(List<Parking> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

	public static String getUsername() {
		return ((Activity) mContext).getSharedPreferences("Login",0).getString(
				"User", null);
	}

}