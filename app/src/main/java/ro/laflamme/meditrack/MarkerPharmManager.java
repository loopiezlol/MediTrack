package ro.laflamme.meditrack;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.laflamme.meditrack.domain.Pharm;

/**
 * Created by motan on 16.07.2015.
 */
public class MarkerPharmManager {

    public static void syncMarkersWithPharms(List<Pharm> pharmList, GoogleMap googleMap, Map<Marker, Pharm> markersMap) {
        for(Pharm pharm : pharmList)
        {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .title(pharm.getName())
                    .snippet(pharm.getDesc())
                    .position(new LatLng(pharm.getLatitude(), pharm.getLongitude())));

            if(pharm.isOpenNow()){
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            }
            markersMap.put(marker,pharm);
        }
    }
}
