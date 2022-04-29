package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostVH extends RecyclerView.ViewHolder {

    public CircleImageView POSTImage;
    public TextView usNAME, pdescription;
    public ImageView post_IMAGE;

    public MyPostVH(@NonNull View itemView) {
        super(itemView);
        POSTImage = itemView.findViewById(R.id.profileImage);
        usNAME = itemView.findViewById(R.id.userNAME);
        pdescription = itemView.findViewById(R.id.postdescription);
        post_IMAGE = itemView.findViewById(R.id.post_image);
    }
}
