package ro.laflamme.meditrack;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.j256.ormlite.android.apptools.OpenHelperManager;



public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ViewPager pager;
    private Toolbar toolbar;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    public DatabaseHelper dbHelper;
    CharSequence Titles[]={"Pharms","Map","Meds"};
    int NumberOfTabs = 3;
    private MediLocation mediLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        adapter = new ViewPagerAdapter(getFragmentManager(),Titles,NumberOfTabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs =(SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position){
                return getResources().getColor(R.color.primary_dark);
            }
        });

        tabs.setViewPager(pager);

        pager.setCurrentItem(2);

        mediLocation = MediLocation.getInstance(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mediLocation.connectApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.addToBackStack(tab.getTag().toString());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }



    @Override
    protected void onPause() {
        super.onPause();
        mediLocation.stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediLocation.disconnectApi();
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
