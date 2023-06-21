package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;


public class UrineAnalysis extends AppCompatActivity {
    JSONObject jsonObject=null;
    HashMap<String,String> hashKey=new HashMap<>();
    int SC_WIDTH=380;
    int SC_HEIGHT=680;
    int SC_COL=54;
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
    HashMap<String,String> params=new HashMap<String,String>();
    Button submitHash=null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }

    public void submitHash(View v){
        // post data toMyConfig.URL_ADDURINE
        submitForm(v);
    }

    void submitForm(View v){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        // Toast.makeText(MyConfig.CONTEXT,""+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();
        Log.e("param values",params.toString());

        //params.put("caseId","162261189973320790");
        params.put("caseId", Config.tmp_caseId);
        params.put("notes","Testing");
        params.put("status","1");
        params.put("ngoId", Config.NGO_ID);

        SubmitForm req=new SubmitForm(this,params);
        req.execute(MyConfig.URL_ADDURINE);
        /*HashMap<String,String> paramHash=new HashMap<String,String>();

        paramHash.put("type", spnType.getSelectedItem().toString());
        paramHash.put("bloodglucose",bloodglucose.getText().toString());
        paramHash.put("status", "1");
        if(etNotes.getText().length()>0 ){
            paramHash.put("notes",etNotes.getText().toString());
        }
        paramHash.put("caseId", Config.tmp_caseId);
        SubmitForm req=new SubmitForm(this,paramHash);
        req.execute(MyConfig.URL_ADDURINE);*/

    }
    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String> {
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

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(UrineAnalysis.this
                    );
                    alertDialog.setMessage("Urine Test Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();


                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urine_analysis);
        submitHash=(Button)findViewById(R.id.submitUrinAny);
        submitHash.setVisibility(View.GONE);
        hashKey.put("LEU","leukocytes");
        hashKey.put("NIT","nitrite");
        hashKey.put("URO","urobilinogen");
        hashKey.put("PRO","protein");
        hashKey.put("pH","ph");
        hashKey.put("BLO","blood");
        hashKey.put("SG","specificGravity");
        hashKey.put("KET","ketone");
        hashKey.put("BIL","bilirubin");
        hashKey.put("GLU","glucose");
        try{
            Display display = getWindowManager().getDefaultDisplay(); Point sizeW = new Point(); display. getSize(sizeW);
            int width = sizeW.x; int height = sizeW.y;
            //SC_WIDTH=pxToDp(width);
            SC_WIDTH=width;
            SC_HEIGHT=height;
            SC_COL=(int)(SC_WIDTH/12); System.out.println(" SC WIDTH="+SC_WIDTH+" / "+SC_COL+ ", H="+SC_HEIGHT);
            AssetManager assetManager = getAssets();

            InputStream inputStream = assetManager.open("urindata");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            String string = new String(buffer);
            jsonObject=new JSONObject(string);
            System.out.println("JDATA:"+jsonObject.toString());
            loadGUI();
        }catch (Exception ee){System.out.println("JError:"+ee);}
    }

    void loadGUI()
    {
        LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams
                ((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsText.weight = SC_COL;


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                ((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = SC_COL;
        params.height = SC_COL;
        LinearLayout layParent = (LinearLayout) findViewById(R.id.layoutUrin);
        String B[]={"b1","b2","b3","b4","b5","b6","b7"};
        Iterator<String> itrx=jsonObject.keys();
        for(;itrx.hasNext()==true;)
        {
            String LTitle=itrx.next();
            try {
                JSONObject rec = jsonObject.getJSONObject(LTitle);
                String T1=rec.getString("t1");
                String T2=rec.getString("t2");

                LinearLayout linearLayoutY = new LinearLayout(this);
                linearLayoutY.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout linearLayoutLT = new LinearLayout(this); // X-Layout
                linearLayoutLT.setPadding(10, 10, 10, 15);
                linearLayoutLT.setOrientation(LinearLayout.VERTICAL);

                TextView textViewLT = new TextView(this);
                textViewLT.setText(LTitle);
                textViewLT.setTextSize(12);
                textViewLT.setLayoutParams(paramsText);
                linearLayoutLT.addView(textViewLT);

                TextView textViewLT2 = new TextView(this);
                textViewLT2.setText("  ");
                textViewLT2.setTextSize(12);
                textViewLT2.setLayoutParams(paramsText);
                linearLayoutLT.addView(textViewLT2);

                linearLayoutY.addView(linearLayoutLT);

                LinearLayout linearLayoutTT = new LinearLayout(this); // X-Layout
                linearLayoutTT.setPadding(10, 10, 10, 15);
                linearLayoutTT.setOrientation(LinearLayout.VERTICAL);
                TextView textViewTT = new TextView(this);
                textViewTT.setText(T1);
                textViewTT.setTextSize(8);
                textViewTT.setLayoutParams(paramsText);
                linearLayoutTT.addView(textViewTT);

                TextView textViewTT2 = new TextView(this);
                textViewTT2.setText(T2);
                textViewTT2.setTextSize(8);
                textViewTT2.setLayoutParams(paramsText);
                linearLayoutTT.addView(textViewTT2);
                linearLayoutY.addView(linearLayoutTT);

                for(String b:B) {
                    String buttonTitle = "";
                    String buttonColor = "#FFFFFF";
                    try {
                        JSONObject boxJSon = rec.getJSONObject(b);

                        if (boxJSon != null) {
                            buttonTitle = boxJSon.getString("t1") ;
                            buttonColor = boxJSon.getString("color");
                        }
                    }catch (Exception je){}
                    LinearLayout linearLayout = new LinearLayout(this); // X-Layout
                    linearLayout.setPadding(10, 10, 10, 15);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    TextView textView1 = new TextView(this);
                    textView1.setText(buttonTitle);
                    textView1.setTextSize(5);
                    textView1.setLayoutParams(paramsText);
                    LinearLayout.LayoutParams paramsUpdate = (LinearLayout.LayoutParams) textView1.getLayoutParams();
                    paramsUpdate.width = SC_COL;
                    linearLayout.addView(textView1);

                    Button button1 = new Button(this);
                    button1.setText("");
                    button1.setBackgroundColor(Color.parseColor(buttonColor));
                    button1.setLayoutParams(params);
                    paramsUpdate = (LinearLayout.LayoutParams) button1.getLayoutParams();
                    paramsUpdate.width = SC_COL;
                    linearLayout.addView(button1);
                    button1.setTag(LTitle+":"+buttonTitle+":"+buttonColor);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String VAL=v.getTag().toString();
                            String cols[]=VAL.split(":");
                           //Toast.makeText(UrineAnalysis.this,"SEL VAL:"+VAL,Toast.LENGTH_SHORT).show();

                            if(cols!=null && cols.length==3){
                                textViewLT2.setBackgroundColor(Color.parseColor(cols[2]));
                                textViewLT2.setText("Result");
                                textViewLT2.setTextSize(10);
                                textViewLT2.setTextColor(Color.WHITE);

                                cols[0]=UrineAnalysis.this.hashKey.get(cols[0]);
                                if(UrineAnalysis.this.params.containsKey(cols[0])==true){
                                    UrineAnalysis.this.params.remove(cols[0]);
                                }
                                UrineAnalysis.this.params.put(cols[0],cols[1]);
                            }
                            UrineAnalysis.this.submitHash.setVisibility(View.VISIBLE);
                        }
                    });
                    linearLayoutY.addView(linearLayout);
                } // Button Loop

                layParent.addView(linearLayoutY);
            }catch (Exception wee){ System.out.println(" JERRORX:"+wee);}
        } // loop
    }
}