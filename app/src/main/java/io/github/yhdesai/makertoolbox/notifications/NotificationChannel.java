package io.github.yhdesai.makertoolbox.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;

import io.github.yhdesai.makertoolbox.MT_Activity;
import io.github.yhdesai.makertoolbox.R;
import io.github.yhdesai.makertoolbox.model.DeveloperMessage;

public class NotificationChannel {

    private DatabaseReference reference;
    private int id;
    private String channel_Id;
    private ArrayList<DeveloperMessage> messages;
    private Context context;

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    public NotificationChannel(Context context, DatabaseReference reference, String channel_Id, int channel_no){
        this.context = context;
        this.channel_Id = channel_Id;
        this.reference = reference.child(channel_Id);
        id = channel_no;
        messages = new ArrayList<>();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void startListner(){
        reference.addChildEventListener(childEventListener);
        Log.e("Debug tag", "event listner added");
    }

    public void stopListner(){
        Log.e("Debug tag", "channel deleted");
        messages.clear();
        reference.removeEventListener(childEventListener);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            DeveloperMessage developerMessage = dataSnapshot.getValue(DeveloperMessage.class);
            messages.add(developerMessage);
            if(messages.size() > 0) Notify();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void Notify(){
        Intent intent = new Intent(context, MT_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) Calendar.getInstance().getTimeInMillis(), intent, 0);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(channel_Id + " channel");

        for (int i = (messages.size() > 5 ? messages.size() - 5 : 0); i < messages.size(); i++){
            DeveloperMessage message = messages.get(i);
            String line = message.getName() + ":\t" + message.getText();
            inboxStyle.addLine(line);
        }

        int count = messages.size();
        String summary = Integer.toString(count) + (count > 1 ? " new messages" : " new message");
        inboxStyle.setSummaryText(summary);
        builder = new NotificationCompat.Builder(context, channel_Id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(inboxStyle)
                .setContentIntent(pendingIntent)
                .setContentTitle(channel_Id + " channel")
                .setContentText(summary);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel notificationChannel = new android.app.NotificationChannel(channel_Id, "Maker Toolbox", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(id, builder.build());
    }
}
