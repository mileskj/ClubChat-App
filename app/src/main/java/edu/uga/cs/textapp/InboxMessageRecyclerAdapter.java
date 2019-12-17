package edu.uga.cs.textapp;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 *
 * The adapter for the recycle view for Inbox messages
 *
 */
public class InboxMessageRecyclerAdapter extends RecyclerView.Adapter<InboxMessageRecyclerAdapter.MessageHolder> {
    private List<InboxMessage> messageList;
    private Context context;

    public InboxMessageRecyclerAdapter(List<InboxMessage> messageList, Context context ) {
        this.messageList = messageList;
        this.context = context;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class MessageHolder extends RecyclerView.ViewHolder {

        TextView message;
        TextView from;
        TextView time;


        public MessageHolder(View v) {
            super(v);
            message = (TextView) v.findViewById(R.id.messageInbox);
            from = (TextView) v.findViewById(R.id.userInbox);
            time = (TextView) v.findViewById(R.id.dateInbox);


        }
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        // We need to make sure that all CardViews have the same, full width, allowed by the parent view.
        // This is a bit tricky, and we must provide the parent reference (the second param of inflate)
        // and false as the third parameter (don't attach to root).
        // Consequently, the parent view's (the RecyclerView) width will be used (match_parent).
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.inbox_text, parent, false );
        return new MessageHolder( view );
    }

    // This method fills in the values of a holder to show a message
    @Override
    public void onBindViewHolder( MessageHolder holder, int position ) {
        InboxMessage wholeMessage = messageList.get( position );
        holder.message.setText(wholeMessage.getMessage());
        holder.from.setText(wholeMessage.getOut());
        holder.time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", wholeMessage.getTime()));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
