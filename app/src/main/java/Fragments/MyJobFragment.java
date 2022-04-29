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

import com.example.modeluapp.R;
import com.example.modeluapp.ViewMyJobDetailsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import Models.JobApplication;
import ViewHolder.MyJobViewHolder;


public class MyJobFragment extends Fragment {

    private static final String TAG = "USERID";
    DatabaseReference databaseReference, jobref, joblistingref, uidref;
    FirebaseDatabase database;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String userRef;
    String userId ,JOBID;
    Query dbQuery;
    String userid, companyID;
    //String newjobId;
    RecyclerView myjobrv;

    FirebaseRecyclerOptions<JobApplication> myjoptions;
    FirebaseRecyclerAdapter<JobApplication, MyJobViewHolder> myjobadaptor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_job, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userid  = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JobApplications");
        jobref = databaseReference.child(userid);
        joblistingref = FirebaseDatabase.getInstance().getReference().child("Joblistings");

        //jobId = jobref.toString();
        myjobrv = view.findViewById(R.id.myjobRV);
        //Log.d(TAG, "job id" +jobId);

        // Toast.makeText(getActivity(), ""+ jobId, Toast.LENGTH_SHORT).show();
        myjobrv.setLayoutManager(new LinearLayoutManager(getContext()));

        LoadJobs();

        return view;
    }

    private void LoadJobs() {
        String u = mUser.getUid();
        Query query = databaseReference.orderByChild("senderId").equalTo(u);
        myjoptions = new FirebaseRecyclerOptions.Builder<JobApplication>().setQuery(query, JobApplication.class).build();
        myjobadaptor = new FirebaseRecyclerAdapter<JobApplication, MyJobViewHolder>(myjoptions) {
            @Override
            protected void onBindViewHolder(@NonNull MyJobViewHolder holder, int position, @NonNull JobApplication model) {
                holder.myjobcompanyName.setText(model.getJobId());
                holder.myjobcompanyName.setText(model.getPublisher());
                holder.myjobTitle.setText(model.getJobTitle());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ViewMyJobDetailsActivity.class);
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