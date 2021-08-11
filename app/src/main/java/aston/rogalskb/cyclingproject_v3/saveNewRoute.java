package aston.rogalskb.cyclingproject_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import aston.rogalskb.cyclingproject_v3.R;

public class saveNewRoute extends AppCompatActivity {
    public TextView originAddress,destinationAddress,originLatLon,destinationLatLon;
    public EditText routeName;
    public Button addButton;
    public String originAdr,destinationAdr;
    Double originLat,originLon,destinationLat,destinationLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_route);

        originAddress = findViewById(R.id.saveRouteOrigin);
        originLatLon = findViewById(R.id.saveRouteOriginLatLon);
        destinationAddress = findViewById(R.id.saveRouteDestination);
        destinationLatLon = findViewById(R.id.saveRouteDestinationLatLon);

        routeName = findViewById(R.id.saveRouteName);

        addButton = findViewById(R.id.saveRouteAdd);


        final Intent i = getIntent();
        if(i != null){
            Log.i("1234567", "The extras that were sent to this activity are" + i.getExtras().getString("originName",""));

            originAdr = i.getExtras().getString("originName","");
            destinationAdr = i.getExtras().getString("destinationName","");

            Double tempOriginLat = i.getExtras().getDouble("originLat",0);
            originLat = tempOriginLat;
            Double tempOriginLon = i.getExtras().getDouble("originLon",0);
            originLon = tempOriginLon;

            Double tempDestinationLat = i.getExtras().getDouble("destinationLat",0);
            destinationLat = tempDestinationLat;
            Double tempDestinationLon = i.getExtras().getDouble("destinationLon",0);
            destinationLon = tempDestinationLon;

            originAddress.setText(originAdr);
            destinationAddress.setText(destinationAdr);

            String oriLotLan = "Latitude : "+ tempOriginLat.toString() + " Longitude " + tempOriginLon.toString();
            String desLotLan = "Latitude : "+ tempDestinationLat.toString() + " Longitude " + tempDestinationLon.toString();

            originLatLon.setText(oriLotLan);
            destinationLatLon.setText(desLotLan);

        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelp = new DatabaseHelper(saveNewRoute.this);
                dbHelp.addRoute(routeName.getText().toString().trim(),originAdr,originLat,originLon,destinationAdr,destinationLat,destinationLon);
                Intent i = new Intent(saveNewRoute.this,savedRoutes.class);
                startActivity(i);
            }
        });




    }
}
