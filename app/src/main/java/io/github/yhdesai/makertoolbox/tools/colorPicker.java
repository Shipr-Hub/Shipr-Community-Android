package io.github.yhdesai.makertoolbox.tools;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import io.github.yhdesai.makertoolbox.R;

public class colorPicker extends AppCompatActivity {
    private int defaultColorR = 0;
    private int defaultColorG = 0;
    private int defaultColorB = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        //getActionBar().hide();
        getSupportActionBar().hide();
        colorPickerDialog(null);

    }

    private void colorPickerDialog(View v) {
        final ColorPicker cp = new ColorPicker(colorPicker.this, defaultColorR, defaultColorG, defaultColorB);
        /* Show color picker dialog */
        cp.show();
        // cp.enableAutoClose(); // Enables auto-dismiss for the dialog

        /* Set a new Listener called when user click "select" */
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(@ColorInt int color) {
                //Set color of background view
                View view = findViewById(R.id.colorPickerOutput);
                view.setBackgroundColor(color);
		    
		defaultColorR = Color.red(color);
                defaultColorG = Color.green(color);
                defaultColorB = Color.blue(color);


                //Set the center text color based on the background color
		    TextView colorOutput1 = findViewById(R.id.colorOutput1);
				if ((defaultColorR+defaultColorG+defaultColorB) > 700) {
					colorOutput1.setText(String.format("#%06X", (0)));
				} else if ((defaultColorR+defaultColorG+defaultColorB) < 700) {
					colorOutput1.setText(String.format("#%06X", (0xFFFFFF & color)));
				}
                //TextView colorOutput2 = findViewById(R.id.colorOutput2);
                //colorOutput2.setText(String.format("#%08X", (0xFFFFFFFF & color)));
              


                // Log the data returned
               /* Log.d("Alpha", Integer.toString(Color.alpha(color)));
                Log.d("Red", Integer.toString(Color.red(color)));
                Log.d("Green", Integer.toString(Color.green(color)));
                Log.d("Blue", Integer.toString(Color.blue(color)));

                Log.d("Pure Hex", Integer.toHexString(color));
                Log.d("#Hex no alpha", String.format("#%06X", (0xFFFFFF & color)));
                Log.d("#Hex with alpha", String.format("#%08X", (0xFFFFFFFF & color)));*/

                // If the auto-dismiss option is not enable (disabled as default) you have to manually dimiss the dialog
                cp.dismiss();
            }
        });

    }
}
