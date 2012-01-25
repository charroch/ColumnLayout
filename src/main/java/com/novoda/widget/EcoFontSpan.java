package com.novoda.widget;


import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;

/**
 * Changes the typeface family of the text to which the span is attached.
 */
public class EcoFontSpan extends MetricAffectingSpan implements ParcelableSpan {
    private final Typeface mFamily;

    /**
     * @param family The font family for this typeface.  Examples include
     * "monospace", "serif", and "sans-serif".
     */
    public EcoFontSpan(Typeface tf) {
        mFamily = tf;
    }

    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString("");
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        apply(ds, mFamily);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint, mFamily);
    }

    private static void apply(Paint paint, Typeface family) {
        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        Typeface tf = family;
        int fake = oldStyle & ~tf.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }

    @Override
    public int getSpanTypeId() {
        return 123456;
    }
}