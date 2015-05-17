package ro.laflamme.meditrack;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by loopiezlol on 16.05.2015.
 */
public class MedDetailFragment extends Fragment {


    private TextView title;
    private TextView desc;
    private Bundle data;
    private ActionBar action;

    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.med_detail, container, false);
        title = (TextView) v.findViewById(R.id.med_title);
        desc = (TextView) v.findViewById(R.id.med_desc);

        setHasOptionsMenu(true);
        action = ((ActionBarActivity) getActivity()).getSupportActionBar();
        action.setHomeButtonEnabled(true);
        action.setDisplayHomeAsUpEnabled(true);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        data =getArguments();
        title.setText(data.getString("title"));
        desc.setText(Html.fromHtml(data.getString("desc")), TextView.BufferType.SPANNABLE);





    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_detail_med, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                action.setDisplayHomeAsUpEnabled(false);
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   @Override
    public void onResume() {
       super.onResume();

       getView().setFocusableInTouchMode(true);
       getView().requestFocus();
       getView().setOnKeyListener(new View.OnKeyListener(){
           @Override
           public boolean onKey(View v, int keyCode, KeyEvent event){
               if(event.getAction()==KeyEvent.ACTION_UP && keyCode==KeyEvent.KEYCODE_BACK){
                   getActivity().getFragmentManager().popBackStack();
                   return true;
               }
               return false;
           }
       });
   }
}