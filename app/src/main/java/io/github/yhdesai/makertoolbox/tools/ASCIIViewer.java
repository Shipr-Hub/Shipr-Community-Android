package io.github.yhdesai.makertoolbox.tools;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.yhdesai.makertoolbox.R;

public class ASCIIViewer extends AppCompatActivity {

    private Button analyse, stats, clear, paste;
    private EditText intro;
    private ListView list;
    private Context c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_asciiviewer);
        c = this;

        analyse = findViewById(R.id.ascii_analyse);
        stats = findViewById(R.id.ascii_stats);
        clear = findViewById(R.id.ascii_clear);
        paste = findViewById(R.id.ascii_paste);
        list = findViewById(R.id.ascii_list);
        intro = findViewById(R.id.trad_intro);

        listen();
    }

    private void listen() {
        analyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = intro.getText().toString();
                if (!text.isEmpty()) {

                    List<String> l = new ArrayList<>();

                    for (int i = 0; i < text.length(); i++) {
                        l.add(">" + text.charAt(i) + "<" + " \t " + text.codePointAt(i));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, android.R.id.text1, l);
                    list.setAdapter(adapter);
                } else {
                    Toast.makeText(c, "Introduce some characters :p", Toast.LENGTH_SHORT).show();
                }


            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intro.setText("");
            }
        });

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String texto = manager.getPrimaryClip().getItemAt(0).getText().toString();
                intro.setText(texto);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = intro.getText().toString();
                SparseIntArray characters = new SparseIntArray();

                for (int i = 0; i < text.length(); i++) {
                    int characNUM = text.charAt(i);
                    if (characters.get(characNUM, -1) == -1) {
                        characters.append(characNUM, 1);
                    } else {
                        int cuenta = characters.get(characNUM, 0);
                        cuenta++;
                        characters.delete(characNUM);
                        characters.append(characNUM, cuenta);
                    }
                }

                String msg = "";
                for (int i = 0; i < characters.size(); i++)
                    msg += (char) characters.keyAt(i) + " (" + characters.keyAt(i) + ") -> " + characters.valueAt(i) + "\n";
                msg = msg.replace("0/0\n", "");


                new AlertDialog.Builder(c)
                        .setTitle("Character stats")
                        .setMessage(msg)
                        .create()
                        .show();
            }
        });
    }
}
