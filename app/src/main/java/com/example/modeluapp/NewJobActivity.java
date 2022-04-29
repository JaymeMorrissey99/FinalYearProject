package com.example.modeluapp;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewJobActivity extends AppCompatActivity {

    private EditText jobtitle, jobdescription, joblocation;
    private Button postJob;
    private String curUserId;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        jobtitle = findViewById(R.id.jobT);
        jobdescription = findViewById(R.id.jobD);
        postJob = findViewById(R.id.postJob);
        joblocation = findViewById(R.id.location);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        curUserId = firebaseUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Joblistings");

        postJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNewJob();
            }
        });
    }

    private void postNewJob() {

        userRef.child(curUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userName = snapshot.child("Username").getValue().toString();
                    String img = snapshot.child("image").getValue().toString();
                    String jtitle = jobtitle.getText().toString().trim();
                    String jdes = jobdescription.getText().toString().trim();
                    String jlocation = joblocation.getText().toString().trim();
                    String userid = firebaseUser.getUid();
                    String postid = databaseReference.push().getKey();

                    Map<String, Object> job = new HashMap<>();
                    job.put("postID", postid);
                    job.put("JobTitle", jtitle);
                    job.put("JobDes", jdes);
                    job.put("JobLocation", jlocation);
                    job.put("Publisher", userName);
                    job.put("userId", userid);
                    job.put("Cimage", img);
                    databaseReference.child(postid).setValue(job).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(NewJobActivity.this, CompanyMainActivity.class);
                            startActivity(intent);
                            Toast.makeText(NewJobActivity.this, "Job Posted", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    databaseReference.child(firebaseUser.getUid()).updateChildren(job).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Intent intent = new Intent(NewJobActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            Toast.makeText(NewJobActivity.this, "Job Posted", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        String jtitle = jobtitle.getText().toString().trim();
//        String jdes = jobdescription.getText().toString().trim();
//        String userid = firebaseUser.getUid();
//        Map<String, Object> job = new HashMap<>();
//        job.put("JobTitle", jtitle);
//        job.put("JobDes", jdes);
//        job.put("Publisher", );
//        job.put("userId", userid);

//        databaseReference.child(firebaseUser.getUid()).updateChildren(job).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Intent intent = new Intent(NewJobActivity.this, MainActivity.class);
//                startActivity(intent);
//                Toast.makeText(NewJobActivity.this, "Job Posted", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

}