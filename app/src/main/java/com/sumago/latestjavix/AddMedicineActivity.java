package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sumago.latestjavix.Adapters.ContactsAdapter;
import com.sumago.latestjavix.Model.Contact;

import java.util.ArrayList;

public class AddMedicineActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<Contact> models = new ArrayList<Contact>();
    RecyclerView rvTechSolPoint;
    ContactsAdapter rvAdapter;
    TextView tvAdd, tvUpdate;
    EditText etEnterName;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        rvTechSolPoint = findViewById(R.id.rvContacts);
        tvAdd = findViewById(R.id.tv_add);
        etEnterName = findViewById(R.id.et_enter_name);
        tvUpdate = findViewById(R.id.tv_update);
        rvTechSolPoint.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvTechSolPoint.setLayoutManager(layoutManager);
        rvAdapter = new ContactsAdapter(getApplicationContext(), models,
                new ContactsAdapter.Onclick() {
                    @Override
                    public void onEvent(Contact model, int pos) {
                        position = pos;
                        tvUpdate.setVisibility(View.VISIBLE);
                        etEnterName.setText(model.getName());
                    }
                });
        rvTechSolPoint.setAdapter(rvAdapter);
        tvAdd.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);

        // That's all!
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add: {
                insertMethod(String.valueOf(etEnterName.getText()));
            }
            break;
            case R.id.tv_update: {
                models.get(position).setName(etEnterName.getText().toString());
                rvAdapter.notifyDataSetChanged();
                tvUpdate.setVisibility(View.GONE);
            }
            break;
        }
    }
    private void insertMethod(String name) {
        /*Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            Contact model = gson.fromJson(String.valueOf(jsonObject), Contact.class);
            models.add(model);
            rvAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        Contact model=new Contact();
        model.setName(name);
        models.add(model);
        rvAdapter.notifyDataSetChanged();
    }
}



