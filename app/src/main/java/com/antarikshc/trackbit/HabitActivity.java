package com.antarikshc.trackbit;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HabitActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<HabitData>> {

    private static final String LOG_TAG = HabitActivity.class.getName();

    /**
     * Global declarations
     **/
    ListView habitsList;
    private CustomAdapter customAdapter;
    LoaderManager loaderManager;

    //Books loaded ID, default = 1 currently using single Loader
    private static int HABITS_LOADER_ID = 1;

    //FAB and TextViews
    FloatingActionButton fabMain, fabAdd, fabDelete;
    TextView textFabAdd, textFabDelete;

    //boolean value for open and close FAB's
    boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        habitsList = findViewById(R.id.habit_list);

        fabMain = findViewById(R.id.fab_default);
        fabAdd = findViewById(R.id.fab_add);
        fabDelete = findViewById(R.id.fab_delete);

        textFabAdd = findViewById(R.id.text_add);
        textFabDelete = findViewById(R.id.text_delete);

        scaleFab();

        customAdapter = new CustomAdapter(getApplicationContext(), new ArrayList<HabitData>());
        //setting customAdapter for book list view
        habitsList.setAdapter(customAdapter);

        //Run SQL queries in background thread
        loaderManager = getLoaderManager();
        loaderManager.initLoader(HABITS_LOADER_ID, null, this);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

    }

    @Override
    public Loader<ArrayList<HabitData>> onCreateLoader(int id, Bundle args) {
        return new HabitsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<HabitData>> loader, ArrayList<HabitData> data) {
        customAdapter.clear();
        if (data != null && !data.isEmpty()) {
            customAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<HabitData>> loader) {
        customAdapter.clear();
    }

    private void animateFab() {
        if (isFabOpen) {

            //translate down with fade out
            fabAdd.animate().translationYBy(80f).setDuration(100).setInterpolator(new AccelerateInterpolator());
            fabAdd.animate().alpha(0.0f).setDuration(100).setInterpolator(new LinearInterpolator());
            fabAdd.setClickable(false);

            textFabAdd.animate().translationYBy(80f).setDuration(100).setInterpolator(new AccelerateInterpolator());
            textFabAdd.animate().alpha(0.0f).setDuration(100).setInterpolator(new LinearInterpolator());

            fabDelete.animate().translationYBy(80f).setDuration(100).setInterpolator(new AccelerateInterpolator());
            fabDelete.animate().alpha(0.0f).setDuration(100).setInterpolator(new LinearInterpolator());
            fabDelete.setClickable(false);

            textFabDelete.animate().translationYBy(80f).setDuration(100).setInterpolator(new AccelerateInterpolator());
            textFabDelete.animate().alpha(0.0f).setDuration(100).setInterpolator(new LinearInterpolator());

            isFabOpen = false;

        } else {

            //translate up with fade in
            fabAdd.animate().translationYBy(-80f).setDuration(100).setInterpolator(new AccelerateInterpolator());
            fabAdd.animate().alpha(1.0f).setDuration(100).setInterpolator(new LinearInterpolator());
            fabAdd.setClickable(true);

            textFabAdd.animate().translationYBy(-80f).setDuration(100).setInterpolator(new AccelerateInterpolator());
            textFabAdd.animate().alpha(1.0f).setDuration(100).setInterpolator(new LinearInterpolator());

            fabDelete.animate().translationYBy(-80f).setDuration(100).setInterpolator(new AccelerateInterpolator());
            fabDelete.animate().alpha(1.0f).setDuration(100).setInterpolator(new LinearInterpolator());
            fabDelete.setClickable(true);

            textFabDelete.animate().translationYBy(-80f).setDuration(100).setInterpolator(new AccelerateInterpolator());
            textFabDelete.animate().alpha(1.0f).setDuration(100).setInterpolator(new LinearInterpolator());

            isFabOpen = true;

        }
    }

    private void scaleFab() {
        //make FAB buttons small and translate them down
        //ummm.. boiler plate maybe?
        fabAdd.animate().alpha(0.0f);
        fabAdd.animate().translationYBy(80f);
        fabAdd.setClickable(false);

        textFabAdd.animate().alpha(0.0f);
        textFabAdd.animate().translationYBy(80f);

        fabDelete.animate().alpha(0.0f);
        fabDelete.animate().translationYBy(80f);
        fabDelete.setClickable(false);

        textFabDelete.animate().alpha(0.0f);
        textFabDelete.animate().translationYBy(80f);
    }
}
