package tech.shipr.socialdev.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.Profile;

public class EditProfileActivity extends Fragment {


    private TextView usernameEdit;
    private TextView emailEdit;
    private EditText nameEdits;
    private EditText ageEditemailEdit;
    private EditText langEdit;
    private EditText gitEdit;
    private EditText twitEdit;
    private EditText linkEdit;
    private DatabaseReference mprofileDatabaseReference;
    private String fullName;
    private String username;
    private String email;
    private String age;
    private String languages;
    private String github;
    private String twitter;
    private String linkedin;
    private Profile mProfile;

    private static final int RC_PROFILE_PHOTO_PICKER = 4;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        nameEdits = rootView.findViewById(R.id.nameEdit);

        emailEdit = rootView.findViewById(R.id.emailEdit);
        usernameEdit = rootView.findViewById(R.id.usernameEdit);
        ageEditemailEdit = rootView.findViewById(R.id.ageEditemailEdit);
        langEdit = rootView.findViewById(R.id.langEdit);
        gitEdit = rootView.findViewById(R.id.gitEdit);
        twitEdit = rootView.findViewById(R.id.twitEdit);
        linkEdit = rootView.findViewById(R.id.linkEdit);
        ImageView profileImageView = rootView.findViewById(R.id.profileImage);

        //    mProgressBar = rootView.findViewById(R.id.pProgressBar);

        FirebaseApp.initializeApp(getActivity());

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String id = user.getUid();
        mprofileDatabaseReference = mFirebaseDatabase.getReference().child("users" + "/" + id + "/" + "profile");
        //mProgressBarPresent = true;


        {
            // Name, email address, and profile photo Url

            String username = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUri = user.getPhotoUrl();


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            //TODO Add a listener and if false, add verift email button


            emailEdit.setText(email);
            usernameEdit.setText(username);
            if (photoUri != null && !photoUri.equals(Uri.EMPTY)) {
                Picasso.get().load(photoUri).fit().into(profileImageView);
            }


        }


        //    private Boolean mProgressBarPresent;
        //    private ProgressBar mProgressBar;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
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
                    setTextIfNotEmpty(email, emailEdit);
                    setTextIfNotEmpty(username, usernameEdit);
                    setEditIfNotEmpty(age, ageEditemailEdit);
                    setEditIfNotEmpty(languages, langEdit);
                    setEditIfNotEmpty(github, gitEdit);
                    setEditIfNotEmpty(twitter, twitEdit);
                    setEditIfNotEmpty(linkedin, linkEdit);
                }

                // mProgressBarCheck();

            }

            private void setEditIfNotEmpty(String sstring, EditText editText) {
                if (sstring != null && !sstring.isEmpty()) {
                    editText.setText(sstring);
                }
            }

            private void setTextIfNotEmpty(String ssstring, TextView seditText) {
                if (ssstring != null && !ssstring.isEmpty()) {
                    seditText.setText(ssstring);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ProfileActivity", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mprofileDatabaseReference.addListenerForSingleValueEvent(postListener);

        FloatingActionButton button = rootView.findViewById(R.id.submitButton);
        button.setOnClickListener(v -> {
            getVariablesFromEditText();
            mProfile = new Profile(
                    fullName,
                    username,
                    //email,
                    null,
                    age,
                    languages,
                    github,
                    twitter,
                    linkedin
            );
            mprofileDatabaseReference.setValue(mProfile);
        });

        return rootView;
    }

    private void getVariablesFromEditText() {
        fullName = nameEdits.getText().toString();
        email = emailEdit.getText().toString();
        username = usernameEdit.getText().toString();
        age = ageEditemailEdit.getText().toString();
        languages = langEdit.getText().toString();
        github = gitEdit.getText().toString();
        twitter = twitEdit.getText().toString();
        linkedin = linkEdit.getText().toString();


    }

    private void clearPic() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(null)
                .build();

    }

    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PROFILE_PHOTO_PICKER);

    }


 /*   private void mProgressBarCheck(){
        if(mProgressBarPresent){
            mProgressBar.setVisibility(View.GONE);
            mProgressBarPresent=false;

        }
    }*/

}
