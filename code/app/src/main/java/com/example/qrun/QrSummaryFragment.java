package com.example.qrun;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qrun.databinding.QrSummaryFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Depreciated, unused in main
public class QrSummaryFragment extends Fragment {

    private QrSummaryFragmentBinding binding;

    private FirebaseFirestore QrRun;
    private QRStorage QrStore;
    private ArrayAdapter<String> sharedQrAdapter;
    private ArrayAdapter<String> commentAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        QrStore = new QRStorage(QrRun);

        binding = QrSummaryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String hexString = "";
        ArrayList<String> sharedUsers = new ArrayList<String>();
        QrRun.collection("QR").whereEqualTo("hexString", hexString ).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()){
                                if(document.exists()){
                                    String user = (String) document.getData().get("username");
                                    sharedUsers.add(user);
                                } else {
                                    Log.d("Storage get()", "No such document");
                                }
                                }
                        }else {
                            Log.d("Storage get()", "get failed with", task.getException());
                        }
                    }
                });

            ArrayList commentList = new ArrayList<String>();

            sharedQrAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_layout, sharedUsers);
            commentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_layout, commentList);

            binding.sharedQrList.setAdapter(sharedQrAdapter);
            binding.commentList.setAdapter(commentAdapter);

           binding.addComment.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   // add comment fragment/activity
               }
           });

           binding.deleteQr.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //add delete stuff here
               }
           });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}