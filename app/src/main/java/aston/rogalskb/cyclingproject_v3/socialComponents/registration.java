package aston.rogalskb.cyclingproject_v3.socialComponents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import aston.rogalskb.cyclingproject_v3.R;

public class registration extends AppCompatActivity {

    EditText passwordField,emailField;
    Button registrationButton;
    TextView goToLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Register new account");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        registrationButton = findViewById(R.id.registerNewUserButton);
        passwordField = findViewById(R.id.passwordEditText);
        emailField = findViewById(R.id.emailEditText);
        goToLogin = findViewById(R.id.regNoAccount);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailField.setError("Email invalid sorry");
                    emailField.setFocusable(true);
                } else if (password.length() < 7){
                    passwordField.setError("Password length less than 7 characters");
                    passwordField.setFocusable(true);
                } else {
                    registerNewUser(email,password);
                }

            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this,login.class);
                startActivity(intent);
            }
        });


    }

    private void registerNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email  = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name",""); //Implement during edit profile :)
                            //Part of the chat activity
                            hashMap.put("onlineStatus","online");
                            hashMap.put("image","");
                            hashMap.put("cover_image","");

                            //hashMap.put("email",email);

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            //Data storge path
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                            databaseReference.child(uid).setValue(hashMap);


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(registration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Error has occured, what to do ?
                Toast.makeText(registration.this,""+ e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
