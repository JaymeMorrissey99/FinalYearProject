package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adaptor.CommentAdaptor;
import Adaptor.PostAdaptor;
import Models.Comment;
import Models.Jobs;
import ViewHolder.CommentVH;
import ViewHolder.JobViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity {

    EditText commentTXT;
    ImageView commentICON;
    DatabaseReference commentRef, cRef;
    CircleImageView pflImage;
    String postId, senderId;
    RecyclerView cmtRV;
    FirebaseUser mUser;
    private CommentAdaptor commentAdaptor;
    private List<Comment> cList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar); 
//        getSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comment Section");
        
        commentTXT = findViewById(R.id.commenttxt);
        commentICON = findViewById(R.id.comment);
        cmtRV = findViewById(R.id.cmtRV);
        pflImage = findViewById(R.id.pflImage);
        senderId = getIntent().getExtras().get("userKey").toString();
        postId = getIntent().getExtras().get("postKey").toString();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        commentRef = FirebaseDatabase.getInstance().getReference("Comments");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cmtRV.setLayoutManager(linearLayoutManager);
        cList = new ArrayList<>();
        commentAdaptor = new CommentAdaptor(this, cList);
        cmtRV.setAdapter(commentAdaptor);

        
        commentICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentTXT.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this, "Please Enter a Comment", Toast.LENGTH_SHORT).show();
                }else{
                    comment();
                }
            }
        });
        readComments();
    }

    private void readComments(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Comments").child(postId);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cList.clear();
                for(DataSnapshot d: snapshot.getChildren()){
                    Comment c = d.getValue(Comment.class);
                    cList.add(c);
                }
                commentAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void comment() {
        String u = mUser.getUid();
        HashMap<String, Object> comment = new HashMap<>();
        comment.put("comment", commentTXT.getText().toString());
        comment.put("senderId", u);

        commentRef.child(postId).push().setValue(comment);
        commentTXT.setText("");
    }


}