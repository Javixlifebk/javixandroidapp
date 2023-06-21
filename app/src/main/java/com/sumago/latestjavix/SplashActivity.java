package com.sumago.latestjavix;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import java.util.HashMap;
import java.util.Map;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    public static final int PERMISSION_ALL = 7;
    public  boolean PERMISSION_DONE=false;
    Button bottom_btn;
    Context context;
    Switch swOffline;
    SQLiteDatabase db;
    final static String[] PERMISSIONS = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MEDIA_CONTENT_CONTROL,
            Manifest.permission.INTERNET,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_splash);
        setContentView(R.layout.activity_splash_update);
        //grantPermissions();
        mVisible = true;
        context=this;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bottom_btn = (Button) findViewById(R.id.bottom_btn);
        bottom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                swOffline=(Switch)findViewById(R.id.swOffline);
                if(swOffline.isChecked()) {

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context
                    );
                    alertDialog.setMessage(""+  getString(R.string._offline_he));
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Config.isOffline = true;
                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(i);
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(),"Error " + ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alertDialog.create();
                    alertDialog.show();
                }else{
                    Config.isOffline=false;

                }
                ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, PERMISSION_ALL);

            }
        });

    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        //mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar

        mVisible = true;

        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    void grantPermissions() {
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);

        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {

            for (String permission : permissions) {
                Toast.makeText(context, permission,Toast.LENGTH_LONG).show();
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }else{
            Toast.makeText(context, "else",Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with all permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.MEDIA_CONTENT_CONTROL, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.MANAGE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                //perms.put(Manifest.permission.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

                            )

                            {
                                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                                db.execSQL("CREATE TABLE IF NOT EXISTS javix_update(_id INTEGER PRIMARY KEY AUTOINCREMENT,service_type VARCHAR,service_name VARCHAR,service_data VARCHAR,data_json VARCHAR,service_image VARCHAR,insert_date DATETIME,_status INTEGER);");
                                db.execSQL("CREATE TABLE IF NOT EXISTS javix_login(_id INTEGER PRIMARY KEY AUTOINCREMENT,fname VARCHAR,lname VARCHAR,email VARCHAR,pwd VARCHAR,userId VARCHAR,roleId INTEGER,javixid VARCHAR,_status INTEGER);");
                                //db.execSQL("CREATE TABLE IF NOT EXISTS javix_citizenlist(_id INTEGER PRIMARY KEY AUTOINCREMENT,citizen_data TEXT,insert_date DATETIME,_status INTEGER);");
                                db.execSQL("CREATE TABLE IF NOT EXISTS javix_citizenlist(_id INTEGER PRIMARY KEY AUTOINCREMENT,screenerid VARCHAR,citizenid VARCHAR,name VARCHAR,sex VARCHAR,pstatus INTEGER,mobile VARCHAR,email VARCHAR,caseId VARCHAR,insert_date DATETIME,_status INTEGER);");
                                swOffline=(Switch)findViewById(R.id.swOffline);
                                if(swOffline.isChecked()) {
                                      Config.isOffline = true;

                                }else{
                                    Config.isOffline=false;
                                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }




                                //Toast.makeText(getApplicationContext(),Integer.toString(grantResults.length) + ":" + Integer.toString(permissions.length),Toast.LENGTH_LONG).show();
                        //Log.d("mylog", "service permission granted");
                       /* if (hasPermissions(SplashActivity.this, PERMISSIONS)) {
                            Toast.makeText(SplashActivity.this, "All Permissions Granted",
                                    Toast.LENGTH_LONG).show();
                            PERMISSION_DONE=true;
                        } else {
                            Toast.makeText(SplashActivity.this, "Please grant all the required permissions",
                                    Toast.LENGTH_LONG).show();
                        }
                        return;*/
                    }
                }
            }
        }
    }
}