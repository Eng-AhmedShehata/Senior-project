package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import qu.edu.qa.seniorproject.adapter.FunctionsAdapter;
import qu.edu.qa.seniorproject.R;

public class GuestHomePageActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private FunctionsAdapter fun;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home_page);


        recyclerView =findViewById(R.id.guest_list);
        fun = new FunctionsAdapter(this);
        layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(fun);
    }
}
