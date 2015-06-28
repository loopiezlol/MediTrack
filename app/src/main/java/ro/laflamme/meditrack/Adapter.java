package ro.laflamme.meditrack;

import ro.laflamme.meditrack.places.models.Place;

/**
 * Created by motan on 10.05.2015.
 */
public class Adapter {

    public static Pharm toPharm(Place place) {
        Pharm pharm = new Pharm();
        pharm.setName(place.getName());
        pharm.setDesc(place.getAddress());
        pharm.setRefference(place.getReference());
        pharm.setLatitude(place.getLatitude());
        pharm.setLongitude(place.getLongitude());
        return pharm;
    }
}
