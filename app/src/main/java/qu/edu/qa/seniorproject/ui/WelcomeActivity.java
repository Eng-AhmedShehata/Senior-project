package qu.edu.qa.seniorproject.ui;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import qu.edu.qa.seniorproject.R;

public class WelcomeActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }



    public void typeOfUser(View view) {
        switch (view.getId()){
            case R.id.student_btn:{
                Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.guset_btn: {
                //TODO 1 go to guest view
                startActivity(new Intent(WelcomeActivity.this,GuestHomePageActivity.class));

            }
        }
    }
}
