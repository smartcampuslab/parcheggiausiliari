package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.util.AusiliariHelper;
import eu.trentorise.smartcampus.parcheggiausiliari.views.NumberPicker;
import eu.trentorise.smartcampus.parcheggiausiliari.views.NumberPicker.OnChangedListener;

public class SegnalaFragment extends Fragment {

	private static final String MY_PREFERENCES = "Ausiliari";
	private NumberPicker mPickerFree;
	private NumberPicker mPickerWork;
	private NumberPicker mPickerPayment;
	private NumberPicker mPickerTimed;

	private Button btnAnnulla;
	private GeoObject obj;
	private TextView mTxt;
	private Button btnSend;
	private TextView txtFree;
	private TextView txtPayment;
	private TextView txtTimed;

	public SegnalaFragment(GeoObject obj) {
		this.obj = obj;
	}

	/**
	 * method called only in onConfigurationChanged to save  current values 
	 */
	private void saveValues() {
		clearFocus();
		SharedPreferences sp = getActivity().getPreferences(0);
		sp.edit().putInt("FREE", mPickerFree.getCurrent()).apply();
		;
		sp.edit().putInt("PAY", mPickerPayment.getCurrent()).apply();
		;
		sp.edit().putInt("TIME", mPickerTimed.getCurrent()).apply();
		;
		sp.edit().putInt("WORK", mPickerWork.getCurrent()).apply();
		;
	}
	/**
	 * method called only in onConfigurationChanged to restore current values 
	 */
	private void restoreValues() {
		SharedPreferences sp = getActivity().getPreferences(0);
		mPickerFree.setCurrent(sp.getInt("FREE", 0));
		mPickerPayment.setCurrent(sp.getInt("PAY", 0));
		mPickerTimed.setCurrent(sp.getInt("TIME", 0));
		mPickerWork.setCurrent(sp.getInt("WORK", 0));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		saveValues();
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		populateViewForOrientation(inflater, (ViewGroup) getView());
	}

	/**
	 * method called to load the correct layout when the device is rotated
	 * @param inflater
	 * @param viewGroup
	 */
	private void populateViewForOrientation(LayoutInflater inflater,
			ViewGroup viewGroup) {
		viewGroup.removeAllViewsInLayout();
		View subview = inflater.inflate(R.layout.fragment_segnala, viewGroup);

		mTxt = (TextView) subview.findViewById(R.id.txtTitle);
		mTxt.setText(obj.getName());
		mPickerFree = (NumberPicker) subview.findViewById(R.id.NumberPicker01);
		mPickerWork = (NumberPicker) subview.findViewById(R.id.NumberPicker04);
		LinearLayout btnsStreet = (LinearLayout) subview
				.findViewById(R.id.streetBtns);
		if (Parking.class.isInstance(obj)) {
			btnsStreet.setVisibility(View.GONE);
			View separatore = subview.findViewById(R.id.separatore);
			if (separatore != null)
				separatore.setVisibility(View.GONE);
		}
		mPickerPayment = (NumberPicker) subview
				.findViewById(R.id.NumberPicker02);
		mPickerTimed = (NumberPicker) subview.findViewById(R.id.NumberPicker03);
		btnAnnulla = (Button) subview.findViewById(R.id.btnReset);
		btnAnnulla.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new ConfirmPopup("Reset",
						"Stai per cancellare i dati... Continuare?",
						R.drawable.ic_rimuovi) {

					@Override
					public void confirm() {
						resetPickers();
						SharedPreferences prefs = getActivity()
								.getSharedPreferences(MY_PREFERENCES,
										Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.remove(obj.getId()).commit();
//						Toast.makeText(getActivity(), "Dati cancellati",
//								Toast.LENGTH_LONG).show();
					}
				}.show(getFragmentManager(), null);
			}
		});

		txtFree = (TextView) subview.findViewById(R.id.txtMaxFree);
		txtPayment = (TextView) subview.findViewById(R.id.txtMaxPayment);
		txtTimed = (TextView) subview.findViewById(R.id.txtMaxTimed);
		int a = 0;
		if (Parking.class.isInstance(obj)) {
			txtFree.setText("/" + ((Parking) obj).getSlotsTotal());
			mPickerFree.setRange(0, ((Parking) obj).getSlotsTotal());
			a += ((Parking) obj).getSlotsTotal();
		} else {
			a += ((Street) obj).getSlotsFree()
					+ ((Street) obj).getSlotsPaying()
					+ ((Street) obj).getSlotsTimed();
			txtFree.setText("/" + ((Street) obj).getSlotsFree());
			mPickerFree.setRange(0, ((Street) obj).getSlotsFree());
			txtPayment.setText("/" + ((Street) obj).getSlotsPaying());
			mPickerPayment.setRange(0, ((Street) obj).getSlotsPaying());
			txtTimed.setText("/" + ((Street) obj).getSlotsTimed());
			mPickerTimed.setRange(0, ((Street) obj).getSlotsTimed());
		}
		mPickerWork.setRange(0, a);

		btnSend = (Button) subview.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ConfirmPopup("Segnalazione",
						"Stai per fare una segnalazione. Continuare?",
						R.drawable.ic_invia) {

					@Override
					public void confirm() {
						clearFocus();
						updateObject();
						new AusiliariHelper(getActivity()).sendData(obj);
						Toast.makeText(getActivity(), "Dati inviati",
								Toast.LENGTH_LONG).show();
						getActivity()
								.getSharedPreferences(MY_PREFERENCES,
										Context.MODE_PRIVATE).edit()
								.remove(obj.getId()).commit();
						resetPickers();
						refresh();
					}
				}.show(getFragmentManager(), null);
			}
		});

		mPickerFree.setOnChangeListener(new MyOnChangeListener());
		mPickerPayment.setOnChangeListener(new MyOnChangeListener());
		mPickerTimed.setOnChangeListener(new MyOnChangeListener());
		mPickerWork.setOnChangeListener(new MyOnChangeListener());
		restoreValues();
	}

	static SegnalaFragment newInstance(GeoObject obj) {
		SegnalaFragment f = new SegnalaFragment(obj);
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_segnala, container,
				false);
		// setRetainInstance(true);
		mTxt = (TextView) rootView.findViewById(R.id.txtTitle);
		mTxt.setText(obj.getName());
		mPickerFree = (NumberPicker) rootView.findViewById(R.id.NumberPicker01);
		mPickerWork = (NumberPicker) rootView.findViewById(R.id.NumberPicker04);
		LinearLayout btnsStreet = (LinearLayout) rootView
				.findViewById(R.id.streetBtns);
		if (Parking.class.isInstance(obj)) {
			btnsStreet.setVisibility(View.GONE);
		}
		mPickerPayment = (NumberPicker) rootView
				.findViewById(R.id.NumberPicker02);
		mPickerTimed = (NumberPicker) rootView
				.findViewById(R.id.NumberPicker03);
		btnAnnulla = (Button) rootView.findViewById(R.id.btnReset);
		btnAnnulla.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new ConfirmPopup("Reset",
						"Stai per cancellare i dati... Continuare?",
						R.drawable.ic_rimuovi) {

					@Override
					public void confirm() {
						resetPickers();
						SharedPreferences prefs = getActivity()
								.getSharedPreferences(MY_PREFERENCES,
										Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.remove(obj.getId()).commit();
//						Toast.makeText(getActivity(), "Dati cancellati",
//								Toast.LENGTH_LONG).show();
					}
				}.show(getFragmentManager(), null);
			}
		});

		txtFree = (TextView) rootView.findViewById(R.id.txtMaxFree);
		txtPayment = (TextView) rootView.findViewById(R.id.txtMaxPayment);
		txtTimed = (TextView) rootView.findViewById(R.id.txtMaxTimed);
		int a = 0;
		if (Parking.class.isInstance(obj)) {
			txtFree.setText("/" + ((Parking) obj).getSlotsTotal());
			mPickerFree.setRange(0, ((Parking) obj).getSlotsTotal());
			a += ((Parking) obj).getSlotsTotal();
		} else {
			a += ((Street) obj).getSlotsFree()
					+ ((Street) obj).getSlotsPaying()
					+ ((Street) obj).getSlotsTimed();
			txtFree.setText("/" + ((Street) obj).getSlotsFree());
			mPickerFree.setRange(0, ((Street) obj).getSlotsFree());
			txtPayment.setText("/" + ((Street) obj).getSlotsPaying());
			mPickerPayment.setRange(0, ((Street) obj).getSlotsPaying());
			txtTimed.setText("/" + ((Street) obj).getSlotsTimed());
			mPickerTimed.setRange(0, ((Street) obj).getSlotsTimed());

			View blockFree = rootView.findViewById(R.id.free_block);
			if (((Street) obj).getSlotsFree() == 0) {
				blockFree.setEnabled(false);
			}
			View blockPayment = rootView.findViewById(R.id.payment_block);
			if (((Street) obj).getSlotsPaying() == 0) {
				blockPayment.setEnabled(false);
			}
			View blockTime = rootView.findViewById(R.id.time_block);
			if (((Street) obj).getSlotsTimed() == 0) {
				blockTime.setEnabled(false);
			}
		}

		
		mPickerWork.setRange(0, a);

		btnSend = (Button) rootView.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ConfirmPopup("Segnalazione",
						"Stai per fare una segnalazione. Continuare?",
						R.drawable.ic_invia) {

					@Override
					public void confirm() {
						clearFocus();
						updateObject();
						new AusiliariHelper(getActivity()).sendData(obj);
						Toast.makeText(getActivity(), "Dati inviati",
								Toast.LENGTH_LONG).show();
						getActivity()
								.getSharedPreferences(MY_PREFERENCES,
										Context.MODE_PRIVATE).edit()
								.remove(obj.getId()).commit();
						resetPickers();
						//refresh();
					}
				}.show(getFragmentManager(), null);
			}
		});

		mPickerFree.setOnChangeListener(new MyOnChangeListener());
		mPickerPayment.setOnChangeListener(new MyOnChangeListener());
		mPickerTimed.setOnChangeListener(new MyOnChangeListener());
		mPickerWork.setOnChangeListener(new MyOnChangeListener());

		return rootView;

	}

	/**
	 * method called after the signal is sent to repopulate the list in the StoricoFragment
	 */
	public void refresh() {
		getFragmentManager().beginTransaction()
				.replace(R.id.container, new DetailsFragment(obj)).commit();
	}

	/**
	 * method called to update value of the numberpickers if the text was
	 * written with the keyboard
	 */
	private void clearFocus() {
		mPickerFree.clearFocus();
		mPickerPayment.clearFocus();
		mPickerTimed.clearFocus();
		mPickerWork.clearFocus();
	}

	/**
	 * method called to reset the number of the pickers, erasing the data saved
	 */
	private void resetPickers() {
		mPickerFree.setCurrent(0);
		mPickerWork.setCurrent(0);
		mPickerPayment.setCurrent(0);
		mPickerTimed.setCurrent(0);
		SharedPreferences prefs = getActivity().getSharedPreferences(
				MY_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(obj.getId()).commit();
	}

	/**
	 * method called just befor sending the data to update all the values of the object
	 */
	private void updateObject() {
		if (Parking.class.isInstance(obj)) {
			((Parking) obj).setSlotsOccupiedOnTotal(mPickerFree.getCurrent());
			((Parking) obj).setSlotsUnavailable(mPickerWork.getCurrent());
		} else {

			((Street) obj).setSlotsOccupiedOnFree(mPickerFree.getCurrent());
			((Street) obj).setSlotsUnavailable(mPickerWork.getCurrent());
			((Street) obj)
					.setSlotsOccupiedOnPaying(mPickerPayment.getCurrent());
			((Street) obj).setSlotsOccupiedOnTimed(mPickerTimed.getCurrent());
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void updateData() {
		SharedPreferences prefs = getActivity().getPreferences(
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		if (Street.class.isInstance(obj))
			editor.putString(
					obj.getId(),
					"" + mPickerFree.getCurrent() + " "
							+ mPickerWork.getCurrent() + " "
							+ mPickerPayment.getCurrent() + " "
							+ mPickerTimed.getCurrent());
		else
			editor.putString(obj.getId(), "" + mPickerFree.getCurrent() + " "
					+ mPickerWork.getCurrent());
		editor.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences prefs = getActivity().getPreferences(
				Context.MODE_PRIVATE);
		String load = prefs.getString(obj.getId(), null);
		if (load != null) {
			String[] splitted = load.split(" ");
			mPickerFree.setCurrent(Integer.parseInt(splitted[0]));
			mPickerWork.setCurrent(Integer.parseInt(splitted[1]));
			if (splitted.length > 2) {
				mPickerPayment.setCurrent(Integer.parseInt(splitted[2]));
				mPickerTimed.setCurrent(Integer.parseInt(splitted[3]));
			}
		}
	}

	private class MyOnChangeListener implements OnChangedListener {
		@Override
		public void onChanged(NumberPicker picker, int oldVal, int newVal) {
			updateData();
		}

	}
}
