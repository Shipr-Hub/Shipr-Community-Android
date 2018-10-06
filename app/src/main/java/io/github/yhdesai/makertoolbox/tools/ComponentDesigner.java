package io.github.yhdesai.makertoolbox.tools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.github.yhdesai.makertoolbox.R;
import io.github.yhdesai.makertoolbox.tools.xml_designer.ButtonDesigner;

public class ComponentDesigner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_designer);
    }

    public void xml_designer_button(View view) {
        startActivity(new Intent(ComponentDesigner.this, ButtonDesigner.class));
    }

}
