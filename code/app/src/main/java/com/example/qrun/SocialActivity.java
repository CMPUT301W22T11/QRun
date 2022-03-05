package com.example.qrun;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.qrun.Fragment.UserQrFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SocialActivity extends AppCompatActivity implements UserQrFragment.OnFragmentInteractionListener{
    FirebaseFirestore db;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String uid = user.getUid();//use user email as reference for now
    Button showUqrButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        db = FirebaseFirestore.getInstance();
        showUqrButton = findViewById(R.id.show_profile_qr_Button);
        showUqrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment showQr = new UserQrFragment();
                Bundle currentUser = new Bundle();
                currentUser.putString("currentUser",uid);
                showQr.setArguments(currentUser);//send current qid into addCommentFragment
                showQr.show(getSupportFragmentManager(),"Uid Qr");
            }
        });
    }

    @Override
    public void onFinishPressed() {

    }
}
