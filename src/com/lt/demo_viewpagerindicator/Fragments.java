package com.lt.demo_viewpagerindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragments extends Fragment{

	public static final String BUNDLE_TITLE="title";
	private String mTitle="DefaultValue";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle arguments=getArguments();
		if(arguments!=null){
			mTitle=arguments.getString(BUNDLE_TITLE);
		}
		TextView tv=new TextView(getActivity());
		tv.setText(mTitle);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}
	
	public static Fragments newInstance(String title){
		Bundle bundle=new Bundle();
		bundle.putString(BUNDLE_TITLE, title);
		Fragments fragments=new Fragments();
		fragments.setArguments(bundle);
		return fragments;
	}
	
}
