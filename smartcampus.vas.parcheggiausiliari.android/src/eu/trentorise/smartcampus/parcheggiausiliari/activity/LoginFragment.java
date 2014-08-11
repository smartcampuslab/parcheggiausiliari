package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {
	
	EditText tvUser;
	EditText tvPass;
	
	public LoginFragment() {
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		tvUser = (EditText)rootView.findViewById(R.id.editText1);
		tvPass = (EditText)rootView.findViewById(R.id.editText2);
		Button btnLogin = (Button)rootView.findViewById(R.id.button1);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO controllare se i dati sono validi
				saveData();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new MapFragment()).commit();
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		return rootView;
	}
	private void saveData() {
		SharedPreferences sp = getActivity().getPreferences( 0);
		sp.edit().putString("User", tvUser.getText().toString()).apply();
		sp.edit().putString("Pass", tvPass.getText().toString()).apply();
	}
	
	
	
}
