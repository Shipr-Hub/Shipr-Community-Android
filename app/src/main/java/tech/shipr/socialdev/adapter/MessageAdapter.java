package tech.shipr.socialdev.adapter;

import android.app.Activity;
import android.content.Context;
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

public class MessageAdapter extends ArrayAdapter<DeveloperMessage> {
    public MessageAdapter(Context context, int resource, List<DeveloperMessage> objects) {
        super(context, resource, objects);
    }

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
        String pic = message.getProfilePic();
        Log.d("profile pic", pic + ".");

        Picasso.get().load(pic).into(profileImageView);

        messageTextView.setText(message.getText() + "  " + pic);


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

}