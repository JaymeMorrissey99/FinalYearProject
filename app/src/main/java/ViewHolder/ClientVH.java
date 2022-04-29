package ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeluapp.R;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientVH extends RecyclerView.ViewHolder {

    public TextView clientname, clienttitle;
    public CircleImageView profilei;

    public ClientVH(@NonNull View itemView) {
        super(itemView);
        clientname = itemView.findViewById(R.id.clientName);
        clienttitle = itemView.findViewById(R.id.clientjobt);
        profilei = itemView.findViewById(R.id.profile_i);
    }
}
