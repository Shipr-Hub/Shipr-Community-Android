package tech.shipr.socialdev.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.Profile;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;
   // private TextView emailTextView;
    private TextView nameEdits;
    private TextView ageTextView;
    private TextView langTextView;
    private TextView gitTextView;
    private TextView twitTextView;
    private TextView linkTextView;

    private String username;
    private String profilePic;
    private String age;
    private String languages;
    private String github;
    private String twitter;
    private String linkedin;
    private Profile mProfile;

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_profile);
//
//        FirebaseApp.initializeApp(this);
//
//        // Get the data from the intent
//        Intent intent = getIntent();
//        String uid = intent.getStringExtra("uid");
//        Log.d("uid received", "onCreate: " + uid);
//
//
//        nameEdits = findViewById(R.id.nameEdit);
//      //  emailTextView = findViewById(R.id.emailEdit);
//        usernameTextView = findViewById(R.id.usernameEdit);
//        ageTextView = findViewById(R.id.ageEditemailEdit);
//        langTextView = findViewById(R.id.langEdit);
//        gitTextView = findViewById(R.id.gitEdit);
//        twitTextView = findViewById(R.id.twitEdit);
//        linkTextView = findViewById(R.id.linkEdit);
//
//
//        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference mprofileDatabaseReference = mFirebaseDatabase.getReference().child("users/" + uid + "/profile");
//
//
//        //    private Boolean mProgressBarPresent;
//        //    private ProgressBar mProgressBar;
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//
//                Log.d("profile", "onDataChange: " + dataSnapshot);
//                mProfile = dataSnapshot.getValue(Profile.class);
//                if (mProfile != null) {
//
//                  //  username = mProfile.getUsername();
//                    profilePic = mProfile.getProfilePic();
//
//                    age = mProfile.getAge();
//                    languages = mProfile.getLanguages();
//                    github = mProfile.getGithub();
//                    twitter = mProfile.getTwitter();
//                    linkedin = mProfile.getLinkedin();
//
//
//                    setTextIfNotEmpty(username, usernameTextView);
//                    setTextIfNotEmpty(age, ageTextView);
//                    setTextIfNotEmpty(languages, langTextView);
//                    setTextIfNotEmpty(github, gitTextView);
//                    setTextIfNotEmpty(twitter, twitTextView);
//                    setTextIfNotEmpty(linkedin, linkTextView);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("tag", "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//        mprofileDatabaseReference.addValueEventListener(postListener);
//
//        //   mprofileDatabaseReference.addListenerForSingleValueEvent(postListener);
//
//    }
//
//    private void setTextIfNotEmpty(String ssstring, TextView seditText) {
//        if (ssstring != null && !ssstring.isEmpty()) {
//            seditText.setText(ssstring);
//        }
//    }
}
