package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileInformation extends AppCompatActivity {

    private CircleImageView propic;
    private TextView userType;
    private EditText iUsername, iFullname, detailsemail, ibio;
    private Button iUpdateDetails;

    String img;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String u;
    DatabaseReference databaseReference, curRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_information);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        propic = findViewById(R.id.iprofilePicture);
        userType = findViewById(R.id.userType);
        iUsername = findViewById(R.id.iUsername);
        iFullname = findViewById(R.id.iFullname);
        detailsemail = findViewById(R.id.detailsemail);
        ibio = findViewById(R.id.ibio);

        iUpdateDetails = findViewById(R.id.iUpdateDetails);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        LoadInfo();

        iUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDetails();
            }
        });


    }

    private void SaveDetails() {

        String updateUname = iUsername.getText().toString();
        String updateFname = iFullname.getText().toString();
        String iBio = ibio.getText().toString();
//        String updateUname = iUsername.getText().toString();
        HashMap<String, Object>updateDetails = new HashMap<>();
        updateDetails.put("Username", updateUname);
        updateDetails.put("FullName", updateFname);
        updateDetails.put("Bio", iBio);
        databaseReference.child(u).updateChildren(updateDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Details Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something went Wrong, \n Please Try Again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadInfo() {

        u = mUser.getUid();
        databaseReference.child(u).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //for(DataSnapshot d: snapshot.getChildren()) {
//                    Picasso.get().load(img).into(profileP);
                    if(!snapshot.child("image").exists()){
                        Toast.makeText(getApplicationContext(), "Upload Profile Pictures \n By Clicking on the Picture", Toast.LENGTH_SHORT).show();

                    }else if(snapshot.child("image").exists()){
                        img = snapshot.child("image").getValue().toString();
                    }
//                        String img = snapshot.child("image").getValue().toString();
                        String type = snapshot.child("type").getValue().toString();
                        String uName = snapshot.child("Username").getValue().toString();
                        String fName = snapshot.child("FullName").getValue().toString();
                        String bio = snapshot.child("Bio").getValue().toString();

                        Picasso.get().load(img).into(propic);
                        userType.setText(type);
                        iUsername.setText(uName);
                        iFullname.setText(fName);
                        ibio.setText(bio);
                        detailsemail.setText("vincentB@gmail.com");
                        // save profile email on set up
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}