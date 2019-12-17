package edu.uga.cs.textapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 *
 * Class hosting the layout which shows the chat room and list view of each chat room
 *
 */

public class ChatRoomText extends AppCompatActivity {

    public TextView textView;
    public TextInputEditText textEdit;
    public Button sendButton;
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listOfMessages;
    private String chatRoom;

    private Button chatRoomButton;
    private Button inboxRoomButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_text);

        /**
         * Initializes the widgets in the room
         */

        textView = (TextView) findViewById(R.id.textView6);
        sendButton = (Button) findViewById(R.id.sendButton);
        Intent intent = getIntent();
        chatRoom = intent.getStringExtra("roomTitle");
        textView.setText(intent.getStringExtra("roomTitle"));

        /**
         * Adds functionality to the sendButton, sending it into the database
         */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText input = (TextInputEditText) findViewById(R.id.chatRoomInput);
                String ref = "/ChatRooms/Messages/";

                FirebaseDatabase.getInstance()
                        .getReference(ref+chatRoom)
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                        );
                input.setText("");
            }
        });


        /**
         * Populates the list view with the messages saved in the database for each chat room
         */

        String ref = "/ChatRooms/Messages/";

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setLayout(R.layout.chat_text)
                .setQuery(FirebaseDatabase.getInstance().getReference(ref+chatRoom), ChatMessage.class)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
                TextView messageText = (TextView) v.findViewById(R.id.chatText);
                TextView messageUser = (TextView) v.findViewById(R.id.chatTextName);
                TextView messageTime = (TextView) v.findViewById(R.id.chatTextDate);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };

        displayChatMessages();

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

    private void displayChatMessages() {
        listOfMessages = (ListView) findViewById(R.id.chatRoom);
        listOfMessages.setAdapter(adapter);

    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}





