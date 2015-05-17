package ro.laflamme.meditrack;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by loopiezlol on 12.05.2015.
 */
@DatabaseTable(tableName="meds")
public class Med {

    public static final String KEY_ID="_id";
    public static final String KEY_NAME="name";
    public static final String KEY_DESC="desc";

    @DatabaseField(id=true, columnName = KEY_ID)
    private int id;
    @DatabaseField(columnDefinition = KEY_NAME)
    private String name;
    @DatabaseField(columnDefinition = KEY_DESC)
    private String desc;



    public Med(int id, String name, String desc){
        this.id=id;
        this.desc=desc;
        this.name=name;
    }

    public Med(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Med() {
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
}
