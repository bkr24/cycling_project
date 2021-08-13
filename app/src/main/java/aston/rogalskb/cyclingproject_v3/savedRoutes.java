package aston.rogalskb.cyclingproject_v3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import aston.rogalskb.cyclingproject_v3.R;
import aston.rogalskb.cyclingproject_v3.socialComponents.viewUsers;

public class savedRoutes extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ArrayList<String> routeId,routeName, dbOriginName, dbDestinationName;
    ArrayList<Double> dbOriginLat,dbOriginLon,dbDestinationLat,dbDestinationLon;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_routes);

        recyclerView = findViewById(R.id.savedRoutesRecyclerView);

        databaseHelper = new DatabaseHelper(savedRoutes.this);
        routeId = new ArrayList<>();
        routeName = new ArrayList<>();
        dbOriginName = new ArrayList<>();
        dbDestinationName = new ArrayList<>();

        dbOriginLat = new ArrayList<>();
        dbOriginLon = new ArrayList<>();
        dbDestinationLat= new ArrayList<>();
        dbDestinationLon= new ArrayList<>();

        saveDataToArrayLists();

        customAdapter = new CustomAdapter(savedRoutes.this,this,routeId,routeName,dbOriginName,dbDestinationName,dbOriginLat,dbOriginLon,dbDestinationLat,dbDestinationLon);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(savedRoutes.this));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 11){
            customAdapter.notifyDataSetChanged();
            recreate();
        }


    }

    void saveDataToArrayLists(){
        Cursor cursor = databaseHelper.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this,"No data stored",Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){

                routeId.add(cursor.getString(0));
                routeName.add(cursor.getString(1));

                dbOriginName.add(cursor.getString(2));
                dbOriginLat.add(cursor.getDouble(3));
                dbOriginLon.add(cursor.getDouble(4));


                dbDestinationName.add(cursor.getString(5));
                dbDestinationLat.add(cursor.getDouble(6));
                dbDestinationLon.add(cursor.getDouble(7));



            }
        }
    }

    //Add menu navigation

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    //When menu is clicked


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID){
            case R.id.menuHome:
                Intent i = new Intent(savedRoutes.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.menuSavedRoutes:
                Intent i1 = new Intent(savedRoutes.this, aston.rogalskb.cyclingproject_v3.savedRoutes.class);
                startActivity(i1);
                return true;
            case R.id.menuMessaging:
                Intent i2 = new Intent(savedRoutes.this, viewUsers.class);
                startActivity(i2);
                return true;
            case R.id.menuPosts:
                Intent i3 = new Intent(savedRoutes.this, aston.rogalskb.cyclingproject_v3.socialComponents.viewPosts.class);
                startActivity(i3);
                return true;
            case R.id.menuLogout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i4 = new Intent(savedRoutes.this, aston.rogalskb.cyclingproject_v3.socialComponents.loginPage.class);
                startActivity(i4);
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + itemID);
        }
    }


}
