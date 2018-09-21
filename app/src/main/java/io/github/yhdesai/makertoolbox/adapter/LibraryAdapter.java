package io.github.yhdesai.makertoolbox.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.yhdesai.makertoolbox.R;
import io.github.yhdesai.makertoolbox.model.DeveloperAwesomeLibraries;

public class LibraryAdapter extends ArrayAdapter<DeveloperAwesomeLibraries> {
    public LibraryAdapter(Context context, int resource, List<DeveloperAwesomeLibraries> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_library, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView subTitleTextView = convertView.findViewById(R.id.subTitleTextView);
        TextView photoUrlTextView = convertView.findViewById(R.id.photoURLTextView);
        TextView wikiTextView = convertView.findViewById(R.id.wikiTextView);
        TextView docsTextView = convertView.findViewById(R.id.docsTextView);
        TextView gitTextView = convertView.findViewById(R.id.gitTextView);

        DeveloperAwesomeLibraries library = getItem(position);

        titleTextView.setText(library.getTitle());
        subTitleTextView.setText(library.getSubtitle());
        photoUrlTextView.setText(library.getPhotoUrl());
        wikiTextView.setText(library.getWiki());
        docsTextView.setText(library.getDocs());
        gitTextView.setText(library.getGit());


        return convertView;
    }
}