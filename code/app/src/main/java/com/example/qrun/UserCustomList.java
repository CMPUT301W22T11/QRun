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
 * Custom User Content to display
 */
public class UserCustomList extends ArrayAdapter<User> {

    private ArrayList<User> UserGameList;
    private Context context;

    /**
     * Initialize the User custom List
     * @param context The Activity Context that this class is located
     * @param lists the user lists references
     */
    public UserCustomList(Context context, ArrayList<User> lists){
        super(context,0, lists);
        this.UserGameList = lists;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.qr_list_content, parent,false);
        }

        User user = UserGameList.get(position);

        TextView qrHash = view.findViewById(R.id.qrhash);
        TextView qrPoints = view.findViewById(R.id.qrpoint);

        qrHash.setText(user.getUsername());
        qrPoints.setText("");
        return view;

    }
}
