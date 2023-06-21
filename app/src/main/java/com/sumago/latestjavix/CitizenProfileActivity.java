package com.sumago.latestjavix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class CitizenProfileActivity extends AppCompatActivity {
    private static final String TAG = "_msg";
    Context context;
    HttpURLConnection conn;
    HashMap hasData;
    TextView txName,txEmail,txMobile,txQualification,txSpecialization,txDob,txCountry,txState,txDistrict,txAddress,txPin,txAdhaadNo;
    String ImagePath="";
    String res="",postImageUrl="";
    int serverResponseCode = 0;
    ImageView mImageView;
    String ImgUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_profile);
        txName=(TextView)findViewById(R.id.txName);
        txEmail=(TextView)findViewById(R.id.txEmail);
        txMobile=(TextView)findViewById(R.id.txMobile);
        mImageView = findViewById(R.id.profile_image);
        /*txQualification=(TextView)findViewById(R.id.txQualification);
        txSpecialization=(TextView)findViewById(R.id.txSpecialization);
        txQualification.setVisibility(View.GONE);
        txSpecialization.setVisibility(View.GONE);*/
        txDob=(TextView)findViewById(R.id.txDob);
        txCountry=(TextView)findViewById(R.id.txCountry);
        txState=(TextView)findViewById(R.id.txState);
        txDistrict=(TextView)findViewById(R.id.txDistrict);
        txAddress=(TextView)findViewById(R.id.txAddress);
        txPin=(TextView)findViewById(R.id.txPin);
        txAdhaadNo=(TextView)findViewById(R.id.txAdhaadNo);
        Bundle bundle=getIntent().getExtras();
        if(!bundle.isEmpty()){
            txName.setText(txName.getText().toString() + bundle.getString("name"));
            txEmail.setText(txEmail.getText().toString() +bundle.getString("email"));
            txMobile.setText(txMobile.getText().toString() +bundle.getString("mobile"));
            txDob.setText(txDob.getText().toString() +bundle.getString("dateOfOnBoarding"));
            txCountry.setText(txCountry.getText().toString() +"INDIA");
            txState.setText(txState.getText().toString() +bundle.getString("state"));
            txDistrict.setText(txDistrict.getText().toString() +bundle.getString("district"));
            txAddress.setText(txAddress.getText().toString() +bundle.getString("address"));
            txPin.setText(txPin.getText().toString() + bundle.getString("pincode"));
            txAdhaadNo.setText(txAdhaadNo.getText().toString() + bundle.getString("aadhaar"));
            ImgUrl=bundle.getString("photo");
            new DownloadImageTask().execute(ImgUrl);
        }
        //mImageView = findViewById(R.id.profile_image);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        /* public DownloadImageTask(ImageView bmImage) {
             this.bmImage = bmImage;
         }
 */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
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


}