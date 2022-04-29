package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import Models.JobApplication;
import Models.confirmedJobs;
import ViewHolder.CAVH;
import ViewHolder.ClientVH;

public class MyClients extends AppCompatActivity {

    private static final String TAG = "USERID";
    DatabaseReference databaseReference, jobref, joblistingref, modelRef;
    FirebaseDatabase database;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String modelId, jobId, jtitle;
    String userid;
    String clientImg, i;

    RecyclerView clientRv;

    FirebaseRecyclerOptions<confirmedJobs> myclientoptions;
    FirebaseRecyclerAdapter<confirmedJobs, ClientVH> myclientadaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clients);

        getSupportActionBar().setTitle("Clients");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userid  = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ConfirmedJobs");
        jobref = databaseReference.child(userid);
        modelRef = FirebaseDatabase.getInstance().getReference().child("Users");
        joblistingref = FirebaseDatabase.getInstance().getReference().child("Joblistings");
        clientRv = findViewById(R.id.companyClientsRV);
        //Log.d(TAG, "job id" +jobId);

        // Toast.makeText(getActivity(), ""+ jobId, Toast.LENGTH_SHORT).show();
        clientRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        LoadClients();


    }

    private void LoadClients() {



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Query query = databaseReference.orderByChild("companyId").equalTo(userid);
                    myclientoptions = new FirebaseRecyclerOptions.Builder<confirmedJobs>().setQuery(query, confirmedJobs.class).build();
                    myclientadaptor = new FirebaseRecyclerAdapter<confirmedJobs, ClientVH>(myclientoptions) {
                        @Override
                        protected void onBindViewHolder(@NonNull ClientVH holder, int position, @NonNull confirmedJobs model) {
                            holder.clientname.setText(model.getmFullName());
                            String jobID = model.getJobId();
                            modelId = model.getModelId();
                            modelRef.child(modelId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        i = snapshot.child("image").getValue().toString();
                                        Glide.with(getApplicationContext()).load(i).into(holder.profilei);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            //getJobInfo(jobID);
//                            holder.clienttitle.setText(jtitle);
                            Query q = joblistingref.orderByChild("postID").equalTo(jobID);
                            q.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for (DataSnapshot d: snapshot.getChildren()){
                                            String jtitle = d.child("JobTitle").getValue().toString();
                                            holder.clienttitle.setText(jtitle);
                                            Log.d(TAG, "onDataChange: "+ jtitle);
                                        }
//                                        String jtitle = snapshot.child("JobTitle").getValue().toString();
//                                        holder.clienttitle.setText(jtitle);
//                                        modelRef.child(modelId).addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                if(snapshot.exists()){
//                                                    String img = snapshot.child("image").getValue().toString();
//                                                    Picasso.get().load(img).into(holder.profilei);
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), FinishJob.class);
                                    intent.putExtra("modelID", model.getModelId());
                                    intent.putExtra("jobKey", model.getJobId());
                                    startActivity(intent);
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public ClientVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);

                            return new ClientVH(view);
                        }
                    };
                    myclientadaptor.startListening();
                    clientRv.setAdapter(myclientadaptor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private String getJobInfo(String jobID) {

        joblistingref.orderByChild("jobId").equalTo(jobID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    jtitle = snapshot.child("JobTitle").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return jtitle;

    }
}