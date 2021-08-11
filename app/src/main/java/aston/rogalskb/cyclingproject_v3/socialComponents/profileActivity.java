package aston.rogalskb.cyclingproject_v3.socialComponents;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import aston.rogalskb.cyclingproject_v3.R;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class profileActivity extends AppCompatActivity {

    TextView email, userName;
    ImageView userProfileImage, coverImage;
    FloatingActionButton floatingActionButton;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    private static final int CAMERA_REQUEST = 451;
    private static final int STORAGE_REQUEST = 452;
    private static final int CAMERA_IMAGE_PICK_REQUEST = 453;
    private static final int GALLERY_IMAGE_PICK_REQUEST = 454;

    String coverOrProfilePicture;


    String[] cameraPermission; //= new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};;
    String[] galleryPermission; //= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};;
    String imageFilePath = "Users_Profile_Cover_Images/";

    Uri image_uri,camera_uri = null;
    File camera_file;

    public profileActivity(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();
        email = findViewById(R.id.profileEmailTextView);
        userName = findViewById(R.id.profileNameTextView);
        userProfileImage = findViewById(R.id.profilePicture);
        coverImage = findViewById(R.id.profileCoverImage);
        floatingActionButton = findViewById(R.id.profileFAB);

        //disableEror();




        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        galleryPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dbs : dataSnapshot.getChildren()) {
                    String savedName = "" + dbs.child("name").getValue();
                    String savedEmail = "" + dbs.child("email").getValue();
                    String savedImage = "" + dbs.child("image").getValue();
                    String saveCoverImage = "" + dbs.child("cover_image").getValue();

                    email.setText(savedEmail);
                    userName.setText(savedName);
                    try {
                        //Try loading database image
                        Picasso.get().load(savedImage).into(userProfileImage);

                    } catch (Exception e) {
                        //If fails load default image
                        Picasso.get().load(R.drawable.ic_add_image).into(userProfileImage);

                    }

                    try {
                        //Try loading database image
                        Picasso.get().load(saveCoverImage).into(coverImage);

                    } catch (Exception e) {
                        //If fails load default image
                        Picasso.get().load(R.drawable.ic_add_image).into(coverImage);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileDialog();
            }
        });


    }


    /*
    public void disableEror(){
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

     */

    private void uploadPhotoToDB(Uri uri) {
        String fileNameAndPath = imageFilePath + "" + coverOrProfilePicture +"_"+firebaseUser.getUid();
        StorageReference storageReference2 = storageReference.child(fileNameAndPath);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uriDownload = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            HashMap<String,Object> result = new HashMap<>();
                            result.put(coverOrProfilePicture,uriDownload.toString());
                            databaseReference.child(firebaseUser.getUid()).updateChildren(result)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(profileActivity.this,"Image added!",Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(profileActivity.this,"Error has occured!",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {

                        }


                        Toast.makeText(profileActivity.this,"Image added!",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profileActivity.this,"Failed adding image!",Toast.LENGTH_SHORT).show();

                    }
                });

    }



    //I will god damn disable camera option if it doesn't keep working - too much time went into trying to fix this piece of shit code :)

    /*
    private void pickCameraImage() {

        //ContentValues contentValues = new ContentValues();
        //contentValues.put(MediaStore.Images.Media.TITLE, "Test Pick");
        //contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Test description");
        //image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //image_uri = profileActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //camera_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        //String fileName = "CameraImage";
        //camera_file = new File(fileName);


        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera_uri = Uri.parse("file:///sdcard/photo.jpg");
        //disableEror();
        //camera_uri = Uri.fromFile(getOutputMediaFile());
        i.putExtra(MediaStore.EXTRA_OUTPUT, camera_uri);
        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(i, CAMERA_IMAGE_PICK_REQUEST);


    }

     */



    private void pickGalleryImage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_IMAGE_PICK_REQUEST);
    }

    private void nameAndEmailDialog(final String key){
        AlertDialog.Builder builder = new AlertDialog.Builder(profileActivity.this);
        builder.setTitle("Update " + key);
        LinearLayout linearLayout = new LinearLayout(profileActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText editText = new EditText(profileActivity.this);
        editText.setHint("Enter " + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String val = editText.getText().toString().trim();
                if(TextUtils.isEmpty(val)){
                    Toast.makeText(profileActivity.this,"Please enter a value",Toast.LENGTH_SHORT).show();

                } else {
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put(key,val);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(profileActivity.this,"Update success!",Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(profileActivity.this,"No update :( ",Toast.LENGTH_SHORT).show();

                                }
                            });

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.create().show();
    }


    private void editProfileDialog() {
        //Make dialog to show all the changes we can edit
        String[] options = {"Edit profile name", "Edit profile picture", "Edit cover photo" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //Edit profile name
                    nameAndEmailDialog("name");

                }  else if (which == 1) {
                    //Edit profile picture
                    coverOrProfilePicture = "image";
                    imagePickerDialog();
                } else if (which == 2) {
                    //Edit cover photo
                    coverOrProfilePicture = "cover_image";
                    imagePickerDialog();
                }
            }
        });

        builder.create().show();
    }

    private boolean checkForStoragePermission() {
        boolean r = ContextCompat.checkSelfPermission(profileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return r;
    }

    private void getStoragePermission() {
        ActivityCompat.requestPermissions(profileActivity.this, galleryPermission, STORAGE_REQUEST);

    }


    private boolean checkForCameraPermission() {
        boolean r = ContextCompat.checkSelfPermission(profileActivity.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean r1 = ContextCompat.checkSelfPermission(profileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return r && r1;
    }

    private void getCameraPermission() {
        ActivityCompat.requestPermissions(profileActivity.this, cameraPermission, CAMERA_REQUEST);

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
                        If camera is choosen
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        /*
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean cameraPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storagePermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (storagePermissionGranted && cameraPermissionGranted) {
                        pickCameraImage();
                    } else {
                        Toast.makeText(this, "Storage and camera permissions required", Toast.LENGTH_SHORT).show();
                    }
                } else {


                }
            }
            break;

         */
        switch(requestCode){

            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storagePermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (storagePermissionGranted == true) {
                        pickGalleryImage();
                    } else {
                        Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
            break;


        }
    }

        @Override
        protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){
            //Intent x = data;
            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA_IMAGE_PICK_REQUEST /*&& data != null && data.getData() != null*/) {
                    //Image from camera is used
                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
                    Uri uri = Uri.fromFile(file);
                    Bitmap bitmap;

                    //image_uri = data.getData();
                    //image_uri = data.getExtras().

                    //camera_uri = MediaStore.Images.Media.getContentUri(android.provider.MediaStore.EXTRA_OUTPUT);
                    //camera_uri = (Uri) data.getExtras().get("data");
                    uploadPhotoToDB(uri);

                    Log.i("bartek", "the uri value is" + image_uri.toString());
                }
                    else if (requestCode == GALLERY_IMAGE_PICK_REQUEST && data != null && data.getData() != null) {
                    //Get the image from gallery my boy


                    image_uri = data.getData();
                    uploadPhotoToDB(image_uri);


                    //imageToUpload.setImageURI(image_uri);


                }
            }

            super.onActivityResult(requestCode, resultCode, data);

    }


    /*
    /
     * Here we store the file url as it will be null after returning from camera
     * app

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", image_uri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        image_uri = savedInstanceState.getParcelable("file_uri");
    }
    */


}
