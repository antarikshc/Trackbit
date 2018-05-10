package com.antarikshc.trackbit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.antarikshc.trackbit.data.HabitContract.HabitEntry;

public class HabitDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = HabitDbHelper.class.getSimpleName();

    /**
     * Name and version of database
     **/
    private static final String DATABASE_NAME = "trackbit.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor with Name and version
     **/
    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_HABITS_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HabitEntry.COLUMN_HABIT_NAME + " TEXT, "
                + HabitEntry.COLUMN_SUNDAY + " INTEGER, "
                + HabitEntry.COLUMN_MONDAY + " INTEGER, "
                + HabitEntry.COLUMN_TUESDAY + " INTEGER, "
                + HabitEntry.COLUMN_WEDNESDAY + " INTEGER, "
                + HabitEntry.COLUMN_THURSDAY + " INTEGER, "
                + HabitEntry.COLUMN_FRIDAY + " INTEGER, "
                + HabitEntry.COLUMN_SATURDAY + " INTEGER );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
