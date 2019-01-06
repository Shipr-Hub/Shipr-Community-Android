package tech.shipr.socialdev.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import tech.shipr.socialdev.R;
import tech.shipr.socialdev.adapter.MessageAdapter;
import tech.shipr.socialdev.model.DeveloperMessage;


public class ChatChannel extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String ANONYMOUS = "anonymous";
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_SIGN_IN = 1;
    private MessageAdapter mMessageAdapter;
    private ImageButton mSendButton;

    private String mName;
    private final String mPlatform = "Android";
    private String mDate;
    private String mTime;
    private String mMessage;
    private String mProfilePic;
    private final String mVersion = "1";
    private Boolean mProgressBarPresent = true;

    // Firebase instance variable
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private EmojiconEditText mEmojiconEditText;
    private EmojIconActions mEmojicon;
    private ImageView mEmojiButton;

    private ListView mMessageListView;

    private String mChannel;
    private ProgressBar mProgressBar;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_general, container, false);


        mName = ANONYMOUS;
        mChannel = "general";

        initFirebase();


        // Initialize references to views

        mProgressBar = rootView.findViewById(R.id.progressBar);
        ListView mMessageListView = rootView.findViewById(R.id.messageListView);


        mMessageListView = rootView.findViewById(R.id.messageListView);

        mSendButton = rootView.findViewById(R.id.sendButton);

        mEmojiButton = rootView.findViewById(R.id.emojiButton);
        mEmojiconEditText = rootView.findViewById(R.id.emojicon_edit_text);


        //Initialize spinner'
        Spinner spinner = rootView.findViewById(R.id.chatChannelSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                R.array.chat_channels, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Emoji
        mEmojicon = new EmojIconActions(getContext().getApplicationContext(), rootView, mEmojiconEditText, mEmojiButton);
        mEmojicon.ShowEmojIcon();
        mEmojicon.setUseSystemEmoji(true);

        mEmojicon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });

        mEmojicon.addEmojiconEditTextList(mEmojiconEditText);


        // Initialize message ListView and its adapter
        List<DeveloperMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(getActivity(), R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);


        //disable send button (will be enabled when text is present)
        mSendButton.setEnabled(false);

        // Enable Send button when there's text to send
        editTextWatcher();

        FirebaseMessaging.getInstance().subscribeToTopic(mChannel);


        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                sendNotificationToUser();
                mEmojiconEditText.setText("");
            }
        });

        authStateCheck();


        //Keep the keyboard closed on start
        ((Activity) getContext()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return rootView;
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(mChannel);
    }


    private void authStateCheck() {
        mAuthStateListener = firebaseAuth -> {
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
                                .setLogo(R.mipmap.ic_launcher)
                                .setTheme(R.style.AppTheme)
                                .setAvailableProviders(
                                        Arrays.asList(
                                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                                new AuthUI.IdpConfig.GoogleBuilder().build()
                                                //new AuthUI.IdpConfig.GitHubBuilder().build()
                                        ))
                                .build(),
                        RC_SIGN_IN);


            }

        };
    }

    private void editTextWatcher() {
        mEmojiconEditText.addTextChangedListener(new TextWatcher() {
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
        mEmojiconEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    private void initVariable() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        mTime = sdf.format(new Date());

        // Getting the date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        mDate = String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year);

        mMessage = mEmojiconEditText.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid = Objects.requireNonNull(user).getUid();


        if (Objects.requireNonNull(user).getPhotoUrl() != null) {
            mProfilePic = Objects.requireNonNull(user.getPhotoUrl()).toString();
        }
    }

    private void sendMessage() {
        initVariable();
        // Sending the Message
        DeveloperMessage developerMessage = new DeveloperMessage(
                mName,
                mProfilePic,
                mMessage,
                null,
                mTime,
                mDate,
                mPlatform,
                mVersion,
                uid);
        mMessagesDatabaseReference.push().setValue(developerMessage);


    }

    private void sendNotificationToUser() {
        DatabaseReference mNotificationsDatabaseReference = mFirebaseDatabase.getReference().child("notificationRequests");

        Map<String, String> notification = new HashMap<>();

        notification.put("channel", mChannel);
        //    notification.put("username", null);
        notification.put("message", mMessage);

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

    private void mProgressBarCheck() {
        if (mProgressBarPresent) {
            mProgressBar.setVisibility(View.GONE);
            mProgressBarPresent = false;

        }
    }

    private void attachDatabaseReadListener() {

        if (mChildEventListener == null) {


            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    DeveloperMessage developerMessage = dataSnapshot.getValue(DeveloperMessage.class);
                    mMessageAdapter.add(developerMessage);
                    mProgressBarCheck();
                }

                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(@NonNull DatabaseError databaseError) {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String items = parent.getSelectedItem().toString();
        Log.i("Selected item : ", items);
        mChannel = items;
        updateChannel(items);
    }

    public void updateChannel(String channelName) {
        mMessageAdapter.clear();
        detachDatabaseReadListener();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("chat/" + channelName);
        attachDatabaseReadListener();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
