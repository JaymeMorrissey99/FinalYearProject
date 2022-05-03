package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import Fragments.CompanyHomeFragment;
import Fragments.CompanyProfile;
import Fragments.ModelHomeFragment;
import Fragments.PostFragment;
import Fragments.ProfileFragment;
import Fragments.SearchFragment;
import Models.Post;

public class CompanyMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference mRef, postRef;
    RecyclerView recyclerView;

    Fragment selectedFrag= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment selectedFrag= null;
                switch (item.getItemId()){
                    case R.id.home:
                       // Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_SHORT).show();
                        selectedFrag = new CompanyHomeFragment();
                        break;
                    case R.id.search:
                        //Toast.makeText(getApplicationContext(), "Search Selected", Toast.LENGTH_SHORT).show();
                        selectedFrag = new SearchFragment();
                        break;
                    case R.id.addPost:
                        selectedFrag=null;
                        //Toast.makeText(getApplicationContext(), "Post Selected", Toast.LENGTH_SHORT).show();
                        SendToPost();
                        break;
                    case R.id.money:
                       // Toast.makeText(getApplicationContext(), "Settings Selected", Toast.LENGTH_SHORT).show();
                        selectedFrag = new PostFragment();

                        break;
                    case R.id.profile:
                       // Toast.makeText(getApplicationContext(), "Profile Selected", Toast.LENGTH_SHORT).show();
                        selectedFrag = new CompanyProfile();
                        break;

                    default:
                        selectedFrag = new CompanyHomeFragment();

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.cfrag_containter, selectedFrag).commit();
                return true;
            }
        });
    }

    private void SendToPost() {
        Intent intent = new Intent(CompanyMainActivity.this, PostActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}