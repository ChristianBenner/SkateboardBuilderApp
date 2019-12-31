package com.games.crispin.skateboardbuilderapp.ConfigReaders;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.ResourceUtilities;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Grip;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Truck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Wheel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to read in an store the data in the config_wheels.xml configuration file.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class WheelConfigReader extends ComponentConfigReaderBase
{
    // Tag for logging
    private static final String TAG = "WheelConfigReader";

    // Singleton instance of the class so that only one can be made
    private static WheelConfigReader singletonInstance = null;

    // The list of wheels that have been read in from the configuration file
    private List<Wheel> wheels;

    /**
     * Creates the wheel configuration reader and loads the config_wheels.xml configuration file.
     * This will obtain and store all of the data for wheels. The constructor is private because it
     * is a singleton and therefore only one instance of the object can be created.
     *
     * @since 1.0
     */
    private WheelConfigReader()
    {
        Logger.info("Reading config_wheels.xml configuration file");

        try
        {
            // Get the wheel config resource file
            InputStream wheelConfig = Crispin.getApplicationContext().getResources().
                    openRawResource(R.raw.config_wheels);
            wheels = super.parse(wheelConfig);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the singleton instance of the wheel configuration reader. If the instance does not
     * exist yet, then create a new one
     *
     * @return  The wheel configuration reader instance
     * @since   1.0
     */
    public static WheelConfigReader getInstance()
    {
        // Check if the instance exists yet
        if(singletonInstance == null)
        {
            // If not, create one
            singletonInstance = new WheelConfigReader();
        }

        // Return the instance
        return singletonInstance;
    }

    /**
     * Get the wheels that have been read in from the configuration file
     *
     * @return  A list of the wheels read in from the configuration file
     * @since   1.0
     */
    public List<Wheel> getWheels()
    {
        return wheels;
    }

    /**
     * Get the wheel that matches the specified ID
     *
     * @param id    The ID of the wheel
     * @return      The wheel that matches the specified ID or null if that array does not contain a
     *              wheel that matches that ID.
     * @since       1.0
     */
    public Wheel getWheel(int id)
    {
        for(Wheel wheel : wheels)
        {
            if(wheel.id == id)
            {
                return wheel;
            }
        }

        return null;
    }

    /**
     * Print some information on the wheel data that has been read in from the configuration file
     *
     * @since   1.0
     */
    @Override
    public void printInfo()
    {
        // Iterate through the different wheel data
        for(int i = 0; i < wheels.size(); i++)
        {
            // Print information on wheel
            System.out.println("Wheel[" + i + "]:\n{");
            System.out.println("\tID: " + wheels.get(i).id);
            System.out.println("\tPrice: " + wheels.get(i).price);
            System.out.println("\tTextureID: " + wheels.get(i).resourceId);
            System.out.println("\tName: " + wheels.get(i).name);
            System.out.println("\tInfo: " + wheels.get(i).info);
            System.out.println("}");
        }
    }

    /**
     * Read the saves data from the XML parser
     *
     * @param parser    The XML pull parser
     * @return  The list of wheels
     * @since   1.0
     */
    @Override
    protected List readComponents(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Create a list for the entries that we will discover
        List entries = new ArrayList();

        // Require the 'wheels' tag to continue
        parser.require(XmlPullParser.START_TAG, null, "wheels");

        // Keep going through the parser until we find the end tag
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If the parser is a start tag, read the entry
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                // Get the name of the entry
                String name = parser.getName();

                // If the entry is a wheel, read it using the wheel parser, otherwise ignore it
                if(name.equals("wheel"))
                {
                    entries.add(readWheelData(parser));
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
     * Read the wheel from the XML Pull Parser
     *
     * @param parser    The XML pull parser
     * @return  The wheel
     * @since   1.0
     */
    private Wheel readWheelData(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Require the wheel tag to continue
        parser.require(XmlPullParser.START_TAG, null, "wheel");

        // Create a wheel object
        Wheel tempWheel = new Wheel();

        // Read and set the ID data
        tempWheel.id = Integer.parseInt(parser.getAttributeValue(null, "id"));

        // Read the price
        tempWheel.price = Float.parseFloat(parser.getAttributeValue(null,
                "price"));

        // Read and set the texture resource ID data
        tempWheel.resourceId = ResourceUtilities.getDrawableResource(
                parser.getAttributeValue(null, "texture"));

        // Read and set the name of the wheel
        tempWheel.name = parser.getAttributeValue(null, "name");

        // Read and set the info of the wheel
        tempWheel.info = parser.getAttributeValue(null, "info");

        // Iterate through the parser until an end tag is found
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If a start tag is found then state that the element is not supported (because it
            // we are expecting no other data)
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                String elementName = parser.getName();
                Logger.error(TAG,
                        "Unsupported element found in config_wheels.xml configuration: " +
                        elementName);
                super.skip(parser);
            }
        }

        return tempWheel;
    }
}
