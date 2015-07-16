package ro.laflamme.meditrack.db;

import android.app.Fragment;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by motan on 07.07.2015.
 */
public class OrmFragment extends Fragment{

    protected DatabaseHelper dbHelper;

    protected void releaseHelper() {
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }


    protected DatabaseHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return dbHelper;
    }
}
