package tech.shipr.socialdev;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import tech.shipr.socialdev.model.Profile;

public class EditProfile extends AppCompatActivity {

    private EditText nameEditText;
    private EditText usernameEdit;
    private EditText titleEditText;
    private EditText progSkillsEditText;

    private String name;
    private String username;
    private String title;
    private String progSkill;

    private TextView emailEdit;
    private EditText ageEditemailEdit;
    private EditText langEdit;
    private EditText gitEdit;
    private EditText twitEdit;
    private EditText linkEdit;
    private DatabaseReference mprofileDatabaseReference;

    private String fullName;
    private String email;
    private String age;
    private String languages;
    private String github;
    private String twitter;
    private String linkedin;
    private Profile mProfile;
    private static final int RC_PROFILE_PHOTO_PICKER = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //SETTING TOOLBAR  FOR WALLFRAGMENR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Edit Profile");
        //set toolbar appearance
        //for crate home button
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEditText = findViewById(R.id.nameEditText);
        usernameEdit = findViewById(R.id.usernameEditText);
        titleEditText = findViewById(R.id.titleEditText);
        progSkillsEditText = findViewById(R.id.proskillEditText);

        emailEdit = findViewById(R.id.emailEditText);
        langEdit = findViewById(R.id.langEdit);
        gitEdit = findViewById(R.id.gitEdit);
        twitEdit = findViewById(R.id.twitEdit);
        linkEdit = findViewById(R.id.linkEdit);
        ImageView profileImageView = findViewById(R.id.img_profile_userimage);

        //    mProgressBar = findViewById(R.id.pProgressBar);

        FirebaseApp.initializeApp(this);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String id = user.getUid();
        mprofileDatabaseReference = mFirebaseDatabase.getReference().child("users" + "/" + id + "/" + "profile");
        //mProgressBarPresent = true;

        Uri photoUri = user.getPhotoUrl();
        // Check if user's email is verified
        boolean emailVerified = user.isEmailVerified();
        //TODO Add a listener and if false, add verift email button



        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                mProfile = dataSnapshot.getValue(Profile.class);
                if (mProfile != null) {
                    name = mProfile.getFullName();
                    username = mProfile.getUsername();
                    title = mProfile.getTitle();




                    setEditIfNotEmpty(name, nameEditText);
                    setEditIfNotEmpty(username, usernameEdit);
                    setEditIfNotEmpty(title, titleEditText);
                    setEditIfNotEmpty(progSkill, progSkillsEditText);


                    if (photoUri != null && !photoUri.equals(Uri.EMPTY)) {
                        Picasso.get().load(photoUri).fit().into(profileImageView);
                    }

                }

                // mProgressBarCheck();

            }



            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ProfileActivity", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mprofileDatabaseReference.addListenerForSingleValueEvent(postListener);


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
    private void getVariablesFromEditText() {
        fullName = nameEditText.getText().toString();
        username = usernameEdit.getText().toString();
        title=titleEditText.getText().toString();
        progSkill = progSkillsEditText.getText().toString();
//        email = emailEdit.getText().toString();



//        age = ageEditemailEdit.getText().toString();
//        languages = langEdit.getText().toString();
//        github = gitEdit.getText().toString();
//        twitter = twitEdit.getText().toString();
//        linkedin = linkEdit.getText().toString();


    }





    public void submit(View v) {
        getVariablesFromEditText();
        mProfile = new Profile(
                fullName,
                username,
                title,
                //email,
                null,
                age,
                languages,
                github,
                twitter,
                linkedin
        );
        mprofileDatabaseReference.setValue(mProfile);
        Toast.makeText(this, "Updated Profile", Toast.LENGTH_SHORT).show();
        finish();
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
