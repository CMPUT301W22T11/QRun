package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.qrun.Fragment.DeleteQRFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Deleting QRs if clicked if an owner want to delete one of those.
public class QRDeleteActivity extends AppCompatActivity {
    private ListView QRListView;

    private ArrayAdapter<String> QRAdapter;

    private ArrayList<String> QRListNames=new ArrayList<String>();

    private ArrayList<QRGame> QRList=new ArrayList<>();
    private QRStorage QRstorage;

    private String QRName;

    private Storage.StoreOnComplete comp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qrdelete);
        EditText SearchQR=findViewById(R.id.editTextTextPersonName);
        Button deleteBut=findViewById(R.id.button2);
        QRListView=findViewById(R.id.QRListView);
        Button searchBut=findViewById(R.id.SearchBut);
        QRAdapter=new ArrayAdapter<String>(this,R.layout.qr_lists_content,QRListNames);

        QRListView.setAdapter(QRAdapter);

        QRstorage = new QRStorage(FirebaseFirestore.getInstance());

        QRstorage.getCol().addSnapshotListener((queryDocumentSnapshots, error) ->
        {
            getData(queryDocumentSnapshots);
        });

        //here it is the listView of QRs and if clicked, the QR will delete
        QRListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QRListNames.remove(i);
                QRAdapter.notifyDataSetChanged();
                QRGame temp=QRList.get(i);
                QRstorage.delete(temp,comp);
                QRList.remove(i);
            }
        });

    }
    //get the data from database
    private void getData(QuerySnapshot queryDocumentSnapshots) {

        QRListNames.clear();

        if(queryDocumentSnapshots != null) {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                if(document.getId() != QRName) {
                    QRListNames.add(new QRGame(document).getHexString());

                    QRList.add(new QRGame(document));

                }

            }

//             remove those QRs with same hexString
            for(int i=0;i<QRListNames.size();i++){
                String key=QRListNames.get(i);
                int j=i+1;
                for(;j<QRListNames.size();j++) {
                    String temp=QRListNames.get(j);
                    if (temp.equals(key)){
                        QRListNames.remove(j);
                        QRList.remove(j);
                    }
                }
            }

            QRAdapter.notifyDataSetChanged();

        }

    }

}