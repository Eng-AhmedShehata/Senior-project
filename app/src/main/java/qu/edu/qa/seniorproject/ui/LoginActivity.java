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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import qu.edu.qa.seniorproject.Names;
import qu.edu.qa.seniorproject.ProfessorHomePage;
import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.User;

import static qu.edu.qa.seniorproject.ui.SplashActivity.db;
import static qu.edu.qa.seniorproject.ui.SplashActivity.mAuth;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG="LoginActivityTest111";
    private ProgressBar progressBar;
    private EditText emailEt;
    private EditText passwordEt;
    private Dialog notVerifyDialog;
    private Button resendEmail;
    private ImageView closeIV;
    public static User user;
    User userTest;
    String test = "no";
    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        syncID();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    emailEt.setText("@qu.edu.qa");
                }else{
                    emailEt.setText("@student.qu.edu.qa");
                }
            }
        });

    }




    private void showNVDialog() {
        notVerifyDialog.setContentView(R.layout.notverifiy_popup);
        notVerifyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notVerifyDialog.show();
        resendEmail = notVerifyDialog.findViewById(R.id.resend_btn);
        closeIV = notVerifyDialog.findViewById(R.id.close_dialog_iv);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notVerifyDialog.dismiss();
            }
        });

        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    notVerifyDialog.dismiss();
                                }else {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    notVerifyDialog.setContentView(R.layout.error_popup);
                                    notVerifyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    notVerifyDialog.show();
                                    resendEmail = notVerifyDialog.findViewById(R.id.resend_btn);
                                    closeIV = notVerifyDialog.findViewById(R.id.close_dialog_iv);
                                    resendEmail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            notVerifyDialog.dismiss();
                                        }
                                    });
                                    closeIV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            notVerifyDialog.dismiss();
                                        }
                                    });

                                }

                            }
                        });

            }
        });

    }







    public void forgetPassword(View view) {
        //TODO # Remove this line
        Toast.makeText(this, "reset the password", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        if (!isValid()) {
            progressBar.setVisibility(View.GONE);
            return;
        }
        mAuth.sendPasswordResetEmail(emailEt.getText().toString()+"@qu.edu.qa").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onComplete:rest password successfully ");
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, task.getException().getMessage());
                }
            }
        });
    }

    private boolean isValid(){
        if (emailEt.getText().toString().isEmpty()){
            emailEt.setError("Please Enter the UserName!");
            return false;
        }

        if (passwordEt.getText().toString().isEmpty()){
            passwordEt.setError("Please Enter a password!");
            return false;
        }

        return true;
    }



    public void goToRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    private void syncID() {
        passwordEt =findViewById(R.id.login_password_et);
        progressBar = findViewById(R.id.progressBar2);
        emailEt = findViewById(R.id.login_user_et);
        passwordEt = findViewById(R.id.login_password_et);
        notVerifyDialog = new Dialog(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        aSwitch=findViewById(R.id.switch_professor);
    }

    public void singInBtn(View view) {
        if (!isValid()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailEt.getText().toString()+"",passwordEt.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "isSuccessful", Toast.LENGTH_SHORT).show();
                            if (!mAuth.getCurrentUser().isEmailVerified()){
                                //send a msg
                                Toast.makeText(LoginActivity.this, "You email is not verify ", Toast.LENGTH_SHORT).show();
                                showNVDialog();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }


                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onComplete:login done successfully ");
                            readData(new MyCallBack() {
                                @Override
                                public void onCallBack(User user) {
                                    LoginActivity.user =user;
                                    Toast.makeText(LoginActivity.this, LoginActivity.user.getName(), Toast.LENGTH_SHORT).show();

                                }
                            });

                            db.collection("user").document(mAuth.getUid()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            Toast.makeText(LoginActivity.this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                                            //TODO TWO VIEW
                                            user = documentSnapshot.toObject(User.class);
                                            Toast.makeText(LoginActivity.this, user.getType(), Toast.LENGTH_SHORT).show();
                                            if (user.getType().equals(Names.STUDENT)){
                                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                Toast.makeText(LoginActivity.this, user.getType(), Toast.LENGTH_SHORT).show();
                                                intent.putExtra("user",documentSnapshot.toObject(User.class));
                                                startActivity(intent);
                                                finish();

                                            }else {
                                                Intent intent = new Intent(LoginActivity.this, ProfessorHomePage.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                Toast.makeText(LoginActivity.this, user.getType(), Toast.LENGTH_SHORT).show();
                                                intent.putExtra("user",documentSnapshot.toObject(User.class));
                                                startActivity(intent);
                                                finish();
                                            }



                                        }
                                    });




                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }



    public void readData(final MyCallBack myCallback) {


    }

    private interface MyCallBack {
        void onCallBack(User user);
    }

}
