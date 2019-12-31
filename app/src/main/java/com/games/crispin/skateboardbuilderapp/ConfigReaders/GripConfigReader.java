package com.games.crispin.skateboardbuilderapp.ConfigReaders;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.ResourceUtilities;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Grip;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to read in an store the data in the config_grips.xml configuration file.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class GripConfigReader extends ComponentConfigReaderBase
{
    // Tag for logging
    private static final String TAG = "GripConfigReader";

    // Singleton instance of the class so that only one can be made
    private static GripConfigReader singletonInstance = null;

    // The list of grips that have been read in from the configuration file
    private List<Grip> grips;

    /**
     * Creates the grip configuration reader and loads the config_grips.xml configuration file. This
     * will obtain and store all of the data for grips. The constructor is private because it is
     * a singleton and therefore only one instance of the object can be created.
     *
     * @since 1.0
     */
    private GripConfigReader()
    {
        Logger.info("Reading config_grips.xml configuration file");

        try
        {
            // Get the grip config resource file
            InputStream gripConfig = Crispin.getApplicationContext().getResources().
                    openRawResource(R.raw.config_grips);
            grips = super.parse(gripConfig);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the singleton instance of the grip configuration reader. If the instance does not
     * exist yet, then create a new one
     *
     * @return  The grip configuration reader instance
     * @since   1.0
     */
    public static GripConfigReader getInstance()
    {
        // Check if the instance exists yet
        if(singletonInstance == null)
        {
            // If not, create one
            singletonInstance = new GripConfigReader();
        }

        // Return the instance
        return singletonInstance;
    }

    /**
     * Get the grips that have been read in from the configuration file
     *
     * @return  A list of the grips read in from the configuration file
     * @since   1.0
     */
    public List<Grip> getGrips()
    {
        return grips;
    }

    /**
     * Get the grip that matches a certain ID
     *
     * @param gripId    ID of the grip to fetch
     * @return  The grip that matches the given ID
     * @since   1.0
     */
    public Grip getGrip(int gripId)
    {
        for(int i = 0; i < grips.size(); i++)
        {
            if(grips.get(i).id == gripId)
            {
                return grips.get(i);
            }
        }

        return null;
    }

    /**
     * Print some information on the grip data that has been read in from the configuration file
     *
     * @since   1.0
     */
    @Override
    public void printInfo()
    {
        // Iterate through the different grip data
        for(int i = 0; i < grips.size(); i++)
        {
            // Print information on grip
            System.out.println("Grip[" + i + "]:\n{");
            System.out.println("\tID: " + grips.get(i).id);
            System.out.println("\tPrice: " + grips.get(i).price);
            System.out.println("\tTextureID: " + grips.get(i).resourceId);
            System.out.println("\tName: " + grips.get(i).name);
            System.out.println("\tInfo: " + grips.get(i).info);
            System.out.println("}");
        }
    }

    /**
     * Read the saves data from the XML parser
     *
     * @param parser    The XML pull parser
     * @return  The list of grips
     * @since   1.0
     */
    @Override
    protected List readComponents(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Create a list for the entries that we will discover
        List entries = new ArrayList();

        // Require the 'grips' tag to continue
        parser.require(XmlPullParser.START_TAG, null, "grips");

        // Keep going through the parser until we find the end tag
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If the parser is a start tag, read the entry
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                // Get the name of the entry
                String name = parser.getName();

                // If the entry is a grip, read it using the grip parser, otherwise ignore it
                if(name.equals("grip"))
                {
                    entries.add(readGripData(parser));
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
     * Read the grip from the XML Pull Parser
     *
     * @param parser    The XML pull parser
     * @return  The grip
     * @since   1.0
     */
    private Grip readGripData(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Require the grip tag to continue
        parser.require(XmlPullParser.START_TAG, null, "grip");

        // Create a grip object
        Grip tempGrip = new Grip();

        // Read and set the ID data
        tempGrip.id = Integer.parseInt(parser.getAttributeValue(null, "id"));

        // Read the price
        tempGrip.price = Float.parseFloat(parser.getAttributeValue(null,
                "price"));

        // Read and set the texture resource ID data
        tempGrip.resourceId = ResourceUtilities.getDrawableResource(
                parser.getAttributeValue(null, "texture"));

        // Read and set the name of the grip
        tempGrip.name = parser.getAttributeValue(null, "name");

        // Read and set the info of the grip
        tempGrip.info = parser.getAttributeValue(null, "info");

        // Iterate through the parser until an end tag is found
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If a start tag is found then state that the element is not supported (because it
            // we are expecting no other data)
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                String elementName = parser.getName();
                Logger.error(TAG,
                        "Unsupported element found in config_grips.xml configuration: " +
                        elementName);
                super.skip(parser);
            }
        }

        return tempGrip;
    }
}
