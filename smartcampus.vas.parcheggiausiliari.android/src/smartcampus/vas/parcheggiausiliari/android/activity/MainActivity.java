package smartcampus.vas.parcheggiausiliari.android.activity;

import java.util.ArrayList;
import java.util.Arrays;

import smartcampus.vas.parcheggiausiliari.android.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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

		if (savedInstanceState != null)
			mCurrent = savedInstanceState.getInt("current");

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// to remove the application logo in the drawer Title
		getSupportActionBar().setIcon(
				new ColorDrawable(android.R.color.transparent));
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

		/**															
        *
        */

		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerList.setAdapter(new DrawerArrayAdapter(getApplicationContext(),
				getResources().getStringArray(R.array.drawer_entries_strings)));

		/**															
        *
        */

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		String[] strings = { "Mappa", "Storico", "Logout" };
		mDrawerList.setAdapter(new DrawerArrayAdapter(getApplicationContext(),
				strings));

		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 != mCurrent)
					if (arg2 == 0) {
						// getSupportFragmentManager().beginTransaction().replace(R.id.container,
						// new MapFragment(getApplicationContext())).commit();
						// Animazione
						getSupportActionBar().setTitle("Mappa");
						FragmentTransaction ft = getSupportFragmentManager()
								.beginTransaction();
						ft.setCustomAnimations(R.anim.enter, R.anim.exit);
						ft.replace(R.id.container, new MapFragment(),
								getString(R.string.map_fragment))
								.addToBackStack(null).commit();
					} else if (arg2 == 1) {
						getSupportActionBar().setTitle("Storico");
						FragmentTransaction ft = getSupportFragmentManager()
								.beginTransaction();
						ft.setCustomAnimations(R.anim.enter, R.anim.exit);
						ft.replace(R.id.container, new StoricoFragment(),
								getString(R.string.storico_fragment))
								.addToBackStack(null).commit();
					} else if (arg2 == 2) {
						getSupportActionBar().setTitle("Login");
						FragmentTransaction ft = getSupportFragmentManager()
								.beginTransaction();
						ft.setCustomAnimations(R.anim.enter, R.anim.exit);
						ft.replace(R.id.container, new Fragment_prova(),
								getString(R.string.login_fragment))
								.addToBackStack(null).commit();
					}

				mDrawerLayout.closeDrawer(mDrawerList);
				mCurrent = arg2;
			}
		});
		mTitle = getTitle();

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new MapFragment(), "Mappa")
					.addToBackStack(null).commit();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("current", mCurrent);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// your action...

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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		menu.setGroupVisible(0, false);
		return true;
	}

	public static class DrawerArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> values;

		public DrawerArrayAdapter(Context context, ArrayList<String> values) {
			super(context, R.layout.drawerrow, values);
			this.context = context;
			this.values = values;
		}

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
			TextView textView = (TextView) rowView.findViewById(R.id.txt1);
			textView.setText(values.get(position));

			return rowView;
		}
	}

}
