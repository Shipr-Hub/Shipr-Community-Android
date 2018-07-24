package io.github.yhdesai.makertoolbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.kbiakov.codeview.CodeView;

public class CodeEditor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_editor);
        CodeView codeView = (CodeView) findViewById(R.id.code_view);
        codeView.setCode(getString(R.string.listing_js), "js");
    }
}
