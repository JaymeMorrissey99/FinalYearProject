package Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modeluapp.CommentsActivity;
import com.example.modeluapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Models.Post;
import Models.User;
import ViewHolder.PostViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdaptor extends RecyclerView.Adapter<PostViewHolder>{

    public Context context;
    public List<Post> postList;
    FirebaseUser mUser;

    public PostAdaptor(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
         mUser = FirebaseAuth.getInstance().getCurrentUser();
         Post post = postList.get(position);

        Glide.with(context).load(post.getPostimage()).into(holder.post_IMAGE);

         if(holder.pdescription.equals("")){
             holder.pdescription.setVisibility(View.GONE);
         }else{
             holder.pdescription.setVisibility(View.VISIBLE);
             holder.pdescription.setText(post.getDescription());
         }

         userInfo(holder.POSTImage, holder.usNAME, post.getuID() ,holder.usertype);
         postLiked(post.getPostid(), holder.postLikes);
         totalPostLikes(holder.likesTxt, post.getPostid());
         getCommentCount(post.getPostid(), holder.postcomments);

         holder.postLikes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(holder.postLikes.getTag().equals("like")){
                     FirebaseDatabase.getInstance().getReference().child("PostLikes").child(post.getPostid()).child(mUser.getUid()).
                             setValue(true);
                 }else{
                     FirebaseDatabase.getInstance().getReference().child("PostLikes").child(post.getPostid()).child(mUser.getUid()).removeValue();

                 }
             }
         });
         holder.comments.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(context, CommentsActivity.class);
                 intent.putExtra("userKey", post.getuID());
                 intent.putExtra("postKey", post.getPostid());
                 context.startActivity(intent);
             }
         });
//        holder.postcomments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, CommentsActivity.class);
//                intent.putExtra("userKey", post.getuID());
//                intent.putExtra("postKey", post.getPostid());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView POSTImage;
        public TextView usNAME, pdescription, likesTxt, postcomments;
        public ImageView post_IMAGE, postLikes, comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            POSTImage = itemView.findViewById(R.id.profileImage);
            usNAME = itemView.findViewById(R.id.userNAME);
            pdescription = itemView.findViewById(R.id.postdescription);
            post_IMAGE = itemView.findViewById(R.id.post_image);
            postLikes = itemView.findViewById(R.id.likePost);
            likesTxt = itemView.findViewById(R.id.postlikes);
            comments = itemView.findViewById(R.id.commentPost);
            postcomments = itemView.findViewById(R.id.commentPost);
        }
    }

    public void userInfo(final CircleImageView profileImg, final TextView usNAME, final String userid, TextView usertype){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getImage()).into(profileImg);
                usNAME.setText(user.getUsername());
                usertype.setText(user.getType());
                //Publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getCommentCount(String postid, TextView postcomments){
        DatabaseReference cRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Glide.with(mContext).load(u.getImage()).into(profileImg);
                postcomments.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void postLiked(String postid, ImageView imageView){
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("PostLikes").child(postid);
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String u = mUser.getUid();
                if(snapshot.child(u).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                }else{
                    imageView.setImageResource(R.drawable.ic_likebtn);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void totalPostLikes(TextView likesTxt, String postid){

        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("PostLikes").child(postid);
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likesTxt.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
