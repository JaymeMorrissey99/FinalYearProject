package ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobViewHolder extends RecyclerView.ViewHolder {

    public TextView xName, xjobTitle;
    public CircleImageView cProfile;

    public JobViewHolder(@NonNull View itemView) {
        super(itemView);
//        xName = itemView.findViewById(R.id.jobcompanyName);
//        xjobTitle = itemView.findViewById(R.id.ucjobTitle);

        xName = itemView.findViewById(R.id.jobcompanyN);
        xjobTitle = itemView.findViewById(R.id.ucjobT);
        cProfile = itemView.findViewById(R.id.profile_i);
    }
}
