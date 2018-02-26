package io.github.yhdesai.devprops;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by gopinath on 26/02/18.
 */

public class Intializing extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
