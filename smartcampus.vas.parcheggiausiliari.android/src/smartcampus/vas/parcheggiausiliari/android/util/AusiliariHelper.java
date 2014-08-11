package smartcampus.vas.parcheggiausiliari.android.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import smartcampus.vas.parcheggiausiliari.android.model.BaseDT;
import smartcampus.vas.parcheggiausiliari.android.model.Parking;
import smartcampus.vas.parcheggiausiliari.android.model.Street;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AusiliariHelper {
	private static Context mContext;
	private static AusiliariHelper helper;

	public AusiliariHelper(Context ctx) {
		this.mContext = ctx;
	}

	public static boolean isInstantiated() {
		return (helper != null);
	}

	public void sendData(BaseDT obj) {
		SetDataTask ast = new SetDataTask();
		ast.execute(obj);
	}

	public static ArrayList<String> getStorico() {
		ArrayList<String> toRtn = new ArrayList<String>();
		try {
			GetStoricoTask ast = new GetStoricoTask();
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

	private static class SetDataTask extends
			AsyncTask<BaseDT, Void, Void> {
		ProgressDialog pd;

		@Override
		protected Void doInBackground(BaseDT... params) {
			if(Parking.class.isInstance(params[0]))
			{
				Parking temp = ((Parking)params[0]);
				Log.d("DEBUG","Parcheggio");
				Log.d("DEBUG", temp.getName()+ " " +temp.getSlotsOccupiedOnTotal()+" "+temp.getSlotsUnavailable());
			} else {
				Street temp = ((Street)params[0]);
				Log.d("DEBUG","Via");
				Log.d("DEBUG", temp.getName()+ " " +temp.getSlotsOccupiedOnFree()+" "+temp.getSlotsOccupiedOnPaying()+" "+temp.getSlotsOccupiedOnTimed()+" "+temp.getSlotsUnavailable());
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

	private static class GetStoricoTask extends
			AsyncTask<Void, Void, ArrayList<String>> {
		ProgressDialog pd;

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			ArrayList<String> array = new ArrayList<String>();
			for (int i = 0; i < 10; i++) {
				array.add(i + ":  14 / 07 / 2014 alle 17.16\n      da Mario Rossi");
			}
			return array;

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
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

	public static Parking[] getParklist() {
		Parking[] array = null;
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

	public static Street[] getStreetlist() {
		Street[] array = null;
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

	private static class GetStreetsTask extends AsyncTask<Void, Void, Street[]> {
		ProgressDialog pd;

		@Override
		protected Street[] doInBackground(Void... params) {
			Street[] array = {
					new Street("Via alla Cascata", "Via di prova", "v001", 10,
							6, 8,null,null),
					new Street("Via Sommarive", "Via di prova", "v002", 2, 18,
							1,null,null),
					new Street("Via dei Valoni", "Via di prova", "v003", 0, 0,
							3,null,null) };
			return array;
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
		protected void onPostExecute(Street[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

	private static class GetParkingTask extends
			AsyncTask<Void, Void, Parking[]> {
		ProgressDialog pd;

		@Override
		protected Parking[] doInBackground(Void... params) {
			double[] loc1 = { 46.068654, 11.150679 };
			double[] loc2 = { 46.069386, 11.151389 };
			double[] loc3 = { 46.068686, 11.151723 };
			Parking[] array = {
					new Parking("Parcheggio1", "Parcheggio di prova", "p001",
							loc1, 132),
					new Parking("Parcheggio2", "Parcheggio di prova", "p002",
							loc2, 948),
					new Parking("Parcheggio3", "Parcheggio di prova", "p003",
							loc3, 33) };
			return array;

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
		protected void onPostExecute(Parking[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pd.isShowing())
				pd.dismiss();
		}
	}

}