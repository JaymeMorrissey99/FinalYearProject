package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Models.Post;
import ViewHolder.ProfilePostImgVH;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyPhotos extends AppCompatActivity {

    CircleImageView profileImg;
    EditText inputfullName, inputBio;
    TextView uType, inputUsername, ltotal, ptotal;
    ImageView lgout;
    Button myPbtn, myJbtn, editPbtn, logoutbtn;
    int linkCount=0;
    int postCount=0;
    DatabaseReference databaseReference, linkRef, postRef;
    //PhotoAdapter photoAdapter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    //List<Post> postList;
    RecyclerView rv;
    Context context;
    String postID, img;
    private static final String TAG = "USERID";
    FirebaseRecyclerOptions<Post> myPostoptions;
    FirebaseRecyclerAdapter<Post, ProfilePostImgVH> myPostadaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photos);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
//        lgout = view.findViewById(R.id.logout);
        profileImg = findViewById(R.id.profilePicture);
        uType = findViewById(R.id.userType);
        inputUsername = findViewById(R.id.iUsername);
        ltotal = findViewById(R.id.linktotal);
        ptotal = findViewById(R.id.posttotal);
        rv = findViewById(R.id.myPostRV);
        rv.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
//        rv.setLayoutManager(linearLayoutManager);
//        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//
//        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
//        rv.setLayoutManager(linearLayoutManager);

        GridLayoutManager gm = new GridLayoutManager(getApplicationContext(), 2);
        rv.setLayoutManager(gm);

        getPostCount();
        LoadUserDetails();
        myPhotos();

    }



    private void LoadUserDetails() {

        databaseReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(!snapshot.child("image").exists()){
                        Toast.makeText(getApplicationContext(), "Upload Profile Pictures \n By Clicking on the Picture", Toast.LENGTH_SHORT).show();

                    }else if(snapshot.child("image").exists()){
                        img = snapshot.child("image").getValue().toString();
                    }
//                    String img = snapshot.child("image").getValue().toString();
                    //String profileImage = snapshot.child("profileImg").getValue().toString();
                    String userType = snapshot.child("type").getValue().toString();
                    String userName = snapshot.child("Username").getValue().toString();
                    Picasso.get().load(img).into(profileImg);
                    uType.setText(userType);
                    inputUsername.setText(userName);

                }else{
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void myPhotos() {

        String u = mUser.getUid();
        Query postQ = postRef.orderByChild("uID").equalTo(u);
        myPostoptions = new FirebaseRecyclerOptions.Builder<Post>().setQuery(postQ, Post.class).build();
        myPostadaptor = new FirebaseRecyclerAdapter<Post, ProfilePostImgVH>(myPostoptions) {

            @NonNull
            @Override
            public ProfilePostImgVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profilepost, parent, false);
                return new ProfilePostImgVH(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProfilePostImgVH holder, int position, @NonNull Post model) {
                Glide.with(getApplicationContext()).load(model.getPostimage()).into(holder.pImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pID = model.getPostid();
                        Intent intent = new Intent(getApplicationContext(), ViewMyPostActivity.class);
                        intent.putExtra("POSTID", pID);
                        startActivity(intent);
                     //   Toast.makeText(getApplicationContext(), ""+ pID, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        myPostadaptor.startListening();
        rv.setAdapter(myPostadaptor);

    }

    private void getPostCount() {
        String u = mUser.getUid();
        Query countQ = postRef.orderByChild("uID").equalTo(u);
        countQ.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                postCount = postCount+1;
                String countString = String.valueOf(postCount);

                ptotal.setText(countString);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}