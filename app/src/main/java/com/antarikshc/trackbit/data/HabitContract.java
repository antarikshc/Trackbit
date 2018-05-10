package com.antarikshc.trackbit.data;

import android.provider.BaseColumns;

public final class HabitContract {

    private HabitContract() {
    }

    public static final class HabitEntry implements BaseColumns {

        public final static String TABLE_NAME = "habits";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HABIT_NAME = "name";
        public final static String COLUMN_SUNDAY = "sunday";
        public final static String COLUMN_MONDAY = "monday";
        public final static String COLUMN_TUESDAY = "tuesday";
        public final static String COLUMN_WEDNESDAY = "wednesday";
        public final static String COLUMN_THURSDAY = "thursday";
        public final static String COLUMN_FRIDAY = "friday";
        public final static String COLUMN_SATURDAY = "saturday";

    }

}
