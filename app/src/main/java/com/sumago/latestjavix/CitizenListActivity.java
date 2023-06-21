package com.sumago.latestjavix;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.Adapters.Constant;
import com.sumago.latestjavix.CitizenProfileActivity;
import com.sumago.latestjavix.MyConfig;
import com.sumago.latestjavix.R;
import com.sumago.latestjavix.RequestPipe;
import com.sumago.latestjavix.ScreeningPickedActivity;
import com.sumago.latestjavix.ScreeningViewActivity;
import com.sumago.latestjavix.TestListActivity;
import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class CitizenListActivity extends AppCompatActivity {
    RecyclerView citizenListRecyclerView;
    Context context;
    EditText searchBox = null;
    ArrayList<CitizenData> recsArrayList = null;
    ArrayList<CitizenData> recsFilteredArrayList = new ArrayList<CitizenData>();
    CitizenListAdapter adapterForAllData = null;
    CitizenListAdapter adapterForFilteredData = null;
    Spinner spnActor;
    Button btnSearch;
    TextView waitGifCList = null;
    private static final String TAG = "_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_list);

        MyConfig.CONTEXT = getApplicationContext();
        context = this;
        waitGifCList = (TextView) findViewById(R.id.waitGifCList);
        waitGifCList.setVisibility(View.GONE);
        citizenListRecyclerView = (RecyclerView) findViewById(R.id.citizenListRecyclerView);
        spnActor = (Spinner) findViewById(R.id.spnActor);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        ArrayAdapter<CharSequence> adp1 = ArrayAdapter.createFromResource(this, R.array.strCitizenSearch, android.R.layout.simple_spinner_item);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnActor.setAdapter(adp1);
        // Toast.makeText(this, Boolean.toString(), Toast.LENGTH_SHORT).show();
        //if(Config._roleid==2){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lnr_layout1);
        linearLayout.setVisibility(View.GONE);
        //Config.isOffline=true;
        if (Config.isOffline) {
            offlineData("");
            iniViews(Config._roleid, "");
        } else {
            iniViews(Config._roleid, "");
        }
        //}

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!spnActor.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    switch (spnActor.getSelectedItem().toString()) {
                        case "All":
                            iniViews(2, "1");
                            break;
                        case "Case Picked":
                            iniViews(2, "2");
                            break;
                        case "Prescribed":
                            iniViews(2, "3");
                            break;

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Option", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void offlineData(String strName) {
        SQLiteDatabase db;
        Cursor c;
        db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
        if (strName.length() == 0) {
            c = db.rawQuery("SELECT * FROM javix_citizenlist", null);
        } else {
            c = db.rawQuery("SELECT * FROM javix_citizenlist where name like '%" + strName + "%'", null);
        }

        //Toast.makeText(this,"Records Count :- " +  Integer.toString(c.getCount()), Toast.LENGTH_SHORT).show();
        if (c.getCount() == 0) {
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        } else {

            //Log.e(TAG,"Records :- " + c.getString(0));
            ArrayList<CitizenData> recsArrayList = new ArrayList<CitizenData>();
            try {
                JSONObject rec = null;
                while (c.moveToNext()) {
                    //Toast.makeText(this,"Records Count :- " +  c.getString(c.getColumnIndex("citizen_data")), Toast.LENGTH_SHORT).show();

                    if (c.getString(1) != null) {
                        // rec = new JSONObject(c.getString(1));

                        try {
                            CitizenData cdata = new CitizenData();
                            cdata.screenerid = c.getString(1);
                            cdata.citizenid = c.getString(2);
                            cdata.name = c.getString(3);
                            cdata.sex = c.getString(4);
                            cdata.pstatus = Integer.parseInt(c.getString(5));
                            cdata.mobile = c.getString(6);
                            cdata.email = c.getString(7);
                            cdata.caseId = c.getString(8);
                            recsArrayList.add(cdata);
                        } catch (Exception eei) {
                            System.out.println("Error 7801:" + eei.toString());
                        }
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }
                    //break;

                }// End of while
            } catch (Exception ee) {
                System.out.println("final Error80901" + ee.getLocalizedMessage());
            }
            if (recsArrayList.size() >= 1) {
                CitizenListAdapter adapter = new CitizenListAdapter(context, recsArrayList);
                // by JILANI for search_________________________________________________
                CitizenListActivity.this.adapterForAllData = adapter;
                CitizenListActivity.this.recsArrayList = recsArrayList;
                // end __________________________________________________________________
                //ngoListRecyclerView.setHasFixedSize(true);
                citizenListRecyclerView.setLayoutManager(new LinearLayoutManager(CitizenListActivity.this));
                citizenListRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //iniViews(Config._roleid, "");
            }

            db.close();
        }
    }

    public void iniViews(Integer strRole, String searchData) {
        HashMap<String, String> paramHash = new HashMap<String, String>();

        if (Config.isOffline == false) {
            paramHash.put("token", "dfjkhsdfaksjfh3756237");
            paramHash.put("ngoId", Config.NGO_ID);
            Log.e("Ruchi", "ngo_id: " + Config.NGO_ID);
            Log.e("Ruchi", "Config_Ngo_id: " + Config.NGO_ID);

            RequestCitizenList req = new RequestCitizenList(this, paramHash);
            req.execute(MyConfig.URL_LIST_CITIZEN_LAST_100);
        }
        searchBox = (EditText) findViewById(R.id.citizenList_searchBox);
        // by JILANI for search_________________________________________________
        final EditText searchBoxObject = searchBox;
        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                waitGifCList.setText("");
                String val = searchBoxObject.getText().toString();
                //Toast.makeText(getApplicationContext(),val,Toast.LENGTH_LONG).show();
                if (Config.isOffline) {
                    offlineData(val);
                } else {

                    Log.e("Response", "v: " + val);
                    paramHash.put("token", "dfjkhsdfaksjfh3756237");  // Live url token

//                    paramHash.put("token", "dfjkhsdfaksjfh375623");  // Staging url token
                    paramHash.put("v", "" + val);
                    paramHash.put("ngoId", Config.NGO_ID);
                    RequestCitizenList req = new RequestCitizenList(CitizenListActivity.this, paramHash);
                    req.execute(MyConfig.URL_LIST_CITIZEN_LAST_100);
                }
                 /* else if(CitizenListActivity.this.adapterForAllData!=null){
                   if(val.length()==0){
                        CitizenListAdapter adapter = new CitizenListAdapter(context, CitizenListActivity.this.recsArrayList);
                        CitizenListActivity.this.citizenListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        ArrayList<CitizenData> recs = CitizenListActivity.this.recsArrayList;
                        ArrayList<CitizenData> filteresRecs = CitizenListActivity.this.recsFilteredArrayList;
                        CitizenListActivity.this.recsFilteredArrayList.clear();
                        int allSize = recs.size();
                        for (int i = 0; i < allSize; i++) {
                            CitizenData rec = recs.get(i);
                            if (rec.name.toLowerCase().indexOf(val.toLowerCase()) >= 0)
                                filteresRecs.add(rec);
                        }
                        CitizenListAdapter adapter = new CitizenListAdapter(context, filteresRecs);
                        CitizenListActivity.this.citizenListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                } // if

                  */
            }
        });

        // end _________________________________________
    }

    class RequestCitizenList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public RequestCitizenList(Activity activity, HashMap<String, String> paramsHash) {
            this.activity = activity;
            this.paramsHash = paramsHash;
            waitGifCList.setVisibility(View.VISIBLE);
            waitGifCList.setText("Wait...");

            //progressDialog = new ProgressDialog(activity);
            //progressDialog.setMessage("loading");
        }

        @Override
        protected void onPreExecute() {
            //progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            return requestPipe.requestForm(params[0], paramsHash);
        }

        protected void onProgressUpdate(Void... progress) {
            //super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            ArrayList<CitizenData> recsArrayList = new ArrayList<CitizenData>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {
                    // Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    waitGifCList.setText("");
                    waitGifCList.setVisibility(View.GONE);
                    JSONObject recsData = jsonObject.getJSONObject("data");
                    JSONArray recsArray = recsData.getJSONArray("data");
                    int recsLen = recsArray.length();
                    int j = 0;
                    for (int i = 0; i < recsLen; i++) {
                        try {
                            JSONObject rec = recsArray.getJSONObject(i);
                            CitizenData cdata = new CitizenData();
                            cdata.screenerid = rec.getString("screenerId");
                            cdata.citizenid = rec.getString("citizenId");
                            cdata.name = rec.getString("firstName") + " " + rec.getString("lastName");
                            cdata.sex = rec.getString("sex");
                            cdata.pstatus = Integer.parseInt(rec.getString("pstatus"));
                            cdata.mobile = rec.getString("mobile");
                            cdata.aadhaar = rec.getString("aadhaar");
                            cdata.email = (rec.has("email") ? rec.getString("email") : "");

                            JSONObject info = rec.getJSONObject("info");
                            cdata.address = info.getString("address");
                            cdata.country = info.getString("country");
                            cdata.state = info.getString("state");
                            cdata.district = info.getString("district");

                            if (rec.has("cases")) {
                                Log.e(TAG, "CaseID: ");
                                try {
                                    JSONObject cases = rec.getJSONObject("cases");
                                    Log.e("Cases", "CaseID: " + cases);
                                    cdata.caseId = cases.getString("caseId");
                                    System.out.println("Case Id7000001=" + cdata.caseId);
                                } catch (Exception ee) {
                                    System.out.println("Error in Case9000" + ee.getLocalizedMessage());
                                    try {
                                        JSONArray cases = rec.getJSONArray("cases");
                                        if (cases != null && cases.length() >= 1) {
                                            JSONObject casesI = cases.getJSONObject(0);
                                            Log.e("Cases", "CaseID: " + cases);
                                            cdata.caseId = casesI.getString("caseId");

                                        }
                                    } catch (Exception ine) {
                                        cdata.caseId = "0";
                                        System.out.println("Cases Error :" + ine.getLocalizedMessage());
                                    }

                                }

                            } else {

                                cdata.caseId = "0";
                            }

                            cdata.dOb = info.getString("dateOfBirth");
                            cdata.pin = info.getString("pincode");
                            if (info.has("photo")) {
                                cdata.photo = info.getString("photo");
                                //Log.e("Cases", "CaseID: " +cases);
                            } else {
                                cdata.photo = "http://143.244.136.145:3010/profile/no-photo-male.jpg";
//                                cdata.photo = "http://192.168.1.136:3010/profile/no-photo-male.jpg";
//                                cdata.photo = "http://159.65.148.197:3001/profile/no-photo-male.jpg";
                            }

                            recsArrayList.add(cdata);
                        } catch (Exception eei) {
                            System.out.println("Error 7801:" + eei.toString());
                        }
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }// loop

                    Log.e("size", "size of citizen List: " + recsArrayList.size());
                } else {
                    CitizenListActivity.this.waitGifCList.setText("Not Found");
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ee) {
                CitizenListActivity.this.waitGifCList.setText("");
                System.out.println("final Error80901" + ee.getLocalizedMessage());
            }
            if (recsArrayList.size() >= 1) {
                CitizenListAdapter adapter = new CitizenListAdapter(context, recsArrayList);
                // by JILANI for search_________________________________________________
                CitizenListActivity.this.adapterForAllData = adapter;
                CitizenListActivity.this.recsArrayList = recsArrayList;
                // end __________________________________________________________________
                //ngoListRecyclerView.setHasFixedSize(true);
                citizenListRecyclerView.setLayoutManager(new LinearLayoutManager(CitizenListActivity.this));
                citizenListRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
}

class CitizenData {
    String screenerid = "";
    String citizenid = "";
    String name = "";
    String sex = "";
    String mobile = "";
    String email = "";
    String address = "";
    String district = "";
    String state = "";
    String country = "";
    String caseId = "";
    String dOb = "";
    String pin = "";
    String photo = "";
    int pstatus = 0;
    String aadhaar = "";

    public CitizenData() {

    }
}

class CitizenViewHolder extends RecyclerView.ViewHolder {
    TextView textViewNgoName = null;
    TextView textViewNgoId = null;
    TextView textViewNgoAddress = null;
    TextView textViewEmail = null;
    TextView textViewMobile = null;
    TextView textViewSex = null;
    TextView textCaseId = null;
    ImageView btnAdd = null;
    ImageView btnView = null;
    AppCompatButton btnRecent = null;
    ImageView txWatsapp = null;
    Button btnRefer;

    public CitizenViewHolder(View itemView) {
        super(itemView);
        textViewNgoName = (TextView) itemView.findViewById(R.id.screenerListRow_name);
        textViewNgoId = (TextView) itemView.findViewById(R.id.screenerListRow_id);
        textViewNgoAddress = (TextView) itemView.findViewById(R.id.screenerListRow_address);
        textViewEmail = (TextView) itemView.findViewById(R.id.screenerListRow_email);
        textViewMobile = (TextView) itemView.findViewById(R.id.screenerListRow_screenerMobileNo);
        textViewSex = (TextView) itemView.findViewById(R.id.screenerListRow_sex);
        textCaseId = (TextView) itemView.findViewById(R.id.txCaseid);
        btnAdd = (ImageView) itemView.findViewById(R.id.btnadd);
        btnView = (ImageView) itemView.findViewById(R.id.btnView);
        btnRecent = (AppCompatButton) itemView.findViewById(R.id.btnrecent);
        txWatsapp = (ImageView) itemView.findViewById(R.id.txWatsapp);

        btnRefer = (Button) itemView.findViewById(R.id.btnRefer);
    }
}

class CitizenListAdapter extends RecyclerView.Adapter<CitizenViewHolder> {
    ArrayList<CitizenData> recs = null;
    Context context;

    public CitizenListAdapter(Context context, ArrayList<CitizenData> recs) {
        this.recs = recs;
        this.context = context;

    }

    @Override
    public CitizenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.citizenlist_row, parent, false);
        CitizenViewHolder viewHolder = new CitizenViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CitizenViewHolder holder, int position) {
        final CitizenData rec = recs.get(position);
        Log.e("info", "_name: " + rec.name);
        holder.textViewNgoName.setText(rec.name);
        holder.textViewSex.setText(rec.sex);
        holder.textViewNgoId.setText(rec.citizenid);
        Log.e("pstaus", "nsme: " + rec.name + "=>" + rec.pstatus);
        String _addr = "";
        if (rec.address.length() > 0)
            _addr = rec.address + " ";
        if (rec.district.length() > 0)
            _addr += rec.district + " ";
        if (rec.state.length() > 0)
            _addr += rec.state;
        //holder.textViewNgoAddress.setText(" " + holder.textViewNgoAddress.getText() + " " +rec.state);
        //holder.textViewNgoAddress.setText(holder.textViewNgoAddress.getText() + rec.district);
        //holder.textViewNgoAddress.setText(rec.address+","+rec.district+"\n"+rec.state);
        holder.textViewNgoAddress.setText(_addr);
        _addr = "";

        //holder.textViewEmail.setText(rec.email);
        if (rec.mobile.length() == 10) {
            //holder.textViewMobile.setText(rec.mobile);
            holder.textViewMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(context, holder.txPhone.getText(),Toast.LENGTH_LONG).show();

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:+91" + rec.mobile));
                        context.startActivity(callIntent);

                    } else {
                        Toast.makeText(context, "Please grant permission manually ! ", Toast.LENGTH_LONG).show();
                   /*ActivityCompat.requestPermissions(context,
                           new String[]{android.Manifest.permission.CALL_PHONE},
                           888);*/
                    }
                }
            });

            holder.btnRecent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //rec.citizenid;
                    Intent i = new Intent(context, ScreeningPickedActivity.class);
                    context.startActivity(i);

                }
            });


            holder.txWatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    context.startActivity(sendIntent);*/

                    PackageManager packageManager = context.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    try {
                        String url = "https://api.whatsapp.com/send?phone=+91" + rec.mobile + "&text=" + URLEncoder.encode("This is test message", "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(packageManager) != null) {
                            context.startActivity(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            //holder.textViewMobile.setText("");
            //holder.textViewMobile.setVisibility(View.GONE);
            //holder.txWatsapp.setVisibility(View.GONE);
        }

        holder.textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String _email = "Email doesn't exists !";
                if (holder.textViewEmail.getText().length() > 0) {
                    _email = rec.email;
                }

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context
                );
                alertDialog.setMessage(_email);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alertDialog.create();
                alertDialog.show();

            }
        });
        holder.textViewMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String _email = "Phone doesn't exists !";
                if (rec.mobile.length() == 10) {
                    _email = rec.mobile;
                }
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context
                );
                alertDialog.setMessage(_email);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
                                PackageManager.PERMISSION_GRANTED) {
                            //Toast.makeText(context, holder.txPhone.getText(),Toast.LENGTH_LONG).show();

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:+91" + rec.mobile));
                            context.startActivity(callIntent);
                        } else {
                            Toast.makeText(context, "Please grant permission manually ! ", Toast.LENGTH_LONG).show();
                   /*ActivityCompat.requestPermissions(context,
                           new String[]{android.Manifest.permission.CALL_PHONE},
                           888);*/
                        }
                    }
                });
                alertDialog.create();
                alertDialog.show();

            }
        });
        //if(holder.textViewEmail.getText().length()==0){holder.textViewEmail.setVisibility(View.GONE);}
        if (holder.textViewSex.getText().toString().equalsIgnoreCase("Male")) {
            holder.textViewSex.setText("");
            holder.textViewSex.setBackgroundResource(R.drawable.ic_male_18dp);
        } else if (holder.textViewSex.getText().toString().equalsIgnoreCase("Female")) {
            holder.textViewSex.setText("");
            holder.textViewSex.setBackgroundResource(R.drawable.ic_female_18dp);
        }
        if (rec.pstatus >= 1) {
            //holder.textCaseId.setText(holder.textCaseId.getText() + rec.caseId);
            holder.textCaseId.setText("Screening Done");
            holder.textCaseId.setTextColor(Color.GREEN);
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnView.setVisibility(View.VISIBLE);
            //holder.textCaseId.setco
        } else {
            //holder.textCaseId.setVisibility(View.GONE);
            holder.textCaseId.setText("Screening Not Done");
            holder.textCaseId.setTextColor(Color.RED);
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnView.setVisibility(View.GONE);
        }

        // Config.isOffline=true;
        if (Config.isOffline) {
            holder.btnView.setVisibility(View.GONE);
            holder.btnRecent.setVisibility(View.GONE);
        }
        holder.textViewSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CitizenProfileActivity.class);
                i.putExtra("name", rec.name);
                i.putExtra("sex", rec.sex);
                i.putExtra("mobile", rec.mobile);
                i.putExtra("email", rec.email);
                i.putExtra("dateOfOnBoarding", rec.dOb);
                i.putExtra("state", rec.state);
                i.putExtra("district", rec.district);
                i.putExtra("address", rec.address);
                i.putExtra("pincode", rec.pin);
                i.putExtra("photo", rec.photo);
                i.putExtra("aadhaar", rec.aadhaar);
                // i.putExtra("ScreenerId",rec.caseId);
                context.startActivity(i);
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MyConfig.CONTEXT, Boolean.toString(Config.isOffline), Toast.LENGTH_SHORT).show();
                if (Config.isOffline) {
                    Intent i = new Intent(context, TestListActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Config.tmp_citizenId = holder.textViewNgoId.getText().toString();
                    Config.tmp_caseId = rec.caseId;
                    // Toast.makeText(MyConfig.CONTEXT, Config.tmp_caseId, Toast.LENGTH_SHORT).show();
                    context.startActivity(i);
                } else {
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context
                    );
                    alertDialog.setMessage("Do you want create new case !");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            Intent i = new Intent(context, TestListActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Config.tmp_citizenId = holder.textViewNgoId.getText().toString();
                            Config.tmp_caseId = "0";
                            context.startActivity(i);
                        }
                    });

                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (Config.isOffline == false) {
                                Intent i = new Intent(context, ScreeningViewActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("CitizenId", holder.textViewNgoId.getText().toString());
                                i.putExtra("ScreenerId", rec.screenerid);
                                context.startActivity(i);
                                dialog.dismiss();
                            }
                        }
                    });

                    alertDialog.create();
                    alertDialog.show();

                    /*Intent  i = new Intent(context, ScreeningViewActivity.class);
                    i.putExtra("CitizenId",holder.textViewNgoId.getText().toString());
                    i.putExtra("ScreenerId",rec.caseId);
                    context.startActivity(i);*/
                }
                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ScreeningViewActivity.class);
                i.putExtra("CitizenId", holder.textViewNgoId.getText().toString());
                i.putExtra("ScreenerId", rec.caseId);

                Log.e("Response", "CitizenId: " + holder.textViewNgoId.getText().toString());
                Log.e("Response", "CaseId: " + rec.caseId);
                context.startActivity(i);
                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Here refer button functionalities


        Log.e("Check", "checking pstatus: " + rec.pstatus);
        if (rec.pstatus >= 1) {

            holder.btnRefer.setVisibility(View.VISIBLE);

            Log.e("doctor_state", "doctor_state: " + Config.doctor);
            if (Config._roleid == 1) {
                holder.btnRefer.setVisibility(View.GONE);  // for hiding refer button from doctor
            }

            holder.btnRefer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ii = new Intent(context, RefereBySevika.class);

//                    ii.putExtra("pstatus", "1");
                    ii.putExtra("isUnrefer", 1);
                    ii.putExtra("citizenId", rec.citizenid);


                    context.startActivity(ii);


                }
            });
        }


//        else if (!Config._doctorid.isEmpty()){
//            holder.btnRefer.setVisibility(View.GONE);
//        }
        else {
            holder.btnRefer.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
}