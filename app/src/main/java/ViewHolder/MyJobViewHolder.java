package ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;
import com.google.android.gms.maps.model.Circle;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyJobViewHolder extends RecyclerView.ViewHolder{

    public TextView myjobcompanyName, myjobTitle, myjobstatus;
    public CircleImageView profile_i;

    public MyJobViewHolder(@NonNull View itemView) {
        super(itemView);
        myjobcompanyName = itemView.findViewById(R.id.jobcompanyN);
        myjobTitle = itemView.findViewById(R.id.ucjobT);
        profile_i = itemView.findViewById(R.id.profile_i);
//        myjobstatus = itemView.findViewById(R.id.myjobstatus);
    }
}
