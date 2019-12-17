package edu.uga.cs.textapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * A class that contains the actual text messaging part between two users
 *
 */
public class InboxTextActivity extends AppCompatActivity {

    public TextView textView;
    public Button sendButton;
    public EditText input;

    private DatabaseReference dRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String userName = user.getDisplayName();

    private String friendName;


    private List<InboxMessage> messageList = new ArrayList<InboxMessage>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private Button chatRoomButton;
    private Button inboxRoomButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_text);
        textView = (TextView) findViewById(R.id.inboxTitle);
        sendButton = (Button) findViewById(R.id.sendInbox);

        //Get fiend's username from the intent
        Intent intent = getIntent();
        friendName = intent.getStringExtra("user");

        textView.setText(friendName);
        input = (EditText)findViewById(R.id.inboxInput);

        //Send button executes ASync task
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddMessage(new InboxMessage(input.getText().toString(), userName, friendName)).execute();
                input.setText("");
            }
        });

        //update messages
        updateMessages();

        // Initialize the Firebase Realtime Databse variable
        dRef = FirebaseDatabase.getInstance().getReference("UserInboxes").child(userName);

        // Database listener to update messages when a new message is in users inbox
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                updateMessages();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateMessages();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                updateMessages();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
    }//onCreate

    //updates the texts visible by pulling from the user's inbox and then uses a
    //recycler view and adapter to populate the contacts on to the page
    private void updateMessages(){
        new Messages().execute();

        recyclerView = (RecyclerView)findViewById(R.id.inboxMessages);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );


        recyclerAdapter = new InboxMessageRecyclerAdapter( messageList , this);
        recyclerView.setAdapter( recyclerAdapter );
    }//updateMessages


    // This is an AsyncTask class that retrieves all messages in inbox between specified user and friend
    private class Messages extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            dRef = FirebaseDatabase.getInstance().getReference("UserInboxes/"+userName);

            dRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    messageList.clear();
                    if(dataSnapshot.exists()){
                        for (DataSnapshot snap : dataSnapshot.getChildren()){

                                InboxMessage message = snap.getValue(InboxMessage.class);

                                //I was having some problems, so I just did a bunch of checks before I added to the list to create a new view
                                if (message != null && message.getOut() != null && !messageList.contains(message) && (message.getOut().equals(friendName) || message.getIn().equals(friendName)))
                                    messageList.add(message);


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;
        }
    }

    // This is an AsyncTask class That retrieves adds a message to both the user and friends inbox
    private class AddMessage extends AsyncTask<Void, Void, Void> {

        private InboxMessage message;

        public AddMessage(InboxMessage message){
            this.message = message;
        }

        // This method will run as a background process to read from db.
        // It will add the message to the Friend and User userInbox
        @Override
        protected Void doInBackground(Void... params) {

            dRef = FirebaseDatabase.getInstance().getReference("UserInboxes/"+userName);
            dRef.push().setValue(message);


            dRef = FirebaseDatabase.getInstance().getReference("UserInboxes/"+friendName);
            dRef.push().setValue(message);

            return null;
        }
    }

}





