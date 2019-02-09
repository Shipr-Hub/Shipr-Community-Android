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
    private DeveloperMessage message;

    public MessageAdapter(Context context, int resource, List<DeveloperMessage> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = convertView.findViewById(R.id.nameTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        //     TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        ImageView profileImageView = convertView.findViewById(R.id.photoImageView);


        message = getItem(position);

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

    public class ClickHandler implements View.OnClickListener {

        private Context mContext;
        private DeveloperMessage message;

        public ClickHandler(Context mContext, DeveloperMessage message) {
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