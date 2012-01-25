
package com.novoda.widget;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.text.GetChars;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.LineHeightSpan;
import android.text.style.ParagraphStyle;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;

public class SpannableXML {

    static final String HTML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    private Context context;

    public SpannableXML(Context context) {
        this.context = context;
    }

    public Spanned get(InputStream in) throws IOException, SAXException {
        mSpannableStringBuilder = new SpannableStringBuilder();

        RootElement root = new RootElement(HTML_NAMESPACE, "html");
        Element body = root.getChild(HTML_NAMESPACE, "body");
        Element h3 = body.getChild(HTML_NAMESPACE, "h3");

        h3.setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                handleP(mSpannableStringBuilder);
                start(mSpannableStringBuilder, new Header(2));
                mSpannableStringBuilder.append(body);
                handleP(mSpannableStringBuilder);
                endHeader(mSpannableStringBuilder);
            }
        });

        Element p = body.getChild(HTML_NAMESPACE, "p");
        p.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {

                appendP(mSpannableStringBuilder, body);
                // handleP(mSpannableStringBuilder);
                // mSpannableStringBuilder.append(body);
                // handleP(mSpannableStringBuilder);
            }
        });

        Element div = body.getChild(HTML_NAMESPACE, "div");
        div.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                Log.i("TEST", attributes.getValue("class"));
            }
        });
        Element letterauthor = div.getChild(HTML_NAMESPACE, "div");
        DivClassElementListener l = new DivClassElementListener();
        letterauthor.setStartElementListener(l);
        letterauthor.setEndTextElementListener(l);

        body.getChild(HTML_NAMESPACE, "figure").getChild(HTML_NAMESPACE, "img")
                .setStartElementListener(new StartElementListener() {

                    @Override
                    public void start(Attributes attributes) {
                        Log.i("TEST", "-> " + attributes.getValue("src"));
                        startImg(mSpannableStringBuilder, attributes);
                    }
                });
        // div.getChild(HTML_NAMESPACE, "div").setEndTextElementListener(new
        // EndTextElementListener() {
        //
        // @Override
        // public void end(String body) {
        // //handleP(mSpannableStringBuilder);
        //
        // appendItalic(mSpannableStringBuilder, body);
        // // mSpannableStringBuilder.append(body);
        // // handleP(mSpannableStringBuilder);
        // }
        // });

        Xml.parse(in, Xml.Encoding.ISO_8859_1, root.getContentHandler());
        return convert();
    }

    private SpannableStringBuilder mSpannableStringBuilder;

    public Spanned convert() {
        // Fix flags and range for paragraph-type markup.
        Object[] obj = mSpannableStringBuilder.getSpans(0, mSpannableStringBuilder.length(),
                ParagraphStyle.class);
        for (int i = 0; i < obj.length; i++) {
            int start = mSpannableStringBuilder.getSpanStart(obj[i]);
            int end = mSpannableStringBuilder.getSpanEnd(obj[i]);

            // If the last line of the range is blank, back off by one.
            if (end - 2 >= 0) {
                if (mSpannableStringBuilder.charAt(end - 1) == '\n'
                        && mSpannableStringBuilder.charAt(end - 2) == '\n') {
                    end--;
                }
            }

            if (end == start) {
                mSpannableStringBuilder.removeSpan(obj[i]);
            } else {
                mSpannableStringBuilder.setSpan(obj[i], start, end, Spannable.SPAN_PARAGRAPH);
            }
        }

        return mSpannableStringBuilder;
    }

    private static final float[] HEADER_SIZES = {
            1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f,
    };

    private static void handleP(SpannableStringBuilder text) {
        int len = text.length();

        if (len >= 1 && text.charAt(len - 1) == '\n') {
            if (len >= 2 && text.charAt(len - 2) == '\n') {
                return;
            }

            text.append("\n");
            return;
        }

        if (len != 0) {
            text.append("\n");
        }
    }

    private static class Header {
        private int mLevel;

        public Header(int level) {
            mLevel = level;
        }
    }

    private static Object getLast(Spanned text, @SuppressWarnings("rawtypes") Class kind) {
        /*
         * This knows that the last returned object from getSpans() will be the
         * most recently added.
         */
        @SuppressWarnings("unchecked")
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            return objs[objs.length - 1];
        }
    }

    private static void start(SpannableStringBuilder text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);
    }

    private void endHeader(SpannableStringBuilder text) {
        int len = text.length();
        Object obj = getLast(text, Header.class);

        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        // Back off not to change only the text, not the blank line.
        while (len > where && text.charAt(len - 1) == '\n') {
            len--;
        }

        if (where != len) {
            Header h = (Header) obj;

            text.setSpan(new RelativeSizeSpan(HEADER_SIZES[h.mLevel]), where, len,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new StyleSpan(Typeface.BOLD), where, len,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new OverlineSpan(), where, len + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void appendItalic(SpannableStringBuilder text, String body) {
        int start = text.length();
        int end = start + body.length();
        text.append(body);
        text.append('\n');
        text.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendWithNewLine(SpannableStringBuilder text, String body) {
        int start = text.length();
        int end = start + body.length();
        text.append(body);
        text.append('\n');
        text.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendP(SpannableStringBuilder text, String body) {
        int start = text.length();
        body = "      " + body;
        int end = start + body.length();
        text.append(body);
        text.append('\n');
        text.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendWithNewLine3(SpannableStringBuilder text, String body) {
        int start = text.length();
        int end = start + body.length();
        text.append(body);
        text.append('\n');
        // new LineHeightSpan(){
        //
        // @Override
        // public void chooseHeight(CharSequence text, int start, int end, int
        // spanstartv, int v,
        // FontMetricsInt fm) {
        // fm.top -= 10;
        // }
        //
        // };
        text.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    class DivClassElementListener implements EndTextElementListener, StartElementListener {

        boolean isLocation = false;

        @Override
        public void start(Attributes attributes) {
            isLocation = attributes.getValue("class").equalsIgnoreCase("location");
        }

        @Override
        public void end(String body) {
            if (isLocation) {
                appendItalic(mSpannableStringBuilder, body);
            } else {
                appendWithNewLine(mSpannableStringBuilder, body);
            }
        }
    }

    // ///////////////////////////////////////////
    private void startImg(SpannableStringBuilder text, Attributes attributes) {
        String src = attributes.getValue("", "src");
        Drawable d = null;

        // if (img != null) {
        // d = img.getDrawable(src);
        // }

        // if (d == null) {
        // d =
        // Resources.getSystem().getDrawable(com.android.internal.R.drawable.unknown_image);
        // d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        // }

        int len = text.length();
        text.append("\uFFFC");
        Bitmap bm = getBitmapFromAsset(src);
        text.setSpan(new ImageSpan(context, bm), len, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.append('\n');
    }

    /**
     * Helper Functions
     * 
     * @throws IOException
     */
    private Bitmap getBitmapFromAsset(String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
}
