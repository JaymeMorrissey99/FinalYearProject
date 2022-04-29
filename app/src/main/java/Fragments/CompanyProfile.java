package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modeluapp.EditProfile;
import com.example.modeluapp.EditProfileInformation;
import com.example.modeluapp.LoginActivity;
import com.example.modeluapp.MyClients;
import com.example.modeluapp.MyJobs;
import com.example.modeluapp.MyPhotos;
import com.example.modeluapp.R;
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

public class CompanyProfile extends Fragment {

    CircleImageView profileImg;
    EditText inputfullName, inputBio;
    TextView uType, inputUsername, ltotal, ptotal;
    ImageView lgout;
    Button myPbtn, myclientsBtn, editPbtn, logoutbtn;
    int linkCount=0;
    int clientCount=0;
    DatabaseReference databaseReference, linkRef, postRef, cjobsRef;
    //PhotoAdapter photoAdapter;
    FirebaseAuth mAuth;
    String img;
    FirebaseUser mUser;
    //List<Post> postList;
    RecyclerView rv;
    Context context;
    String postID;
    private static final String TAG = "USERID";
    FirebaseRecyclerOptions<Post> myPostoptions;
    FirebaseRecyclerAdapter<Post, ProfilePostImgVH> myPostadaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_company_profile, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        cjobsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
//        lgout = view.findViewById(R.id.logout);
        profileImg = v.findViewById(R.id.profilePicture);
        uType = v.findViewById(R.id.userType);
        inputUsername = v.findViewById(R.id.iUsername);
        ltotal = v.findViewById(R.id.linktotal);

        //PROFILE BUTTONS
        myPbtn = v.findViewById(R.id.photoBtn);
        myclientsBtn = v.findViewById(R.id.myclientsBtn);
        editPbtn = v.findViewById(R.id.editProfilebtn);
        logoutbtn = v.findViewById(R.id.logoutBtn);
//        ptotal = view.findViewById(R.id.postTotal);
//
//        rv = view.findViewById(R.id.myPostRV);
//        rv.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
//        rv.setLayoutManager(linearLayoutManager);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        myPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyPhotos.class);
                startActivity(intent);
            }
        });

        myclientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyClients.class);
                startActivity(intent);
            }
        });

        editPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileInformation.class);
                startActivity(intent);
            }
        });



        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(getContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });



        LoadUserDetails();
        getLinkCount();
        getClientCount();
        //myPhotos();


        return v;
    }


    private void LoadUserDetails() {

        databaseReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(!snapshot.child("image").exists()){
                        Toast.makeText(getContext(), "Upload Profile Pictures \n By Clicking on the Picture", Toast.LENGTH_SHORT).show();

                    }else if(snapshot.child("image").exists()){
                        img = snapshot.child("image").getValue().toString();
                    }
                    //String img = snapshot.child("image").getValue().toString();
                    //String profileImage = snapshot.child("profileImg").getValue().toString();
                    String userType = snapshot.child("type").getValue().toString();
                    String userName = snapshot.child("Username").getValue().toString();
                    Picasso.get().load(img).into(profileImg);
                    uType.setText(userType);
                    inputUsername.setText(userName);

                }else{
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getClientCount() {
        String u = mUser.getUid();
        Query countQ = cjobsRef.orderByChild("companyId").equalTo(u);
        countQ.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                clientCount = clientCount+1;
                String countString = String.valueOf(clientCount);

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

    private void getLinkCount(){
        String u = mUser.getUid();
//        Query q = linkRef.orderByChild("");
        linkRef = FirebaseDatabase.getInstance().getReference().child("Links").child(u).child("links");
        linkRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                linkCount = linkCount+1;
                String countString = String.valueOf(linkCount);

                ltotal.setText(countString);
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
//        q.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    ltotal.setText(""+snapshot.getChildrenCount());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}