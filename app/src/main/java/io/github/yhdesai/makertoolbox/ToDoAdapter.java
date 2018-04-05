package io.github.yhdesai.makertoolbox;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        DeveloperToDo todo = getItem(position);

        titleTextView.setText(todo.gettName());
        descTextView.setText(todo.gettDesc());

        return convertView;
    }
}