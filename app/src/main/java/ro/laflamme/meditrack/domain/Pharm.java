package ro.laflamme.meditrack.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by loopiezlol on 19.04.2015.
 */
@DatabaseTable(tableName="pharms")
public class Pharm implements Serializable {

    @DatabaseField(columnDefinition = "place_id", id = true)
    private String placeId;

    @DatabaseField(columnDefinition = "name")
    private String name;

    @DatabaseField(columnDefinition = "desc")
    private String desc;

    @DatabaseField(columnDefinition = "refference")
    private String refference;

    @DatabaseField(columnDefinition = "latitude")
    private double latitude;

    @DatabaseField(columnDefinition = "longitude")
    private double longitude;

    @DatabaseField(columnDefinition = "open_now")
     private boolean openNow;

    public Pharm() {
    }

    public Pharm(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Pharm(double longitude, double latitude, String desc, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.desc = desc;
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getRefference() {
        return refference;
    }

    public void setRefference(String refference) {
        this.refference = refference;
    }
}
