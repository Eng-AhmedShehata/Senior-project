package qu.edu.qa.seniorproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.Place;

public class ExploreAdapter  extends RecyclerView.Adapter<ExploreAdapter.CustomViewHolder> {

    public ExploreAdapter() {


    }


    ArrayList<Place> places = new ArrayList<>();
    public static Context context;

    public ExploreAdapter(Context context, ArrayList<Place> places) {
        this.context = context;
        this.places=places;
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder{

        private ImageView placeImageView;
        private TextView tileTV;
        private TextView textTV;
        private TextView loctionTV;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImageView = itemView.findViewById(R.id.place_image);
            tileTV = itemView.findViewById(R.id.place_title_tv);
            textTV = itemView.findViewById(R.id.place_text_tv);
            loctionTV = itemView.findViewById(R.id.place_loc_tv);

        }

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_single_list,parent,false);
        return new CustomViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {

        try{
            Picasso.get().load(Uri.parse(places.get(position).getPhoto())).into(holder.placeImageView);
        }catch (Exception e){

        }
        holder.tileTV.setText(places.get(position).getTitle());
        holder.textTV.setText(places.get(position).getText());
        holder.loctionTV.setText(places.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
