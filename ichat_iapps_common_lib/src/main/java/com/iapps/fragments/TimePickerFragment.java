package com.iapps.fragments;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

public class TimePickerFragment extends DialogFragment {
	
	private TimePickerDialog.OnTimeSetListener mListener;
	private Calendar mCal = Calendar.getInstance();
	
	public void setListener(TimePickerDialog.OnTimeSetListener l){
		this.mListener = l;
	}
	
	public void setCalendar(Calendar cal){
		this.mCal = cal;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = mCal;
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), mListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
	
}
