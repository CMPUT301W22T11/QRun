package com.example.qrun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Formatter;

/**
 * this class is to customize the sorted qr list
 * @author lucheng
 */
public class CustomSortedQr extends ArrayAdapter<QRDist> {
    private ArrayList<QRDist> qrs;
    private Context context;
    public CustomSortedQr(@NonNull Context context, ArrayList<QRDist> qrs) {
        super(context,0,qrs);
        this.qrs = qrs;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.sorted_qr_list,parent,false);
        }
        QRDist qrDist = qrs.get(position);
        TextView qScore = view.findViewById(R.id.score_text);
        TextView distance = view.findViewById(R.id.distance_text);
        Formatter formatter = new Formatter();//format distance into 2 digits
        formatter.format("%.2f", qrDist.getDistance());

        qScore.setText("Points: " + String.valueOf(qrDist.getPoint()));
        distance.setText("Dist: " + String.valueOf(formatter)+"m");

        return view;
    }
}
