package com.sumago.latestjavix;

import static com.sumago.latestjavix.WebService.Constant.LINK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.WebService.ApiInterface;
import com.sumago.latestjavix.WebService.MyNewConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BreastiTestActivity extends AppCompatActivity {


    TextView txNotesRightBreast, txNotesLeftBreast;
    EditText etNotesRightBreast, etNotesLeftBreast;
    TextView txtLumps, txt_nipple_discharge, txtNipple_Retraction, txtSwelling, txtRash, txtBreast;
    SwitchCompat lamps1, lamps2, lamps3, lamps4, nipple1, nipple2, nipple3, nipple4, skin1, skin2, skin3, skin4, swelling1, swelling2, swelling3, swelling4, rash1, rash2, rash3, rash4, breast1, breast2, breast3, breast4;
    CheckBox cb_lumps, cb_nipple, cb_Skin, cb_swelling, cb_rash, cb_breast;
    int lumps_right_breast, lumps_right_cyclic, lumps_left_breast, lumps_left_cyclic, niple_discharge_right_breast, niple_discharge_right_cyclic, niple_discharge_left_breast, niple_discharge_left_cyclic, niple_skin_right_breast, niple_skin_right_cyclic, niple_skin_left_breast, niple_skin_left_cyclic,
            swelling_right_breast, swelling_right_cyclic, swelling_left_breast, swelling_left_cyclic, rush_scaling_right_breast, rush_scaling_right_cyclic, rush_scaling_left_breast, rush_scaling_left_cyclic,
            breastPain_right_breast, breastPain_right_cyclic, breastPain_left_breast, breastPain_left_cyclic;
    Button breastiSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breasti_test);

        txNotesRightBreast = findViewById(R.id.txNotesRightBreast);
        txNotesLeftBreast = findViewById(R.id.txNotesLeftBreast);
        etNotesRightBreast = findViewById(R.id.etNotesRightBreast);
        etNotesLeftBreast = findViewById(R.id.etNotesLeftBreast);


        txtLumps = findViewById(R.id.txtLumps);
        txt_nipple_discharge = findViewById(R.id.txt_nipple_discharge);
        txtNipple_Retraction = findViewById(R.id.txtNipple_Retraction);
        txtSwelling = findViewById(R.id.txtSwelling);
        txtRash = findViewById(R.id.txtRash);
        txtBreast = findViewById(R.id.txtBreast);

        lamps1 = findViewById(R.id.lamps1);
        lamps2 = findViewById(R.id.lamps2);
        lamps3 = findViewById(R.id.lamps3);
        lamps4 = findViewById(R.id.lamps4);

        nipple1 = findViewById(R.id.nipple1);
        nipple2 = findViewById(R.id.nipple2);
        nipple3 = findViewById(R.id.nipple3);
        nipple4 = findViewById(R.id.nipple4);

        skin1 = findViewById(R.id.skin1);
        skin2 = findViewById(R.id.skin2);
        skin3 = findViewById(R.id.skin3);
        skin4 = findViewById(R.id.skin4);
        swelling1 = findViewById(R.id.swelling1);
        swelling2 = findViewById(R.id.swelling2);
        swelling3 = findViewById(R.id.swelling3);
        swelling4 = findViewById(R.id.swelling4);
        rash1 = findViewById(R.id.rash1);
        rash2 = findViewById(R.id.rash2);
        rash3 = findViewById(R.id.rash3);
        rash4 = findViewById(R.id.rash4);
        breast1 = findViewById(R.id.breast1);
        breast2 = findViewById(R.id.breast2);
        breast3 = findViewById(R.id.breast3);
        breast4 = findViewById(R.id.breast4);

        cb_lumps = findViewById(R.id.cb_lumps);
        cb_nipple = findViewById(R.id.cb_nipple);
        cb_Skin = findViewById(R.id.cb_Skin);
        cb_swelling = findViewById(R.id.cb_swelling);
        cb_rash = findViewById(R.id.cb_rash);
        cb_breast = findViewById(R.id.cb_breast);

        breastiSubmit = findViewById(R.id.breastiSubmit);

        txNotesRightBreast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNotesRightBreast.setVisibility(View.VISIBLE);
            }
        });

        txNotesLeftBreast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNotesLeftBreast.setVisibility(View.VISIBLE);
            }
        });
        Log.d("TAG", "onCreate: "+Config.tmp_caseId);
        breastiSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("lumps_checked", cb_lumps.isChecked());
                    jsonObject.put("lumps_right_breast", lumps_right_breast);
                    jsonObject.put("lumps_right_cyclic", lumps_right_cyclic);
                    jsonObject.put("lumps_left_breast", lumps_left_breast);
                    jsonObject.put("lumps_left_cyclic", lumps_left_cyclic);
                    jsonObject.put("niple_discharge_checked", cb_nipple.isChecked());
                    jsonObject.put("niple_discharge_right_breast", niple_discharge_right_breast);
                    jsonObject.put("niple_discharge_right_cyclic", niple_discharge_right_cyclic);
                    jsonObject.put("niple_discharge_left_breast", niple_discharge_left_breast);
                    jsonObject.put("niple_discharge_left_cyclic", niple_discharge_left_cyclic);
                    jsonObject.put("niple_skin_checked", cb_Skin.isChecked());
                    jsonObject.put("niple_skin_right_breast", niple_skin_right_breast);
                    jsonObject.put("niple_skin_right_cyclic", niple_skin_right_cyclic);
                    jsonObject.put("niple_skin_left_breast", niple_skin_left_breast);
                    jsonObject.put("niple_skin_left_cyclic", niple_skin_left_cyclic);
                    jsonObject.put("swelling_checked", cb_swelling.isChecked());
                    jsonObject.put("swelling_right_breast", swelling_right_breast);
                    jsonObject.put("swelling_right_cyclic", swelling_right_cyclic);
                    jsonObject.put("swelling_left_breast", swelling_left_breast);
                    jsonObject.put("swelling_left_cyclic", swelling_left_cyclic);
                    jsonObject.put("rush_scaling_checked", cb_rash.isChecked());
                    jsonObject.put("rush_scaling_right_breast", rush_scaling_right_breast);
                    jsonObject.put("rush_scaling_right_cyclic", rush_scaling_right_cyclic);
                    jsonObject.put("rush_scaling_left_breast", rush_scaling_left_breast);
                    jsonObject.put("rush_scaling_left_cyclic", rush_scaling_left_cyclic);
                    jsonObject.put("breastPain_checked", cb_breast.isChecked());
                    jsonObject.put("breastPain_right_breast", breastPain_right_breast);
                    jsonObject.put("breastPain_right_cyclic", breastPain_right_cyclic);
                    jsonObject.put("breastPain_left_breast", breastPain_left_breast);
                    jsonObject.put("breastPain_left_cyclic", breastPain_left_cyclic);
                    jsonObject.put("note_breast_right", etNotesRightBreast.getText().toString());
                    jsonObject.put("note_breast_left", etNotesLeftBreast.getText().toString());
                    jsonObject.put("ngoId", Config.NGO_ID);
                    // jsonObject.put("caseId", "167337394426747716");
                    jsonObject.put("caseId", Config.tmp_caseId);
                    jsonObject.put("screenerId", Config.javixid);
                    jsonObject.put("citizenId", Config.tmp_citizenId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                if ( !etNotesRightBreast.getText().toString().equals("") && !etNotesLeftBreast.getText().toString().equals("")) {
//                    upload_data(jsonObject);
//                    Log.d("TAG", "onClick: " + jsonObject.toString());
//                }else {
//                    Toast.makeText(BreastiTestActivity.this, "Please Fill Notes", Toast.LENGTH_SHORT).show();
//                }

                upload_data(jsonObject);
            }
        });


        OnCheckMethod();

        CheckedThenReset();


    }
    private void upload_data(JSONObject jsonObject) {
        ApiInterface apiInterface = MyNewConfig.getRetrofit().create(ApiInterface.class);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());
                Call<ResponseBody> result =  apiInterface.addbreasttest(body);

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("TAG", "onResponse: " + response.code());
                if (response.code() == 200) {
                    finish();
                    Toast.makeText(BreastiTestActivity.this, "Test Submitted Successfully", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Retrofit Error:", t.getMessage());
            }
        });
    }


    private void CheckedThenReset() {

        cb_lumps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (!cb_lumps.isChecked()) {


                    lamps1.setChecked(false);
                    lamps2.setChecked(false);
                    lamps3.setChecked(false);
                    lamps4.setChecked(false);
                    cb_lumps.setChecked(false);
                }

            }
        });


        cb_nipple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (!cb_nipple.isChecked()) {


                    nipple1.setChecked(false);
                    nipple2.setChecked(false);
                    nipple3.setChecked(false);
                    nipple4.setChecked(false);
                    cb_nipple.setChecked(false);
                }

            }
        });


        cb_Skin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (!cb_Skin.isChecked()) {


                    skin1.setChecked(false);
                    skin2.setChecked(false);
                    skin3.setChecked(false);
                    skin4.setChecked(false);
                    cb_Skin.setChecked(false);
                }

            }
        });


        cb_swelling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (!cb_swelling.isChecked()) {


                    swelling1.setChecked(false);
                    swelling2.setChecked(false);
                    swelling3.setChecked(false);
                    swelling4.setChecked(false);
                    cb_swelling.setChecked(false);
                }

            }
        });


        cb_rash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (!cb_rash.isChecked()) {


                    rash1.setChecked(false);
                    rash2.setChecked(false);
                    rash3.setChecked(false);
                    rash4.setChecked(false);
                    cb_rash.setChecked(false);
                }

            }
        });


        cb_breast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (!cb_breast.isChecked()) {


                    breast1.setChecked(false);
                    breast2.setChecked(false);
                    breast3.setChecked(false);
                    breast4.setChecked(false);
                    cb_breast.setChecked(false);
                }

            }
        });


    }

    private void OnCheckMethod() {

        lamps1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_lumps.setChecked(true);
                //Toast.makeText(BreastiTestActivity.this, "Run", Toast.LENGTH_SHORT).show();
                if (b) {
                    lumps_right_breast = 1;
                } else {
                    lumps_right_breast = 0;
                }
                if (!cb_lumps.isChecked()) {

                    lamps1.setChecked(false);
                    lamps2.setChecked(false);
                    lamps3.setChecked(false);
                    lamps4.setChecked(false);
                }
            }
        });

        lamps2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_lumps.setChecked(true);
                if (b) {
                    lumps_right_cyclic = 1;
                } else {
                    lumps_right_cyclic = 0;
                }

//                Toast.makeText(BreastiTestActivity.this, "Run"+ , Toast.LENGTH_SHORT).show();
            }
        });

        lamps3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_lumps.setChecked(true);
                if (b) {
                    lumps_left_breast = 1;
                } else {
                    lumps_left_breast = 0;
                }
                //Toast.makeText(BreastiTestActivity.this, "Run", Toast.LENGTH_SHORT).show();
            }
        });

        lamps4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_lumps.setChecked(true);
                if (b) {
                    lumps_left_cyclic = 1;
                } else {
                    lumps_left_cyclic = 0;
                }
                //Toast.makeText(BreastiTestActivity.this, "Run", Toast.LENGTH_SHORT).show();
            }
        });


        nipple1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_nipple.setChecked(true);
                if (b) {
                    niple_discharge_right_breast = 1;
                } else {
                    niple_discharge_right_breast = 0;
                }
            }
        });

        nipple2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_nipple.setChecked(true);
                if (b) {
                    niple_discharge_right_cyclic = 1;
                } else {
                    niple_discharge_right_cyclic = 0;
                }
            }
        });

        nipple3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_nipple.setChecked(true);
                if (b) {
                    niple_discharge_left_breast = 1;
                } else {
                    niple_discharge_left_breast = 0;
                }
            }
        });

        nipple4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_nipple.setChecked(true);
                if (b) {
                    niple_discharge_left_cyclic = 1;
                } else {
                    niple_discharge_left_cyclic = 0;
                }
            }
        });


        skin1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_Skin.setChecked(true);
                if (b) {
                    niple_skin_right_breast = 1;
                } else {
                    niple_skin_right_breast = 0;
                }
            }
        });

        skin2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_Skin.setChecked(true);
                if (b) {
                    niple_skin_right_cyclic = 1;
                } else {
                    niple_skin_right_cyclic = 0;
                }
            }
        });

        skin3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_Skin.setChecked(true);
                if (b) {
                    niple_skin_left_breast = 1;
                } else {
                    niple_skin_left_breast = 0;
                }
            }
        });

        skin4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_Skin.setChecked(true);
                if (b) {
                    niple_skin_left_cyclic = 1;
                } else {
                    niple_skin_left_cyclic = 0;
                }
            }
        });


        swelling1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_swelling.setChecked(true);
                if (b) {
                    swelling_right_breast = 1;
                } else {
                    swelling_right_breast = 0;
                }
            }
        });

        swelling2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_swelling.setChecked(true);
                if (b) {
                    swelling_right_cyclic = 1;
                } else {
                    swelling_right_cyclic = 0;
                }
            }
        });

        swelling3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_swelling.setChecked(true);
                if (b) {
                    swelling_left_breast = 1;
                } else {
                    swelling_left_breast = 0;
                }
            }
        });

        swelling4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_swelling.setChecked(true);
                if (b) {
                    swelling_left_cyclic = 1;
                } else {
                    swelling_left_cyclic = 0;
                }
            }
        });

        rash1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_rash.setChecked(true);
                if (b) {
                    rush_scaling_right_breast = 1;
                } else {
                    rush_scaling_right_breast = 0;
                }
            }
        });

        rash2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_rash.setChecked(true);
                if (b) {
                    rush_scaling_right_cyclic = 1;
                } else {
                    rush_scaling_right_cyclic = 0;
                }
            }
        });

        rash3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_rash.setChecked(true);
                if (b) {
                    rush_scaling_left_breast = 1;
                } else {
                    rush_scaling_left_breast = 0;
                }
            }
        });

        rash4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_rash.setChecked(true);
                if (b) {
                    rush_scaling_left_cyclic = 1;
                } else {
                    rush_scaling_left_cyclic = 0;
                }
            }
        });


        breast1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_breast.setChecked(true);
                if (b) {
                    breastPain_right_breast = 1;
                } else {
                    breastPain_right_breast = 0;
                }
            }
        });

        breast2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_breast.setChecked(true);
                if (b) {
                    breastPain_right_cyclic = 1;
                } else {
                    breastPain_right_cyclic = 0;
                }
            }
        });

        breast3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_breast.setChecked(true);
                if (b) {
                    breastPain_left_breast = 1;
                } else {
                    breastPain_left_breast = 0;
                }
            }
        });

        breast4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_breast.setChecked(true);
                if (b) {
                    breastPain_left_cyclic = 1;
                } else {
                    breastPain_left_cyclic = 0;
                }
            }
        });


    }


}