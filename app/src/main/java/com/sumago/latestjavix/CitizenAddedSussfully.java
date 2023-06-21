package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CitizenAddedSussfully extends AppCompatActivity {
    private ImageView checkView;
    private static Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_added_sussfully);
        checkView = (ImageView) findViewById(R.id.check);
        loginButton = (Button) findViewById(R.id.loginBtn);
        checkView.setVisibility(View.VISIBLE);
        ((Animatable) checkView.getDrawable()).start();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CitizenAddedSussfully.this, ScreenerActivity.class);
                finish();
                startActivity(i);
            }
        });
    }
}