package ro.laflamme.meditrack.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Loader;
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
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ro.laflamme.meditrack.MediLocation;
import ro.laflamme.meditrack.exception.NoGpsException;
import ro.laflamme.meditrack.PharmDetailFragment;
import ro.laflamme.meditrack.PharmsLoader;
import ro.laflamme.meditrack.R;
import ro.laflamme.meditrack.Sync;
import ro.laflamme.meditrack.domain.Pharm;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class PharmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pharm>> {

    private static final String TAG = "PharmsFragment";
    private static final int LOADER_ID = 1;

    private PharmsAdapter mAdapter;
    private FloatingActionButton mFab;
    private ListView mListView;
    private long mLastClickTime =0;
    android.support.v7.app.ActionBar action;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.pharms_fragment, null);
        mListView = (ListView) rootView.findViewById(R.id.pharms_list);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        mAdapter = new PharmsAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        final ExecutorService es = Executors.newFixedThreadPool(1);
        final MediLocation mediLocation = MediLocation.getInstance(getActivity());

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Sync sync = new Sync(getActivity());
                final int RADIUS = 500;

                try {
                    final double LAT = mediLocation.getLatitude();
                    final double LNG = mediLocation.getLongitude();

                    es.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sync.syncPharms(RADIUS, LAT, LNG);
                                getLoaderManager().restartLoader(LOADER_ID, null, PharmsFragment.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });



                } catch (NoGpsException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "You need GPS.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean doubleClickThreat = SystemClock.elapsedRealtime() - mLastClickTime < 1000;
                if (doubleClickThreat) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                openDetailFragment(position);
            }
        });

        action = ((ActionBarActivity) getActivity()).getSupportActionBar();
        action.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public Loader<List<Pharm>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new PharmsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Pharm>> loader, List<Pharm> data) {
        Log.d(TAG, "onLoadFinished");
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();}


    @Override
    public void onLoaderReset(Loader<List<Pharm>> loader) {
        Log.d(TAG, "onLoaderReset");
    }


    private void openDetailFragment(int position) {
        Pharm pharm = mAdapter.getItem(position);
        Bundle data = new Bundle();
        data.putString("title", pharm.getName());
        data.putString("desc", pharm.getDesc());
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
}