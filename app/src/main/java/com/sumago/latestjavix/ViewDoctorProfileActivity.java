package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class ViewDoctorProfileActivity extends AppCompatActivity {
    private static final String TAG = "_msg";
    Context context;
    HttpURLConnection conn;
    HashMap hasData;
    TextView txName,txEmail,txMobile,txQualification,txSpecialization,txDob,txCountry,txState,txDistrict,txAddress;
    String ImagePath="";
    String res="",postImageUrl="";
    int serverResponseCode = 0;
    ImageView mImageView,mImgSignature;
    String ImgUrl="",SigUrl="",updateType="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor_profile);
        downloadJSON();
        txName=(TextView)findViewById(R.id.txName);
        txEmail=(TextView)findViewById(R.id.txEmail);
        txMobile=(TextView)findViewById(R.id.txMobile);
        txQualification=(TextView)findViewById(R.id.txQualification);
        txSpecialization=(TextView)findViewById(R.id.txSpecialization);
        txDob=(TextView)findViewById(R.id.txDob);
        txCountry=(TextView)findViewById(R.id.txCountry);
        txState=(TextView)findViewById(R.id.txState);
        txDistrict=(TextView)findViewById(R.id.txDistrict);
        txAddress=(TextView)findViewById(R.id.txAddress);
        mImageView = findViewById(R.id.profile_image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        //.setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1) //You can skip this for free form aspect ratio)
                        .start(ViewDoctorProfileActivity.this);
                updateType="Photo";
            }
        });
        mImgSignature= findViewById(R.id.signature_image);
        mImgSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        //.setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1) //You can skip this for free form aspect ratio)
                        .start(ViewDoctorProfileActivity.this);
                updateType="Signature";
            }
        });
    }
    private void downloadJSON() {

        class DownloadJSON extends AsyncTask<Void, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ViewDoctorProfileActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int respStatus=jsonObject.getInt("status");
                    if(respStatus==1) {
                        //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        ArrayList<DoctorData> recsArrayList=new ArrayList<DoctorData>();
                        JSONObject recsData=jsonObject.getJSONObject("data");
                        JSONArray recsArray=recsData.getJSONArray("data");
                        int recsLen=recsArray.length();
                        for(int i=0;i<recsLen;i++) {
                            JSONObject rec = recsArray.getJSONObject(i);
                            DoctorData doctorDataData = new DoctorData();
                            txName.setText("Name : Dr." +  rec.getString("firstName") + " " + rec.getString("lastName"));
                            txEmail.setText("Email :" + rec.getString("email"));
                            txMobile.setText("Mobile :" + rec.getString("mobile"));
                            SigUrl=rec.getString("signature");
                            JSONObject info = rec.getJSONObject("info");
                            txQualification.setText("Qualification :" + info.getString("qualification"));
                            txSpecialization.setText("Specialization :" + info.getString("specialisation"));
                            txDob.setText("DOB :" + info.getString("dateOfBirth"));
                            txCountry.setText("Country :" + info.getString("country"));
                            txState.setText("State :" + info.getString("state"));
                            txDistrict.setText("District :" + info.getString("district"));
                            txAddress.setText("Address :" + info.getString("address"));
                            ImgUrl=info.getString("photo");
                            new DownloadImageTask().execute(ImgUrl);
                            new DownloadSignatureTask().execute(SigUrl);
                            /*doctorDataData.mobile = rec.getString("mobile");
                            doctorDataData.email = rec.getString("email");
                            JSONObject info = rec.getJSONObject("info");
                            doctorDataData.address = info.getString("address");
                            doctorDataData.country = info.getString("country");
                            doctorDataData.state = info.getString("state");
                            doctorDataData.district = info.getString("district");
                            doctorDataData.Qualification = info.getString("qualification");
                            doctorDataData.Qualification = URLDecoder.decode(doctorDataData.Qualification, "utf-8");
                            doctorDataData.Specialization = info.getString("specialisation");
                            doctorDataData.Specialization = URLDecoder.decode(doctorDataData.Specialization, "utf-8");
                            JSONArray is_online = rec.getJSONArray("online");
                            JSONObject is_loggedin = is_online.getJSONObject(0);*/
                        }
                }} catch (JSONException e) {
                    //e.printStackTrace();
                    Log.e(TAG, "json err : " + e.getMessage());
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL(MyConfig.URL_DOCTOR_PROFILE);
                    StringBuilder sb = new StringBuilder();
                    conn = (HttpURLConnection) url.openConnection();
                    //conn.setRequestProperty("Content-Type", "application/text");
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setChunkedStreamingMode(0);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    StringBuilder result = new StringBuilder();
                    result.append(URLEncoder.encode("userId", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(Config._uid, "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("token", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode("dfjkhsdfaksjfh3756237", "UTF-8"));
                    writer.write(result.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.e(TAG, "postdata: " + result);
                    Log.e(TAG, "Response Code: " + Integer.toString(responseCode));
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        sb = new StringBuilder();
                        String response;
                        while ((response = br.readLine()) != null) {
                            sb.append(response);
                        }
                        Log.e(TAG, "response: " + sb);
                    }
                    else{
                        Log.e(TAG, "else part : " +sb);
                        return "{\"status\":0,\"message\":\"Login Failed.\"}";
                    }


                    return sb.toString().trim();
                }catch (Exception e) {
                    Log.e(TAG, "exception : " + e.getMessage());

                    return "{\"status\":0,\"message\":\"Login Failed.\"}";
                }
                finally {
                    conn.disconnect();
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }


    void submitForm(){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());

        ErrBox.errorsStatus();
        //Toast.makeText(getApplicationContext(),"Error !"+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"Screener ID"+ Config._screenerid,Toast.LENGTH_SHORT).show();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        //Config._screenerid="161315136114567577";
        if(updateType.equalsIgnoreCase("Photo")) {
            paramHash.put("roleId", Integer.toString(Config._roleid));
            paramHash.put("ngoId", Config.NGO_ID);

            paramHash.put("id", Config._doctorid);

            paramHash.put("photo", postImageUrl);
        }else{
            paramHash.put("doctorId", Config._doctorid);
            paramHash.put("signature",postImageUrl);
            paramHash.put("ngoId", Config.NGO_ID);

        }

        ViewDoctorProfileActivity.SubmitForm req=new ViewDoctorProfileActivity.SubmitForm(this,paramHash);
        if(updateType.equalsIgnoreCase("Photo"))
            req.execute(MyConfig.URL_UPDATEPHOTO);
        else
            req.execute(MyConfig.URL_UPDATESIGNATURE);
    }
    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String>{
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
        protected void onPreExecute()
        {
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            Log.e(TAG, "parametersssss : " +paramsHash);
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

                    Toast.makeText(getApplicationContext(), "Doctor's Photo Updated Successfully !", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(getApplicationContext(), "Exception !.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class UploadImage extends AsyncTask<Void, String, String> {

        private Dialog loadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(ViewDoctorProfileActivity.this, "Please wait", "Loading...");
        }
        @Override
        protected String doInBackground(Void... params) {
            String fileName =ImagePath;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(fileName);
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://143.244.136.145:3010/upload/profile");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("profile", fileName);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"profile\";filename=\""
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
                        Toast.makeText(ViewDoctorProfileActivity.this, "MalformedURLException" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ViewDoctorProfileActivity.this, ImagePath,
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
                    Toast.makeText(ViewDoctorProfileActivity.this, "File Uploaded Successfully ",
                            Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, "status : " + obj1.getString("message"));

                    if(obj1.getString("success").equalsIgnoreCase("1")) {
                        postImageUrl = obj1.getString("profile-url");
                        submitForm();
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

    //*******************************Image Cropoper ***********************************
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if(updateType.equalsIgnoreCase("Photo"))
                    mImageView.setImageURI(resultUri);
                else
                    mImgSignature.setImageURI(resultUri);
                ImagePath=resultUri.getPath().toString();
                new ViewDoctorProfileActivity.UploadImage().execute();
                Bitmap bMap = BitmapFactory.decodeFile(resultUri.getPath().toString());
                saveToInternalStorage(bMap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage){
        DownloadImage();
        Bitmap cbitmap;
        cbitmap=ShrinkBitmap(ImagePath,1366,768);
        if(isStoragePermissionGranted()) {
            String selectedImagePath = "javix_" + String.valueOf(System.currentTimeMillis())
                    + ".jpg";
            File file = new File(Environment.getExternalStorageDirectory(),
                    selectedImagePath);
            ImagePath=Environment.getExternalStorageDirectory()+"/"+selectedImagePath;
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                cbitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
                // UploadImage la = new UploadImage();
                //la.execute();
            } catch (Exception e) {
                Toast.makeText(ViewDoctorProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    public Bitmap ShrinkBitmap(String file, int width, int height){
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);
        //Toast.makeText(BillViewActivityHcl.this, Integer.toString(bmpFactoryOptions.outHeight), Toast.LENGTH_SHORT).show();
        if(heightRatio > 1 || widthRatio > 1){
            if(heightRatio > widthRatio){bmpFactoryOptions.inSampleSize = heightRatio;}
            else{bmpFactoryOptions.inSampleSize = widthRatio;}
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }
    void DownloadImage() {
        if (ContextCompat.checkSelfPermission(ViewDoctorProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ViewDoctorProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewDoctorProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(ViewDoctorProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                // Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            mImageView.setImageBitmap(result);
        }

    }

    private class DownloadSignatureTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        /* public DownloadImageTask(ImageView bmImage) {
             this.bmImage = bmImage;
         }
 */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                // Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            mImgSignature.setImageBitmap(result);
        }

    }
}