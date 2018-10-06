package io.github.yhdesai.makertoolbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.yhdesai.makertoolbox.billing.GamePlayActivity;
import io.github.yhdesai.makertoolbox.ssh.SshActivity;
import io.github.yhdesai.makertoolbox.tools.CodeEditor;
import io.github.yhdesai.makertoolbox.tools.ComponentDesigner;
import io.github.yhdesai.makertoolbox.tools.Libraries;
import io.github.yhdesai.makertoolbox.tools.colorPicker;

public class Dev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
    }


    public void library(View v) {
        startActivity(new Intent(Dev.this, Libraries.class));
    }


    public void intro(View v) {
        startActivity(new Intent(Dev.this, Welcome_Screen.class));
    }


    public void color(View v) {
        startActivity(new Intent(Dev.this, colorPicker.class));
    }

    public void ssh(View v) {
        startActivity(new Intent(Dev.this, SshActivity.class));
    }


    public void code_editor(View view) {
        startActivity(new Intent(Dev.this, CodeEditor.class));
    }

    public void billing(View view) {startActivity(new Intent(Dev.this, GamePlayActivity.class));}

    public void xml_designer(View view) {
        startActivity(new Intent(Dev.this, ComponentDesigner.class));
    }
}
