package io.github.yhdesai.makertoolbox.ChatChannel;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.github.yhdesai.makertoolbox.DeveloperMessage;
import io.github.yhdesai.makertoolbox.adapter.MessageAdapter;
import io.github.yhdesai.makertoolbox.R;


public class general extends Fragment {
    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private static final String TAG = "general";
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mName;
    private String mPlatform;
    private String mChannel = "general";
    private String mDate;
    private String mTime;
    private String mMessage;
    /*private String mDisplayName;*/
    private String mProfilePic;
    private String mVersion;
    private Button addPic;

    // Firebase instance variable
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mNotificationsDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final int RC_CHAT_PHOTO_PICKER = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_general, container, false);
        FirebaseApp.initializeApp(getActivity());


        mName = ANONYMOUS;
        mPlatform = "Android";
        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("general");


        // Initialize references to views
        mProgressBar = rootView.findViewById(R.id.progressBar);
        mMessageListView = rootView.findViewById(R.id.messageListView);
        mMessageEditText = rootView.findViewById(R.id.messageEditText);
        mSendButton = rootView.findViewById(R.id.sendButton);
        addPic = rootView.findViewById(R.id.addPic);

        // Initialize message ListView and its adapter

        List<DeveloperMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(getActivity(), R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);


        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mSendButton.setEnabled(false);

        /* FirebaseMessaging.getInstance().subscribeToTopic("general");*/

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_CHAT_PHOTO_PICKER);
            }
        });


        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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

                mMessage = mMessageEditText.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    for (UserInfo profile : user.getProviderData()) {
                        // Id of the provider (ex: google.com)
                        String providerId = profile.getProviderId();

                        // UID specific to the provider
                        String uid = profile.getUid();

                        // Name, email address, and profile photo Url
                        /*  mDisplayName = profile.getDisplayName();*/
                        Uri uri = profile.getPhotoUrl();
                        /*mProfilePic = uri.toString();*/
                    }

                }


                // Sending the Message
                DeveloperMessage developerMessage = new DeveloperMessage(
                        mName,
                        /*mDisplayName,*/
                        mProfilePic,
                        mMessage,
                        null,
                        mTime,
                        mDate,
                        mPlatform,
                        mVersion
                );
                mMessagesDatabaseReference.push().setValue(developerMessage);

                sendNotificationToUser(mChannel, null, mMessageEditText.getText().toString());


                // Clear input box
                mMessageEditText.setText("");
            }
        });


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
                                                    //   new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                            ))
                                    .build(),
                            RC_SIGN_IN);


                }

            }
        };
        return rootView;


    }

    private void sendNotificationToUser(String channel, String user, final String message) {
        mNotificationsDatabaseReference = mFirebaseDatabase.getReference().child("notificationRequests");


        Map notification = new HashMap<>();
        notification.put("channel", channel);
        notification.put("username", user);
        notification.put("message", message);

        mNotificationsDatabaseReference.push().setValue(notification);
    }

    private void onSignedInInitialize(String username) {
        mName = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mName = ANONYMOUS;
        mMessageAdapter.clear();
        detachDatabaseReadListener();

    }


    private void attachDatabaseReadListener() {

        if (mChildEventListener == null) {


            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DeveloperMessage developerMessage = dataSnapshot.getValue(DeveloperMessage.class);
                    mMessageAdapter.add(developerMessage);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };

            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mMessageAdapter.clear();
    }


}