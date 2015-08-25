package ro.laflamme.meditrack.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ro.laflamme.meditrack.MediLocation;
import ro.laflamme.meditrack.PharmDetailFragment;
import ro.laflamme.meditrack.PharmsLoader;
import ro.laflamme.meditrack.R;
import ro.laflamme.meditrack.Sync;
import ro.laflamme.meditrack.domain.Pharm;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class PharmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pharm>>, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "PharmsFragment";
    private static final int LOADER_ID = 1;

    private PharmsAdapter mAdapter;
    private FloatingActionButton mFab;
    private ListView mListView;
    private long mLastClickTime = 0;
    private android.support.v7.app.ActionBar mActionBar;
    private MediLocation mMediLocation;
    private Sync mSync;

    private MaterialDialog mProgressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.pharms_fragment, null);
        mListView = (ListView) rootView.findViewById(R.id.pharms_list);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediLocation = MediLocation.getInstance(getActivity(), this);
        mMediLocation.connectApi();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediLocation.disconnectApi();
    }

/*    @Override
    public void onPause() {
        super.onPause();
        mMediLocation.stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMediLocation.startLocationUpdates();
    }*/

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(savedInstanceState);

    }

    private void initComponents(Bundle savedInstanceState) {
        mProgressDialog = new MaterialDialog.Builder(getActivity())
                .title("Syncing pharms")
                .content("Please wait.")
                .progress(true, 0)
                .show();

        mSync = new Sync(getActivity());


        mAdapter = new PharmsAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        final ExecutorService es = Executors.newFixedThreadPool(1);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncAndRestartLoader(5000, mMediLocation.getLatitude(), mMediLocation.getLongitude());
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetailFragmentWithDoubleClickThreat(position);
            }
        });

        mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
    }

    private void openDetailFragmentWithDoubleClickThreat(int position) {
        boolean doubleClickThreat = SystemClock.elapsedRealtime() - mLastClickTime < 1000;
        if (doubleClickThreat) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        openDetailFragment(position);
    }


    private void syncAndRestartLoader(final int radius, final double lat, final double lng) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mSync.syncPharms(radius, lat, lng);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getLoaderManager().restartLoader(LOADER_ID, null, PharmsFragment.this);
            }
        }.execute();
    }


    @Override
    public Loader<List<Pharm>> onCreateLoader(int id, Bundle args) {
        return new PharmsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Pharm>> loader, List<Pharm> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLoaderReset(Loader<List<Pharm>> loader) {
        Log.d(TAG, "onLoaderReset");
    }


    private void openDetailFragment(int position) {
        Pharm pharm = mAdapter.getItem(position);
        Bundle data = new Bundle();
        data.putSerializable("pharm", pharm);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PharmDetailFragment detailFragment = new PharmDetailFragment();
        detailFragment.setArguments(data);
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
        fragmentTransaction.add(R.id.pharm_list_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_pharm, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Called when GooglePlayServices connected, can get location.
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        mMediLocation.syncLastLocation();
        mMediLocation.startLocationUpdates();

        mProgressDialog.hide();

        syncAndRestartLoader(500, mMediLocation.getLatitude(), mMediLocation.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}