package com.antarikshc.trackbit;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.antarikshc.trackbit.data.EditorActivity;
import com.antarikshc.trackbit.data.HabitContract.HabitEntry;
import com.antarikshc.trackbit.data.HabitDbHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class HabitActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<HabitData>> {

    private static final String LOG_TAG = HabitActivity.class.getName();

    /**
     * Global declarations
     **/
    ListView habitsList;
    private CustomAdapter customAdapter;
    LoaderManager loaderManager;

    //to create HabitDbHelper instance
    private HabitDbHelper mDbHelper;

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

        //get current day
        Calendar calendar = Calendar.getInstance();
        final int currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        habitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LayoutInflater factory = LayoutInflater.from(HabitActivity.this);
                final View dialogView = factory.inflate(R.layout.popup_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(HabitActivity.this).create();

                dialog.setView(dialogView);

                //we don't want title
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.popup_dialog);

                //bind views from dialog layout
                final TextView dialogHabitNumber = dialogView.findViewById(R.id.dialog_habit_number);
                ImageView dialogAdd = dialogView.findViewById(R.id.dialog_add_button);
                ImageView dialogSub = dialogView.findViewById(R.id.dialog_sub_button);
                Button dialogSave = dialogView.findViewById(R.id.dialog_save);

                //get habit records and update habit number textview
                HabitData currentHabit = customAdapter.getItem(position);
                final Integer records[] = currentHabit.getHabitDays();
                dialogHabitNumber.setText(String.valueOf(records[currentDay - 1]));

                final Integer habitId = currentHabit.getHabitId();

                dialogAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = Integer.parseInt(dialogHabitNumber.getText().toString());
                        i += 1;
                        dialogHabitNumber.setText(String.valueOf(i));
                    }
                });

                dialogSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = Integer.parseInt(dialogHabitNumber.getText().toString());
                        i -= 1;
                        dialogHabitNumber.setText(String.valueOf(i));
                    }
                });

                dialogSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //just close dialog box if user hasn't changed value
                        if (dialogHabitNumber.getText().toString().equals
                                (String.valueOf(records[currentDay - 1])
                                )) {
                            dialog.dismiss();
                        } else {

                            //get value from textview and update records array
                            records[currentDay - 1] = Integer.parseInt(dialogHabitNumber.getText().toString());

                            //using column array instead of switch-case to update habit of current day
                            String[] columnNames = {HabitEntry.COLUMN_SUNDAY,
                                    HabitEntry.COLUMN_MONDAY,
                                    HabitEntry.COLUMN_TUESDAY,
                                    HabitEntry.COLUMN_WEDNESDAY,
                                    HabitEntry.COLUMN_THURSDAY,
                                    HabitEntry.COLUMN_FRIDAY,
                                    HabitEntry.COLUMN_SATURDAY};

                            SQLiteDatabase db = mDbHelper.getWritableDatabase();

                            ContentValues values = new ContentValues();

                            //good thing that we have column names array xD
                            for (int i = 0; i <= 6; i++) {
                                values.put(columnNames[i], records[i]);
                            }

                            db.update(HabitEntry.TABLE_NAME, values,
                                    HabitEntry._ID + " = ?",
                                    new String[]{String.valueOf(habitId)});

                            db.close();

                            loaderManager.restartLoader(HABITS_LOADER_ID, null, HabitActivity.this);

                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();

            }
        });

        habitsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                HabitData currentHabit = customAdapter.getItem(position);

                if (currentHabit != null) {

                    Intent intent = new Intent(getApplicationContext(), EditorActivity.class);

                    //probably easy way to let EditorActivity know if the data is present or not
                    intent.putExtra("dataPresent", true);

                    intent.putExtra("id", currentHabit.getHabitId());
                    intent.putExtra("name", currentHabit.getHabitName());

                    Integer records[] = currentHabit.getHabitDays();

                    intent.putExtra("dayNum", records[currentDay - 1]);

                    startActivity(intent);

                }
                return true;
            }
        });

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        mDbHelper = new HabitDbHelper(this);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_habit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.insert_dummy_data:
                insertDummy();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.clear_records:
                clearRecords();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addHabit(View view) {

        Intent intent = new Intent(getApplicationContext(), EditorActivity.class);

        //probably easy way to let EditorActivity know if the data is present or not
        intent.putExtra("dataPresent", false);

        animateFab();
        startActivity(intent);

    }

    public void deleteAll(View view) {
        // Create and/or open a database to write to it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(HabitEntry.TABLE_NAME, null, null);
        db.close();
        animateFab();

        loaderManager.restartLoader(HABITS_LOADER_ID, null, this);
    }

    private void insertDummy() {
        // Create and/or open a database to write to it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, "Learn something New");
        values.put(HabitEntry.COLUMN_SUNDAY, 3);
        values.put(HabitEntry.COLUMN_MONDAY, 0);
        values.put(HabitEntry.COLUMN_TUESDAY, 2);
        values.put(HabitEntry.COLUMN_WEDNESDAY, 4);
        values.put(HabitEntry.COLUMN_THURSDAY, 0);
        values.put(HabitEntry.COLUMN_FRIDAY, 7);
        values.put(HabitEntry.COLUMN_SATURDAY, 12);
        db.insert(HabitEntry.TABLE_NAME, null, values);
        db.close();

        loaderManager.restartLoader(HABITS_LOADER_ID, null, this);
    }

    private void clearRecords() {
        // Create and/or open a database to write to it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_SUNDAY, 0);
        values.put(HabitEntry.COLUMN_MONDAY, 0);
        values.put(HabitEntry.COLUMN_TUESDAY, 0);
        values.put(HabitEntry.COLUMN_WEDNESDAY, 0);
        values.put(HabitEntry.COLUMN_THURSDAY, 0);
        values.put(HabitEntry.COLUMN_FRIDAY, 0);
        values.put(HabitEntry.COLUMN_SATURDAY, 0);

        db.update(HabitEntry.TABLE_NAME, values, null, null);

        loaderManager.restartLoader(HABITS_LOADER_ID, null, this);
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
