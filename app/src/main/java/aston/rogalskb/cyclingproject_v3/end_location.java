package aston.rogalskb.cyclingproject_v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import aston.rogalskb.cyclingproject_v3.R;

public class end_location extends AppCompatActivity {
    PlacesClient placesClient;
    LatLng currentLocation;
    Double wLat, wLng;
    Place endPlace;
    String endAddress;
    String id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_location);


        String apiKey = getString(R.string.apiKEY);

        Button finishEnd =  findViewById(R.id.endLocationFinish);
        final TextView endText = findViewById(R.id.endTextView);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), apiKey);
        }


        /*
        if(getIntent().getExtras().getString("id") != null){
            id = getIntent().getExtras().getString("id");
            //id = bundle.getString("id");
        }

         */

        Intent intent = this.getIntent();
        try { if(intent.getExtras().containsKey("id")){
            id = intent.getStringExtra("id");
        }

        } catch (NullPointerException E) {
            Log.i("bartek", "onCreate: Null Pointer exception was triggered");

        }

        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragmentEnd =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_end);

        autocompleteSupportFragmentEnd.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));

        autocompleteSupportFragmentEnd.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                resetPlace();
                endPlace = place;
                resetLocation();
                setLocation(latLng);
                //Log.i("PlacesApi", "onPlaceSelected: "+ latLng.latitude+"\n"+latLng.longitude);
                //Get longitude and langitude into variables to use in open weather API
                setParametersForTransfer(latLng.latitude,wLng = latLng.longitude,place.getName());
                endText.setText(endAddress);
                //wLat = latLng.latitude;
                //wLng = latLng.longitude;
                //address = place.getAddress();
                //getWeather();

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });


        finishEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent();
                goBack.putExtra("endAddress",endAddress);
                goBack.putExtra("endLatitude",wLat);
                goBack.putExtra("endLongitude",wLng);

                if(id != null){
                    goBack.putExtra("id",id);
               }


                if(getParent() == null){
                    setResult(RESULT_OK,goBack);
                } else{
                    getParent().setResult(Activity.RESULT_OK,goBack);
                }


                if(endAddress == null){
                    Toast.makeText(end_location.this,"No location selected",Toast.LENGTH_LONG).show();
                }

                finish();


            }
        });





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

    public void resetPlace(){
        endPlace = null;
    }



    public void setParametersForTransfer(Double latitude, Double longitude, String street){
        wLat = latitude;
        wLng = longitude;
        endAddress = street;
    }


}
