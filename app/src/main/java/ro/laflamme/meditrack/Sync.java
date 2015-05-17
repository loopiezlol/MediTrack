package ro.laflamme.meditrack;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Log.d(TAG, "Query complete. Found " + placesPharms.size() + " entries");
        listPharmacies(placesPharms);

        Log.d(TAG, "Querying local pharms...");
        List<Pharm> localPharms = getHelper().getPharmDao().queryForAll();
        Log.d(TAG, "Query complete. Found " + localPharms.size() + " entries");
        listPharmacies(localPharms);

        Log.d(TAG, "Building hashmap ...");
        Map<String, Pharm> map = new HashMap<>();
        for (Pharm localPharm : localPharms) {
            map.put(localPharm.getRefference(), localPharm);
        }
        Log.d(TAG, "Built hashmap with " + map.size() + " entries");

        int added = 0;
        for (Pharm placePharm : placesPharms) {
            Log.d(TAG, "Comparing " + placePharm.getName());
            Pharm match = map.get(placePharm.getRefference());
            if (match == null) {
                Log.d(TAG, "Pharmacy " + placePharm.getName() + " is going to be added");
                getHelper().getPharmDao().create(placePharm);
                added++;
            } else {
                Log.d(TAG, "Already have the " + match.getName() + " pharmacy, ignoring.");
            }
        }
        Log.d(TAG, "Sync completed. Added " + added + " pharmacies.");


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

    @Debug
    public void listPharmacies(List<Pharm> ps) {
        for (Pharm p : ps) {
            Log.d(TAG, "Pharmacy [" + p.getName() + "}");
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
