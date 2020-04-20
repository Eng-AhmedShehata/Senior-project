package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import qu.edu.qa.seniorproject.ProfessorHomePage;
import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.User;

import static qu.edu.qa.seniorproject.ui.LoginActivity.user;

public class SplashActivity extends AppCompatActivity {

    public static FirebaseAuth mAuth;
    public static FirebaseFirestore db;
    public static StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser()!=null){

            db.collection("user").document(mAuth.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            finish();
                            user = documentSnapshot.toObject(User.class);
                            if (user.getType().equals("student")){
                                Intent intent = new Intent(SplashActivity.this,HomePageActivity.class);
                                intent.putExtra("user",user);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(SplashActivity.this, ProfessorHomePage.class);
                                intent.putExtra("user",user);
                                startActivity(intent);
                                finish();
                            }



                        }
                    });
        }else
        {
            Intent i = new Intent(SplashActivity.this,WelcomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }
    }

}
