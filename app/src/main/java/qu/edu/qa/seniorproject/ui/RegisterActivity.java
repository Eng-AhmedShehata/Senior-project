package qu.edu.qa.seniorproject.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.User;

import static qu.edu.qa.seniorproject.ui.SplashActivity.db;
import static qu.edu.qa.seniorproject.ui.SplashActivity.mAuth;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RRRRRRRR";

    private EditText nameEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText rePasswordEt;
    private Button signUpBtn;
    private TextView signInTv;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private Dialog verifyDialog;
    private Button resendEmailB;
    private ImageView closeDialog;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String[] userAndPass;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        syncID();
        final ArrayList<String> typesList = new ArrayList<>();
        typesList.add("Student");
        typesList.add("Faculty Member");



        spinnerAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,typesList);
        spinner.setAdapter(spinnerAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: {
                        Toast.makeText(RegisterActivity.this, typesList.get(i), Toast.LENGTH_SHORT).show();
                        emailEt.setHint("QU user as aa1234567");
                        type = "student";
                        break;
                    }
                    case 1: {
                        Toast.makeText(RegisterActivity.this, typesList.get(i), Toast.LENGTH_SHORT).show();
                        emailEt.setHint("QU user as a.ali");
                        type = "professor";
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAndPass = getUserAndPass();
                if (userAndPass==null)
                    return;


                if (type.equals("student")){
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d(TAG, userAndPass[1]+"@student.qu.edu.qa");
                    mAuth.createUserWithEmailAndPassword(userAndPass[1]+"@student.qu.edu.qa",userAndPass[2])
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "successfully", Toast.LENGTH_SHORT).show();
                                        firebaseUser = mAuth.getCurrentUser();
//                                    user = new User(firebaseAuth.getCurrentUser().getUid(),userAndPass[1]+"@qu.edu.qa",userAndPass[0],"student");
//                                    myRef = database.getReference(firebaseUser.getUid()).child("user");

                                        firebaseUser.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        db.collection("user").document(mAuth.getUid()).set(new User(userAndPass[0],mAuth.getUid(),type,mAuth.getCurrentUser().getEmail()));
                                                        showVerifyDialog(task);
                                                    }
                                                });
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d(TAG, userAndPass[1]+"@qu.edu.qa");
                    mAuth.createUserWithEmailAndPassword(userAndPass[1]+"@qu.edu.qa",userAndPass[2])
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "successfully", Toast.LENGTH_SHORT).show();
                                        firebaseUser = mAuth.getCurrentUser();



                                        firebaseUser.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        db.collection("user").
                                                                document(mAuth.getUid()).set(new User(userAndPass[0],mAuth.getUid(),type,mAuth.getCurrentUser().getEmail()));
                                                        mAuth.signOut();
                                                        showVerifyDialog(task);
                                                    }
                                                });
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }


            }
        });
    }


    private String[] getUserAndPass() {
        String[] userAndPass=new String[5];

        if (nameEt.getText().toString().isEmpty()){
            nameEt.setError("Please Enter You name!");
            return null;
        }else
            userAndPass[0] = nameEt.getText().toString();

        if (emailEt.getText().toString().isEmpty()){
            emailEt.setError("Please Enter You UserName!");
            return null;
        }else if (emailEt.getText().toString().length()!=9){
            emailEt.setError("Please Enter a valid UserName!");
            return null;
        }else
            userAndPass[1] = emailEt.getText().toString();
        //I'll work on re-enter password to be more professional;

        //auto detection of how strong is the password;
        if (passwordEt.getText().toString().isEmpty()) {
            passwordEt.setError("Please Enter a password!");
            return null;
        }
//        }else if (passwordEt.getText().toString().length()<){
//            passwordEt.setError("Password must meMore than six digits");
//            return null;
//        }

        if (rePasswordEt.getText().toString().isEmpty()){
            rePasswordEt.setError("Please Enter a password!");
            return null;
        }else if (!rePasswordEt.getText().toString().equals(passwordEt.getText().toString())){
            rePasswordEt.setError("Password dose not match");
            return null;
        }else
            userAndPass[2] = rePasswordEt.getText().toString();

        return userAndPass;
    }

    private void syncID() {
        nameEt = findViewById(R.id.name_et);
        emailEt = findViewById(R.id.login_user_et);
        passwordEt = findViewById(R.id.login_password_et);
        rePasswordEt = findViewById(R.id.re_password_et);
        signUpBtn = findViewById(R.id.register_btn);
        signInTv =findViewById(R.id.signin_tv);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        verifyDialog = new Dialog(this);
        spinner = findViewById(R.id.spinner);
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        type="student";
    }

    private void showVerifyDialog(Task<Void> task){
        if (task.isSuccessful()){
            verifyDialog.setContentView(R.layout.verify_popup);
            resendEmailB = verifyDialog.findViewById(R.id.resend_btn);
            closeDialog = verifyDialog.findViewById(R.id.close_dialog_iv);
            verifyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            verifyDialog.show();
            closeDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    verifyDialog.dismiss();
                }
            });

            resendEmailB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    verifyDialog.dismiss();                }
            });

            Log.d(TAG, "onComplete: send email");
            Toast.makeText(RegisterActivity.this, "Please Check you email for verification ", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onComplete: Sent An email");

        }else {
            verifyDialog.setContentView(R.layout.error_popup);
            resendEmailB = verifyDialog.findViewById(R.id.resend_btn);
            closeDialog = verifyDialog.findViewById(R.id.close_dialog_iv);
            verifyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            verifyDialog.show();
            closeDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyDialog.dismiss();
                }
            });

            resendEmailB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(RegisterActivity.this, "yes", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, task.getException().getMessage());

        }
    }





    public void goToSignIN(View view) {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }
}

