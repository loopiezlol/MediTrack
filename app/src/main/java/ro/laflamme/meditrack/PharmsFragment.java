package ro.laflamme.meditrack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class PharmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pharm>> {

    private static final int LOADER_ID = 1;
    private static final String TAG = "PharmsFragment";

    private PharmsAdapter mAdapter;
    private FloatingActionButton mFab;
    private ListView mListView;


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
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                disableDoubleTap();
                openDetailFragment(position);
            }
        });

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
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLoaderReset(Loader<List<Pharm>> loader) {
        Log.d(TAG, "onLoaderReset");
    }


    private void disableDoubleTap() {
        mListView.setEnabled(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setEnabled(true);
            }
        }, 150);
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


}