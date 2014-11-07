package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/**
 * class to handle the creation of confirmation dialogs similar to AlertDialogs
 * @author Michele Armellini
 */
public abstract class ConfirmPopup extends DialogFragment {

	private int icon;
	private String title;
	private String message;

	public ConfirmPopup(String title, String message, int icon) {
		this.title = title;
		this.message = message;
		this.icon = icon;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);

		View v = inflater.inflate(R.layout.fragment_popup, container, false);
		// setRetainInstance(true);

		TextView tv = (TextView) v.findViewById(R.id.txtLastData);
		tv.setText(message);
		v.findViewById(R.id.txtLastDataHeader).setVisibility(View.GONE);
		Button btnStorico = (Button) v.findViewById(R.id.btnStorico);
		btnStorico.setText(R.string.button_continua);
		btnStorico.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirm();
				dismiss();
			}
		});
		Button btnAnnulla = (Button) v.findViewById(R.id.btnDel);
		btnAnnulla.setText(R.string.button_annulla);
		btnAnnulla.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		int c = getActivity().getResources().getColor(R.color.button_pressed);
		getDialog().setTitle(
				Html.fromHtml("<font color='#E84E26'>" + title + "</font>"));

		if (getDialog().getWindow().getDecorView().findViewById(
						getActivity().getResources().getIdentifier("titleDivider", "id", "android")) != null) {
			/*  sets the color of the line under the title*/
			getDialog().getWindow().getDecorView().findViewById(
					getActivity().getResources().getIdentifier("titleDivider", "id", "android")).setBackgroundColor(c);
			/*  fixes the dimension problem in old API*/
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

		getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, icon);

	}

	public abstract void confirm();
}
