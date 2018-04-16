package io.github.yhdesai.makertoolbox;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ToDoAdapter extends ArrayAdapter<DeveloperToDo> {
    public ToDoAdapter(Context context, int resource, List<DeveloperToDo> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_todo, parent, false);
        }
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView descTextView = convertView.findViewById(R.id.descTextView);
        TextView featureTextView = convertView.findViewById(R.id.feature);
        TextView bugTextView = convertView.findViewById(R.id.bug);

        DeveloperToDo todo = getItem(position);

        titleTextView.setText(todo.gettName());
        descTextView.setText(todo.gettDesc());
        Boolean isFeature = todo.getFeature();
        if (isFeature = true) {
            featureTextView.setVisibility(TextView.VISIBLE);
        } else {
            featureTextView.setVisibility(TextView.INVISIBLE);
        }

        Boolean isBug = todo.getBug();
        if (isBug = true) {
            bugTextView.setVisibility(TextView.VISIBLE);
        } else {
            bugTextView.setVisibility(TextView.INVISIBLE);
        }


        return convertView;
    }
}