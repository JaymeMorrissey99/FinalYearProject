package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewMyJobDetailsActivity extends AppCompatActivity {

    DatabaseReference jobsRef, applicationRef, jobListingsRef, userRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userId, jobid, cName, ctitle, cStatus, location;
    TextView status, statValue, companyName, companyjobTitle, companyJobDescription, jobLocation, xtra;
    Button mButton;
    CircleImageView companyImg;
    String cimg, cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_job_details);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        jobsRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedJobs");
        applicationRef = FirebaseDatabase.getInstance().getReference().child("JobApplications");
        jobListingsRef = FirebaseDatabase.getInstance().getReference().child("Joblistings");
        userRef = FirebaseDatabase.getInstance().getReference().child("Joblistings");

        jobid = getIntent().getExtras().get("jobKey").toString();
        userId = getIntent().getExtras().get("userKey").toString();


        Toast.makeText(getApplicationContext(), "" + userId, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "" + jobid, Toast.LENGTH_SHORT).show();

        status = findViewById(R.id.Status);
        statValue = findViewById(R.id.sValue);
        companyName = findViewById(R.id.companyN);
        companyjobTitle = findViewById(R.id.comapnyJobT);
        companyJobDescription = findViewById(R.id.companyJobD);
        jobLocation = findViewById(R.id.companyjoblocation);
        companyImg = findViewById(R.id.myjobCompanyprofileImg);
//        xtra = findViewById(R.id.extra);
        mButton = findViewById(R.id.mapbtn);
        mButton.setVisibility(View.INVISIBLE);
        checkStatus();
    }

    private void checkStatus() {

        String u = mUser.getUid();
        Query q = jobsRef.orderByChild("jobId").equalTo(jobid);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String mId = d.child("modelId").getValue().toString();
                        //String mId = snapshot.child("modelId").getValue().toString();
                        if (mId.equalsIgnoreCase(u)) {
                            cName = d.child("publisher").getValue().toString();
//                            ctitle = d.child("jobTitle").getValue().toString();
                            cStatus = d.child("status").getValue().toString();
                            Query listingQ = jobListingsRef.orderByChild("postID").equalTo(jobid);
                            listingQ.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            cimg = ds.child("Cimage").getValue().toString();
                                           // cid = ds.child("userId").getValue().toString();
                                            ctitle = ds.child("JobTitle").getValue().toString();
                                            String jobD = ds.child("JobDes").getValue().toString();
                                            location = ds.child("JobLocation").getValue().toString();
                                            companyJobDescription.setText(jobD);
                                            Toast.makeText(getApplicationContext(), ""+ location, Toast.LENGTH_SHORT).show();
                                            Picasso.get().load(cimg).into(companyImg);
                                            companyName.setText(cName);
                                            companyjobTitle.setText(ctitle);
                                            statValue.setText(cStatus);
                                            jobLocation.setText(location);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
//                            String uid = "BpxQccqhvNM7bNk9qKS9eimiqTb2";
//                            userRef.child(uid).addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if(snapshot.exists()){
//                                        cimg = snapshot.child("image").getValue().toString();
//                                        Picasso.get().load(cimg).into(companyImg);
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
                            //getCompanyPhoto();
//                            companyName.setText(cName);
//                            companyjobTitle.setText(ctitle);
//                            statValue.setText(cStatus);
//                            jobLocation.setText(location);
                            mButton.setVisibility(View.VISIBLE);
                            mButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    Bundle b = new Bundle();
//                                    b.putString("location", location);
//                                    MapsFragment mf = new MapsFragment();
//                                    mf.setArguments(b);
//                                        getSupportFragmentManager().beginTransaction()
//                                                .add(R.id.c, new MapsFragment())
//                                                .commit();

                                    Intent intent = new Intent(ViewMyJobDetailsActivity.this, MapsActivity.class);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("jobID", jobid);
                                    intent.putExtra("location", location);
//                        intent.putExtra("userKey", getRef(position).getKey().toString());
                                    //intent.putExtra("jobKey", model.getJobId());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Error at check status", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (!snapshot.exists()) {
                    checkApplicationStatus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Query query = databaseReference.orderByChild("senderId").equalTo(u);


    }

//    private String getCompanyPhoto() {
//
//        userRef.child(cid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    cimg = snapshot.child("image").getValue().toString();
//                    Picasso.get().load(cimg).into(companyImg);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return
//    }


    private void checkApplicationStatus() {

        String u = mUser.getUid();
        Query q = applicationRef.orderByChild("senderId").equalTo(u);
        //Query q2 = q.orderByChild("jobId").equalTo(jobid);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String jId = d.child("jobId").getValue().toString();
                        if (jId.equalsIgnoreCase(jobid)) {
                            String stat = d.child("status").getValue().toString();
                            if (stat.equalsIgnoreCase("pending")) {
                                cName = d.child("publisher").getValue().toString();
                                ctitle = d.child("jobTitle").getValue().toString();

                                companyName.setText(cName);
                                companyjobTitle.setText(ctitle);
                                statValue.setText(stat);
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}