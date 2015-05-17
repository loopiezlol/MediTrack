package ro.laflamme.meditrack;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class PharmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pharm>> {

    private static final int LOADER_ID = 1;

    private PharmsAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pharms_fragment, null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.pharms_recycler_view);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);

        adapter = new PharmsAdapter(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setClickable(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<List<Pharm>> onCreateLoader(int id, Bundle args){
        return new PharmsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Pharm>> loader, List<Pharm> data){
        adapter.setData(data);
    }



    @Override
    public void onLoaderReset(Loader<List<Pharm>> loader){
        adapter.clear();
    }

}