package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.Arrays;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	private int mCurrent = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null)
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new MapFragment(), "Mappa").commit();
		else
			mCurrent = savedInstanceState.getInt("current");
		
		
		/* ***Drawer Settings*** */
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		/** Used to open the Drawer by clicking the actionBar */
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		String[] strings = { "Mappa", "Le mie segnalazioni", "Logout" };
		mDrawerList.setAdapter(new DrawerArrayAdapter(getApplicationContext(),
				strings));

		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FragmentManager fm = getSupportFragmentManager();
				if (arg2 == 0
						&& !(fm.findFragmentById(R.id.container) instanceof MapFragment)) {
					getSupportActionBar().setTitle("Mappa");
					FragmentTransaction ft = getSupportFragmentManager()
							.beginTransaction();
					ft.setCustomAnimations(R.anim.enter, R.anim.exit);
					ft.replace(R.id.container, new MapFragment(),
							getString(R.string.map_fragment))
							.addToBackStack(null).commit();
				} else if (arg2 == 1
						&& !(fm.findFragmentById(R.id.container) instanceof StoricoAgenteFragment)) {
					getSupportActionBar().setTitle("Storico");
					FragmentTransaction ft = getSupportFragmentManager()
							.beginTransaction();
					ft.setCustomAnimations(R.anim.enter, R.anim.exit);
					ft.replace(R.id.container, new StoricoAgenteFragment(),
							getString(R.string.storico_fragment))
							.addToBackStack(null).commit();
				} else if (arg2 == 2) {
					logout();

				}

				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});
		mTitle = getTitle();
	}

	/**
	 * method used for logging out of the application, which opens a popup for asking confirmation of the action
	 */
	private void logout() {
		new ConfirmPopup("Logout",
				"Stai per effettuare il logout. Continuare?",
				R.drawable.ic_logout_confirm) {

			@Override
			public void confirm() {
				// TODO Auto-generated method stub

				getSupportActionBar().setTitle("Login");
				getSharedPreferences("Login", MODE_PRIVATE).edit()
						.remove("User").apply();
				Intent i = new Intent(getActivity(), LoginActivity.class);
				startActivity(i);
				finish();
			}
		}.show(getSupportFragmentManager(), null);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("current", mCurrent);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * open the drawer when the "menu" key is pressed
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.openDrawer(mDrawerList);
			} else
				mDrawerLayout.closeDrawer(mDrawerList);
			return true;
		}
		return super.onKeyDown(keyCode, e);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ArrayAdapter used to populate the Drawer
	 *
	 */
	public static class DrawerArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> values;

		public DrawerArrayAdapter(Context context, String[] values) {
			super(context, R.layout.drawerrow, values);
			this.context = context;
			this.values = new ArrayList<String>(Arrays.asList(values));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.drawerrow, parent, false);
			TextView textView = (TextView) rowView
					.findViewById(R.id.textDrawer);
			textView.setText(values.get(position));
			ImageView img = (ImageView) rowView.findViewById(R.id.iconDrawer);
			switch (position) {
			case 0:
				img.setImageDrawable(rowView.getResources().getDrawable(
						R.drawable.ic_map));
				break;
			case 1:
				img.setImageDrawable(rowView.getResources().getDrawable(
						R.drawable.ic_storico));
				break;
			case 2:
				img.setImageDrawable(rowView.getResources().getDrawable(
						R.drawable.ic_logout));
				break;
			}
			return rowView;
		}
	}

}
