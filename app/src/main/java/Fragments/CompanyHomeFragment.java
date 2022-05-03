package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.modeluapp.NewJobActivity;
import com.example.modeluapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Adaptor.CompanyVP;
import Models.Post;

public class CompanyHomeFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference mRef, postRef;
    //FirebaseRecyclerAdapter<Post, MyViewHolder> adapter;
   // FirebaseRecyclerOptions<Post> options;
    StorageReference postImgRef;
    RecyclerView recyclerView;
    FloatingActionButton addjob;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    CompanyVP pagerAdaptor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postImgRef = FirebaseStorage.getInstance().getReference().child("posts");

        tabLayout = view.findViewById(R.id.cjobslayout);
        viewPager2 = view.findViewById(R.id.cvp2);
        addjob = view.findViewById(R.id.addJob);
        FragmentManager fm = getParentFragmentManager();
        pagerAdaptor = new CompanyVP(fm, getLifecycle());
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setAdapter(pagerAdaptor);

        addjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToAddJob();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                //viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }

    private void sendToAddJob() {
        Intent intent = new Intent(getContext(), NewJobActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}