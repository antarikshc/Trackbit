package com.antarikshc.trackbit;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<HabitData> {

    private ArrayList<HabitData> dataSet;
    private Context mContext;

    private static class ViewHolder {
        TextView habitName;
        TextView textSunday;
        TextView numSunday;
        TextView textMonday;
        TextView numMonday;
        TextView textTueday;
        TextView numTueday;
        TextView textWednesday;
        TextView numWednesday;
        TextView textThursday;
        TextView numThursday;
        TextView textFriday;
        TextView numFriday;
        TextView textSaturday;
        TextView numSaturday;
    }

    CustomAdapter(@NonNull Context context, ArrayList<HabitData> dataSet) {
        super(context, R.layout.custom_list, dataSet);
        this.dataSet = dataSet;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HabitData dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_list, parent, false);

            viewHolder.habitName = convertView.findViewById(R.id.habit_name);
            viewHolder.textSunday = convertView.findViewById(R.id.text_sunday);
            viewHolder.numSunday = convertView.findViewById(R.id.sunday_number);
            viewHolder.textMonday = convertView.findViewById(R.id.text_monday);
            viewHolder.numMonday = convertView.findViewById(R.id.monday_number);
            viewHolder.textTueday = convertView.findViewById(R.id.text_tuesday);
            viewHolder.numTueday = convertView.findViewById(R.id.tuesday_number);
            viewHolder.textWednesday = convertView.findViewById(R.id.text_wednesday);
            viewHolder.numWednesday = convertView.findViewById(R.id.wednesday_number);
            viewHolder.textThursday = convertView.findViewById(R.id.text_thursday);
            viewHolder.numThursday = convertView.findViewById(R.id.thursday_number);
            viewHolder.textFriday = convertView.findViewById(R.id.text_friday);
            viewHolder.numFriday = convertView.findViewById(R.id.friday_number);
            viewHolder.textSaturday = convertView.findViewById(R.id.text_saturday);
            viewHolder.numSaturday = convertView.findViewById(R.id.saturday_number);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (dataModel != null) {

            viewHolder.habitName.setText(dataModel.getHabitName());
            Integer[] records = dataModel.getHabitDays();

            //Variables to set colors and make UI changes
            int dayColor;
            int numColor;
            int numTextColor;
            GradientDrawable dayOval;

            for (int i = 0; i <= records.length - 1; i++) {

                //get appropriate colors depending on values
                switch (records[i]) {
                    case 0:
                        dayColor = getContext().getResources().getColor(R.color.day0);
                        numColor = getContext().getResources().getColor(R.color.num0);
                        numTextColor = getContext().getResources().getColor(R.color.day0);
                        break;
                    case 1:
                    case 2:
                        dayColor = getContext().getResources().getColor(R.color.day12);
                        numColor = getContext().getResources().getColor(R.color.num12);
                        numTextColor = getContext().getResources().getColor(android.R.color.white);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        dayColor = getContext().getResources().getColor(R.color.day345);
                        numColor = getContext().getResources().getColor(R.color.num345);
                        numTextColor = getContext().getResources().getColor(android.R.color.white);
                        break;
                    default:
                        dayColor = getContext().getResources().getColor(R.color.day678);
                        numColor = getContext().getResources().getColor(R.color.num678);
                        numTextColor = getContext().getResources().getColor(android.R.color.white);
                        break;
                }

                switch (i) {
                    case 0:

                        //get Drawable to change background color
                        dayOval = (GradientDrawable) viewHolder.textSunday.getBackground();
                        dayOval.setColor(dayColor);

                        //set background color and text color for number box
                        viewHolder.numSunday.setBackgroundColor(numColor);
                        viewHolder.numSunday.setTextColor(numTextColor);

                        //adjust UI padding if number of habits is 2 digit and orientation is portrait
                        if (records[i] > 10 && getContext().getResources().getConfiguration().orientation == 1)
                            viewHolder.numSunday.setPadding(30, 26, 30, 26);

                        viewHolder.numSunday.setText(String.valueOf(records[i]));

                        break;
                    case 1:

                        dayOval = (GradientDrawable) viewHolder.textMonday.getBackground();
                        dayOval.setColor(dayColor);

                        viewHolder.numMonday.setBackgroundColor(numColor);
                        viewHolder.numMonday.setTextColor(numTextColor);

                        //adjust UI padding if number of habits is 2 digit and orientation is portrait
                        if (records[i] > 10 && getContext().getResources().getConfiguration().orientation == 1)
                            viewHolder.numMonday.setPadding(30, 26, 30, 26);

                        viewHolder.numMonday.setText(String.valueOf(records[i]));

                        break;
                    case 2:

                        dayOval = (GradientDrawable) viewHolder.textTueday.getBackground();
                        dayOval.setColor(dayColor);

                        viewHolder.numTueday.setBackgroundColor(numColor);
                        viewHolder.numTueday.setTextColor(numTextColor);

                        //adjust UI padding if number of habits is 2 digit and orientation is portrait
                        if (records[i] > 10 && getContext().getResources().getConfiguration().orientation == 1)
                            viewHolder.numTueday.setPadding(30, 26, 30, 26);

                        viewHolder.numTueday.setText(String.valueOf(records[i]));

                        break;
                    case 3:

                        dayOval = (GradientDrawable) viewHolder.textWednesday.getBackground();
                        dayOval.setColor(dayColor);

                        viewHolder.numWednesday.setBackgroundColor(numColor);
                        viewHolder.numWednesday.setTextColor(numTextColor);

                        if (records[i] > 10 && getContext().getResources().getConfiguration().orientation == 1)
                            viewHolder.numWednesday.setPadding(30, 26, 30, 26);

                        viewHolder.numWednesday.setText(String.valueOf(records[i]));

                        break;
                    case 4:

                        dayOval = (GradientDrawable) viewHolder.textThursday.getBackground();
                        dayOval.setColor(dayColor);

                        viewHolder.numThursday.setBackgroundColor(numColor);
                        viewHolder.numThursday.setTextColor(numTextColor);

                        if (records[i] > 10 && getContext().getResources().getConfiguration().orientation == 1)
                            viewHolder.numThursday.setPadding(30, 26, 30, 26);

                        viewHolder.numThursday.setText(String.valueOf(records[i]));

                        break;
                    case 5:

                        dayOval = (GradientDrawable) viewHolder.textFriday.getBackground();
                        dayOval.setColor(dayColor);

                        viewHolder.numFriday.setBackgroundColor(numColor);
                        viewHolder.numFriday.setTextColor(numTextColor);

                        //adjust UI padding if number of habits is 2 digit and orientation is portrait
                        if (records[i] > 10 && getContext().getResources().getConfiguration().orientation == 1)
                            viewHolder.numFriday.setPadding(30, 26, 30, 26);

                        viewHolder.numFriday.setText(String.valueOf(records[i]));

                        break;
                    case 6:

                        dayOval = (GradientDrawable) viewHolder.textSaturday.getBackground();
                        dayOval.setColor(dayColor);

                        viewHolder.numSaturday.setBackgroundColor(numColor);
                        viewHolder.numSaturday.setTextColor(numTextColor);

                        //adjust UI padding if number of habits is 2 digit and orientation is portrait
                        if (records[i] > 10 && getContext().getResources().getConfiguration().orientation == 1)
                            viewHolder.numSaturday.setPadding(30, 26, 30, 26);

                        viewHolder.numSaturday.setText(String.valueOf(records[i]));

                        break;
                    default:
                        break;

                }
            }
        }
        return convertView;
    }
}

