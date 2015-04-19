package ro.laflamme.meditrack;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by loopiezlol on 19.04.2015.
 */
@DatabaseTable(tableName="pharms")
public class Pharm extends Item {

    public static final String KEY_ID ="_id";
    public static final String KEY_NAME ="name";
    public static final String KEY_DESC="desc";

    @DatabaseField(id = true, columnName = KEY_ID)
    private int id;
    @DatabaseField(columnDefinition = KEY_NAME)
    private String name;
    @DatabaseField(columnDefinition = KEY_DESC)
    private String desc;

    public Pharm() {
    }

    public Pharm(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Pharm(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
