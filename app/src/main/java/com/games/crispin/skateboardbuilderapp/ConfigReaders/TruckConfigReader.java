package com.games.crispin.skateboardbuilderapp.ConfigReaders;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.ResourceUtilities;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Bearing;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Truck;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Wheel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to read in an store the data in the config_trucks.xml configuration file.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class TruckConfigReader extends ComponentConfigReaderBase
{
    // Tag for logging
    private static final String TAG = "TruckConfigReader";

    // Singleton instance of the class so that only one can be made
    private static TruckConfigReader singletonInstance = null;

    // The list of trucks that have been read in from the configuration file
    private List<Truck> trucks;

    /**
     * Creates the truck configuration reader and loads the config_trucks.xml configuration file.
     * This will obtain and store all of the data for trucks. The constructor is private because
     * it is a singleton and therefore only one instance of the object can be created.
     *
     * @since 1.0
     */
    private TruckConfigReader()
    {
        Logger.info("Reading config_trucks.xml configuration file");

        try
        {
            // Get the truck config resource file
            InputStream truckConfig = Crispin.getApplicationContext().getResources().
                    openRawResource(R.raw.config_trucks);
            trucks = super.parse(truckConfig);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the singleton instance of the truck configuration reader. If the instance does not
     * exist yet, then create a new one
     *
     * @return  The truck configuration reader instance
     * @since   1.0
     */
    public static TruckConfigReader getInstance()
    {
        // Check if the instance exists yet
        if(singletonInstance == null)
        {
            // If not, create one
            singletonInstance = new TruckConfigReader();
        }

        // Return the instance
        return singletonInstance;
    }

    /**
     * Get the trucks that have been read in from the configuration file
     *
     * @return  A list of the trucks read in from the configuration file
     * @since   1.0
     */
    public List<Truck> getTrucks()
    {
        return trucks;
    }

    /**
     * Print some information on the truck data that has been read in from the configuration file
     *
     * @since   1.0
     */
    @Override
    public void printInfo()
    {
        // Iterate through the different truck data
        for(int i = 0; i < trucks.size(); i++)
        {
            // Print information on truck
            System.out.println("Truck[" + i + "]:\n{");
            System.out.println("\tID: " + trucks.get(i).id);
            System.out.println("\tPrice: " + trucks.get(i).price);
            System.out.println("\tTextureID: " + trucks.get(i).resourceId);
            System.out.println("\tModelID: " + trucks.get(i).modelResourceId);
            System.out.println("\tName: " + trucks.get(i).name);
            System.out.println("\tInfo: " + trucks.get(i).info);
            System.out.println("}");
        }
    }

    /**
     * Read the saves data from the XML parser
     *
     * @param parser    The XML pull parser
     * @return  The list of trucks
     * @since   1.0
     */
    @Override
    protected List readComponents(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Create a list for the entries that we will discover
        List entries = new ArrayList();

        // Require the 'trucks' tag to continue
        parser.require(XmlPullParser.START_TAG, null, "trucks");

        // Keep going through the parser until we find the end tag
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If the parser is a start tag, read the entry
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                // Get the name of the entry
                String name = parser.getName();

                // If the entry is a truck, read it using the truck parser, otherwise ignore it
                if(name.equals("truck"))
                {
                    entries.add(readTruckData(parser));
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
     * Read the truck from the XML Pull Parser
     *
     * @param parser    The XML pull parser
     * @return  The truck
     * @since   1.0
     */
    private Truck readTruckData(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Require the truck tag to continue
        parser.require(XmlPullParser.START_TAG, null, "truck");

        // Create a truck object
        Truck tempTruck = new Truck();

        // Read and set the ID data
        tempTruck.id = Integer.parseInt(parser.getAttributeValue(null, "id"));

        // Read the price
        tempTruck.price = Float.parseFloat(parser.getAttributeValue(null,
                "price"));

        // Read and set the texture resource ID data
        tempTruck.resourceId = ResourceUtilities.getDrawableResource(parser.getAttributeValue(
                null, "texture"));

        // Read and set the model resource ID data
        tempTruck.modelResourceId = ResourceUtilities.getRawResource(parser.getAttributeValue(
                null, "model"));

        // Read and set the name of the truck
        tempTruck.name = parser.getAttributeValue(null, "name");

        // Read and set the info of the truck
        tempTruck.info = parser.getAttributeValue(null, "info");

        // Iterate through the parser until an end tag is found
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If a start tag is found then state that the element is not supported (because it
            // we are expecting no other data)
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                String elementName = parser.getName();
                Logger.error(TAG,
                        "Unsupported element found in config_trucks.xml configuration: " +
                                elementName);
                super.skip(parser);
            }
        }

        return tempTruck;
    }
}
