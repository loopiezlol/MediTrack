package ro.laflamme.meditrack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MedsFragment extends Fragment {

    ArrayList<Med> listMeds;
    ListView listView;
    MedsAdapter adapter;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edit_search;
    long mLastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.meds_fragment, container, false);

        setHasOptionsMenu(true);

        listMeds = new ArrayList<Med>();

        Med med1 = new Med(1, "Paracetamol", "<CENTER>\n" +
                "<H1>A Simple Sample Web Page</H1>\n" +
                "\n" +
                " \n" +
                "\n" +
                " \n" +
                "\n" +
                " \n" +
                "\n" +
                "  <H4>By Sheldon Brown</H4>\n" +
                "\n" +
                "<H2>Demonstrating a few HTML features</H2>\n" +
                "\n" +
                "</CENTER>\n" +
                "\n" +
                "HTML is really a very simple language. It consists of ordinary text, with commands that are enclosed by \"<\" and \">\" characters, or bewteen an \"&\" and a \";\". <P>\n" +
                " \n" +
                "\n" +
                "You don't really need to know much HTML to create a page, because you can copy bits of HTML from other pages that do what you want, then change the text!<P>\n" +
                " \n" +
                "\n" +
                "This page shows on the left as it appears in your browser, and the corresponding HTML code appears on the right. The HTML commands are linked to explanations of what they do.\n" +
                " " +
                "<H3>Line Breaks</H3>\n" +
                "\n" +
                "HTML doesn't normally use line breaks for ordinary text. A white space of any size is treated as a single space. This is because the author of the page has no way of knowing the size of the reader's screen, or what size type they will have their browser set for.<P>\n" +
                "\n" +
                " \n" +
                "\n" +
                "If you want to put a line break at a particular place, you can use the \"<BR>\" command, or, for a paragraph break, the \"<P>\" command, which will insert a blank line. The heading command (\"<4></4>\") puts a blank line above and below the heading text.\n" +
                "\n");
        Med med2 = new Med(1, "Algocalmin", "Prospect\n" +
                "\n" +
                "Compozitie\n" +
                "Comprimate a 0,50 g natrium phenyl dimethylpyrazolonum methyl aminome-thansulfonicum. \n" +
                "Solutie injectabila 50%, natrium phenyl dimethylpyrazolonum methyl aminome-thansulfonicum. \n" +
                "Supozitoare pentru adulti: 1g natrium phenyl dimethylpyrazolonum methyl aminome-thansulfonicum, 1,53 g excipient. \n" +
                "Supozitoare pentru copii: 0,30 g natrium phenyl dimethylpyrazolonum methyl aminome-thansulfonicum, 0,82 g excipient. \n" +
                "\n" +
                "Actiune terapeutica\n" +
                "algocalminul este un analgezic puternic, inlocuind intr-o larga masura opiaceele,fara a determina efectele secundare obisnuite dupa administrarea acestora. Fiind un derivat pirazolonic, algocalminul are si o actiune antipiretica.");
        Med med3 = new Med(1, "Ketonal", "ba");

        listMeds.add(med1);
        listMeds.add(med2);
        listMeds.add(med3);

        listView = (ListView) v.findViewById(R.id.meds_list);


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new MedsAdapter(getActivity(), listMeds);
        listView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //close keyboard and searchbar
                if (isSearchOpened) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
                    handleMenuSearch();
                }

                doSearch("");
                openDetailFragment(position);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_med, menu);
        mSearchAction = menu.findItem(R.id.action_search);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        ActionBar action = ((ActionBarActivity) getActivity()).getSupportActionBar();
        action.setDisplayShowCustomEnabled(false);
        action.setDisplayShowTitleEnabled(true);
    }

    protected void handleMenuSearch() {

        //get the actionBar
        ActionBar action = ((ActionBarActivity) getActivity()).getSupportActionBar();


        //test if the search is open
        if (isSearchOpened) {

            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);

            //hide the keyboard

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);


            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_24dp));

            isSearchOpened = false;

            imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);

        } else {

            //open search entry
            action.setDisplayShowCustomEnabled(true);

            //custom view in action bar
            action.setCustomView(R.layout.search_bar);
            action.setDisplayShowTitleEnabled(false);

            edit_search = (EditText) action.getCustomView().findViewById(R.id.search_bar);

            //this is a listener to do a search when the user clicks on search button
            edit_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    doSearch(edit_search.getText().toString());
                }
            });
            edit_search.requestFocus();

            //open keyboard in focused mode
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edit_search, InputMethodManager.SHOW_IMPLICIT);

            //add close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_white_24dp));

            isSearchOpened = true;

        }
    }

    private void doSearch(String text) {
        adapter.getFilter().filter(text);
    }



    private void openDetailFragment(int position) {
        Med med = listMeds.get(position);
        Bundle data = new Bundle();
        data.putString("title", med.getName());
        data.putString("desc", med.getDesc());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MedDetailFragment detailFragment = new MedDetailFragment();
        detailFragment.setArguments(data);
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
        fragmentTransaction.add(R.id.med_list_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}