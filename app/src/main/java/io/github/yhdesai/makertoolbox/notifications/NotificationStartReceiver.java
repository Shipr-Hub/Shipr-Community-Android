package io.github.yhdesai.makertoolbox.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class NotificationStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent service = new Intent(context, NotificationService.class);
        if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(service);
            else context.startService(service);
        }
        else if(NotificationService.service_broadcast.equals(action) || Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)) context.startService(service);
    }
}
