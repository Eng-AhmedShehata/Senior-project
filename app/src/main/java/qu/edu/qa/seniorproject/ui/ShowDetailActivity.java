package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import qu.edu.qa.seniorproject.R;

public class ShowDetailActivity extends AppCompatActivity {

    private TextView tv_room, tv_office;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_std);
        tv_room = findViewById(R.id.tv_room);
        tv_office = findViewById(R.id.tv_office);

        String stringExtra = getIntent().getStringExtra("data");
        Toast.makeText(this, stringExtra, Toast.LENGTH_SHORT).show();

        if (stringExtra.equals("room")) {
            tv_office.setVisibility(View.GONE);
        } else {
            tv_room.setVisibility(View.GONE);
        }
    }
}
