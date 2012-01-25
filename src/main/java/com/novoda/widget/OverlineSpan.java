
package com.novoda.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.LineBackgroundSpan;
import android.text.style.LineHeightSpan;

class OverlineSpan implements LineBackgroundSpan, LineHeightSpan {

    private Paint linePaint;

    public OverlineSpan() {
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline,
            int bottom, CharSequence text, int start, int end, int lnum) {
        c.drawLine(left, top+1, right, top+1, p);
    }

    @Override
    public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v,
            FontMetricsInt fm) {
        fm.top -= 10;
        fm.ascent -= 10;
        fm.descent += 20;
    }

}
