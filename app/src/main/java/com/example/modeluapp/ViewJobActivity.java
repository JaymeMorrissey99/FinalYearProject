package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewJobActivity extends AppCompatActivity {
    private static final String TAG = "USERID";
    DatabaseReference databaseReference, applytoJob, userRef, ref;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String applicationid;
    String userId, senderId, jobid;
    String jobt, jobd;
    String currentState;
    TextView jobViewTitle, jobViewDes;
    ImageView closex;
    Button applytoJobbtn, canceljobBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);

        getSupportActionBar().setTitle("Job Details");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Joblistings");
        applytoJob = FirebaseDatabase.getInstance().getReference().child("JobApplications");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userId = getIntent().getExtras().get("userKey").toString();
        jobid = getIntent().getExtras().get("jobKey").toString();
        applicationid = applytoJob.push().getKey();
        senderId = mUser.getUid();
        jobViewTitle = findViewById(R.id.viewjobT);
        jobViewDes = findViewById(R.id.viewjobD);

        closex = findViewById(R.id.closex);

        applytoJobbtn = findViewById(R.id.applybtn);
        canceljobBtn = findViewById(R.id.cancelapply);

        //Toast.makeText(this, "" + jobid, Toast.LENGTH_SHORT).show();


//        closex.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendToJobListings();
//            }
//        });

        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

//        currentState = "nothing";
//        recyclerView = findViewById(R.id.jobRV);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LoadJob();

        canceljobBtn.setVisibility(View.INVISIBLE);

        //CheckUserExistence(userId);
        if (!senderId.equals(userId)) {
            applytoJobbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    applytoJobbtn.setEnabled(false);

                    if (currentState.equals("nothing")) {
                        sendapplyRequest();
                    }
                    if (currentState.equals("application_sent")) {
                        cancelapplication();
                    }
//                    if (currentState.equals("request_recieved")) {
//                        acceptapplication();
//                    }
//                    if (currentState.equals("linked")) {
//                        unLinkwithPerson();
//                    }
                }
            });
        } else {
            applytoJobbtn.setVisibility(View.INVISIBLE);
            canceljobBtn.setVisibility(View.INVISIBLE);
        }

        currentState = "nothing";
    }

    private void sendapplyRequest() {

        databaseReference.child(jobid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String publisher = snapshot.child("Publisher").getValue().toString();
                    String u = mUser.getUid();
                    userRef.child(u).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("FullName").getValue().toString();
                            String uname = snapshot.child("Username").getValue().toString();
                            String applicationid = applytoJob.push().getKey();
                            Map<String, Object> jobApp = new HashMap<>();
                            jobApp.put("applicationID", applicationid);
                            jobApp.put("jobId", jobid);
                            jobApp.put("request_type", "application_sent");
                            jobApp.put("companyId", userId);
                            jobApp.put("senderId", senderId);
                            jobApp.put("jobTitle", jobt);
                            jobApp.put("publisher", publisher);
                            jobApp.put("jobDes", jobd);
                            jobApp.put("mname", name);
                            jobApp.put("muname", uname);
                            jobApp.put("status", "pending");
                            //applytoJob.child(senderId).child("Application").setValue(jobApp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            applytoJob.child(applicationid).setValue(jobApp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        applytoJobbtn.setEnabled(true);
                                        currentState = "application_sent";
                                        applytoJobbtn.setText("Cancel Application");
                                        canceljobBtn.setVisibility(View.INVISIBLE);
                                        canceljobBtn.setEnabled(false);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void cancelapplication() {

        //ref = applytoJob.child(senderId);
        Query q = applytoJob.orderByChild("jobId").equalTo(jobid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    String id = d.child("senderId").getValue().toString();
                    if(senderId.equalsIgnoreCase(id)) {
                        d.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    applytoJobbtn.setEnabled(true);
                                    currentState = "nothing";
                                    applytoJobbtn.setText("Apply");

                                    canceljobBtn.setVisibility(View.INVISIBLE);
                                    canceljobBtn.setEnabled(false);
                                }
                            }
                        });
                    }
//                    String applicationid = snapshot.getKey();
//                    ref.child(applicationid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                applytoJobbtn.setEnabled(true);
//                                currentState = "nothing";
//                                applytoJobbtn.setText("Apply");
//
//                                canceljobBtn.setVisibility(View.INVISIBLE);
//                                canceljobBtn.setEnabled(false);
//                            }
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        applytoJob.orderByChild("jobId").equalTo(jobid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    String uid = mUser.getUid();
//                    applytoJob.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                applytoJobbtn.setEnabled(true);
//                                    currentState = "nothing";
//                                    applytoJobbtn.setText("Apply");
//
//                                    canceljobBtn.setVisibility(View.INVISIBLE);
//                                    canceljobBtn.setEnabled(false);
//                            }
//                        }
//                    });
////                    String uid = mUser.getUid();
////                    String s = snapshot.child("jobId").getValue().toString();
////                    Log.d(TAG, ": " + s);
////                    if(uid.equalsIgnoreCase(snapshot.child("senderId").getValue().toString())){
////                        applytoJob.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
////                            @Override
////                            public void onComplete(@NonNull Task<Void> task) {
////                                if(task.isSuccessful()){
////                                    applytoJobbtn.setEnabled(true);
////                                    currentState = "nothing";
////                                    applytoJobbtn.setText("Apply");
////
////                                    canceljobBtn.setVisibility(View.INVISIBLE);
////                                    canceljobBtn.setEnabled(false);
////                                }
////                            }
////                        });
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            applytoJob.child(userId)
//                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        applytoJobbtn.setEnabled(true);
//                                        currentState = "nothing";
//                                        applytoJobbtn.setText("Apply");
//
//                                        canceljobBtn.setVisibility(View.INVISIBLE);
//                                        canceljobBtn.setEnabled(false);
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });

//        applytoJob.child(senderId).removeValue()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            applytoJob.child(userId)
//                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        applytoJobbtn.setEnabled(true);
//                                        currentState = "nothing";
//                                        applytoJobbtn.setText("Apply");
//
//                                        canceljobBtn.setVisibility(View.INVISIBLE);
//                                        canceljobBtn.setEnabled(false);
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });

    }


//    private void sendToJobListings() {
//        Intent intent = new Intent(ViewJobActivity.this, JobListingActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }

    private void LoadJob() {

        Toast.makeText(this, "" + senderId, Toast.LENGTH_SHORT).show();

        databaseReference.child(jobid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    jobt = snapshot.child("JobTitle").getValue().toString();
                    jobd = snapshot.child("JobDes").getValue().toString();

                    jobViewTitle.setText(jobt);
                    jobViewDes.setText(jobd);

                    maintananceButtons();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void maintananceButtons() {

        //ref = applytoJob.child(senderId);
        Query q = applytoJob.orderByChild("jobId").equalTo(jobid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot d: snapshot.getChildren()){
                        String u = mUser.getUid();
                        String sender = d.child("senderId").getValue().toString();
                        if(sender.equalsIgnoreCase(u)){
                            String request_T = d.child("request_type").getValue().toString();
                            if(request_T.equalsIgnoreCase("application_sent")){
                                currentState = "application_sent";
                                applytoJobbtn.setText("Cancel Application");
                                canceljobBtn.setVisibility(View.INVISIBLE);
                                canceljobBtn.setEnabled(false);

                                canceljobBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cancelapplication();
                                    }
                                });
                            }
                        }else if (!sender.equalsIgnoreCase(u)){
                            currentState = "nothing";
                        }

                    }
                }else if(!snapshot.exists()){
                    currentState = "nothing";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}