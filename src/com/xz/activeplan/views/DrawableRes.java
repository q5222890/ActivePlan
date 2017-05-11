package com.xz.activeplan.views;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Denotes that an integer parameter, field or method return value is expected
 * to be a drawable resource reference (e.g. {@link android.R.attr#alertDialogIcon}).
 *
 * {@hide}
 */
@Documented
@Retention(SOURCE)
@Target({METHOD, PARAMETER, FIELD})
public @interface DrawableRes {
}
