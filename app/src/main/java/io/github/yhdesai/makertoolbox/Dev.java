package io.github.yhdesai.makertoolbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.yhdesai.makertoolbox.ssh.SshActivity;

public class Dev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
    }

    public void chat(View v) {
        startActivity(new Intent(Dev.this, chat.class));
    }

    public void add_todo(View v) {
        startActivity(new Intent(Dev.this, addTodo.class));
    }

    public void library(View v) {
        startActivity(new Intent(Dev.this, Awesome_Libraries.class));
    }

    public void profile(View v) {
        startActivity(new Intent(Dev.this, Profile.class));
    }

    public void edit_profile(View v) {
        startActivity(new Intent(Dev.this, Edit_Profile.class));
    }

    public void intro(View v) {
        startActivity(new Intent(Dev.this, Welcome_Screen.class));
    }

    public void git(View v) {
        startActivity(new Intent(Dev.this, Github.class));
    }

    public void color(View v) {
        startActivity(new Intent(Dev.this, colorPicker.class));
    }

    public void ssh(View v) {
        startActivity(new Intent(Dev.this, SshActivity.class));
    }



}
