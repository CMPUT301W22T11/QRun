package com.example.qrun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QRGameCustomList extends ArrayAdapter<QRGame> {

    private ArrayList<QRGame> QRGameList;
    private Context context;

    public QRGameCustomList(Context context, ArrayList<QRGame> lists){
        super(context,0, lists);
        this.QRGameList = lists;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.qr_list_content, parent,false);
        }

        QRGame qrGame = QRGameList.get(position);

        TextView qrHash = view.findViewById(R.id.qrhash);
        TextView qrPoints = view.findViewById(R.id.qrpoint);

        qrHash.setText(qrGame.getHexString());
        qrPoints.setText(String.valueOf(qrGame.getPoints()));
        return view;

    }
}
