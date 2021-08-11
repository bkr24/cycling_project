package aston.rogalskb.cyclingproject_v3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import aston.rogalskb.cyclingproject_v3.R;
import aston.rogalskb.cyclingproject_v3.dateTimePicker.datePickerFragment;
import aston.rogalskb.cyclingproject_v3.dateTimePicker.reminderBroadcast;
import aston.rogalskb.cyclingproject_v3.dateTimePicker.timePickerFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import javax.annotation.Nullable;

public class updateRoute extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    TextView origin,destination,originLatLng,destinationLatLng;
    EditText routeName;
    Button saveButton, loadRoute, deleteRoute, setReminder;
    String id,routeSavedName,originName,destinationName,originCoordinates,destinationCoordinates;
    Double originLat,originLon,destinationLat,destinationLot;

    private static final int UPDATE_ORIGIN_REQUEST_CODE = 69;
    private static final int UPDATE_DESTINATION_REQUEST_CODE = 70;

    public static Double eLat,eLng,sLat,sLng;
    public static String eAdr, sAdr, newRouteName;
    public String theID;
    public static boolean updatedOrigin,updatedDestination;

    public static Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_route);

        routeName = findViewById(R.id.editRouteName);
        origin = findViewById(R.id.editRouteOrigin);
        destination = findViewById(R.id.editRouteDestination);
        originLatLng = findViewById(R.id.editRouteOriginLatLon);
        destinationLatLng = findViewById(R.id.editRouteDestinationLatLon);

        saveButton = findViewById(R.id.editRouteSaveButton);
        loadRoute = findViewById(R.id.updateRouteLoadRouteButton);
        deleteRoute = findViewById(R.id.updareRouteDeleteButton);
        setReminder = findViewById(R.id.updateRouteSetReminder);

        calendar = Calendar.getInstance();
        makeNotificationChannel();




        getSetIntentData();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(routeSavedName);
        }




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSetIntentData();
                DatabaseHelper databaseHelper = new DatabaseHelper(updateRoute.this);
                newRouteName = routeName.getText().toString().trim();
                //Testing route name only
                routeSavedName = routeName.getText().toString().trim();


                Log.i("d123", "Origin " + sAdr + " " +sLat + " "+ sLng + " Destination" + eAdr + " " + eLat + " " + eLng + " Origin true = " + updatedOrigin + " destination = true" + updatedDestination );



                if(updatedDestination && updatedOrigin){
                    databaseHelper.updateBoth(theID,newRouteName,sAdr,sLat,sLng,eAdr,eLat,eLng);
                    Log.i("1234567","Destination and origin was updated with id " + theID + " new address " + sAdr + " destination " + eAdr);

                } else if (updatedOrigin){
                    databaseHelper.updateOriginData(theID,newRouteName,sAdr,sLat,sLng);
                    Log.i("1234567","Origin was updated with id " + theID + " new address " + sAdr + " lat " + sLat + " lon " + sLng);
                } else if (updatedDestination){
                    databaseHelper.updateDestinationData(theID,newRouteName,eAdr,eLat,eLng);
                    Log.i("1234567","Destination was updated with id " + theID + " new address " + eAdr + " lat " + eLat + " lon " + eLng);

                } else {
                    databaseHelper.updateNameOnly(theID,routeSavedName);
                    Log.i("bartek","The else was triggered with value of" + newRouteName);
                }

                Intent intent = new Intent(updateRoute.this, savedRoutes.class);
                startActivity(intent);

            }
        });


        origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(updateRoute.this,start_location.class);
                intent.putExtra("id",id);
                startActivityForResult(intent,UPDATE_ORIGIN_REQUEST_CODE);

            }
        });

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(updateRoute.this,end_location.class);
                intent.putExtra("id",id);
                startActivityForResult(intent,UPDATE_DESTINATION_REQUEST_CODE);
            }
        });

        loadRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent drawRouteIntent = new Intent(updateRoute.this,drawMapRoute.class);
                drawRouteIntent.putExtra("endAddressMap",destinationName);
                drawRouteIntent.putExtra("endLatitudeMap",destinationLat);
                drawRouteIntent.putExtra("endLongitudeMap",destinationLot);
                drawRouteIntent.putExtra("startAddressMap",originName);
                drawRouteIntent.putExtra("startLatitudeMap",originLat);
                drawRouteIntent.putExtra("startLongitudeMap",originLon);
                //drawRouteIntent.putExtra("weatherDescription",wData);
                //drawRouteIntent.putExtra("weatherTemperature",wTemp);
                startActivity(drawRouteIntent);
            }
        });

        deleteRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFragment datePicker = new datePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    public void getSetIntentData(){
        //Intent i = getIntent();
        if(getIntent().hasExtra("id") && getIntent().hasExtra("routeName") && getIntent().hasExtra("originName") && getIntent().hasExtra("destinationName")
        && getIntent().hasExtra("originLat") && getIntent().hasExtra("originLon") && getIntent().hasExtra("destinationLat") && getIntent().hasExtra("destinationLon")){

            id = getIntent().getExtras().getString("id","");
            routeSavedName = getIntent().getExtras().getString("routeName","");
            routeName.setText(routeSavedName);
            originName = getIntent().getExtras().getString("originName","");
            origin.setText(originName);
            destinationName  = getIntent().getExtras().getString("destinationName","");
            destination.setText(destinationName);

            Double tempDestinationLat = getIntent().getExtras().getDouble("destinationLat",0);
            destinationLat = tempDestinationLat;
            Double tempDestinationLon = getIntent().getExtras().getDouble("destinationLon",0);
            destinationLot = tempDestinationLon;

            destinationCoordinates= makeLatLongText(tempDestinationLat,tempDestinationLon);
            destinationLatLng.setText(destinationCoordinates);

            Double tempOriginLat = getIntent().getExtras().getDouble("originLat",0);
            originLat = tempOriginLat;
            Double tempOriginLon = getIntent().getExtras().getDouble("originLon",0);
            originLon = tempOriginLon;


            originCoordinates = makeLatLongText(tempOriginLat,tempOriginLon);
            originLatLng.setText(originCoordinates);

        } else {
            Toast.makeText(this, "No data present", Toast.LENGTH_SHORT).show();
        }



    }

    public String makeLatLongText(Double latitude,Double longitude){
        String lat = " Lat " + latitude.toString()+ " ";
        String lon = " Lon " + longitude.toString() + " ";

        String anwser = lat + lon;
        return anwser;

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("1234567", "THE RESULT OF INTENT IS" + resultCode);


        //Get the intents upon the completion of the activities
        switch (requestCode){
            case UPDATE_ORIGIN_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    theID = data.getExtras().getString("id");
                    sAdr = data.getExtras().getString("startAddress");
                    origin.setText(sAdr);
                    sLat = data.getExtras().getDouble("startLatitude");
                    sLng = data.getExtras().getDouble("startLongitude");
                    updatedOrigin = true;

                    Log.i("ori123","Origin was updated with id " + theID + " new address " + sAdr + " lat " + sLat + " lon " + sLng + " and updatedOrigin is " + updatedOrigin);

                }
                break;
            case UPDATE_DESTINATION_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    theID = data.getExtras().getString("id");
                    eAdr = data.getExtras().getString("endAddress");
                    destination.setText(eAdr);
                    eLat = data.getExtras().getDouble("endLatitude");
                    eLng = data.getExtras().getDouble("endLongitude");
                    updatedDestination = true;

                }
                break;
        }

    }

    void confirmDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete route??");
        builder.setMessage("Are you sure you want to delete this route?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper myDB = new DatabaseHelper(updateRoute.this);
                myDB.deleteRow(id);
                //Refresh Activity
                //Intent intent = new Intent(updateRoute.this, updateRoute.class);
                //startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


    //This code should probably belong in another class but my procastinating genius is trying to finish this product in record-time, so bad programming practises are allowed :)


    //Code for setting the date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        Log.i("bartek",currentDate);

        //Do the code for time picker dialog
        //int hour =
        DialogFragment timePicker = new timePickerFragment();
        timePicker.show(getSupportFragmentManager(),"time picker");



    }


    //Set the time
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);

       //int date = calendar.getWeekYear();






        String time = "Hour " + hourOfDay + " Minute" + minute;
        //Log.i("bartek",);

        Intent intent = new Intent(updateRoute.this, reminderBroadcast.class);
        intent.putExtra("routeName",routeSavedName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(updateRoute.this,200,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);




    }


    //Notification code
    private void makeNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            CharSequence name = "CyclingReminderChannel";
            String description = "Channel for cycling notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("notifyRoute",name,importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);


        }
    }


}
