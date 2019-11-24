package com.ziad.anda1;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public  class PdfListAdapter extends RecyclerView.Adapter<PdfListAdapter.MyViewHolder> {
    private PdfListActivity context;
    private List<PaymentList> getVoucher;
    View itemView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // CheckBox c1;
        public TextView txtDate,txtLabIDShow, txtAmount,txtStatus,id,txtExpire,txtProduct;
        public TextView txtCreatedDate,txtTokenNuber;
        public ImageView imgRemove,imgBill;
        Button btnMinus,btnPlus;
        int amount=0;
        //public Button buttonInc,buttonDec,orderNow;
        public MyViewHolder(View view) {
            super(view);
            this.txtCreatedDate =  view.findViewById(R.id.txtCreatedDate);
            this.txtTokenNuber =  view.findViewById(R.id.txtTokenNuber);
//            txtDate = view.findViewById(R.id.txtDate);
//            txtAmount = view.findViewById(R.id.txtAmount);
//            txtStatus = view.findViewById(R.id.txtStatus);
        }
    }

    public PdfListAdapter(PdfListActivity context, List<PaymentList> getVoucher) {
        this.context = context;
        this.getVoucher = getVoucher;
    }


    @NonNull
    @Override
    public PdfListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_layout, parent, false);
        return new PdfListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PdfListAdapter.MyViewHolder holder, final int position) {
        final PaymentList paymentList = getVoucher.get(position);

//        holder.txtCreatedDate.setText("Date: "+paymentList.getEntryDate());
        holder.txtTokenNuber.setText("Name:  "+paymentList.getTokenId());
        Toast.makeText(context,paymentList.getTokenId(),Toast.LENGTH_LONG).show();
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy    hh:mm a");

        Date date = new Date(getVoucher.get(position).getEntryDate());
        try {
            holder.txtCreatedDate.setText("Date: "+ outputFormat.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File imageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Image_to_Pdf_assignment/" +paymentList.getTokenId() );
                try{
                    if (imageFile.exists()) {
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri apkURI = FileProvider.getUriForFile(
                                context,
                                context.getApplicationContext()
                                        .getPackageName() + ".provider", imageFile);
                        install.setDataAndType(apkURI, "application/pdf");
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(install);
                    }else{
                        Uri uri = Uri.parse(paymentList.getmImage_path()); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("error opening pdf ",e.getMessage());
                }
                Toast.makeText(context,"file path "+imageFile.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"Opening Your Document",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return getVoucher.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

}

