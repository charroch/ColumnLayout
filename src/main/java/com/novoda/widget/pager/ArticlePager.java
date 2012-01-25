
package com.novoda.widget.pager;

import com.novoda.widget.ColumnLayoutIterator;
import com.novoda.widget.ColumnTextLayout;
import com.novoda.widget.R;
import com.novoda.widget.SpannableXML;
import com.novoda.widget.columnLayout.ColumnLayout;
import com.novoda.widget.xhtml.XHTMLParser;
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

import javax.xml.parsers.ParserConfigurationException;

public class ArticlePager extends FragmentActivity {
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
            XHTMLParser parser = new XHTMLParser();
            parser.parser(getAssets().open("politics.xhtml"));

            InputStream in = getAssets().open("business.xhtml");
            text = new SpannableXML(this).get(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
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
            return 2;
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
            ColumnLayout l = (ColumnLayout) LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.test_column_layout_1, null);
            l.setText(text);
            l.setColumnOffset(position * l.getColumnCount());
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
            ((ViewPager) collection).removeView((ColumnLayout) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ColumnLayout) object);
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
