package ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;

import de.hdodenhof.circleimageview.CircleImageView;
 public class CAVH extends RecyclerView.ViewHolder {
    public TextView cPosition, applicantName;
    public CircleImageView profile_i;


    public CAVH(@NonNull View itemView) {
        super(itemView);
        profile_i = itemView.findViewById(R.id.profile_i);
        cPosition = itemView.findViewById(R.id.jobt);
        applicantName = itemView.findViewById(R.id.mname);

    }
}
