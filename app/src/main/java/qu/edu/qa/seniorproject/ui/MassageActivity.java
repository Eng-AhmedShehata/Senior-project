package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.Massage;

public class MassageActivity extends AppCompatActivity {

    private TextView msg;
    Massage massage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massge);
        //TODO change the label name
        massage = (Massage) getIntent().getSerializableExtra("massage");
        setTitle(getIntent().getStringExtra("title"));

        msg = findViewById(R.id.single_msg_tv);
        msg.setText(massage.getText());

    }

    public void emailHim(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        String[] to = {massage.getSender(), ""};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "RE:"+massage.getTopic()+"-Using QU Buddy");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
