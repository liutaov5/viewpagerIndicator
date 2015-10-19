package com.lt.demo_viewpagerindicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class MainActivity extends FragmentActivity {

	private List<Fragment> mTabContent=new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private List<String> mDatas = Arrays.asList("短信1", "短信2", "短信3", "短信4",
			"短信5", "短信6", "短信7", "短信8", "短信9");
	
	private ViewPagerIndicator mIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.viewpager_indicator);
        initView();
        initDatas();
        mIndicator.setVisibleTabCount(5);
        mIndicator.setTabItemTitles(mDatas);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager, 0);
    }
	private void initDatas() {
		for(String data:mDatas){
			Fragments fragments=Fragments.newInstance(data);
			mTabContent.add(fragments);
		}
		mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return mTabContent.size();
			}
			
			@Override
			public Fragment getItem(int position) {
				return mTabContent.get(position);
			}
		};
	}
	private void initView() {
		mViewPager=(ViewPager)findViewById(R.id.id_vp);
		mIndicator=(ViewPagerIndicator)findViewById(R.id.id_indicator);
	}
}
