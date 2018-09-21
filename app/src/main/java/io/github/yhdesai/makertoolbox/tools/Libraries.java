package io.github.yhdesai.makertoolbox.tools;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.yhdesai.makertoolbox.R;
import io.github.yhdesai.makertoolbox.adapter.LibraryAdapter;
import io.github.yhdesai.makertoolbox.model.DeveloperAwesomeLibraries;
import io.github.yhdesai.makertoolbox.tools.add.addLibrary;


public class Libraries extends AppCompatActivity {

    private static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Awesome_Libraries";
    private LibraryAdapter LibraryAdapter;

    private String mUsername;

    private DatabaseReference mLibrariesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awesome__libraries);

        mUsername = ANONYMOUS;
        // Initialize Firebase components
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mLibrariesDatabaseReference = mFirebaseDatabase.getReference().child("libraries");


        // Initialize references to views
        ProgressBar mProgressBar = findViewById(R.id.progressBar);
        ListView mLibraryListView = findViewById(R.id.libraryListView);


        // Initialize message ListView and its adapter

        List<DeveloperAwesomeLibraries> developerLibrary = new ArrayList<>();
        LibraryAdapter = new LibraryAdapter(Libraries.this, R.layout.item_library, developerLibrary);
        mLibraryListView.setAdapter(LibraryAdapter);

        /*

         List<DeveloperToDo> friendlyTodo = new ArrayList<>();
        ToDoAdapter = new ToDoAdapter(getActivity(), R.layout.item_todo, friendlyTodo);
        mMessageListView.setAdapter(ToDoAdapter);
         */


        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

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
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                    //    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                            ))
                                    .build(),
                            RC_SIGN_IN);


                }

            }
        };


    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        LibraryAdapter.clear();
        detachDatabaseReadListener();

    }

    private void attachDatabaseReadListener() {

        if (mChildEventListener == null) {


            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DeveloperAwesomeLibraries developerAwesomeLibraries = dataSnapshot.getValue(DeveloperAwesomeLibraries.class);
                    LibraryAdapter.add(developerAwesomeLibraries);
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

            mLibrariesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mLibrariesDatabaseReference.removeEventListener(mChildEventListener);
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
        LibraryAdapter.clear();
    }

    public void add_library(View view) {
        startActivity(new Intent(Libraries.this, addLibrary.class));
    }
}
