package com.example.qrun.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrun.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class UserQrFragment extends DialogFragment {
    private ImageView QrIv;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    private OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener {
        void onFinishPressed();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserQrFragment.OnFragmentInteractionListener){
            listener = (UserQrFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.user_qr_fragment,null);
        QrIv = view.findViewById(R.id.idIVQrcode);
        String uid;
        Bundle currentUid = getArguments();
        uid = currentUid.getString("currentUser");


        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(uid, null, QRGContents.Type.TEXT,1000);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            QrIv.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("ProfileQr")
                .setNegativeButton("Finish", null)
                .create();
    }
}
