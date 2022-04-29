package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.modeluapp.R;
import com.example.modeluapp.ViewApplicationActivity;
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
import ViewHolder.CAVH;
//import ViewHolder.CompanyApplicationsVH;


public class CompanyApplicationsFragment extends Fragment {

    private static final String TAG = "USERID";
    DatabaseReference databaseReference, jobref, joblistingref, modelRef;
    FirebaseDatabase database;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String modelId, jobId;
    String userid;
    String img;
    RecyclerView applicationsRV;

    FirebaseRecyclerOptions<JobApplication> mycoptions;
    FirebaseRecyclerAdapter<JobApplication, CAVH> mycadaptor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_applications, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userid  = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JobApplications");
        jobref = databaseReference.child(userid);
        modelRef = FirebaseDatabase.getInstance().getReference().child("Users");
        joblistingref = FirebaseDatabase.getInstance().getReference().child("Joblistings");
        applicationsRV = v.findViewById(R.id.companyApplicationsRV);
        //Log.d(TAG, "job id" +jobId);

        // Toast.makeText(getActivity(), ""+ jobId, Toast.LENGTH_SHORT).show();
        applicationsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        LoadApplications();

        Toast.makeText(getContext(), ""+img, Toast.LENGTH_SHORT).show();

        return v;
    }

    private void LoadApplications() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Query query = databaseReference.orderByChild("companyId").equalTo(userid);
                    mycoptions = new FirebaseRecyclerOptions.Builder<JobApplication>().setQuery(query, JobApplication.class).build();
                    mycadaptor = new FirebaseRecyclerAdapter<JobApplication, CAVH>(mycoptions) {
                        @Override
                        protected void onBindViewHolder(@NonNull CAVH holder, int position, @NonNull JobApplication model) {
                            holder.cPosition.setText(model.getJobTitle());
                            holder.applicantName.setText(model.getMuname());
                            String m = model.getSenderId();
                            modelRef.child(m).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        img = snapshot.child("image").getValue().toString();
                                        Toast.makeText(getContext(), ""+img, Toast.LENGTH_SHORT).show();
                                        Picasso.get().load(img).into(holder.profile_i);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                Toast.makeText(getActivity(), ""+ modelId, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), ViewApplicationActivity.class);
                                    intent.putExtra("modelID", model.getSenderId());
                                    intent.putExtra("jobKey", model.getJobId());
                                    startActivity(intent);
                                }
                            });
                        }

//                        @Override
//                        protected void onBindViewHolder(@NonNull CAV holder, int position, @NonNull JobApplication model) {
//                            holder.cPosition.setText(model.getJobTitle());
//                            holder.applicantName.setText(model.getMuname());
//                            String m = model.getSenderId();
//                            modelRef.child(m).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if(snapshot.exists()){
//                                        String img = snapshot.child("image").getValue().toString();
//                                        Picasso.get().load(img).into(holder.profile_i);
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//
//                            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
////                                Toast.makeText(getActivity(), ""+ modelId, Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getActivity(), ViewApplicationActivity.class);
//                                    intent.putExtra("modelID", model.getSenderId());
//                                    intent.putExtra("jobKey", model.getJobId());
//                                    startActivity(intent);
//                                }
//                            });
//                        }

                        @NonNull
                        @Override
                        public CAVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);

                            return new CAVH(view);
                        }
                    };
                    mycadaptor.startListening();
                    applicationsRV.setAdapter(mycadaptor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}