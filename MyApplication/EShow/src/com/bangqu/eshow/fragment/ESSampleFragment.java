package com.bangqu.eshow.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 全屏的Fragment
 *
 */
public class ESSampleFragment extends DialogFragment {

	private View mContentView;

	public ESSampleFragment() {
		super();
	}
	
	/**
	 * Create a new instance of AbSampleFragment.
	 */
	public static ESSampleFragment newInstance() {
		ESSampleFragment f = new ESSampleFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mContentView;
	}

	public View getContentView() {
		return mContentView;
	}

	public void setContentView(View mContentView) {
		this.mContentView = mContentView;
	}

}
