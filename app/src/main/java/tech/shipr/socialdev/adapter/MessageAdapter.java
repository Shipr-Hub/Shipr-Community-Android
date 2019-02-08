package tech.shipr.socialdev.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.DeveloperMessage;
import tech.shipr.socialdev.view.MTActivity;
import tech.shipr.socialdev.view.ProfileActivity;


public class MessageAdapter extends ArrayAdapter<DeveloperMessage> implements View.OnClickListener {

    Context mContext;

    public MessageAdapter(Context context, int resource, List<DeveloperMessage> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = convertView.findViewById(R.id.nameTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        //     TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        ImageView profileImageView = convertView.findViewById(R.id.photoImageView);

        DeveloperMessage message = getItem(position);

        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getText());
        authorTextView.setText(message.getName());

        // Click listeners
        profileImageView.setOnClickListener(MessageAdapter.this);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoImageView:
                FragmentManager frag2 = ((FragmentActivity)mContext).getSupportFragmentManager();
                frag2.beginTransaction().replace(R.id.content_frame, new ProfileActivity()).commit();
                ((MTActivity)mContext).navigation.setSelectedItemId(R.id.navigation_profile);

                break;
        }
    }
}