package qu.edu.qa.seniorproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import qu.edu.qa.seniorproject.ui.MapsActivity;
import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.ui.ScannerActivity;


public class FunctionsAdapter extends RecyclerView.Adapter<FunctionsAdapter.CustomViewHolder> {

    public FunctionsAdapter() {

        addToArray();
    }

    ArrayList<String> functions = new ArrayList<>();
    Context context;

    public FunctionsAdapter(Context context) {
        this.context = context;
        addToArray();
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView textOfIcon;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_image_view);
            textOfIcon = itemView.findViewById(R.id.text_icon_tv);

        }

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_function_layout,parent,false);

        return new CustomViewHolder(itemView);
    }

    private void addToArray(){
        functions.add("Navigate");
        functions.add("Show Information");
        functions.add("Leave Message");
        functions.add("Explore!");
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        holder.textOfIcon.setText(functions.get(position));
        if (position==1) {
            int resId = context.getResources().getIdentifier("calendar_icon", "drawable", context.getPackageName());
            holder.icon.setImageResource(resId);
        }
        if (position==2) {
            int resId = context.getResources().getIdentifier("email_icon", "drawable", context.getPackageName());
            holder.icon.setImageResource(resId);
        }
        if (position==3) {
            int resId = context.getResources().getIdentifier("info_icon", "drawable", context.getPackageName());
            holder.icon.setImageResource(resId);
        }

            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (position!=0) {
                        Intent intent = new Intent(context, ScannerActivity.class);
                        intent.putExtra("position", functions.get(position));
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, MapsActivity.class);
                        intent.putExtra("position", functions.get(position));
                        context.startActivity(intent);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return functions.size();
    }
}
