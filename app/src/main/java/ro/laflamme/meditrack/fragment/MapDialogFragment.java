package ro.laflamme.meditrack.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import ro.laflamme.meditrack.R;
import ro.laflamme.meditrack.domain.Pharm;

/**
 * Created by loopiezlol on 24.05.2015.
 */
public class MapDialogFragment extends DialogFragment {

    Bundle bundle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("")
                .customView(R.layout.dialog_map_detail, true)
                .build();

        bundle=getArguments();
        Pharm pharm = (Pharm) bundle.getSerializable("pharm");
        dialog.setTitle(pharm.getName());
        TextView tv= (TextView) dialog.getCustomView().findViewById(R.id.desc_pharm);
        tv.setText(pharm.getDesc());
        return dialog;
    }


    public void show(Activity context) {
        show(context.getFragmentManager(), "Pharm detail");
    }

}
