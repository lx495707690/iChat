package com.iapps.ichat.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iapps.ichat.R;
import com.iapps.ichat.model.BeanLog;
import com.iapps.libs.helpers.BaseConstants;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;
import com.iapps.libs.helpers.HTTPAsyncTask;
import com.iapps.libs.objects.Response;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper
	extends BaseHelper {

//	public static void applyOauthToken(HTTPAsyncTask async, Fragment frag) {
//		if (frag == null || async == null)
//			return;
//
//		async.setFragment(frag);
//		async.setCache(false);
//
////		// Pass platform as android
////		if (async.getMethod().equals(Constants.GET)){
////			async.setGetParams(Keys.PLATFORM, Constants.PLATFORM);
////			Log.d(Constants.LOG, "Get");
////		}
////		else if (async.getMethod().equals(Constants.POST)){
////			async.setPostParams(Keys.PLATFORM, Constants.PLATFORM);
////			Log.d(Constants.LOG, "Post");
////		}
//
//		// ICS below can't accept SSL certificate from server
//		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//			async.setEnableSSLCheck(false);
//
//		// Pass lat long if exists
//		if (frag.getActivity() instanceof com.iapps.ichat.helper.GenericActivityiSAN) {
//			com.iapps.ichat.helper.GenericActivityiSAN activity = (GenericActivityiSAN) frag.getActivity();
//			if (activity.isLocationEnabled() && activity.isConnected())
//				if (async.getMethod().equals(BaseConstants.GET)) {
//					if (!com.iapps.ichat.helper.Helper.isEmpty(async.getUrl().toString())) {
//						async.setGetParams(Keys.LAT, Double.toString(activity.getMylocation().getLatitude()));
//						async.setGetParams(Keys.LONG, Double.toString(activity.getMylocation().getLongitude()));
//					}
//					Log.d(Constants.LOG, "Get");
//				}
//				else if (async.getMethod().equals(BaseConstants.POST)) {
//					async.setPostParams(Keys.LAT, Double.toString(activity.getMylocation().getLatitude()));
//					async.setPostParams(Keys.LONG, Double.toString(activity.getMylocation().getLongitude()));
//					Log.d(Constants.LOG, "Post");
//				}
//		}
//		oauth(async, frag.getActivity());
//	}

//	public static void applyOauthToken(HTTPAsyncTask async, Context context) {
//		if (context == null || async == null)
//			return;
//
//		oauth(async, context);
//	}



	public static long getMillis(DateTime curTime, DateTime endTime) {
		// curTime.withZone(endTime.getZone());
		// return new Interval(curTime, endTime).toDurationMillis();
		return Seconds.secondsBetween(curTime.toLocalTime(), endTime.toLocalTime()).getSeconds();
	}

	public static SpannableString textDecorIU(String text) {
		SpannableString content = new SpannableString(text);
		content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
		content.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, text.length(), 0);
		return content;
	}

	public static void intentMap(Context context, String name, String subName, String url) {
		url += "($3,$4)";
		if (!com.iapps.ichat.helper.Helper.isEmpty(name))
			url.replace("$3", name);
		else
			url.replace("$3", "");

		if (!com.iapps.ichat.helper.Helper.isEmpty(subName))
			url.replace("$4", subName);
		else
			url.replace("$4", "");

		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}


	public static void intentCall(Context context, String number) {
		if (com.iapps.ichat.helper.Helper.isEmpty(number)) {
			com.iapps.ichat.helper.Helper.showAlert(context, R.string.alert_no_phone);
			return;
		}

		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + number.replace(" ", "")));
		context.startActivity(intent);
	}

	public static void intentEmail(Context context, String email, String subject) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{
				email
		});
		context.startActivity(Intent.createChooser(emailIntent, "Send mail.."));
	}

	public static void intentWeb(Context context, String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(browserIntent);
	}

	public static void intentPlaystore(Context context, String url) {
		Uri marketUri = Uri.parse(url);
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
		context.startActivity(marketIntent);
	}

	public static void intentPdf(Fragment frag, String url) {
		try {
			Intent intentUrl = new Intent(Intent.ACTION_VIEW);
			intentUrl.setDataAndType(Uri.parse(url), "application/pdf");
			intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			frag.startActivity(intentUrl);
		}
		catch (ActivityNotFoundException e) {
			com.iapps.ichat.helper.Helper.intentWeb(frag.getActivity(), url);
			// Open in web view
			// Now can't
			// if (frag.getActivity() instanceof ActivityHome)
			// ((ActivityHome) frag.getActivity()).setFragment(
			// new FragmentWebview(R.string.ssc_title_receipt,
			// Api.GOOGLE_DOC_PDF + url));
		}
	}


	public static boolean isPreICS() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	public static void setupUIAutoCloseOnSoftKeyBoardBackgroundTouch(final Activity act, View view) {

		//Set up touch listener for non-text box views to hide keyboard.
		if(!(view instanceof EditText)) {

			view.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(act);
					return false;
				}

			});
		}

		//If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUIAutoCloseOnSoftKeyBoardBackgroundTouch(act, innerView);
			}
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



//	public static boolean isValidOauth(Response response, final Context context) {
//		if (response != null) {
//			try {
//				if(Build.VERSION.SDK_INT >= 17){
//					if (context instanceof Activity && ((Activity) context).isFinishing() || ((Activity) context).isDestroyed())
//						return false;
//				}else{
//					if (context instanceof Activity && ((Activity) context).isFinishing())
//						return false;
//				}
//
//
//				if (Constants.IS_DEBUGGING)
//					Log.d(Constants.LOG, response.getContent().toString());
//
//				if (response.getStatusCode() == Constants.STATUS_OAUTH_INVALID) {
//					com.iapps.ichat.helper.Helper.showAlert(context, "", context.getString(R.string.isan_alert_session_expired),
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									if (context instanceof HomeActivity)
//										((HomeActivity) context).logout();
//								}
//							});
//					return false;
//				}
//
//			} catch (Exception e) {
//				return false;
//			}
//		}
//		else
//			return false;
//
//		return true;
//	}


	public static String getCountryNameByCountryCode(String cc){

		if(cc.compareToIgnoreCase("id")==0){
			return "Indonesia";
		}else if(cc.compareToIgnoreCase("my")==0){
			return "Malaysia";
		}

		return "Unknown";
	}

	public static String getGenderSymbol(String s){
		return ((s.compareToIgnoreCase("Male")==0))? "M":"F";
	}

	public static String getGenderFromSymbol(String s){
		return ((s.compareToIgnoreCase("M")==0))? "Male":"Female";
	}

	public static String limitNIKThisDigit(String s, int limit){
		String ori = s;
		String result = "";

		try {
			int start = s.length() - limit;
			s = s.substring(start);
			for(int i = 0; i < start ; i++)result = result + "*";

			result = result + s;
		} catch (Exception e) {
			return ori;
		}

		return result;
	}

	public static void logThisAPI(Context ctx, HTTPAsyncTask apiname, Response response){
		try {
			String shortenClassName = apiname.getClass().toString().substring(apiname.getClass().toString().indexOf("$")+1);
			String params = apiname.getParams().toString();
			String method = apiname.getMethod();
			String header = apiname.getmHeaderParams().toString();
			String fileparam = apiname.getFileParams().toString();
			String byteparam = apiname.getBytesParams().toString();
			String url = apiname.getUrl().toString();
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.TIME_JSON_SHORTEN);
			String dateString = formatter.format(new Date(DateTime.now().getMillis()));
			logEventLocal(ctx, "API", shortenClassName + "\n" +
					"Date : " + dateString + "\n" +
					"URL : " + url + "\n" +
					"Method : " + method + "\n" +
					"Header : " + header + "\n" +
					"Params : " + params + "\n" +
					"File Params : " + fileparam + "\n" +
					"Byte Params : " + byteparam + "\n" +
					"Status Code : " + response.getStatusCode() + "\n" +
					"Response : " + response.getContent().toString());
		} catch (Exception e) {}
	}

	public static void logEventLocal(Context ctx, String type, String event){
		try {
			ArrayList<BeanLog> templog = null;
			try {
				templog = Preference.getInstance(ctx).getLogTrackerSaved();
			} catch (Exception e) {
				if(templog==null) templog = new ArrayList<BeanLog>();
			}
			if(templog==null) templog = new ArrayList<BeanLog>();
			templog.add(new BeanLog(type, event, String.valueOf(DateTime.now().getMillis())));
			Preference.getInstance(ctx).saveLogTrackerSaved(templog);
		} catch (Exception e) {}
	}

	public static String getErrorMessage(Activity act, Response response){

		String message = act.getResources().getString(R.string.conn_timeout); //default message

		try {
			if(response.getStatusCode() == BaseConstants.STATUS_NO_CONNECTION){
                message = act.getResources().getString(R.string.no_connection);
            }
		} catch (Exception e) {}

		return message;
	}

	public static String ExceptionToString(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public static Bitmap loadImageSaveMemory(String imgPath) {
		BitmapFactory.Options options;
		try {
			options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
			return bitmap;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap loadImageFromFilePath(String imgPath) {
		BitmapFactory.Options options;
		try {
			options = new BitmapFactory.Options();
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
			return bitmap;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static int sizeOfBitmap(Bitmap data) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			return data.getRowBytes() * data.getHeight();
		} else {
			return data.getByteCount();
		}
	}


	public static File processImage(Activity activity, ImageView imgButton, String filePath) {
		File avatar = null;
		Bitmap bitmap = null;

		try {

			avatar = new File(filePath);
			Uri uri = Uri.fromFile(avatar);

			if (imgButton != null) {
				if (imgButton.getMeasuredHeight() > 0
						&& imgButton.getMeasuredWidth() > 0) {
					int ratio = imgButton.getMeasuredHeight() > imgButton
							.getMeasuredWidth() ? imgButton.getMeasuredWidth()
							: imgButton.getMeasuredHeight();
					bitmap = BaseUIHelper.getThumbnail(activity, uri, ratio);
				}
				else {
					bitmap = BaseUIHelper.getThumbnail(activity, uri);
				}

				try {
					ExifInterface exif = new ExifInterface(avatar.getPath());
					int exifOrientation = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);

					int rotate = 0;

					switch (exifOrientation) {
						case ExifInterface.ORIENTATION_ROTATE_90:
							rotate = 90;
							break;

						case ExifInterface.ORIENTATION_ROTATE_180:
							rotate = 180;
							break;

						case ExifInterface.ORIENTATION_ROTATE_270:
							rotate = 270;
							break;
					}

					if (rotate != 0) {
						int w = bitmap.getWidth();
						int h = bitmap.getHeight();

						// Setting pre rotate
						Matrix mtx = new Matrix();
						mtx.preRotate(rotate);

						// Rotating Bitmap & convert to ARGB_8888, required by tess
						bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
						bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
					}
				} catch (Exception e) {
					com.iapps.ichat.helper.Helper.logEventLocal(activity, "INFO", com.iapps.ichat.helper.Helper.ExceptionToString(e));
				}

				imgButton.setImageBitmap(bitmap);
			}


			try {
				File dir = new File(filePath);
				if(!dir.exists())
                    dir.mkdirs();
				File file = new File(filePath);
				FileOutputStream fOut = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}


			BaseUIHelper.saveSmallerImage(filePath, BaseConstants.MAX_IMAGE_SIZE,
					BaseConstants.MAX_IMAGE_SIZE);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return avatar;
	}

	public static void confirmCall(Activity activity, String phoneNum) {
		final Activity actv = activity;
		final String num = phoneNum;

		if (!activity.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_TELEPHONY)) {
			com.iapps.ichat.helper.Helper.showAlert(activity, "Phone Number:", phoneNum);
			return;
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				activity);
		// CallListener.createListenerFor(activity);
		// set dialog message
		alertDialogBuilder
				.setMessage(
						activity.getResources().getString(R.string.call)
								+ " " + phoneNum + " ?")
				.setCancelable(false)
				.setPositiveButton(
						activity.getResources().getString(android.R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								CallListener.createListenerFor(actv);
								Intent callIntent = new Intent(
										Intent.ACTION_CALL, Uri.parse("tel:"
										+ num));
								actv.startActivity(callIntent);
							}
						})

				.setNegativeButton(
						activity.getResources().getString(android.R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	public static void sendSMS(Activity activity, String smsBody) {
		sendSMS(activity, smsBody, null);
	}

	public static void sendSMS(Activity activity, String smsBody,
							   String phoneNumber) {

		try {

			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			if (phoneNumber != null && phoneNumber.trim().length() > 0) {
				// sendIntent.setData(Uri.fromParts("sms", phoneNumber, null));
				sendIntent.putExtra("address", phoneNumber);
			}
			if (smsBody != null) {
				sendIntent.putExtra("sms_body", smsBody);
			}
			sendIntent.setType("vnd.android-dir/mms-sms");

			activity.startActivity(sendIntent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(activity, "Unable to send SMS", Toast.LENGTH_SHORT)
					.show();
		}
	}


	public static  String generateTxtMessage(String message,String channalId,String fromId,String toId){
		message = "{\"cmd\":\"message\",\"from\":\""+ fromId +"\",\"to\":\""+ toId + "\",\"channal\":\""+ channalId +"\",\"data\": \"" + message +"\",\"type\":\"text\"}";
		return  message;
	}

	public static  String generateLoginMessage(String account,String pwd,String imgUrl){
		String message = ("{\"cmd\":\"login\",\"username\":\""+ account +"\",\"userpass\":\""+ pwd +"\",\"avatar\":\""+ imgUrl +"\"}");
		return  message;
	}

	public static  String generateGetFriendMessage(String userId){
		String message = "{\"cmd\":\"getFriendList\",\"userId\":\""+ userId +"\"}";
		return  message;
	}


	/**
	 * 汉语拼音转换工具
	 *
	 * @param chinese
	 * @return
	 */
	public static String converterToPinYin(String chinese)
	{
		String pinyinString = "";
		char[] charArray = chinese.toCharArray();
		// 根据需要定制输出格式，我用默认的即可
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		try {
			// 遍历数组，ASC码大于128进行转换
			for (int i = 0; i < charArray.length; i++) {
				if (charArray[i] > 128) {
					if (charArray[i] >= 0x4e00 && charArray[i] <= 0x9fa5) {
						pinyinString += PinyinHelper.toHanyuPinyinStringArray(
								charArray[i], defaultFormat)[0].charAt(0);
					}
					else {
						pinyinString += "?";
					}
				}
				else {
					pinyinString += charArray[i];
				}
			}
			return pinyinString;
		}
		catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isWord(String str)
	{
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		else {
			return true;
		}
	}

	public static boolean isAllWord(String str)
	{
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		else {
			return true;
		}
	}

	public static void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
				} catch (Exception e) {

				}
			}
		}.start();
	}
}
