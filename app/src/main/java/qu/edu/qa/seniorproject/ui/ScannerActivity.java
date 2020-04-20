package qu.edu.qa.seniorproject.ui;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import qu.edu.qa.seniorproject.Names;
import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.model.Massage;
import qu.edu.qa.seniorproject.model.Place;


import static android.Manifest.permission.CAMERA;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static qu.edu.qa.seniorproject.ui.LoginActivity.user;
import static qu.edu.qa.seniorproject.ui.SplashActivity.db;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Dialog notVerifyDialog;
    private EditText topicEditText;
    private EditText textEditText;
    private Button sendBtn;
    private FirebaseFirestore dbMassages = FirebaseFirestore.getInstance();
    private ProgressBar levProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("position");
        setTitle(title);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;


        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(checkPermission()) {
            }
            else
            {
                requestPermission();
            }
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText().substring(0, 4);
//        String show = myResult.substring(0, 4);
//        String id = myResult.substring(3);

        //TODO take the client to the browser to download the app;
        switch (myResult){
            case Names.LEAVE_MSG:{
//                Toast.makeText(this, Names.LEAVE_MSG, Toast.LENGTH_SHORT).show();
                notVerifyDialog = new Dialog(this);
                notVerifyDialog.setContentView(R.layout.leave_msg_layout);
                notVerifyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                gettingIDs();
                sendMsgToDatabase();
                notVerifyDialog.show();

            }
            break;
            case Names.EXPLOR:{
//                Toast.makeText(this, Names.EXPLOR, Toast.LENGTH_SHORT).show();
                gettingPlaces("BCR");

            }break;
            case Names.SHOW_DETAILS:{
//                Toast.makeText(this, Names.SHOW_DETAILS, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ScannerActivity.this,ShowDetailActivity.class);
                List<String> stringList = new ArrayList<>();
                stringList.add("room");
                stringList.add("office");
                Random rand = new Random();
                intent.putExtra("data", stringList.get(rand.nextInt(stringList.size())));
                startActivity(intent);
                finish();
            }break;
            default:{

                new AlertDialog.Builder(ScannerActivity.this)
                        .setMessage("This QR code is not in the system")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.ic_error)
                        .create()
                        .show();
            }

        }


    }

    private void gettingPlaces(final String building){

        db = FirebaseFirestore.getInstance();

        db.collection("places").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Intent intent = new Intent(ScannerActivity.this, ExploreActivity.class);
                        ArrayList<Place> places = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Place place = document.toObject(Place.class);
                            if (place.getBuilding().equals(building)){
                                places.add(place);
                            }

                        }
                        //Log.d(TAG, "Number"+ " => " + places.size());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("places",places);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                        finish();
                    }
                });

    }


    private void insertPlaces() {
        ArrayList<Place> places = new ArrayList<>();
        //TODO  photo,  title,  text,  location,  building)
        places.add(new Place(null,"Subway","one of the best restaurant in the university, opening time is between 8:00AM - 8:00PM","Second floor G201","BCR"));
        places.add(new Place(null,"Kebab","one of the best restaurant in the university, opening time is between 8:00AM - 8:00PM","Second floor G201","BCR"));
        places.add(new Place(null,"Vending Machine","one of the best restaurant in the university, opening time is between 8:00AM - 8:00PM","Second floor G201","BCR"));
        places.add(new Place(null,"Meeting Room","one of the best restaurant in the university, opening time is between 8:00AM - 8:00PM","Second floor G201","BCR"));

        for (Place p : places) {
            db = FirebaseFirestore.getInstance();
            db.collection("places").document()
                    .set(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        //Toast.makeText(ScannerActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    private void gettingIDs() {
        sendBtn = notVerifyDialog.findViewById(R.id.send_btn);
        textEditText = notVerifyDialog.findViewById(R.id.text_et);
        topicEditText = notVerifyDialog.findViewById(R.id.topic_et);
        levProgressBar = notVerifyDialog.findViewById(R.id.lev_progressBar);
        levProgressBar.setVisibility(View.GONE);
    }

    private void sendMsgToDatabase() {

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levProgressBar.setVisibility(View.VISIBLE);
                //TODO
                String text;
                String topic;
                if (textEditText.getText().toString().isEmpty()){
                    textEditText.setError("Required");
                    return;
                }else{
                    text = textEditText.getText().toString();
                }
                if (topicEditText.getText().toString().isEmpty()){
                    topicEditText.setError("Required");
                    return;
                }
                topic = topicEditText.getText().toString();


                Massage massage = new Massage(topic,text,user.getEmail(),"mm1602805@qu.edu.qa");//TODO get the barcode owner to send him the msg.
                dbMassages.collection("massages").document()
                        .set(massage).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            levProgressBar.setVisibility(View.GONE);
                            Toast.makeText(ScannerActivity.this, "Massage has sent!", Toast.LENGTH_SHORT).show();
                            notVerifyDialog.dismiss();
                            finish();
                        }else{
                            levProgressBar.setVisibility(View.GONE);
                            notVerifyDialog.dismiss();
                            Toast.makeText(ScannerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });
    }
}