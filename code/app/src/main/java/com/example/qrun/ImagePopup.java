package com.example.qrun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.annotation.Nullable;

public class ImagePopup extends AppCompatDialogFragment {

    private OnFragmentInteractionListener listener;

    private ImageView imageView;

    public interface OnFragmentInteractionListener {
        void onOkPressed();

        void onDiscard();
    }

    public static ImagePopup newInstance(QRGame qr) {

        Bundle args = new Bundle();

        args.putSerializable("QR", qr);

        ImagePopup popup = new ImagePopup();
        popup.setArguments(args);
        return popup;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " OnFragmentInteractionListener not implemented");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.image_popup, null);

        QRGame qr = (QRGame) getArguments().getSerializable("QR");
        final long ONE_MEGABYTE = 1024 * 1024;
        imageView = view.findViewById(R.id.image_view);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child(qr.getPath());
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener((bytes) -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
            }
        );
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Image")
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDiscard();
                    }
                })
                .setPositiveButton("Show QR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences prefs =  getContext().getSharedPreferences(
                                "com.example.app", Context.MODE_PRIVATE); // Get the Shared preferences
                        String usrName = prefs.getString("usrName", null);
                        Intent intent = new Intent(getContext(), QrSummary.class);
                        intent.putExtra("hexString", qr.getHexString());
                        intent.putExtra("username", usrName);
                        startActivity(intent);
                        //listener.onOkPressed();
                    }
                }).create();
    }


    public Bitmap stringToBitmap(String string){
        //String type in database converted to Bitmap
        Bitmap bitmap;

        if(string!=null){
            byte[] bytes= Base64.decode(string,Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return bitmap;
        }
        else {
            return null;
        }
    }
}
