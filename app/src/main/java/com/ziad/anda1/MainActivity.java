package com.ziad.anda1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{


    private AppBarConfiguration mAppBarConfiguration;
    TextView txtToken;
    Button btnGenerateNew;
    String imagePath;
//    ProgressRequestBody fileBody;
    Document document;
    RelativeLayout rel;
    Button btnOk,btnClose,btnHistory,btnCameraImages,btn_submit,btnGallaryImages;
    Context context;
    Uri imageUri;
    int CAMERA_REQUEST=1,GALLARY_REQUEST=2;
    public  static List<Uri> imagesUriArrayList = new ArrayList<Uri>();
    public  static  List<String> imagesArrayListString = new ArrayList<String>();
    listImagesHorizontal customAdapter;
    RecyclerView listImages;
//    public static com.github.chrisbanes.photoview.PhotoView imgFullSize;
    TextView txtPercentage,txtCamera;
    EditText txtTokenNuber;
    String pdfName="";
    public static RelativeLayout rel_layout_Success,rel_layout_Image,rel_layout_Progress;
    String insertedID="0",currentDate="",tokenId="";
//    public  static List<MultipartBody.Part> partList=new ArrayList<MultipartBody.Part>();
    File imageFile;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static ArrayList<String> mImagesUri = new ArrayList<>();
    public static final String IMAGE_SCALE_TYPE_ASPECT_RATIO = "maintain_aspect_ratio";

    //    ImageToPDFOptions mPdfOptions;
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA,READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE,CALL_PHONE,READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);

    }
    ProgressBar progressBar;
    double file_size;
    ImageView imgFullSize;
    EditText editEmail;
    Button btnSubmit;
    //    Button btnViewPDF;
    String path = "";
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);



        btnCameraImages=findViewById(R.id.btnCameraImages);
        listImages=findViewById(R.id.listImages);
        btnGallaryImages=findViewById(R.id.btnGallaryImages);
        btn_submit=findViewById(R.id.btn_submit);
        imgFullSize=findViewById(R.id.imgFullSize);
        txtCamera=findViewById(R.id.txtCamera);
        btnOk=findViewById(R.id.btnOk);
        btnSubmit=findViewById(R.id.btnSubmit);
        editEmail=findViewById(R.id.editEmail);
        btnClose=findViewById(R.id.btnClose);
//        txtAddInvoices=findViewById(R.id.txtAddInvoices);
        txtTokenNuber=findViewById(R.id.txtTokenNuber);
        txtPercentage=findViewById(R.id.txtPercentage);
        rel_layout_Success=findViewById(R.id.rel_layout_Success);
        rel_layout_Image=findViewById(R.id.rel_layout_Image);
        rel_layout_Progress=findViewById(R.id.rel_layout_Progress);
        rel=findViewById(R.id.rel);
        btnHistory=findViewById(R.id.btnHistory);
        progressBar=findViewById(R.id.progressBar);
//        btnViewPDF=findViewById(R.id.btnViewPDF);
        context=MainActivity.this;
        requestPermission();
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(context);
//        mLayoutManager1.setReverseLayout(true);
//        mLayoutManager1.setStackFromEnd(true);
        mLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        listImages.setLayoutManager(mLayoutManager1);
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        txtToken=findViewById(R.id.txtToken);

        if (getEmail().equals(""))
            rel.setVisibility(View.VISIBLE);
        else
            rel.setVisibility(View.INVISIBLE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=editEmail.getText().toString();
                if (email.equals(""))
                    toast("please enter email address");
                else new SendRequest().execute();

            }
        });

        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }



        File file ;
        String directoryPath = android.os.Environment.getExternalStorageDirectory().toString()+"/Image_to_Pdf_assignment";
        try{
            file = new File(directoryPath);
            boolean success = true;
            if (!file.exists()) {
//                file.mkdirs();
                success = file.mkdirs();
            }
            if (success) {
                Log.e("Folder Created",file.toString());
            } else {
                Log.e("Folder not Created",file.toString());
            }
        }
        catch (Exception e) {
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesUriArrayList.size() > 0) {
                    rel_layout_Success.setVisibility(View.VISIBLE);
                }
                else
                    toast("no images found");

            }
        });
        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCameraImages.performClick();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_layout_Success.setVisibility(View.GONE);
                pdfName=txtTokenNuber.getText().toString();
                if (!pdfName.equals("")) {
                    new async().execute();
                }else
                    toast("enter pdf name");
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_layout_Image.setVisibility(View.GONE);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PdfListActivity.class);
                startActivity(intent);

            }
        });

        btnCameraImages.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkCameraPermission()) {

                    if (context!=null) {
                        ActivityCompat.requestPermissions( MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    }
                } else {
                    try {
                        ContentValues values;
                        values = new ContentValues();
//                        values.put(MediaStore.Images.Media.ORIENTATION, "orientation");
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");

                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUri = context.getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, CAMERA_REQUEST);
                    }catch (Exception e){}

                }
            }
        });

        btnGallaryImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkCameraPermission()) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLARY_REQUEST);
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }




    public boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED;

    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLARY_REQUEST){
            try {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    imagesUriArrayList.add(uri);
                    imageFile = new File(FilePath.getPath(context,uri));
                    mImagesUri.add(String.valueOf(imageFile));
//                    getRealPathFromURI(uri);
//                    mImagesUri.add(data.getData())
                }
                Log.e("SIZE", imagesUriArrayList.size() + "");

                customAdapter = new listImagesHorizontal(MainActivity.this, imagesUriArrayList);
                listImages.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
                toast("Total images "+imagesUriArrayList.size());
                toast("image added");
            }
//                }

            catch (Exception e) {
             try{
                 Uri selectedFileUri = data.getData();

                imagesUriArrayList.add(selectedFileUri);
                customAdapter = new listImagesHorizontal(MainActivity.this, imagesUriArrayList);
                listImages.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
                imageFile = new File(FilePath.getPath(context,selectedFileUri));
                mImagesUri.add(String.valueOf(imageFile));
                toast("image added");
            } catch (Exception exc){}}
        }

        if (requestCode==CAMERA_REQUEST){
            if (resultCode == RESULT_OK) {
                try {
                    if (imageUri != null) {

                        Bitmap bitmapImage = BitmapFactory.decodeFile( getRealPathFromURI2(imageUri));

                        int nh = (int) ( bitmapImage.getHeight() * (2048.0 / bitmapImage.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 2048, nh, true);

                        imageUri=  getImageUri(context,scaled);
                        imagesUriArrayList.add(imageUri);
                        getRealPathFromURI(imageUri);

                        customAdapter = new listImagesHorizontal(MainActivity.this, imagesUriArrayList);
                        listImages.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();

                        toast("image added");
//                        if (imagesUriArrayList.size() > 0) {
////                            txtAddInvoices.setText("Add more Invoices");
//
//                            btn_submit.setAlpha(1);
//                            btn_submit.setClickable(true);
//                            btnGenerateNew.setAlpha(.2f);
//                            btnGenerateNew.setClickable(false);
////                            txtToken.setText("");
//                        }

                    } else
                        toast("scan again");


                } catch (Exception e) {
                    toast("please try again " + e.getMessage());
                    Log.e("image error", e.getMessage());
                }
            }
        }






    }






    public  void toast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }


    public  String getRealPathFromURI(Uri uri) {
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                Log.e("path ",path);
                mImagesUri.add(path);
                cursor.close();
            }
        }
        return path;
    }

    public  String getRealPathFromURI2(Uri uri) {
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    class async extends AsyncTask<Void,Void,Void> {
        String directoryPath;
        ProgressDialog progressDialog=new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            progressDialog.show();
//            imagePath=  getRealPathFromURI(imageUri);

            File  file ;
            directoryPath = android.os.Environment.getExternalStorageDirectory().toString()+"/Image_to_Pdf_assignment";
            try{
                file = new File(directoryPath);
                boolean success = true;
                if (!file.exists()) {
//                file.mkdirs();
                    success = file.mkdirs();
                }
                if (success) {
                    Log.e("Folder Created",file.toString());
                } else {
                    Log.e("Folder not Created",file.toString());
                }

                document=null;

                int mMarginLeft=0,mMarginRight=0,mMarginTop=0,mMarginBottom=0;
                Rectangle pageSize=new Rectangle(PageSize.getRectangle("A4"));
                Log.e("page size  ",pageSize.toString());
                try {
                    pageSize = new Rectangle(PageSize.A4);
                    Log.e("page size1  ",pageSize.toString());
                }catch (Exception e){
                    Log.e("error page size onpre ",e.getMessage());
                }
                pageSize.setBackgroundColor(getBaseColor(1));
                document = new Document(pageSize,
                        mMarginLeft, mMarginRight, mMarginTop, mMarginBottom);
                Log.e("stage 2", "Document Created");
                document.setMargins(mMarginLeft, mMarginRight, mMarginTop, mMarginBottom);

            }
            catch (Exception e) {
                Log.e("error document onpre ",e.getMessage());
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            txtToken.setText("Your PDF Created Successfully");
            progressDialog.dismiss();
            toast("Your PDF Created Successfully");
            toast("Pdf Saved on 'Image_to_Pdf_assignment' Folder ");
            mImagesUri.clear();
            imagesUriArrayList.clear();
            customAdapter.notifyDataSetChanged();
            txtTokenNuber.setText("");
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String directoryPath = android.os.Environment.getExternalStorageDirectory().toString()+"/Image_to_Pdf_assignment";

                Rectangle documentRect = document.getPageSize();
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/"+pdfName+".pdf"));
                writer.setFullCompression();

                document.open();
                Log.e("docuent open", "for loop started");

                for (int i = 0; i < mImagesUri.size(); i++) {
                    int quality;
                    quality = 30;
                    String path = mImagesUri.get(i);
                    Image image = Image.getInstance(path);
                    Log.e("image ",
                            image.getScaledWidth()+"");

                    Log.e("path ",path);
                    if (image.getScaledWidth() > image.getScaledHeight())
                        image.setRotationDegrees(270);


//                    Image image = Image.getInstance(FilePath.getPath(context, imageUri));
                    // compressionLevel is a value between 0 (best speed) and 9 (best compression)
                    double qualtyMod = quality * 0.09;
                    image.setCompressionLevel((int) qualtyMod);
                    image.setBorder(Rectangle.BOX);
                    image.setBorderWidth(0);
//                    image.setRotationDegrees(0);

//                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                    Bitmap bitmap = BitmapFactory.decodeFile(mImagesUri.get(i), bmOptions);

//                    float pageWidth = document.getPageSize().getWidth() - (mMarginLeft + mMarginRight);
//                    float pageHeight = document.getPageSize().getHeight() - (mMarginBottom + mMarginTop);
//                    if ("mImageScaleType".equals(IMAGE_SCALE_TYPE_ASPECT_RATIO))
//                        image.scaleToFit(pageWidth, pageHeight);
//                    else
//                        image.scaleAbsolute(pageWidth, pageHeight);
                    image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                    Log.e("scaleToFit ", PageSize.A4.toString());

                    image.setAbsolutePosition(
                            (documentRect.getWidth() - image.getScaledWidth()) / 2,
                            (documentRect.getHeight() - image.getScaledHeight()) / 2);

//                    Log.e("width: ", pageWidth+" height "+pageHeight+" size "+pageSize);

//                    addPageNumber(documentRect, writer);
                    document.add(image);

                    document.newPage();
//
                    Log.e("image ","x " +image.getXYRatio()+" y "+image.getAbsoluteY());
                }

                document.close();

//                PdfReader reader = new PdfReader(new FileInputStream( directoryPath+ "/payment_invoice.pdf"));
//                PdfStamper stamper = new PdfStamper(reader, new FileOutputStream( directoryPath+ "/AAAAA0428B.pdf"));
//                int total = reader.getNumberOfPages() + 1;
//                for ( int i=1; i<total; i++) {
//                    reader.setPageContent(i + 1, reader.getPageContent(i + 1));
//                }
//                stamper.setFullCompression();
//                stamper.close();


                toast("pdf created at this location : "+directoryPath + "/"+pdfName);
                mImagesUri.clear();
                imagesUriArrayList.clear();
                customAdapter.notifyDataSetChanged();
                Log.e("Stage 8", "Record inserted in database");

            } catch (Exception e) {
//                e.printStackTrace();
                Log.e("error document ",e.getMessage());
//        mSuccess = false;
            }
            return null;
        }
    }



    private BaseColor getBaseColor(int color) {
        return new BaseColor(
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {//DO your stuff }
            Intent intent=new Intent(getApplicationContext(), PdfListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_slideshow) {//DO your stuff }
            Intent intent=new Intent(getApplicationContext(), AboutApp.class);
            startActivity(intent);
        }

        return false;
    }



    public class SendRequest extends AsyncTask<String, Void, String> {


        ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading please wait......");
            progressDialog.show();
        }

        protected String doInBackground(String... arg0) {

            try{
                //Change your web app deployed URL or u can use this for attributes (name, country)
                URL url = new URL("https://script.google.com/macros/s/AKfycbz9rW6NAhK8Sg7I4HAk5uCbGww8Oulbof9xyv-8J1K3Z8e_Wg6s/exec");

                JSONObject json = new JSONObject();

                //int i;
                //for(i=1;i<=70;i++)


                //    String usn = Integer.toString(i);



                json.put("email",email );
                json.put("action","addItem" );
                Log.e("params",json.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(json));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    setEmail(email);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("success");
                            rel.setVisibility(View.GONE);
                        }
                    });
                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String(" " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }



    public void setEmail(String setEmployeeCode)
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("setEmail",setEmployeeCode);
        editor.commit();
    }
    public String getEmail()
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);
        return sharedPreferences.getString("setEmail","");
    }






}
