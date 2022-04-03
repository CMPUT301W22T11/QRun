package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class ImageCompressionActivity extends AppCompatActivity {
    SeekBar seekBar;
    TextView seekBarTV;
    String userName;
    UserStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_compression);
        seekBar = (SeekBar) findViewById(R.id.seekBarComp);
        seekBarTV = (TextView) findViewById(R.id.seekBarValTV);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userName = extras.getString("userName");
        }
        seekBar.setMax(100);
        seekBar.setMin(0);
        storage = new UserStorage(FirebaseFirestore.getInstance());
        storage.get(userName, (data) -> {
            if(data != null) {
                long compression = (long)data.get("Compression");
                seekBar.setProgress((int)(compression));
                seekBarTV.setText(String.valueOf(compression));
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarTV.setText(String.valueOf(i));
                storage.get(userName, (data) -> {
                    if(data != null) {
                        data.put("Compression", Long.valueOf(i));
                        storage.update(userName, data, comp ->{
                            if(comp) Log.d("update Compression()", "Succesfully");
                            else Log.d("update Compression()", "Failed");
                        });
                    }
                });
//                CompressionValStore compressionValue = CompressionValStore.getInstance();
//                compressionValue.setValue(i);
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