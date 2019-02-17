package tech.shipr.socialdev.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.DeveloperMessage;
import tech.shipr.socialdev.view.ViewProfileActivity;


public class MessageAdapter extends ArrayAdapter<DeveloperMessage> {

    private final Context mContext;

    public MessageAdapter(Context context, int resource, List<DeveloperMessage> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {

        initFirebase();

        DeveloperMessage message = getItem(position);
        assert message != null;


        //TODO: refer issue #94
       /* if (convertView == null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            assert user != null;
            String uid = user.getUid();
            if (message.getUid().equals(uid)) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message_right, parent, false);
            } else {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);

            }
        }*/

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }


        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = convertView.findViewById(R.id.nameTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        //     TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        ImageView profileImageView = convertView.findViewById(R.id.photoImageView);


        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getText());
        authorTextView.setText(message.getName());

        // Click listeners
        profileImageView.setOnClickListener(new ClickHandler(mContext, message));


        String pic = message.getProfilePic();

        if (pic == null) {

            Picasso.get()
                    .load(R.drawable.ic_account_circle_black_36dp)
                    .into(profileImageView);
        } else {
            Picasso.get().load(pic).fit().into(profileImageView);
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
        return convertView;
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(getContext());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    }

    class ClickHandler implements View.OnClickListener {

        private final Context mContext;
        private final DeveloperMessage message;

        ClickHandler(Context mContext, DeveloperMessage message) {
            this.mContext = mContext;
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