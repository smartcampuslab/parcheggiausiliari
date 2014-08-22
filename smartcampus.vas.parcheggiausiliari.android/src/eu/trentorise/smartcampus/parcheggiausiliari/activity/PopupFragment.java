package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.Date;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;

public class PopupFragment extends DialogFragment {

	private GeoObject obj = null;

	public PopupFragment(GeoObject obj) {
		// TODO Auto-generated constructor stub
		this.obj = obj;
	}

	public static PopupFragment newInstance(GeoObject obj) {

		return null;
	}

	public void setObj(GeoObject obj) {
		this.obj = obj;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);
		View v = inflater.inflate(R.layout.popup_fragment, container, false);
		// setRetainInstance(true);

		TextView tv = (TextView) v.findViewById(R.id.txtLastData);
		if (Parking.class.isInstance(obj)) {
			Date d = new Date(((Parking) obj).getLastChange().getTime());
			tv.setText(((Parking) obj).getLastChange() != null ? (((Parking) obj)
					.getLastChange().getAuthor()
					+ " - ore "
					+ String.format("%02d", d.getHours())
					+ ":"
					+ String.format("%02d", d.getMinutes())
					+ " - "
					+ String.format("%02d", d.getDate())
					+ "/"
					+ String.format("%02d", (d.getMonth() + 1)) + "/" + (d
					.getYear() + 1900))
					: "Not recently updated");
		} else {
			Date d = new Date(((Street) obj).getLastChange().getTime());
			tv.setText(((Street) obj).getLastChange() != null ? (((Street) obj)
					.getLastChange().getAuthor()
					+ " - ore "
					+ String.format("%02d", d.getHours())
					+ ":"
					+ String.format("%02d", d.getMinutes())
					+ " - "
					+ String.format("%02d", d.getDate())
					+ "/"
					+ String.format("%02d", (d.getMonth() + 1)) + "/" + (d
					.getYear() + 1900)) : "Not recently updated");
		}
		Button btnStorico = (Button) v.findViewById(R.id.btnStorico);
		btnStorico.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.wtf("TAG", "Pressed");
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.enter, R.anim.exit);

				ft.replace(R.id.container, new DetailsFragment(obj),
						getString(R.string.storico_fragment))
						.addToBackStack(null).commit();
				dismiss();

			}
		});
		Button btnAnnulla = (Button) v.findViewById(R.id.btnDel);
		btnAnnulla.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		int c = getActivity().getResources().getColor(R.color.button_pressed);
		getDialog().setTitle(
				Html.fromHtml("<font color='#E84E26'>" + obj.getName()
						+ "</font>"));

		if (getDialog()
				.getWindow()
				.getDecorView()
				.findViewById(
						getActivity().getResources().getIdentifier(
								"titleDivider", "id", "android")) != null) {
			getDialog()
					.getWindow()
					.getDecorView()
					.findViewById(
							getActivity().getResources().getIdentifier(
									"titleDivider", "id", "android"))
					.setBackgroundColor(c);
			Rect displayRectangle = new Rect();
			Window window = getActivity().getWindow();
			window.getDecorView()
					.getWindowVisibleDisplayFrame(displayRectangle);
			v.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
		}
		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);

		getDialog().setFeatureDrawableResource(
				Window.FEATURE_LEFT_ICON,
				obj instanceof Parking ? R.drawable.ic_parcheggi
						: R.drawable.ic_vie);

	}
}
