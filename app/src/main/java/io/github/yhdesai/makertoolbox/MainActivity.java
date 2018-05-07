package io.github.yhdesai.makertoolbox;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

import android.content.res.Resources;


// This is the MainActivity which opens when the App is opened

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseApp.initializeApp(this);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
 ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
 this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
 drawer.addDrawerListener(toggle);
 toggle.syncState();



        //Check if the User is signed in

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is signed in
                    onSignedInInitialize(user.getDisplayName());
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    navigationView.setNavigationItemSelectedListener(MainActivity.this);
                    View hView = navigationView.getHeaderView(0);
                    TextView user_email = hView.findViewById(R.id.nUserEmail);
                    user_email.setText(user.getEmail());
                    TextView user_name = hView.findViewById(R.id.nUserName);
                    user_name.setText(user.getDisplayName());



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
                mUsername = username;

            }

            private void onSignedOutCleanup() {
                mUsername = ANONYMOUS;


            }
        };


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /*  int id = item.getItemId();
       if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chat) {
            startActivity(new Intent(MainActivity.this, chat.class));
        } else if (id == R.id.todo) {
            FragmentManager help = getFragmentManager();
            help.beginTransaction().replace(R.id.content_frame, new todo()).addToBackStack("general").commit();
        } else if (id == R.id.color) {
            startActivity(new Intent(MainActivity.this, colorPicker.class));


        } else if (id == R.id.nav_awe_lib) {
            startActivity(new Intent(MainActivity.this, Awesome_Libraries.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));
        } else if (id == R.id.nav_edit_profile) {
            startActivity(new Intent(MainActivity.this, Edit_Profile.class));
        } else if (id == R.id.nav_intro) {
            startActivity(new Intent(MainActivity.this, Welcome_Screen.class));
        } else if (id == R.id.nav_git) {
            startActivity(new Intent(MainActivity.this, Github.class));
        }

        //   getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("my_fragment").commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void addToDo(View view) {
        startActivity(new Intent(MainActivity.this, addTodo.class));
    }

    public void chat(View view) {
        startActivity(new Intent(MainActivity.this, chat.class));
    }

    public void todo(View view) {
        FragmentManager help = getFragmentManager();
        help.beginTransaction().replace(R.id.content_frame, new todo()).commit();
    }

    public void dev(View view) {
        startActivity(new Intent(MainActivity.this, Dev.class));
    }


    public void color(View view) {
        startActivity(new Intent(MainActivity.this, colorPicker.class));
    }
}
