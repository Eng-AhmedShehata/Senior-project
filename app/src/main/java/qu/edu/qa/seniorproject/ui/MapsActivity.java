package qu.edu.qa.seniorproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intentfilter.androidpermissions.PermissionManager;
import com.intentfilter.androidpermissions.models.DeniedPermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import qu.edu.qa.seniorproject.R;
import qu.edu.qa.seniorproject.helper.GPSTracker;
import qu.edu.qa.seniorproject.helper.MapUtil;
import qu.edu.qa.seniorproject.localDB.AppDataBase;
import qu.edu.qa.seniorproject.localDB.Location;
import qu.edu.qa.seniorproject.model.map.MapResponse;

import static java.util.Collections.singleton;
import static qu.edu.qa.seniorproject.helper.MapUtil.BASE_URL;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    public static double latitude;
    public static double longitude;
    public LatLng mlatLng;
    private Spinner mSpinner;
    private List<Location> mLocations;
    private ExecutorService executorFav;
    private ExecutorService executorHttp;
    private AppDataBase appDataBase;
    private Location location;
    private android.location.Location mLastLocation;
    private GPSTracker gpsTracker;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setSpinner();
        getLocalData();
        // Set google maps
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
        mSpinner = findViewById(R.id.spinner);
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
        //Google map setup
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Show marker on the screen and adjust the zoom level
        LatLng mOrigin = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin,15));
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
           //openGoogleMaps();
           setRoute(location);
       }
    }

    private void setRoute(Location location) {
        executorHttp = Executors.newSingleThreadExecutor();
        executorHttp.execute(() -> makeRequest(location));
    }

    private void makeRequest(Location location) {
        String strOrigin = "origin=" + latitude + "," + longitude;
        String strDestination = "destination=" + location.getLatitude() + "," + location.getLong();
        String APIKEY = getResources().getString(R.string.google_maps_key);
        String param = strOrigin + "&" + strDestination + "&" +/*sensor + "&" + mode +*/ "key=";

        mRequestQueue = Volley.newRequestQueue(this);
        //String Request initialized
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, BASE_URL + param + APIKEY , response -> {
            // Convert response to json
            Gson mGson = new GsonBuilder().create();
            MapResponse mapResponse = mGson.fromJson(response, MapResponse.class);
            if (mMap != null) {
                MapUtil.addMarkersToMap(mapResponse, mMap);
            }

        }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());

        mRequestQueue.add(mStringRequest);
    }

    private void updateFavNum() {
        int favNum = location.getFavNum() + 1;
        location.setFavNum(favNum);
        executorFav = Executors.newSingleThreadExecutor();

        executorFav.execute(() -> {
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
        if (executorFav != null) {
            executorFav.shutdown();
        }
        if (executorHttp != null) {
            executorHttp.shutdown();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}