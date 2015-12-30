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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by tony on 28/12/15.
 */
public class AlarmRecieverActivity extends Activity{
    private MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,"My Wake Log");
        //This is why the screen will stay on
        mWakeLock.acquire();
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
                finish();
            }
        });

        Button stopAlarm = (Button) findViewById(R.id.Stop);
        //Here is where the alarm will stop
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stops the music
                mMediaPlayer.stop();

                //stops the pending intent
                Intent intent = new Intent(AlarmRecieverActivity.this,AlarmRecieverActivity.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmRecieverActivity.this,
                        3, intent, 0);
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.cancel(pendingIntent);
                PendingIntent.getBroadcast(AlarmRecieverActivity.this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT).cancel();
                Toast.makeText(getBaseContext(),"Alarm has been cancelled",
                        Toast.LENGTH_LONG).show();

                finish();
            }
        });

        playSound(this, getAlarmUri());
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
