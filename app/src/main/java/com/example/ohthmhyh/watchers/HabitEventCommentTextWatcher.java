package com.example.ohthmhyh.watchers;

import android.text.Editable;
import android.widget.EditText;

import com.example.ohthmhyh.entities.HabitEvent;

public class HabitEventCommentTextWatcher extends LengthTextWatcher {

    private HabitEvent habitEvent;

    /**
     * Create a new instance of the HabitEventCommentTextWatcher to act on an EditText and a
     * HabitEvent.
     *
     * @param editText  The EditText for this TextWatcher to act on
     * @param minLength The minimum length of the string in the EditText
     * @param maxLength The maximum length of the string in the EditText
     * @param habitEvent The HabitEvent to change the comment of upon the text changing
     */
    public HabitEventCommentTextWatcher(
            EditText editText, int minLength, int maxLength, HabitEvent habitEvent) {
        super(editText, minLength, maxLength);
        this.habitEvent = habitEvent;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        super.afterTextChanged(editable);
        habitEvent.setComment(editText.getText().toString());
    }
}
