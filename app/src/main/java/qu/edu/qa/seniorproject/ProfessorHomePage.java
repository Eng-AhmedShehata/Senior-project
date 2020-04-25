package qu.edu.qa.seniorproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import qu.edu.qa.seniorproject.model.User;
import qu.edu.qa.seniorproject.ui.FavouriteNavigationActivity;
import qu.edu.qa.seniorproject.ui.SplashActivity;

import static qu.edu.qa.seniorproject.ui.LoginActivity.user;
import static qu.edu.qa.seniorproject.ui.SplashActivity.db;
import static qu.edu.qa.seniorproject.ui.SplashActivity.mAuth;
import static qu.edu.qa.seniorproject.ui.SplashActivity.mStorageRef;

public class ProfessorHomePage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static final int PROFILE_IMAGE = 1234;
    private static final String TAG = "TAGGG";
    private Uri uriProfile;
    private ImageView imageView ;
    private TextView nav_email;
    private TextView nav_user ;
    private View hView;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home_page);
        refresh();

        Toolbar toolbar = findViewById(R.id.toolbar_pro);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout_pro);
        navigationView = findViewById(R.id.nav_view_pro);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_pro,R.id.nav_msg_pro,R.id.nav_account_pro,R.id.nav_signout_pro)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_pro);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if (destination.getId()==R.id.nav_signout_pro){
                    mAuth.signOut();
                    startActivity(new Intent(ProfessorHomePage.this, SplashActivity.class));
                }
            }
        });



        //setting the profile image, title and email
        hView =  navigationView.getHeaderView(0);
        imageView = hView.findViewById(R.id.pro_profile_image);
        nav_email = hView.findViewById(R.id.pro_email_tv);
        nav_user = hView.findViewById(R.id.pro_name_tv);
        mStorageRef = FirebaseStorage.getInstance().getReference("profileImage");
        db = FirebaseFirestore.getInstance();
    }

    public void proChooseProfileImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select Profile Image"),PROFILE_IMAGE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO getting the profile image from the cloud
        refresh();


        try{
            Picasso.get().load(Uri.parse(user.getProfileImage())).into(imageView);
        }catch (Exception e){

        }

        if (user != null) {
            nav_user.setText(user.getName());
            nav_email.setText(user.getEmail());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==PROFILE_IMAGE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uriProfile=data.getData();
            Log.d(TAG, "onActivityResult: "+data.getData().getPath());

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfile);
                imageView.setImageBitmap(bitmap);
                uploadPhoto();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fav_navi :
                startActivity(new Intent(this, FavouriteNavigationActivity.class));
                break;
            case android.R.id.home :
                drawer.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_pro);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private String getFileExtension(Uri uri){
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void refresh() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


            db.collection("user").document(mAuth.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            user = documentSnapshot.toObject(User.class);
                        }
                    });

    }

    private void uploadPhoto(){
        if (uriProfile !=null){
            final StorageReference profileImageRef = mStorageRef.child(user.getId()+"."+ getFileExtension(uriProfile));
            profileImageRef.putFile(uriProfile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ProfessorHomePage.this, "Uploading successfully"+taskSnapshot.getUploadSessionUri().toString(), Toast.LENGTH_SHORT).show();
                            user.setProfileImage(taskSnapshot.getUploadSessionUri().toString());

                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    db.collection("user").document(user.getId()).update("profileImage",String.valueOf(uri))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    startActivity(new Intent(ProfessorHomePage.this,SplashActivity.class));


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ProfessorHomePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfessorHomePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else {
            Toast.makeText(this, "no photo selected", Toast.LENGTH_SHORT).show();
        }
    }

}
