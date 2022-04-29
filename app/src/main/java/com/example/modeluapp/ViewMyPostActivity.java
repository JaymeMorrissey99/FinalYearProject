package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import Models.Post;
import ViewHolder.MyPostVH;

public class ViewMyPostActivity extends AppCompatActivity {

    String pID;
    DatabaseReference postRef, userRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RecyclerView vpRV;
    FirebaseRecyclerOptions<Post> viewPostoptions;
    FirebaseRecyclerAdapter<Post, MyPostVH> viewPostadaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_post);

        pID = getIntent().getExtras().get("POSTID").toString();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        vpRV = findViewById(R.id.viewPostRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        vpRV.setLayoutManager(linearLayoutManager);

        LoadPost();
    }

    private void LoadPost() {
        Query q = postRef.orderByChild("postid").equalTo(pID);
        viewPostoptions = new FirebaseRecyclerOptions.Builder<Post>().setQuery(q, Post.class).build();
        viewPostadaptor = new FirebaseRecyclerAdapter<Post, MyPostVH>(viewPostoptions) {
            @NonNull
            @Override
            public MyPostVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
                return new MyPostVH(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyPostVH holder, int position, @NonNull Post model) {
                Glide.with(getApplicationContext()).load(model.getPostimage()).into(holder.post_IMAGE);
                holder.usNAME.setText(model.getUsername());
                holder.pdescription.setText(model.getDescription());
                Glide.with(getApplicationContext()).load(model.getProfileImg()).into(holder.POSTImage);

            }
        };

        viewPostadaptor.startListening();
        vpRV.setAdapter(viewPostadaptor);
    }
}