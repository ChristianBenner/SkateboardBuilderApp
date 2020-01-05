package com.games.crispin.skateboardbuilderapp.ConfigReaders;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Bearing;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to read in an store the data in the config_bearings.xml configuration file.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class BearingConfigReader extends ComponentConfigReaderBase
{
    // Tag for logging
    private static final String TAG = "BearingConfigReader";

    // Singleton instance of the class so that only one can be made
    private static BearingConfigReader singletonInstance = null;

    // The list of bearings that have been read in from the configuration file
    private List<Bearing> bearings;

    /**
     * Creates the bearing configuration reader and loads the config_bearings.xml configuration file.
     * This will obtain and store all of the data for bearings. The constructor is private because it
     * is a singleton and therefore only one instance of the object can be created.
     *
     * @since 1.0
     */
    private BearingConfigReader()
    {
        Logger.info("Reading config_bearings.xml configuration file");

        try
        {
            // Get the bearing config resource file
            InputStream bearingConfig = Crispin.getApplicationContext().getResources().
                    openRawResource(R.raw.config_bearings);
            bearings = super.parse(bearingConfig);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the singleton instance of the bearing configuration reader. If the instance does not
     * exist yet, then create a new one
     *
     * @return  The bearing configuration reader instance
     * @since   1.0
     */
    public static BearingConfigReader getInstance()
    {
        // Check if the instance exists yet
        if(singletonInstance == null)
        {
            // If not, create one
            singletonInstance = new BearingConfigReader();
        }

        // Return the instance
        return singletonInstance;
    }

    /**
     * Get the bearings that have been read in from the configuration file
     *
     * @return  A list of the bearings read in from the configuration file
     * @since   1.0
     */
    public List<Bearing> getBearings()
    {
        return bearings;
    }

    /**
     * Get the bearing that matches the specified ID
     *
     * @param id    The ID of the bearing
     * @return      The bearing that matches the specified ID or null if that array does not contain
     *              a bearing that matches that ID.
     * @since       1.0
     */
    public Bearing getBearing(int id)
    {
        for(Bearing bearing : bearings)
        {
            if(bearing.id == id)
            {
                return bearing;
            }
        }

        return null;
    }

    /**
     * Print some information on the bearing data that has been read in from the configuration file
     *
     * @since   1.0
     */
    @Override
    public void printInfo()
    {
        // Iterate through the different bearing data
        for(int i = 0; i < bearings.size(); i++)
        {
            // Print information on bearing
            System.out.println("Bearing[" + i + "]:\n{");
            System.out.println("\tID: " + bearings.get(i).id);
            System.out.println("\tPrice: " + bearings.get(i).price);
            System.out.println("\tTextureID: " + bearings.get(i).resourceId);
            System.out.println("\tName: " + bearings.get(i).name);
            System.out.println("\tInfo: " + bearings.get(i).info);
            System.out.println("}");
        }
    }

    /**
     * Read the saves data from the XML parser
     *
     * @param parser    The XML pull parser
     * @return  The list of bearings
     * @since   1.0
     */
    @Override
    protected List readComponents(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Create a list for the entries that we will discover
        List entries = new ArrayList();

        // Require the 'bearings' tag to continue
        parser.require(XmlPullParser.START_TAG, null, "bearings");

        // Keep going through the parser until we find the end tag
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If the parser is a start tag, read the entry
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                // Get the name of the entry
                String name = parser.getName();

                // If the entry is a bearing, read it using the bearing parser, otherwise ignore it
                if(name.equals("bearing"))
                {
                    entries.add(readBearingData(parser));
                }
                else
                {
                    super.skip(parser);
                }
            }
        }
        return entries;
    }

    /**
     * Read the bearing from the XML Pull Parser
     *
     * @param parser    The XML pull parser
     * @return  The bearing
     * @since   1.0
     */
    private Bearing readBearingData(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Require the bearing tag to continue
        parser.require(XmlPullParser.START_TAG, null, "bearing");

        // Create a bearing object
        Bearing tempBearing = new Bearing();

        // Read and set the ID data
        tempBearing.id = Integer.parseInt(parser.getAttributeValue(null, "id"));

        // Read the price
        tempBearing.price = Float.parseFloat(parser.getAttributeValue(null,
                "price"));

        // Read and set the texture resource ID data
        tempBearing.resourceId = super.getDrawableResource(
                parser.getAttributeValue(null, "texture"));

        // Read and set the name of the bearing
        tempBearing.name = parser.getAttributeValue(null, "name");

        // Read and set the info of the bearing
        tempBearing.info = parser.getAttributeValue(null, "info");

        // Iterate through the parser until an end tag is found
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If a start tag is found then state that the element is not supported (because it
            // we are expecting no other data)
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                String elementName = parser.getName();
                Logger.error(TAG,
                        "Unsupported element found in config_bearings.xml configuration: " +
                        elementName);
                super.skip(parser);
            }
        }

        return tempBearing;
    }
}
