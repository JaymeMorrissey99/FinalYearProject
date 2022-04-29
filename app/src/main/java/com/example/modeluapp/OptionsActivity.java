package com.example.modeluapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {

    private Button mModelOption, mCompanyOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getSupportActionBar().hide();

        mModelOption = findViewById(R.id.modelOption);
        mCompanyOption = findViewById(R.id.companyOption);

        mModelOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelRegister();
            }
        });

        mCompanyOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyRegister();
            }
        });
    }

    private void modelRegister() {
        Intent intent = new Intent(OptionsActivity.this, RegisterModelActivity.class);
        startActivity(intent);
        finish();
    }

    private void companyRegister() {
        Intent intent = new Intent(OptionsActivity.this, RegisterCompanyActivity.class);
        startActivity(intent);
        finish();
    }
}