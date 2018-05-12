package com.antarikshc.trackbit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.antarikshc.trackbit.data.HabitContract.HabitEntry;
import com.antarikshc.trackbit.data.HabitDbHelper;

import java.util.Calendar;

public class EditorActivity extends AppCompatActivity {

    /**
     * Global Declarations
     **/
    Intent intent;

    EditText habitName, habitNumber;
    FloatingActionButton fabDeleteHabit;

    Calendar calendar;
    int currentDay;

    //to create HabitDbHelper instance
    private HabitDbHelper mDbHelper;

    int habitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        habitName = findViewById(R.id.edit_habit_name);
        habitNumber = findViewById(R.id.habit_number);
        fabDeleteHabit = findViewById(R.id.fab_delete_habit);

        //get current day
        //we won't allow user to edit previous day activities, it defeats the whole purpose of the app
        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        intent = getIntent();
        habitId = intent.getIntExtra("id", 10000);  //default value is just avoidance

        //default value may not matter
        if (intent.getBooleanExtra("dataPresent", true)) {

            habitName.setText(intent.getStringExtra("name"));
            habitNumber.setText(String.valueOf(intent.getIntExtra("dayNum", 0)));

            //fab delete is GONE by default, only visible when user is updating habit
            fabDeleteHabit.setVisibility(View.VISIBLE);
            fabDeleteHabit.setClickable(true);

        }
        //Initialize HabitDbHelper
        mDbHelper = new HabitDbHelper(getApplicationContext());
    }

    public void insertPet(View view) {
        // Create and/or open a database to write to it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //using column array instead of switch-case to update habit of current day
        String[] columnNames = {HabitEntry.COLUMN_SUNDAY,
                HabitEntry.COLUMN_MONDAY,
                HabitEntry.COLUMN_TUESDAY,
                HabitEntry.COLUMN_WEDNESDAY,
                HabitEntry.COLUMN_THURSDAY,
                HabitEntry.COLUMN_FRIDAY,
                HabitEntry.COLUMN_SATURDAY};

        try {

            if (intent.getBooleanExtra("dataPresent", true)) {

                ContentValues values = new ContentValues();
                values.put(HabitEntry.COLUMN_HABIT_NAME, habitName.getText().toString());
                values.put(columnNames[currentDay - 1], Integer.parseInt(habitNumber.getText().toString()));

                db.update(HabitEntry.TABLE_NAME,
                        values,
                        HabitEntry._ID + " = ?",
                        new String[]{String.valueOf(habitId)});

            } else {

                if (habitName.getText().length() > 0 && !habitName.getText().toString().isEmpty()) {


                    //Initialize records array to prevent null values in columns
                    Integer[] records = {0, 0, 0, 0, 0, 0, 0};

                    if (habitNumber.getText().length() > 0 && !habitNumber.getText().toString().isEmpty()) {
                        //get the input from user and update the array
                        records[currentDay - 1] = Integer.parseInt(habitNumber.getText().toString());

                    }

                    ContentValues values = new ContentValues();
                    values.put(HabitEntry.COLUMN_HABIT_NAME, habitName.getText().toString());

                    //good thing that we have column names array xD
                    for (int i = 0; i <= 6; i++) {
                        values.put(columnNames[i], records[i]);
                    }

                    //insert query to add new habit
                    db.insert(HabitEntry.TABLE_NAME, null, values);

                } else {
                    Toast.makeText(this, "Give habit a name!", Toast.LENGTH_SHORT).show();
                }
            }

        } finally {
            //Close and Finish it.
            db.close();
            finish();
        }
    }

    public void deleteHabit(View view) {
        // Create and/or open a database to write to it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(HabitEntry.TABLE_NAME,
                HabitEntry._ID + " = ?",
                new String[]{String.valueOf(habitId)});
        db.close();
        finish();
    }

    public void increment(View view) {
        int i = Integer.parseInt(habitNumber.getText().toString());
        i += 1;
        habitNumber.setText(String.valueOf(i));
    }

    public void decrement(View view) {
        int i = Integer.parseInt(habitNumber.getText().toString());
        i -= 1;
        habitNumber.setText(String.valueOf(i));
    }

}
