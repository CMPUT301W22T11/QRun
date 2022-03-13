package com.example.qrun_deletingplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DeletingPlayer extends AppCompatActivity {
    EditText name;
    Button confirm_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleting_player);
        name = (EditText) findViewById(R.id.editTextTextPersonName);
        confirm_delete=(Button)findViewById(R.id.button);

        confirm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name= name.getText().toString();
                Storage.delete(user_name);
            }
        });
    }
}