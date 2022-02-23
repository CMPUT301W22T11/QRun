package com.example.qrun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import javax.annotation.Nullable;

public class AddQRPopup extends AppCompatDialogFragment {

    private static String QRCODE = "com.example.qrun.QRCODE";

    private TextView pointsView;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onOkPressed(QR qr);
        void onDiscard();
    }

    public static AddQRPopup newInstance(QR qr){
        Bundle args = new Bundle();

        args.putSerializable(QRCODE, qr);
        AddQRPopup popup = new AddQRPopup();
        popup.setArguments(args);
        return popup;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof  OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + " OnFragmentInteractionListener not implemented");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.points_popup, null);

        pointsView = view.findViewById(R.id.points_view);

        QR qr = (QR) getArguments().get(QRCODE);

        pointsView.setText("This QR code gives you " + String.valueOf(qr.getPoints()) + " points!!");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Add QR")
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDiscard();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onOkPressed(qr);
                    }
                }).create();

    }

}
