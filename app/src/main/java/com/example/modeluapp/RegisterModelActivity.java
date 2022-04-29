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

public class RegisterModelActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText mregisterEmail, mregisterPassword, mConfirmPassword, mregisterUName, mregisterName, mregisterBio;
    private Button msignUp;
    private TextView mhaveAccount;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_model);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mregisterEmail = findViewById(R.id.registerEmail);
        mregisterPassword = findViewById(R.id.registerPassword);
        mConfirmPassword = findViewById(R.id.confirmPassword);
//        mregisterUName = findViewById(R.id.mUserName);
//        mregisterName = findViewById(R.id.mFullName);
//        mregisterBio = findViewById(R.id.mBio);
        mhaveAccount = findViewById(R.id.haveAccount);
        msignUp = findViewById(R.id.registerButton);

        msignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        mhaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLogin();
            }
        });
    }

    private void registerUser() {
        String e = mregisterEmail.getText().toString().trim();
        String p = mregisterPassword.getText().toString().trim();
        String cp = mConfirmPassword.getText().toString().trim();

        if(!cp.equals(p)){
            Toast.makeText(RegisterModelActivity.this,"Passwords Do Not Match",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegisterModelActivity.this, "Successful Register", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterModelActivity.this, ModelSetUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterModelActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
//        String uname = mregisterUName.getText().toString().trim();
//        String fName = mregisterName.getText().toString().trim();
//        String b = mregisterBio.getText().toString().trim();


    }

    private void sendToLogin() {
        Intent intent = new Intent(RegisterModelActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}