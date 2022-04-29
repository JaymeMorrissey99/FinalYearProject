package com.example.modeluapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanySetUpActivity extends AppCompatActivity {

    private EditText mcUName, mcName, mcBio;
    private Button msetUpSave;
    CircleImageView mProfile;
    private String des;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference mRef;
    //StorageReference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_set_up);

        mcUName = findViewById(R.id.cUName);
        mcName = findViewById(R.id.cName);
        mcBio = findViewById(R.id.cBio);
        msetUpSave = findViewById(R.id.setUpBtn);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference().child("Users");

        msetUpSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInformation();
            }
        });
    }

    private void saveInformation() {
        String userName = mcUName.getText().toString().trim();
        String fullName = mcName.getText().toString().trim();
        String bio = mcBio.getText().toString().trim();
        String userid = firebaseUser.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("type", "Company");
        user.put("id", userid);
        user.put("Username", userName);
        user.put("FullName", fullName);
        user.put("Bio", bio);

        mRef.child(userid).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(CompanySetUpActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(CompanySetUpActivity.this, "Set Up Complete", Toast.LENGTH_SHORT).show();
            }
        });

    }
}