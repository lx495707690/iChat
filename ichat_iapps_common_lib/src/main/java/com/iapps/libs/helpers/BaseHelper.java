package com.iapps.libs.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iapps.common_library.R;
import com.iapps.libs.objects.Response;
import com.iapps.libs.views.LoadingCompound;

public class BaseHelper {

	private static final String MIN_AGO = "min ago.";
	private static final String MINS_AGO = "mins ago.";
	private static final String HOUR_AGO = "hour ago.";
	private static final String HOURS_AGO = "hours ago.";
	private static final String DAY_AGO = "day ago.";
	private static final String DAYS_AGO = "days ago.";

	private static final String MIN_LEFT = "min left.";
	private static final String MINS_LEFT = "mins left.";
	private static final String HOUR_LEFT = "hour left.";
	private static final String HOURS_LEFT = "hours left.";
	private static final String DAY_TO_GO = "day to go.";
	private static final String DAYS_TO_GO = "days to go.";
	public static final int REQUEST_EMAIL_CODE = 7221;
	public static final int REQUEST_GET_IMAGE_CODE = 9882;

	/**
	 * Check if a string is empty (length == 0)
	 * 
	 * @param string , to be checked
	 * @return true if empty
	 */
	public static boolean isEmpty(String string) {
		if (string == null || string.trim().length() == 0 || string.compareToIgnoreCase("{}")==0) { return true; }
		return false;
	}

	/**
	 * Change the state of a view to View.GONE
	 * 
	 * @param v , view to be updated
	 */
	public static void goneView(View v) {
		if (v != null) {
			v.setVisibility(View.GONE);
		}

	}

	/**
	 * Change the state of a view to View.VISIBLE
	 * 
	 * @param v , view to be updated
	 */
	public static void visibleView(View v) {
		if (v != null) {
			v.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Change the state of a view to View.INVISIBLE
	 * 
	 * @param v , view to be updated
	 */
	public static void invisibleView(View v) {
		if (v != null) {
			v.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Send email
	 * 
	 * @param activity
	 * @param emailAddresses
	 * @param subject
	 * @param body
	 * @param uri , image or attachment
	 */
	public static void sendEmail(
			Activity activity, String[] emailAddresses, String subject, String body, Uri uri) {
		Intent email = new Intent(Intent.ACTION_SEND);
		if (emailAddresses != null) {
			email.putExtra(Intent.EXTRA_EMAIL, emailAddresses);
		}
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		if (body == null) {
			body = "";
		}

		if (uri != null) {
			email.putExtra(Intent.EXTRA_STREAM, uri);
		}

		email.putExtra(Intent.EXTRA_TEXT, body);

		email.setType("message/rfc822");

		activity.startActivityForResult(Intent.createChooser(email, "Choose an Email client :"),
				REQUEST_EMAIL_CODE);
	}

	/**
	 * Open activity to send an SMS
	 * 
	 * @param activity to start the sms activity from
	 * @param smsBody
	 */
	public static void sendSMS(Activity activity, String smsBody) {
		sendSMS(activity, smsBody, null);
	}

	/**
	 * Open activity to send an SMS, specifying the phone number
	 * 
	 * @param activity
	 * @param smsBody
	 * @param phoneNumber
	 */
	public static void sendSMS(Activity activity, String smsBody, String phoneNumber) {

		try {

			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			if (phoneNumber != null && phoneNumber.trim().length() > 0) {
				sendIntent.putExtra("address", phoneNumber);
			}
			if (smsBody != null) {
				sendIntent.putExtra("sms_body", smsBody);
			}
			sendIntent.setType("vnd.android-dir/mms-sms");

			activity.startActivity(sendIntent);
		}
		catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(activity, "Unable to send SMS", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Show dialog alert
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @return {@link AlertDialog}
	 */
	public static AlertDialog showAlert(
			Context context, String title, String message,
			DialogInterface.OnClickListener listener) {
		if (listener == null) {
			listener = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			};
		}

		AlertDialog d = new AlertDialog.Builder(context).setMessage(message).setTitle(title)
				.setCancelable(true).setNeutralButton(android.R.string.ok, listener).show();
		return d;
	}

	public static AlertDialog showAlert(Context context, String title, String message) {
		return showAlert(context, title, message, null);
	}

	public static AlertDialog showAlert(Context context, String message) {
		return showAlert(context, null, message, null);
	}

	public static AlertDialog showAlert(Context context, int titleResId, int messageResId) {
		String title = null;
		try {
			title = context.getString(titleResId);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String message = null;
		try {
			message = context.getString(messageResId);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return showAlert(context, title, message, null);

	}

	public static AlertDialog showAlert(Context context, int messageResId) {

		String message = null;
		try {
			message = context.getString(messageResId);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return showAlert(context, null, message, null);

	}

	public static AlertDialog.Builder buildAlert(Context context, String title, String message) {
		Builder dlg = new AlertDialog.Builder(context).setMessage(message).setTitle(title)
				.setCancelable(true);
		return dlg;
	}

	public static void showInternetError(Context context) {
		showAlert(context, context.getResources().getString(R.string.iapps__network_error), context
				.getResources().getString(R.string.iapps__no_internet), null);
	}

	public static void showUnknownResponseError(Context context) {
		showAlert(context, context.getResources().getString(R.string.iapps__network_error), context
				.getResources().getString(R.string.iapps__unknown_response), null);
	}

	/**
	 * Handle response object and returns {@link JSONObject}
	 * 
	 * @param response , response object to be handled
	 * @param loading , loading compound to show the error message/ to hide
	 * @return valid {@link JSONObject}
	 */
	public static JSONObject handleResponse(Response response, LoadingCompound loading) {
		return handleResponse(response, loading, false, null);
	}

	/**
	 * Handle response object and returns {@link JSONObject}
	 * 
	 * @param response , response object to be handled
	 * @param shouldDisplayDialog , true if should display error dialog popup
	 * @param context , context being used
	 * @return valid {@link JSONObject}
	 */
	public static JSONObject handleResponse(
			Response response, boolean shouldDisplayDialog, Context context) {
		return handleResponse(response, null, shouldDisplayDialog, context);
	}

	private static JSONObject handleResponse(
			Response response, LoadingCompound loading, boolean shouldDisplayDialog,
			Context context) {
		if (response != null) {
			JSONObject json = response.getContent();
			if (response.getStatusCode() == BaseConstants.STATUS_SUCCESS) {
				return json;
			}
			else if (response.getStatusCode() == BaseConstants.STATUS_TIMEOUT) {
				if (loading != null) {
					loading.showError(null, loading.getContext().getString(R.string.iapps__conn_timeout));
				}
				else if (shouldDisplayDialog && context != null) {
					BaseHelper.showAlert(context, null,
							context.getString(R.string.iapps__conn_timeout));
				}
			}
			else if (response.getStatusCode() == BaseConstants.STATUS_NO_CONNECTION) {
				if (loading != null) {
					loading.showError(null, loading.getContext().getString(R.string.iapps__conn_fail));
				}
				else if (shouldDisplayDialog && context != null) {
					BaseHelper.showAlert(context, null,
							context.getString(R.string.iapps__conn_fail));
				}
			}
			else {

				if(shouldDisplayDialog==false) return json;

				try {
					String message = json.getString(BaseKeys.MESSAGE);
					if (loading != null) {
						loading.showError(null, message);
					}
					else if (shouldDisplayDialog && context != null) {
						BaseHelper.showAlert(context, null, message, null);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
					if (loading != null) {
						loading.showUnknownResponse();
					}
					else if (shouldDisplayDialog && context != null) {
						BaseHelper.showUnknownResponseError(context);
					}
				}

			}
		}
		else {
			if (loading != null) {
				loading.showInternetError();
			}
			else if (shouldDisplayDialog && context != null) {
				BaseHelper.showInternetError(context);
			}
		}
		return null;
	}

	/**
	 * Show required popup message
	 * 
	 * @param context
	 * @param field
	 */
	public static void showRequired(Context context, String field) {
		showAlert(context, context.getResources().getString(R.string.iapps__required_field), field,
				null);
	}

	public static void showRequired(Context context, int resId) {
		try {
			String f = context.getString(resId);
			showRequired(context, f);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isValidEmail(String emailAddress) {
		if (emailAddress != null && !EmailValidator.getInstance().isValid(emailAddress.trim())) { return false; }
		return true;
	}

	public static boolean isSameDay(DateTime first, DateTime second) {
		return DateTimeComparator.getDateOnlyInstance().compare(first, second) == 0 ? true : false;
	}

	public static boolean isEmpty(EditText edt) {
		return isEmpty(edt.getText().toString());
	}

	/**
	 * Whether the target equals 'Y'
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isYes(String target) {
		if (target == null) { return false; }

		if (target.equals(BaseConstants.YES)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Show confirm dialog popup
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param l
	 */
	public static AlertDialog.Builder confirm(
			Context context, String title, String message, final ConfirmListener l,
			final CancelListener c) {
		AlertDialog.Builder b = BaseHelper.buildAlert(context, title, message);
		if (c != null)
			b.setNegativeButton(context.getResources().getString(R.string.iapps__no),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							c.onNo();
						}
					});
		else b.setNegativeButton(context.getResources().getString(R.string.iapps__no), null);

		b.setPositiveButton(context.getResources().getString(R.string.iapps__yes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						l.onYes();
					}
				});

		b.show();

		return b;
	}

	public static AlertDialog.Builder confirm(
			Context context, String title, String message, final ConfirmListener l) {
		return confirm(context, title, message, l, null);
	}

	/**
	 * Show confirm dialog popup
	 * 
	 * @param context
	 * @param titleResId
	 * @param messageResId
	 * @param l
	 */
	public static AlertDialog.Builder confirm(
			Context context, int titleResId, int messageResId, final ConfirmListener l) {
		String title = null;
		try {
			title = context.getString(titleResId);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String message = null;
		try {
			message = context.getString(messageResId);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return confirm(context, title, message, l, null);
	}

	/**
	 * Show confirm dialog
	 * 
	 * @param context
	 * @param messageResId
	 * @param l
	 */
	public static AlertDialog.Builder confirm(
			Context context, int messageResId, final ConfirmListener l) {

		String message = null;
		try {
			message = context.getString(messageResId);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return confirm(context, null, message, l, null);
	}

	public static interface ConfirmListener {

		public void onYes();
	}

	public static interface CancelListener {

		public void onNo();
	}

	/**
	 * Show a prompt dialog to the user
	 * 
	 * @param context
	 * @param titleResId
	 * @param positiveButtonResId
	 * @param negativeButtonResId
	 * @param l
	 */
	public static void prompt(
			Context context, int titleResId, int positiveButtonResId, int negativeButtonResId,
			final PromptListener l) {
		prompt(context, titleResId, positiveButtonResId, negativeButtonResId,
				InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, l);
	}

	/**
	 * Show a prompt dialog to the user with custom input type
	 * 
	 * @param context
	 * @param titleResId
	 * @param positiveButtonResId
	 * @param negativeButtonResId
	 * @param inputType
	 * @param l
	 */
	public static void prompt(
			Context context, int titleResId, int positiveButtonResId, int negativeButtonResId,
			int inputType, final PromptListener l) {
		String title = null;

		try {
			title = context.getString(titleResId);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String positiveButton = null;
		try {
			positiveButton = context.getString(positiveButtonResId);
		}
		catch (Exception e) {
			e.printStackTrace();
			positiveButton = context.getString(R.string.iapps__done);
		}

		String negativeButton = null;
		try {
			negativeButton = context.getString(negativeButtonResId);
		}
		catch (Exception e) {
			e.printStackTrace();
			negativeButton = context.getString(R.string.iapps__cancel);
		}

		prompt(context, title, positiveButton, negativeButton, inputType, l);
	}

	public static void prompt(
			Context context, String title, String positiveButton, String negativeButton,
			int inputType, final PromptListener l) {
		final EditText input = new EditText(context);
		input.setInputType(inputType);
		input.setSingleLine();

		new AlertDialog.Builder(context).setTitle(title).setView(input)
				.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						String mInput = input.getText().toString().trim();
						if (l != null) {
							l.onYes(mInput);
						}
					}
				}).setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						// Do nothing.
					}
				}).show();
	}

	public static interface PromptListener {

		public void onYes(String input);
	}

	public static String formatDate(Date date, String format) {
		SimpleDateFormat postFormater = new SimpleDateFormat(format, Locale.ENGLISH);

		String newDateStr = postFormater.format(date);
		return newDateStr;
	}

	public static String formatDateTime(DateTime date, String format) {
		return DateTimeFormat.forPattern(format).print(date);
	}

	public static String formatDateTime(String dateTime, String formatFrom, String formatTo) {
		return DateTime.parse(dateTime, DateTimeFormat.forPattern(formatFrom)).toString(formatTo);
	}

	public static String formatISO8601(DateTime date) {
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
		return fmt.print(date);
	}

	public static Date parseDate(String sqlDate, String format) {
		// 2012-12-24 02:01:57
		SimpleDateFormat curFormater = new SimpleDateFormat(format, Locale.ENGLISH);
		Date dateObj = null;
		try {
			dateObj = curFormater.parse(sqlDate);
			return dateObj;
		}
		catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void pickCSV(int requestCode, Activity activity) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(BaseConstants.MIME_CSV);
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		activity.startActivityForResult(intent, requestCode);
	}

	public static DateTime parseDateTime(String dateString, String format) {
		return DateTimeFormat.forPattern(format).parseDateTime(dateString);
	}

	public static DateTime parseISO8601(String dateString) {
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
		DateTime dt = null;
		try {
			dt = fmt.parseDateTime(dateString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return dt;
	}

	public static String formatDouble(double d) {
		DecimalFormat formatter = new DecimalFormat("#,###.##");
		d = Math.round(d);
		return formatter.format(d);
	}

	public static String trim(TextView edt) {
		return edt.getText().toString().trim();
	}

	/**
	 * use calcTimeDiff(DateTime target, Context context)
	 * 
	 * @param target
	 * @return
	 */
	@Deprecated
	public static String calcTimeDiff(DateTime target) {
		return calcTimeDiff(target.toDate());
	}

	/**
	 * Use calcTimeDiff(Date, Context)
	 * 
	 * @param target
	 * @return
	 */
	@Deprecated
	public static String calcTimeDiff(Date target) {
		if (target == null) { return null; }
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();

		calendar1.setTime(target);

		long milsecs1 = calendar1.getTimeInMillis();
		long milsecs2 = calendar2.getTimeInMillis();

		long diff = milsecs2 - milsecs1;
		long dminutes = diff / (60 * 1000);
		long dhours = diff / (60 * 60 * 1000);
		long ddays = diff / (24 * 60 * 60 * 1000);

		String toReturn = "";
		if (diff >= 0) {
			// In the past
			if (dminutes < 60) {
				toReturn = String.valueOf(dminutes);
				if (dminutes == 1) {
					toReturn += " " + MIN_AGO;
				}
				else {
					toReturn += " " + MINS_AGO;
				}
			}
			else if (dhours < 24) {
				toReturn = String.valueOf(dhours);
				if (dhours == 1) {
					toReturn += " " + HOUR_AGO;
				}
				else {
					toReturn += " " + HOURS_AGO;
				}
			}
			else if (ddays <= 7) {
				toReturn = String.valueOf(ddays);
				if (ddays == 1) {
					toReturn += " " + DAY_AGO;
				}
				else {
					toReturn += " " + DAYS_AGO;
				}
			}
			else {
				toReturn = formatDate(target, "dd MMM yyy");
			}
		}
		else {
			// In the future
			diff *= -1;
			dminutes *= -1;
			dhours *= -1;
			ddays *= -1;
			if (dminutes < 60) {
				toReturn = String.valueOf(dminutes);
				if (dminutes == 1) {
					toReturn += " " + MIN_LEFT;
				}
				else {
					toReturn += " " + MINS_LEFT;
				}
			}
			else if (dhours < 24) {
				toReturn = String.valueOf(dhours);
				if (dhours == 1) {
					toReturn += " " + HOUR_LEFT;
				}
				else {
					toReturn += " " + HOURS_LEFT;
				}
			}
			else if (ddays <= 7) {
				toReturn = String.valueOf(ddays);
				if (ddays == 1) {
					toReturn += " " + DAY_TO_GO;
				}
				else {
					toReturn += " " + DAYS_TO_GO;
				}
			}
			else {
				toReturn = formatDate(target, "dd MMM yyy");
			}
		}
		return toReturn;
	}

	public static String calcTimeDiff(DateTime target, Context context) {
		return calcTimeDiff(target.toDate(), context);
	}

	/**
	 * Calculate absolute time difference
	 * 
	 * @param target , date to be compared to now
	 * @param context , the context
	 * @return time difference, or date if it is more than 7 days
	 */
	public static String calcTimeDiff(Date target, Context context) {
		if (target == null || context == null) { return null; }
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();

		calendar1.setTime(target);

		long milsecs1 = calendar1.getTimeInMillis();
		long milsecs2 = calendar2.getTimeInMillis();

		long diff = milsecs2 - milsecs1;
		if (diff < 0) {
			diff *= -1; // make it absolute
		}
		long dminutes = diff / (60 * 1000);
		long dhours = diff / (60 * 60 * 1000);
		long ddays = diff / (24 * 60 * 60 * 1000);

		String toReturn = "";
		if (dminutes < 60) {
			toReturn = String.valueOf(dminutes);
			toReturn += " "
					+ context.getResources().getQuantityString(R.plurals.minutes, (int) dminutes);
		}
		else if (dhours < 24) {
			toReturn = String.valueOf(dhours);
			toReturn += " "
					+ context.getResources().getQuantityString(R.plurals.hours, (int) dhours);
		}
		else if (ddays <= 7) {
			toReturn = String.valueOf(ddays);
			toReturn += " " + context.getResources().getQuantityString(R.plurals.days, (int) ddays);
		}
		else {
			toReturn = formatDate(target, "dd MMM yyy");
		}
		return toReturn;
	}

	public static File processFileResult(Intent data, Context context) {
		if (data == null || context == null)
			return null;

		Uri _uri = data.getData();
		InputStream is = null;
		OutputStream os = null;
		if (_uri != null && "content".equals(_uri.getScheme())) {
			File ef = new File(Environment.getExternalStorageDirectory(), "last.csv");
			try {
				is = context.getContentResolver().openInputStream(_uri);
				os = new FileOutputStream(ef);

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = is.read(bytes)) != -1) {
					os.write(bytes, 0, read);
				}

				is.close();
				os.close();

			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
			finally {
				if (is != null) {
					try {
						is.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (os != null) {
					try {
						os.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return ef;
		}
		else {
			return FileUtils.getFile(_uri.getPath());
		}
	}

	public static boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isAlphaNumeric(String s) {

		String pattern = "^[a-zA-Z0-9 ]*$";
		if (s.matches(pattern)) { return true; }
		return false;
	}

	// private static Intent pickFromGallery() {
	// Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
	// pickPhoto.setType("image/*");
	// pickPhoto.putExtra("crop", "true");
	// // Set this to define the X aspect ratio of the shape
	// pickPhoto.putExtra("aspectX", 1);
	// // Set this to define the Y aspect ratio of the shape
	// pickPhoto.putExtra("aspectY", 1);
	// // pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
	// // pickPhoto
	// // .putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
	// return pickPhoto;
	// }

	private static Intent pickFromGallery() {
		Intent pickPhoto = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickPhoto.setType("image/*");
		pickPhoto.putExtra("crop", "true");
		// Set this to define the X aspect ratio of the shape
		pickPhoto.putExtra("aspectX", 1);
		// Set this to define the Y aspect ratio of the shape
		pickPhoto.putExtra("aspectY", 1);
		pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		pickPhoto.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		return pickPhoto;
	}

	private static Intent pickFromCamera() {

		Intent capturePhoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		capturePhoto.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		capturePhoto.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

		// Crop feature causes crash
		// capturePhoto.putExtra("crop", "true");
		// capturePhoto.putExtra("scale", true);
		// Set this to define the X aspect ratio of the shape
		// capturePhoto.putExtra("aspectX", 1);
		// Set this to define the Y aspect ratio of the shape
		// capturePhoto.putExtra("aspectY", 1);
		return capturePhoto;
	}

	public static Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}

	/**
	 * Get the URL to the app detail page in the PlayStore
	 * 
	 * @param context
	 * @return URL to the PlayStore
	 */
	public static String getPlayStoreLink(Context context) {
		final String packageName = context.getPackageName();
		return BaseConstants.PLAY_STORE_LINK + packageName;
	}

	private static File getTempFile() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			File file = new File(Environment.getExternalStorageDirectory(),
					BaseConstants.TEMP_PHOTO_FILE);
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			return file;
		}
		else {
			return null;
		}
	}

	public static String getRealPathFromURI(Uri contentUri, Activity context) {
		String[] proj = {
				MediaStore.Images.Media.DATA
		};
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * Select an image and allow user to crop the image using requestCode {@link BaseHelper}
	 * .REQUEST_IMAGE_CODE Implement {@link BaseUIHelper} .processImage / processPreview
	 * onActivityResult to process the image results and get the file
	 * 
	 * @param activity , activity to handle the onActivityResult
	 */
	public static void pickImage(final Activity activity) {
		AlertDialog.Builder bld = BaseHelper.buildAlert(activity, "Select Image Source", null);
		bld.setItems(R.array.imageSource, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent i = pickFromGallery();
					activity.startActivityForResult(i, REQUEST_GET_IMAGE_CODE);
					break;
				case 1:
					// camera
					PackageManager pm = activity.getApplicationContext().getPackageManager();

					if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
						Intent i2 = pickFromCamera();
						activity.startActivityForResult(i2, REQUEST_GET_IMAGE_CODE);
					}
					else {
						BaseHelper.showAlert(activity, null, "You dont have a camera", null);
					}

					break;

				default:
					// nothing
				}
			}
		});
		bld.setNeutralButton(android.R.string.cancel, null);
		bld.setCancelable(true);
		bld.show();
	}

	/**
	 * Select an image and allow user to crop the image using requestCode {@link BaseHelper}
	 * .REQUEST_IMAGE_CODE Implement {@link BaseUIHelper} .processImage / processPreview
	 * onActivityResult to process the image results and get the file
	 * 
	 * @param fragment , fragment to handle the onActivityResult
	 */
	public static void pickImage(final Fragment fragment) {
		AlertDialog.Builder bld = BaseHelper.buildAlert(fragment.getActivity(),
				"Select image source", null);
		bld.setItems(R.array.imageSource, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent i = pickFromGallery();
					fragment.startActivityForResult(i, REQUEST_GET_IMAGE_CODE);
					break;
				case 1:
					// camera
					PackageManager pm = fragment.getActivity().getApplicationContext()
							.getPackageManager();

					if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
						Intent i2 = pickFromCamera();
						fragment.startActivityForResult(i2, REQUEST_GET_IMAGE_CODE);
					}
					else {
						BaseHelper.showAlert(fragment.getActivity(), null,
								"You dont have a camera", null);
					}

					break;

				default:
					// nothing
				}
			}
		});
		bld.setNeutralButton(android.R.string.cancel, null);
		bld.setCancelable(true);
		bld.show();
	}

	/**
	 * Get the version name of this build
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		if (context == null) { return null; }
		String ver = null;
		PackageInfo pInfo;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			ver = pInfo.versionName;
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return ver;
	}

	/**
	 * Get the version code of this build
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		if (context == null) { return 0; }
		int ver = 0;
		PackageInfo pInfo;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			ver = pInfo.versionCode;
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return ver;
	}

	/**
	 * Format distance to be displayed to the user
	 * 
	 * @param d , distance in metres
	 * @return formatted {@link String}
	 */
	public static String formatDistance(double d) {
		DecimalFormat formatter = new DecimalFormat("#,### 'm'");
		d = Math.round(d);
		if (d > 1000) {
			formatter = new DecimalFormat("#,###.# 'km'");
			d = d / 1000;
		}

		return formatter.format(d);
	}

	public static String formatCurrency(double d) {
		DecimalFormat formatter = new DecimalFormat("'$'#,##0.00");
		return formatter.format(d);
	}
}
