package com.example.antonioreyes.aguas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Calendar;


public class formulario_reporte extends ActionBarActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Calendar calendar;
    private TextView dateView;
    private TextView timeView;
    private TextView placeView;
    private TextView commentView;
    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_reporte);

        Parse.enableLocalDatastore(getApplication());
        Parse.initialize(getApplication(), "D4tClbuaS3OCWzRvQjT2L3vB8KUDJesFvVNbeJke", "6xkgf0GC8KvhNXPtXrGG01vXip5EfffnDkacnZ8O");

        dateView = (TextView) findViewById(R.id.dateText);
        timeView = (TextView) findViewById(R.id.timeText);
        placeView = (TextView) findViewById(R.id.placeText);
        commentView = (TextView) findViewById(R.id.commentText);

        Intent intent = getIntent();
        String center = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);

        placeView.setText(center);

        Toast.makeText(this, center, Toast.LENGTH_SHORT).show();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        showDate(year, month+1, day);
        showTime(hour, minute);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }else if(id == 2){
            return new TimePickerDialog(this, myTimeListener, hour, minute, false);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(1);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    private DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @SuppressWarnings("deprecation")
    public void setTime(View view){
        showDialog(2);
    }

    private TimePickerDialog.OnTimeSetListener myTimeListener =  new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            showTime(selectedHour, selectedMinute);
        }
    };

    private void showTime(int hour, int minute) {
        String min_tmp;
        if(minute < 10){
            min_tmp = '0' + String.valueOf(minute);
        }else{
            min_tmp = String.valueOf(minute);
        }
        timeView.setText(new StringBuilder().append(hour).append(":")
                .append(min_tmp));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario_reporte, menu);
        return true;
    }

    public void saveParse(View view){

        ParseObject reportObject = new ParseObject("ReportTest");

        reportObject.put("ReportDate", dateView.getText().toString());
        reportObject.put("ReportTime", timeView.getText().toString());
        reportObject.put("Place", placeView.getText().toString());
        reportObject.put("Comment", commentView.getText().toString());

        reportObject.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    //Success
                    Toast.makeText(getApplicationContext(), "Save on Parse", Toast.LENGTH_SHORT).show();
                } else {
                    //Error
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void change_location(View view){
        Intent intent = new Intent(this, MapsActivity2.class);
        // mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        //center = mMap.getCameraPosition().target;
        //center = mMap.getCameraPosition().target;

        // this.onLocationChanged(mMap.getMyLocation());
        //intent.putExtra(EXTRA_MESSAGE, center+" ");
        startActivity(intent);
    }
}
