
package com.novoda.widget.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

import android.text.Spanned;

import java.io.IOException;
import java.io.InputStream;

public class XHTMLMarshaller {

    public Spanned get(InputStream in) throws IOException, SAXException {
        Document doc = Jsoup.parse(in, "ISO-8859-1", "android_asset://");
        
        return null;
    }
}
