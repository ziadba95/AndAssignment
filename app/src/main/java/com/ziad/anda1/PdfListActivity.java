package com.ziad.anda1;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PdfListActivity extends AppCompatActivity {
    RecyclerView listPaymentHistory;
    Context context;
    PdfListAdapter historyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        context=PdfListActivity.this;
        listPaymentHistory=findViewById(R.id.listPaymentHistory);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        listPaymentHistory.setLayoutManager(mLayoutManager);
        getHistoryList();
    }

    public void getHistoryList(){
        List<PaymentList> paymentLists=new ArrayList<PaymentList>();

        try {
            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Image_to_Pdf_assignment/";
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles();
            File file;
            for (int i=0;i<files.length;i++) {
                PaymentList paymentList=new PaymentList();
                file = files[i];
                Date lastModDate = new Date(file.lastModified());
                paymentList.setEntryDate(lastModDate.toString());
                paymentList.setTokenId(file.getName());
                paymentLists.add(paymentList);
                historyListAdapter=new PdfListAdapter(PdfListActivity.this,paymentLists);
                listPaymentHistory.setAdapter(historyListAdapter);
                Log.e("file name",file.toString());
                Log.i("File last modified @ : ", lastModDate.toString());
                Toast.makeText(context,i+"",Toast.LENGTH_SHORT).show();
            }

//            Toast.makeText(getApplicationContext(), " Data Send to server Successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("error ",e.getMessage());
        }


    }


}
