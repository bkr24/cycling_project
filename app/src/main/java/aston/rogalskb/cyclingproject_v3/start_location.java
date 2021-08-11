package aston.rogalskb.cyclingproject_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import aston.rogalskb.cyclingproject_v3.R;

//import afu.org.checkerframework.checker.oigj.qual.O;

public class start_location extends AppCompatActivity {
    PlacesClient placesClient2;
    LatLng currentLocation2;
    Double sLat, sLng;
    Place startPlace;
    String startAddress;
    String wData, wTemp;
    FusedLocationProviderClient locationProvider;
    TextView startText;
    private RequestQueue mQ;
    String openWeatherApi;
    String address;
    String id;


    private static final int REQUEST_LOCATION_PERMISSION = 42;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_location);


        String apiKey = getString(R.string.apiKEY);
        openWeatherApi = getString(R.string.weatherAPI);


        Button finishStart =  findViewById(R.id.startLocationFinish);
        startText = findViewById(R.id.startTextView);

        Button currentLocationGet = findViewById(R.id.startCurrentLocation);

        locationProvider = LocationServices.getFusedLocationProviderClient(this);


        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), apiKey);
        }

        Log.i("bart12","This works ");
        //Log.i("bart123","The ID is +" + getIntent().getExtras().getString("id"));



        Intent intent = this.getIntent();
        try { if(intent.getExtras().containsKey("id")){
            id = intent.getStringExtra("id");
        }

        } catch (NullPointerException E) {
            Log.i("bartek", "onCreate: Null Pointer exception was triggered");

        }

        /*
        Intent intent = getIntent();
        if(intent.getExtras().getString("id") != null){
            id = getIntent().getExtras().getString("id");
            Log.i("id123","the ID is equal to " + id);
        }

         */

        /*
        Intent intent = this.getIntent();
        if(intent != null){
            id = intent.getStringExtra("id");
            Log.i("bart12","Intent not null ");
        } else {
            Log.i("bart12","Intent null ");

        }

         */

        //Log.i("id123","the ID is equal to " + id);


        placesClient2 = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragmentEnd =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_start);

        autocompleteSupportFragmentEnd.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));

        autocompleteSupportFragmentEnd.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                resetPlace();
                startPlace = place;
                resetLocation();
                setLocation(latLng);
                setParametersForTransfer(latLng.latitude,sLng = latLng.longitude,place.getName());
                changeTextView(startAddress);
                getWeatherData();

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });


        finishStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent();
                goBack.putExtra("startAddress",startAddress);
                goBack.putExtra("startLatitude",sLat);
                goBack.putExtra("startLongitude",sLng);
                goBack.putExtra("weatherDescription",wData);
                goBack.putExtra("weatherTemperature",wTemp);

                //Check if no location was selected
                if(startAddress == null){
                    Toast.makeText(start_location.this,"No location selected",Toast.LENGTH_LONG).show();
                }




                if(id != null){
                    goBack.putExtra("id",id);
                }
                if(getParent() == null){
                    setResult(RESULT_OK,goBack);
                } else{
                    getParent().setResult(Activity.RESULT_OK,goBack);
                }

                Log.i("start1", "The start location at end is " + startAddress + " with the coordinates " + sLng + " and " + sLat);


                finish();


            }
        });

        currentLocationGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(start_location.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_PERMISSION);
                }
                }
        });

        mQ = Volley.newRequestQueue(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==REQUEST_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            } else {
                Toast.makeText(this,"DENIED", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setLocation(LatLng loc){
        currentLocation2 = loc;
    }

    public LatLng getLocation(){
        return currentLocation2;

    }

    public void resetLocation(){
        currentLocation2 = null;
    }

    public void resetPlace(){
        startPlace = null;
    }



    public void setParametersForTransfer(Double latitude, Double longitude, String street){
        sLat = latitude;
        sLng = longitude;
        startAddress = street;
    }

    public void setWeatherTransferData(String weather, String temp){
        wData = weather;
        wTemp = temp;
    }


    public void changeTextView(String t){
        startText.setText(t);
    }

    public void getCurrentLocation(){
        Log.i("12345", "Is the client connected ");

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(start_location.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(start_location.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int locationIndex = locationResult.getLocations().size() -1;
                            double lat = locationResult.getLocations().get(locationIndex).getLatitude();
                            double lon = locationResult.getLocations().get(locationIndex).getLongitude();
                            LatLng loc = new LatLng(lat,lon);
                            setLocation(loc);
                            Log.i("12345", "Lan is " + lat + " Lon is " + lon);
                            getAddressFromCoordinates(start_location.this,lat,lon);
                            getWeatherData();
                        }
                    }
                }, Looper.getMainLooper());
    }

    public void getAddressFromCoordinates(Context context, Double lat, Double lon){
        Log.i("12345", "The function was triggered");

        Geocoder geocoder;
        geocoder = new Geocoder(this,Locale.getDefault());

        try {
            List<Address> addresses;
            addresses = geocoder.getFromLocation(lat,lon,1);
            if(addresses != null){
                address = addresses.get(0).getAddressLine(0);
                Log.i("12345", "The address is not null " + address);
                changeTextView(address);
                setParametersForTransfer(lat,lon,address);
            } else {
                address = "Address was null";
                Log.i("12345", "The address is null " + address);
                setParametersForTransfer(lat,lon,address);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("12345", "The address is " + address);

    }

    public void getWeatherData(){
        if(getLocation() != null){
            Double lonWeather = getLocation().longitude;
            Double latWeather = getLocation().latitude;
            //String longitude = String.format("%.2f", lonWeather);
            //String latitude = String.format("%.2f", latWeather);
            //String url = HttpRequest.excuteGet("https://samples.openweathermap.org/data/2.5/weather?lat="+ lan1 + "&lon=" + lng1 + "&appid=" + openWeatherApi);
            Log.i("Success123","https://api.openweathermap.org/data/2.5/weather?lat="+ latWeather + "&lon=" + lonWeather + "&appid=" + openWeatherApi);

            String url = "https://api.openweathermap.org/data/2.5/weather?lat="+ lonWeather + "&lon=" + latWeather + "&appid=" + openWeatherApi;

            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Get the proper data from the JSON format, and fix the fucking temperature lol
                        JSONObject main = response.getJSONObject("main");
                        Log.i("W123", main.getString("temp"));
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject w = weatherArray.getJSONObject(0);


                        String wDesc = w.getString("description");
                        //String location = response.getString("name");



                        //JSONObject object = response.getJSONObject("0");
                        Double tempC = main.getDouble("temp") - 273.15;
                        //String temperature = String.valueOf(tempC);
                        String temperature =  String.format("%.2f", tempC) + " *C";
                        //String weatherCondition = object.getString("description");
                        //String weatherSummary = wDesc +  "  "+ temperature;
                        setWeatherTransferData(wDesc,temperature);
                        Log.i("Weather", "The weather at this location is " + wDesc + " with temperature of " + temperature);


                    } catch (JSONException e){
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQ.add(jor);

        }

    }




}

