package com.example.tony.alarmshame;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by tony on 28/12/15.
 */
public class AlarmRecieverActivity extends Activity{
    private MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock mWakeLock;
    private Vibrator vibrator;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,"My Wake Log");
        //This is why the screen will stay on
        mWakeLock.acquire();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.alarm);

        Button snoozeAlarm = (Button) findViewById(R.id.Snooze);
        //Here is where the alarm will snooze
        snoozeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stops the music
                mMediaPlayer.stop();
                vibrator.cancel();
                finish();
            }
        });

        Button stopButton = (Button) findViewById(R.id.Stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                //stops the request at that day of the week
                int requestID = c.get(Calendar.DAY_OF_WEEK);

                Intent intent = new Intent(AlarmRecieverActivity.this, AlarmRecieverActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(AlarmRecieverActivity.this, requestID,
                        intent, 0);
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.cancel(pendingIntent);
                //stops the music
                mMediaPlayer.stop();


                //creates new alarm if you made it repeating

                int currentTime = (c.get(c.HOUR_OF_DAY) *60 * 60) +
                        (c.get(c.MINUTE) * 60) + c.get(c.SECOND) ;

                int setTime = (MainActivity.hour * 60 * 60) + (MainActivity.minute * 60);
                int i = setTime - currentTime;
                //checks if it is 6 days from now or 7 days from now
                if (i <0){
                    i = i+(60*60*24 * 6);
                } else {
                    i = i + (60 * 60 * 24 * 7);
                }

                intent = new Intent(AlarmRecieverActivity.this,AlarmRecieverActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

                pendingIntent = PendingIntent.getActivity(AlarmRecieverActivity.this,requestID,
                        intent,PendingIntent.FLAG_CANCEL_CURRENT);
                am = (AlarmManager) getSystemService(ALARM_SERVICE);
                //sets the alarm into an infinite loop
                //use 15*1000 for testing
                //use 10*60*1000 for release or make variable but that's later
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(i*1000),
                        10 * 60 * 1000,pendingIntent);

                vibrator.cancel();
                finish();
                Toast.makeText(getApplicationContext(), "Alarm has been stopped"
                        , Toast.LENGTH_LONG).show();
            }
        });

        playSound(this, getAlarmUri());
        long[] pattern = { 0, 200, 0 };
        vibrator.vibrate(pattern, 0);
    }

    private void playSound(Context context, Uri alert){
        mMediaPlayer = new MediaPlayer();
        try{
            mMediaPlayer.setDataSource(context,alert);
            final AudioManager audioManager =
                    (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
            //checks if mute
            //if(audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0){
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            //}
        }catch (IOException exc) {
            Log.i("AlarmReciever","No audio files are found!");
        }
    }

    //This is where it gets the music
    private Uri getAlarmUri(){
        //TODO get personal music
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert==null){
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null){
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    protected void onStop(){
        super.onStop();
        mWakeLock.release();
    }
}
