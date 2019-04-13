package tech.shipr.socialdev.view;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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

import tech.shipr.socialdev.BillingActivity;
import tech.shipr.socialdev.PrivacyPolicy;
import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.DeveloperMessage;
import tech.shipr.socialdev.notification.NotificationService;


public class MainActivity extends AppCompatActivity {

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
    private final String mVersion = "1";
    private Uri downloadUri;
    private String uid;
    private DatabaseReference mMessagesDatabaseReference;
    private static DatabaseReference rootRef;
    private Intent service;
    private Toolbar toolbar;
    private ChatChannel chatChannel;
    private TextView categoryTextView;
    // Firebase instance variable

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_chat:
                FragmentManager frag = getSupportFragmentManager();
                chatChannel = new ChatChannel();
                setCategoryLabel(getResources().getString(R.string.general));
                frag.beginTransaction().replace(R.id.content_frame, chatChannel).commit();

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

        chatChannel = new ChatChannel();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        categoryTextView = findViewById(R.id.categoryTextView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.filter));
        setSupportActionBar(toolbar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager frag1 = getSupportFragmentManager();
        frag1.beginTransaction().replace(R.id.content_frame, chatChannel).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_channel_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.general:
                chatChannel.updateChannel(getResources().getStringArray(R.array.chat_channels)[0]);
                setCategoryLabel(getResources().getString(R.string.general));
                return true;
            case R.id.help:
                chatChannel.updateChannel(getResources().getStringArray(R.array.chat_channels)[1]);
                setCategoryLabel(getResources().getString(R.string.help));
                return true;
            case R.id.android:
                chatChannel.updateChannel(getResources().getStringArray(R.array.chat_channels)[2]);
                setCategoryLabel(getResources().getString(R.string.android));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setCategoryLabel(String categoryLabel) {
        categoryTextView.setText(categoryLabel);
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);

        if (rootRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            rootRef = database.getReference();
        }

        mMessagesDatabaseReference = rootRef.child("chat/general");

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();


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
                Log.d("Error Code", String.valueOf(Objects.requireNonNull(Objects.requireNonNull(response).getError()).getErrorCode()));
                Log.d("Error Message", Objects.requireNonNull(response.getError()).getMessage());
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
                final StorageReference photoRef = mProfileStroageReference.child(Objects.requireNonNull(Objects.requireNonNull(selectedImageUri).getLastPathSegment()));


                photoRef.putFile(selectedImageUri).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return photoRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        //TODO return the url of the image uploaded here


                        // Getting the time
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
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
                                Objects.requireNonNull(downloadUri).toString(),
                                mTime,
                                mDate,
                                mPlatform,
                                mVersion,
                                uid);
                        mMessagesDatabaseReference.push().setValue(developerMessage);
                    } else {
                        Toast.makeText(MainActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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

        UploadTask uploadTask = photoRef.putFile(selectedImageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }

            // Continue with the task to get the download URL
            return photoRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                downloadUri = task.getResult();
                updateProfilePic(downloadUri);
            }  // Handle failures
            // ...

        });

    }

    public void purchaseHelp(View view) {
        startActivity(new Intent(MainActivity.this, BillingActivity.class));
    }

    public void openPrivacyPolicy(View view) {
        startActivity(new Intent(MainActivity.this, PrivacyPolicy.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (service == null) service = new Intent(getBaseContext(), NotificationService.class);
        if (isServiceRunning(NotificationService.class)) {
            NotificationService.state = false;
            stopService(service);
        }

        Intent i = new Intent(NotificationService.service_broadcast);
        this.sendBroadcast(i, NotificationService.service_broadcast);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        if (!isServiceRunning(NotificationService.class)) startService(service);
        super.onPause();
    }

}


