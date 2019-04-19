package tech.shipr.socialdev.view;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconsPopup;
import tech.shipr.socialdev.R;
import tech.shipr.socialdev.adapter.MessageAdapter;
import tech.shipr.socialdev.model.DeveloperMessage;
import tech.shipr.socialdev.notification.NotificationService;


public class ChatChannel extends Fragment {
    private static final String ANONYMOUS = "anonymous";
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_SIGN_IN = 1;
    private MessageAdapter mMessageAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageButton mSendButton;

    private String mName;
    private String mDate;
    private String mTime;
    private String mMessage;
    private String mProfilePic;
    private Boolean mProgressBarPresent = true;

    // Firebase instance variable
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private EmojiconEditText mEmojiconEditText;

    private String mChannel;
    private ProgressBar mProgressBar;
    private String uid;
    private RecyclerView mMessageRecycler;
    private EmojIconActions mEmojicon;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_general, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        mName = ANONYMOUS;
        mChannel = "general";

        initFirebase();


        // Initialize references to views

        mProgressBar = rootView.findViewById(R.id.progressBar);
        mMessageRecycler = rootView.findViewById(R.id.messageRecyclerView);
        mSendButton = rootView.findViewById(R.id.sendButton);
        ImageView mEmojiButton = rootView.findViewById(R.id.emojiButton);
        mEmojiconEditText = rootView.findViewById(R.id.emojicon_edit_text);
        mEmojicon = new EmojIconActions(Objects.requireNonNull(getContext()).getApplicationContext(), rootView, mEmojiconEditText, mEmojiButton);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Init RecyclerView
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(mLinearLayoutManager);
        mMessageRecycler.setHasFixedSize(true);

        //Clear Notification
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        int channel_id = NotificationService.getIntId("general");
        if (channel_id != -1) notificationManager.cancel(channel_id);

        //Emoji
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
        disableAutoOpenEmoji(mEmojicon);

        setUpAdapter();

        //disable send button (will be enabled when text is present)
        mSendButton.setEnabled(false);

        // Enable Send button when there's text to send
        editTextWatcher();

        FirebaseMessaging.getInstance().subscribeToTopic(mChannel);

        // Send button sends a item_message and clears the EditText
        mSendButton.setOnClickListener(view -> {
            sendMessage();
            mEmojiconEditText.setText("");
        });


        authStateCheck();

        //Keep the keyboard closed on start
        //      ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setUpAdapter() {
        // Init Firebase Recycler options
        FirebaseRecyclerOptions<DeveloperMessage> options =
                new FirebaseRecyclerOptions.Builder<DeveloperMessage>()
                        .setQuery(mMessagesDatabaseReference, DeveloperMessage.class)
                        .setLifecycleOwner(this)
                        .build();
        // Init adapter
        mMessageAdapter = new MessageAdapter(getContext(), options) {
            @Override
            public void onLoaded() {
                mProgressBarCheck();
            }
        };

        // Scroll to bottom on new messages
        mMessageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount());
            }
        });

        // Set the adapter
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    private void disableAutoOpenEmoji(EmojIconActions emojActions) {
        try {
            Field field = emojActions.getClass().getDeclaredField("popup");
            field.setAccessible(true);
            EmojiconsPopup emojiconsPopup = (EmojiconsPopup) field.get(emojActions);
            field = emojiconsPopup.getClass().getDeclaredField("pendingOpen");
            field.setAccessible(true);
            field.set(emojiconsPopup, false);
        } catch (Exception ignored) {

        }
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        mTime = sdf.format(new Date());

        // Getting the date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        mDate = day + "-" + month + "-" + year;

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
        String mVersion = "1";
        String mPlatform = "Android";
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

    private void onSignedInInitialize(String username) {
        mName = username;
        updateChannel(mChannel);
    }

    private void onSignedOutCleanup() {
        mName = ANONYMOUS;
        detachDatabaseReadListener();
    }

    private void mProgressBarCheck() {
        if (mProgressBarPresent) {
            mProgressBar.setVisibility(View.GONE);
            mProgressBarPresent = false;

        }
    }

    private void detachDatabaseReadListener() {
        mMessageAdapter.stopListening();
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
    }

    public void updateChannel(String channelName) {
        detachDatabaseReadListener();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("chat/" + channelName);

        setUpAdapter();
        subToChannel(channelName);

    }

    private void subToChannel(String channelName) {
        FirebaseMessaging.getInstance().subscribeToTopic(channelName)
                .addOnCompleteListener(task -> {
                    String msg = "success";
                    if (!task.isSuccessful()) {
                        msg = "failed";

                    }
                    Log.d("msg", msg);
                    //    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                });
    }

}