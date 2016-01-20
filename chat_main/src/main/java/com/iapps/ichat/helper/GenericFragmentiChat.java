package com.iapps.ichat.helper;

import android.os.Bundle;
import android.view.View;

import com.iapps.ichat.activity.HomeActivity;
import com.iapps.libs.generics.GenericFragment;


public class GenericFragmentiChat
	extends GenericFragment {

	private com.iapps.ichat.helper.Api api;
	private com.iapps.ichat.helper.UserInfoManager infoManager;

	public String clientId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.api = com.iapps.ichat.helper.Api.getInstance(getActivity());
		this.infoManager = UserInfoManager.getInstance(getActivity());
		this.clientId = UserInfoManager.getInstance(getActivity()).getClientId();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
//		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
	}

	public Api getApi() {
		return this.api;
	}

	public HomeActivity home() {

		try {
			return (HomeActivity) getActivity();
		} catch (Exception e) {
			return null;
		}
	}

	public UserInfoManager getUserInfo() {
		return infoManager;
	}

}
