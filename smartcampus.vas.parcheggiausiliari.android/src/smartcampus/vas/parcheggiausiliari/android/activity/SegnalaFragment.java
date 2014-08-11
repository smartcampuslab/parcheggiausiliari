package smartcampus.vas.parcheggiausiliari.android.activity;

import smartcampus.vas.parcheggiausiliari.android.R;
import smartcampus.vas.parcheggiausiliari.android.model.BaseDT;
import smartcampus.vas.parcheggiausiliari.android.model.Parking;
import smartcampus.vas.parcheggiausiliari.android.model.Street;
import smartcampus.vas.parcheggiausiliari.android.util.AusiliariHelper;
import smartcampus.vas.parcheggiausiliari.android.views.NumberPicker;
import smartcampus.vas.parcheggiausiliari.android.views.NumberPicker.OnChangedListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SegnalaFragment extends Fragment {

	private static final String MY_PREFERENCES = "Ausiliari";
	private NumberPicker mPickerFree;
	private NumberPicker mPickerWork;
	private NumberPicker mPickerPayment;
	private NumberPicker mPickerTimed;

	private int mValue1;
	private int mValue2;
	private int mValue3;
	private int mValue4;
	private Button btnAnnulla;
	private BaseDT obj;
	private TextView mTxt;
	private Button btnSend;
	private TextView txtFree;
	private TextView txtPayment;
	private TextView txtTimed;

	public SegnalaFragment(BaseDT obj) {
		// TODO Auto-generated constructor stub
		this.obj = obj;
	}

	static SegnalaFragment newInstance(BaseDT obj, int val1, int val2,
			int val3, int val4) {
		SegnalaFragment f = new SegnalaFragment(obj);
		Bundle args = new Bundle();
		args.putInt("val1", val1);
		args.putInt("val2", val2);
		args.putInt("val3", val3);
		args.putInt("val4", val4);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_segnala, container,
				false);
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
		btnAnnulla.setOnClickListener(new MyCLickListener());

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
		}

		// TODO ??????????????????
		mPickerWork.setRange(0, a);

		btnSend = (Button) rootView.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
						.setIcon(android.R.drawable.ic_menu_upload)
						.setTitle("Segnalazione")
						.setMessage(
								"Stai per fare una segnalazione. Continuare?")
						.setPositiveButton("Si",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										clearFocus();
										updateObject();
										new AusiliariHelper(getActivity())
												.sendData(obj);
										Toast.makeText(getActivity(),
												"Data Sent", Toast.LENGTH_LONG)
												.show();
									}
								}).setNegativeButton("No", null).show();
			}
		});

		mPickerFree.setOnChangeListener(new MyOnChangeListener());
		mPickerPayment.setOnChangeListener(new MyOnChangeListener());
		mPickerTimed.setOnChangeListener(new MyOnChangeListener());
		mPickerWork.setOnChangeListener(new MyOnChangeListener());

		return rootView;

	}

	/**
	 * method called to update value of the numberpickers if the text was
	 * written with the keyboard
	 */
	private void clearFocus() {
		// TODO Auto-generated method stub
		mPickerFree.clearFocus();
		mPickerPayment.clearFocus();
		mPickerTimed.clearFocus();
		mPickerWork.clearFocus();
	}

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

	private class MyCLickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(getActivity())
					.setIcon(android.R.drawable.ic_delete)
					.setTitle("Reset")
					.setMessage("Stai per cancellare i dati... Continuare?")
					.setPositiveButton("Si",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									mPickerFree.setCurrent(0);
									mPickerWork.setCurrent(0);
									mPickerPayment.setCurrent(0);
									mPickerTimed.setCurrent(0);

									SharedPreferences prefs = getActivity()
											.getSharedPreferences(
													MY_PREFERENCES,
													Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = prefs
											.edit();
									editor.remove(obj.getId()).commit();
									Toast.makeText(getActivity(),
											"Dati cancellati",
											Toast.LENGTH_LONG).show();
								}

							}).setNegativeButton("No", null).show();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void updateData() {
		SharedPreferences prefs = getActivity().getSharedPreferences(
				MY_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		if (Parking.class.isInstance(obj))
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
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences prefs = getActivity().getSharedPreferences(
				MY_PREFERENCES, Context.MODE_PRIVATE);
		String load = prefs.getString(obj.getId(), null);
		if (load != null) {
			Log.e("LOAD", load);
			String[] splitted = load.split(" ");
			mPickerFree.setCurrent(Integer.parseInt(splitted[0]));
			mPickerWork.setCurrent(Integer.parseInt(splitted[1]));
			if (splitted.length > 4) {
				mPickerPayment.setCurrent(Integer.parseInt(splitted[2]));
				mPickerTimed.setCurrent(Integer.parseInt(splitted[3]));
			}
		} else {

			mValue1 = getArguments().getInt("val1");
			mValue2 = getArguments().getInt("val2");
			mValue3 = getArguments().getInt("val3");
			mValue4 = getArguments().getInt("val4");
		}

	}

	private class MyOnChangeListener implements OnChangedListener {

		@Override
		public void onChanged(NumberPicker picker, int oldVal, int newVal) {
			updateData();
		}

	}
}
