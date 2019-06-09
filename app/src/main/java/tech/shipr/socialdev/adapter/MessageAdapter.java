package tech.shipr.socialdev.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.DeveloperMessage;
import tech.shipr.socialdev.view.ViewProfileActivity;

public abstract class MessageAdapter extends FirebaseRecyclerAdapter<DeveloperMessage, MessageAdapter.MessageViewHolder> {

    private final Context mContext;
    private FirebaseUser mUser;

    private static final int MESSAGE_TYPE_RIGHT = 1;
    private static final int MESSAGE_TYPE_LEFT = 2;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessageAdapter(Context mContext, @NonNull FirebaseRecyclerOptions<DeveloperMessage> options) {
        super(options);
        this.mContext = mContext;

        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public int getItemViewType(int position) {
        DeveloperMessage message = getItem(position);
        assert mUser != null;

        if (message.getUid() != null) {
            if (message.getUid().equals(mUser.getUid())) {
                return MESSAGE_TYPE_RIGHT;
            } else {
                return MESSAGE_TYPE_LEFT;
            }
        } else {
            return MESSAGE_TYPE_LEFT;
        }

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View messageView;

        if (viewType == MESSAGE_TYPE_RIGHT) {
            messageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_layout_send, parent, false);
        } else {
            messageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_layout_recieved, parent, false);
        }

        return new MessageViewHolder(messageView);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull DeveloperMessage msg) {
        holder.setMessage(msg);
        if(holder.profileImageView != null) {
            holder.profileImageView.setOnClickListener(new ClickHandler(msg));
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        onLoaded();
    }

    public abstract void onLoaded();

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        //     TextView authorTextView;
           TextView timeTextView;
        ImageView profileImageView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView  = itemView.findViewById(R.id.timeTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            messageTextView.setMaxWidth(itemView.getResources().getDisplayMetrics().widthPixels - 160);
        }

        public void setMessage(DeveloperMessage message) {
            messageTextView.setText(message.getText());
            //TODO uncomment this     authorTextView.setText(message.getName());
                 timeTextView.setText(message.getTime());

            //Sent Message doesnt have the profile Image View
            if (profileImageView != null) {

                //If the User hasn't set his profile pic
                if (message.getProfilePic() == null) {

//                    // Set default profile icon as message author profile pic
                   Picasso.get()
                            .load(R.drawable.profile_circle_black_36dp)
                            .into(profileImageView);
                } else {
                    Picasso.get().load(message.getProfilePic()).fit().into(profileImageView);
                }
            }else{
                Log.d("pfp", "couldnt load for msg: "+ message.getText());
            }

            String time = message.getTime();
            Log.d("time", time);
            try {
                SimpleDateFormat mTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                mTime.setTimeZone(TimeZone.getTimeZone("IST"));
                Date date = mTime.parse(time);
                Log.d("date", date.toString());
                //       dateTextView.setText(date.toString());
                mTime.setTimeZone(TimeZone.getDefault());
                String formattedTime = mTime.format(date);
                  timeTextView.setText(formattedTime);

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

        }
    }

    class ClickHandler implements View.OnClickListener {

        private final DeveloperMessage message;

        ClickHandler(DeveloperMessage message) {
            this.message = message;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.photoImageView:
                    Intent intentName = new Intent(mContext, ViewProfileActivity.class);
                    intentName.putExtra("uid", message.getUid());
                    Log.d("uid sent", "onClick: " + message.getUid());
                    mContext.startActivity(intentName);
                    break;
            }
        }
    }
}
