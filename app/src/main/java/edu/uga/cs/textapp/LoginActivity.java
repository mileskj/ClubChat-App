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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 *
 * The login activity that utilizes FireBaseAuth to check email and pass
 *
 */
public class LoginActivity extends AppCompatActivity {

    EditText password;
    EditText email;

    Button login;

    private FirebaseAuth mAuth;
    private DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password = (EditText)findViewById(R.id.passwordL);
        email = (EditText)findViewById(R.id.emailL);

        login = (Button)findViewById(R.id.Login);

        // Initialize Firebase Auth and sends it to AsyncTask to check login
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                new Login(email.getText().toString(), password.getText().toString()).execute();
            }
        });
    }


    /**
     * Asynch task that logs in the user
     */
    private class Login extends AsyncTask<Void, Void, Void> {

        private String pass;
        private String email;

        public Login(String email, String pass) {
            this.pass = pass;
            this.email = email;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, ChatRoom.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

            return null;
        }
    }
}
