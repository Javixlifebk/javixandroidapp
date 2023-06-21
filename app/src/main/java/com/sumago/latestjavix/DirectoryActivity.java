package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DirectoryActivity extends AppCompatActivity {
    RecyclerView directoryListRecyclerView ;
    private ArrayList<DirectoryData> directoryData;
    ArrayList<DirectoryData> recsFilteredArrayList=new ArrayList<DirectoryData>();
    private static final String TAG = "_msg";
    DirectoryListAdapter adapter;
    DirectoryListAdapter adapterForFilteredData=null;
    Context context;
    EditText searchBox=null;
    Spinner spnActor;
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        spnActor=(Spinner)findViewById(R.id.spnActor);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        ArrayAdapter<CharSequence> adp1= ArrayAdapter.createFromResource(this, R.array.strActors, android.R.layout.simple_spinner_item);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnActor.setAdapter(adp1);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!spnActor.getSelectedItem().toString().equalsIgnoreCase("Select Actors")){
                    switch (spnActor.getSelectedItem().toString()){
                        case "All":
                            iniViews("0");
                            break;
                        case "Doctor":
                            iniViews("1");
                            break;
                        case "Screener":
                            iniViews("2");
                            break;
                        case "Ngo":
                            iniViews("3");
                            break;
                        case "Sevika":
                            iniViews("21");
                            break;
                        case "Pharmacy":
                            iniViews("4");
                            break;
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Please Select Actors",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void iniViews(String roleId){
        directoryListRecyclerView = (RecyclerView) findViewById(R.id.directoryListRecyclerView);
        directoryListRecyclerView.setHasFixedSize(true);
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("email","jilani.it@gmail.com");
        paramHash.put("status","1");
        paramHash.put("ngoId", Config.NGO_ID);

        if(!roleId.equalsIgnoreCase("0")) {
            paramHash.put("roleId", roleId);
        }
        DirectoryList req=new DirectoryList(this,paramHash);
        req.execute(MyConfig.URL_USER_LIST);
        searchBox=(EditText)findViewById(R.id.citizenList_searchBox);

        // by JILANI for search_________________________________________________
        final EditText searchBoxObject=searchBox;
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String val=searchBoxObject.getText().toString();
                if(DirectoryActivity.this.directoryData!=null){
                    if(val.length()==0){
                        DirectoryListAdapter adapter = new DirectoryListAdapter(context, DirectoryActivity.this.directoryData);
                        DirectoryActivity.this.directoryListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        ArrayList<DirectoryData> recs = DirectoryActivity.this.directoryData;
                        ArrayList<DirectoryData> filteresRecs = DirectoryActivity.this.recsFilteredArrayList;
                        DirectoryActivity.this.recsFilteredArrayList.clear();
                        int allSize = recs.size();
                        for (int i = 0; i < allSize; i++) {
                            DirectoryData rec = recs.get(i);
                            if (rec._name.toLowerCase().indexOf(val.toLowerCase()) >= 0)
                                filteresRecs.add(rec);
                        }
                        DirectoryListAdapter adapter = new DirectoryListAdapter(context, filteresRecs);
                        DirectoryActivity.this.directoryListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // end _________________________________________
    }
    class DirectoryList extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public DirectoryList(Activity activity,HashMap<String, String> paramsHash) {
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
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    //ArrayList<DirectoryData> recsArrayList=new ArrayList<DirectoryData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    directoryData=new ArrayList<>();
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++){
                        JSONObject rec = recsArray.getJSONObject(i);
                        JSONObject info=rec.getJSONObject("info");
                        directoryData.add(new DirectoryData(rec.getString("roleId"),info.getString("firstName") + " " + info.getString("lastName"),rec.getString("email"),info.getString("phoneNo")));

                        //Log.e(TAG, "data : " +"d");
                        /*DirectoryData dirdata=new DirectoryData();
                        dirdata._role=rec.getString("roleId");
                        dirdata._email=rec.getString("email");
                        JSONObject info=rec.getJSONObject("info");
                        dirdata._name=info.getString("firstName") + " " + info.getString("lastName");
                        dirdata._phone=info.getString("phoneNo");*/
                        //recsArrayList.add(dirdata);
                        //Toast.makeText(MyConfig.data.state,Toast.LENGTH_SHORT).show();
                    }

                    adapter = new DirectoryListAdapter(context,directoryData);

                    // by JILANI for search_________________________________________________
                    DirectoryActivity.this.adapter=adapter;
                    DirectoryActivity.this.directoryData=directoryData;
                    // end __________________________________________________________________
                    //ngoListRecyclerView.setHasFixedSize(true);
                    directoryListRecyclerView.setLayoutManager(new LinearLayoutManager(DirectoryActivity.this));
                    directoryListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                //DirectoryListAdapter
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }*/
}
class DirectoryData {
    String _id="";
    String  _name="";
    String _role="";
    String _phone="";
    String _email="";

    public DirectoryData(String _id,String _name,String _email,String _phone) {
        this._id=_id;
        this._name=_name;
        this._role=_role;
        this._phone=_phone;
        this._email=_email;
    }
}

class DirectoryViewHolder extends RecyclerView.ViewHolder {
    TextView txId=null;
    TextView txName=null;
    TextView txPhone=null;
    TextView txEmail=null;
    ImageView imgCall=null;
    ImageView txWatsapp=null;

    public DirectoryViewHolder(View itemView){
        super(itemView);
        txName=(TextView)itemView.findViewById(R.id.txName);
        txPhone=(TextView)itemView.findViewById(R.id.txPhone);
        txEmail=(TextView)itemView.findViewById(R.id.txEmail);
        imgCall=(ImageView)itemView.findViewById(R.id.imgCall);
        txWatsapp=(ImageView) itemView.findViewById(R.id.txWatsapp);
    }
}

class DirectoryListAdapter extends RecyclerView.Adapter<DirectoryViewHolder>{
    ArrayList<DirectoryData> recs=null;
    ArrayList<DirectoryData> allList;
    Context context;
    public DirectoryListAdapter(Context context, ArrayList<DirectoryData> recs) {
        this.recs = recs;
        this.allList=recs;
        //allList=new ArrayList<>(recs);
        this.context = context;
    }
    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.recycler_directory, parent, false);
        DirectoryViewHolder viewHolder = new DirectoryViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        HashMap<String,String> roleHash=new HashMap<String,String>();
        roleHash.put("1","Doctor");
        roleHash.put("2","Screener");
        roleHash.put("3","NGO");
        roleHash.put("4","Pharmacy");
        roleHash.put("5","Pathalogy");
        roleHash.put("6","Citizen");
        roleHash.put("21","Sevika");
        roleHash.put("91","System Admin");
        roleHash.put("92","Doctor Admin");
        roleHash.put("99","Controller Admin");
        final DirectoryData rec = recs.get(position);
        holder.txName.setText(rec._name + "(" + roleHash.get( rec._id) + ")");
        holder.txEmail.setText(rec._email);
        holder.txPhone.setText(rec._phone);

        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED)
                {
                    //Toast.makeText(context, holder.txPhone.getText(),Toast.LENGTH_LONG).show();

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+91" + holder.txPhone.getText()));
                    context.startActivity(callIntent);
                }
               else{
                   Toast.makeText(context,"Please grant permission manually ! ",Toast.LENGTH_LONG).show();
                   /*ActivityCompat.requestPermissions(context,
                           new String[]{android.Manifest.permission.CALL_PHONE},
                           888);*/
               }
                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9934312075"));
                if (ActivityCompat.checkSelfPermission(context,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(,
                            android.Manifest.permission.CALL_PHONE)) {
                    } else {
                        ActivityCompat.requestPermissions(context,
                                new String[]{android.Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                }
                context.startActivity(callIntent);*/

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
                    String url = "https://api.whatsapp.com/send?phone=+91"+ holder.txPhone.getText() +"&text=" + URLEncoder.encode("This is test message", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        context.startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
   /* @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DirectoryData> filterList=new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filterList.addAll(allList);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (DirectoryData dlist:allList){
                    if(dlist._name.contains(filterPattern)){
                        Log.e("Filter _name ", dlist.toString());
                        filterList.add(dlist);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values=filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //allList.clear();
            //allList.addAll((Collection<? extends DirectoryData>)filterResults.values);
            Log.e("Filter Restult",charSequence.toString() +"::=" + filterResults.toString());

            allList.addAll((List)filterResults.values);
            notifyDataSetChanged();
            //allList.clear();

        }
    };*/
}
