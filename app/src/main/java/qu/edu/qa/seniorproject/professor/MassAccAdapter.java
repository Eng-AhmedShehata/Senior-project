package qu.edu.qa.seniorproject.professor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import qu.edu.qa.seniorproject.ui.MassageActivity;
import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.Massage;
import qu.edu.qa.seniorproject.model.User;

import static qu.edu.qa.seniorproject.ui.SplashActivity.db;


public class MassAccAdapter extends RecyclerView.Adapter<MassAccAdapter.CustomViewHolder> {

    Context context;
    FirebaseStorage dbStorage;
    ArrayList<Massage> massages;
    ArrayList<User> users;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public MassAccAdapter(Context context,ArrayList<Massage> mas,ArrayList<User> users) {
        this.context = context;
        this.massages = mas;
        this.users = users;
        db = FirebaseFirestore.getInstance();
        dbStorage = FirebaseStorage.getInstance();
        //Toast.makeText(context, mas.size(), Toast.LENGTH_SHORT).show();

    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView timeTv;
        private TextView senderTv;
        private TextView msgTv;
        private View view;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_account);
            timeTv = itemView.findViewById(R.id.time_tv);
            senderTv = itemView.findViewById(R.id.sender_tv);
            msgTv = itemView.findViewById(R.id.msg_tv);
            view = itemView.findViewById(R.id.containerView);


        }

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_acount_list,parent,false);
        return new CustomViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {


        Massage msg = massages.get(position);
        holder.senderTv.setText(findUserName(msg.getSender()));
        holder.msgTv.setText(msg.getText());
        holder.timeTv.setText(dateFormat.format(msg.getDate()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MassageActivity.class);
                intent.putExtra("massage",massages.get(position));
                intent.putExtra("title",findUserName(massages.get(position).getSender()));
                context.startActivity(intent);

            }
        });










        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Send the user to the account
                Toast.makeText(context, "Send the user to the account", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String findUserName(String email) {
        for (User user: users) {
            if (user.getEmail().equals(email)){
                return user.getType()+":"+user.getName();
            }

        }
        return email;
    }

    @Override
    public int getItemCount() {

        return massages.size();
    }
}
