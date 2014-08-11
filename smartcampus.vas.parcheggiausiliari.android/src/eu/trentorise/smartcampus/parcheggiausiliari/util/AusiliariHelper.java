package eu.trentorise.smartcampus.parcheggiausiliari.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.ParkingLog;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
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

	public void sendData(GeoObject obj) {
		SetDataTask ast = new SetDataTask();
		ast.execute(obj);
	}

	public static ArrayList<ParkingLog> getStorico() {
		ArrayList<ParkingLog> toRtn = new ArrayList<ParkingLog>();
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

	public static ArrayList<ParkingLog> getStoricoAgente() {
		ArrayList<ParkingLog> toRtn = new ArrayList<ParkingLog>();
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
			AsyncTask<GeoObject, Void, Void> {
		ProgressDialog pd;

		@Override
		protected Void doInBackground(GeoObject... params) {
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
			AsyncTask<Void, Void, ArrayList<ParkingLog>> {
		ProgressDialog pd;

		@Override
		protected ArrayList<ParkingLog> doInBackground(Void... params) {
			ArrayList<ParkingLog> array = new ArrayList<ParkingLog>();
			int a = new Random().nextInt(90000000);
			for (int i = 0; i < 10; i++) {
				ParkingLog p = new ParkingLog();
				p.setAuthor("Mario Rossi");
				p.setTime(System.currentTimeMillis()+ (i*a));
				array.add(p);
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
		protected void onPostExecute(ArrayList<ParkingLog> result) {
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
					new Street(),
					new Street(),
					new Street() };
			array[0].setName("Via alla Cascata");
			array[0].setDescription("Via di prova");
			array[0].setId("v001");
			array[0].setSlotsFree(10);
			array[0].setSlotsPaying(6);
			array[0].setSlotsTimed(8);
			array[1].setName("Via Sommarive");
			array[1].setDescription("Via di prova");
			array[1].setId("v002");
			array[1].setSlotsFree(2);
			array[1].setSlotsPaying(18);
			array[1].setSlotsTimed(1);
			array[2].setName("Via dei Valoni");
			array[2].setDescription("Via di prova");
			array[2].setId("v003");
			array[2].setSlotsFree(0);
			array[2].setSlotsPaying(0);
			array[2].setSlotsTimed(16);
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
					new Parking(),
					new Parking(),
					new Parking() };
			
			array[0].setName("Parcheggio 1");
			array[0].setDescription("Parcheggio di prova");
			array[0].setId("p001");
			array[0].setSlotsTotal(132);
			array[0].setPosition(loc1);
			array[1].setName("Parcheggio 2");
			array[1].setDescription("Parcheggio di prova");
			array[1].setId("p002");
			array[1].setSlotsTotal(948);
			array[1].setPosition(loc2);
			array[2].setName("Parcheggio 3 ");
			array[2].setDescription("Parcheggio di prova");
			array[2].setId("p003");
			array[2].setSlotsTotal(33);
			array[2].setPosition(loc3);
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