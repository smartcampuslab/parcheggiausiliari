package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.Arrays;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		// mDrawerLayout.setBackgroundColor(Color.parseColor("#E15829"));
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		/**															
        *
        */
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		String[] strings = { "Mappa", "Le mie segnalazioni", "Logout" };
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
						ft.replace(R.id.container, new StoricoAgenteFragment(),
								getString(R.string.storico_fragment))
								.addToBackStack(null).commit();
					} else if (arg2 == 2) {
						getSupportActionBar().setTitle("Login");
						logout();
						FragmentTransaction ft = getSupportFragmentManager()
								.beginTransaction();
						ft.setCustomAnimations(R.anim.enter, R.anim.exit);
						ft.replace(R.id.container, new LoginFragment(),
								getString(R.string.logout_fragment)).commit();
					}

				mDrawerLayout.closeDrawer(mDrawerList);
				mCurrent = arg2;
			}
		});
		mTitle = getTitle();

		if (savedInstanceState == null) {

			SharedPreferences sp = getPreferences(MODE_PRIVATE);
			if (sp.getString("User", null) == null) {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new LoginFragment()).commit();
			} else {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new MapFragment(), "Mappa")
						.commit();
			}
		}

	}

	protected void logout() {
		getPreferences(MODE_PRIVATE).edit().remove("User").apply();
		getPreferences(MODE_PRIVATE).edit().remove("Pass").apply();
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
		    //Used to put dark icons on light action bar

		    //Create the search view
		    final SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
		    searchView.setQueryHint("Search");


		    menu.add(Menu.NONE,Menu.NONE,1,"Search")
		        .setIcon(android.R.drawable.ic_search_category_default)
		        .setActionView(searchView)
		        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		    searchView.setOnQueryTextListener(new OnQueryTextListener() {
		        @Override
		        public boolean onQueryTextChange(String newText) {
		            if (newText.length() > 0) {
		                // Search

		            } else {
		                // Do something when there's no input
		            }
		            return false;
		        }
		        @Override
		        public boolean onQueryTextSubmit(String query) { 

		            InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

		            Toast.makeText(getBaseContext(), "dummy Search", Toast.LENGTH_SHORT).show();
		            setSupportProgressBarIndeterminateVisibility(true);

		            Handler handler = new Handler(); 
		            handler.postDelayed(new Runnable() { 
		                 public void run() { 
		                     setSupportProgressBarIndeterminateVisibility(false);
		                 } 
		            }, 2000);

		            return false; }
		    });

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
