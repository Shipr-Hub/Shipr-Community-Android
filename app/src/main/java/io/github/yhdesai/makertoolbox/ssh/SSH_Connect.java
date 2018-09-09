package io.github.yhdesai.makertoolbox.ssh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.yhdesai.makertoolbox.R;

public class SSH_Connect extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssh__connect);
    }

   /* public void sshConnect(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        ft.addToBackStack(null);

        // Create and show the dialog.
        SshConnectFragmentDialog newFragment = SshConnectFragmentDialog.newInstance();
        newFragment.setListener(new ConnectionStatusListener() {
            @Override
            public void onDisconnected() {

                mTvHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mConnectStatus.setText("Connection Status: NOT CONNECTED");
                    }
                });
            }

            @Override
            public void onConnected() {

                mTvHandler.post(new Runnable() {
                    public static final String TAG = "SSH_Connect";

                    @Override
                    public void run() {
                        Log.i(TAG, "Connected");
                    }
                });
            }
        });

        newFragment.show(ft, "dialog");
    }*/
}

