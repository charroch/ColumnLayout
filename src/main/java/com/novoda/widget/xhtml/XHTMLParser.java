
package com.novoda.widget.xhtml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XHTMLParser {

    public void parser(InputStream inputStream) throws FileNotFoundException, SAXException,
            IOException, ParserConfigurationException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxFactory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setEncoding("ISO-8859-1");
        reader.setContentHandler(new Handler());
        reader.parse(inputSource);
        Log.i("TEST", "start=> ");

    }

    class Handler extends DefaultHandler {

        private StringBuilder bodyBuilder;

        private int depth;

        private String currentQName;

        private boolean isMixedContent;

        public Handler() {
            depth = 0;
            bodyBuilder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            Log.i("TEST", " <=start=> " + qName + " " + depth);
            isMixedContent = isMixedContentAtStart();
            currentQName = qName;
            depth++;
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            Log.i("TEST", isMixedContent + "end=> " + qName + " " + bodyBuilder);
            depth--;
            if (!isMixedContent) {
                bodyBuilder = new StringBuilder();
                isMixedContent = false;
            }
        }

        @Override
        public void characters(char[] buffer, int start, int length) throws SAXException {
            if (bodyBuilder != null) {
                bodyBuilder.append(buffer, start, length);
            }
        }

        public boolean isMixedContentAtStart() {
            return !(bodyBuilder != null && bodyBuilder.length() == 0);
        }
    }
}
