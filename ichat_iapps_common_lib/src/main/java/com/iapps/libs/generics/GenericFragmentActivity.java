package com.iapps.libs.generics;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

public class GenericFragmentActivity extends RoboSherlockFragmentActivity {
	protected GenericFragmentActivity getActivityInstance(){
		return this;
	}
}
