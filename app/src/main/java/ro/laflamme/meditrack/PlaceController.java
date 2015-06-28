package ro.laflamme.meditrack;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ro.laflamme.meditrack.domain.Pharm;
import ro.laflamme.meditrack.exception.NoPlacesFoundException;
import ro.laflamme.meditrack.places.GooglePlaces;
import ro.laflamme.meditrack.places.models.Place;
import ro.laflamme.meditrack.places.models.PlacesResult;
import ro.laflamme.meditrack.places.models.Result;
import ro.laflamme.meditrack.util.Adapter;

/**
 * Created by motan on 10.05.2015.
 */
public class PlaceController {

    public static List<Pharm> getPharms(int radius, double lat, double lng)
            throws NoPlacesFoundException, JSONException, IOException {

        GooglePlaces googlePlaces = new GooglePlaces("AIzaSyDmtFoPKs9x1rrRTKbHAOJVWDxOfQKo_YQ");

        List<Place> places = null;
        PlacesResult result = null;

        result = googlePlaces.getPlaces("pharmacy", radius, lat, lng);

        if (result.getStatusCode() == Result.StatusCode.OK) {
            places = result.getPlaces();
        } else throw new NoPlacesFoundException();

        List<Pharm> pharms = new ArrayList<>();
        for (Place p : places) pharms.add(Adapter.toPharm(p));

        return pharms;
    }

}
