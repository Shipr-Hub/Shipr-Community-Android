package io.github.yhdesai.makertoolbox;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.github.yhdesai.makertoolbox.ssh.SshActivity;
import io.github.yhdesai.makertoolbox.tools.NetActivity;
import io.github.yhdesai.makertoolbox.tools.colorPicker;
import io.github.yhdesai.makertoolbox.tools.todo;

public class ToolsList extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tools_list, container, false);
        Button color = view.findViewById(R.id.tools_color_button);
        Button todo = view.findViewById(R.id.tools_todo_button);
        Button ssh = view.findViewById(R.id.tools_ssh_button);
        Button net = view.findViewById(R.id.tools_network_button);

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), colorPicker.class));
            }
        });

        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager help = getFragmentManager();
                help.beginTransaction().replace(R.id.content_frame, new todo()).commit();
            }
        });

        ssh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SshActivity.class));

            }
        });

        net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NetActivity.class));
            }
        });
        return view;
    }

  /* private void chat() {
        FragmentManager frag = getFragmentManager();
        frag.beginTransaction().replace(R.id.content_frame, new general()).commit();


    }*/

    /*
      public void chat(View view) {
          startActivity(new Intent(MainActivity.this, chat.class));
      }
  */
    public void todo() {
        FragmentManager help = getFragmentManager();
        help.beginTransaction().replace(R.id.content_frame, new todo()).commit();
    }
/*
    public void dev(View view) {
        startActivity(new Intent(MainActivity.this, Dev.class));
    }
*/


    public void color(View view) {
        startActivity(new Intent(getActivity(), colorPicker.class));
    }

}

