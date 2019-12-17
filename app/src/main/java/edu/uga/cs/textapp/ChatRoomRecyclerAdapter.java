package edu.uga.cs.textapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 *
 * The adapter for the recycle view for the Chat room topics
 *
 */
public class ChatRoomRecyclerAdapter extends RecyclerView.Adapter<ChatRoomRecyclerAdapter.ChatHolder> {
    private List<String> chatList;
    private Context context;

    public ChatRoomRecyclerAdapter(List<String> chatList, Context context ) {
        this.chatList = chatList;
        this.context = context;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class ChatHolder extends RecyclerView.ViewHolder {

        TextView text;

        String s;

        public ChatHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById( R.id.text );

            /**
             * here is where each recycler view cell can be clicked to take you to the chat room
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ChatRoomText.class);
                    intent.putExtra("roomTitle",s);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        // We need to make sure that all CardViews have the same, full width, allowed by the parent view.
        // This is a bit tricky, and we must provide the parent reference (the second param of inflate)
        // and false as the third parameter (don't attach to root).
        // Consequently, the parent view's (the RecyclerView) width will be used (match_parent).
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.chat_room_card, parent, false );
        return new ChatHolder( view );
    }

    // This method fills in the values of a holder to show a Chat Room.
    // The position parameter indicates the position on the list of rooms that are available.
    @Override
    public void onBindViewHolder( ChatHolder holder, int position ) {
        String chat = chatList.get( position );
        holder.s = chat;
        holder.text.setText( chat );
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
