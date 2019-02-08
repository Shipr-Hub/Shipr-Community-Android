package tech.shipr.socialdev.utils;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    public void initFirebaseApp(Context mContext) {
        FirebaseApp.initializeApp(mContext);
    }

    public void initFirebaseAuth(FirebaseAuth mFirebaseAuth) {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void initFirebaseDatabase(FirebaseDatabase mFirebaseDatabase, DatabaseReference mDatabaseReference, String loc) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(loc);
    }

    public void dbSetpersistent(FirebaseDatabase db) {
        db.setPersistenceEnabled(true);
    }

    public void authStateCheck() {

    }
}
