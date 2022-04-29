package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModelSetUpActivity extends AppCompatActivity {

    private EditText msetUpUsername, msetUpFullName, mBio, mGender, mAge;
    private Button msetUpSave;
    //    private ImageView profilePic;
    CircleImageView mProfile;
    private String des;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    //StorageReference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_set_up);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

        mProfile = findViewById(R.id.profile);
        msetUpUsername = findViewById(R.id.mUserName);
        msetUpFullName = findViewById(R.id.mFullName);
        mBio = findViewById(R.id.mBio);
        mGender = findViewById(R.id.mGender);
        mAge = findViewById(R.id.mAge);

        msetUpSave = findViewById(R.id.registerSetUpButton);

//        mProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent getPic = new Intent();
////                getPic.setAction(Intent.ACTION_GET_CONTENT);
////                getPic.setType("image/*");
////                startActivityForResult(intent, REQ);
//            }
//        });

        msetUpSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInformation();
            }
        });

    }

    private void saveInformation() {
        String userName = msetUpUsername.getText().toString().trim();
        String fullName = msetUpFullName.getText().toString().trim();
        String bio = mBio.getText().toString().trim();
        String gender = mGender.getText().toString().trim();
        String age = mAge.getText().toString().trim();
        String userid = mUser.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("type", "Model");
        user.put("id", userid);
        user.put("Username", userName);
        user.put("FullName", fullName);
        user.put("Bio", bio);
        user.put("Gender", gender);
        user.put("Age", age);

        databaseReference.child(userid).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(ModelSetUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(ModelSetUpActivity.this, "Set Up Complete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}