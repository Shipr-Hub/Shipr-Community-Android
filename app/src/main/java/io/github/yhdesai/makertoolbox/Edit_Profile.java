package io.github.yhdesai.makertoolbox;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;

import static io.github.yhdesai.makertoolbox.MainActivity.ANONYMOUS;
import static io.github.yhdesai.makertoolbox.MainActivity.RC_SIGN_IN;

public class Edit_Profile extends AppCompatActivity {
private com.google.firebase.auth.FirebaseAuth mFirebaseAuth;
    private com.google.firebase.auth.FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is signed in
                    onSignedInInitialize(user.getDisplayName());

                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    //   new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build(),
                                                    //   new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                    //    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                            ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }

            private void onSignedInInitialize(String username) {
                String mUsername = username;

            }

            private void onSignedOutCleanup() {
                String mUsername = ANONYMOUS;


            }
        };
        setValueOnView();

    }


    public void setValueOnView() {
        EditText nameEditText = findViewById(R.id.pNameTextView);
            TextView emailTextView = findViewById(R.id.pEmailTextView);


       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
     if (user != null) {

    String name = user.getDisplayName();
    String email = user.getEmail();
    android.net.Uri photoUrl = user.getPhotoUrl();
         nameEditText.setText(user.getDisplayName());
         emailTextView.setText(user.getEmail());


    // Check if user's email is verified
         // boolean emailVerified = user.isEmailVerified();

    // The user's ID, unique to the Firebase project. Do NOT use this value to
    // authenticate with your backend server, if you have one. Use
    // FirebaseUser.getIdToken() instead.
         //  String uid = user.getUid();

     }
    }

    public void submit(View view) {
        EditText nameEditText = findViewById(R.id.pNameTextView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(MainActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            FriendlyMessage friendlyMessage =
                                    new FriendlyMessage(null, mUsername, mPhotoUrl,
                                            task.getResult().getDownloadUrl()
                                                    .toString());
                            mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key)
                                    .setValue(friendlyMessage);
                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }


}


