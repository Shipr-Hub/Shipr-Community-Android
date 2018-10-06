package io.github.yhdesai.makertoolbox.tools;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.yhdesai.makertoolbox.R;
import io.github.yhdesai.makertoolbox.adapter.ToDoAdapter;
import io.github.yhdesai.makertoolbox.model.DeveloperToDo;
import io.github.yhdesai.makertoolbox.tools.add.addTodo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class todo extends Fragment {
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "general";
    private ToDoAdapter ToDoAdapter;
    private String mUid;
    private String mUsername;

    // Firebase instance variable
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTodoDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);
        FirebaseApp.initializeApp(getActivity());
        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is signed in
                    onSignedInInitialize(user.getDisplayName(), user.getUid());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Collections.singletonList(
                                                    //   new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build(),
                                                    // new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                    //    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                            ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        // Initialize references to views
        ProgressBar mProgressBar = rootView.findViewById(R.id.progressBar);
        ListView mTodoListView = rootView.findViewById(R.id.todoListView);

        // Initialize To-Do ListView and its adapter
        List<DeveloperToDo> friendlyTodo = new ArrayList<>();
        ToDoAdapter = new ToDoAdapter(getActivity(), R.layout.item_todo, friendlyTodo);
        mTodoListView.setAdapter(ToDoAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        FloatingActionButton addtodobtn = rootView.findViewById(R.id.addTodo);
        addtodobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), addTodo.class));
            }
        });

        return rootView;


    }

    private void onSignedInInitialize(String username, String uid) {
        mUid = uid;
        mUsername = username;
        String todoLoc = "todo/" + mUid;
        todoLoc = todoLoc.replaceAll(" ", "_").toLowerCase();
        mTodoDatabaseReference = mFirebaseDatabase.getReference().child(todoLoc);
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = "ANONYMOUS";
        ToDoAdapter.clear();
        detachDatabaseReadListener();

    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DeveloperToDo developerTodo = dataSnapshot.getValue(DeveloperToDo.class);
                    ToDoAdapter.add(developerTodo);
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

            mTodoDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mTodoDatabaseReference.removeEventListener(mChildEventListener);
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
        ToDoAdapter.clear();
    }


}