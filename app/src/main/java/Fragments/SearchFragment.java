package Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.modeluapp.R;
import com.example.modeluapp.ViewLinkActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import Models.User;
import ViewHolder.SearchViewHolder;

public class SearchFragment extends Fragment {

    FirebaseRecyclerOptions<User> options;
    FirebaseRecyclerAdapter<User, SearchViewHolder> adaptor;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RecyclerView recyclerView;
    Toolbar toolbar;
    EditText searchBox;
    public static final String TAG = "TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        SearchView searchView = new SearchView(((getContext())).getSupportActionBar().getThemedContext());
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mUser = mAuth.getCurrentUser();
        searchBox = view.findViewById(R.id.searchBox);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        LoadUsers("");

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String u = searchBox.getText().toString();
                LoadUsers(u);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void LoadUsers(String s) {
        Query query = databaseReference.orderByChild("Username").startAt(s).endAt(s + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        adaptor = new FirebaseRecyclerAdapter<User, SearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull User model) {
                if (!mUser.getUid().equals(getRef(position).getKey().toString())) {
                    String u = model.getUsername();
//                    holder.profile_i

                    Glide.with(getContext()).load(model.getImage()).into(holder.profile_i);
                    holder.username.setText(model.getUsername());
                    holder.userFullName.setText(model.getFullName());
                    holder.userTYPE.setText(model.getType());
                    Log.d(TAG, "username: " + u);
//                    Toast.makeText(getContext(), ""+u, Toast.LENGTH_SHORT).show();

                } else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ViewLinkActivity.class);
                        intent.putExtra("userKey", getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem, parent, false);

                return new SearchViewHolder(view);
            }
        };
        adaptor.startListening();
        recyclerView.setAdapter(adaptor);
    }



//        @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.search, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                LoadUsers(s);
//                return false;
//            }
//        });
//    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.search, menu);
//        MenuItem menuItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                LoadUsers(s);
//                return false;
//            }
//        });
//        return true;
//
//    }
}