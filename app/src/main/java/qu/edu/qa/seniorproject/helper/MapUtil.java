package qu.edu.qa.seniorproject.helper;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

import qu.edu.qa.seniorproject.model.map.EndLocation;
import qu.edu.qa.seniorproject.model.map.MapResponse;
import qu.edu.qa.seniorproject.model.map.StartLocation;

public class MapUtil {

    public static String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";

    public static void addMarkersToMap(MapResponse results, GoogleMap mMap) {
        StartLocation startLocation = results.getRoutes().get(0).getLegs().get(0).getStartLocation();
        EndLocation endLocation = results.getRoutes().get(0).getLegs().get(0).getEndLocation();
        String startAddress = results.getRoutes().get(0).getLegs().get(0).getStartAddress();
        String endAddress = results.getRoutes().get(0).getLegs().get(0).getEndAddress();

        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(startLocation.getLat(), startLocation.getLng())).title(startAddress));
            mMap.addMarker(new MarkerOptions().position(new LatLng(endLocation.getLat(), endLocation.getLng())).title(endAddress));
            addPolyline(results, mMap);
        }

    }

    private static void addPolyline(MapResponse results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.getRoutes().get(0).getOverviewPolyline().getPoints());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.RED).width(12));
    }

    /*
    private void openGoogleMaps() {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",latitude, longitude, "Home Sweet Home", location.getLatitude(), location.getLong() , "Where the party is at");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
     */
}
