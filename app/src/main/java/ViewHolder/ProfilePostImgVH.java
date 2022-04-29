package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePostImgVH extends RecyclerView.ViewHolder {

    public ImageView pImage;

    public ProfilePostImgVH(@NonNull View itemView) {
        super(itemView);
        pImage = itemView.findViewById(R.id.post_image);
    }
}
