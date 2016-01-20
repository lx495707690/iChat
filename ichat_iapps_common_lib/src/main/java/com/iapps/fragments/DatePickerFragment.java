package com.iapps.fragments;

import java.util.Calendar;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment {

	private DatePickerDialog.OnDateSetListener mListener;

	private Calendar mCal = Calendar.getInstance();

	private Calendar mMinCal;
	private Calendar mMaxCal;

	public void setListener(DatePickerDialog.OnDateSetListener l) {
		this.mListener = l;
	}

	public void setInitCalendar(Calendar cal) {
		this.mCal = cal;
	}

	public void setMinDate(Calendar min) {
		this.mMinCal = min;
	}

	public void setMaxDate(Calendar max) {
		this.mMaxCal = max;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);

		// Use the current date as the default date in the picker
		final Calendar c = mCal;
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog pickerDialog;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			pickerDialog = new DatePickerDialog(getActivity(), mListener, year,
					month, day);
			if (mMinCal != null) {
				pickerDialog.getDatePicker().setMinDate(
						mMinCal.getTimeInMillis());
			}
			if (mMaxCal != null) {
				pickerDialog.getDatePicker().setMaxDate(
						mMaxCal.getTimeInMillis());
			}
		} else {
			pickerDialog = new DatePickerDialog(getActivity(), mListener, year,
					month, day) {
				@Override
				public void onDateChanged(DatePicker view, int year, int month,
						int day) {
					if(isAfterMinDate(year, month, day) && isBeforeMaxDate(year, month, day)){
						view.updateDate(year, month, day);
					}
				}
			};
		}

		return pickerDialog;
	}

	private boolean isBeforeMaxDate(int year, int month, int day) {
		if (mMaxCal != null) {
			if (year <= mMaxCal.get(Calendar.YEAR)
					&& month <= mMaxCal.get(Calendar.MONTH)
					&& day <= mMaxCal.get(Calendar.DAY_OF_MONTH)) // meets your
																	// criteria
			{
				return true;
			}else{
				return false;
			}
		} else {
			return true;
		}
	}

	private boolean isAfterMinDate(int year, int month, int day) {
		if (mMinCal != null) {
			if (year >= mMaxCal.get(Calendar.YEAR)
					&& month >= mMaxCal.get(Calendar.MONTH)
					&& day >= mMaxCal.get(Calendar.DAY_OF_MONTH)) // meets your
																	// criteria
			{
				return true;
			}else{
				return false;
			}
		} else {
			return true;
		}
	}
}
