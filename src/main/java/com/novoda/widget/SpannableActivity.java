
package com.novoda.widget;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.LineBackgroundSpan;
import android.text.style.LineHeightSpan;
import android.text.style.MetricAffectingSpan;
import android.widget.TextView;

public class SpannableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText("hello world I am so cool");
        view.setTextSize(32);

        SpannableStringBuilder sp = new SpannableStringBuilder(view.getText());
        sp.setSpan(new MySpan(), 0, "hello world I am so cool".length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(sp);
        setContentView(view);
    }

    class MySpan implements LineBackgroundSpan, LineHeightSpan {

        @Override
        public void drawBackground(Canvas c, Paint p1, int left, int right, int top, int baseline,
                int bottom, CharSequence text, int start, int end, int lnum) {

            Paint p = new Paint();
            p.setStrokeWidth(20.0f);
            p.setColor(Color.RED);
            c.drawLine(left, top, right, top, p);
        }

        @Override
        public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v,
                FontMetricsInt fm) {
            fm.top -= 10;
        }

    }
}
