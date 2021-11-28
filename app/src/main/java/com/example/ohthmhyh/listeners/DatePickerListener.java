package com.example.ohthmhyh.listeners;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;

import java.util.Calendar;

/**
 * A View.OnClickListener that will pull up a DatePickerDialog given a Context and
 * DatePickerDialog.OnDateSetListener.
 *
 * There are no outstanding issues that we are aware of.
 */
public class DatePickerListener implements View.OnClickListener {

    private Context context;
    private DatePickerDialog.OnDateSetListener listener;

    /**
     * Create the DatePickerListener instance
     * @param context The context for the DatePickerListener
     * @param listener The listener for when the DatePicker returns
     */
    public DatePickerListener(Context context, DatePickerDialog.OnDateSetListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * Pull up the DatePickerDialog with today's date
     * @param view The view that was clicked on
     */
    @Override
    public void onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                listener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}
