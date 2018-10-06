package io.github.yhdesai.makertoolbox;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        //Get updated token

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", token);

        //You can save the token into third party server to do anything you want
    }
}
