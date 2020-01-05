package com.games.crispin.skateboardbuilderapp.ConfigReaders;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.skateboardbuilderapp.R;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Deck;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to read in an store the data in the config_decks.xml configuration file.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class DeckConfigReader extends ComponentConfigReaderBase
{
    // Tag for logging
    private static final String TAG = "DeckConfigReader";

    // Singleton instance of the class so that only one can be made
    private static DeckConfigReader singletonInstance = null;

    // The list of decks that have been read in from the configuration file
    private List<Deck> decks;

    /**
     * Creates the deck configuration reader and loads the config_decks.xml configuration file. This will
     * obtain and store all of the data for decks. The constructor is private because it is a
     * singleton and therefore only one instance of the object can be created.
     *
     * @since 1.0
     */
    private DeckConfigReader()
    {
        Logger.info("Reading config_decks.xml configuration file");

        try
        {
            // Get the deck config resource file
            InputStream deckConfig = Crispin.getApplicationContext().getResources().
                    openRawResource(R.raw.config_decks);
            decks = super.parse(deckConfig);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the singleton instance of the deck configuration reader. If the instance does not exist
     * yet, then create a new one
     *
     * @return  The deck configuration reader instance
     * @since   1.0
     */
    public static DeckConfigReader getInstance()
    {
        // Check if the instance exists yet
        if(singletonInstance == null)
        {
            // If not, create one
            singletonInstance = new DeckConfigReader();
        }

        // Return the instance
        return singletonInstance;
    }

    /**
     * Get the decks that have been read in from the configuration file
     *
     * @return  A list of the decks read in from the configuration file
     * @since   1.0
     */
    public List<Deck> getDecks()
    {
        return decks;
    }

    /**
     * Get a specific deck based on its ID
     *
     * @param id    The ID of the deck to find
     * @return      Deck with specified ID
     * @since       1.0
     */
    public Deck getDeck(int id)
    {
        for(int i = 0; i < decks.size(); i++)
        {
            if(decks.get(i).id == id)
            {
                return decks.get(i);
            }
        }

        Logger.error(TAG, "No deck found with an ID of " + id);
        return null;
    }

    /**
     * Print some information on the deck data that has been read in from the configuration file
     *
     * @since   1.0
     */
    @Override
    public void printInfo()
    {
        // Iterate through the different deck data
        for(int i = 0; i < decks.size(); i++)
        {
            // Print information on deck
            System.out.println("Deck[" + i + "]:\n{");
            System.out.println("\tID: " + decks.get(i).id);
            System.out.println("\tModelID: " + decks.get(i).modelId);
            System.out.println("\tGripModelID: " + decks.get(i).gripModelId);
            System.out.println("\tName: " + decks.get(i).name);
            System.out.println("\tInfo: " + decks.get(i).info);
            System.out.println("\tTruckOneRelativePosition: " +
                    decks.get(i).truckTwoRelativePosition);
            System.out.println("\tTruckTwoRelativePosition: " +
                    decks.get(i).truckTwoRelativePosition);
            System.out.println("}");
        }
    }

    /**
     * Read the saves data from the XML parser
     *
     * @param parser    The XML pull parser
     * @return  The list of decks
     * @since   1.0
     */
    @Override
    protected List readComponents(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Create a list for the entries that we will discover
        List entries = new ArrayList();

        // Require the 'decks' tag to continue
        parser.require(XmlPullParser.START_TAG, null, "decks");

        // Keep going through the parser until we find the end tag
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If the parser is a start tag, read the entry
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                // Get the name of the entry
                String name = parser.getName();

                // If the entry is a deck, read it using the deck parser, otherwise ignore it
                if(name.equals("deck"))
                {
                    entries.add(readDeckData(parser));
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
     * Read the deck from the XML Pull Parser
     *
     * @param parser    The XML pull parser
     * @return  The deck
     * @since   1.0
     */
    private Deck readDeckData(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // Require the deck tag to continue
        parser.require(XmlPullParser.START_TAG, null, "deck");

        // Create a deck object
        Deck tempDeck = new Deck();

        // Read and set the deck ID
        tempDeck.id = Integer.parseInt(parser.getAttributeValue(null, "id"));

        // Read and set the model resource ID
        tempDeck.modelId = super.getRawResource(parser.getAttributeValue(
                null, "model"));

        // Read and set the grip model resource ID
        tempDeck.gripModelId = super.getRawResource(parser.getAttributeValue(
                null, "gripmodel"));

        // Read and set the name of the deck
        tempDeck.name = parser.getAttributeValue(null, "name");

        // Read and set the info of the deck
        tempDeck.info = parser.getAttributeValue(null, "info");

        float truckOneX = Float.parseFloat(parser.getAttributeValue(null,
                "truckOneX"));
        float truckOneY = Float.parseFloat(parser.getAttributeValue(null,
                "truckOneY"));
        float truckOneZ = Float.parseFloat(parser.getAttributeValue(null,
                "truckOneZ"));
        tempDeck.truckOneRelativePosition = new Point3D(truckOneX, truckOneY, truckOneZ);

        float truckTwoX = Float.parseFloat(parser.getAttributeValue(null,
                "truckTwoX"));
        float truckTwoY = Float.parseFloat(parser.getAttributeValue(null,
                "truckTwoY"));
        float truckTwoZ = Float.parseFloat(parser.getAttributeValue(null,
                "truckTwoZ"));
        tempDeck.truckTwoRelativePosition = new Point3D(truckTwoX, truckTwoY, truckTwoZ);

        // Iterate through the parser until an end tag is found
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If a start tag is found then state that the element is not supported (because it
            // we are expecting no other data)
            if(parser.getEventType() == XmlPullParser.START_TAG)
            {
                String elementName = parser.getName();
                Logger.error(TAG,
                        "Unsupported element found in config_decks.xml configuration: " +
                        elementName);
                super.skip(parser);
            }
        }

        return tempDeck;
    }
}
