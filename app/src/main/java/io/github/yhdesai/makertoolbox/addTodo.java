package io.github.yhdesai.makertoolbox;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Calendar;

public class addTodo extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private static final String TAG = "addTodo";
   /* private EditText todoDesc;*/
    private Button mSendButton;
    /*private TextView dateTextViewButton;*/

    private String mUsername;
    private EditText todoName;

    /*private String isFeature;
    private String isBug;*/

  /*  private int day;
    private int month;
    private int year;*/

    // Firebase instance variable
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    /*private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            *//*dateTextViewButton.setText(selectedDay + "/" + (selectedMonth + 1) + "/"
                    + selectedYear);*//*
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        mUsername = ANONYMOUS;
        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();


        // Initialize references to views
        todoName = findViewById(R.id.todoName);
      /*  todoDesc = findViewById(R.id.todoDesc);*/
        mSendButton = findViewById(R.id.sendButton);
      /*  dateTextViewButton = findViewById(R.id.dateTextViewButton);*/


        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Sending the Message
                String sTodoName = todoName.getText().toString();
                if (sTodoName.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "ToDo Name can't be empty", Toast.LENGTH_SHORT);

                    toast.show();
                } else {


                   // DeveloperToDo developerToDo = new DeveloperToDo(todoName.getText().toString(), todoDesc.getText().toString(), dateTextViewButton.getText().toString(), isFeature, isBug);
                    DeveloperToDo developerToDo = new DeveloperToDo(todoName.getText().toString(), null, null, null, null);
                    mMessagesDatabaseReference.push().setValue(developerToDo);

                    // Clear input box
                    todoName.setText("");
                  /*  todoDesc.setText("");
                    dateTextViewButton.setText("");*/
                    finish();
                }
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


       /* dateTextViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(dateTextViewButton.getWindowToken(), 0);


                showDialog(0);
            }
        });*/

    }

  /*  @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }*/


    private void onSignedInInitialize(String username) {
        mUsername = username;
        String todoLoc = "todo/" + mUsername;
        todoLoc = todoLoc.replaceAll(" ", "_").toLowerCase();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(todoLoc);
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;

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

    }

    /*public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkbox_feature:
                if (checked) {

                    isFeature = "Feature";
                } else
                    isFeature = "";

                break;
            case R.id.checkbox_bug:
                if (checked) {
                    isBug = "Bug";
                } else
                    isBug = "";
                break;

        }
    }*/


}