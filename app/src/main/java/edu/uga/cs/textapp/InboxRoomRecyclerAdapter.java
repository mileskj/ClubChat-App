package edu.uga.cs.textapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 *
 * The adapter for the recycle view for the the contacts visible in the InboxRoom
 *
 */
public class InboxRoomRecyclerAdapter extends RecyclerView.Adapter<InboxRoomRecyclerAdapter.InboxHolder> {
    private List<String> userList;
    private Context context;

    public InboxRoomRecyclerAdapter(List<String> userList, Context context ) {
        this.userList = userList;
        this.context = context;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class InboxHolder extends RecyclerView.ViewHolder {

        TextView text;

        String s;

        public InboxHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById( R.id.text );

            //THIS IS WHERE MOVING TO THE SPECIFIC ROOM WILL COME IN
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), InboxTextActivity.class);

                    intent.putExtra("user",s);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }

    @Override
    public InboxHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        // We need to make sure that all CardViews have the same, full width, allowed by the parent view.
        // This is a bit tricky, and we must provide the parent reference (the second param of inflate)
        // and false as the third parameter (don't attach to root).
        // Consequently, the parent view's (the RecyclerView) width will be used (match_parent).
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.chat_room_card, parent, false );
        return new InboxHolder( view );
    }


    @Override
    public void onBindViewHolder( InboxHolder holder, int position ) {
        String chat = userList.get( position );
        holder.s = chat;
        holder.text.setText( chat );
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
