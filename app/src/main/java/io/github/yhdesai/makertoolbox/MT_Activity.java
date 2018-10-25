package io.github.yhdesai.makertoolbox;


import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.github.yhdesai.makertoolbox.ChatChannel.ChatFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.github.yhdesai.makertoolbox.model.DeveloperMessage;
import io.github.yhdesai.makertoolbox.notification.NotificationService;

public class MT_Activity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2;
    private static final int RC_CHAT_PHOTO_PICKER = 3;

    private StorageReference mProfileStroageReference;

    private String mName;
    private String mPlatform;
    private String mChannel = "general";
    private String mDate;
    private String mTime;
    private String mMessage;
    /*private String mDisplayName;*/
    private String mProfilePic;
    private String mVersion;

    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mNotificationsDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;

    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ChatFragment chatFragment;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private Intent service;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    FragmentManager frag = getFragmentManager();
                    frag.beginTransaction().replace(R.id.content_frame, new ChatFragment()).commit();
                    return true;
                case R.id.navigation_tools:
                    FragmentManager frag1 = getFragmentManager();
                    frag1.beginTransaction().replace(R.id.content_frame, new ToolsList()).commit();
                    return true;
                case R.id.navigation_profile:
                    FragmentManager frag2 = getFragmentManager();
                    frag2.beginTransaction().replace(R.id.content_frame, new Profile()).commit();
                    return true;
            }
            return false;
        }
    };

    private void showChannelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setTitle("Channel Alert Dialog");
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt);
        initViews();
        initFirebase();
        FragmentManager frag1 = getFragmentManager();
        chatFragment = new ChatFragment();
        frag1.beginTransaction().replace(R.id.content_frame, chatFragment).commit();
    }

    private void initViews() {
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.channel_menu_general : {
                        mChannel = "general";
                        updateChannel();
                        break;
                    }
                    case R.id.channel_menu_help : {
                        mChannel = "help";
                        updateChannel();
                        break;
                    }
                    case R.id.channel_menu_introductions : {
                        mChannel = "introductions";
                        updateChannel();
                        break;
                    }
                    case R.id.channel_menu_mt_gen : {
                        mChannel = "mt-general";
                        updateChannel();
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.START);
        }
        return true;
    }

    private void updateChannel() {
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(mChannel);
        chatFragment.updateChatRoom(mChannel);
        drawerLayout.closeDrawers();
    }

    private void initFirebase() {
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfileStroageReference = mFirebaseStorage.getReference().child("profile_pic");
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(mChannel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mProfileStroageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                            Log.d("url : ", downloadUrl.toString());

                            // Set the download URL to the message box, so that the user can send it to the database
 *//*                         FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUrl.toString());
                            mMessagesDatabaseReference.push().setValue(friendlyMessage);*//*
                        }
                    });
        }else */

        //TODO fix this
        if (requestCode == RC_CHAT_PHOTO_PICKER && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            final StorageReference photoRef = mProfileStroageReference.child(selectedImageUri.getLastPathSegment());
            photoRef.putFile(selectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
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
                                String uid = profile.getUid();

                                // Name, email address, and profile photo Url
                                    mName = profile.getDisplayName();
                               /* Uri uri = profile.getPhotoUrl();*/
                                 /*  mProfilePic = uri.toString();*/
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
                                mVersion
                        );
                        mMessagesDatabaseReference.push().setValue(developerMessage);
                    } else {
                        Toast.makeText(MT_Activity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Upload file to Firebase Storage
         /*   photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getDownloadURL();

                            //Set the download URL to the message box, so that the user can send it to the database
                            Log.d("url : ", downloadUrl.toString());
*//*                            // Getting the time
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
                                    String uid = profile.getUid();

                                    // Name, email address, and profile photo Url
                                    *//**//*  mDisplayName = profile.getDisplayName();*//**//*
                                    Uri uri = profile.getPhotoUrl();
                                    *//**//*mProfilePic = uri.toString();*//**//*
                                }

                            }


                            DeveloperMessage developerMessage = new DeveloperMessage(
                                    mName,
                                    *//**//*mDisplayName,*//**//*
                                    mProfilePic,
                                    mMessage,
                                    downloadUrl.toString(),
                                    mTime,
                                    mDate,
                                    mPlatform,
                                    mVersion
                            );
                            mMessagesDatabaseReference.push().setValue(developerMessage);

                            // sendNotificationToUser(mChannel, null, mMessageEditText.getText().toString());
                        *//*
                        }
                    });*/

        }
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            if (event.getAction() == KeyEvent.ACTION_DOWN){


                super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_UP));
                super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_UP));

                return true;
            }}

        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (event.getAction() == KeyEvent.ACTION_DOWN){

                super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN));
                super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_DOWN));
                Intent list = new Intent(MT_Activity.this, Dev.class);
                startActivity(list);

                return true;
            }}
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(service == null) service = new Intent(getBaseContext(), NotificationService.class);
        if(isServiceRunning(NotificationService.class)) {
            NotificationService.state = false;
            stopService(service);
        }

        Intent i = new Intent(NotificationService.service_broadcast);
        this.sendBroadcast(i, NotificationService.service_broadcast);
    }

    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        if(!isServiceRunning(NotificationService.class)) startService(service);
        super.onPause();
    }
}


