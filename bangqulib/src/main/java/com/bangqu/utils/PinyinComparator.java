package com.bangqu.utils;

import com.bangqu.bean.CityBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CityBean> {

	public int compare(CityBean o1, CityBean o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
