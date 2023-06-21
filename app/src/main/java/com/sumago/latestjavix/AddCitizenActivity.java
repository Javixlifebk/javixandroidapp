package com.sumago.latestjavix;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import com.theartofdev.edmodo.cropper.CropImage
import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.Util.StateClass;
import com.sumago.latestjavix.Util.Validations;
import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class AddCitizenActivity extends  UtilityRuntimePermission implements Camera.AsyncResponse, AdapterView.OnItemSelectedListener {
    private Camera camera;
    Button cmdSubmit = null;
    EditText firstName, lastName;
    Spinner gender;
    EditText mobileNo, aadharNo, raadharNo;
    EditText email;
    EditText dob;
    Spinner blood_group;
    EditText txPin;
    EditText country;
    Spinner state, district;
    //EditText ;
    EditText address;
    AppCompatImageView mImageView;
    String _imagePath = "";
    String imageName= "";

    final Calendar myCalendar = Calendar.getInstance();

    private static final String TAG = "_msg";
    ProgressDialog dialog1 = null;
    int serverResponseCode = 0;
    RadioButton mGenderM;
    RadioButton mGenderF;
    String mGender = "";
    boolean mFname, mLname, mPin, mPhoto, mAadhaar, mState, mDistrict, bGender, mDob;
    Spinner spinnerState = null;
    Spinner spinnerDistrict = null;
    ArrayList<String> arrayState = new ArrayList<String>();
    ArrayList<String> arrayDistrict = new ArrayList<String>();
    ArrayAdapter aaDistrict = null;
    JSONObject jsonObject = null;
    Validations validations;
    private String postImageUrl="";
    private String res="";

    /* Update Spiner */
    public List<String> populateBloodgroup() {
        String x = getString(R.string.blood_group);
        String y[] = x.split(",");
        return new ArrayList<String>(Arrays.asList(y));
    }

    public List<String> populateGender() {
        String x = getString(R.string.sex);
        String y[] = x.split(",");
        return new ArrayList<String>(Arrays.asList(y));
    }

    public List<String> populateState() {
        String x = getString(R.string.state);
        String y[] = x.split(",");
        return new ArrayList<String>(Arrays.asList(y));
    }

    public void spinersView_load() {
        //1.
        List<String> bloodgroup = populateBloodgroup();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodgroup);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blood_group.setAdapter(dataAdapter);
        List<String> categoriesState = populateState();
        ArrayAdapter<String> dataAdapterState = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesState);
        dataAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(dataAdapterState);

        spinnerState = (Spinner) findViewById(R.id.spnState);
        spinnerDistrict = (Spinner) findViewById(R.id.spnDistrict);
        arrayState.clear();
        arrayDistrict.clear();
        try {
            jsonObject = new JSONObject(StateClass.DATA);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String KEYNAME = keys.next();
                System.out.println(KEYNAME);
                arrayState.add(KEYNAME);
            }
        } catch (Exception ee) {
            Toast.makeText(AddCitizenActivity.this, "Exp:-" + ee, Toast.LENGTH_SHORT).show();
            System.out.println("JEXP:-" + ee.toString());
        }

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayState);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(aa);
        spinnerState.setOnItemSelectedListener(this);

        aaDistrict = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayDistrict);
        aaDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(aaDistrict);

        /*List<String> categoriesGender = populateGender();
        ArrayAdapter<String> dataAdapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesGender);
        dataAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(dataAdapterGender);
       */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_citizen);
        MyConfig.CONTEXT = this;
        camera = new Camera(AddCitizenActivity.this);
        validations = new Validations();
        cmdSubmit = (Button) findViewById(R.id.addDoctorProfile_submit);
        SQLiteDatabase db;
        mImageView = findViewById(R.id.imageview_id_picture);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadImage();
                if (AddCitizenActivity.super.requestAppPermissions(AddCitizenActivity.this))

                    camera.selectImage(mImageView, 1);


            }
        });
        firstName = (EditText) findViewById(R.id.txFname);
        lastName = (EditText) findViewById(R.id.txLname);
        //gender=(Spinner)findViewById(R.id.addDoctorProfile_gender);
        mGenderM = (RadioButton) findViewById(R.id.identification_gender_male);
        mGenderF = (RadioButton) findViewById(R.id.identification_gender_female);
        mobileNo = (EditText) findViewById(R.id.txMobile);
        email = (EditText) findViewById(R.id.txEmail);
        dob = (EditText) findViewById(R.id.addDoctorProfile_dbo);
        blood_group = (Spinner) findViewById(R.id.blood_group);
        country = (EditText) findViewById(R.id.txCountry);
        state = (Spinner) findViewById(R.id.spnState);
        district = (Spinner) findViewById(R.id.spnDistrict);
        address = (EditText) findViewById(R.id.txAddress);
        txPin = (EditText) findViewById(R.id.txPin);
        aadharNo = (EditText) findViewById(R.id.addDoctorProfile_adhar);
        raadharNo = (EditText) findViewById(R.id.addDoctorProfile_radhar);
        // Date Picker
        final EditText editDob = dob;
//        final Calendar myCalendar = Calendar.getInstance();
        //if(mGender.length()==0) {
        mGenderF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "Female";
                //Toast.makeText(getApplicationContext(), mGender, Toast.LENGTH_SHORT).show();
            }
        });

        mGenderM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "Male";
                //Toast.makeText(getApplicationContext(), mGender, Toast.LENGTH_SHORT).show();
            }
        });


        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String pattern = "dd-MM-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String selDate = simpleDateFormat.format(myCalendar.getTime());
                editDob.setText(selDate);
                Log.e("DOB", "editDob: " + editDob.getText().toString().toString().trim());
            }
        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//                new DatePickerDialog(AddCitizenActivity.this, dateListener, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCitizenActivity.this, dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


//                DatePickerDialog datePickerDialog=new DatePickerDialog(getApplicationContext(), dateListener, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));


                Log.e("DOB", "DOB: " + dob.getText().toString());
            }
        });
        ErrBox.clearAll();
        //ErrBox.add(firstName,"TEXT","First name can't be blank",true);
        //ErrBox.add(lastName,"TEXTONLY","Last name can't be blank",true);
        //ErrBox.add(mGender,"TEXT","select gender",true);
        // ErrBox.add(mobileNo,"MOBILE","fill mobile no (only  digits)",true);
        // ErrBox.add(email,"EMAIL","fill valid email",true);
        //ErrBox.add(dob,"MIXED","select date of birth",true);
        //ErrBox.add(blood_group,"TEXT","select blood group (only char)",true);
        // ErrBox.add(country,"TEXT","fill country (only char)",true);
        // ErrBox.add(state,"TEXT","fill state (only char)",true);
        //ErrBox.add(district,"TEXT","fill district (only char)",true);
        //ErrBox.add(address,"MIXED","fill address (No Special Char)",true);

        // ErrBox.add(txPin,"MIXED","fill Pin Code (No Special Char)",true);
        // SPiner Update
        spinersView_load();

        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstName.getText().length() == 0) {
                    mFname = false;
                    Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._firstName_he), Toast.LENGTH_SHORT).show();
                } else {
                    mFname = true;
                }
                if (mFname == true) {
                    if (lastName.getText().length() == 0) {
                        mLname = false;
                        Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._lastName_he), Toast.LENGTH_SHORT).show();
                    } else {
                        mLname = true;
                    }
                }


                if (mLname == true) {
                    // Toast.makeText(getApplicationContext(),Integer.toString(dob.getText().length()),Toast.LENGTH_SHORT).show();
                    if (dob.getText().length() == 0) {
                        mDob = false;
                        Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._dob_he), Toast.LENGTH_SHORT).show();
                    } else {
                        mDob = true;
                    }
                }
                if (mDob == true) {
                    if (txPin.getText().length() != 6) {
                        mPin = false;
                        Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._pin_he), Toast.LENGTH_SHORT).show();
                    } else {
                        mPin = true;
                    }
                }

                if (mPin == true) {
                    if (state.getSelectedItem().equals("*State")) {
                        mState = false;
                        Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._state_he), Toast.LENGTH_SHORT).show();
                    } else {
                        mState = true;
                    }
                }

                if (mState == true) {
                    if (aadharNo.getText().length() != 12 && raadharNo.getText().length() == 0) {
                        mAadhaar = false;
                        Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._adhaar_he), Toast.LENGTH_SHORT).show();
                    } else if (aadharNo.getText().length() == 0 && raadharNo.getText().length() != 12) {
                        mAadhaar = false;
                        Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._adhaar_he), Toast.LENGTH_SHORT).show();
                    } else {
                        mAadhaar = true;
                    }
                }
                if (mAadhaar == true) {
                    if (mGender.length() == 0) {
                        bGender = false;
                        Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._gender_he), Toast.LENGTH_SHORT).show();
                    } else {
                        bGender = true;
                    }
                }
//                if (!_imagePath.equals("")) {

                    if (bGender == true) {
                        if (mobileNo.getText().length() > 0) {
                            if (mobileNo.getText().length() == 10) {
                                submitForm(v);
                            } else {
                                Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._phone_he), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            submitForm(v);
                        }

                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), AddCitizenActivity.this.getString(R.string._photomsg_he), Toast.LENGTH_SHORT).show();
//                }

                //submitForm(v);
            }
        });
    }

    @Override
    public void processFinish(String result, int img_no) {
        if (img_no == 1) {
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            imageName = imagename;
            _imagePath = result;
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
    }


    void submitForm(View v) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        //Toast.makeText(getApplicationContext(),"Error !"+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"Screener ID"+ Config._screenerid,Toast.LENGTH_SHORT).show();
        HashMap<String, String> paramHash = new HashMap<String, String>();
        //Config._screenerid="161315136114567577";
//        paramHash.put("screenerId", "167248188112951214");
        paramHash.put("screenerId", Config.javixid);
        Log.e("screen", "Config._screenerid" + Config._screenerid);
        paramHash.put("token", Config._token);
        paramHash.put("firstName", firstName.getText().toString());
        paramHash.put("lastName", lastName.getText().toString());
        paramHash.put("sex", mGender);
        if (mobileNo.getText().length() > 0) {
            paramHash.put("mobileNo", mobileNo.getText().toString());
        }
        if (email.getText().length() > 0) {
            paramHash.put("email", email.getText().toString());
        }
        paramHash.put("dateOfBirth", dob.getText().toString());
        paramHash.put("dateOfOnBoarding", currentDate);
        paramHash.put("bloodGroup", blood_group.getSelectedItem().toString());
        paramHash.put("country", country.getText().toString());
        paramHash.put("state", state.getSelectedItem().toString());
        paramHash.put("district", district.getSelectedItem().toString());
        paramHash.put("address", address.getText().toString());
        paramHash.put("pincode", txPin.getText().toString());
        paramHash.put("photo", postImageUrl);
        paramHash.put("aadhaar", aadharNo.getText().toString());
        paramHash.put("ngoId", Config.NGO_ID);
        Log.d("dataofRow", "submitForm: " + paramHash.toString());
        if (Config.isOffline) {
            try {
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                Random rnd = new Random();
                char[] digits = new char[15];
                digits[0] = (char) (rnd.nextInt(9) + '1');
                for (int i = 1; i < digits.length; i++) {
                    digits[i] = (char) (rnd.nextInt(10) + '0');
                }
                String uniqueID = new String(digits);
                paramHash.put("citizenId", uniqueID);
                Log.e("citizen", "Config.citizenId" + uniqueID);
                paramHash.put("ngoId", Config.NGO_ID);
                Log.e("ngoId", "Config.NGO_ID" + Config.NGO_ID);

                Config.tmp_citizenId = uniqueID;
                JSONObject jsonObject = new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('Add Citizen','" + MyConfig.URL_ADD_CITIZEN + "','" + paramHash.toString() + "','" + jsonObject.toString() + "','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddCitizenActivity.this
                );
                alertDialog.setMessage(AddCitizenActivity.this.getString(R.string._citizenAdd_he));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(AddCitizenActivity.this, TestListActivity.class);
                        Config.tmp_caseId = "0";
                        finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();

            } catch (Exception ex) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddCitizenActivity.this
                );
                alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(AddCitizenActivity.this, TestListActivity.class);
                        Config.tmp_caseId = "0";
                        finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        } else {
            AddCitizenActivity.SubmitForm req = new AddCitizenActivity.SubmitForm(this, paramHash);

            Log.e("Atul", "paramshash: " + paramHash.toString());
            req.execute(MyConfig.URL_ADD_CITIZEN);
        }
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

            Log.e(TAG, "parametersssss : " + paramsHash);
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

                // JSONObject recsData=jsonObject.getJSONObject("data");
                //recsData=recsData.getJSONObject("data");
                //Log.e(TAG,"Response in Array" + jsonObject.getString("status"));
                //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
                //JSONArray recsArray=recsData.getJSONArray("data");
                //
                //JSONObject rec = recsArray.getJSONObject(0);
                // Log.e("Response in Object",rec.toString());
                if (respStatus == 1) {
                    JSONObject recsData = jsonObject.getJSONObject("data");
                    recsData = recsData.getJSONObject("data");
                    Config.tmp_citizenId = recsData.getString("citizenId");
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddCitizenActivity.this
                    );
                    alertDialog.setMessage(AddCitizenActivity.this.getString(R.string._citizenAdd_he));
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i = new Intent(AddCitizenActivity.this, TestListActivity.class);
                            Config.tmp_caseId = "0";
                            finish();
                            startActivity(i);
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                } else {
                    //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show()};
                    JSONArray recsData = jsonObject.getJSONArray("data");
                    String Msg = "";
                    for (int i = 0; i < recsData.length(); i++) {
                        JSONObject valObj = recsData.getJSONObject(i);
                        Msg += valObj.getString("msg") + "\n\n";
                    }
                    Log.e(TAG, "Response in Array" + recsData);
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddCitizenActivity.this
                    );
                    alertDialog.setMessage(Msg);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            /*Intent i = new Intent(AddCitizenActivity.this, TestListActivity.class);
                            Config.tmp_caseId="0";
                            finish();
                            startActivity(i);*/
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }

            } catch (Exception ee) {
                //Toast.makeText(getApplicationContext(), "Records Already Exists !", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int respStatus = jsonObject.getInt("status");
                    JSONObject recsData = jsonObject.getJSONObject("data");
                    //recsData = recsData.getJSONObject("data");

                } catch (Exception ex) {
                }
                //JSONArray jsonArray=new JSONArray(e.ge)
            }
        }
    }

    //*******************************Image Cropoper ***********************************
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(this);
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

    File FILESELECTED = null;

    private void saveToInternalStorage(Bitmap bitmapImage) {
        Log.v(TAG, "saveToInternalStorage");
        DownloadImage();
        Bitmap cbitmap;
        cbitmap = ShrinkBitmap(_imagePath, 1366, 768);
        if (isStoragePermissionGranted()) {
            String selectedImagePath = "javix_" + String.valueOf(System.currentTimeMillis())
                    + ".jpg";

            Log.v(TAG, selectedImagePath);
            File file = new File(Environment.getExternalStorageDirectory(),
                    selectedImagePath);
            _imagePath = Environment.getExternalStorageDirectory() + "/" + selectedImagePath;
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                cbitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
                UploadImage la = new UploadImage();
                la.execute();
            } catch (Exception e) {
                Toast.makeText(AddCitizenActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Log.v(TAG, "Permission is not granted");
        }
    }

    public Bitmap ShrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);
        //Toast.makeText(BillViewActivityHcl.this, Integer.toString(bmpFactoryOptions.outHeight), Toast.LENGTH_SHORT).show();
        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    void DownloadImage() {
        if (ContextCompat.checkSelfPermission(AddCitizenActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(AddCitizenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddCitizenActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(AddCitizenActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);

        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    //****************************** Upload Image *****************************************

    class UploadImage extends AsyncTask<Void, String, String> {

        private Dialog loadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(AddCitizenActivity.this, "Uploading Photo, Please wait", "Loading...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String fileName = _imagePath;
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
//                URL url = new URL("http://192.168.1.136:3010/upload/profile");
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
                        Toast.makeText(AddCitizenActivity.this, "MalformedURLException" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(AddCitizenActivity.this, _imagePath,
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
                    Toast.makeText(AddCitizenActivity.this, "Photo Uploaded Successfully ",
                            Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, "status : " + obj1.getString("message"));
                    if (obj1.getString("success").equalsIgnoreCase("1")) {

                        postImageUrl = obj1.getString("profile-url");


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
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (position >= 0) {
            String state = arrayState.get(position);
            // Toast.makeText(AddCitizenActivity.this, state, Toast.LENGTH_SHORT).show();
            arrayDistrict.clear();
            if (jsonObject != null) {
                try {
                    JSONArray array = jsonObject.getJSONArray(state);
                    for (int i = 0; i < array.length(); i++) {
                        arrayDistrict.add(array.getString(i));
                    }
                    aaDistrict.notifyDataSetChanged();
                } catch (Exception ee) {
                }
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}