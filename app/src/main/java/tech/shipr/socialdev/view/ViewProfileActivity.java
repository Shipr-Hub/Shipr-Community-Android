package tech.shipr.socialdev.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tech.shipr.socialdev.R;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // Get the data from the intent
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        //TODO Based on the user id, load the persons data
    }
}
