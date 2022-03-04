package com.example.qrun;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements SignupFragment.OnFragmentInteractionListener {
    private Button Signup, Login;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Signup = findViewById(R.id.signup_button);
        Login = findViewById(R.id.login_button);
        Signup.setOnClickListener((l) -> {
            new SignupFragment().show(getSupportFragmentManager(), "Signup");
        });
    }

    @Override
    public void onOkPressed(String name) {
        if(name != null) {
            
            Log.d("onOkPressed", "User Id login" + name);
        }
    }
}