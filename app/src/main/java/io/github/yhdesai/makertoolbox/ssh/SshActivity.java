
package io.github.yhdesai.makertoolbox.ssh;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import io.github.yhdesai.makertoolbox.R;
import io.github.yhdesai.makertoolbox.ssh.dialogs.SshConnectFragmentDialog;
import io.github.yhdesai.makertoolbox.ssh.sshutils.ConnectionStatusListener;
import io.github.yhdesai.makertoolbox.ssh.sshutils.ExecTaskCallbackHandler;
import io.github.yhdesai.makertoolbox.ssh.sshutils.SessionController;

/**
 * Main SSH activity. Connect to SSH server and launch command shell.
 */

//TODO add a top bar and adda play/stop button there
//TODO redesign the ssh connect xml layout
//TODO Add back button disconnect warning

public class SshActivity extends Activity implements OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView mConnectStatus;

    private io.github.yhdesai.makertoolbox.ssh.SshEditText mCommandEdit;
    private Button mEndSessionBtn, mSftpButton;

    private Handler mHandler;
    private Handler mTvHandler;
    private String mLastLine;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Set no title
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_ssh);
        mEndSessionBtn = findViewById(R.id.endsessionbutton);
        mSftpButton = findViewById(R.id.sftpbutton);
        mCommandEdit = findViewById(R.id.command);
        mConnectStatus = findViewById(R.id.connectstatus);
        // set onclicklistener
        // mButton.setOnClickListener(this);
        mEndSessionBtn.setOnClickListener(this);
        mSftpButton.setOnClickListener(this);

        mConnectStatus.setText("Connect Status: NOT CONNECTED");
        //handlers
        mHandler = new Handler();
        mTvHandler = new Handler();


        //text change listener, for getting the current input changes.
        mCommandEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String[] sr = editable.toString().split("\r\n");
                mLastLine = sr[sr.length - 1];

            }
        });


        mCommandEdit.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        //Log.d(TAG, "editor action " + event);
                        if (isEditTextEmpty(mCommandEdit)) {
                            return false;
                        }

                        // run command
                        else {
                            if (event == null || event.getAction() != KeyEvent.ACTION_DOWN) {
                                return false;
                            }
                            // get the last line of terminal
                            String command = getLastLine();
                            ExecTaskCallbackHandler t = new ExecTaskCallbackHandler() {
                                @Override
                                public void onFail() {
                                    makeToast();
                                }

                                @Override
                                public void onComplete(String completeString) {
                                }
                            };
                            mCommandEdit.AddLastInput(command);
                            SessionController.getSessionController().executeCommand(mHandler, mCommandEdit, t, command);
                            return false;
                        }
                    }
                }
        );
        showDialog();
    }


    /**
     * Displays toast to user.
     *
     */

    private void makeToast() {
        Toast.makeText(this, getResources().getString(R.string.taskfail), Toast.LENGTH_SHORT).show();
    }

    /**
     * Start activity to do SFTP transfer. User will choose from list of files
     * to transfer.
     */
    private void startSftpActivity() {
        Intent intent = new Intent(this, FileListActivity.class);
        String[] info = {
                SessionController.getSessionController().getSessionUserInfo().getUser(),
                SessionController.getSessionController().getSessionUserInfo().getHost(),
                SessionController.getSessionController().getSessionUserInfo().getPassword()
        };

        intent.putExtra("UserInfo", info);

        startActivity(intent);
    }

    /**
     * @return
     */
    private String getLastLine() {
        int index = mCommandEdit.getText().toString().lastIndexOf("\n");
        if (index == -1) {
            return mCommandEdit.getText().toString().trim();
        }
        if (mLastLine == null) {
            Toast.makeText(this, "no text to process", Toast.LENGTH_LONG);
            return "";
        }
        String[] lines = mLastLine.split(Pattern.quote(mCommandEdit.getPrompt()));
        String lastLine = mLastLine.replace(mCommandEdit.getPrompt().trim(), "");
        Log.d(TAG, "command is " + lastLine + ", prompt is  " + mCommandEdit.getPrompt());
        return lastLine.trim();
    }

    private String getSecondLastLine() {

        String[] lines = mCommandEdit.getText().toString().split("\n");
        if (lines == null || lines.length < 2) return mCommandEdit.getText().toString().trim();

        else {
            int len = lines.length;
            String ln = lines[len - 2];
            return ln.trim();
        }
    }

    /**
     * Checks if the EditText is empty.
     *
     * @param editText
     * @return true if empty
     */
    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText() == null || editText.getText().toString().equalsIgnoreCase("");
    }

    public void onClick(View v) {
       /* if (v == mButton) {
           showDialog();*/

        if (v == mSftpButton) {
            if (SessionController.isConnected()) {

                startSftpActivity();

            }
        } else if (v == this.mEndSessionBtn) {
            try {
                if (SessionController.isConnected()) {
                    SessionController.getSessionController().disconnect();
                }
            } catch (Throwable t) { //catch everything!
                Log.e(TAG, "Disconnect exception " + t.getMessage());
            }

        }

    }

    private void showDialog() {

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
                    @Override
                    public void run() {
                        mConnectStatus.setText("Connection Status: CONNECTED");
                    }
                });
            }
        });

        newFragment.show(ft, "dialog");
    }
}
