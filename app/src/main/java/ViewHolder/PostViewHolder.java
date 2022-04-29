package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView POSTImage;
    public TextView usNAME, pdescription, likesTxt, postcomments, usertype;
    public ImageView post_IMAGE, postLikes, comments;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        POSTImage = itemView.findViewById(R.id.profileImage);
        usNAME = itemView.findViewById(R.id.userNAME);
        pdescription = itemView.findViewById(R.id.postdescription);
        post_IMAGE = itemView.findViewById(R.id.post_image);
        postLikes = itemView.findViewById(R.id.likePost);
        likesTxt = itemView.findViewById(R.id.postlikes);
        comments = itemView.findViewById(R.id.commentPost);
        postcomments = itemView.findViewById(R.id.cc);
        usertype = itemView.findViewById(R.id.usertype);
    }
}
