package com.iapps.ichat.helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.iapps.libs.helpers.CertHelper;
import com.iapps.libs.helpers.SecureProxy;

public class UserInfoManager {

	private final String KEY_ACCESS_TOKEN = "F8BC3";
	private final String KEY_ACCOUNT_ID = "C5D83";
	private final String KEY_COUNTRY = "G8BC3";


	private final String KEY_CLIENT_ID = "client_id";

	private static com.iapps.ichat.helper.UserInfoManager _userInfo = null;
	private static String FILE_NAME = "isan_user_sec";
	private String accessToken;
	private String accountId;
	private String clientId;
	private String countryCode;

	private SharedPreferences prefs;
	private SecureProxy security;

	private UserInfoManager() {
		super();
	}

	public static com.iapps.ichat.helper.UserInfoManager getInstance(Context c) {
		if (_userInfo == null) {
			_userInfo = new com.iapps.ichat.helper.UserInfoManager();
			_userInfo.openPrefs(c.getApplicationContext());
			_userInfo.openSecurity(c);
		}

		return _userInfo;
	}

	private void openPrefs(Context c) {
		this.prefs = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}

	private void openSecurity(Context c) {
		CertHelper ch = new CertHelper();
		@SuppressWarnings("static-access")
		String p = ch.getSHA1Fingerprint(c.getPackageManager(), c.getPackageName());
		this.security = new SecureProxy(p);
	}

	public void saveUserId(String accountId) {
		this.accountId = encryptText(accountId);
		SharedPreferences.Editor editor = this.prefs.edit();
		editor.putString(KEY_ACCOUNT_ID, this.accountId);
		editor.commit();
	}

	public void saveUserInfo(String accessToken, String accountId) {
		this.accessToken = encryptText(accessToken);
		this.accountId = encryptText(accountId);
		SharedPreferences.Editor editor = this.prefs.edit();
		editor.putString(KEY_ACCESS_TOKEN, this.accessToken);
		editor.putString(KEY_ACCOUNT_ID, this.accountId);
		editor.commit();
	}

	public void saveUserInfo(String accessToken) {
		this.accessToken = encryptText(accessToken);
		SharedPreferences.Editor editor = this.prefs.edit();
		editor.putString(KEY_ACCESS_TOKEN, this.accessToken);
		editor.commit();
	}

	public void saveCountry(String countryCode) {
		this.countryCode = encryptText(countryCode);
		SharedPreferences.Editor editor = this.prefs.edit();
		editor.putString(KEY_COUNTRY, this.countryCode);
		editor.commit();
	}

	public void saveClientId(String id) {
//		clientId = encryptText(id);
		clientId = id;
		SharedPreferences.Editor editor = this.prefs.edit();
		editor.putString(KEY_CLIENT_ID, clientId);
		editor.commit();
	}

	public String getClientId() {
		if (clientId == null) {
			this.clientId = this.prefs.getString(KEY_CLIENT_ID, null);
		}
//		return decryptText(clientId);
		return clientId;
	}

	public String getAuthToken() {
		if (accessToken == null) {
			this.accessToken = this.prefs.getString(KEY_ACCESS_TOKEN, null);

			if (accessToken == null)
				return null;
		}
		return decryptText(accessToken);
	}

	public String getAccountId() {
		if (accountId == null) {
			this.accountId = this.prefs.getString(KEY_ACCOUNT_ID, null);
		}
		return decryptText(accountId);
	}

	public String getCountryCode() {
		if (countryCode == null) {
			this.countryCode = this.prefs.getString(KEY_COUNTRY, null);
		}
		return decryptText(countryCode);
	}

	public void logout() {
		prefs.edit().clear().commit();
		_userInfo = null;
	}

	public boolean isSignedIn() {
		if (this.getAccountId() != null && this.getAuthToken() != null) { return true; }
		return false;
	}

	public String encryptText(String text) {
		return security.encrypt(text);
	}

	public String decryptText(String text) {
		return security.decrypt(text);
	}

}
