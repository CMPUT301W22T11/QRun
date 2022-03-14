package com.example.qrun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

/**
 * The Screen Before Login/Signup
 */
public class MainActivity extends AppCompatActivity implements SignupFragment.OnFragmentInteractionListener
{
    /**
     * Login based on its username
     * @param username
     */
    private void login(@NonNull String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserStorage store = new UserStorage(db);
        store.get(username, (check) -> {
            if(check != null) {
                onOkPressed(username);
            }
        });
    }
    private ActivityResultLauncher<Intent> ac = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is 1, then update the list
                    if(result.getResultCode() == 1) {
                        Intent intent = result.getData();
                        String username = (String) intent.getSerializableExtra("QR");
                        login(username);
                    }
                }
            }
    );

    private Button Signup, Login;
    private Context ctx;
    private SharedPreferences prefs;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Signup = findViewById(R.id.signup_button);
        Login = findViewById(R.id.login_button);
        ctx = this;
        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE); // Get the Shared preferences
        String usrName = prefs.getString("usrName", null);
        if(usrName != null) {
            login(usrName);
        }
        Signup.setOnClickListener((l) -> {
            new SignupFragment().show(getSupportFragmentManager(), "Signup");
        });
        Login.setOnClickListener((l) -> {
            Intent intent = new Intent(ctx, ScanningActivity.class);
            ac.launch(intent);
        });
    }

    /**
     * this will show about if name is good or not
     * @param name
     */
    @Override
    public void onOkPressed(String name) {
        if(name != null) {
            Log.d("Signup", "User Signed up: " + name);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("usrName", name);
            editor.commit();
            Toast.makeText(MainActivity.this, "Login/Signup Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainScreen.class);
            intent.putExtra("userName",name);
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this, "Unable to identify user", Toast.LENGTH_SHORT).show();
        }

    }
}