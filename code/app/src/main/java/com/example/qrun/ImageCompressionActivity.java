package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class ImageCompressionActivity extends AppCompatActivity {
    SeekBar seekBar;
    TextView seekBarTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_compression);
        seekBar = (SeekBar) findViewById(R.id.seekBarComp);
        seekBarTV = (TextView) findViewById(R.id.seekBarValTV);
        seekBar.setMax(100);
        seekBar.setMin(0);
        seekBarTV.setText(String.valueOf(0));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarTV.setText(String.valueOf(i));
                CompressionValStore compressionValue = CompressionValStore.getInstance();
                compressionValue.setValue(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void returnHome(View view){ // remove this code
        String name = "test";
        Intent intent = new Intent(this, MainScreen.class);
        intent.putExtra("userName",name);
        startActivity(intent);
    }
}