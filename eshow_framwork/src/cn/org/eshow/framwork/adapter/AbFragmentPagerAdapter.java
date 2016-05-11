/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.org.eshow.framwork.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.ArrayList;
/**
 *一个通用的Fragment适配器
 */
public class AbFragmentPagerAdapter extends android.support.v13.app.FragmentPagerAdapter {

	/** The m fragment list. */
	private ArrayList<Fragment> mFragmentList = null;

	/**
	 * Instantiates a new ab fragment pager adapter.
	 * @param mFragmentManager the m fragment manager
	 * @param fragmentList the fragment list
	 */
	public AbFragmentPagerAdapter(FragmentManager mFragmentManager, ArrayList<Fragment> fragmentList) {
		super(mFragmentManager);
		mFragmentList = fragmentList;
	}

	/**
	 * 描述：获取数量.
	 *
	 * @return the count
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mFragmentList.size();
	}

	/**
	 * 描述：获取索引位置的Fragment.
	 *
	 * @param position the position
	 * @return the item
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;
		if (position < mFragmentList.size()){
			fragment = mFragmentList.get(position);
		}else{
			fragment = mFragmentList.get(0);
		}
		return fragment;

	}
}
