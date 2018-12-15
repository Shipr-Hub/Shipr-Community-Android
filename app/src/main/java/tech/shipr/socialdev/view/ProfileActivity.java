package tech.shipr.socialdev.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tech.shipr.socialdev.model.Profile;

import tech.shipr.socialdev.R;

public class ProfileActivity extends Fragment {


    private TextView pUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mprofileDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    private String fullName;
    private String username;
    private String email;
    private String age;
    private String languages;
    private String github;
    private String twitter;
    private String linkedin;
    private Profile mProfile;
    EditText usernameEdit;
    EditText emailEdit;
    EditText nameEdits;
    EditText ageEditemailEdit;
    EditText langEdit;
    EditText gitEdit;
    EditText twitEdit;
    EditText linkEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        nameEdits = rootView.findViewById(R.id.nameEdit);

        emailEdit = rootView.findViewById(R.id.emailEdit);
        usernameEdit = rootView.findViewById(R.id.usernameEdit);
        ageEditemailEdit = rootView.findViewById(R.id.ageEditemailEdit);
        langEdit = rootView.findViewById(R.id.langEdit);
        gitEdit = rootView.findViewById(R.id.gitEdit);
        twitEdit = rootView.findViewById(R.id.twitEdit);
        linkEdit = rootView.findViewById(R.id.linkEdit);

        FirebaseApp.initializeApp(getActivity());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String id = user.getUid();
        mprofileDatabaseReference = mFirebaseDatabase.getReference().child("users" + "/" + id + "/" + "profile");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                mProfile = dataSnapshot.getValue(Profile.class);
                if (mProfile != null) {
                    fullName = mProfile.getFullName();
                    email = mProfile.getEmail();
                    username = mProfile.getUsername();
                    age = mProfile.getAge();
                    languages = mProfile.getLanguages();
                    github = mProfile.getGithub();
                    twitter = mProfile.getTwitter();
                    linkedin = mProfile.getLinkedin();

                    setEditIfNotEmpty(fullName, nameEdits);
                    setEditIfNotEmpty(email, emailEdit);
                    setEditIfNotEmpty(username, usernameEdit);
                    setEditIfNotEmpty(age, ageEditemailEdit);
                    setEditIfNotEmpty(languages, langEdit);
                    setEditIfNotEmpty(github, gitEdit);
                    setEditIfNotEmpty(twitter, twitEdit);
                    setEditIfNotEmpty(linkedin, linkEdit);
                }

            }

            private void setEditIfNotEmpty(String sstring, EditText editText) {
                if (sstring != null && !sstring.isEmpty()) {
                    editText.setText(sstring);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ProfileActivity", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mprofileDatabaseReference.addListenerForSingleValueEvent(postListener);





 /*       //TODO profile pic
        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                Log.d("ping", "Ping");
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                Log.d("pong", "Pong");
            }
        });*/


        Button button = (Button) rootView.findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVariablesFromEditText();
                mProfile = new Profile(
                        fullName,
                        username,
                        email,
                        age,
                        languages,
                        github,
                        twitter,
                        linkedin
                );
                mprofileDatabaseReference.setValue(mProfile);
            }
        });

        return rootView;
    }

    private void getVariablesFromEditText() {
        fullName = nameEdits.getText().toString();
        email = emailEdit.getText().toString();
username = usernameEdit.getText().toString();
age=ageEditemailEdit.getText().toString();
languages= langEdit.getText().toString();

    github=gitEdit.getText().toString();
    twitter=twitEdit.getText().toString();
    linkedin=linkEdit.getText().toString();


    }


}
