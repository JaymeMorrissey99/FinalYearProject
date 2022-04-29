package com.example.modeluapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Pay extends AppCompatActivity {

    Button pbtn;

    String SECRET_KEY="sk_test_51KtDAeFS5FvIAVGAYWDnRqnYLtKNvCUM13jiYGd5xEzOSCvymNnizh4Km1ObyJXV4HE5qf2jR7LxoeqiwSVaYE7300zjlR2o8L";
    String PUBLISH_KEY="pk_test_51KtDAeFS5FvIAVGAvQsVO99TRKTikGGQCGHMPWdcquMAx62OnjjjPmTUfDSQBQmvGRnmPicpp3xfNLfQNM8uJ2VB00vL0TDtdR";
    PaymentSheet paymentSheet;

    String customerID;
    String EphericalKey;
    String ClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        pbtn = findViewById(R.id.paybtn);

        PaymentConfiguration.init(this, PUBLISH_KEY);

        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFlow();
            }
        });

        //4242 4242 4242 424 for test card

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
                            Toast.makeText(Pay.this, customerID, Toast.LENGTH_SHORT).show();

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
                HashMap<String, String> header=new HashMap<>();
                header.put("Authorization", "Bearer "+ SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Pay.this);
        requestQueue.add(stringRequest);




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
                            Toast.makeText(Pay.this, EphericalKey, Toast.LENGTH_SHORT).show();


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

        RequestQueue requestQueue = Volley.newRequestQueue(Pay.this);
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
                            Toast.makeText(Pay.this, ClientSecret, Toast.LENGTH_SHORT).show();



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

        RequestQueue requestQueue = Volley.newRequestQueue(Pay.this);
        requestQueue.add(stringRequest);

    }

    private void PaymentFlow() {

        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("JD", new PaymentSheet.CustomerConfiguration(
                        customerID, EphericalKey
                ))
        );

    }
}