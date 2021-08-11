package aston.rogalskb.cyclingproject_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import aston.rogalskb.cyclingproject_v3.R;


//import afu.org.checkerframework.checker.oigj.qual.O;


public class planRouteActivity extends AppCompatActivity {
    TextView startTextFinal,endTextFinal;
    private static final int START_ACTIVITY_REQUEST_CODE = 10;
    private static final int END_ACTIVITY_REQUEST_CODE = 20;
    private static Double eLat,eLng,sLat,sLng;
    public static String eAdr, sAdr, wData, wTemp;
    String apiKey;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_route);

        apiKey = getString(R.string.apiKEY);


        Button startLocBtn =  findViewById(R.id.startLocationBtn);
        Button endLocBtn = findViewById(R.id.endLocationBtn);
        endTextFinal= findViewById(R.id.endLocationText);
        startTextFinal = findViewById(R.id.startLocationText);

        Button drawRoute = findViewById(R.id.btnCompleteLocations);




        startLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(planRouteActivity.this, start_location.class);
                startActivityForResult(intent1, START_ACTIVITY_REQUEST_CODE);
            }
        });

        endLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(planRouteActivity.this,end_location.class);
                startActivityForResult(intent2,END_ACTIVITY_REQUEST_CODE);
            }
        });


        drawRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Was checking if the variables got deleted after exiting the activity
                Intent drawRouteIntent = new Intent(planRouteActivity.this,drawMapRoute.class);



                drawRouteIntent.putExtra("endAddressMap",eAdr);
                drawRouteIntent.putExtra("endLatitudeMap",eLat);
                drawRouteIntent.putExtra("endLongitudeMap",eLng);
                drawRouteIntent.putExtra("startAddressMap",sAdr);
                drawRouteIntent.putExtra("startLatitudeMap",sLat);
                drawRouteIntent.putExtra("startLongitudeMap",sLng);
                drawRouteIntent.putExtra("weatherDescription",wData);
                drawRouteIntent.putExtra("weatherTemperature",wTemp);


                if(sAdr == null){
                    Toast.makeText(planRouteActivity.this,"No start address set",Toast.LENGTH_LONG).show();
                }

                if(eAdr == null){
                    Toast.makeText(planRouteActivity.this,"No end address set",Toast.LENGTH_LONG).show();
                }


                if(sAdr != null && eAdr != null){
                    //If both are not empty then you can start then next activity
                    startActivity(drawRouteIntent);

                }

                //startActivity(drawRouteIntent);


            }
        });






    }

    private void checkIfStartNull(String startAdrress){
        if(startAdrress.equals("")){
            Toast.makeText(this, "You did not enter a start location", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkIfEndNull(String endAdress){
        if(endAdress.equals("")){
            Toast.makeText(this, "You did not enter a destination", Toast.LENGTH_SHORT).show();
        }

    }



    public void openStartLocation(){
        Intent intent = new Intent(planRouteActivity.this, start_location.class);
        //startActivityForResult(intent,START_ACTIVITY_REQUEST_CODE);
        startActivity(intent);

    }


    /*
    public void openEndLocation(){
        //Intent intent = new Intent(this, end_location.class);
        //startActivity(intent);
        Intent intent1 = new Intent(this, end_location.class);
        //intent.setFlags(0);
        startActivityForResult(intent1,END_ACTIVITY_REQUEST_CODE);

    }

     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("1234567", "THE RESULT OF INTENT IS" + resultCode);
        //Log.i("12345678","THE ADRESS IS " + data.getStringExtra("CUNT"));


        //Get the intents upon the completion of the activities
        switch (requestCode){
            case END_ACTIVITY_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    eAdr = data.getExtras().getString("endAddress");
                    eLat = data.getExtras().getDouble("endLatitude");
                    eLng = data.getExtras().getDouble("endLongitude");
                    endTextFinal.setText(eAdr);
                }
                break;
            case START_ACTIVITY_REQUEST_CODE:
                if(resultCode == RESULT_OK ){
                    sAdr = data.getExtras().getString("startAddress");
                    sLat = data.getExtras().getDouble("startLatitude");
                    sLng = data.getExtras().getDouble("startLongitude");
                    wData = data.getExtras().getString("weatherDescription",wData);
                    wTemp = data.getExtras().getString("weatherTemperature",wTemp);
                    startTextFinal.setText(sAdr);

                }
                break;

        }

    }


}



