package edu.uga.cs.textapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 *
 * The Settings activity that displays the User's name and a button that they can log out with
 *
 */
public class Settings extends AppCompatActivity {

    Button logout;

    private Button chatRoomButton;
    private Button inboxRoomButton;
    private ImageButton settingsButton;
    private TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (TextView) findViewById(R.id.textView5);
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        user.setText(username);

        //Button that when clicked will log the user out using FirebaseUser and will change activity
        //to the Splash screen
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Settings.this, SplashActivity.class));
            }
        });

        /**
         * Adds functionality to the nav bar
         */

        chatRoomButton = (Button) findViewById(R.id.roomsButton);
        inboxRoomButton = (Button) findViewById(R.id.inboxButton);
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);

        chatRoomButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatRoom.class);
                startActivity(intent);
            }
        });

        inboxRoomButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InboxRoom.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Settings.class);
                startActivity(intent);
            }
        });
    }

}
