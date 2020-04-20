package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intentfilter.androidpermissions.PermissionManager;
import com.intentfilter.androidpermissions.models.DeniedPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.helper.GPSTracker;
import qu.edu.qa.seniorproject.localDB.AppDataBase;
import qu.edu.qa.seniorproject.localDB.Location;

import static java.util.Collections.singleton;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    public static double latitude;
    public static double longitude;
    public LatLng mlatLng;
    private Spinner mSpinner;
    private List<Location> mLocations;
    private ExecutorService executorService;
    private AppDataBase appDataBase;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setSpinner();
        getLocalData();
        grantPermission();
        appDataBase = AppDataBase.getInstance(this);
    }

    private void getLocalData() {
        AppDataBase.getInstance(this).dataDao().getAllLocation().observe(this, locations -> {
            List<String> mData = new  ArrayList<>();
            if (locations.size() > 0) {
                mData.clear();
                mLocations = locations;
                mData.add("choose a location you wanna go");
                for (int i = 0; i < locations.size(); i++) {
                    mData.add(locations.get(i).getName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mData);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(dataAdapter);
            }
        });
    }

    private void setSpinner() {
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void grantPermission() {
        PermissionManager permissionManager = PermissionManager.getInstance(this);
        permissionManager.checkPermissions(singleton(Manifest.permission.ACCESS_COARSE_LOCATION), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                getCurrentLocation();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);
            }

            @Override
            public void onPermissionDenied(DeniedPermissions deniedPermissions) {
                Toast.makeText(getApplicationContext(), "You should accept permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
        if (mlatLng != null) {
            mMap.addMarker(new MarkerOptions().position(mlatLng).title("Your position"));
        } else {
            Toast.makeText(MapsActivity.this, "null", Toast.LENGTH_SHORT).show();
        }
        mMap.setOnMapClickListener(latLng -> {
            mlatLng = latLng ;
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Your position"));

        });
    }

    private void getCurrentLocation() {
        gpsTracker = new GPSTracker(MapsActivity.this);

        if (gpsTracker.getLocation() != null) {
            if (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                mlatLng = new LatLng(latitude, longitude);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        location = null;
        for (int i = 0; i < mLocations.size(); i++) {
            if(item.equals(mLocations.get(i).getName())) {
                location = mLocations.get(i);
            }
        }
       if (location != null) {
           updateFavNum();
           openGoogleMaps();
       }
    }

    private void openGoogleMaps() {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",latitude, longitude, "Home Sweet Home", location.getLatitude(), location.getLong() , "Where the party is at");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void updateFavNum() {
        int favNum = location.getFavNum() + 1;
        location.setFavNum(favNum);
        executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            appDataBase.dataDao().updateFavNum(location);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpinner.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}