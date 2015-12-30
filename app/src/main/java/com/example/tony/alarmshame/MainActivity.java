package com.example.tony.alarmshame;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static final int dialog_id = 0;
    int hour,minute;
    private Button startButton;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startButton =(Button) findViewById(R.id.StartAlarm);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //shows the time picker
                //create the alarm at TimePickerDialog
                //showDialog(dialog_id);
                int requestID = (int) System.currentTimeMillis();

                //set i to time till alarm
                int i = 10;
                Intent intent = new Intent(MainActivity.this,AlarmRecieverActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);


                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,requestID,
                        intent,PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                //sets the alarm into an infinite loop
                //use 15*1000 for testing
                //use 10*60*1000 for release or make variable but that's later
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(i*1000),
                        15 * 1000,pendingIntent);

                if (mToast != null){
                    mToast.cancel();
                }

                mToast = Toast.makeText(getApplicationContext(),
                        "Alarm starts in " + i + " seconds", Toast.LENGTH_LONG);
                mToast.show();
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

    protected Dialog onCreateDialog(int id){
        switch(id) {
            case dialog_id:
                return new TimePickerDialog(this, mTimeSetListener, hour, minute, false);
        }
        return null;
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
}

