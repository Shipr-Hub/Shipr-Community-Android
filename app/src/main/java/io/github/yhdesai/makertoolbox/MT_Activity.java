package io.github.yhdesai.makertoolbox;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import io.github.yhdesai.makertoolbox.ChatChannel.general;

public class MT_Activity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    FragmentManager frag = getFragmentManager();
                    frag.beginTransaction().replace(R.id.content_frame, new general()).commit();

                    return true;
                case R.id.navigation_tools:
                    FragmentManager frag1 = getFragmentManager();
                    frag1.beginTransaction().replace(R.id.content_frame, new ToolsList()).commit();

                    //
                    return true;
                case R.id.navigation_profile:
                    FragmentManager frag2 = getFragmentManager();
                    frag2.beginTransaction().replace(R.id.content_frame, new Profile()).commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager frag1 = getFragmentManager();
        frag1.beginTransaction().replace(R.id.content_frame, new general()).commit();


    }

}
