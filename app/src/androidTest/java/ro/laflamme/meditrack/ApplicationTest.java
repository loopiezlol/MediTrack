package ro.laflamme.meditrack;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import ro.laflamme.meditrack.places.GooglePlaces;
import ro.laflamme.meditrack.places.models.Place;
import ro.laflamme.meditrack.places.models.PlacesResult;
import ro.laflamme.meditrack.places.models.Result;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private static final int RADIUS = 500;
    private static final double LAT = 40.764941;
    private static final double LNG = -73.984886;

    public ApplicationTest() {
        super(Application.class);
    }

    public void testPlaces() {

        GooglePlaces googlePlaces = new GooglePlaces("AIzaSyDmtFoPKs9x1rrRTKbHAOJVWDxOfQKo_YQ");

        PlacesResult result = null;
        try {
            result = googlePlaces.getPlaces("food", RADIUS, LAT, LNG);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull("No internet.", result);
        assertEquals(result.getStatusCode(), Result.StatusCode.OK);

        List<Place> places = result.getPlaces();

        for (Place p : places) {
            Log.d("Places", p.getName());
        }

    }

    public void testPharmacyController() throws Exception {
        List<Pharm> pharms = PlaceController.getPharms(RADIUS, LAT, LNG);

        assertNotSame(pharms.size(), 0);

        for (Pharm p : pharms) {
            Log.d("Pharms", p.getName());
        }
    }
}