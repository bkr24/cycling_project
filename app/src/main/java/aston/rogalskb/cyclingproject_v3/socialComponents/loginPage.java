package aston.rogalskb.cyclingproject_v3.socialComponents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import aston.rogalskb.cyclingproject_v3.R;

public class loginPage extends AppCompatActivity {

    Button registerBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //setContentView(R.layout.activity_login_page);

        registerBtn = findViewById(R.id.registerButton);
        loginBtn = findViewById(R.id.loginButton);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the register actvity :)
                startActivity(new Intent(loginPage.this,registration.class));


            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginPage.this,login.class));

            }
        });








    }
}
