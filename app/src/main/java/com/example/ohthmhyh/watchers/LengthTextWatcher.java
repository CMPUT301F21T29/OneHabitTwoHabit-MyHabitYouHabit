package com.example.ohthmhyh.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Displays an error on an EditText if the text in the EditText is below the minimum length or above
 * the maximum length specified.
 *
 * There are no outstanding issues that we are aware of.
 */
public class LengthTextWatcher implements TextWatcher {

    private EditText editText;
    private int minLength;
    private int maxLength;

    /**
     * Create a new instance of the LengthTextWatcher to act on an EditText
     * @param editText The EditText for this TextWatcher to act on
     * @param minLength The minimum length of the string in the EditText
     * @param maxLength The maximum length of the string in the EditText
     */
    public LengthTextWatcher(EditText editText, int minLength, int maxLength) {
        this.editText = editText;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /**
     * Invoked after text in the EditText has changed and displays an error on the EditText if the
     * String in the EditText is less than the minimum length or greater than the maximum length
     * @param editable The editable field in the EditText
     */
    @Override
    public void afterTextChanged(Editable editable) {
        if (editText.getText().toString().length() > maxLength) {
            editText.setError("Too long!");
            editText.requestFocus();
        } else if (editText.getText().toString().length() < minLength) {
            editText.setError("Too short!");
            editText.requestFocus();
        } else {
            editText.setError(null);
        }
    }
}
