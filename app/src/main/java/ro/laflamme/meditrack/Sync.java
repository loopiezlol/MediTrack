package ro.laflamme.meditrack;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by motan on 10.05.2015.
 */
public class Sync {

    /*
    This should always be called in background thread.
     */
    public static void syncPharms(int radius, double lat, double lng) throws Exception{
        List<Pharm> pharms = PlaceController.getPharms(radius, lat, lng);

        Map<String, Pharm> pharmMap = new HashMap<>();

        for (Pharm pharm : pharms) { pharmMap.put(pharm.getRefference(), pharm); }

        
    }

}
