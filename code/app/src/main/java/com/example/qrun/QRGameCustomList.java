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


/**
 * This is to display the QR Game single content when it is displayed in list
 */
public class QRGameCustomList extends ArrayAdapter<QRGame> {

    private ArrayList<QRGame> QRGameList;
    private Context context;

    /**
     * Initialize Custom List for QR
     * @param context
     * @param lists
     */
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

        TextView qrHash = view.findViewById(R.id.rank);
        TextView qrPoints = view.findViewById(R.id.usernameContent);

        qrHash.setText(qrGame.getHexString());
        qrPoints.setText(String.valueOf(qrGame.getPoints()));
        return view;

    }
}
