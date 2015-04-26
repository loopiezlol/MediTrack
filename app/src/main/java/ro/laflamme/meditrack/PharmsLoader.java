package ro.laflamme.meditrack;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import java.util.List;

/**
 * Created by loopiezlol on 19.04.2015.
 */
public class PharmsLoader extends AsyncTaskLoader<List<Pharm>> {
    public DatabaseHelper dbHelper;
    private List<Pharm> mData;

    public PharmsLoader(Context context) { super(context); }

    @Override
    public List<Pharm> loadInBackground() {
        return getHelper().getPharmDao().queryForAll();
    }

    @Override
    public void deliverResult(List<Pharm> data) {
        if (isReset()) {
            releaseHelper();
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        releaseHelper();
    }

    @Override
    public void onCanceled(List<Pharm> data) {
        releaseHelper();
    }

    private void releaseHelper() {
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }


    private DatabaseHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
        }
        return dbHelper;
    }
}
