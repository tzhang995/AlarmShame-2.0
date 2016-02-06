package com.example.tony.alarmshame;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.AnalogClock;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Gagan on 1/10/2016.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    int hour;
    int minutes;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minutes,false);
                //DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        //Toast.makeText(getActivity(), "hi", Toast.LENGTH_SHORT).show();
        hour = hourOfDay;
        minutes = minute;
        MainActivity.hour = hourOfDay;
        MainActivity.minute = minute;
        Toast.makeText(getActivity(), hourOfDay + " " + minute, Toast.LENGTH_SHORT).show();
    }
    public int getHour(){
        return hour;
    }
    public int getMinute(){
        return minutes;
    }
}