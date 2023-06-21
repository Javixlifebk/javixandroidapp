package com.sumago.latestjavix;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.VideoIntigration.RecordVideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class VisualExam extends  UtilityRuntimePermission implements Camera.AsyncResponse{
    private static final int PICK_IMAGE_CAMERA = 10;
    private Camera camera;
    private ImageView mImageView, mImageView1, mImageView2, mImageView3, imageview_id_video, imageview_id_video2;
    String ImagePath = "";
    int Img1 = 1, Img2 = 2, Img3 = 3, Img4 = 4;
    TextView txNotes;
    EditText etNotes;
    Button cmdSubmit = null;
    private static final String TAG = "_msg";
    String res = "", postImageUrl = "";
    int serverResponseCode = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    String Video_No="";
    final HashMap<String, String> imgMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_exam);
        camera = new Camera(VisualExam.this);
        etNotes = (EditText) findViewById(R.id.etNotes);
        txNotes = (TextView) findViewById(R.id.txNotes);
        mImageView = findViewById(R.id.imageview_id_picture);
        mImageView1 = findViewById(R.id.imageview_id_picture1);
        mImageView2 = findViewById(R.id.imageview_id_picture2);
        mImageView3 = findViewById(R.id.imageview_id_picture3);
        imageview_id_video = findViewById(R.id.imageview_id_video);
        imageview_id_video2 = findViewById(R.id.imageview_id_video2);

        //image-1
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadImage();
                if (VisualExam.super.requestAppPermissions(VisualExam.this))
                    camera.selectImage(mImageView, 1);
            }
        });
        //image-2
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VisualExam.super.requestAppPermissions(VisualExam.this))

                    camera.selectImage(mImageView1, Img2);

            }
        });
        //image-3
        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VisualExam.super.requestAppPermissions(VisualExam.this))

                    camera.selectImage(mImageView2, Img3);

            }
        });
        //image-4
        mImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VisualExam.super.requestAppPermissions(VisualExam.this))

                    camera.selectImage(mImageView3, Img4);

            }
        });
        //image-5
        imageview_id_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog("1");
            }
        });

        //image-5
        imageview_id_video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startDialog("2");
                }
        });




        cmdSubmit = (Button) findViewById(R.id.btnSubmit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getApplicationContext(),"Record Added Successfully !",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(VisualExam.this, TestListActivity.class);
                finish();
                startActivity(i);*/

                postImageUrl = "";
                for (Map.Entry<String, String> entry : imgMap.entrySet()) {
                    postImageUrl += entry.getValue() + ",";
                }
//                if (Config.video1Base64!=null) {
                if (postImageUrl.contains("")) {
                    //Toast.makeText(getApplicationContext(),postImageUrl,Toast.LENGTH_LONG).show();
                    submitForm(v);
                }else {
                    Toast.makeText(getApplicationContext(),"Upload Images",Toast.LENGTH_LONG).show();

                }
            }
        });

        txNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNotes.setVisibility(View.VISIBLE);
            }
        });

    }

    private void startDialog(String video_no) {
        Video_No=video_no;
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(VisualExam.this);
        myAlertDialog.setTitle("Upload Video Option");
        myAlertDialog.setMessage("How do you want to set up your Video?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("video/*");
                        startActivityForResult(pickIntent, GALLERY_PICTURE);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent =new Intent(VisualExam.this,RecordVideoActivity.class);
                        intent.putExtra("video_no",video_no);
                        startActivity(intent);

                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void processFinish(String result, int img_no) {
        if (img_no == 1) {
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            Log.d(TAG, "processFinish:1 "+imagename);
            Log.d(TAG, "processFinish:1 "+result);
            ImagePath = result;
            UploadImage la = new UploadImage();
            la.execute();

        }
        if (img_no == 2) {
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            Log.d(TAG, "processFinish:2 "+imagename);
            Log.d(TAG, "processFinish:2 "+result);
            ImagePath = result;
            UploadImage la = new UploadImage();
            la.execute();

        }
        if (img_no == 3) {
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            Log.d(TAG, "processFinish:3 "+imagename);
            Log.d(TAG, "processFinish:3 "+result);
            ImagePath = result;
            UploadImage la = new UploadImage();
            la.execute();

        }
        if (img_no == 4) {
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            Log.d(TAG, "processFinish:4 "+imagename);
            Log.d(TAG, "processFinish:4 "+result);
            ImagePath = result;
            UploadImage la = new UploadImage();
            la.execute();

        }
    }

    @Override
    public void onPermissionsGranted(boolean result) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            camera.myActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ++"+requestCode);
        Log.d(TAG, "onActivityResult: ++"+resultCode);
        Log.d(TAG, "onActivityResult: ++"+data);

            if (resultCode == RESULT_OK) {
                if (data!=null) {
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri.toString().contains("video")) {
                        String[] filePathColumn = {MediaStore.Video.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedMediaUri,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        try {
                            if (filePath != null) {

                                MediaPlayer mp = MediaPlayer.create(this, Uri.parse(filePath));
                                int duration = mp.getDuration();
                                mp.release();

                                if ((duration / 1000) > 30) {
                                    Toast.makeText(VisualExam.this, "Please Upload 30 Second Video Only", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "onActivityResult: " + filePath);
                                    ConvertToString(filePath);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    }


    void submitForm(View v) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        HashMap<String, String> paramHash = new HashMap<String, String>();
        paramHash.put("screenerId", Config._screenerid);
        if (etNotes.getText().length() > 0) {
            paramHash.put("notes", etNotes.getText().toString());
        }
        paramHash.put("status", "1");
        paramHash.put("caseId", Config.tmp_caseId);
        paramHash.put("citizenId", Config.tmp_citizenId);
        paramHash.put("image", postImageUrl);
        paramHash.put("ngoId", Config.NGO_ID);
        paramHash.put("visual_exam_file1", Config.video1Base64);
        paramHash.put("visual_exam_file2", Config.video2Base64);

        SubmitForm req = new SubmitForm(this, paramHash);


        Log.e("check", "caseId: " + Config.tmp_caseId);
        Log.e("check", "citizenId: " + Config.tmp_citizenId);
        Log.e("check", "image: " + postImageUrl);
        Log.e("check", "ngo: " + Config.NGO_ID);
        Log.e("check", "screenerId: " + Config._screenerid);
        Log.e("check", "vido: " + Config.video1Base64);
        Log.e("check", "vido2: " + Config.video2Base64);

        req.execute(MyConfig.URL_VISUALEXAM);

    }

    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public SubmitForm(Activity activity, HashMap<String, String> paramsHash) {
            this.activity = activity;
            this.paramsHash = paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("loading");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            return requestPipe.requestForm(params[0], paramsHash);
        }

        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(VisualExam.this
                    );
                    alertDialog.setMessage("Visual Exam Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Config.video1Base64=null;
                            Config.video2Base64=null;
                            Intent i = new Intent(VisualExam.this, TestListActivity.class);
                            finish();
                            startActivity(i);
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Config.video1Base64=null;
                    Config.video2Base64=null;
                }
            } catch (Exception ee) {
                Log.e("check", "opps: " + ee);
                Config.video1Base64=null;
                Config.video2Base64=null;
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }






    void DownloadImage() {
        if (ContextCompat.checkSelfPermission(VisualExam.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(VisualExam.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(VisualExam.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VisualExam.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(VisualExam.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(VisualExam.this, new String[]{Manifest.permission.CAMERA}, 123);

        }
    }



    //****************************** Upload Image *****************************************

    class UploadImage extends AsyncTask<Void, String, String> {

        private Dialog loadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(VisualExam.this, "Uploading Media File, Please wait", "Loading...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String fileName = ImagePath;
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
//                URL url = new URL("http://139.59.59.31:3001/upload/documents");
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
                    res = sb.toString();
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
                        Toast.makeText(VisualExam.this, "MalformedURLException" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(VisualExam.this, ImagePath,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e("Response", "Response from server: " + result);
            Log.e(TAG, "return url : " + result);
            String jsonStr = result;

            loadingDialog.dismiss();
            if (jsonStr != null) {
                try {
                    JSONObject obj1 = new JSONObject(jsonStr);
                    Toast.makeText(VisualExam.this, "Media File Uploaded Successfully ",
                            Toast.LENGTH_SHORT).show();
                   if (obj1.getString("success").equalsIgnoreCase("1")) {
                        if (Img1 == 1) {
                            if (imgMap.containsKey("Img1"))
                                imgMap.put("Img1", obj1.getString("url"));
                            else {
                                imgMap.remove("Img1");
                                imgMap.put("Img1", obj1.getString("url"));
                            }
                        } else if (Img2 == 2) {
                            if (imgMap.containsKey("Img2"))
                                imgMap.put("Img2", obj1.getString("url"));
                            else {
                                imgMap.remove("Img2");
                                imgMap.put("Img2", obj1.getString("url"));
                            }
                        } else if (Img3 == 3) {
                            if (imgMap.containsKey("Img3"))
                                imgMap.put("Img3", obj1.getString("url"));
                            else {
                                imgMap.remove("Img3");
                                imgMap.put("Img3", obj1.getString("url"));
                            }
                        } else if (Img4 == 4) {
                            if (imgMap.containsKey("Img4"))
                                imgMap.put("Img4", obj1.getString("url"));
                            else {
                                imgMap.remove("Img4");
                                imgMap.put("Img4", obj1.getString("url"));
                            }
                        }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Log.e("Back Button", "Pressed");
            Intent i = new Intent(VisualExam.this, TestListActivity.class);
            finish();
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.video1Base64!=null){
            imageview_id_video.setBackground(getResources().getDrawable(R.drawable.black_rect_light));
        }

        if (Config.video2Base64!=null){
            imageview_id_video2.setBackground(getResources().getDrawable(R.drawable.black_rect_light));
        }
    }


    public void ConvertToString(String path) {
        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output64.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Video_No.contains("1")) {
            //video 1
            Config.video1Base64 = output.toString();
        } else {
            //video 2
            Config.video2Base64 = output.toString();
        }
        Log.d("TAG", "ConvertToString: " + output.toString());
    }

    @Override
    public void onBackPressed() {
        finish();
        Config.video1Base64=null;
        Config.video2Base64=null;
    }

}

