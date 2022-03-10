package com.example.qrun;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qrun.databinding.QrSummaryFragmentBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class QrSummaryFragment extends Fragment {

    private QrSummaryFragmentBinding binding;

    FirebaseFirestore QrRun;
    Storage QrStore;
    ArrayAdapter<String> sharedQrAdapter;
    ArrayAdapter<String> commentAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        QrStore = new Storage(QrRun, "QR");

        binding = QrSummaryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            ArrayList list = new ArrayList<String>();
            sharedQrAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_layout, list);
            commentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_layout, list);
            binding.sharedQrList.setAdapter(sharedQrAdapter);
            binding.commentList.setAdapter(commentAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}