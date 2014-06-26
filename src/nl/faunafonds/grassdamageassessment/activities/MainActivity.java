package nl.faunafonds.grassdamageassessment.activities;

import nl.faunafonds.grassdamageassessment.R;
import nl.faunafonds.grassdamageassessment.fragments.FragmentsInterface;
import nl.faunafonds.grassdamageassessment.other.Case;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

public class MainActivity extends FragmentActivity {
	ViewPager Tab;
	TabPagerAdapter TabAdapter;
	ActionBar actionBar;
	
	public static Case theCase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent i = getIntent();
		theCase = (Case) i.getParcelableExtra("case");

		TabAdapter = new TabPagerAdapter(getSupportFragmentManager());

		Tab = (ViewPager) findViewById(R.id.pager);
		Tab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

				actionBar = getActionBar();
				actionBar.setSelectedNavigationItem(position);
				FragmentsInterface fragment = (FragmentsInterface) TabAdapter
						.instantiateItem(Tab, position);
				if (fragment != null) {
					fragment.fragmentBecameVisible();
				}
			}
		});
		Tab.setAdapter(TabAdapter);

		actionBar = getActionBar();
		// Enable Tabs on Action Bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

				Tab.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}
		};
		// Add New Tab
		actionBar.addTab(actionBar.newTab().setText("General Info")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Map")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Damage Areas")
				.setTabListener(tabListener));

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	Intent intent = new Intent(this, LoadCaseActivity.class);
			startActivity(intent);
			finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

}
