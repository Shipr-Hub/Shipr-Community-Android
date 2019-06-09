package tech.shipr.socialdev;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button editBtn = view.findViewById(R.id.editBtn);
        final ImageView image = view.findViewById(R.id.user_profile_photo);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileFragment.this.getContext(), EditProfile.class);
                Pair[] pair = new Pair[1];
                pair[0] = new Pair<View,String>(image,"userImage");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProfileFragment.this.getActivity(),
                        pair);
                ProfileFragment.this.getActivity().startActivity(i,options.toBundle());
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
