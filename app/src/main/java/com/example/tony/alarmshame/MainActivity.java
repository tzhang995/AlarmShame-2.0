package com.example.tony.alarmshame;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static final int dialog_id = 0;
    int hour,minute;
    private Toast mToast;
    private Switch mySwitch;
    TimePickerFragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mySwitch = (Switch) findViewById(R.id.alarmSwitch);
        //if the switch is turned on
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Calendar c = Calendar.getInstance();
                int currentTime = (c.get(c.HOUR_OF_DAY) *60 * 60) +
                        (c.get(c.MINUTE) * 60) + c.get(c.SECOND) ;
                if(isChecked){
                    //shows the time picker
                    //create the alarm at TimePickerDialog
                    //showDialog(dialog_id);
                    int requestID = 1;

                    //set i to time till alarm
                    int setTime = (newFragment.getHour() * 60 * 60) + (newFragment.getMinute() * 60);
                    int i = setTime - currentTime;
                    //checks if it is tomorrow
                    if (i <0){
                        i = i+(60*60*24);
                    }

                    Intent intent = new Intent(MainActivity.this,AlarmRecieverActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,requestID,
                            intent,PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    //sets the alarm into an infinite loop
                    //use 15*1000 for testing
                    //use 10*60*1000 for release or make variable but that's later
                    am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(i*1000),
                            10 * 60 * 1000,pendingIntent);

                    if (mToast != null){
                        mToast.cancel();
                    }

                    mToast = Toast.makeText(getApplicationContext(),
                            "Alarm starts in " + i + " seconds"+
                                    newFragment.getMinute()+"minute"+newFragment.getHour()+"hour", Toast.LENGTH_LONG);
                    mToast.show();
                } else {
                    int requestID = 1;

                    Intent intent = new Intent(MainActivity.this,AlarmRecieverActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,requestID,
                            intent,0);
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.cancel(pendingIntent);

                    Toast.makeText(getApplicationContext(),"Alarm has been stopped"
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.out.print(System.nanoTime());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Dialog onCreateDialog(int id){
        switch(id) {
            case dialog_id:
                //return new TimePickerDialog(this, mTimeSetListener, hour, minute, false);
        }
        return null;
    }

    public void showTimePickerDialog(View v) {
        newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int hour_minute) {
            hour = hourOfDay;
            minute = hour_minute;
            //TODO language syntax
            //Toast.makeText(getBaseContext(),"Alarm is set to " + hour+ " hours and " +minute + " minutes",
            //        Toast.LENGTH_LONG).show();
        }
    };

    public void setViewTime(int hour, int minute) {//},Activity act) {
        //AnalogClock ac = new act.findViewById(R.id.analogClock);
        //ac.set
        Toast.makeText(getApplicationContext(), hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }
}

