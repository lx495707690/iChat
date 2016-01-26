package com.iapps.ichat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iapps.ichat.R;
import com.iapps.ichat.activity.LoginActivity;
import com.iapps.ichat.helper.GenericFragmentiChat;
import com.iapps.ichat.helper.UserInfoManager;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;

import roboguice.inject.InjectView;

public class FragmentProfile
	extends GenericFragmentiChat {

	@InjectView(R.id.imgAvatar)
	private ImageView imgAvatar;
	@InjectView(R.id.tvName)
	private TextView tvName;
	@InjectView(R.id.llLogout)
	private LinearLayout llLogout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		home().showBottomBar();
		home().setActionBar(getResources().getString(R.string.iapps));
		init();
	}
	private void init(){

		String avatarUrl = UserInfoManager.getInstance(getActivity()).getAvatar();
		if(!BaseHelper.isEmpty(avatarUrl)){
			BaseUIHelper.loadImageWithPlaceholder(getActivity(), avatarUrl, imgAvatar, R.drawable.default_useravatar);
		}else{
			imgAvatar.setImageResource(R.drawable.default_useravatar);
		}

		tvName.setText(UserInfoManager.getInstance(getActivity()).getAccount());

		llLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				UserInfoManager.getInstance(getActivity()).logout();
				startActivity(new Intent(getActivity(), LoginActivity.class));
				home().finish();
			}
		});
	}
}
