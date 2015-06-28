package ro.laflamme.meditrack;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by loopiezlol on 19.04.2015.
 */
@DatabaseTable(tableName="pharms")
public class Pharm implements Serializable {

    public static final String KEY_NAME ="name";
    public static final String KEY_DESC = "desc";
    private static final String KEY_REFFERENCE = "refference";
    public static final String KEY_LAT="latitude";
    public static final String KEY_LONG="longitude";

    @DatabaseField(columnDefinition = KEY_NAME)
    private String name;
    @DatabaseField(columnDefinition = KEY_DESC)
    private String desc;
    @DatabaseField(columnDefinition = KEY_REFFERENCE, id = true)
    private String refference;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @DatabaseField(columnDefinition = KEY_LAT)
    private double latitude;
    @DatabaseField(columnDefinition = KEY_LONG)
    private double longitude;

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
