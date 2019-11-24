package com.ziad.anda1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import static com.ziad.anda1.MainActivity.imagesUriArrayList;
import static com.ziad.anda1.MainActivity.mImagesUri;


public  class listImagesHorizontal extends RecyclerView.Adapter<listImagesHorizontal.MyViewHolder> {
    private Context context;
    private List<Uri> getVoucher;
    View itemView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // CheckBox c1;
        public TextView txtDate,txtLabIDShow, txtAmount,txtStatus,id,txtExpire,txtProduct;
        public ImageView imgRemove,imgBill;
        Button btnMinus,btnPlus;
        int amount=0;
        //public Button buttonInc,buttonDec,orderNow;
        public MyViewHolder(View view) {
            super(view);
            imgBill = view.findViewById(R.id.imgBill);
            imgRemove = view.findViewById(R.id.imgRemove);
//            txtDate = view.findViewById(R.id.txtDate);
//            txtAmount = view.findViewById(R.id.txtAmount);
//            txtStatus = view.findViewById(R.id.txtStatus);
        }
    }

    public listImagesHorizontal(Context context, List<Uri> getVoucher) {
        this.context = context;
        this.getVoucher = getVoucher;
    }


    @NonNull
    @Override
    public listImagesHorizontal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_listbill, parent, false);
        return new listImagesHorizontal.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final listImagesHorizontal.MyViewHolder holder, final int position) {
        final Uri uri = getVoucher.get(position);
        File path=null ;
        try {
             path = new File(FilePath.getPath(context, uri));
        }catch (Exception e){
            path = new File(getRealPathFromURI(uri, context));

        }



        if (path != null && !path.equals("")) {
            holder.imgBill.setVisibility(View.VISIBLE);
            try {
                Picasso.with(context)
                        .load(path) // Add this
                         .config(Bitmap.Config.RGB_565)
                        .fit().centerCrop()
                        .into(holder.imgBill);
            } catch (Exception e) {
                Bitmap myBitmap = BitmapFactory.decodeFile(path.getAbsolutePath());
                holder.imgBill.setImageBitmap(myBitmap);
            }
        }
//        imgFullSize.setImageURI(uri);

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Are you sure you want to delete this Image ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                imagesUriArrayList.remove(position);
                                mImagesUri.remove(position);
                                notifyDataSetChanged();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rel_layout_Image.setVisibility(View.VISIBLE);
//                try {
//                    Picasso.with(context)
//                            .load(new File(FilePath.getPath(context, getVoucher.get(position)))) // Add this
//                            .config(Bitmap.Config.RGB_565)
////                            .fit().centerCrop()
//                            .into(imgFullSize);
//
//                    imgFullSize.setImageURI(uri);
//                }catch (Exception e){
//                    imgFullSize.setImageURI(uri);
//                }
//            }
//        });

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

    public String getRealPathFromURI(Uri uri, Context context) {
        String path="";
        if (context.getContentResolver() != null) {
            try {

            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
            }catch (Exception e){}
        }

        return path;
    }


}

