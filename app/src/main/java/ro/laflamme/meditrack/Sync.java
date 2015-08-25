package ro.laflamme.meditrack;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.laflamme.meditrack.db.DatabaseHelper;
import ro.laflamme.meditrack.domain.Pharm;
import ro.laflamme.meditrack.exception.NoPlacesFoundException;

/**
 * Created by motan on 10.05.2015.
 */
public class Sync {
    private static final String TAG = "Sync";
    private Context context;
    public DatabaseHelper dbHelper;

    public Sync(Context context) {
        this.context = context;
    }

    /*
        This should always be called in background thread.
         */
    public void syncPharms(int radius, double lat, double lng) {
        Log.d(TAG, "Querying places...");
        List<Pharm> placesPharms = null;
        try {
            placesPharms = PlaceController.getPharms(radius, lat, lng);
        } catch (NoPlacesFoundException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        Log.v(TAG, "Query complete. Found " + placesPharms.size() + " entries");
        listPharmacies(placesPharms);

        Log.v(TAG, "Querying local pharms...");
        List<Pharm> localPharms = getHelper().getPharmDao().queryForAll();
        Log.v(TAG, "Query complete. Found " + localPharms.size() + " entries");
        listPharmacies(localPharms);

        Log.v(TAG, "Building hashmap ...");
        Map<String, Pharm> map = new HashMap<>();
        for (Pharm localPharm : localPharms) {
            map.put(localPharm.getPlaceId(), localPharm);
        }
        Log.v(TAG, "Built hashmap with " + map.size() + " entries");

        int added = 0;
        for (Pharm placePharm : placesPharms) {
            Log.v(TAG, "Comparing " + placePharm.getName());
            Pharm match = map.get(placePharm.getPlaceId());
            if (match == null) {
                Log.v(TAG, "Pharmacy " + placePharm.getName() + " is going to be added");
                getHelper().getPharmDao().create(placePharm);
                added++;
            } else {
                Log.v(TAG, "Already got the " + match.getName() + " pharmacy, ignoring.");
            }
        }
        Log.v(TAG, "Sync completed. Added " + added + " pharmacies.");


        listPharmacies(getHelper().getPharmDao().queryForAll());


        /*
        if (localPharms.size() == 0) {
            for (Pharm pharm : placesPharms) {
                getHelper().getPharmDao().create(pharm);
            }
        }
        else
            for (Pharm localPharm : localPharms) {
                Pharm match = pharmMap.get(localPharm.getRefference());
                if (match != null) {
                    Log.d(TAG, "Adding pharmacy [" + match.getName() + "]");
                    getHelper().getPharmDao().create(match);
                }
            }
*/

        releaseHelper();
    }


    public void listPharmacies(List<Pharm> ps) {
        for (Pharm p : ps) {
            Log.v(TAG, "Pharmacy [" + p.getName() + "}");
        }
    }

    private void releaseHelper() {
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }


    private DatabaseHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return dbHelper;
    }


}
