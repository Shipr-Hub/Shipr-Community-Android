package tech.shipr.socialdev;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import tech.shipr.socialdev.model.Profile;


public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView titleTextView;


    private String name;
    private String title;


    private String age;
    private String languages;
    private String github;
    private String twitter;
    private String linkedin;
    private Profile mProfile;
    private TextView nameEdits;
    private TextView ageTextView;
    private TextView langTextView;
    private TextView gitTextView;
    private TextView twitTextView;
    private TextView linkTextView;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button editBtn = view.findViewById(R.id.editBtn);




        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));


        nameTextView = view.findViewById(R.id.user_profile_name);
        titleTextView = view.findViewById(R.id.user_profile_about);
        final ImageView image = view.findViewById(R.id.user_profile_photo);
        //  emailTextView = findViewById(R.id.emailEdit);

//        ageTextView = findViewById(R.id.ageEditemailEdit);
//        langTextView = findViewById(R.id.langEdit);
//        gitTextView = findViewById(R.id.gitEdit);
//        twitTextView = findViewById(R.id.twitEdit);
//        linkTextView = findViewById(R.id.linkEdit);


        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        DatabaseReference mprofileDatabaseReference = mFirebaseDatabase.getReference().child("users/" + uid + "/profile");
Log.d("uid", uid);

        //    private Boolean mProgressBarPresent;
        //    private ProgressBar mProgressBar;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                Profile mProfile = dataSnapshot.getValue(Profile.class);
                if (mProfile != null) {
                    name = mProfile.getFullName();
                    title = mProfile.getTitle();
//                    languages = mProfile.getLanguages();
//                    github = mProfile.getGithub();
//                    twitter = mProfile.getTwitter();
//                    linkedin = mProfile.getLinkedin();


                    setTextIfNotEmpty(name, nameTextView);
                    setTextIfNotEmpty(title, titleTextView);
//                    setTextIfNotEmpty(github, gitTextView);
//                    setTextIfNotEmpty(twitter, twitTextView);
//                    setTextIfNotEmpty(linkedin, linkTextView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("tag", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        //  mprofileDatabaseReference.addValueEventListener(postListener);
        mprofileDatabaseReference.addListenerForSingleValueEvent(postListener);

        editBtn.setOnClickListener(view1 -> {
            Intent i = new Intent(ProfileFragment.this.getContext(), EditProfile.class);
            Pair[] pair = new Pair[1];
            pair[0] = new Pair<View, String>(image, "userImage");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProfileFragment.this.getActivity(),
                    pair);
            Objects.requireNonNull(ProfileFragment.this.getActivity()).startActivity(i, options.toBundle());
        });

        return view;
    }

    private void setTextIfNotEmpty(String ssstring, TextView seditText) {
        if (ssstring != null && !ssstring.isEmpty()) {
            seditText.setText(ssstring);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
