package ro.laflamme.meditrack;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by loopiezlol on 19.04.2015.
 */
@DatabaseTable(tableName="pharms")
public class Pharm extends Item {

    public static final String KEY_NAME ="name";
    public static final String KEY_DESC="desc";
    private static final String KEY_REFFERENCE = "refference";

    @DatabaseField(columnDefinition = KEY_NAME)
    private String name;
    @DatabaseField(columnDefinition = KEY_DESC)
    private String desc;
    @DatabaseField(columnDefinition = KEY_REFFERENCE, id = true)
    private String refference;

    public Pharm() {
    }


    public Pharm(String name, String desc) {
        this.name = name;
        this.desc = desc;
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

    @Override
    public String getTitle() {
        return this.name;
    }

    @Override
    public String getSubtitle() {
        return this.desc;
    }

    public String getRefference() {
        return refference;
    }

    public void setRefference(String refference) {
        this.refference = refference;
    }
}
