package tech.shipr.socialdev.notification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.DeveloperMessage;
import tech.shipr.socialdev.view.MainActivity;


public class NotificationChannel {

    private DatabaseReference reference;
    private int id;
    private String channel_Id;
    private ArrayList<DeveloperMessage> messages;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String channel_Key;
    private int count;

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            int local_count = 0;
            for (DataSnapshot message : dataSnapshot.getChildren()) {
                DeveloperMessage developerMessage = message.getValue(DeveloperMessage.class);
                local_count++;
                if (local_count > count) {
                    count = local_count;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("count", count);
                    editor.apply();
                    messages.add(developerMessage);
                }
            }
            if (messages.size() > 0) Notify();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public NotificationChannel(Context context, DatabaseReference reference, String channel_Id, int channel_no) {
        this.context = context;
        this.channel_Id = channel_Id;
        this.reference = reference.child(channel_Id);
        channel_Key = context.getString(R.string.shared_preference_key) + channel_Id;
        sharedPreferences = context.getSharedPreferences(channel_Key, Context.MODE_PRIVATE);
        count = sharedPreferences.getInt("count", 0);
        id = channel_no;
        messages = new ArrayList<>();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public String getChannel_Id() {
        return channel_Id;
    }

    public void startListner() {
        reference.addValueEventListener(eventListener);
    }

    public void stopListner() {
        messages.clear();
        reference.removeEventListener(eventListener);
    }

    private void Notify() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) Calendar.getInstance().getTimeInMillis(), intent, 0);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(channel_Id + " channel");

        for (int i = (messages.size() > 5 ? messages.size() - 5 : 0); i < messages.size(); i++) {
            DeveloperMessage message = messages.get(i);
            String line = message.getName() + ":\t" + message.getText();
            inboxStyle.addLine(line);
        }

        int count = messages.size();
        String summary = Integer.toString(count) + (count > 1 ? " new messages" : " new message");
        inboxStyle.setSummaryText(summary);
        builder = new NotificationCompat.Builder(context, channel_Id)
                .setSmallIcon(R.mipmap.ic_launcher) //Todo: change the small icon with an xml icon
                .setStyle(inboxStyle)
                .setContentIntent(pendingIntent)
                .setContentTitle(channel_Id + " channel")
                .setContentText(summary);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel notificationChannel = new android.app.NotificationChannel(channel_Id, "Maker Toolbox", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(id, builder.build());
    }
}
