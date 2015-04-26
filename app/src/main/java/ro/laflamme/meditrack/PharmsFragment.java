package ro.laflamme.meditrack;

import android.app.Fragment;
import android.app.ListFragment;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class PharmsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Pharm>> {

    private static final int LOADER_ID = 1;

    private ArrayAdapter adapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        setEmptyText("No pharms.");
        setListAdapter(adapter);


        getLoaderManager().initLoader(LOADER_ID, null, this);
    }



    @Override
    public Loader<List<Pharm>> onCreateLoader(int id, Bundle args){
        return new PharmsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Pharm>> loader, List<Pharm> data){
        adapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Pharm>> loader){
        adapter.clear();
    }

}