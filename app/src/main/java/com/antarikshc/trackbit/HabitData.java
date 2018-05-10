package com.antarikshc.trackbit;

public class HabitData {

    private String habitName;
    private Integer[] habitDays;

    HabitData(String habitName, Integer[] habitDays) {
        this.habitName = habitName;
        this.habitDays = habitDays;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public Integer[] getHabitDays() {
        return habitDays;
    }

    public void setHabitDays(Integer[] habitDays) {
        this.habitDays = habitDays;
    }
}
