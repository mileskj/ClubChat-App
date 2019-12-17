package edu.uga.cs.textapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/*
 *
 * This class holds the layout where users can register into ClubChat
 *
 */

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText email;

    Button register;

    private FirebaseAuth mAuth;
    private DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);

        register = (Button)findViewById(R.id.register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Sends all the information to an AsyncTask that inputs the data as a user if the information is all correct
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                new Register(username.getText().toString(), password.getText().toString(), email.getText().toString()).execute();
            }
        });
    }

    //The AsyncTask that registers a new user with FirebaseUser
    private class Register extends AsyncTask<Void, Void, Void> {

        private String userN;
        private String pass;
        private String email;

        public Register(String user, String pass, String email){
            this.userN = user;
            this.pass = pass;
            this.email = email;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                //Saves the user information into Firebase for later use
                                final String uID = user.getUid();
                                dRef = FirebaseDatabase.getInstance().getReference("Users").child(uID);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", uID);
                                map.put("username", userN);
                                dRef.setValue(map);

                                //sets display name for the user for later
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userN)
                                        .build();
                                user.updateProfile(profileUpdates);


                                //Creates a user Inbox for the newly created user
                                dRef = FirebaseDatabase.getInstance().getReference("UserInboxes").child(userN);
                                InboxMessage mess = new InboxMessage("Hello, and welcome!", "Club Chat Bot", userN);
                                dRef.push().setValue(mess);

                                //Adds the message to Club Chat Bot user inbox
                                dRef = FirebaseDatabase.getInstance().getReference("Users/"+uID).child("friends");
                                HashMap<String, String> map3 = new HashMap<>();
                                map3.put("1", "Club Chat Bot");

                                //sends to splash activity
                                dRef.setValue(map3).addOnCompleteListener(new OnCompleteListener<Void>(){
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(RegisterActivity.this, SplashActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

            return null;
        }
    }
}
