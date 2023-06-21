package com.sumago.latestjavix;

import static com.sumago.latestjavix.WebService.Constant.LINK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestListActivity extends AppCompatActivity {

    CardView crd_vital,crd_test,crd_symptom,crd_report,crd_report1,crd_report2,crd_exam,crd_Heart,crd_Lung,crd_precription, crd_allTests, crd_breast_examination;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }

    public void initView(){
        crd_vital=(CardView)findViewById(R.id.crd_vital);
        crd_test=(CardView)findViewById(R.id.crd_test);
        crd_symptom=(CardView)findViewById(R.id.crd_symptom);
        crd_report=(CardView)findViewById(R.id.crd_report);
        crd_report1=(CardView)findViewById(R.id.crd_report1);
        crd_report2=(CardView)findViewById(R.id.crd_report2);
        crd_exam=(CardView)findViewById(R.id.crd_exam);
        crd_Heart=(CardView)findViewById(R.id.crd_Heart);
        crd_Lung=(CardView)findViewById(R.id.crd_Lung);
        crd_precription=(CardView)findViewById(R.id.crd_precription);
        crd_allTests=(CardView)findViewById(R.id.crd_allTests);
        crd_breast_examination=(CardView)findViewById(R.id.crd_breast_examination);
        //crd_precription.setVisibility(View.GONE);

        if(Config.isOffline){
            crd_Heart.setVisibility(View.GONE);
            crd_report.setVisibility(View.GONE);
            crd_report1.setVisibility(View.GONE);
            crd_report2.setVisibility(View.GONE);
            crd_allTests.setVisibility(View.GONE);
            crd_breast_examination.setVisibility(View.GONE);

        }


        if (Config._roleid == 1 || Config._roleid == 2){
            crd_breast_examination.setVisibility(View.VISIBLE);
        }else {
            crd_breast_examination.setVisibility(View.GONE);

        }

        if (Config.recent_investigation == 1){
            crd_breast_examination.setVisibility(View.GONE);
        }

        Log.e("Response", "TestListActivity_referredClick: " + Config.referredClick);
        if (Config.referredClick == 1){
            crd_breast_examination.setVisibility(View.GONE);
            //Toast.makeText(this, "crd_breast_examination visibility gone ", Toast.LENGTH_SHORT).show();
        }

      try {
          if(Config.tmp_Pid!=null) {
              if (Config.tmp_Pid.equalsIgnoreCase("1")) {
                  crd_precription.setVisibility(View.VISIBLE);
                  crd_vital.setVisibility(View.GONE);
                  crd_test.setVisibility(View.GONE);
                  crd_symptom.setVisibility(View.GONE);
                  crd_exam.setVisibility(View.GONE);
                  crd_Heart.setVisibility(View.GONE);
                  crd_Lung.setVisibility(View.GONE);
                  crd_report1.setVisibility(View.GONE);

                  Config.tmp_Pid = "0";
              }
          }

      }catch (Exception e){
          Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
      }

        if(Config._roleid==21){
            crd_test.setVisibility(View.GONE);
            crd_exam.setVisibility(View.GONE);
            crd_Heart.setVisibility(View.GONE);
            crd_Lung.setVisibility(View.GONE);
            crd_report2.setVisibility(View.VISIBLE);
            crd_allTests.setVisibility(View.GONE);
        }

        if (Config._roleid == 2 || Config._roleid == 91 || Config._roleid == 3 || Config._roleid == 4 || Config._roleid == 5){
            crd_allTests.setVisibility(View.GONE);
        }

        bundle=getIntent().getExtras();
        crd_vital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(Config.tmp_caseId.equalsIgnoreCase("0")) {

                    Intent i = new Intent(TestListActivity.this, ScreeningActivity.class);
                    // i.putExtra("CitizenId", bundle.getString("CitizenId"));
                    //i.putExtra("ScreenerId", bundle.getString("ScreenerId"));
                    finish();
                    startActivity(i);
               /* }
                else{
                    Toast.makeText(getApplicationContext(),"Screening is already Done !",Toast.LENGTH_LONG).show();
                }*/
            }
        });



        crd_Lung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TestListActivity.this, LungFuncation.class);

                if(!Config.tmp_caseId.equalsIgnoreCase("0")){
                    finish();
                    startActivity(i);}else{
                    Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                }

            }
        });

        crd_Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TestListActivity.this, HeartActivity.class);
                if(!Config.tmp_caseId.equalsIgnoreCase("0")){
                    finish();
                    startActivity(i);}else{
                    Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                }

            }
        });

        crd_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(TestListActivity.this, LabTestActivity.class);

                if(!Config.tmp_caseId.equalsIgnoreCase("0")){
                    finish();
                    startActivity(i);}else{
                        Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                    }

            }
        });

        crd_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TestListActivity.this, VisualExam.class);
                //finish();
                //startActivity(i);

                if(!Config.tmp_caseId.equalsIgnoreCase("0")){
                    finish();
                    startActivity(i);
            }else{
                    Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                }

            }
        });

        crd_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TestListActivity.this,symptomslist.class);
                i.putExtra("CaseId", Config.tmp_caseId);
                //Toast.makeText(MyConfig.CONTEXT,"Case ID !" + Integer.toString(Config.tmp_caseId.length()),Toast.LENGTH_SHORT).show();
                if(!Config.tmp_caseId.equalsIgnoreCase("0")){
                    finish();
                    startActivity(i);}else{
                    Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                }
            }
        });
        crd_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Config.tmp_caseId.equalsIgnoreCase("0")) {
                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "citizenId=" + Config.tmp_citizenId);
                    Log.e("Response", "citizenId: " +Config.tmp_citizenId);
                    String url = "http://143.244.136.145:3010/api/report/createHistoryReport";
//                    String url = "http://159.65.148.197:3001/api/report/createHistoryReport";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .addHeader("postman-token", "08b43f25-b3a6-b582-35c8-25cfdad00694")
                            .build();
                    System.out.println("OKAY CLIENT");
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("JCANCEL " + e.toString());
                            call.cancel();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String myResponse = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(myResponse);
                                Log.e("Response", "jsonObject: " +jsonObject);
                                if (jsonObject.getInt("status") == 1) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    data = data.getJSONObject("data");
                                    String fileName = data.getString("filename");
                                    Log.e("Response", "fileName: " +fileName);
                                    Intent intx = new Intent(Intent.ACTION_VIEW);
                                    intx.setData(Uri.parse(fileName));
                                    intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MyConfig.CONTEXT.startActivity(intx);
                                }
                            } catch (Exception ee) {
                            }
                        }
                    });
                }else{
                    Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                }

            }
        });
       // crd_report2.setVisibility(View.GONE);
        crd_report1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Config.tmp_caseId.equalsIgnoreCase("0")) {
                    /*Intent i = new Intent(TestListActivity.this, CommonReport.class);
                    i.putExtra("CaseId", Config.tmp_caseId);
                    startActivity(i);*/
                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "caseId=" + Config.tmp_caseId + "&ngoId=" + Config.NGO_ID);
                    Log.e("Response", "(TestListActivity)caseId: " +Config.tmp_caseId);
                    String url = "http://143.244.136.145:3010/api/report/createCaseReport";
//                    String url = "http://159.65.148.197:3001/api/report/createCaseReport";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .addHeader("postman-token", "08b43f25-b3a6-b582-35c8-25cfdad00694")
                            .build();
                    System.out.println("OKAY CLIENT");
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("JCANCEL " + e.toString());
                            call.cancel();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String myResponse = response.body().string();
                            Log.e("Response", "(TestListActivity)myResponse: " +myResponse);
                            try {
                                JSONObject jsonObject = new JSONObject(myResponse);
                                Log.e("Response", "(TestListActivity)jsonObject: " +jsonObject);
                                if (jsonObject.getInt("status") == 1) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    Log.e("Response", "(TestListActivity)data: " +data);
                                    data = data.getJSONObject("data");
                                    String fileName = data.getString("filename");
                                    Intent intx = new Intent(Intent.ACTION_VIEW);
                                    intx.setData(Uri.parse(fileName));
                                    intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MyConfig.CONTEXT.startActivity(intx);
                                }
                            } catch (Exception ee) {
                            }
                        }
                    });
                }else{
                    Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                }
            }
        });
        crd_report2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Config.tmp_caseId.equalsIgnoreCase("0")) {

                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "caseId=" + Config.tmp_caseId + "&ngoId=" + Config.NGO_ID);
                    Log.e("Response","Prescription Case Id : "+Config.tmp_caseId);
                    String url = LINK+"report/createPrescriptionReport";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .addHeader("postman-token", "08b43f25-b3a6-b582-35c8-25cfdad00694")
                            .build();
                    System.out.println("OKAY CLIENT");
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("JCANCEL " + e.toString());
                            call.cancel();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String myResponse = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(myResponse);
                                if (jsonObject.getInt("status") == 1) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    data = data.getJSONObject("data");
                                    String fileName = data.getString("filename");
                                    Intent intx = new Intent(Intent.ACTION_VIEW);
                                    intx.setData(Uri.parse(fileName));
                                    intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MyConfig.CONTEXT.startActivity(intx);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Prescription is not done !",Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ee) {
                            }
                        }
                    });
                }else{
                    Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                }
            }
        });


        crd_precription.setOnClickListener(new View .OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Config.tmp_caseId.equalsIgnoreCase("0")) {
                    Intent i = new Intent(getApplicationContext(), AddPrescriptionActivity.class);
                    //Log.e(TAG, "parameters : " +rec.screenerid);
                    i.putExtra("CitizenId",Config.tmp_citizenId);
                    i.putExtra("ScreenerId",Config.tmp_screenerid);
                    i.putExtra("doctorId", Config._doctorid);
                    i.putExtra("recordId","0");
                    i.putExtra("caseId",Config.tmp_caseId);
                    finish();
                    startActivity(i);
                }else{
                    Toast.makeText(MyConfig.CONTEXT,"Please take vital first !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        crd_allTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Config.tmp_caseId.equalsIgnoreCase("0")){
                   // Toast.makeText(TestListActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(TestListActivity.this, ScreeningViewActivity.class);
                    i.putExtra("CitizenId", Config.tmp_citizenId);
                    i.putExtra("ScreenerId", Config.tmp_caseId);

                    Log.e("Response", "CitizenId: " + Config.tmp_citizenId);
                    Log.e("Response", "CaseId: " +Config.tmp_caseId);
                    startActivity(i);
                    //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

            crd_breast_examination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Config.tmp_caseId.equalsIgnoreCase("0")) {

                        Intent ii = new Intent(TestListActivity.this, BreastiTestActivity.class);
                        startActivity(ii);
                    } else {
                        Toast.makeText(MyConfig.CONTEXT, "Please take vital first !", Toast.LENGTH_SHORT).show();

                    }
                }
            });

    }


    @Override
    protected void onStop() {
        super.onStop();
        Config.referredClick = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Config.referredClick = 0;
    }
}