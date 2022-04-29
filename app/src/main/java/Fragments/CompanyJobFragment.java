package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.modeluapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import Models.Jobs;
import ViewHolder.JobViewHolder;

public class CompanyJobFragment extends Fragment {

    DatabaseReference databaseReference, jobref;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    RecyclerView jobrv;
    String jobKey1;
    String curUserID;

    FirebaseRecyclerOptions<Jobs> joptions;
    FirebaseRecyclerAdapter<Jobs, JobViewHolder> adaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_job, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Company Home");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Joblistings");
        jobref = databaseReference.child("postID");
        jobKey1 = jobref.toString();
        mUser = mAuth.getCurrentUser();
        curUserID = mUser.getUid();
        jobrv = view.findViewById(R.id.companyjobRV);

        jobrv.setLayoutManager(new LinearLayoutManager(getContext()));
        LoadJobs("");

        return view;
    }

    private void LoadJobs(String s) {
        Query query = databaseReference.orderByChild("userId").equalTo(curUserID);
        //Query query = databaseReference.child(curUserID);
//        Query query = databaseReference.orderByChild(curUserID).startAt(s).endAt(s+"\uf8ff");
        joptions = new FirebaseRecyclerOptions.Builder<Jobs>().setQuery(query, Jobs.class).build();
        adaptor = new FirebaseRecyclerAdapter<Jobs, JobViewHolder>(joptions) {
            @Override
            protected void onBindViewHolder(@NonNull JobViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Jobs model) {
                if(!mUser.getUid().equals(getRef(position).getKey())){
                    /*holder.cProfile.*/
//                    Glide.with(getContext()).load(model.getCimage()).into(holder.cProfile);
                    Glide.with(getContext()).load(model.getCimage()).into(holder.cProfile);
                    holder.xName.setText(model.getPublisher());
                    holder.xjobTitle.setText(model.getJobTitle());
                }else{
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(getActivity(), ViewJobActivity.class);
//                        intent.putExtra("userKey", getRef(position).getKey().toString());
//                        intent.putExtra("jobKey", j.getPostID());
//                        startActivity(intent);
                    }
                });
            }

//            @Override
//            protected void onBindViewHolder(@NonNull JobViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull jobs j) {
//                if(!mUser.getUid().equals(getRef(position).getKey())){
//                    holder.xName.setText(j.getPublisher());
//                    holder.xjobTitle.setText(j.getJobTitle());
//                }else{
//                    holder.itemView.setVisibility(View.GONE);
//                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
//                }
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getActivity(), ViewJobActivity.class);
//                        intent.putExtra("userKey", getRef(position).getKey().toString());
//                        intent.putExtra("jobKey", j.getPostID());
//                        startActivity(intent);
//                    }
//                });
//            }

            @NonNull
            @Override
            public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_job_item, parent, false);

                return new JobViewHolder(view);
            }
        };
        adaptor.startListening();
        jobrv.setAdapter(adaptor);




    }
}