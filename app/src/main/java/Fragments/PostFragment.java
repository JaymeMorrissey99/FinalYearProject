package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.modeluapp.R;
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
import com.google.maps.GeoApiContext;

import java.util.ArrayList;
import java.util.List;

import Adaptor.PostAdaptor;
import Models.Post;
import ViewHolder.MyPostVH;
import ViewHolder.PostViewHolder;
import ViewHolder.ProfilePostImgVH;


public class PostFragment extends Fragment {

    String pID;
    DatabaseReference postRef, linkedRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RecyclerView vpRV;
    private PostAdaptor postAdaptor;
    private List<String>followingList;
    private List<Post>postList;
    FirebaseRecyclerOptions<Post> viewPostoptions;
    FirebaseRecyclerAdapter<Post, PostViewHolder> viewPostadaptor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String u = mUser.getUid();
        vpRV = view.findViewById(R.id.viewPostRV);
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        linkedRef = FirebaseDatabase.getInstance().getReference().child("Links").child(u).child("links");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        vpRV.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdaptor = new PostAdaptor(getContext(), postList);
        vpRV.setAdapter(postAdaptor);
        //LoadPosts();
        checkFollowers();
        return view;
    }

    private void checkFollowers() {
        followingList = new ArrayList<>();
        linkedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot d: snapshot.getChildren()){
                        followingList.add(d.getKey());
                     //   Toast.makeText(getContext(), ""+followingList.toString(), Toast.LENGTH_SHORT).show();
                        readPost();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readPost(){
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    for(String id: followingList){
                        if(post.getuID().equals(id)){
                            postList.add(post);
                        }
                    }
                }
                postAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    private void readPost() {
//        String u = mUser.getUid();
//        //Query q = linkedRef.child(u).child("links");
//        postRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot d: snapshot.getChildren()){
//                    Post p = d.getValue(Post.class);
//                    for(String id: followingList){
//                        if(p.getuID().equals(id)){
//                            Query q = postRef.orderByChild("uID").equalTo(id);
//                            viewPostoptions = new FirebaseRecyclerOptions.Builder<Post>().setQuery(q, Post.class).build();
//                            viewPostadaptor = new FirebaseRecyclerAdapter<Post, PostViewHolder>(viewPostoptions) {
//                                @Override
//                                protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
//                                    Glide.with(getContext()).load(model.getPostimage()).into(holder.post_IMAGE);
//                                    holder.usNAME.setText(model.getUsername());
//                                    holder.pdescription.setText(model.getDescription());
//                                }
//
//                                @NonNull
//                                @Override
//                                public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
//                                    return new PostViewHolder(v);
//                                }
//                            };
//                            viewPostadaptor.startListening();
//                            viewPostadaptor.notifyDataSetChanged();
//                            vpRV.setAdapter(viewPostadaptor);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void LoadPosts() {
//        String u = mUser.getUid();
//        Query q = linkedRef.child(u);
//        q.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    String pushID= .push().getKey();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}