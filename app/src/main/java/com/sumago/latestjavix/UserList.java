package com.sumago.latestjavix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserList extends AppCompatActivity {
    RecyclerView userListRecyclerView ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userauthmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        UserList.RequestList req=new UserList.RequestList(this,paramHash);
        switch (id){
            case R.id.userauthmenu_approved:

                paramHash.put("email","xjilani@gmail.com");
                paramHash.put("status","1");
                paramHash.put("token","dfjkhsdfaksjfh3756237");
                paramHash.put("ngoId", Config.NGO_ID);

                req.execute(MyConfig.URL_USER_LIST);
                // END Calling NGO Task List
                return true;
            case R.id.userauthmenu_unapproved:

               paramHash.put("email","xjilani@gmail.com");
               paramHash.put("status","0");
               paramHash.put("token","dfjkhsdfaksjfh3756237");
                paramHash.put("ngoId", Config.NGO_ID);

                req.execute(MyConfig.URL_USER_LIST);
                // END Calling NGO Task List
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        userListRecyclerView=(RecyclerView) findViewById(R.id.userListRecyclerView);
        MyConfig.CONTEXT=this;
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("email","jilani.it@gmail.com");
        paramHash.put("status","1");
        paramHash.put("ngoId", Config.NGO_ID);


        UserList.RequestList req=new UserList.RequestList(this,paramHash);
        req.execute(MyConfig.URL_USER_LIST);
        // END Calling NGO Task List
    }
    //*********
    // Calling Screener LIST
    class RequestList extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestList(Activity activity,HashMap<String, String> paramsHash) {
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
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    ArrayList<UserData> recsArrayList=new ArrayList<UserData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++)
                    {
                        JSONObject rec = recsArray.getJSONObject(i);
                        UserData data=new UserData();
                        data.status=Integer.parseInt(rec.getString("status"));
                        data.id=rec.getString("userId");

                        data.email=rec.getString("email");
                        data.roleId=rec.getString("roleId");
                        Log.e("Role ID",data.roleId);
                        data.role=MyConfig.getRoleName(data.roleId);
                        JSONObject info=rec.getJSONObject("info");
                        data.name=info.getString("firstName")+" "+info.getString("lastName");
                        data.isBlocked=info.getBoolean("isBlocked");
                        data.isExpired=info.getBoolean("isExpired");
                        data.isUnActive=info.getBoolean("isUnActive");
                        recsArrayList.add(data);
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }
                   UserListAdapter adapter = new UserListAdapter(recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    userListRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    userListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){Toast.makeText(MyConfig.CONTEXT, "Exp-90:"+ee, Toast.LENGTH_SHORT).show();}

        }

    }
    // Updat CheckList Status
    static class RequestUpdate extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        View activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestUpdate(View activity,HashMap<String, String> paramsHash) {
            this.activity=activity;
            this.paramsHash=paramsHash;
            progressDialog = new ProgressDialog(activity.getContext());
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
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){Toast.makeText(MyConfig.CONTEXT, "Exp-90:"+ee, Toast.LENGTH_SHORT).show();}

        }

    }
    // ENd Update CheckList Status
}

class UserData {
    String id="";
    String name="";
    String email="";
    String role="";
    String roleId="-1";
    int status=0;
    boolean isBlocked=false;
    boolean isExpired=false;
    boolean isUnActive=false;
    public UserData() {

    }
}

class UserViewHolder extends RecyclerView.ViewHolder {
    TextView textViewName=null;
    TextView textViewId=null;
    TextView textViewRole=null;
    TextView textViewEmail=null;
    SwitchCompat switchStatus=null;
    public UserViewHolder(View itemView)
    {
        super(itemView);
        textViewName=(TextView)itemView.findViewById(R.id.userListRow_name);
        textViewRole=(TextView)itemView.findViewById(R.id.userListRow_role);
        textViewId=(TextView)itemView.findViewById(R.id.userListRow_id);
        textViewEmail=(TextView)itemView.findViewById(R.id.userListRow_email);
        switchStatus=(SwitchCompat) itemView.findViewById(R.id.userListRow_status);
    }
}

class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {
    ArrayList<UserData> recs=null;
    public UserListAdapter(ArrayList<UserData> recs) {
        this.recs = recs;
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.userlist_row, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final UserData rec = recs.get(position);
        HashMap<String,String> roleHash=new HashMap<>();
        roleHash.put("1","Doctor");
        roleHash.put("2","Screener");
        roleHash.put("3","NGO");
        roleHash.put("4","Pharmacy");
        roleHash.put("5","Doctor");
        roleHash.put("6","");
        roleHash.put("7","");
        roleHash.put("8","");
        roleHash.put("9","");
        roleHash.put("91","Javix Admin");
        roleHash.put("92","Doctor Admin");
        roleHash.put("93","Super Admin");
        roleHash.put("21","Sevika");

        holder.textViewName.setText(rec.name + "(" + roleHash.get(rec.roleId) +")");
        holder.textViewId.setText(rec.id);
        holder.textViewEmail.setText(rec.email);
        //holder.textViewRole.setText(rec.role);


        if(rec.status>0) { holder.switchStatus.setChecked(true);}
        else { holder.switchStatus.setChecked(false);}
        final UserViewHolder finalHolder=holder;

        holder.switchStatus.setTag(rec.id);
        holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CompoundButton.OnCheckedChangeListener THIS=this;
                /* COnfirmation Alert */

                AlertDialog.Builder alert = new AlertDialog.Builder(holder.switchStatus.getContext());
                alert.setTitle("Update");
                alert.setMessage("Are you sure you want to update status of  user : "+holder.switchStatus.getTag().toString()+" ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String,String> paramHash=new HashMap<String,String>();
                        paramHash.put("forUserId",holder.switchStatus.getTag().toString());
                        paramHash.put("status",finalHolder.switchStatus.isChecked()==true?"1":"0");
                        paramHash.put("isBlocked",rec.isBlocked==true?"1":"0");
                        paramHash.put("isExpired",rec.isExpired?"1":"0");
                        paramHash.put("isUnActive",rec.isUnActive?"1":"0");
                        paramHash.put("token","dfjkhsdfaksjfh3756237");
                        paramHash.put("ngoId", Config.NGO_ID);

                        UserList.RequestUpdate reqUpdate=new UserList.RequestUpdate(finalHolder.switchStatus,paramHash);
                        reqUpdate.execute(MyConfig.URL_USER_APROVING);

                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalHolder.switchStatus.setOnCheckedChangeListener(null);
                        finalHolder.switchStatus.setChecked(!finalHolder.switchStatus.isChecked());
                        finalHolder.switchStatus.setOnCheckedChangeListener(THIS);
                        dialog.dismiss();
                    }
                });

                alert.show();
                    /* ENd Confirmation Alert */

            }
        });


    }
    @Override
    public int getItemCount() {
        return recs.size();
    }
}
