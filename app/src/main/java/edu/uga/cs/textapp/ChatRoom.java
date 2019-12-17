package edu.uga.cs.textapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * This class holds the chat room and populates it with the correct messages for the topic
 *
 */

public class ChatRoom extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private List<String> chatList = new ArrayList<String>();

    private Button chatRoomButton;
    private Button inboxRoomButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Rooms for chats being added
        chatList.add("Politics");
        chatList.add("Sports");
        chatList.add("Anime");
        chatList.add("Nerd Stuff");

        /**
         * Populates the recyclerView
         */

        recyclerView = (RecyclerView)findViewById(R.id.chatList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );

        recyclerAdapter = new ChatRoomRecyclerAdapter( chatList , this);
        recyclerView.setAdapter( recyclerAdapter );

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
