package Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modeluapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Models.Comment;
import Models.User;
import ViewHolder.CommentVH;
import ViewHolder.PostViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdaptor extends RecyclerView.Adapter<CommentVH> {

    private Context mContext;
    private List<Comment> commentList;

    private FirebaseUser mUser;

    public CommentAdaptor(Context mContext, List<Comment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment, parent, false);
        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentVH holder, int position) {

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Comment c = commentList.get(position);

        holder.commenttext.setText(c.getComment());
        getInfo(holder.pflImg, holder.cusername, c.getSenderId());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void getInfo(CircleImageView profileImg, TextView username, String senderId){ //addimg view for profile

        DatabaseReference cmtR = FirebaseDatabase.getInstance().getReference().child("Users").child(senderId);
        cmtR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                Glide.with(mContext).load(u.getImage()).into(profileImg);
                //Glide.with(getApplicationContext()).load(model.getProfileImg()).into(holder.POSTImage);
//                username.setText(u.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
