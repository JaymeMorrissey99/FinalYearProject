package ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    public TextView username, userFullName, userTYPE;
    public CircleImageView profile_i;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.un);
        userFullName = itemView.findViewById(R.id.fn);
        userTYPE = itemView.findViewById(R.id.ut);
        profile_i = itemView.findViewById(R.id.profile_i);
//        username = itemView.findViewById(R.id.uName);
//        userFullName = itemView.findViewById(R.id.uFullName);
    }
}
