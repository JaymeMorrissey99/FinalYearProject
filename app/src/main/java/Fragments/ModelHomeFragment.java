package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.modeluapp.R;
import com.example.modeluapp.ViewJobActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Adaptor.CompanyVP;
import Adaptor.ModelVP;
import Models.Jobs;
import Models.Post;
import ViewHolder.JobViewHolder;


public class ModelHomeFragment extends Fragment {

//    FirebaseAuth mAuth;
//    FirebaseUser firebaseUser;
//    DatabaseReference mRef, postRef;
//    //FirebaseRecyclerAdapter<Post, MyViewHolder> adapter;
//    //FirebaseRecyclerOptions<Post> options;
//    StorageReference postImgRef;
//    RecyclerView recyclerView;
//
//    TabLayout tabLayout;
//    ViewPager2 viewPager2;
//    ModelVP mpagerAdaptor;
//    Fragment selectFragmnet = null;

    DatabaseReference databaseReference, jobref;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    RecyclerView jobrv;
    String jobKey1, jobUserId;

    FirebaseRecyclerOptions<Jobs>joptions;
    FirebaseRecyclerAdapter<Jobs, JobViewHolder>jadaptor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_model_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Joblistings");


        jobrv = view.findViewById(R.id.joblistingRV);
        jobrv.setLayoutManager(new LinearLayoutManager(getContext()));

        LoadJobs("");


        return view;
    }

    private void LoadJobs(String s) {

        Query q = databaseReference.orderByChild("Publisher").startAt(s).endAt(s+"\uf8ff");
        joptions = new FirebaseRecyclerOptions.Builder<Jobs>().setQuery(q, Jobs.class).build();
        jadaptor = new FirebaseRecyclerAdapter<Jobs, JobViewHolder>(joptions) {
            @Override
            protected void onBindViewHolder(@NonNull JobViewHolder holder, int position, @NonNull Jobs model) {
                if(!mUser.getUid().equals(getRef(position).getKey())){
                    //holder.cProfile
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
                        Intent intent = new Intent(getActivity(), ViewJobActivity.class);
                        intent.putExtra("userKey", model.getUserId());
//                        intent.putExtra("userKey", getRef(position).getKey().toString());
                        intent.putExtra("jobKey", model.getPostID());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_job_item, parent, false);
                return new JobViewHolder(view);
            }
        };
        jadaptor.startListening();
        jobrv.setAdapter(jadaptor);

    }
}