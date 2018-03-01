package io.github.yhdesai.devprops.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.github.yhdesai.devprops.R;
import io.github.yhdesai.devprops.hackclub;


public class workshop extends Fragment   {




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workshop, container, false);
    return rootView;}


    EditText codeEdit = (EditText) getActivity().findViewById(R.id.codeEdit);

    public void code(View view){
        String code = codeEdit.getText().toString();
        if (code.equals("hack club")){
            Intent hack = new Intent(getActivity(), hackclub.class);

        }
    }

    public void form (View view){
        String url = "https://goo.gl/forms/KTIHsTM7efZyv88p1";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}