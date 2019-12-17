package edu.uga.cs.textapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 *
 * The Splash screen that allows users to Login, Register, or if the are already logged in just go
 * straight into the app.
 *
 */
public class SplashActivity extends AppCompatActivity {

    Button login;
    Button register;

    FirebaseUser fUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //If the user is already logged in it goes straight into the app
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fUser != null)
            startActivity(new Intent(SplashActivity.this, ChatRoom.class));

        login = (Button)findViewById(R.id.lButton);
        register = (Button)findViewById(R.id.rButton);

        //Login button redirects it to login activity
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //Register button redirects it to register activity
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
