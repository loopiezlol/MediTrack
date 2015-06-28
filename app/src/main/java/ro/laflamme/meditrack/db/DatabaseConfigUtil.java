package ro.laflamme.meditrack.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;

import ro.laflamme.meditrack.domain.Pharm;

/**
 * Created by loopiezlol on 19.04.2015.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            Pharm.class
    };

    public static void main(String[] args) throws Exception {
        writeConfigFile(new File("app/src/main/res/raw/ormlite_config.txt"), classes);
    }

    public static final int DATABASE_VERSION = 10;
}
