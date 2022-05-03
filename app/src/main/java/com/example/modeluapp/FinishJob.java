package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FinishJob extends AppCompatActivity {

    DatabaseReference jobsRef, userRef, jobListingsRef, completedJobs;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String currentState, companyUser, saveCurDate;
    String jobid, modelId;
    String mname, image, jobtitle, jobdescription, joblocation, userImg;

    EditText mEMAIL;
    CircleImageView cImg, mImg;
    TextView mNAME, jTITLE, jDESCRIPTION, jLocation;
    Button completeBtn, payBtn;

    String SECRET_KEY="sk_test_51KtDAeFS5FvIAVGAYWDnRqnYLtKNvCUM13jiYGd5xEzOSCvymNnizh4Km1ObyJXV4HE5qf2jR7LxoeqiwSVaYE7300zjlR2o8L";
    String PUBLISH_KEY="pk_test_51KtDAeFS5FvIAVGAvQsVO99TRKTikGGQCGHMPWdcquMAx62OnjjjPmTUfDSQBQmvGRnmPicpp3xfNLfQNM8uJ2VB00vL0TDtdR";
    PaymentSheet paymentSheet;

    String customerID;
    String EphericalKey;
    String ClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_job);

        getSupportActionBar().setTitle("Finalise Contract");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        jobsRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedJobs");

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        jobListingsRef = FirebaseDatabase.getInstance().getReference().child("Joblistings");
        completedJobs = FirebaseDatabase.getInstance().getReference().child("CompletedContracts");

        cImg = findViewById(R.id.cimage);
        mImg = findViewById(R.id.mimage);

        mNAME = findViewById(R.id.mn);
        mEMAIL = findViewById(R.id.me);
        jTITLE = findViewById(R.id.jt);
        jDESCRIPTION = findViewById(R.id.jd);
        jLocation = findViewById(R.id.jl);
        companyUser = mAuth.getCurrentUser().getUid();

        completeBtn = findViewById(R.id.completecontract);
        payBtn = findViewById(R.id.paymentBtn);

        PaymentConfiguration.init(this, PUBLISH_KEY);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFlow();
            }
        });

        jobid = getIntent().getExtras().get("jobKey").toString();
        modelId = getIntent().getExtras().get("modelID").toString();

        LoadDetails();

//        payBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(FinishJob.this, "Successful Register", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(FinishJob.this, Payment.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentState.equals("nothing")) {
                    completeContract();
                }
                if (currentState.equals("completed")) {
//                    revokeJob();
                    completeBtn.setEnabled(false);
                    payBtn.setEnabled(true);
                    payBtn.setVisibility(View.VISIBLE);

                }
//                completeContract();
            }
        });

        maintenanceOfButtons();


        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                          //  Toast.makeText(FinishJob.this, customerID, Toast.LENGTH_SHORT).show();

                            getEphericalKey(customerID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //customerID = object.getString("id");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>header=new HashMap<>();
                header.put("Authorization", "Bearer "+ SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FinishJob.this);
        requestQueue.add(stringRequest);



    }


    private void completeContract() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurDate = currentDate.format(cal.getTime());

        userRef.child(companyUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String applicationid = completedJobs.push().getKey();
                    Map<String, Object> completedJob = new HashMap<>();

                    completedJob.put("completedJobID", applicationid);
                    completedJob.put("modelID", modelId);
                    completedJob.put("JobID", jobid);
                    completedJob.put("companyID", companyUser);
                    completedJob.put("Date", saveCurDate);
                    completedJob.put("ModelName", mname);
                    completedJob.put("status", "Completed");
                    completedJobs.child(applicationid).setValue(completedJob).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                currentState = "completed";
                                completeBtn.setEnabled(false);
                                completeBtn.setText("Completed");
                                payBtn.setEnabled(true);
                                payBtn.setVisibility(View.VISIBLE);

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void LoadDetails() {

        userRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
//                    for(DataSnapshot d: snapshot.getChildren()){
//
//                    }
                    userImg = snapshot.child("image").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query mQ = userRef.orderByChild(modelId);
        userRef.child(modelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mname = snapshot.child("FullName").getValue().toString();
                    image = snapshot.child("image").getValue().toString();
                    Query jobQ = jobListingsRef.child(jobid);
                    jobListingsRef.child(jobid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                    jobtitle = snapshot.child("JobTitle").getValue().toString();
                                    jobdescription = snapshot.child("JobDes").getValue().toString();
                                    joblocation = snapshot.child("JobLocation").getValue().toString();
                         //       Toast.makeText(getApplicationContext(), ""+ jobtitle, Toast.LENGTH_SHORT).show();

                                Picasso.get().load(image).into(mImg);
                                Picasso.get().load(userImg).into(cImg);
                                mNAME.setText(mname);
                                jTITLE.setText(jobtitle);
                                mEMAIL.setText("vincentB@gmail.com");
                                jDESCRIPTION.setText(jobdescription);
                                jLocation.setText(joblocation);


                                maintenanceOfButtons();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        currentState = "nothing";
    }

    public void maintenanceOfButtons(){

        Query q = completedJobs.orderByChild("jobId").equalTo(jobid);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot d: snapshot.getChildren()){
                        String s = d.child("status").getValue().toString();
                        if(s.equals("completed")){
                            currentState = "completed";
                            completeBtn.setEnabled(false);
                            completeBtn.setText("Completed");
                            payBtn.setEnabled(true);
                            payBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }else if(!snapshot.exists()){

                    Query applicationQ = jobsRef.orderByChild("jobId").equalTo(jobid);
                    applicationQ.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot s: snapshot.getChildren()){
                                    String mId = s.child("modelId").getValue().toString();
                                    String stat = s.child("status").getValue().toString();
                                    if(modelId.equals(mId)){
                                        currentState = "nothing";
                                    }
                                    if(stat.equalsIgnoreCase("approved")){
                                        currentState = "nothing";
                                        completeBtn.setVisibility(View.VISIBLE);
                                        completeBtn.setText("Complete Contract");
                                        payBtn.setVisibility(View.INVISIBLE);
//                                        declineApplication.setEnabled(false);
                                    }
//                                    String request_T
                                }
                            }else if (!snapshot.exists()){
                                Toast.makeText(getApplicationContext(), "Application Does Not Exist Anymore", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");

                            getClientSecret(customerID, EphericalKey);
                         //   Toast.makeText(FinishJob.this, EphericalKey, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //customerID = object.getString("id");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>header=new HashMap<>();
                header.put("Authorization", "Bearer "+ SECRET_KEY);
                header.put("Stripe-Version", "2020-08-27");

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String>params=new HashMap<>();
                params.put("customer", customerID);
                //header.put("Stripe-Version", "2020-08-27");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FinishJob.this);
        requestQueue.add(stringRequest);

    }

    private void getClientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");

                            //getClientSecret(customerID, EphericalKey);
                         //   Toast.makeText(FinishJob.this, ClientSecret, Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //customerID = object.getString("id");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>header=new HashMap<>();
                header.put("Authorization", "Bearer "+ SECRET_KEY);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String>params=new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", "1000"+"00"); //concatinate
                params.put("currency", "eur");
                params.put("automatic_payment_methods[enabled]", "true");
                //header.put("Stripe-Version", "2020-08-27");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FinishJob.this);
        requestQueue.add(stringRequest);

    }

    private void PaymentFlow() {

        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration(""+mname, new PaymentSheet.CustomerConfiguration(
                        customerID, EphericalKey
                ))
        );

    }






}