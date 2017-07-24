package com.keg.kegutils.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.keg.kegutils.R;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gamer on 11/07/2017.
 */

public class ValidationEditText extends AppCompatEditText implements View.OnFocusChangeListener {

    private boolean validateOnFocusExit = false;
    private List<Validator.IValidator<String>> validators;

    private OnFocusChangeListener onFocusChangeListener;

    public ValidationEditText(Context context) {
        super(context);
        initAttributeSet(null, 0);
    }

    public ValidationEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(attrs, 0);
    }

    public ValidationEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(attrs, defStyleAttr);
    }

    private void initAttributeSet(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ValidationEditText, defStyle, 0);
        validateOnFocusExit = a.getBoolean(R.styleable.ValidationEditText_validateOnFocusExit, false);
        a.recycle();

        validators = new LinkedList<>();
        super.setOnFocusChangeListener(this);

    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus && validateOnFocusExit) {
            validate();
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    public void validate() {
        Validator.IValidator validator;
        String editTextValue = getText().toString();
        Iterator<Validator.IValidator<String>> iterator = validators.iterator();
        while (iterator.hasNext()) {
            validator = iterator.next();
            if (!validator.validate(editTextValue)) {
                setError(getResources().getString(validator.getError()));
                break;
            }
        }
    }

    public void addValidator(Validator.IValidator<String> validator) {
        if (validator == null || validators.contains(validator)) {
            return;
        }
        validators.add(validator);
    }

    public boolean removeValidator(Validator.IValidator<String> validator) {
        return validators.remove(validator);
    }

}
