package com.sumago.latestjavix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class HeartActivity extends AppCompatActivity {
    ImageView mImageView;
    String ImagePath="";
    TextView txFilename;
    EditText etNotes;
    Button cmdSubmit=null,cmdCopy=null,cmdEko=null;
    private static final String TAG = "_msg";
    String res="",postImageUrl="";
    int serverResponseCode = 0;
    private int PICK_PDF_REQUEST = 1;
    private Uri filePath;
    int flag_eko=0;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);

        txFilename=(TextView) findViewById(R.id.txFilename);
        mImageView = findViewById(R.id.imageview_id_picture);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DownloadImage();
                CropImage.activity()
                        //.setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1) //You can skip this for free form aspect ratio)
                        .start(HeartActivity.this);*/
                requestStoragePermission();
                showFileChooser();
            }
        });

        cmdSubmit=(Button)findViewById(R.id.btnSubmit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getApplicationContext(),"Record Added Successfully !",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HeartActivity.this, TestListActivity.class);
                finish();
                startActivity(i);*/
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HeartActivity.this);
                alertDialog.setMessage("Have you Captured data from EKO Device !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        submitForm(v);
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                alertDialog.create();
                alertDialog.show();


            }
        });


        cmdCopy=(Button)findViewById(R.id.btnCopy);
        cmdCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("CaseID", "CaseId:"+Config.tmp_caseId);
                clipboard.setPrimaryClip(clip);
                if(clip.toString().length()>0){
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HeartActivity.this
                    );
                    alertDialog.setMessage("Copied Case ID " + clipboard.getText().toString());
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();

                }

            }
        });

        cmdEko=(Button)findViewById(R.id.btnEko);
        cmdEko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.ekodevices.android.intl");
                    startActivity(launchIntent);

            }
        });
    }


    void submitForm(View v){
        postImageUrl="NoUrl";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("screenerId", Config._screenerid);
        paramHash.put("caseId", Config.tmp_caseId);
        paramHash.put("citizenId", Config.tmp_citizenId);
        paramHash.put("url", postImageUrl);
        paramHash.put("ngoId", Config.NGO_ID);

        SubmitForm req=new SubmitForm(this,paramHash);
        req.execute(MyConfig.URL_ADDHEART);
    }
    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public SubmitForm(Activity activity,HashMap<String, String> paramsHash) {
            this.activity=activity;
            this.paramsHash=paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("loading");
        }
        @Override
        protected void onPreExecute(){
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            return requestPipe.requestForm(params[0],paramsHash);
        }
        protected void onProgressUpdate(Void ...progress) {
            super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus=jsonObject.getInt("status");
                if(respStatus==1) {
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HeartActivity.this
                    );
                    alertDialog.setMessage("Heart Data Updated Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i = new Intent(HeartActivity.this, TestListActivity.class);
                            finish();
                            startActivity(i);
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    /**************File Chooser***********////
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }
    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.e("File Path",filePath.getPath());
            String _fileNameo[]= filePath.getPath().split(":");
            File file0=new File(_fileNameo[0]+"/"+_fileNameo[1]);
            String _fileName=file0.getName();
            //String path = getFileNameByUri(this, filePath);
            //String path=FilePath.getPath(this,filePath);
            //String path
            /*File downloadFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File _file=new File(downloadFolder,_fileName);
            System.out.println("--------------------"+_file.getAbsoluteFile());
            System.out.println("File Name =" + _fileName);
            if(_file.exists()) {
                FILESELECTED=_file;
                //Toast.makeText(getApplicationContext(), downloadFolder.getAbsolutePath(), Toast.LENGTH_LONG).show();
                UploadImage la = new UploadImage();
                la.execute(_file);
            }*/

            File downloadFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File _file=null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            {

                _fileName=queryName(HeartActivity.this.getContentResolver(),filePath );
                _file=new File(downloadFolder,_fileName);
            }
            else _file=new File(downloadFolder,_fileName);

            System.out.println("--------------------"+_file.getAbsoluteFile());
            System.out.println("File Name =" + _fileName);
            if(_file.exists()) {
                FILESELECTED=_file;
                //Toast.makeText(getApplicationContext(), downloadFolder.getAbsolutePath(), Toast.LENGTH_LONG).show();
                UploadImage la = new UploadImage();
                la.execute(_file);
            }

        }
    }
    String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
    File FILESELECTED=null;
    //***************End of file chooser***************///
    //****************************** Upload Image *****************************************
    class UploadImage extends AsyncTask<File, String, String> {

        private Dialog loadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(HeartActivity.this, "Uploading Pdf File, Please wait", "Loading...");
            System.out.println("****************Pre Executed");
        }
        @Override
        protected String doInBackground(File... params) {
            File file_=params[0];

            //String fileName ="/storage/emulated/0/Download/37875_2018_39_1501_24602_Judgement_04-Nov-2020 (1).pdf";
            String fileName =file_.getName();
            System.out.println("****************Background=" + fileName);
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = file_;//new File(fileName);
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://143.244.136.145:3010/upload/documents");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("document", fileName);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"document\";filename=\""
                        + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                serverResponseCode = conn.getResponseCode();
                if (serverResponseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String response;
                    while ((response = br.readLine()) != null) {
                        sb.append(response);
                    }
                    Log.e(TAG, "response: " + sb);
                    res=sb.toString();
                }
                String serverResponseMessage = conn.getResponseMessage();
                //res = serverResponseMessage;
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(HeartActivity.this, "MalformedURLException" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("EXP:"+e);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(HeartActivity.this, ImagePath,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            //Log.e("Response", "Response from server: " + result);
            Log.e(TAG, "return url : " +result);
            String jsonStr = result;

            loadingDialog.dismiss();

            if (jsonStr != null) {
                try {
                    JSONObject obj1 = new JSONObject(jsonStr);
                    Toast.makeText(HeartActivity.this, "Pdf Uploaded Successfully ",
                            Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, "status : " + obj1.getString("message"));
                    if(obj1.getString("success").equalsIgnoreCase("1")) {
                        postImageUrl = obj1.getString("url");
                    }


                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json exception: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            super.onPostExecute(result);
        }
    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getFileNameByUri(Context context, Uri uri){
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0){
            Cursor cursor = context.getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;
        }
        else
        if (uri.getScheme().compareTo("file") == 0){
            try
            {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            }
            catch (URISyntaxException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
            filepath = uri.getPath();
        }
        return filepath;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Log.e("Back Button","Pressed");
            Intent i = new Intent(HeartActivity.this, TestListActivity.class);
            finish();
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}