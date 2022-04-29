package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterCompanyActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText mcEmail, mcPassword, mcrPassword, mcUName;
    private Button msignUp;
    private TextView mhaveAccount;
    private String userID;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);

        mcEmail = findViewById(R.id.rcompanyEmail);
        mcPassword = findViewById(R.id.rcompanyPassword);
        mcrPassword = findViewById(R.id.cconfirmPassword);
        //mcUName = findViewById(R.id.rcompanyName);

        mhaveAccount = findViewById(R.id.haveAccount);
        msignUp = findViewById(R.id.registercButton);
        mAuth = FirebaseAuth.getInstance();

        msignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCompany();
            }
        });

        mhaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLogin();
            }
        });
    }

    private void sendToLogin() {
        Intent intent = new Intent(RegisterCompanyActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void registerCompany() {
        String e = mcEmail.getText().toString().trim();
        String p = mcPassword.getText().toString().trim();
        String cp = mcrPassword.getText().toString().trim();

        if(!cp.equals(p)){
            Toast.makeText(RegisterCompanyActivity.this,"Passwords Do Not Match",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegisterCompanyActivity.this, "Successful Register", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterCompanyActivity.this, CompanySetUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterCompanyActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}