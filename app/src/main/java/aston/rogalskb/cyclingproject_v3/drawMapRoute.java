package aston.rogalskb.cyclingproject_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import aston.rogalskb.cyclingproject_v3.R;
import aston.rogalskb.cyclingproject_v3.mapfragmenthelp.fetchURL;
import aston.rogalskb.cyclingproject_v3.mapfragmenthelp.taskLoadedCB;

public class drawMapRoute extends AppCompatActivity implements OnMapReadyCallback, taskLoadedCB {
    GoogleMap map;
    Button drawRoute;
    MarkerOptions origin,destination;
    Polyline polyLines;
    String apiKey;
    String oAddress, dAddress, wData, wTemperature;
    String distance,time;
    Double oLat, oLng, dLat, dLng;
    LatLng originCoordinates, destinationCoordinates;
    TextView weatherText,timeText,distanceText,temperatureText;
    FloatingActionButton saveRouteButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_map_route);

        drawRoute = findViewById(R.id.mapButton);

        weatherText = findViewById(R.id.drawRouteWeather);
        timeText = findViewById(R.id.drawRouteTime);
        distanceText = findViewById(R.id.drawRouteDistance);
        temperatureText = findViewById(R.id.drawRouteTemperature);

        saveRouteButton = findViewById(R.id.drawRouteSaveRouteButton);

        saveRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(drawMapRoute.this,saveNewRoute.class);
                intent.putExtra("originName",oAddress);
                intent.putExtra("originLat",oLat);
                intent.putExtra("originLon",oLng);

                intent.putExtra("destinationName",dAddress);
                intent.putExtra("destinationLat",dLat);
                intent.putExtra("destinationLon",dLng);


                startActivity(intent);

            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MapFragment);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }

        apiKey = getString(R.string.apiKEY);

        final Intent i = getIntent();
        if(i != null){
            oAddress = i.getExtras().getString("startAddressMap");
            oLat = i.getExtras().getDouble("startLatitudeMap");
            oLng = i.getExtras().getDouble("startLongitudeMap");

            wData = i.getExtras().getString("weatherDescription");
            wTemperature = i.getExtras().getString("weatherTemperature");
            Log.i("12345678", "Temp is " + wTemperature + " Weather description " + wData);
            //weatherText.setTextColor(Color.RED);
            weatherText.setText(wData);
            //temperatureText.setTextColor(Color.RED);
            temperatureText.setText(wTemperature);

            dAddress = i.getExtras().getString("endAddressMap");
            dLat = i.getExtras().getDouble("endLatitudeMap");
            dLng = i.getExtras().getDouble("endLongitudeMap");
        }


        destinationCoordinates = new LatLng(dLat,dLng);
        originCoordinates = new LatLng(oLat,oLng);

        origin = new MarkerOptions().position(originCoordinates).title(oAddress);
        destination = new MarkerOptions().position(destinationCoordinates).title(dAddress);

        drawRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildUrl(origin.getPosition(),destination.getPosition(),"bicycling");

                new fetchURL(drawMapRoute.this).execute(url,"bicycling");

                getSharedPreferences();


                animateMap(originCoordinates,destinationCoordinates);

                Log.i("12345", "The test locations are " + oAddress + " " + "Origin lan " + oLat + " Origin lng " + oLng + " " +  dAddress + " Destination lan " + dLat + " Destination lng " + dLng);
            }
        });




    }



    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        map.addMarker(origin);
        Log.i("tag123", "Origin is currently" + origin);
        map.addMarker(destination);
        Log.i("tag1234", "destination is currently" + destination);

    }

    private String buildUrl(LatLng origin, LatLng destination, String directionMode) {

        // Pass the origin of route
        String strOrigin = "origin=" + Double.toString(origin.latitude) + "," + Double.toString(origin.longitude);
        // Pass the destination of route
        String strDestination = "destination=" + Double.toString(destination.latitude) + "," + Double.toString(destination.longitude);
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDestination + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + apiKey;
        Log.i("URL", "The url created is" + url);
        return url;
    }


    @Override
    public void onTaskDone(Object... values) {

        if(polyLines != null){
                polyLines.remove();
        }
        polyLines = map.addPolyline((PolylineOptions) values[0]);

        //polyLines = map.addPolyline(((PolylineOptions) values[0]).width(10).color(Color.RED).geodesic(true));

    }

    //Animate the map to center in on the two markers
    public void animateMap(LatLng o, LatLng d){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(o);
        builder.include(d);
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.2);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
        map.animateCamera(cameraUpdate);

    }

    public void getSharedPreferences(){
        timeDistanceObject timeDistanceObject = new timeDistanceObject();
        timeText.setText(timeDistanceObject.getTime());
        distanceText.setText(timeDistanceObject.getDistance());

    }



}
