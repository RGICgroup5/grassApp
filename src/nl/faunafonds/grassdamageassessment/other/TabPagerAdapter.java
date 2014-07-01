package nl.faunafonds.grassdamageassessment.other;

import nl.faunafonds.grassdamageassessment.fragments.ResultFragment;
import nl.faunafonds.grassdamageassessment.fragments.InfoFragment;
import nl.faunafonds.grassdamageassessment.fragments.MapFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    public TabPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
        case 0:
            //Fragement for Android Tab
            return new InfoFragment();
        case 1:
           //Fragment for Ios Tab
            return new MapFragment();
        case 2:
            //Fragment for Windows Tab
            return new ResultFragment();
        }
		return null;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3; //No of Tabs
	}


    }