package ro.laflamme.meditrack;


import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class MapCustomFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = MapCustomFragment.class.getSimpleName();
    private GoogleMap googleMap;
    private Map<Marker,Pharm> markersMap = new HashMap<Marker,Pharm>();
    private ActionBar action;
    private List<Pharm> pharmList = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.map_fragment, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){


        try{
            initilizeMap();
        } catch (Exception e){
            e.printStackTrace();
        }

        googleMap.setMyLocationEnabled(true);

        googleMap.setOnInfoWindowClickListener(this);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        action = ((ActionBarActivity) getActivity()).getSupportActionBar();
        //action.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        action.invalidateOptionsMenu();



    }


    private void initilizeMap(){
        if (googleMap == null) {
            googleMap = getMapFragment().getMap();


            if (googleMap == null) {
                Toast.makeText(getActivity(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();
        initilizeMap();
    }*/

    public class populateMap extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            pharmList = dbHelper.getPharmDao().queryForAll();
            Toast.makeText(getActivity(),pharmList.get(0).getDesc(),Toast.LENGTH_SHORT).show();
            for(Pharm pharm : pharmList)
            {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(pharm.getLatitude(), pharm.getLongitude()))
                        .title(pharm.getName()).snippet(pharm.getDesc())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                markersMap.put(marker,pharm);
            }

            return null;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_map, menu);

    }


    private MapFragment getMapFragment() {
        FragmentManager fm = null;

        Log.d(TAG, "sdk: " + Build.VERSION.SDK_INT);
        Log.d(TAG, "release: " + Build.VERSION.RELEASE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "using getFragmentManager");
            fm = getFragmentManager();
        } else {
            Log.d(TAG, "using getChildFragmentManager");
            fm = getChildFragmentManager();
        }

        return (MapFragment) fm.findFragmentById(R.id.map);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Pharm pharm = markersMap.get(marker);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pharm", pharm);
        MapDetailFragment dialog = new MapDetailFragment();
        dialog.setArguments(bundle);
        dialog.show(getActivity());



    }

}