package com.antarikshc.trackbit;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.antarikshc.trackbit.data.HabitContract.HabitEntry;
import com.antarikshc.trackbit.data.HabitDbHelper;

import java.util.ArrayList;

public class HabitsLoader extends AsyncTaskLoader<ArrayList<HabitData>> {

    //to create HabitDbHelper instance
    private HabitDbHelper mDbHelper;

    HabitsLoader(Context mContext) {
        super(mContext);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<HabitData> loadInBackground() {

        // Create an empty ArrayList that we can start adding habits to
        ArrayList<HabitData> habits = new ArrayList<>();

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new HabitDbHelper(getContext());
        Cursor cursor = null;

        // Create and/or open a database to write to it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        try {
            // to get a Cursor that contains all rows from the pets table.
            cursor = db.query(HabitEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            //Cursor sometimes return columns in unordered fashion
            //Use this string array to retrieve column index
            String[] columnNames = {HabitEntry.COLUMN_SUNDAY,
                    HabitEntry.COLUMN_MONDAY,
                    HabitEntry.COLUMN_TUESDAY,
                    HabitEntry.COLUMN_WEDNESDAY,
                    HabitEntry.COLUMN_THURSDAY,
                    HabitEntry.COLUMN_FRIDAY,
                    HabitEntry.COLUMN_SATURDAY};

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {

                String name;
                Integer id;
                //Initialize records array to prevent null values in columns
                Integer[] records = {0, 0, 0, 0, 0, 0, 0};

                id = cursor.getInt(0);
                name = cursor.getString(1);
                for (int i = 0; i <= 6; i++) {
                    records[i] = cursor.getInt(cursor.getColumnIndex(columnNames[i]));
                }

                habits.add(new HabitData(id, name, records));

            }

            return habits;

        } finally {
            cursor.close();
            db.close();
        }
    }
}
