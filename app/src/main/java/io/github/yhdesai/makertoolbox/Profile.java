package io.github.yhdesai.makertoolbox;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class Profile extends Fragment {
    private EditText pUsername;
    private EditText pDisplayName;
    private TextView pEmail;

    private String username;
    private String displayName;
    private String email;
    private String profilePic;
    private Button mSubmitButton;


    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseApp.initializeApp(getActivity());
        mFirebaseAuth = FirebaseAuth.getInstance();

        pUsername = rootView.findViewById(R.id.usernameEditText);
        pDisplayName = rootView.findViewById(R.id.nameEditText);
        pEmail = rootView.findViewById(R.id.emailView);
        mSubmitButton = rootView.findViewById(R.id.submit);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();
                Log.d("provider id", providerId);

                // UID specific to the provider
                String uid = profile.getUid();
                Log.d("uid", uid);

                // Name, email address, and profile photo Url

                displayName = profile.getDisplayName();
                Uri uri = profile.getPhotoUrl();
                /*  profilePic = uri.toString();*/
                email = profile.getEmail();

            }

        }
        pUsername.setText(displayName);
        pEmail.setText(email);
        /*  pDisplayName.setText();*/


        mSubmitButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(pUsername.getText().toString())
                       /* .setPhotoUri(Uri.parse(profilePic))*/
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("profile", "User profile updated.");
                                }
                            }
                        });
            }
        });
        return rootView;
    }


}
