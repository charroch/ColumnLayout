
package com.novoda.widget;

import com.android.debug.hv.ViewServer;
import com.novoda.widget.columnLayout.ColumnLayout;
import com.novoda.widget.xhtml.XHTMLParser;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

public class ColumnLayoutActivity extends Activity {

    private TextView text;

    private static final String OFFICINA_SANS_BOLD = "fonts/ofsnmd__.ttf";

    private static final String OFFICINA_SANS_REG = "fonts/ofsnbk__.ttf";

    private static final String DIG = "fonts/LA_RATA_BIZARRA.ttf";

    private static Typeface sansRegular;

    private static Typeface sansBold;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        sansRegular = Typeface.createFromAsset(getAssets(), DIG);
        sansBold = Typeface.createFromAsset(getAssets(), DIG);
        setContentView(R.layout.test_column_layout_1);

        ColumnLayout layout = (ColumnLayout) findViewById(R.id.cl);
        Spanned tt = null;
        try {
            XHTMLParser parser = new XHTMLParser();
            parser.parser(getAssets().open("letters.xhtml"));
            InputStream in = getAssets().open("letters.xhtml");
            tt = new SpannableXML(this).get(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        layout.setText(tt);
        ViewServer.get(this).addWindow(this);
    }

    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }
}
