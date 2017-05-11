package com.xz.activeplan.ui.recommend.activity;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CashierInputFilter implements InputFilter {
    Pattern mPattern;

    private static final int MAX_VALUE = Integer.MAX_VALUE;

    private static final int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    private static final String ZERO = "0";

    public CashierInputFilter() {
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();
        String destText = dest.toString();

        if (TextUtils.isEmpty(sourceText)) {
            return "";
        }

        Matcher matcher = mPattern.matcher(source);

        if (sourceText.matches("^[0-9]{0}([0-9]|[.])+$")) {

            if (destText.contains(POINTER)) {
                if (!matcher.matches()) {
                    return "";
                } else {
                    if (POINTER.equals(source)) {
                        return "";
                    }
                }

                int index = destText.indexOf(POINTER);
                int length = dend - index;

                if (length > POINTER_LENGTH) {
                    return dest.subSequence(dstart, dend);
                }
            } else {
                if (!matcher.matches()) {
                    return "";
                } else {
                    if ((POINTER.equals(source) || ZERO.equals(source)) && TextUtils.isEmpty(destText)) {
                        return "";
                    }
                }
            }

        } else {
            return "";
        }

        double sumText = Double.parseDouble(destText + sourceText);
        if (sumText > MAX_VALUE) {
            return dest.subSequence(dstart, dend);
        }

        return dest.subSequence(dstart, dend) + sourceText;
    }
}  