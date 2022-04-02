package com.example.qrun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

    public static ImagePopup newInstance(String image) {

        Bundle args = new Bundle();

        args.putString("image", image);

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

        String img = getArguments().getString("image");
        final long ONE_MEGABYTE = 1024 * 1024;
        imageView = view.findViewById(R.id.image_view);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child(img);
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
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        listener.onOkPressed();
                    }
                }).create();
    }
}
