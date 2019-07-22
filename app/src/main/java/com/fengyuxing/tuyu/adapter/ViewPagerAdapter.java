package com.fengyuxing.tuyu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by LGJ on 2018/5/30.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> fragments;
	private String[] titles;

	public ViewPagerAdapter(FragmentManager fm, String[] titles, List<Fragment> fragments) {
		super(fm);
		this.titles = titles;
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Log.e("getPageTitle","titles.length="+titles.length+"  position="+position);
		return titles[position];

	}

	@Override
	public int getCount() {
		return fragments.size();
	}




}
