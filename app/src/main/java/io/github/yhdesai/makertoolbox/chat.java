package io.github.yhdesai.makertoolbox;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import io.github.yhdesai.makertoolbox.ChatChannel.Ideas;
import io.github.yhdesai.makertoolbox.ChatChannel.resources;
import io.github.yhdesai.makertoolbox.ChatChannel.general;
import io.github.yhdesai.makertoolbox.ChatChannel.bug;
import io.github.yhdesai.makertoolbox.ChatChannel.help;
import io.github.yhdesai.makertoolbox.ChatChannel.intro;


public class chat extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager general = getFragmentManager();
        general.beginTransaction().replace(R.id.content_chat_frame, new general()).commit();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent signout = new Intent(chat.this, MainActivity.class);
            startActivity(signout);
            return true;
        } //else if (id == R.id.action_settings) {
            //return true;
       // }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.chat_general) {
            FragmentManager general = getFragmentManager();
            general.beginTransaction().replace(R.id.content_chat_frame, new general()).commit();
        } else if (id == R.id.chat_intro) {
            FragmentManager intro = getFragmentManager();
            intro.beginTransaction().replace(R.id.content_chat_frame, new intro()).commit();
        } else if (id == R.id.chat_ideas) {
            FragmentManager ideas = getFragmentManager();
            ideas.beginTransaction().replace(R.id.content_chat_frame, new Ideas()).commit();
        } else if (id == R.id.chat_help) {
            FragmentManager help = getFragmentManager();
            help.beginTransaction().replace(R.id.content_chat_frame, new help()).commit();
        } else if (id == R.id.chat_resource) {
            FragmentManager res = getFragmentManager();
            res.beginTransaction().replace(R.id.content_chat_frame, new resources()).commit();
        } else if (id == R.id.chat_bug){
            FragmentManager bug = getFragmentManager();
            bug.beginTransaction().replace(R.id.content_chat_frame, new bug()).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
