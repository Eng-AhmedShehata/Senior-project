package qu.edu.qa.seniorproject.professor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.Massage;
import qu.edu.qa.seniorproject.model.User;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static qu.edu.qa.seniorproject.ui.LoginActivity.user;
import static qu.edu.qa.seniorproject.ui.SplashActivity.db;

public class MassagesFragment extends Fragment {


    private RecyclerView recyclerView;
    private MassAccAdapter fun;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Massage> massages;
    private ArrayList<User> users;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_massages, container, false);

        recyclerView =root.findViewById(R.id.accounts_list);
        massages = new ArrayList<>();
        users = new ArrayList<>();
        massages.clear();
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        db.collection("massages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Massage massage = document.toObject(Massage.class);
                                if (massage.getResiver().equals(user.getEmail())){
                                    massages.add(massage);
                                }

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            db.collection("user").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                User user = document.toObject(User.class);
                                                users.add(user);
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                            }
                                            fun = new MassAccAdapter(getActivity(),massages,users);
                                            recyclerView.setAdapter(fun);
                                        }
                                    });






                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        return root;
    }
}