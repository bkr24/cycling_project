package aston.rogalskb.cyclingproject_v3.socialComponents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import aston.rogalskb.cyclingproject_v3.R;

public class addPostActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference userDBref;

    ImageView imageToUpload;
    EditText postTitle,postDescription;
    Button uploadPostBTN;

    private static final int CAMERA_REQUEST = 401;
    private static final int GALLERY_REQUEST = 402;


    private static final int CAMERA_IMAGE_PICK = 501;
    private static final int GALLERY_IMAGE_PICK = 502;

    String[] cameraPermission;
    String[] galleryPermission;
    String timeStamp;

    Uri image_uri = null;

    String userEmail,userID, dp, uName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        galleryPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        userDBref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDBref.orderByChild("email").equalTo(userEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dbs: dataSnapshot.getChildren()){
                    userEmail = ""+ dbs.child("email").getValue();
                    dp = ""+ dbs.child("image").getValue();
                    uName = ""+ dbs.child("name").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imageToUpload = findViewById(R.id.addPostImageView);
        postTitle = findViewById(R.id.addPostTitleET);
        postDescription = findViewById(R.id.addPostMessageET);
        uploadPostBTN = findViewById(R.id.addPostPublishBTN);

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        galleryPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};






        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create post");

        uploadPostBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = postTitle.getText().toString().trim();
                String description = postDescription.getText().toString().trim();

                if(TextUtils.isEmpty(description)){
                    Toast.makeText(addPostActivity.this,"Description missing...",Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(title)){
                    Toast.makeText(addPostActivity.this,"Title missing...",Toast.LENGTH_SHORT).show();
                }


                if(image_uri == null){
                    //Post to db with image
                    uploadDataToDB(title,description,"noImage");
                } else {
                    //Post with an image
                    uploadDataToDB(title,description,String.valueOf(image_uri));
                }

            }

        });

        imageToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerDialog();

            }
        });



    }


    public void checkUser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            //Move to login activity
            Intent intent = new Intent(addPostActivity.this,loginPage.class);
            startActivity(intent);
        } else {
            //If user is signed in don't do shit :)
            userEmail = user.getEmail();
            userID = user.getUid();
        }
    }

    public void imagePickerDialog(){
        String[] choices = {"Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload image from...");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    /*
                    case 0:
                        //If camera is choosen
                        if(checkForCameraPermission() == false){
                            getCameraPermission();
                        } else {
                            pickCameraImage();
                        }
                        break;

                     */
                    case 0:
                        if(checkForStoragePermission() == false){
                            getStoragePermission();
                        } else {
                            pickGalleryImage();
                        }
                        //If gallery is choosen
                        break;
                }
            }
        });

        builder.show();

    }


   public void uploadDataToDB(final String title, final String Description, String uri){

        timeStamp = String.valueOf(System.currentTimeMillis());
        String filePath = "Posts/" + "post_"  + timeStamp;


        if(!uri.equals("noImage")){
            //Post with image
            StorageReference  storageReference = FirebaseStorage.getInstance().getReference().child(filePath);
            storageReference.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            //while(!uriTask.isSuccessful());
                            while (!uriTask.isSuccessful());

                            String uriDownload = uriTask.getResult().toString();

                            if(uriTask.isSuccessful()){
                                HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("uId",userID);
                                hashMap.put("uEmail",userEmail);
                                hashMap.put("uDp",dp);
                                hashMap.put("uName",uName);
                                hashMap.put("postTitle",title);
                                hashMap.put("postDesc",Description);
                                hashMap.put("postImage",uriDownload);
                                hashMap.put("postTime",timeStamp);

                                //Data path to storage
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(addPostActivity.this,"Post created!",Toast.LENGTH_SHORT).show();
                                                Intent goBackHome = new Intent(addPostActivity.this, viewPosts.class);
                                                startActivity(goBackHome);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //post wasn't added
                                                Toast.makeText(addPostActivity.this,"Post failed!",Toast.LENGTH_SHORT).show();


                                            }
                                        });

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {
            //Post without an image is uploaded
            HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("uId",userID);
            hashMap.put("uEmail",userEmail);
            hashMap.put("uDp",dp);
            hashMap.put("uName",uName);
            hashMap.put("postTitle",title);
            hashMap.put("postDesc",Description);
            hashMap.put("postImage","noImage");
            hashMap.put("postTime",timeStamp);

            //Data path to storage
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            ref.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(addPostActivity.this,"Post created!",Toast.LENGTH_SHORT).show();
                            Intent goBackHome = new Intent(addPostActivity.this, viewPosts.class);
                            startActivity(goBackHome);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //post wasn't added
                            Toast.makeText(addPostActivity.this,"Post failed!",Toast.LENGTH_SHORT).show();


                        }
                    });

        }

    }


    private void pickCameraImage(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Test Pick");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Test description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(i,CAMERA_IMAGE_PICK);


    }

    private void pickGalleryImage(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,GALLERY_IMAGE_PICK);
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private boolean checkForStoragePermission(){
        boolean r = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return r;
    }

    private void getStoragePermission(){
        ActivityCompat.requestPermissions(this,galleryPermission,GALLERY_REQUEST);

    }


    private boolean checkForCameraPermission(){
        boolean r = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean r1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return r && r1;
    }

    private void getCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            /*
            case CAMERA_REQUEST:{
                if(grantResults.length >0){
                    boolean cameraPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storagePermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(storagePermissionGranted && cameraPermissionGranted){
                        pickCameraImage();
                    } else {
                        Toast.makeText(this,"Storage and camera permissions required",Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
            break;

             */

            case GALLERY_REQUEST:
                if(grantResults.length > 0){
                    boolean storagePermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(storagePermissionGranted == true){
                        pickGalleryImage();
                    } else {
                        Toast.makeText(this,"Storage permission required",Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            break;
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            /*
            if(requestCode == CAMERA_IMAGE_PICK){
                //Image from camera is used

                imageToUpload.setImageURI(image_uri);


            }

             */

            if(requestCode == GALLERY_IMAGE_PICK){
                //Get the image from gallery my boy

                image_uri = data.getData();

                imageToUpload.setImageURI(image_uri);

                //imageToUpload.setImageURI(image_uri);


            }
        }
    }
}
