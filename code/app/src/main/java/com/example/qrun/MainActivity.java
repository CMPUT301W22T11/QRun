package com.example.qrun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements SignupFragment.OnFragmentInteractionListener
{
    private ActivityResultLauncher<Intent> ac = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is 1, then update the list
                    if(result.getResultCode() == 1) {
                        Intent intent = result.getData();
                        String username = (String) intent.getSerializableExtra("QR");
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        UserStorage store = new UserStorage(db);
                        store.get(username, (check) -> {
                           if(check != null) {
                               onOkPressed(username);
                           }
                        });
                    }
                }
            }
    );

    private Button Signup, Login;
    private Context ctx;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Signup = findViewById(R.id.signup_button);
        Login = findViewById(R.id.login_button);
        ctx = this;
        Signup.setOnClickListener((l) -> {
            new SignupFragment().show(getSupportFragmentManager(), "Signup");
        });
        Login.setOnClickListener((l) -> {
            Intent intent = new Intent(ctx, ScanningActivity.class);
            ac.launch(intent);
        });
    }
    public void onOkPressed(String name) {
        if(name != null) {
            Log.d("Signup", "User Signed up: " + name);
        }
    }
}