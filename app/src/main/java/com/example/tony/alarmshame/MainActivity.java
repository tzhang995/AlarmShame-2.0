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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int dialog_id = 0;
    static int hour,minute;
    private Toast mToast;
    TimePickerFragment newFragment;

    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ListView listView = (ListView) findViewById(R.id.listview);
        generateListContent();
        listView.setAdapter(new MyListAdapter(this, R.layout.alarm_list,data));

    }

    private void generateListContent(){
        for(int i = 0; i<10;i++){
            data.add("what" + i);
        }
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

    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ViewHolder viewHolder= new ViewHolder();
                viewHolder.alarmSwitch = (Switch) convertView.findViewById(R.id.list_item_alarmSwitch);
                viewHolder.alarmTime = (TextView) convertView.findViewById(R.id.list_item_alarmTime);
                viewHolder.alarmSunday = (Button) convertView.findViewById(R.id.list_item_sunday);
                viewHolder.alarmMonday = (Button) convertView.findViewById(R.id.list_item_monday);
                viewHolder.alarmTuesday = (Button) convertView.findViewById(R.id.list_item_tuesday);
                viewHolder.alarmWednesday = (Button) convertView.findViewById(R.id.list_item_wednesday);
                viewHolder.alarmThursday = (Button) convertView.findViewById(R.id.list_item_thursday);
                viewHolder.alarmFriday = (Button) convertView.findViewById(R.id.list_item_friday);
                viewHolder.alarmSaturday = (Button) convertView.findViewById(R.id.list_item_saturday);
                convertView.setTag(viewHolder);

            } else {
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.alarmTime.setText(getItem(position));
            }
            return convertView;
        }
    }


    /****************THIS IS WHERE ALL THE ITEMS IN EACH LIST WILL GO ******/
    public class ViewHolder{
        Switch alarmSwitch;
        TextView alarmTime;
        Button alarmSunday;
        Button alarmMonday;
        Button alarmTuesday;
        Button alarmWednesday;
        Button alarmThursday;
        Button alarmFriday;
        Button alarmSaturday;
    }
}

