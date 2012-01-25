
package com.novoda.widget.pager;

import com.novoda.widget.ColumnLayoutIterator;
import com.novoda.widget.ColumnTextLayout;
import com.novoda.widget.R;
import com.novoda.widget.SpannableXML;
import com.viewpagerindicator.CirclePageIndicator;

import org.xml.sax.SAXException;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.BufferType;

import java.io.IOException;
import java.io.InputStream;

public class ArticlePager2 extends FragmentActivity {
    private ViewPager awesomePager;

    private static int NUM_AWESOME_VIEWS = 20;

    private Context cxt;

    private AwesomePagerAdapter awesomeAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_pager);
        cxt = this;

        Spanned text = null;
        try {
            InputStream in = getAssets().open("letters.xhtml");
            text = new SpannableXML(this).get(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        awesomeAdapter = new AwesomePagerAdapter(text);
        awesomePager = (ViewPager) findViewById(R.id.pager);


        awesomePager.setAdapter(awesomeAdapter);

        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(awesomePager);
    }
    
    @Override
    protected void onResume() {
        Log.i("TEST", "============>" + awesomePager.getWidth() + " " + awesomePager.getHeight());
        super.onResume();
    }

    private class AwesomePagerAdapter extends PagerAdapter {

        private final Spanned text;

        private TextView paint;

        public AwesomePagerAdapter(Spanned text) {
            this.text = text;
        }

        @Override
        public int getCount() {
            return 3;
        }

        /**
         * Create the page for the given position. The adapter is responsible
         * for adding the view to the container given here, although it only
         * must ensure this is done by the time it returns from
         * {@link #finishUpdate()}.
         * 
         * @param container The containing View in which the page will be shown.
         * @param position The page position to be instantiated.
         * @return Returns an Object representing the new page. This does not
         *         need to be a View, but can be some other container of the
         *         page.
         */
        @Override
        public Object instantiateItem(View collection, int position) {
            Log.i("TEST", "============> " + position);
            LinearLayout l = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.four_column, null);

            TextView tv = ((TextView) l.getChildAt(0));
            tv.setText(text, BufferType.SPANNABLE);

            StaticLayout lay = new StaticLayout(text, tv.getPaint(), 260, Alignment.ALIGN_NORMAL,
                    1, 1, true);

            ColumnLayoutIterator columnLayoutIterator = new ColumnLayoutIterator(lay, 0, 0, 1200,
                    647);
            CharSequence cs = text;

            ColumnTextLayout next = null;

            for (int i = 0; i < 4 * position; i++) {
                next = columnLayoutIterator.next();
            }

            for (int i = 0; i < 4; i++) {
                next = columnLayoutIterator.next();
                Log.i("TEST", next + " < -");
                TextView v = null;
                switch (i) {
                    case 0:
                        tv.setText(cs.subSequence(next.charStart, next.charEnd));
                        break;
                    case 1:
                        v = (TextView) l.findViewById(R.id.second);
                        break;
                    case 2:
                        v = (TextView) l.findViewById(R.id.third);
                        break;
                    case 3:
                        v = (TextView) l.findViewById(R.id.fourth);
                        break;
                }

                LayoutParams layoutParams = new LinearLayout.LayoutParams(260,
                        LinearLayout.LayoutParams.FILL_PARENT);
                layoutParams.setMargins(0, 0, 40, 0);
                if (v != null) {
                    v.setLayoutParams(layoutParams);
                    v.setText(cs.subSequence(next.charStart, next.charEnd));
                }
            }
            ((ViewPager) collection).addView(l, 0);
            return l;
        }

        /**
         * Remove a page for the given position. The adapter is responsible for
         * removing the view from its container, although it only must ensure
         * this is done by the time it returns from {@link #finishUpdate()}.
         * 
         * @param container The containing View from which the page will be
         *            removed.
         * @param position The page position to be removed.
         * @param object The same object that was returned by
         *            {@link #instantiateItem(View, int)}.
         */
        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((LinearLayout) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        /**
         * Called when the a change in the shown pages has been completed. At
         * this point you must ensure that all of the pages have actually been
         * added or removed from the container as appropriate.
         * 
         * @param container The containing View which is displaying this
         *            adapter's page views.
         */
        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

    }

}
