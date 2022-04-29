package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewApplicationActivity extends AppCompatActivity {

    DatabaseReference databaseReference, requestRef, jobsRef, jobApplicationsRef, ref;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String username, userT, saveCurDate;
    String modelId, companyUser, jobId;
    String mName, mType;
    String currentState;

    CircleImageView profileImg;
    TextView muserName, muserType;
    ImageView close;
    Button acceptApplication, declineApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_application);

        modelId = getIntent().getStringExtra("modelID");
        jobId = getIntent().getStringExtra("jobKey");

        getSupportActionBar().setTitle("Application");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        companyUser = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users"); //.child(userId)

        jobsRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedJobs");
//
        jobApplicationsRef = FirebaseDatabase.getInstance().getReference().child("JobApplications");

        muserName = findViewById(R.id.modeluserN);
        muserType = findViewById(R.id.modeluserType);
        profileImg = findViewById(R.id.profileImg);

        close = findViewById(R.id.x);
        acceptApplication = findViewById(R.id.acceptApplication);
        declineApplication = findViewById(R.id.declineApplication);

        LoadApplication();

        if (!companyUser.equals(modelId)) {
            acceptApplication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptApplication.setEnabled(false);
                    if (currentState.equals("application_declined")) {
                        acceptApplication.setVisibility(View.INVISIBLE);
                        declineApplication.setVisibility(View.VISIBLE);
                        declineApplication.setText("Application Declined");
                    }
                    if (currentState.equals("nothing")) {
                        acceptModel();
                    }
                    if (currentState.equals("jobConfirm")) {
                        revokeJob();
                    }
                }
            });
        } else {
            acceptApplication.setVisibility(View.INVISIBLE);
            declineApplication.setVisibility(View.INVISIBLE);
        }

        declineApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineJob();
            }
        });
    }

    private void LoadApplication(){

        databaseReference.child(modelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String image = snapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(profileImg);

                    mName = snapshot.child("Username").getValue().toString();
                    mType = snapshot.child("type").getValue().toString();

                    muserName.setText(mName);
                    muserType.setText(mType);

                    MaintenanceofButtons();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        currentState = "nothing";

//        jobApplicationsRef.child(modelId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    mName = snapshot.child("Username").getValue().toString();
//                    mType = snapshot.child("type").getValue().toString();
//
//                    muserName.setText(mName);
//                    muserType.setText(mType);
//
//                    MaintenanceofButtons();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        currentState = "nothing";
    }

    private void acceptModel() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurDate = currentDate.format(cal.getTime());

//        ref = jobApplicationsRef.child(modelId);
        Query q = jobApplicationsRef.orderByChild("jobId").equalTo(jobId);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot d : snapshot.getChildren()) {
                        String mid = d.child("senderId").getValue().toString();
                        if(modelId.equalsIgnoreCase(mid)){
                            String publisher = d.child("publisher").getValue().toString();
                            String uName = d.child("muname").getValue().toString();
                            String fullname = d.child("mname").getValue().toString();
                            String applicationid = jobsRef.push().getKey();
                            Map<String, Object> jobConfirm = new HashMap<>();
                            String cjobId = jobsRef.push().getKey();
                            jobConfirm.put("date", saveCurDate);
                            jobConfirm.put("jobId", jobId);
                            jobConfirm.put("modelId", modelId);
                            jobConfirm.put("muName", uName);
                            jobConfirm.put("mFullName", fullname);
                            jobConfirm.put("publisher", publisher);
                            jobConfirm.put("companyId", companyUser);
                            jobConfirm.put("status", "approved");
                            jobsRef.child(applicationid).setValue(jobConfirm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        acceptApplication.setEnabled(true);
                                        currentState = "jobConfirm";
                                        acceptApplication.setText("Revoke Job");
                                        declineApplication.setVisibility(View.INVISIBLE);
                                        declineApplication.setEnabled(false);
                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void revokeJob() {
        //ref = applytoJob.child(senderId);
        Query q = jobsRef.orderByChild("jobId").equalTo(jobId);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    String id = d.child("companyId").getValue().toString();
                    if(companyUser.equalsIgnoreCase(id)) {
                        d.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    acceptApplication.setEnabled(true);
                                    currentState = "nothing";
                                    acceptApplication.setText("Accept Application");

                                    declineApplication.setVisibility(View.VISIBLE);
                                    declineApplication.setEnabled(true);
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

    }


    private void declineJob() {

        Query q = jobApplicationsRef.orderByChild("jobId").equalTo(jobId);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot d: snapshot.getChildren()){
                        String mId = d.child("senderId").getValue().toString();
                        String appid = d.child("applicationID").getValue().toString();
                        if(modelId.equalsIgnoreCase(mId)){
                            HashMap<String, Object> update = new HashMap<>();
                            update.put("status", "declined");
                            jobApplicationsRef.child(appid).updateChildren(update);
                            currentState = "application_declined";
                            acceptApplication.setVisibility(View.INVISIBLE);
                            declineApplication.setText("Application Declined");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        jobApplicationsRef.child(companyUser).child("Reciever").child("senderId").child(modelId)
//                .removeValue()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            jobApplicationsRef.child(modelId).child("Sender").child("recieverID").child(companyUser)
//                                    .removeValue()
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                acceptApplication.setEnabled(true);
//                                                currentState = "nothing";
//                                                acceptApplication.setText("Apply");
//
//                                                declineApplication.setVisibility(View.INVISIBLE);
//                                                declineApplication.setEnabled(false);
//                                            }
//
//                                        }
//                                    });
//                        }
//                    }
//                });

    }



    public void MaintenanceofButtons(){

        Query q = jobsRef.orderByChild("jobId").equalTo(jobId);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot d: snapshot.getChildren()){
                        String s = d.child("status").getValue().toString();
                        if(s.equals("approved")){
                            currentState = "jobConfirm";
                            acceptApplication.setText("Revoke Application");

                            declineApplication.setVisibility(View.INVISIBLE);
                            declineApplication.setEnabled(false);
                        }
                    }
                }else if(!snapshot.exists()){

                    Query applicationQ = jobApplicationsRef.orderByChild("jobId").equalTo(jobId);
                    applicationQ.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot s: snapshot.getChildren()){
                                    String mId = s.child("senderId").getValue().toString();
                                    String stat = s.child("status").getValue().toString();
                                    if(modelId.equals(mId)){
                                        currentState = "nothing";
                                    }
                                    if(stat.equalsIgnoreCase("declined")){
                                        currentState = "application_declined";
                                        acceptApplication.setVisibility(View.INVISIBLE);
                                        declineApplication.setVisibility(View.VISIBLE);
                                        declineApplication.setText("Application Declined");
                                        declineApplication.setEnabled(false);
                                    }
//                                    String request_T
                                }
                            }else if (!snapshot.exists()){
                                Toast.makeText(getApplicationContext(), "Application Does Not Exist Anymore", Toast.LENGTH_SHORT).show();
                            }
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








//        jobApplicationsRef.child(companyUser).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChild("Reciever")) {
//                    String request_T = snapshot.child("Reciever").child("request_type").getValue().toString();
//                     if (request_T.equals("application_recieved")) {
//                        currentState = "application_recieved";
//                        acceptApplication.setText("Accept Applicaiton");
//
//                        declineApplication.setText("Decline Application");
//                        declineApplication.setVisibility(View.VISIBLE);
//                        declineApplication.setEnabled(true);
//
//                        declineApplication.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                declineJob();
//                            }
//                        });
//                    }
//                } else {
//                    jobsRef.child(companyUser).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.child("JobInfo").hasChild(modelId)) {
//                                currentState = "jobConfirm";
//                                acceptApplication.setText("Revoked Job");
//
//                                declineApplication.setVisibility(View.INVISIBLE);
//                                declineApplication.setEnabled(false);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }


}