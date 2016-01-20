package com.iapps.libs.generics;

import com.iapps.common_library.R;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class GenericListFragment extends GenericFragment {

	@InjectView(tag="listViewListGeneric") protected ListView lvList;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_generic_list, container, false);
	}
}
