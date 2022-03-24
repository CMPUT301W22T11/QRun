package com.example.qrun_deletingplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
                delete(user_name);
            }
        });
    }
    public void delete(String user_name){
        FirebaseFirestore docRef = FirebaseFirestore.getInstance();
        docRef.collection("User").document(user_name)
                .delete().
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("player delete()", " player delete successfully ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("player delete()", "delete failed", e);
                    }
                });
    }
}