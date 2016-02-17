package com.example.tony.alarmshame;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

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

                    // TODO: Grab actual time from picker

                    // Compute the alarm time in minutes...
                    int setTime = (hour * 60 * 60) + (minute * 60);
                    // ...compute the current time in seconds...
                    Calendar c = Calendar.getInstance();
                    int currentTime = (c.get(c.HOUR_OF_DAY) *60 * 60) +
                            (c.get(c.MINUTE) * 60) + c.get(c.SECOND) ;

                    // ...and find the difference in seconds
                    int differenceTime = setTime - currentTime;

                    int numSecsInDay = 60 * 60 * 24;

                    // Checks if it is tomorrow instead
                    boolean later = (differenceTime <= 0);

                    // Iterate through each enumerated day of the week and start alarms based the user's choices
                    for(int forDay = 0; forDay <7; forDay++) {

                        // Determine whether the user has selected for the alarm to activate on forDay
                        ToggleButton button = (ToggleButton) findViewById(bArray[forDay]);
                        requestID[forDay] = button.isChecked() ? 1 : 0;

                        // Create a new PendingIntent (Alarm) if the day of week is selected
                        if(requestID[forDay] == 1) {
                            Intent intent = new Intent(MainActivity.this, AlarmRecieverActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            // Creates a PendingIntent for the alarm to activate
                            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                                    Calendar.SUNDAY + forDay,
                                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

                            // Find number of days until alarm activation
                            int diffBetweenDays = forDay - (c.get(Calendar.DAY_OF_WEEK) - 1);
                            if (diffBetweenDays < 0) {
                                diffBetweenDays += 7;
                            } else if (diffBetweenDays == 0 && differenceTime <= 0) {
                                diffBetweenDays += 7;
                            }

                            // Convert to milliseconds
                            int alarmStartIn = differenceTime * 1000;

                            // Compute the final time until the first call of the alarm
                            alarmStartIn = alarmStartIn + (1000 * diffBetweenDays * numSecsInDay);
                            mToast = Toast.makeText(getApplicationContext(),
                                    "Alarm starts in true " + (alarmStartIn/1000), Toast.LENGTH_SHORT);
                            System.out.println("Day: " + diffBetweenDays + " at " + (alarmStartIn/1000));

                            //sets the alarm into an infinite loop
                            //use 15*1000 for testing
                            //use 10*60*1000 for release
                            am.setRepeating(AlarmManager.RTC_WAKEUP,
                                    System.currentTimeMillis() + alarmStartIn,
                                    15 * 1000, pendingIntent);
                            mToast.show();
                        }
                    }


                    ///mmmmmToast
                    /*
                    if (mToast != null){
                        mToast.cancel();
                    }

                    mToast = Toast.makeText(getApplicationContext(),
                            "Alarm starts in " + i, Toast.LENGTH_SHORT);
                    mToast.show();
                    */
                } else {
                    //stops the alarms
                    for (int allDay = 0; allDay < 7; ++allDay) {
                        if (requestID[allDay] == 1) {
                            Intent intent = new Intent(MainActivity.this, AlarmRecieverActivity.class);

                            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                                    Calendar.SUNDAY + allDay, intent, 0);
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

