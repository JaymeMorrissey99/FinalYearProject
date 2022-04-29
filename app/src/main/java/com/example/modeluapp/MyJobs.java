package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import Models.JobApplication;
import ViewHolder.MyJobViewHolder;

public class MyJobs extends AppCompatActivity {

    private static final String TAG = "USERID";
    DatabaseReference databaseReference, jobref, joblistingref, userRef;
    FirebaseDatabase database;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    //String userRef;
    String userId ,JOBID;
    Query dbQuery;
    String userid, companyID;
    //String newjobId;
    RecyclerView myjobrv;

    FirebaseRecyclerOptions<JobApplication> myjoptions;
    FirebaseRecyclerAdapter<JobApplication, MyJobViewHolder> myjobadaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);
        getSupportActionBar().setTitle("My Jobs");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userid  = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JobApplications");
        jobref = databaseReference.child(userid);
        joblistingref = FirebaseDatabase.getInstance().getReference().child("Joblistings");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //jobId = jobref.toString();
        myjobrv = findViewById(R.id.myjobRV);
        //Log.d(TAG, "job id" +jobId);

        // Toast.makeText(getActivity(), ""+ jobId, Toast.LENGTH_SHORT).show();
        myjobrv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        LoadJobs();
    }

    private void LoadJobs() {

        String u = mUser.getUid();
        Query query = databaseReference.orderByChild("senderId").equalTo(u);
        myjoptions = new FirebaseRecyclerOptions.Builder<JobApplication>().setQuery(query, JobApplication.class).build();
        myjobadaptor = new FirebaseRecyclerAdapter<JobApplication, MyJobViewHolder>(myjoptions) {
            @Override
            protected void onBindViewHolder(@NonNull MyJobViewHolder holder, int position, @NonNull JobApplication model) {
//                holder.myjobcompanyName.setText(model.getJobId());

                holder.myjobcompanyName.setText(model.getPublisher());
                holder.myjobTitle.setText(model.getJobTitle());
                String companyId = model.getCompanyId();
                userRef.child(companyId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String img = snapshot.child("image").getValue().toString();
                            Glide.with(getApplicationContext()).load(img).into(holder.profile_i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ViewMyJobDetailsActivity.class);
                        intent.putExtra("userKey", model.getSenderId());
//                        intent.putExtra("userKey", getRef(position).getKey().toString());
                        intent.putExtra("jobKey", model.getJobId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_job_item, parent, false);

                return new MyJobViewHolder(view);
            }
        };
        myjobadaptor.startListening();
        myjobrv.setAdapter(myjobadaptor);
    }

}