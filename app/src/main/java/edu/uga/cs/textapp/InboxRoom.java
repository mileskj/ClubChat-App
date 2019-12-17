package edu.uga.cs.textapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *
 * This is the class that holds all of your contacts
 *
 */
public class InboxRoom extends AppCompatActivity {


    private DatabaseReference dRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    private List<String> userList = new ArrayList<String>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private Button chatRoomButton;
    private Button inboxRoomButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_room);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
         *
         * This floating action button allows you to add a new friend to your contacts,
         * if they are in the database as a user
         *
         */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InboxRoom.this);
                builder.setTitle("Add a Friend");

                final EditText input = new EditText(InboxRoom.this);
                input.setHint("Friend's Username");
                builder.setView(input);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String friendStr = input.getText().toString();
                        new AddFriend(friendStr).execute();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        //updates the inbox
        updateInbox();

        // Initialize the Firebase Realtime Databse variables
        dRef = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()).child("friends");

        // Database listener if they gain more friends it updates the inbox
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                updateInbox();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateInbox();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                updateInbox();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateInbox();
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


    //AsyncTask that is in charge of adding friends to the user account
    private class AddFriend extends AsyncTask<Void, Void, Void> {

        private String friend;
        private boolean isUser = false;

        public AddFriend(String friend) {
            this.friend = friend;
        }

        @Override
        protected Void doInBackground(Void... voids) {



            dRef = FirebaseDatabase.getInstance().getReference("Users");

            /**
             * Finds the user and their friends for the inbox
             */

            dRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        for(DataSnapshot snap : dataSnapshot.getChildren()){

                            try{
                                //This is long and complex and there is prob a better way to do this
                                String user = snap.getValue().toString().split("=")[3].replace("=","").replace("}",""); //will get the user name!!
                                if(user.equals(friend))
                                    isUser = true;
                            }catch (Exception e){}

                        }

                        //only adds the friend once and also if it is an actual user
                        if(isUser && !userList.contains(friend)){
                            dRef = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/friends");

                            HashMap<String, String> map = new HashMap<>();
                            int i;
                            for(i = 0; i < userList.size(); i++){
                                int num = i+1;
                                map.put(num+"", userList.get(i));
                            }//for

                            int num = i+1;
                            map.put(num+"", friend);

                            dRef.setValue(map);

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

    //updates the contacts visible by pulling from the friend list in the user and then uses a
    //recycler view and adapter to populate the contacts on to the page
    private void updateInbox(){
        new UserList().execute();

        recyclerView = (RecyclerView)findViewById(R.id.inboxList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );


        recyclerAdapter = new InboxRoomRecyclerAdapter( userList , this);
        recyclerView.setAdapter( recyclerAdapter );
    }

    // This is an AsyncTask class That retrieves all friends
    private class UserList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            dRef = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/friends");

            dRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //create new list of friends
                    userList.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snap : dataSnapshot.getChildren()){
                            String user = snap.getValue(String.class);
                            userList.add(user);
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

}
