package qu.edu.qa.seniorproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.localDB.Location;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private List<Location> mLocation;

    public FavouriteAdapter(List<Location> Location) {
        this.mLocation = Location;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_navi,
                parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText(mLocation.get(position).getName());
        holder.tv_num_fav.setText("Num of visits: " + mLocation.get(position).getFavNum());
    }

    @Override
    public int getItemCount() {
        return mLocation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tv_name;
        public TextView tv_num_fav;
        public LinearLayout parent;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            parent = view.findViewById(R.id.lin_parent);
            tv_name = view.findViewById(R.id.tv_name);
            tv_num_fav = view.findViewById(R.id.tv_num_fav);
        }
    }
}
