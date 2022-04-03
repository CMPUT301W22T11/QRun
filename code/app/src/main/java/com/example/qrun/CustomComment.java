
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
 * This class is to display a single content
 */
public class CustomComment extends ArrayAdapter<Comment> {

    private ArrayList<Comment> comments;
    private Context context;


    /**
     * Constructor for custom comment
     * @param context the context in which this list is located
     * @param comments the comment references
     */

    public CustomComment(@NonNull Context context, ArrayList<Comment> comments) {
        super(context,0,comments);
        this.comments = comments;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.comment_list_content_view,parent,false);
        }

        Comment comment = comments.get(position);

        TextView userId = view.findViewById(R.id.uid_text);
        TextView commentText = view.findViewById(R.id.comment_text);

        userId.setText(comment.getUid());

        commentText.setText(comment.getComment());

        return view;
    }
}
