package tech.shipr.socialdev.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tech.shipr.socialdev.R;
import tech.shipr.socialdev.model.DeveloperMessage;

public class Profile extends Fragment {
    private TextView pUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mprofileDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    private String fullName;
    private String username;
    private String email;
    private String age;
    private String languages;
    private String github;
    private String twitter;
    private String linkedin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseApp.initializeApp(getActivity());


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String id = user.getUid();
        mprofileDatabaseReference = mFirebaseDatabase.getReference().child("user" + "/" + id + "/" + "profile");





//        //TODO profile pic
//        mProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                Log.d("ping", "Ping");
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
//                Log.d("pong", "Pong");
//            }
//        });


        return rootView;
    }

    private void getVariables() {
    }
    private void sendMessage(){
        getVariables();
        tech.shipr.socialdev.model.Profile profile = new tech.shipr.socialdev.model.Profile(
                fullName,
                username,
                email,
                age,
                languages,
                github,
                twitter,
                linkedin
        );
        mprofileDatabaseReference.push().setValue(profile);


    }

}
