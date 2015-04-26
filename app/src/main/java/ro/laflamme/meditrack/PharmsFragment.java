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
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class PharmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pharm>> {

    private PharmsAdapter adapter;
    private RecyclerView recyclerView;

    public DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.pharms_fragment,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.pharms_recycler_view);
        initView();

        Toast.makeText(getActivity(),Integer.toString(getHelper().getPharmDao().queryForAll().size()), Toast.LENGTH_SHORT).show();


    }

    private void initView() {
        adapter = new PharmsAdapter(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setClickable(true);
    }



    @Override
    public Loader<List<Pharm>> onCreateLoader(int id, Bundle args){
        return new PharmsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Pharm>> loader, List<Pharm> data){
        List<Item> items = new ArrayList<Item>(data);
        adapter.setData(items);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Pharm>> loader){
        adapter.setData(null);
    }

    public DatabaseHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return dbHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }
}