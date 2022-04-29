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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText mE, mP;
    private Button mLogin, mRergister;
    private TextView mgotoforgotP;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String uid;
    DatabaseReference databaseReference, curRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mE = findViewById(R.id.e);
        mP = findViewById(R.id.p);
        mLogin = findViewById(R.id.loginBtn);
        mRergister = findViewById(R.id.registerBtn);
//        mgotoforgotP = findViewById(R.id.forgotPasswordBtn);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
//        uid = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               UserLogin();
            }
        });


        mRergister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegister();
            }
        });
    }

    private void UserLogin() {
        String email = mE.getText().toString().trim();
        String pass = mP.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mUser = mAuth.getCurrentUser();
                    uid = mUser.getUid();
                    CheckUserType(uid);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Fehler"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void CheckUserType(String uid) {
        //uid = mUser.getUid();
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user.getType().equals("Company")){
                    sendToCompanyMain();
                }
                else if(user.getType().equals("Model")){
                    sendToModelMain();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendToModelMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendToCompanyMain() {
        Intent intent = new Intent(LoginActivity.this, CompanyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendToRegister() {
        Intent intent = new Intent(LoginActivity.this, OptionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}