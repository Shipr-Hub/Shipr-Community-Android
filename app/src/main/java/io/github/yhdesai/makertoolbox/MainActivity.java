package io.github.yhdesai.makertoolbox;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


/**
 DrawerLayout drawer = findViewById(R.id.drawer_layout);
 ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
 this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
 drawer.addDrawerListener(toggle);
 toggle.syncState();
 **/
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseApp.initializeApp(this);

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
        //     DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //    if (drawer.isDrawerOpen(GravityCompat.START)) {
        //         drawer.closeDrawer(GravityCompat.START);
        //     } else {
        super.onBackPressed();
        //    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
     /*   if (id == R.id.action_settings) {
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
        }

        //   getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("my_fragment").commit();

        //  DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //  drawer.closeDrawer(GravityCompat.START);
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
}
