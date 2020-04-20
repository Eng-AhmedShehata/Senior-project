package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.adapter.FavouriteAdapter;
import qu.edu.qa.seniorproject.localDB.AppDataBase;
import qu.edu.qa.seniorproject.localDB.Location;

public class FavouriteNavigationActivity extends AppCompatActivity {

    private AppDataBase appDataBase ;
    private FavouriteAdapter favouriteAdapter;
    private RecyclerView recyclerView;
    private TextView errorMessage;
    private List<Location> locationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_navigation);
        recyclerView = findViewById(R.id.rv_fav);
        errorMessage = findViewById(R.id.tv_error_message);

        appDataBase = AppDataBase.getInstance(this);
        displayInfo();

    }

    private void displayInfo() {
        // to add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // display items
        appDataBase.dataDao().getAllFavLocation().observe(this, locations ->
        {
            if (locations.size() != 0) {
                errorMessage.setVisibility(View.GONE);
                locationList.clear();

                for (int i = 0;i < locations.size();i++) {
                    if (locations.get(i).getFavNum() > 0) {
                        locationList.add(locations.get(i));
                    }
                }
                favouriteAdapter = new FavouriteAdapter(locationList);
                recyclerView.setAdapter(favouriteAdapter);
        } else {
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }
}
