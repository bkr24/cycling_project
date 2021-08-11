package aston.rogalskb.cyclingproject_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import aston.rogalskb.cyclingproject_v3.R;

public class ViewWeather extends AppCompatActivity {
    PlacesClient placesClient;
    Double wLat, wLng;
    TextView t1_location,t2_weather_description,t3_temp,t4_date;
    LatLng currentLocation;
    String openWeatherApi;
    private static ViewWeather instance;
    private RequestQueue mQ;
    String[] responseAnwser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_weather);
        //currentLocation = null;
        //wLat = null;
        //wLng = null;

        String apiKey = getString(R.string.apiKEY);

        Button parseButton = findViewById(R.id.doWeatherBtn);

        openWeatherApi = getString(R.string.weatherAPI);
        //Weather related text views


        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_weather);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                resetLocation();
                setLocation(latLng);
                Log.i("PlacesApi", "onPlaceSelected: "+ latLng.latitude+"\n"+latLng.longitude);
                //Get longitude and langitude into variables to use in open weather API
                wLat = latLng.latitude;
                wLng = latLng.longitude;
                //getWeather();

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });




        parseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherData();
            }
        });

        mQ = Volley.newRequestQueue(this);




    }

    public void getWeatherData(){
        if(getLocation() != null){
            Double longG = getLocation().longitude;
            Double latigG = getLocation().latitude;
            String longitude = String.format("%.2f", longG);
            String latitude = String.format("%.2f", latigG);
            t1_location = findViewById(R.id.tView1);
            t2_weather_description = findViewById(R.id.tView2);
            t3_temp = findViewById(R.id.tView3);
            t4_date = findViewById(R.id.tView4);
            //String url = HttpRequest.excuteGet("https://samples.openweathermap.org/data/2.5/weather?lat="+ lan1 + "&lon=" + lng1 + "&appid=" + openWeatherApi);
            Log.i("Success123","https://api.openweathermap.org/data/2.5/weather?lat="+ latitude + "&lon=" + longitude + "&appid=" + openWeatherApi);

            String url = "https://api.openweathermap.org/data/2.5/weather?lat="+ latitude + "&lon=" + longitude + "&appid=" + openWeatherApi;

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
                        String location = response.getString("name");



                        //JSONObject object = response.getJSONObject("0");
                        Double tempC = main.getDouble("temp") - 273.15;
                        //String temperature = String.valueOf(tempC);
                        String temperature =  String.format("%.2f", tempC) + " *C";
                        //String weatherCondition = object.getString("description");
                        Log.i("W1234", response.getString("name"));


                        // t1_location.setText(location);
                        t1_location.setText(location);
                        t2_weather_description.setText(wDesc);
                        t3_temp.setText(temperature);


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

    public String[] getWeatherDataWithCoordinates(Double lat, Double lng){
            Double longG = lng;
            Double latigG = lat;
            String longitude = String.format("%.2f", longG);
            String latitude = String.format("%.2f", latigG);
            //t1_location = findViewById(R.id.tView1);
            //t2_weather_description = findViewById(R.id.tView2);
           // t3_temp = findViewById(R.id.tView3);
            //t4_date = findViewById(R.id.tView4);
            //String url = HttpRequest.excuteGet("https://samples.openweathermap.org/data/2.5/weather?lat="+ lan1 + "&lon=" + lng1 + "&appid=" + openWeatherApi);
            Log.i("Success123","https://api.openweathermap.org/data/2.5/weather?lat="+ latitude + "&lon=" + longitude + "&appid=" + openWeatherApi);

            String url = "https://api.openweathermap.org/data/2.5/weather?lat="+ latitude + "&lon=" + longitude + "&appid=" + openWeatherApi;

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
                        String location = response.getString("name");



                        //JSONObject object = response.getJSONObject("0");
                        Double tempC = main.getDouble("temp") - 273.15;
                        //String temperature = String.valueOf(tempC);
                        String temperature =  String.format("%.2f", tempC) + " *C";
                        //String weatherCondition = object.getString("description");
                        Log.i("W1234", response.getString("name"));


                        // t1_location.setText(location);
                        //t1_location.setText(location);
                        //t2_weather_description.setText(wDesc);
                       // t3_temp.setText(temperature);
                        responseAnwser[0] = wDesc;
                        responseAnwser[1] = temperature;


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
        return responseAnwser;


    }


    public void setLocation(LatLng loc){
        currentLocation = loc;
    }

    public LatLng getLocation(){
        return currentLocation;

    }

    public void resetLocation(){
        currentLocation = null;
    }


    public static ViewWeather getInstance(){
        return instance;
    }



}

