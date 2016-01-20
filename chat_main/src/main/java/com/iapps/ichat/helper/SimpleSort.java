package com.iapps.ichat.helper;

import com.iapps.libs.objects.SimpleBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SimpleSort {

	public void sort(ArrayList<? extends SimpleBean> alBean) {
		Collections.sort(alBean, new CustomComparator());
	}

	public class CustomComparator implements Comparator<SimpleBean> {

		@Override
		public int compare(SimpleBean o1, SimpleBean o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}
}