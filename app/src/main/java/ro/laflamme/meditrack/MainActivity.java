package ro.laflamme.meditrack;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import ro.laflamme.meditrack.db.DatabaseHelper;
import ro.laflamme.meditrack.fragment.MapCustomFragment;
import ro.laflamme.meditrack.fragment.MedsFragment;
import ro.laflamme.meditrack.fragment.PharmsFragment;
import ro.laflamme.meditrack.view.SlidingTabLayout;
import ro.laflamme.meditrack.view.ViewPagerAdapter;


public class MainActivity extends ActionBarActivity  {

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

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.primary_dark);
            }
        });

        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                getFragmentManager().popBackStack();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        pager.setCurrentItem(2);

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
//        mediLocation.stopLocationUpdates();
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
