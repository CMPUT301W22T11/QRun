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

import javax.annotation.Nullable;

public class ImagePopup extends AppCompatDialogFragment {

    private ImagePopup.OnFragmentInteractionListener listener;

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

        if (context instanceof ImagePopup.OnFragmentInteractionListener) {
            listener = (ImagePopup.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " OnFragmentInteractionListener not implemented");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.image_popup, null);

        String img = getArguments().getString("image");

        imageView = view.findViewById(R.id.image_view);

        imageView.setImageBitmap(stringToBitmap(img));

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
