package com.example.ohthmhyh.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class LengthTextWatcher implements TextWatcher {

    private EditText editText;
    private int minLength;
    private int maxLength;

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
