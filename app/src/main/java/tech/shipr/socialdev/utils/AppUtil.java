package tech.shipr.socialdev.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tech.shipr.socialdev.BuildConfig;
import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.App;

public class AppUtil {
    private Activity mActivity;
    private Dialog mDialog;

    private AppUtil(Activity activity) {
        mActivity = activity;
    }

    public static AppUtil init(Activity activity) {
        return (new AppUtil(activity));
    }

    public void checkForUpdate() {
        System.out.println("Checking for updates...");

        final int currentVersion = BuildConfig.VERSION_CODE;
        DatabaseReference appRef = FirebaseDatabase.getInstance().getReference().child("app");

        appRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    App app = dataSnapshot.getValue(App.class);

                    if(app != null) {
                        if (currentVersion == app.versionCode) {
                            Log.w(getClass().getName(),"App is up to date");
                        } else {
                            Log.w(getClass().getName(),"App is not up to date");
                            showUpdateAlert(app.message, app.updateUrl);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void showUpdateAlert(String message, final String url){
        if(mDialog == null) {
            mDialog = new Dialog(mActivity);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_update_layout);

        TextView updateMessageView = mDialog.findViewById(R.id.update_message);
        updateMessageView.setText(message);
        mDialog.findViewById(R.id.install_button).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            mActivity.startActivity(intent);
            mActivity.finish();
        });
        mDialog.findViewById(R.id.skip_button).setOnClickListener(v -> mDialog.dismiss());

        mDialog.show();
    }
}
