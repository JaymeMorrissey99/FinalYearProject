package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentVH extends RecyclerView.ViewHolder {

    public TextView cusername, commenttext;
    public CircleImageView pflImg;

    public CommentVH(@NonNull View itemView) {
        super(itemView);

        pflImg = itemView.findViewById(R.id.pflImage);
        cusername = itemView.findViewById(R.id.cusername);
        commenttext = itemView.findViewById(R.id.commenttext);
    }
}
