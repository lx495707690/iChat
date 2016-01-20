package com.iapps.ichat.helper;

import com.iapps.libs.helpers.BaseConstants;

public class Constants
	extends BaseConstants {

	public static final boolean IS_DEBUGGING = true;

	// GCM id for push notification
	public static final String GCM_ID = "258364618683";

	// App's details
	public static final String PLATFORM = "android";
	public static final String PUBLIC_USER = "PU";

	public static final String LOG = "result";
	public static final String CHANNEL = "public_user";
	public static final int WIDTH_DRAWER_AREA = 200;
	public static final int WIDTH_DRAWER_TOUCH = 400;

	public static final int PAGE_DEFAULT = 1;
	public static final int PAGE_LIMIT = 10;

	public static final String MALE_ = "Male";
	public static final String FEMALE_ = "Female";

	public static final String CURRENT = "current";
	public static final String YES = "yes";
	public static final String NO = "no";
	public static final String TRUE = "Y";
	public static final String FALSE = "N";
	public static final String TRUE_ = "TRUE";
	public static final String FALSE_ = "FALSE";
	public static final String NULL = "null";
	public static final String FAIL = "F";
	public static final String SUCCESS = "S";

	public static final String DATE_MD = "EEE, dd MMM";
	public static final String DATE_MDY = DATE_MD + " yyyy";
	public static final String DATE_EMDY = "EEE, " + DATE_MDY;
	public static final String DATE_JSON = "yyyy-MM-dd";
	public static final String DATE_TIME_JSON = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_hMA = "h:mm a ";
	public static final String TIME_hA = "h a";
	public static final String TIME_hM = "h:mm";
	public static final String TIME_MS = "mm:ss";
	public static final String TIME_JSON_HM = "HH:mm";
	public static final String TIME_JSON_HMS = "HH:mm:ss";
	public static final String DATE_MONTH = "dd MMM yyyy";
	public static final String TIME_JSON_HMS_SSS = "HH:mm:ss.SSS";
	public static final String TIME_JSON_SHORTEN = "EEE, d MMM yyyy HH:mm:ss";
	public static final String DATE_MDYhMA = DATE_MDY +" "+ TIME_hMA;

	public static final int STATUS_OAUTH_INVALID = 403;
	public static final int STATUS_LOGIN_SUCCESS = 1007;
	public static final int STATUS_LOGIN_FAILED = 1008;

	public static final int STATUS_REGISTER_USER_SUCCESS = 1000;
	public static final int STATUS_REGISTER_USER_ALREADY_REGISTERED = 1001;
	public static final int STATUS_REGISTER_USER_PROCEED_TO_PROFILE = 1013;
	public static final int STATUS_REGISTER_USER_FAILED = 1002;
	public static final int STATUS_REGISTER_USER_INVALID_CODE = 2000;

	public static final int STATUS_GET_USER_PROFILE_SUCCESS = 1009;
	public static final int STATUS_GET_USER_PROFILE_MOBILE_NO_NOT_EXIST = 1003;
	public static final int STATUS_GET_USER_PROFILE_FAILED = 1010;

	public static final int STATUS_USER_GUESS = 1;
	public static final int STATUS_USER_UNVERIFIED = 2;
	public static final int STATUS_USER_ACTIVE = 3;

	public static final int STATUS_OTP_MOBILE_NO_VERIFIED = 1005;
	public static final int STATUS_OTP_MOBILE_NO_DOES_NOT_EXIST = 1003;
	public static final int STATUS_OTP_FAILED_VERIFY_MOBILE_NO = 1004;

	public static final int STATUS_RESEND_OTP_SENT = 1000;
	public static final int STATUS_RESEND_OTP_MOBILE_NO_DOES_NOT_EXIST = 1003;
	public static final int STATUS_RESEND_OTP_FAILED = 1016;

	public static final int STATUS_REGISTRATION_COMPLETED = 1006;
	public static final int STATUS_REGISTRATION_FAILED = 1002;

	public static final int STATUS_VERIFY_USER_SUCCESSFULLY = 1014;
	public static final int STATUS_VERIFY_USER_UPDATE_FAILED = 1012;
	public static final int STATUS_VERIFY_USER_FAILED = 1015;

	public static final int STATUS_UPLOAD_PHOTO_SUCCESSFULLY = 1011;
	public static final int STATUS_UPLOAD_PHOTO_FAILED = 1012;

	public static final int STATUS_GET_CONFIG_SUCCESS = 1021;
	public static final int STATUS_GET_CONFIG_FAILED = 1022;

	public static final int STATUS_GET_VOUCHER_SUCCESS = 1051;
	public static final int STATUS_INVALID_VOUCHER_STATUS = 1034;
	public static final int STATUS_GET_VOUCHER_FAILED = 1052;

	public static final int STATUS_PURCHASE_VOUCHER_SUCCESS = 1031;
	public static final int STATUS_INVALID_RECIPIENT = 1028;
	public static final int STATUS_FAILED_TO_ADD_TRANSACTION = 1030;
	public static final int STATUS_FAILED_TO_PURCHASE_VOUCHER = 1032;
	public static final int STATUS_INVALID_PURCHASE_ROLE = 1033;
	public static final int STATUS_INVALID_TRANSACTION_TYPE = 1035;
	public static final int STATUS_INVALID_TRANSACTION_STATUS = 1036;
	public static final int STATUS_FAILED_TO_GET_INCREMENT_ID = 1037;
	public static final int STATUS_INVALID_SERVICE_CONFIGURATION = 1038;
	public static final int STATUS_FAILED_TO_SAVE_CONFIGURATION = 1039;
	public static final int STATUS_FAILED_TO_SAVE_VOUCHER = 1040;

	public static final int STATUS_GET_RECIPIENT_SUCCESS = 1025;
	public static final int STATUS_FAILED_TO_GET_RECIPIENT = 1026;

	public static final int STATUS_ADD_RECIPIENT_SUCCESS = 1023;
	public static final int STATUS_FAILED_TO_ADD_RECIPIENT = 1024;
	public static final int STATUS_RECIPIENT_ALREADY_EXIST = 1027;

	public static final int STATUS_UPDATE_TRANSACTION_SUCCESS = 1046;

	// Gender
	public static final String SEX_MALE = "Male";
	public static final String SEX_FEMALE = "Female";
	public static final String CODE_MALE = "M", CODE_FEMALE = "F";

	// Action Code
	public static final int ACTION_WEB = 501;
	public static final int ACTION_EMAIL = 502;
	public static final int ACTION_CALL = 503;
	public static final int ACTION_ADDRESS = 504;
	public static final int ACTION_YOUTUBE = 505;

	// Regex
	// Alpha numeric input
	public static final String REGEX_PASSWORD = "([0-9]+[a-zA-Z][0-9a-zA-Z]*)|([a-zA-Z]+[0-9][0-9a-zA-Z]*)";
	public static final String REGEX_URL_ENCODE = "%[A-Z0-9]{2}";
	public static final String[] RESTRICTED_CHAR = {
			"\'", "\"", ";", "<", ">", "=", "(", ")"
	};

	// Websocket server
	public static final String WEBSOCKET_HOST = "ws://192.168.1.129:9503";
	public static final String CONNECT_SUCCESSFULLY = "connect_successfully";
	public static final String CONNECT_FAILED = "connect_failed";
	public static final String CONNECT_CLOSED = "connect_closed";

	//command
	public static final String CMD_SENDMESSAGE = "msg_send";
	public static final String CMD_RECEIVEMESSAGE = "msg_receive";

	//get cmd from server
	public static final String CMD_LOGIN = "login";
	public static final String CMD_MESSAGE = "message";
	public static final String CMD_CONNECT = "connect";


	//DB
	public static final String DB_CHAT = "db_chat";
	public static final String DB_MESSAGE = "db_message";
	public static final String DB_FRIEND = "db_friend";

}
