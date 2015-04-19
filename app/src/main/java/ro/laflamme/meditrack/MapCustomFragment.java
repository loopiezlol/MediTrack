package ro.laflamme.meditrack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class MapCustomFragment extends Fragment {

    private static final String TAG = MapCustomFragment.class.getSimpleName();
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.map_fragment ,container,false);

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
}