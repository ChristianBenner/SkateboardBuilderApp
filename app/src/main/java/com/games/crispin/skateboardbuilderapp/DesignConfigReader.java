package com.games.crispin.skateboardbuilderapp;

import android.util.Xml;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Utilities.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to read in an store the data in the design.xml configuration file.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class DesignConfigReader
{
    // Tag for logging
    private static final String TAG = "DesignConfigReader";

    // Singleton instance of the class so that only one can be made
    private static DesignConfigReader singletonInstance = null;

    // The list of designs that have been read in from the configuration file
    private List<Design> designs;

    /**
     * Creates the design configuration reader and loads the designs.xml configuration file. This
     * will obtain and store all of the data for designs. The constructor is private because it is
     * a singleton and therefore only one instance of the object can be created.
     *
     * @since 1.0
     */
    private DesignConfigReader()
    {
        Logger.info("Reading designs.xml configuration file");

        try
        {
            // Get the design config resource file
            InputStream designConfig = Crispin.getApplicationContext().getResources().
                    openRawResource(R.raw.designs);
            designs = parse(designConfig);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the singleton instance of the design configuration reader. If the instance does not
     * exist yet, then create a new one
     *
     * @return  The design configuration reader instance
     * @since   1.0
     */
    public static DesignConfigReader getInstance()
    {
        // Check if the instance exists yet
        if(singletonInstance == null)
        {
            // If not, create one
            singletonInstance = new DesignConfigReader();
        }

        // Return the instance
        return singletonInstance;
    }

    /**
     * Get the designs that have been read in from the configuration file
     *
     * @return  A list of the designs read in from the configuration file
     * @since   1.0
     */
    public List<Design> getDesigns()
    {
        return designs;
    }

    /**
     * Print some information on the design data that has been read in from the configuration file
     *
     * @since   1.0
     */
    public void printInfo()
    {
        // Iterate through the different design data
        for(int i = 0; i < designs.size(); i++)
        {
            // Print information on design
            System.out.println("Design[" + i + "]:\n{");
            System.out.println("\tID: " + designs.get(i).id);
            System.out.println("\tWidth: " + designs.get(i).deckId);
            System.out.println("\tTextureID: " + designs.get(i).resourceId);
            System.out.println("\tName: " + designs.get(i).name);
            System.out.println("\tInfo: " + designs.get(i).info);
            System.out.println("}");
        }
    }

    /**
     * Parse the configuration file
     *
     * @param inputStream   The config file input stream
     * @return  The list of designs
     * @since   1.0
     */
    private List parse(InputStream inputStream) throws XmlPullParserException, IOException
    {
        // Attempt to parse the XML file
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readSaves(parser);
        }
        finally
        {
            // Close the input stream because we are finished with it
            inputStream.close();
        }
    }

    /**
     * Read the saves data from the XML parser
     *
     * @param parser    The XML pull parser
     * @return  The list of designs
     * @since   1.0
     */
    private List readSaves(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Create a list for the entries that we will discover
        List entries = new ArrayList();

        // Require the 'designs' tag to continue
        parser.require(XmlPullParser.START_TAG, null, "designs");

        // Keep going through the parser until we find the end tag
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If the parser is a start tag, read the entry
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                // Get the name of the entry
                String name = parser.getName();

                // If the entry is a design, read it using the design parser, otherwise ignore it
                if(name.equals("design"))
                {
                    entries.add(readDesignSave(parser));
                }
                else
                {
                    skip(parser);
                }
            }
        }
        return entries;
    }

    /**
     * Read the design from the XML Pull Parser
     *
     * @param parser    The XML pull parser
     * @return  The design
     * @since   1.0
     */
    private Design readDesignSave(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Require the design tag to continue
        parser.require(XmlPullParser.START_TAG, null, "design");

        // Create a design object
        Design tempDesign = new Design();

        // Read and set the ID data
        tempDesign.id = Integer.parseInt(parser.getAttributeValue(null, "id"));

        // Read and set the deck ID data
        tempDesign.deckId = Integer.parseInt(parser.getAttributeValue(null,
                "deckId"));

        // Read and set the texture resource ID data
        tempDesign.resourceId = ResourceUtilities.getResourceIdFromName(
                parser.getAttributeValue(null, "texture"));

        // Read and set the name of the design
        tempDesign.name = parser.getAttributeValue(null, "name");

        // Read and set the info of the design
        tempDesign.info = parser.getAttributeValue(null, "info");

        // Iterate through the parser until an end tag is found
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If a start tag is found then state that the element is not supported (because it
            // we are expecting no other data)
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                String elementName = parser.getName();
                Logger.error(TAG, "Unsupported element found in design.xml configuration: " +
                        elementName);
                skip(parser);
            }
        }

        return tempDesign;
    }

    /**
     * Skip the current XML entry
     *
     * @param parser    The XML pull parser
     * @since 1.0
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
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
