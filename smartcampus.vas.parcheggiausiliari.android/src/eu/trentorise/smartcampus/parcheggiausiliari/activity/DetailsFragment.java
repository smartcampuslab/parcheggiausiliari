package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.parcheggiausiliari.views.PagerSlidingTabStrip;
import smartcampus.vas.parcheggiausiliari.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailsFragment extends Fragment {
	private PagerSlidingTabStrip tabs;
	private GeoObject obj;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_details, container,
				false);
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		tabs.setTextColor(getResources().getColorStateList(R.color.tab_titles_color));
		pager.setAdapter(new MyPagerAdapter(getFragmentManager()));
		// Bind the tabs to the ViewPager
		tabs.setViewPager(pager);
		return rootView;

	}

	public DetailsFragment(GeoObject obj) {
		// TODO Auto-generated constructor stub
		this.obj = obj;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		private final String[] TITLES = { "Segnala", "Storico" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);

			if (getCount() <= 3)
				tabs.setShouldExpand(true);

		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0)
				return SegnalaFragment.newInstance(obj);
			else
				return new StoricoFragment(obj);
		}

	}
}
