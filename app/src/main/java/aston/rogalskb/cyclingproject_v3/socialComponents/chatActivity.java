package aston.rogalskb.cyclingproject_v3.socialComponents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import aston.rogalskb.cyclingproject_v3.*;
import aston.rogalskb.cyclingproject_v3.socialComponents.*;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class chatActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView imageView;
    TextView userNameTV, statusTV;
    EditText editText;
    ImageButton sendButton;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String myUID;
    String otherUID;
    String theirImage;
    //Test variable
    Integer counter = 0;

    ValueEventListener seenListener;
    DatabaseReference userReferenceForSeen;
    List<chatModel> chatList;
    ChatAdapter chatAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        recyclerView = findViewById(R.id.chatRecyclerView);
        imageView = findViewById(R.id.chatProfilePic);
        userNameTV = findViewById(R.id.chatActivityReceiverName);
        statusTV = findViewById(R.id.chatActivityStatus);
        editText = findViewById(R.id.chatEditText);
        sendButton = findViewById(R.id.chatSendButton);

        //Linear layout for recycler view to show images
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(chatActivity.this, chatList,theirImage);
        recyclerView.setAdapter(chatAdapter);


        /*
        final Intent i = getIntent();
        if(i != null){
            Log.i("1234567", "Other UID is" + i.getStringExtra(otherUID));
            otherUID = i.getExtras().getString("oUID");

        }

         */

        Intent intent = this.getIntent();
        try { if(intent.getExtras().containsKey("oUID")){
            otherUID = intent.getStringExtra("oUID");
            Log.i("oUID", "UID is + " +otherUID);

        }

        } catch (NullPointerException E) {
            Log.i("oUID", "onCreate: Null Pointer exception was triggered");

        }
        //Intent i = getIntent();
        //Log.i("1234567", "Other UID is" + i.getStringExtra(otherUID));

        //if(i != null){
            //otherUID = i.getStringExtra("oUID");
        //}
        //otherUID = i.getStringExtra("otherUID");





        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myUID = firebaseAuth.getUid();
        databaseReference = firebaseDatabase.getReference("Users");

        Query query = databaseReference.orderByChild("uid").equalTo(otherUID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    //Get data from DB
                    String userName = "" + dataSnapshot1.child("name").getValue();
                    theirImage = "" + dataSnapshot1.child("image").getValue();
                    String status = "" + dataSnapshot1.child("onlineStatus").getValue();
                    //Set the data
                    userNameTV.setText(userName);
                    if(status.equals("online")){
                        statusTV.setText(status);
                    } else {
                        //Make timestamp the current time in the correct format
                        //Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        /*
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(Long.parseLong(status));
                        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                        String statusText = "Last online..." + dateTime;
                        statusTV.setText(statusText);

                         */

                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        try {
                            cal.setTimeInMillis(Long.parseLong(status));

                        } catch(Exception ex) {

                            ex.printStackTrace();
                        }

                        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
                        String statusText = "Last online..." + dateTime;
                        statusTV.setText(statusText);



                        /* Kinda working but need to try the old solution
                        Calendar c = Calendar.getInstance(Locale.ENGLISH);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formatedDate = sdf.format(c.getTime());
                        String statusText = "Last online..." + formatedDate;
                        statusTV.setText(statusText);

                         */

                    }

                    //Code for loading the image
                    try {
                        Picasso.get().load(theirImage).placeholder(R.drawable.ic_profile_standard).into(imageView);

                    } catch (Exception E){
                        Picasso.get().load(R.drawable.ic_profile_standard).into(imageView);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Message is sent by pressing the button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString().trim();

                if(TextUtils.isEmpty(msg)){
                    Toast.makeText(chatActivity.this,"Message is empty... cannot send",Toast.LENGTH_LONG).show();
                } else {
                    sendMessage(msg);
                }

            }
        });

        readMessages();
        sawMessage();


    }

    private void sawMessage() {
        userReferenceForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userReferenceForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    chatModel chat = ds.getValue(chatModel.class);
                    try {
                        if(chat.getReceiver().equals(myUID) && chat.getSender().equals(otherUID)){
                            HashMap<String ,Object> seenHashMap = new HashMap<>();
                            seenHashMap.put("wasSeen",true);
                            ds.getRef().updateChildren(seenHashMap);
                        }

                    } catch (Exception E){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    chatModel chat = ds.getValue(chatModel.class);
                    Log.i("1234567", "myUID is " + myUID + " otherUID  " + otherUID);
                    Log.i("1234567", "RECEIVER " + chat.getReceiver() + " SENDER ID " + chat.getSender());
                    //Log.i("1234567","Counter = " + counter++ );

                    //Log.i("666", "The message is + " + chat.getMessage());
                    Log.i("666", "One comparison is  "  + chat.getReceiver() + " AND  " + myUID + " ALSO 2ND " + chat.getSender() + " " + otherUID );
                    Log.i("777", "One comparison is  "  + chat.getReceiver() + " AND  " + myUID + " ALSO 2ND " + chat.getSender() + " " + otherUID );



                    //if((chat.getReceiver().equals(myUID) && chat.getSender().equals(otherUID) ) || chat.getReceiver().equals(otherUID) && chat.getSender().equals(myUID))
                    if(( myUID.equals(chat.getReceiver()) && otherUID.equals(chat.getSender()) ) || otherUID.equals(chat.getReceiver()) && myUID.equals(chat.getSender()))
                    {
                        chatList.add(chat);
                        Log.i("666", "The message is + " + chat.getMessage());
                    }

                    chatAdapter = new ChatAdapter(chatActivity.this,chatList,theirImage);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Set online status as current timestamp
        String timeStamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timeStamp);

        userReferenceForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        checkOnlineStatus("online");
        //readMessages();
        super.onResume();
    }

    private void sendMessage(String message){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        //Get the current time
        String timeStamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",myUID);
        hashMap.put("receiver",otherUID);
        hashMap.put("message",message);
        hashMap.put("timeStamp",timeStamp);
        hashMap.put("wasSeen",false);


        dbRef.child("Chats").push().setValue(hashMap);

        //Clean edit text field after sending message
        editText.setText("");
    }



    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            //don't do shit as user is fucking here geez
            myUID = user.getUid();
        } else {
            Intent intent = new Intent(this,loginPage.class);
            startActivity(intent);
            finish();
        }
    }


    private void checkOnlineStatus(String stat){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUID);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", stat);

        //Update online status value for the current user
        databaseReference.updateChildren(hashMap);

    }



}
