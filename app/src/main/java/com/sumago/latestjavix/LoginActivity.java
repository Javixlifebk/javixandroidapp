package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "_msg";
    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    Context context;
    HttpURLConnection conn;
    HashMap hasData;
    //Shared preferences
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Pwd = "nameKey";
    public static final String Email = "emailKey";


//    public static final String Pwd = "password";
//    public static final String Email = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login_update);

        context = this;
        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_btn);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Config.isOffline) {
                    try {
                        SQLiteDatabase db;
                        db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                        Cursor c = db.rawQuery("SELECT * FROM javix_login where email='" + emailid.getText().toString() + "' and pwd='" + password.getText().toString() + "'", null);
                        if (c.getCount() == 0) {
                            Toast.makeText(getApplicationContext(), "Invalid Login !", Toast.LENGTH_LONG).show();
                            //String SqlStr = "INSERT INTO javix_login(email,pwd,userId,roleId,javixid,_status)";

                        } else {
                            c.moveToNext();
                            Config._roleid = Integer.parseInt(c.getString(c.getColumnIndex("roleId")));
//                            Toast.makeText(getApplicationContext(),c.getString(1),Toast.LENGTH_LONG).show();
                            //Config._uid = c.getString(c.getColumnIndex("uid"));
                            Config._fname = c.getString(c.getColumnIndex("fname"));
                            Config._lname = c.getString(c.getColumnIndex("lname"));
                            Config._email = c.getString(c.getColumnIndex("email"));
                            //Config._token =c.getString(c.getColumnIndex("uid"));
                            Intent i;
                            switch (Config._roleid) {
                                case 91:
                                    i = new Intent(LoginActivity.this, AdminActivity.class);
                                    finish();
                                    startActivity(i);
                                    break;
                                case 1:
                                    Config._doctorid = c.getString(c.getColumnIndex("javixid"));
                                    i = new Intent(LoginActivity.this, DoctorActivity.class);
                                    finish();
                                    startActivity(i);
                                    break;
                                case 2:
                                    //Config._screenerid = c.getString(c.getColumnIndex("javixid"));
                                    i = new Intent(LoginActivity.this, ScreenerActivity.class);
                                    finish();
                                    startActivity(i);
                                    break;
                                case 3:
                                    Config._nogid = c.getString(c.getColumnIndex("javixid"));
                                    i = new Intent(LoginActivity.this, NgoActivity.class);
                                    finish();
                                    startActivity(i);
                                    break;
                                case 21:
                                    //Config._screenerid = c.getString(c.getColumnIndex("javixid"));
                                    Log.d(TAG, "onClick: "+Config._roleid);
                                    i = new Intent(LoginActivity.this, SevikaActivity.class);
                                    finish();
                                    startActivity(i);
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "Invalid Role", Toast.LENGTH_LONG).show();
                            }
                        }

                        db.close();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Error !" + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                } else {

                    if (isInternetOn(context)) {

                        Config._targetUrl = "http://143.244.136.145:3010/api/"; //Live IP
//                        Config._targetUrl = "http://192.168.1.195:3010/api/";
//                        Config._targetUrl = "http://159.65.148.197:3001/api/";  // Staging url
//                        Config._targetUrl = "http://192.168.1.7:3000/api/auth/login?=";  // local IP for testing purpose
                        Save(v);
                        downloadJSON(Config._targetUrl + "auth/login", emailid.getText().toString(), password.getText().toString());

                    } else {
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(LoginActivity.this
                        );
                        alertDialog.setMessage("No Internet Connection !");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                return;
                            }
                        });
                        alertDialog.create();
                        alertDialog.show();
                    }
                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(i);
            }
        });
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        if (isChecked) {

                            show_hide_password.setText("Hide Password");// change
                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText("Show Password");// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Email)) {
            emailid.setText(sharedpreferences.getString(Email, ""));
        }
        if (sharedpreferences.contains(Pwd)) {
            password.setText(sharedpreferences.getString(Pwd, ""));

        }
    }

    public boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
    }

    private void downloadJSON(final String urlWebService, final String _uid, final String _pwd) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();

                try {
                    getJSONData(s);
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.e(TAG, "json err : " + e.getMessage());
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL(urlWebService);
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
                    result.append(URLEncoder.encode("email", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(_uid, "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("password", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(_pwd, "UTF-8"));
                    writer.write(result.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.e(TAG, "targeturl: " + urlWebService);
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
                    } else {
                        Log.e(TAG, "else part : " + sb);
                        return "{\"status\":0,\"message\":\"Login Failed.\"}";
                    }


                    return sb.toString().trim();
                } catch (Exception e) {
                    Log.e(TAG, "exception : " + e.getMessage());

                    return "{\"status\":0,\"message\":\"Login Failed.\"}";
                } finally {
                    conn.disconnect();
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void getJSONData(String json) throws JSONException {
        String jsonStr = json;
        int _status = 0, roleId = 0;
        if (jsonStr != null) {
            try {
                //String in;
                JSONObject obj1 = new JSONObject(jsonStr);
                //Log.e(TAG, "status : " + obj1.getString("message"));
                if (obj1.getString("status").equalsIgnoreCase("1")) {
                    JSONObject obj2 = new JSONObject(obj1.getString("data"));
                    JSONObject obj3 = new JSONObject(obj2.getString("data"));
                    hasData = new HashMap<>();
                    hasData.put("_status", obj1.getString("status"));
                    hasData.put("_message", obj1.getString("message"));
                    hasData.put("_fname", obj3.getString("firstName"));
                    hasData.put("_lname", obj3.getString("lastName"));
                    hasData.put("_email", obj3.getString("email"));
                    hasData.put("_uid", obj3.getString("userId"));
                    hasData.put("_roleid", obj3.getString("roleId"));
                    hasData.put("javixid", obj3.getString("javixid"));
                    hasData.put("_token", obj3.getString("token"));

                    hasData.put("ngoId", Config.NGO_ID);

                    Config.NGO_ID = obj3.getString("ngoId");
                    Config._screenerid = obj3.getString("javixid");
                    Log.e("Ruchi", "ngo_id: " + Config.NGO_ID);


                    if (obj3.getString("javixid") == null) {
                        Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                        i.putExtra("_fname", obj3.get("firstName").toString());
                        i.putExtra("_fname", obj3.get("firstName").toString());
                        i.putExtra("_fname", obj3.get("firstName").toString());
                        startActivity(i);
                    }

                    //put data in confi file
                    Config._roleid = Integer.parseInt(hasData.get("_roleid").toString());
                    Config._uid = hasData.get("_uid").toString();
                    Config._fname = hasData.get("_fname").toString();
                    Config._lname = hasData.get("_lname").toString();
                    Config._email = hasData.get("_email").toString();
                    Config._token = hasData.get("_token").toString();
                    Config.javixid = hasData.get("javixid").toString();
                    Log.d(TAG, "getJSONData: "+Config._roleid);
                    _status = Integer.parseInt(hasData.get("_status").toString());
                    try {

                        SQLiteDatabase db;
                        db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                        Cursor c = db.rawQuery("SELECT * FROM javix_login", null);

                        //if (c.getCount() == 0) {
                        //Toast.makeText(getApplicationContext(),"Count !" ,Toast.LENGTH_LONG).show();
                        String SqlStr = "INSERT INTO javix_login(email,pwd,userId,roleId,javixid,fname,lname,_status)";
                        SqlStr += " VALUES('" + hasData.get("_email").toString() + "','" + password.getText().toString() + "',";
                        SqlStr += "'" + hasData.get("_uid").toString() + "','" + hasData.get("_roleid").toString() + "',";
                        SqlStr += "'" + hasData.get("javixid").toString() + "','" + hasData.get("_fname").toString() + "','" + hasData.get("_lname").toString() + "',1)";
                        //SqlStr += " VALUES('" + hasData.get("_email").toString() + "','" + password.getText().toString() + "','" + hasData.get("_uid").toString() + "'," + hasData.get("_roleid").toString() + ",'" + hasData.get("javixid").toString() + "',1);";
                        //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                        Log.e("Offline", SqlStr);
                        db.execSQL(SqlStr);
                        //}
                        db.close();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Error !" + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (Config._roleid == 1) {
                        submitForm();
                    }

                } else {
                    hasData = new HashMap<>();
                    hasData.put("_status", obj1.getString("status"));
                    hasData.put("_message", obj1.getString("message"));
                    _status = Integer.parseInt(hasData.get("_status").toString());
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

        if (_status == 1) {
            roleId = Config._roleid;
            Log.e("Response", "roleId: " + roleId);
            Intent i;
            switch (roleId) {
                case 91:
                    i = new Intent(LoginActivity.this, AdminActivity.class);
                    finish();
                    startActivity(i);
                    break;
                case 1:
                    i = new Intent(LoginActivity.this, DoctorActivity.class);
                    finish();
                    startActivity(i);
                    break;
                case 3:
                    i = new Intent(LoginActivity.this, NgoActivity.class);
                    finish();
                    startActivity(i);
                    break;
                case 2:

                    if (Config.javixid.equalsIgnoreCase("") || Config.javixid == null) {

                        Toast.makeText(context, "Please complete profile", Toast.LENGTH_SHORT).show();

                    } else {
                        i = new Intent(LoginActivity.this, ScreenerActivity.class);
                        Log.e("Ruchi", "screener_id: " + Config._screenerid);
                        Log.e("Ruchi", "JavixId: " + Config.javixid);
                        finish();
                        startActivity(i);
                    }


                    break;
                case 21:

                    i = new Intent(LoginActivity.this, SevikaActivity.class);
                    finish();
                    startActivity(i);
                    break;
                case 4:
                    i = new Intent(LoginActivity.this, PharmacyActivity.class);
                    finish();
                    startActivity(i);
                    break;
                case 5:
                    i = new Intent(LoginActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
                    break;

                default:
                    Toast.makeText(getApplicationContext(), "Invalid Role", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), hasData.get("_message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    void submitForm() {
        HashMap<String, String> paramHash = new HashMap<String, String>();
        paramHash.put("doctorId", Config._uid);
        paramHash.put("loggedIn", "1");
        SubmitForm req = new SubmitForm(this, paramHash);

        req.execute("http://143.244.136.145:3010/api/login/updatelogindoc");
//        req.execute("http://192.168.1.195:3010/api/login/updatelogindoc");// Loccaall host aapi for tesstingg
//        req.execute("http://159.65.148.197:3001/api/login/updatelogindoc"); // Staging URL for testing
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

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {
                    Toast.makeText(getApplicationContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
                Toast.makeText(getApplicationContext(), "Exception !.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Save(View view) {
        String n = emailid.getText().toString();
        String e = password.getText().toString();

        Log.e("Login", "Email: " + n + " " + " Password: " + e);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Email, n);
        editor.putString(Pwd, e);
        editor.commit();
    }

    public void clear(View view) {
        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        emailid.setText("");
        password.setText("");

    }

    public void Get(View view) {
        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Email)) {
            emailid.setText(sharedpreferences.getString(Email, ""));
        }
        if (sharedpreferences.contains(Pwd)) {
            password.setText(sharedpreferences.getString(Pwd, ""));

        }
    }

}