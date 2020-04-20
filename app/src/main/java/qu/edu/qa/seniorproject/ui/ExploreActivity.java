package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import qu.edu.qa.seniorproject.adapter.ExploreAdapter;
import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.Place;

public class ExploreActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ExploreAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Place> places;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Explore!");
        setContentView(R.layout.activity_explor);

        places = new ArrayList<>();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        places = (ArrayList<Place>) bundle.getSerializable("places");
        Toast.makeText(this, places.size()+"", Toast.LENGTH_SHORT).show();
        recyclerView= findViewById(R.id.explore_list);
        layoutManager = new LinearLayoutManager(this);
        adapter= new ExploreAdapter(this,places);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
