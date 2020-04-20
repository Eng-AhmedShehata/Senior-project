package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.User;

import static qu.edu.qa.seniorproject.ui.LoginActivity.user;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.hello);
        user = (User) getIntent().getSerializableExtra("user");

        textView.setText(user.getName());


    }



    public void goTo(View view) {
        startActivity(new Intent(MainActivity.this, HomePageActivity.class));
    }
}
