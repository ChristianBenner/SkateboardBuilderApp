package com.games.crispin.skateboardbuilderapp.ConfigReaders;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Base class for component configuration readers. They are designed to read a config file that
 * contains all of the data relevant to a particular type of component e.g. wheels or trucks
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public abstract class ComponentConfigReaderBase
{
    /**
     * Print some information on the components data that has been read in from the configuration
     * file
     *
     * @since   1.0
     */
    public abstract void printInfo();

    /**
     * Read the components from the XML pull parser
     *
     * @param parser    The XML pull parser
     * @return  The list containing the components
     * @since   1.0
     */
    protected abstract List readComponents(XmlPullParser parser)  throws XmlPullParserException,
            IOException;

    /**
     * Parse the configuration file
     *
     * @param inputStream   The config file input stream
     * @return  The list of designs
     * @since   1.0
     */
    protected List parse(InputStream inputStream) throws XmlPullParserException, IOException
    {
        // Attempt to parse the XML file
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readComponents(parser);
        }
        finally
        {
            // Close the input stream because we are finished with it
            inputStream.close();
        }
    }
    /**
     * Skip the current XML entry
     *
     * @param parser    The XML pull parser
     * @since 1.0
     */
    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // If it isn't a start tag we are skipping then throw an error
        if(parser.getEventType() != XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }

        int levels = 1;

        // Count how many levels (entries) we are inside so we can make sure we skip the entry and
        // all of its sub entries without skipping anything else.
        while(levels != 0)
        {
            switch (parser.next())
            {
                case XmlPullParser.END_TAG:
                    levels--;
                    break;
                case XmlPullParser.START_TAG:
                    levels++;
                    break;
            }
        }
    }
}
