package ro.laflamme.meditrack.fragment;


import android.location.Location;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.laflamme.meditrack.MarkerPharmManager;
import ro.laflamme.meditrack.R;
import ro.laflamme.meditrack.db.OrmFragment;
import ro.laflamme.meditrack.domain.Pharm;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class MapCustomFragment extends OrmFragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLoadedCallback{

    private static final String TAG = MapCustomFragment.class.getSimpleName();
    private GoogleMap googleMap;
    private Map<Marker,Pharm> markersMap = new HashMap<Marker,Pharm>();
    private ActionBar action;
    private List<Pharm> pharmList = new ArrayList<>();
    private Location location;
    private LatLng myLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        initilizeMap();

        googleMap.setMyLocationEnabled(true);

        googleMap.setOnInfoWindowClickListener(this);

        googleMap.setOnMapLoadedCallback(this);
        new PopulateMapAsyncTask().execute(); // load markers


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



    @Override
    public void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public void onMapLoaded() {
        if(googleMap!=null){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(googleMap.getMyLocation().getLatitude(),googleMap.getMyLocation().getLongitude()),14.0f));

        }
    }

    public class PopulateMapAsyncTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            pharmList = getHelper().getPharmDao().queryForAll();
//            Toast.makeText(getActivity(),pharmList.get(0).getDesc(),Toast.LENGTH_SHORT).show();
//            releaseHelper();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MarkerPharmManager.syncMarkersWithPharms(pharmList, googleMap, markersMap);
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

 /*       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "using getFragmentManager");
            fm = getFragmentManager();
        } else {
            Log.d(TAG, "using getChildFragmentManager");
            fm = getChildFragmentManager();
        }*/
        fm = getFragmentManager();

        return (MapFragment) fm.findFragmentById(R.id.map);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Pharm pharm = markersMap.get(marker);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pharm", pharm);
        MapDialogFragment dialog = new MapDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(getActivity());

    }

    public void focusMap(double latitude, double longitude){
        LatLng latLng = new LatLng(latitude,longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }

    public String computeDistance(double latitude, double longitude){


        LatLng latLng = new LatLng(latitude,longitude);
        LatLng myLatLng = new LatLng(googleMap.getMyLocation().getLatitude(),googleMap.getMyLocation().getLongitude());

        Double distance = SphericalUtil.computeDistanceBetween(myLatLng,latLng);



        return Double.toString(distance)+" km";
    }




}