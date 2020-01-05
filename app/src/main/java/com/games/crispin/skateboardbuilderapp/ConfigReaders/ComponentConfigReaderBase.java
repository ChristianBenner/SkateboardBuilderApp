package com.games.crispin.skateboardbuilderapp.ConfigReaders;

import android.util.Xml;

import com.games.crispin.skateboardbuilderapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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
    // Return value for when a resource could not be found
    protected static final int RESOURCE_FILE_NOT_FOUND = -1;

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

    /**
     * Get the resource ID of the given filename
     *
     * @param resourceName  The filename of the resource
     * @return              The resource ID of a file with a filename matching the given string
     * @since               1.0
     */
    protected static int getDrawableResource(String resourceName)
    {
        try
        {
            // Get the ID of the drawable resource
            Field idField = R.drawable.class.getDeclaredField(resourceName);
            return (idField).getInt(idField);
        }
        catch (Exception e)
        {
            // File with that filename could not be found in the drawable resource directory
            e.printStackTrace();
            return RESOURCE_FILE_NOT_FOUND;
        }
    }

    /**
     * Get the resource ID of the given filename
     *
     * @param resourceName  The filename of the resource
     * @return              The resource ID of a file with a filename matching the given string
     * @since               1.0
     */
    protected static int getRawResource(String resourceName)
    {
        try
        {
            // Get the ID of the raw resource
            Field idField = R.raw.class.getDeclaredField(resourceName);
            return (idField).getInt(idField);
        }
        catch (Exception e)
        {
            // File with that filename could not be found in the drawable resource directory
            e.printStackTrace();
            return RESOURCE_FILE_NOT_FOUND;
        }
    }
}
