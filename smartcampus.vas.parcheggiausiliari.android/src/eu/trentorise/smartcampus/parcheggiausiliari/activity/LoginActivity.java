package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Activity launched at first startup, which launches MainActivity if the user logs in
 * Also, unless the user logs out, this activity will be "skipped" on next launches
 * 
 * @author Michele Armellini
 */
public class LoginActivity extends ActionBarActivity {

	EditText tvUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
		if (sp.getString("User", null) != null) {
			startMap();
		}
		setContentView(R.layout.activity_login);
		tvUser = (EditText) findViewById(R.id.editText1);
		Button btnLogin = (Button) findViewById(R.id.button1);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!validate()) {
					Toast.makeText(LoginActivity.this, R.string.empty_user, Toast.LENGTH_LONG).show(); 
					return;
				}
				saveData();
				startMap();
			}
		});

	}

	private void startMap() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void saveData() {
		SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
		sp.edit().putString("User", tvUser.getText().toString()).apply();
	}

	private boolean validate() {
		if (tvUser.getText() == null || 
			tvUser.getText().toString() == null ||
			tvUser.getText().toString().trim().length() == 0) 
		{
			return false;
		}
		return true;
	}
}
