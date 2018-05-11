package com.antarikshc.trackbit;

public class HabitData {

    private Integer habitId;
    private String habitName;
    private Integer[] habitDays;

    HabitData(Integer habitId, String habitName, Integer[] habitDays) {
        this.habitId = habitId;
        this.habitName = habitName;
        this.habitDays = habitDays;
    }

    public Integer getHabitId() {
        return habitId;
    }

    public void setHabitId(Integer habitId) {
        this.habitId = habitId;
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
