package io.github.yhdesai.devprops;

/**
 * Created by yash on 26/2/18.
 */

        import android.app.Activity;
        import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;


        import org.w3c.dom.Text;

        import java.util.List;

public class MessageAdapter extends ArrayAdapter<DeveloperMessage> {
    public MessageAdapter(Context context, int resource, List<DeveloperMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

       // ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);

        DeveloperMessage message = getItem(position);


        messageTextView.setVisibility(View.VISIBLE);
    //    photoImageView.setVisibility(View.GONE);
        messageTextView.setText(message.getText());

        authorTextView.setText(message.getName());
        dateTextView.setText(message.getTime());

        return convertView;
    }
}