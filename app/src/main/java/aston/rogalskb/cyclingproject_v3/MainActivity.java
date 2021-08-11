package aston.rogalskb.cyclingproject_v3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import aston.rogalskb.cyclingproject_v3.*;

import aston.rogalskb.cyclingproject_v3.R;
import aston.rogalskb.cyclingproject_v3.socialComponents.addPostActivity;
import aston.rogalskb.cyclingproject_v3.socialComponents.loginPage;
import aston.rogalskb.cyclingproject_v3.socialComponents.profileActivity;
import aston.rogalskb.cyclingproject_v3.socialComponents.viewPosts;
import aston.rogalskb.cyclingproject_v3.socialComponents.viewUsers;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    private CardView planRoute;
    private CardView savedRoutes;
    private CardView userProfile;
    private CardView viewPosts;
    private CardView viewMessaging;
    private CardView logOut;
    private CardView createPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find buttons by their id
        planRoute = findViewById(R.id.mainPlanRoute);
        savedRoutes = findViewById(R.id.mainSavedRoutes);
        userProfile = findViewById(R.id.mainUserProfile);
        viewPosts = findViewById(R.id.mainPosts);
        viewMessaging = findViewById(R.id.mainMessaging);
        logOut = findViewById(R.id.mainLogOut);
        createPost = findViewById(R.id.mainCreatePost);




        firebaseAuth = FirebaseAuth.getInstance();


        //Code to open the activity upon clicking on it.

        checkUserStatus();

        planRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, planRouteActivity.class);
                startActivity(i);
            }
        });

        savedRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, savedRoutes.class);
                startActivity(i);
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, profileActivity.class);
                startActivity(i);

            }
        });

        viewMessaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, viewUsers.class);
                startActivity(i);
            }
        });


        viewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, viewPosts.class);
                startActivity(i);
            }
        });



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUserStatus();
            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, addPostActivity.class);
                startActivity(i);
            }
        });



    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            //don't do shit as user is fucking here geez
        } else {
            Intent intent = new Intent(this,loginPage.class);
            startActivity(intent);
            //finish();
        }
    }



}

