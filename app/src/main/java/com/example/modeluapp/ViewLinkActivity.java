package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewLinkActivity extends AppCompatActivity {

    DatabaseReference databaseReference, requestRef, linkedRef, linkRequest, lReq;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String username, userT, saveCurDate;
    String userId, senderId, uName, uId;
    String currentState;

    CircleImageView linkprofileImg;
    TextView userName, userType;
    ImageView close;
    Button requestLinkbtn, cancelLinkbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_link);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        userId = getIntent().getExtras().get("userKey").toString();
        senderId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users"); //.child(userId)
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        linkedRef = FirebaseDatabase.getInstance().getReference().child("Links");

        linkRequest = FirebaseDatabase.getInstance().getReference().child("LinkRequests");


        mUser = mAuth.getCurrentUser();

        userName = findViewById(R.id.userN);
        userType = findViewById(R.id.userType);
        linkprofileImg = findViewById(R.id.linkprofileImg);

        close = findViewById(R.id.close);

        requestLinkbtn = findViewById(R.id.requestLink);
        cancelLinkbtn = findViewById(R.id.cancelRequest);

        Toast.makeText(this, "" + userId, Toast.LENGTH_SHORT).show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToSearch();
            }
        });

        LoadUser();

//        requestLinkbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RequestLink(userId);
//            }
//        });

        cancelLinkbtn.setVisibility(View.INVISIBLE);

        //CheckUserExistence(userId);
        if (!senderId.equals(userId)) {
            requestLinkbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestLinkbtn.setEnabled(false);

                    if (currentState.equals("nothing")) {
                        sendLinkRequest();
                    }
                    if (currentState.equals("request_sent")) {
                        cancelLinkRequest();
                    }
                    if (currentState.equals("request_recieved")) {
                        acceptLinkRequest();
                    }
                    if (currentState.equals("linked")) {
                        unLinkwithPerson();
                    }
                }
            });
        } else {
            requestLinkbtn.setVisibility(View.INVISIBLE);
            cancelLinkbtn.setVisibility(View.INVISIBLE);
        }
    }

    private void unLinkwithPerson() {

        linkedRef.child(senderId).child("links").child(userId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            linkedRef.child(userId).child("links").child(senderId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                requestLinkbtn.setEnabled(true);
                                                currentState = "nothing";
                                                requestLinkbtn.setText("Send Link");

                                                cancelLinkbtn.setVisibility(View.INVISIBLE);
                                                cancelLinkbtn.setEnabled(false);
                                            }

                                        }
                                    });
                        }
                    }
                });
    }


    private void acceptLinkRequest() {

//        userId = getIntent().getExtras().get("userKey").toString();
//        senderId = mAuth.getCurrentUser().getUid();
//        String u = mUser.getUid();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurDate = currentDate.format(cal.getTime());
//        lReq = FirebaseDatabase.getInstance().getReference().child("LinkRequests");
//        //String u = mUser.getUid();
//        Query query = lReq.orderByChild("receiverID").equalTo(u);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    Query uq = databaseReference.child(u);
//                    uq.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()){
//                                String
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    Query uQ = databaseReference.child(u);
//                    uQ.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()){
//                                for(DataSnapshot s: snapshot.getChildren()){
//                                     uName = s.child("Username").getValue().toString();
//                                }
//                                Query cQ = databaseReference.child(userId);
//                                cQ.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        if(snapshot.exists()){
//                                            for(DataSnapshot d: snapshot.getChildren()){
//                                                String cName = d.child("Username").getValue().toString();
//
//                                                String applicationid = linkedRef.push().getKey();
//                                                Map<String, Object> links = new HashMap<>();
//                                                links.put("applicationID", applicationid);
//                                                links.put("curUserID", senderId);
//                                                links.put("curUseruname", uName);
//                                                links.put("userId", userId);
//                                                links.put("otheruname", cName);
//                                                links.put("date", saveCurDate);
//                                                linkedRef.child(applicationid).setValue(links).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if(task.isSuccessful()){
//                                                            requestLinkbtn.setEnabled(true);
//                                                            currentState = "linked";
//                                                            requestLinkbtn.setText("UnLink");
//
//                                                            cancelLinkbtn.setVisibility(View.INVISIBLE);
//                                                            cancelLinkbtn.setEnabled(false);
//                                                        }
//                                                    }
//                                                });
//
//                                            }
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        linkedRef.child(senderId).child("links").child(userId).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    linkedRef.child(userId).child("links").child(senderId).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                linkRequest.child(senderId).child(userId)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    linkRequest.child(userId).child(senderId)
                                                            .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        requestLinkbtn.setEnabled(true);
                                                                        currentState = "linked";
                                                                        requestLinkbtn.setText("UnLink");

                                                                        cancelLinkbtn.setVisibility(View.INVISIBLE);
                                                                        cancelLinkbtn.setEnabled(false);
                                                                    }

                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });
//        linkedRef.child(senderId).child(userId).child("date").setValue(saveCurDate).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    linkedRef.child(userId).child(senderId).child("date").setValue(saveCurDate).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                linkRequest.child(senderId).child(userId)
//                                        .removeValue()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    linkRequest.child(userId).child(senderId)
//                                                            .removeValue()
//                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task<Void> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        requestLinkbtn.setEnabled(true);
//                                                                        currentState = "linked";
//                                                                        requestLinkbtn.setText("UnLink");
//
//                                                                        cancelLinkbtn.setVisibility(View.INVISIBLE);
//                                                                        cancelLinkbtn.setEnabled(false);
//                                                                    }
//
//                                                                }
//                                                            });
//                                                }
//                                            }
//                                        });
//                            }
//                        }
//                    });
//                }
//            }
//        });



//        Query q = jobApplicationsRef.orderByChild("jobId").equalTo(jobId);
//        q.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot d : snapshot.getChildren()) {
//                        String mid = d.child("senderId").getValue().toString();
//                        if(modelId.equalsIgnoreCase(mid)){
//                            String publisher = d.child("publisher").getValue().toString();
//                            String uName = d.child("muname").getValue().toString();
//                            String fullname = d.child("mname").getValue().toString();
//                            String applicationid = jobsRef.push().getKey();
//                            Map<String, Object> jobConfirm = new HashMap<>();
//                            String cjobId = jobsRef.push().getKey();
//                            jobConfirm.put("date", saveCurDate);
//                            jobConfirm.put("jobId", jobId);
//                            jobConfirm.put("modelId", modelId);
//                            jobConfirm.put("muName", uName);
//                            jobConfirm.put("mFullName", fullname);
//                            jobConfirm.put("publisher", publisher);
//                            jobConfirm.put("companyId", companyUser);
//                            jobConfirm.put("status", "approved");
//                            jobsRef.child(applicationid).setValue(jobConfirm).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        acceptApplication.setEnabled(true);
//                                        currentState = "jobConfirm";
//                                        acceptApplication.setText("Revoke Job");
//                                        declineApplication.setVisibility(View.INVISIBLE);
//                                        declineApplication.setEnabled(false);
//                                    }
//                                }
//                            });
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


    }

    private void cancelLinkRequest() {
        linkRequest.child(senderId).child(userId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            linkRequest.child(userId).child(senderId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                requestLinkbtn.setEnabled(true);
                                                currentState = "nothing";
                                                requestLinkbtn.setText("Send Link");

                                                cancelLinkbtn.setVisibility(View.INVISIBLE);
                                                cancelLinkbtn.setEnabled(false);
                                            }

                                        }
                                    });
                        }
                    }
                });
    }

    private void sendLinkRequest() {

//        databaseReference.child(senderId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                        String senderUsername = snapshot.child("Username").getValue().toString();
//                        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()){
//                                    String receiverusername = snapshot.child("Username").getValue().toString();
//                                    String applicationid = linkRequest.push().getKey();
//                                    Map<String, Object> linkRe = new HashMap<>();
//                                    linkRe.put("applicationID", applicationid);
//                                    linkRe.put("senderID", senderId);
//                                    linkRe.put("senderUsername", senderUsername);
//                                    linkRe.put("receiverID", userId);
//                                    linkRe.put("receiverusername", receiverusername);
//                                    linkRequest.child(applicationid).setValue(linkRe).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()){
//                                                requestLinkbtn.setEnabled(true);
//                                                currentState = "request_sent";
//                                                requestLinkbtn.setText("Cancel Link");
//                                                cancelLinkbtn.setVisibility(View.INVISIBLE);
//                                            }
//                                        }
//                                    });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        databaseReference.child(jobid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    String publisher = snapshot.child("Publisher").getValue().toString();
//                    String u = mUser.getUid();
//                    userRef.child(u).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            String name = snapshot.child("FullName").getValue().toString();
//                            String uname = snapshot.child("Username").getValue().toString();
//                            String applicationid = applytoJob.push().getKey();
//                            Map<String, Object> jobApp = new HashMap<>();
//                            jobApp.put("applicationID", applicationid);
//                            jobApp.put("jobId", jobid);
//                            jobApp.put("request_type", "application_sent");
//                            jobApp.put("companyId", userId);
//                            jobApp.put("senderId", senderId);
//                            jobApp.put("jobTitle", jobt);
//                            jobApp.put("publisher", publisher);
//                            jobApp.put("jobDes", jobd);
//                            jobApp.put("mname", name);
//                            jobApp.put("muname", uname);
//                            jobApp.put("status", "pending");
//                            //applytoJob.child(senderId).child("Application").setValue(jobApp).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            applytoJob.child(applicationid).setValue(jobApp).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        applytoJobbtn.setEnabled(true);
//                                        currentState = "application_sent";
//                                        applytoJobbtn.setText("Cancel Application");
//                                        canceljobBtn.setVisibility(View.INVISIBLE);
//                                        canceljobBtn.setEnabled(false);
//                                    }
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        linkRequest.child(senderId).child(userId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            linkRequest.child(userId).child(senderId)
                                    .child("request_type").setValue("recieved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                requestLinkbtn.setEnabled(true);
                                                currentState = "request_sent";
                                                requestLinkbtn.setText("Cancel Link");
                                                cancelLinkbtn.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }


    private void LoadUser() {

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String img = snapshot.child("image").getValue().toString();
                    Picasso.get().load(img).into(linkprofileImg);
                    username = snapshot.child("Username").getValue().toString();
                    userT = snapshot.child("type").getValue().toString();

                    userName.setText(username);
                    userType.setText(userT);

                    currentState = "nothing";
                    maintananceButtons();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ViewLinkActivity.this, "not working", Toast.LENGTH_SHORT).show();
            }
        });

        //currentState = "nothing";
    }

    private void maintananceButtons() {
        linkRequest.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)) {
                    String request_T = snapshot.child(userId).child("request_type").getValue().toString();
                    if (request_T.equals("sent")) {
                        currentState = "request_sent";
                        requestLinkbtn.setText("Cancel");
                        cancelLinkbtn.setVisibility(View.INVISIBLE);
                        cancelLinkbtn.setEnabled(false);
                    } else if (request_T.equals("recieved")) {
                        currentState = "request_recieved";
                        requestLinkbtn.setText("Accept Link");

                        cancelLinkbtn.setText("Decline");
                        cancelLinkbtn.setVisibility(View.VISIBLE);
                        cancelLinkbtn.setEnabled(true);

                        cancelLinkbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelLinkRequest();
                            }
                        });
                    }
                } else if(!snapshot.exists()){
                    checkLinked();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkLinked() {
        linkedRef.child(senderId).child("links").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)) {
                    currentState = "linked";
                    requestLinkbtn.setText("Unlink");

                    cancelLinkbtn.setVisibility(View.INVISIBLE);
                    cancelLinkbtn.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void backToSearch() {
//        Intent intent = new Intent(ViewLinkActivity.this, FindLinkActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
    }
}