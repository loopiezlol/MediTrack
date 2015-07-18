package ro.laflamme.meditrack;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import ro.laflamme.meditrack.db.DatabaseHelper;
import ro.laflamme.meditrack.fragment.MapCustomFragment;
import ro.laflamme.meditrack.view.SlidingTabLayout;
import ro.laflamme.meditrack.view.ViewPagerAdapter;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    public static ViewPager pager;
    private Toolbar toolbar;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    public DatabaseHelper dbHelper;
    private MediLocation mediLocation;

    public static final int NUMBER_OF_TABS = 3;
    public static final CharSequence TITLES[]={"Pharms","Map","Meds"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        adapter = new ViewPagerAdapter(getFragmentManager(), TITLES, NUMBER_OF_TABS);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.primary_dark);
            }
        });

        tabs.setViewPager(pager);

        pager.setCurrentItem(0);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getFragmentManager().popBackStack();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mediLocation = MediLocation.getInstance(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mediLocation.connectApi();
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

    public void goOnMap(double latitude,double longitude){
        pager.setCurrentItem(1);
        MapCustomFragment fragment = (MapCustomFragment)adapter.getRegisteredFragment(pager.getCurrentItem());
        fragment.focusMap(latitude, longitude);
    }

    


}
