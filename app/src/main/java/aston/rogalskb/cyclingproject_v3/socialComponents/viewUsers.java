package aston.rogalskb.cyclingproject_v3.socialComponents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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



}
