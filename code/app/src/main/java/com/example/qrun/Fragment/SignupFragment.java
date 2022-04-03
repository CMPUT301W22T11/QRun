package com.example.qrun.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrun.R;
import com.example.qrun.UserStorage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


/**
 * This is the Signup Fragment for the Users
 */
public class SignupFragment extends DialogFragment {
    private EditText nameText;
    private EditText usernameText;
    private EditText emailText;
    private EditText phoneText;
    private OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener {
        void onOkPressed(String name);
    }

    /**
     * attach for its context
     * @param context
     */
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener)context;
        }
        else throw new RuntimeException(context.toString() + " must implemented");
    }

    /**
     * Create the Dialog
     * @param savedInstanceState
     * @return
     */
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.signupfragmentlayout, null);
        nameText = view.findViewById(R.id.name);
        usernameText = view.findViewById(R.id.username);
        emailText = view.findViewById(R.id.email);
        phoneText = view.findViewById(R.id.phone);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("Sign Up").setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = nameText.getText().toString();
                        String username = usernameText.getText().toString();
                        String email = emailText.getText().toString();
                        String phone = phoneText.getText().toString();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        UserStorage store = new UserStorage(db);
                        CollectionReference col = store.getCol();
                        store.get(username, (check) -> {
                            if(check == null) {
                                HashMap<String, String> preData = new HashMap<>();
                                preData.put("name", name);
                                preData.put("email", email);
                                preData.put("phone", phone);
                                store.add(username, preData, (boolean isTrue) -> {
                                    if(isTrue) {
                                        listener.onOkPressed(username);
                                    }
                                    else {
                                        listener.onOkPressed(null);
                                    }
                                });
                            }
                            else {
                                listener.onOkPressed(null);
                            }
                        });

                    }
                }).create();
    }
}
