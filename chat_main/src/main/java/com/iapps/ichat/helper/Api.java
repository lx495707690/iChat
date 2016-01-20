package com.iapps.ichat.helper;

import android.content.Context;

import com.iapps.ichat.helper.UserInfoManager;

public class Api {

	private UserInfoManager infoManager;
	private static com.iapps.ichat.helper.Api _api = null;

	// Production
//	private final String BASE_URL = "http://43.231.128.231:8889/api/index.php/";
//	public static final String WHICH_SERVER = "Production";

	// Development
	private final String BASE_URL = "http://43.231.128.231:8889/api/index.php/";
//	private final String BASE_URL = "http://192.168.1.171:8888/isan/api/index.php/";
	public static final String WHICH_SERVER = "Development";

	// Staging / UAT
//	private final String BASE_URL = "http://43.231.128.231:8889/api/index.php/";
//	 public static final String WHICH_SERVER = "Staging";

	private final String LOGIN = "user/login";
	private final String PROFILE = "agent/profile";
	private final String REG_INIT = "guess/register/init";
	private final String USER_PROFILE = "agent/get/profile/%s/%s";
	private final String VERIFY_OTP_USER = "guess/register/mobile/verify";
	private final String RESEND_OTP_USER = "guess/register/mobile/otp/resend";
	private final String REGISTRATION_COMPLETE = "guess/register/complete";
	private final String UPLOAD_IC = "agent/account/upload/ic";
	private final String UPLOAD_PROFILE_PICTURE = "agent/account/upload/profile_picture";
	private final String VERIFY_ACCOUNT = "agent/account/verify";
	private final String COMMON_SERVICE_CONFIG = "common/service/config";
	private final String ADD_RECIPIENT = "user/recipient/add";
	private final String RECIPIENT_LIST = "user/recipient/list";
	private final String ADD_VOUCHER = "user/transaction/voucher/add";
	private final String GET_VOUCHER = "agent/transaction/voucher/get";
	private final String VOUCHER_CONFIRMATION = "agent/transaction/voucher/confirm";
	private final String VOUCHER_CASHOUT = "user/transaction/voucher/tobecashedout";
	private final String VOUCHER_HISTORY = "user/transaction/voucher/history";
	private final String VOUCHER_RECEIVE_MONEY = "user/transaction/voucher/tobecashedout";

	// Production
//	 private final String BASE_URL =
//	 "q7CDciYt06T81w3Roqnc3TVTupK1y5WohdulwkA1Kv+Vn4sbpjiIWjfveTY7aw5FdMv4NOWAQwJPfPhQuVWYow==";
//	 private final String LOGIN =
//	 "xnCAuIfbhfxmd4aGmOZEeO8gdHwQHtj4wTen8Ts8AO0=";


	public static com.iapps.ichat.helper.Api getInstance(Context context) {
		if (_api == null) {
			_api = new com.iapps.ichat.helper.Api();
			_api.openSecurity(context);
		}

		return _api;
	}

//	 private String[] arr = {
//	 CART_EDIT_SENJA,CART_REMOVE_SENJA,POOL_SENJA_GET,POOL_SENJA_ADD,
//	 BASE_URL, BASE_URL_SETTING,
//	 FORGOT_PIN,
//	 ACCOUNT_CITIZENSHIP,
//	 ACCOUNT_CONTACTMODE, V2_ACCOUNT_CREATE, ACCOUNT_SIGNUP, ACCOUNT_DELETE,
//	 ACCOUNT_EMP,
//	 ACCOUNT_INTERESTS, ACCOUNT_INVOICE, V2_ACCOUNT_LOGIN, ACCOUNT_RACE,
//	 ACCOUNT_VERIFY,
//	 ACCOUNT_VERIFY_SINGPASS, ACCOUNT_OTP_RESEND, ACCOUNT_VERSION,
//	 ADDON_ADD_TO_CART,
//	 ADDON_CART_DELETE, ADDON_LISTING, ADDRESS_BY_POSTAL, CART_CLEAR,
//	 CART_EDIT_FACILITIES,
//	 CART_EDIT_FEATURED, CART_EDIT_GYM, CART_EDIT_MISC, CART_EDIT_POOL,
//	 CART_EXTEND_EXPIRY,
//	 CART_GET, CART_GET_EXPRESS, CART_GET_INFO, CART_REMOVE_FACILITY,
//	 CART_REMOVE_FEATURED,
//	 CART_REMOVE_GYM, CART_REMOVE_POOL, CART_REBOOK, V2_CHANGE_PASSWORD,
//	 CHECK_PROFILEPASSWORD,
//	 CHECKOUT_CREDIT_CARD, V2_CHECKOUT_WALLET, CREATE_ACTIVESG,
//             V2_CREATE_SUPPLEMENTARY_ACCOUNT,
//	 EDIT_EMAIL, EDIT_MOBILE, V2_EWALLET_PIN_CHANGE, V2_EWALLET_PIN_SET,
//	 EWALLET_TOPUP,
//	 EWALLET_TOPUP_LIST, FACILITY_ACTIVITY_ALL, FACILITY_ACTIVITY_TYPE_ALL,
//	 FACILITY_BOOKING_ADD, FACILITY_FAV_ADD, FACILITY_FAV_DELETE,
//	 FACILITY_FAV_GET,
//	 FACILITY_INFO, FACILITY_ITEMS, FACILITY_MAX_DATE, FACILITY_MYBOOK_ALL,
//	 FACILITY_MYBOOK_INFO, FACILITY_MYBOOK_MONTHLY, FACILITY_MYBOOK_PREV,
//	 FACILITY_MYBOOK_UPCOMING, FACILITY_NEARBY, FACILITY_PREF_ADD,
//	 FACILITY_PREF_DELETE,
//	 FACILITY_PREF_GET, FACILITY_RECOMMENDED, FACILITY_SEARCH, FACILITY_SLOT,
//	 FACILITY_VENUE, FACILITY_VENUE_ALL, FACILITY_VENUE_INFO, FORGOT_PASSWORD,
//	 GET_ACCOUNT_LISTING, GET_EWALLET_LISTING, GET_NEWSFEED_LISTING,
//	 GET_NEWSFEED_DETAILS,
//	 POST_NEWSFEED_FAV_GET, POST_NEWSFEED_FAV_ADD, POST_NEWSFEED_FAV_REMOVE,
//	 PROG_RECOMMENDED, PROG_VENUES, PROG_ACTIVITIES, PROG_NEARBY, PROG_SEARCH,
//	 PROG_INFO,
//	 PROG_INFO_MORE, PROG_FAV_GET, PROG_FAV_ADD, PROG_FAV_REMOVE,
//	 PROG_FAV_CHECK,
//	 PROG_BOOKING_ADD, PROG_CART_REMOVE, PROG_UPDATE_GENDER_DOB,
//	 PROG_ADD_WAITING_LIST,
//	 PROG_MYBOOK_ALL, PROG_MYBOOK_UPCOMING, PROG_MYBOOK_PREV,
//	 PROG_MYBOOK_MONTHLY,
//	 PROG_MYBOOK_INFO, PROG_UPDATE_SPORTS_INTEREST, PASS_DETAILS, POOL_GET,
//	 POOL_ADD,
//	 POOL_FEATURED_GET, POOL_FEATURED_ADD, GYM_GET, GYM_ADD, GYM_CHECKOUT,
//	 GYM_GET_VENUE,
//	 GYM_IS_IN_GYM, INFO, LINK_FACEBOOK, MAP_IMAGE_STATIC,
//	 //MYVIRTUALCARD_INFORMATION,
//	 NOTIF_GET, OCBC_URL, OCBC_URL_INFO, OCBC_URL_PAYMENT, OCBC_URL_PROCESS,
//	 OCBC_RETURN_BASE, OCBC_RETURN_URL, OCBC_RETURN_URL_SUCCESS, PASS_GET,
//	 PROFILE_INFORMATION, REGISTER_DEVICETOKEN, UNREGISTER_DEVICETOKEN,
//             V2_SET_PASSWORD,
//	 SIGNUP_IDTYPE, SWITCH_ACCOUNT, UPDATE_MEMBERSHIP_STATUS, UPDATE_PROFILE,
//	 VERIFY_OTP_EMAILMOBILE, WALLET_GET, WALLET_GET_HISTORY,
//	 SETTING_USER_GUIDE,
//	 SETTING_FAQ, SETTING_TERMS, SETTING_COMPLIMENT, SETTING_PRIVACY,
//	 SETTING_SINGPASS };
//
//	 public void print() {
//	 for (int i = 0; i < arr.length; i++) {
//	 Log.d(Constants.LOG,
//             "Normal : " + arr[i] + "\nEncrypt : " + infoManager.encryptText(arr[i]));
//	 }
//	 }

	private void openSecurity(Context context) {
		this.infoManager = UserInfoManager.getInstance(context);
	}

	private String decrypt(String text) {
		// Development
		return text;

		// Production
		 //if (infoManager != null) { return infoManager.decryptText(text); }
		 //return null;
	}

	public String getBaseUrl() {
		return decrypt(BASE_URL);
	}

	public String postLogin() {
		return decrypt(BASE_URL + LOGIN);
	}

	public String getProfile() {
		return decrypt(BASE_URL + PROFILE);
	}

	public String postRegInit() {
		return decrypt(BASE_URL + REG_INIT);
	}

	public String getUserProfile() {
		return decrypt(BASE_URL + USER_PROFILE);
	}

	public String postVerifyOTPUser() {
		return decrypt(BASE_URL + VERIFY_OTP_USER);
	}

	public String postResendOTPUser() {
		return decrypt(BASE_URL + RESEND_OTP_USER);
	}

	public String postRegistrationComplete() {
		return decrypt(BASE_URL + REGISTRATION_COMPLETE);
	}

	public String postUploadIC() {
		return decrypt(BASE_URL + UPLOAD_IC);
	}

	public String postUploadProfilePicture() {
		return decrypt(BASE_URL + UPLOAD_PROFILE_PICTURE);
	}

	public String postVerifyAccount() {
		return decrypt(BASE_URL + VERIFY_ACCOUNT);
	}

	public String postCommonServiceConfig() {
		return decrypt(BASE_URL + COMMON_SERVICE_CONFIG);
	}

	public String postAddRecipient() {
		return decrypt(BASE_URL + ADD_RECIPIENT);
	}

	public String getRecipientList() {
		return decrypt(BASE_URL + RECIPIENT_LIST);
	}

	public String postAddVoucher() {
		return decrypt(BASE_URL + ADD_VOUCHER);
	}

	public String postGetVoucher() {
		return decrypt(BASE_URL + GET_VOUCHER);
	}

	public String postGetReceiveMoney() {
		return decrypt(BASE_URL + VOUCHER_RECEIVE_MONEY);
	}

	public String postVoucherConfirmation() {
		return decrypt(BASE_URL + VOUCHER_CONFIRMATION);
	}

	public String postVoucherCashPut() {
		return decrypt(BASE_URL + VOUCHER_CASHOUT);
	}

	public String getVoucherHistory() {
		return decrypt(BASE_URL + VOUCHER_HISTORY);
	}

}
