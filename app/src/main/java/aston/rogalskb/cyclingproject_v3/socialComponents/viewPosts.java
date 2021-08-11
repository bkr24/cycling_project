package aston.rogalskb.cyclingproject_v3.socialComponents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import aston.rogalskb.cyclingproject_v3.R;

public class viewPosts extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdaptor postAdaptor;
    ArrayList<post_model> postList;
    DatabaseReference databaseReference;
    //LinearLayoutManager layoutManager;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        /*
        recyclerView = findViewById(R.id.viewPostsRV);
        recyclerView.setAdapter(postAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewPosts.this));

        databaseReference = FirebaseDatabase.getInstance().getReference();

         */

        //postList.clear();
        //postList = new List<>();


        //loadPosts();

        //Log.i("bartek","Is the list empty?" + postList.get(0).toString());
        recyclerView = findViewById(R.id.viewPostsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<post_model>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    post_model postModel = dataSnapshot1.getValue(post_model.class);
                    postList.add(postModel);
                }
                postAdaptor = new PostAdaptor(viewPosts.this,postList);
                recyclerView.setAdapter(postAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(viewPosts.this,"Welp... an error has occured",Toast.LENGTH_SHORT).show();

            }
        });

    }




    /*
    private void loadPosts() {
        Query query = databaseReference.child("Posts");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:  dataSnapshot.getChildren()){

                    post_model postModel = new post_model();
                    postModel.setPostDesc(ds.child("postDesc").getValue().toString());
                    postModel.setPostImage(ds.child("postImage").getValue().toString());
                    postModel.setPostTime(ds.child("postTime").getValue().toString());
                    postModel.setPostTitle(ds.child("postTitle").getValue().toString());
                    postModel.setuDp(ds.child("uDp").getValue().toString());
                    postModel.setuEmail(ds.child("uEmail").getValue().toString());
                    postModel.setuId(ds.child("uID").getValue().toString());
                    postModel.setuName(ds.child("uName").getValue().toString());

                    postList.add(postModel);

                }

                postAdaptor = new PostAdaptor(getApplicationContext(),postList);
                recyclerView.setAdapter(postAdaptor);
                postAdaptor.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

     */
        /*databaseReference = FirebaseDatabase.getInstance().getReference("Posts/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    post_model postModel = ds.getValue(post_model.class);

                    postList.add(postModel);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(viewPosts.this,"It failed!",Toast.LENGTH_SHORT).show();

            }
        });

         */
    //}



}
