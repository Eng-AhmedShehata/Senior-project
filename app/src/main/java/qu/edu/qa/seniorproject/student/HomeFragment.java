package qu.edu.qa.seniorproject.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import qu.edu.qa.seniorproject.adapter.FunctionsAdapter;
import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.ui.FavouriteNavigationActivity;

public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private FunctionsAdapter fun;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_std, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      setUpRecyclerView(view);
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.student_list);
        fun = new FunctionsAdapter(getActivity());
        layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(fun);
    }


}