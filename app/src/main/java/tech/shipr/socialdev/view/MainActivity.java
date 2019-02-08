package tech.shipr.socialdev.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.DeveloperMessage;


public class MainActivity extends FragmentActivity {

    private static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private static final int RC_CHAT_PHOTO_PICKER = 3;

    private StorageReference mProfileStroageReference;

    private String mName;
    private String mPlatform;
    private String mChannel = "general";
    private String mDate;
    private String mTime;
    private String mMessage;
    private String mProfilePic;
    private String mVersion = "1";
    private UploadTask uploadTask;
    private Uri downloadUri;
    private String uid;
    private DatabaseReference mMessagesDatabaseReference;
    private static DatabaseReference rootRef;
    // Firebase instance variable

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_chat:
                FragmentManager frag = getSupportFragmentManager();
                frag.beginTransaction().replace(R.id.content_frame, new ChatChannel()).commit();

                return true;

            case R.id.navigation_profile:
                FragmentManager frag2 = getSupportFragmentManager();
                frag2.beginTransaction().replace(R.id.content_frame, new EditProfileActivity()).commit();

                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt);
        
        initFirebase();
        
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager frag1 = getSupportFragmentManager();
        frag1.beginTransaction().replace(R.id.content_frame, new ChatChannel()).commit();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);

        if (rootRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            rootRef = database.getReference();
        }

        mMessagesDatabaseReference = rootRef.child("chat/general");

        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        mProfileStroageReference = mFirebaseStorage.getReference().child("profile_pic");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MT_Activity", "onActivityResult executed");
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d("Error Code", String.valueOf(Objects.requireNonNull(response).getError().getErrorCode()));
                Log.d("Error Message", response.getError().getMessage());
            }
        } else if (requestCode == 4 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            String id = user.getUid();

            StorageReference photoRef = mProfileStroageReference.child(id);
            uploadImageToStorage(photoRef, selectedImageUri);

            // Upload file to Firebase Storage

        } else
            //TODO fix this
            if (requestCode == RC_CHAT_PHOTO_PICKER && resultCode == RESULT_OK) {

                Uri selectedImageUri = data.getData();

                // Get a reference to store file at chat_photos/<FILENAME>
                final StorageReference photoRef = mProfileStroageReference.child(selectedImageUri.getLastPathSegment());


                photoRef.putFile(selectedImageUri).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return photoRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        //TODO return the url of the image uploaded here


                        // Getting the time
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
                        mTime = sdf.format(new Date());

                        // Getting the date
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        mDate = String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year);


                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            for (UserInfo profile : user.getProviderData()) {
                                // Id of the provider (ex: google.com)
                                String providerId = profile.getProviderId();

                                // UID specific to the provider

                                uid = profile.getUid();

                                // Name, email address, and profile photo Url
                                mName = profile.getDisplayName();
                                Uri uri = profile.getPhotoUrl();
                                if (uri != null) {
                                    mProfilePic = uri.toString();
                                }
                            }

                        }

                        DeveloperMessage developerMessage = new DeveloperMessage(
                                mName,

                                mProfilePic,
                                mMessage,
                                downloadUri.toString(),
                                mTime,
                                mDate,
                                mPlatform,
                                mVersion,
                                uid);
                        mMessagesDatabaseReference.push().setValue(developerMessage);
                    } else {
                        Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }

    public void profileImageEdit(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 4);

    }

    private void updateProfilePic(Uri profilePicString) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(profilePicString)
                .build();

        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("profile Pic", "User profile updated.");
                        Toast.makeText(MainActivity.this, "Profile pic set", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    /*  private void uploadImageToStorage(StorageReference photoRef, Uri selectedImageUri) {
          Log.d("debug uploadimage", "started");
          Log.d("debug had got ref", photoRef.toString());
          Log.d("debug had got uri", selectedImageUri.toString());
          String test;
          photoRef.putFile(selectedImageUri)
                  .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          // When the image has successfully uploaded, we get its download URL

                          updateProfilePic(taskSnapshot.      );
                          Toast.makeText(MTActivity.this, "Your Profile Image has been updated", Toast.LENGTH_SHORT).show();
                      }
                  });

      }*/
    public void signout(View view) {

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    Toast.makeText(MainActivity.this, "You have been signed out", Toast.LENGTH_SHORT).show();

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Collections.singletonList(
                                                    new AuthUI.IdpConfig.EmailBuilder().build()
                                            ))
                                    .build(),
                            RC_SIGN_IN);
                });
    }

    private void uploadImageToStorage(StorageReference photoRef, Uri selectedImageUri) {

        uploadTask = photoRef.putFile(selectedImageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return photoRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                downloadUri = task.getResult();
                updateProfilePic(downloadUri);
            } else {
                // Handle failures
                // ...
            }
        });

    }
}


