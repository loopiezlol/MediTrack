package ro.laflamme.meditrack;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import java.util.List;

/**
 * Created by loopiezlol on 19.04.2015.
 */
public class PharmsLoader extends AsyncTaskLoader<List<Pharm>> {
    private static final String TAG = "PharmsLoader";
    public DatabaseHelper dbHelper;
    private List<Pharm> mData;

    public PharmsLoader(Context context) { super(context); }

    @Override
    public List<Pharm> loadInBackground() {
        Log.d(TAG, "loadInBackground");
        List<Pharm> results = getHelper().getPharmDao().queryForAll();
        Log.d(TAG, "loadInBackground delivers " + results.size() + " pharms");
        return results;
    }

    @Override
    public void deliverResult(List<Pharm> data) {
        Log.d(TAG, "deliverResult");
        if (isReset()) {
            Log.d(TAG, "Loader is reset. exiting");
            releaseHelper();
            return;
        }

        List<Pharm> oldData = mData;
        mData = data;

        if (isStarted()) {
            Log.d(TAG, "loader is started, delivering");
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            Log.d(TAG, "Should recycle oldData");
//            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");
        if (mData != null) {
            deliverResult(mData);
        }
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        Log.d(TAG, "onStopLoading");
//        cancelLoad();
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset");
        onStopLoading();
//        releaseHelper();
        if (mData != null) {
            mData = null;
        }
    }

    @Override
    public void onCanceled(List<Pharm> data) {
        Log.d(TAG, "onCanceled");
//        releaseHelper();
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
