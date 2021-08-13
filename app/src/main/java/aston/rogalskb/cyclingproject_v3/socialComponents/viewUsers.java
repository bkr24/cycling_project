package aston.rogalskb.cyclingproject_v3.socialComponents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aston.rogalskb.cyclingproject_v3.R;
import aston.rogalskb.cyclingproject_v3.UsersAdapter;
import aston.rogalskb.cyclingproject_v3.*;

public class viewUsers extends AppCompatActivity {

    RecyclerView recyclerView;
    UsersAdapter usersAdapter;
    ArrayList<userModel> usersList;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        recyclerView = findViewById(R.id.viewUsersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        //Code for removing the user from seeing himself in chat list
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList = new ArrayList<userModel>() {};
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    userModel userModel = dataSnapshot1.getValue(userModel.class);
                    if(userID.equals(userModel.getUid())){
                        //Don't add anything
                    } else {
                        usersList.add(userModel);
                    }
                }
                usersAdapter = new UsersAdapter(viewUsers.this,usersList);
                recyclerView.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(viewUsers.this,"An error has occured loading content",Toast.LENGTH_SHORT).show();

            }
        });
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
                Intent i = new Intent(viewUsers.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.menuSavedRoutes:
                Intent i1 = new Intent(viewUsers.this, aston.rogalskb.cyclingproject_v3.savedRoutes.class);
                startActivity(i1);
                return true;
            case R.id.menuMessaging:
                Intent i2 = new Intent(viewUsers.this, viewUsers.class);
                startActivity(i2);
                return true;
            case R.id.menuPosts:
                Intent i3 = new Intent(viewUsers.this, aston.rogalskb.cyclingproject_v3.socialComponents.viewPosts.class);
                startActivity(i3);
                return true;
            case R.id.menuLogout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i4 = new Intent(viewUsers.this, aston.rogalskb.cyclingproject_v3.socialComponents.loginPage.class);
                startActivity(i4);
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + itemID);
        }
    }



}
