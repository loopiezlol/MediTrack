package ro.laflamme.meditrack;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class PharmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pharm>> {

    private static final int LOADER_ID = 1;
    private static final String TAG = "PharmsFragment";

    private PharmsAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.pharms_fragment, null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.pharms_recycler_view);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        adapter = new PharmsAdapter(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setClickable(true);


        getLoaderManager().initLoader(LOADER_ID, null, this);

        final ExecutorService es = Executors.newFixedThreadPool(1);
        final MediLocation mediLocation = MediLocation.getInstance(getActivity());

        fab.setOnClickListener(new View.OnClickListener() {
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
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onLoaderReset(Loader<List<Pharm>> loader) {
        Log.d(TAG, "onLoaderReset");
    }

}