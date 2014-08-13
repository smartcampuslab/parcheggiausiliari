package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.Date;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.popup_fragment, container, false);
		TextView tv = (TextView) v.findViewById(R.id.txtLastData);
		if(Parking.class.isInstance(obj)){
				tv.setText(((Parking)obj).getLastChange() != null ? ((Parking)obj).getLastChange().getAuthor() + " " + new Date(((Parking)obj).getLastChange().getTime()) : "Not recently updated");
		}else{
			tv.setText(((Street)obj).getLastChange() != null ? ((Street)obj).getLastChange().getAuthor() + " " + new Date(((Street)obj).getLastChange().getTime()) : "Not recently updated");
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
				 .addToBackStack(null).commit(); dismiss();
				 
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
		getDialog().setTitle(obj.getName());
		return v;
	}
}
