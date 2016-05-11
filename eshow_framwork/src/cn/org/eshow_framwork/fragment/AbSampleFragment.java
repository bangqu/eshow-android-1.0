package cn.org.eshow_framwork.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 全屏的Fragment
 *
 */
public class AbSampleFragment extends DialogFragment {

	private View mContentView;

	public AbSampleFragment() {
		super();
	}
	
	/**
	 * Create a new instance of AbSampleFragment.
	 */
	public static AbSampleFragment newInstance() {
		AbSampleFragment f = new AbSampleFragment();
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
