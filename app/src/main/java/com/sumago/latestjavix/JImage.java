package com.sumago.latestjavix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JImage {
    public JImage()
    {

    }
    public static File createImageFile()  {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File storageDir= MyConfig.CONTEXT.getFilesDir();
            File jcameraDir = new File(storageDir, "jcamera");
            String imageFileName="Image_"+timeStamp;
            boolean jcameraBoolean = false;
            if (jcameraDir.exists() == false) {
                jcameraBoolean = jcameraDir.mkdir();
               // Toast.makeText(MyConfig.CONTEXT," CREATED DIR "+jcameraDir.getAbsolutePath(),Toast.LENGTH_SHORT).show();
            } else {jcameraBoolean = true;}
            if (jcameraBoolean == true) {
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        jcameraDir      /* directory */
                );
                File imagex = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        jcameraDir      /* directory */
                );
                //Toast.makeText(MyConfig.CONTEXT," CREATED FILE",Toast.LENGTH_SHORT).show();
                return image;
            }
        }catch (Exception ee){}
        return(null);
    }

    // Camera
    final static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=9001;
    static public File fileImage=null;
    static private File fileImageResize=null;
    static private Bitmap lastResizedBitmap=null;
    static Uri mImageCaptureUri=null;
    static void captureImage(Activity _this) {
        JImage.fileImage=null;
        JImage.fileImageResize=null;
        File fileTmp= JImage.createImageFile();
        fileImage=fileTmp;

        Uri fileURI= FileProvider.getUriForFile(MyConfig.CONTEXT, BuildConfig.APPLICATION_ID + ".fileprovider",fileTmp);
        JImage.mImageCaptureUri=fileURI;
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, fileURI);
        _this.startActivityForResult(camera, JImage.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    public static Bitmap rotateImageIfRequired(Bitmap img,String selectedImagePath) throws IOException {
        int rotate = 0;
        int orientation = 0;
        try {
            File imageFile = new File(selectedImagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
             orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /****** Image rotation ****/
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        Bitmap cropped = Bitmap.createBitmap(img,0,0 ,img.getWidth(), img.getHeight(), matrix,true);
        img.recycle();
        return cropped;


    }
    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img,0,0 ,img.getWidth(), img.getHeight(), matrix,true);
        img.recycle();
        return rotatedImg;
    }
    static Bitmap getLastResizeBitmap()
    {
       return(lastResizedBitmap);
    }
    static Bitmap resizeBitmap(Bitmap bitmap, int setWidth)
    {
        try {
           JImage.fileImageResize=null;
           JImage.lastResizedBitmap=null;
            float aspectRatio = bitmap.getWidth() /
                    (float) bitmap.getHeight();
            int width = setWidth;
            int height = Math.round(width / aspectRatio);

            Bitmap _bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            JImage.fileImageResize=new File(JImage.fileImage.getAbsoluteFile().toString());
            FileOutputStream fout=new FileOutputStream(JImage.fileImageResize);
            _bitmap.compress(Bitmap.CompressFormat.JPEG,98,fout);
            fout.close();
           JImage.lastResizedBitmap=_bitmap;
            return (_bitmap);
        }catch (Exception ee){}
        return(null);
    }
    public static  Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    public static Bitmap getCapturedBitmap()
    {
        try {
            File fileImage =JImage.fileImage;

            Bitmap bitmap = BitmapFactory.decodeFile(fileImage.getAbsolutePath());
            Bitmap newBitmap=JImage.resizeBitmap(bitmap,800);
            return(newBitmap);
        }catch (Exception er) {Toast.makeText(MyConfig.CONTEXT,"Captures image has been lost",Toast.LENGTH_SHORT).show(); }
        return(null);
    }
    public static File getOriginalFile()
    {
        try{
            return(fileImage);
        }catch (Exception ee)
        {}
        return(null);
    }
    public static File getResizedFile()
    {
        try{
            return(fileImageResize);
        }catch (Exception ee)
        {}
        return(null);
    }
    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
   /* static void uploadImage(Activity activity,Bitmap bitmap) {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, "Uploading Image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MyConfig.CONTEXT, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put("imageData", uploadImage);

                String result = rh.sendPostRequest(MyConfig.UPLOAD_IMAGE, data);

                return result;
            }
        }
        if(bitmap!=null) {
            UploadImage ui = new UploadImage();
            ui.execute(bitmap);
        }
    }*/
}
