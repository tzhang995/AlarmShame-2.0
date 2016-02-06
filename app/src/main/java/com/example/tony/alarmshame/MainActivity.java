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
import android.os.Vibrator;
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
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static final int dialog_id = 0;
    static int hour,minute;
    private Toast mToast;
    private Switch mySwitch;
    TimePickerFragment newFragment;

    //requestID for each days of the week
    //first digit is for request id
    //second number is if the alarm for this request is on
    int requestID[] = {0,0,0,0,0,0,0};
    private final int bArray [] = {R.id.sunday,R.id.monday,R.id.tuesday,R.id.wednesday,R.id.thursday,
    R.id.friday,R.id.saturday};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mySwitch = (Switch) findViewById(R.id.alarmSwitch);
        //if the switch is turned on
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //set i to time till alarm
                    int setTime = (hour * 60 * 60) + (minute * 60);
                    Calendar c = Calendar.getInstance();
                    int currentTime = (c.get(c.HOUR_OF_DAY) *60 * 60) +
                            (c.get(c.MINUTE) * 60) + c.get(c.SECOND) ;
                    int i = setTime - currentTime;
                    //checks if it is tomorrow
                    if (i <0){
                        i = i+(60*60*24);
                    }


                    //A forloop that creates a pending intent on the
                    // days that they want the alarm to be activated
                    for(int allDay = 0; allDay <7; allDay++) {

                        //checks to see the button for that day is activated or not
                        ToggleButton button = (ToggleButton) findViewById(bArray[allDay]);
                        if (button.isChecked()){
                            requestID[allDay] = 1;
                        }


                        //If the button for that day is checked, we create a new pendingintent
                        if(requestID[allDay] == 1) {
                            Intent intent = new Intent(MainActivity.this, AlarmRecieverActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, bArray[allDay],
                                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                            //sets the alarm into an infinite loop
                            //use 15*1000 for testing
                            //use 10*60*1000 for release or make variable but that's later
                            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000 + 3* 1000* allDay),
                                    15 * 1000, pendingIntent);
                        }
                    }


                    ///mmmmmToast
                    if (mToast != null){
                        mToast.cancel();
                    }

                    mToast = Toast.makeText(getApplicationContext(),
                            "Alarm starts in " + i, Toast.LENGTH_SHORT);
                    mToast.show();
                } else {
                    //stops the alarms
                    for(int allDay = 0; allDay <7; allDay++) {
                        if (requestID[allDay] == 1) {
                            Intent intent = new Intent(MainActivity.this, AlarmRecieverActivity.class);

                            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, bArray[allDay],
                                    intent, 0);
                            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                            am.cancel(pendingIntent);

                            Toast.makeText(getApplicationContext(), "Alarm has been stopped"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
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

    public void setViewTime(int hour, int minute) {//},Activity act) {
        //AnalogClock ac = new act.findViewById(R.id.analogClock);
        //ac.set
        Toast.makeText(getApplicationContext(), hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }
}

