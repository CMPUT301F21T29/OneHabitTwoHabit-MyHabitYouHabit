package com.example.ohthmhyh.listeners;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;

import com.example.ohthmhyh.HabitsFragment;

import java.util.Calendar;

public class DatePickerListener implements View.OnClickListener {

    private Context context;
    private DatePickerDialog.OnDateSetListener listener;

    public DatePickerListener(Context context, DatePickerDialog.OnDateSetListener listener) {
        this.context = context;
        this.listener = listener;
    }

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
