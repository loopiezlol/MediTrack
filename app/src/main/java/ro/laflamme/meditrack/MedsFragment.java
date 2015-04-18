package ro.laflamme.meditrack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class MedsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v =inflater.inflate(R.layout.meds_fragment,container,false);
    return v;
}
}