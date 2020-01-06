package com.games.crispin.skateboardbuilderapp.ConfigReaders;

import android.content.Context;
import android.util.Xml;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.skateboardbuilderapp.SkateboardComponents.Skateboard;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Save manager class contains static functions designed to read or write saves. The save manager
 * will manage the current save and the list of skateboard saves in the currentsave.xml config and
 * saves.xml configuration files respectively.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class SaveManager
{
    // Tag used for logging
    private static final String TAG = "SaveManager";

    // Namespace used in the XML parser
    private static final String NAMESPACE = null;

    /**
     * Load the current skateboard save from the currentsave.xml configuration file
     *
     * @return The current skateboard save
     * @since 1.0
     */
    public static Skateboard loadCurrentSave()
    {
        Logger.info(TAG + ": Reading current save");

        // Attempt to load the current skateboard save
        try
        {
            // Open and parse the configuration file
            List<Skateboard> skateboards = SaveManager.parse(Crispin.getApplicationContext().
                    openFileInput("currentsave.xml"));

            // If there is no skateboards, print an error
            if(skateboards.isEmpty())
            {
                Logger.error(TAG, "Current save empty");
                return new Skateboard();
            }
            else
            {
                // Return the first skateboard in the array
                return skateboards.get(0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Skateboard();
        }
    }

    /**
     * Write the current skateboard subject to the currentsave.xml configuration file
     *
     * @param currentSkateboard The current skateboard subject to save
     * @since 1.0
     */
    public static void writeCurrentSave(Skateboard currentSkateboard)
    {
        // Attempt to write configuration file for the current save
        try
        {
            Logger.info("Writing save file currentsave.xml");
            FileOutputStream fileOutputStream = Crispin.getApplicationContext().openFileOutput(
                    "currentsave.xml", Context.MODE_PRIVATE);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "saves");

            // Write skateboard XML
            serializer.startTag("", "skateboard");
            serializer.attribute("", "deck",
                    Integer.toString(currentSkateboard.getDeck()));
            serializer.attribute("", "grip",
                    Integer.toString(currentSkateboard.getGrip()));
            serializer.attribute("", "trucks",
                    Integer.toString(currentSkateboard.getTrucks()));
            serializer.attribute("", "bearings",
                    Integer.toString(currentSkateboard.getBearings()));
            serializer.attribute("", "wheels",
                    Integer.toString(currentSkateboard.getWheels()));
            serializer.attribute("", "design",
                    Integer.toString(currentSkateboard.getDesign()));
            serializer.attribute("", "name",
                    currentSkateboard.getName());
            serializer.endTag("", "skateboard");

            serializer.endTag("", "saves");
            serializer.endDocument();
            serializer.flush();
            fileOutputStream.close();
        }
        catch (Exception e)
        {
            Logger.error(TAG, "Failed to write save file currentsave.xml");
            e.printStackTrace();
        }
    }

    /**
     * Load the current skateboard saves from the saves.xml configuration file
     *
     * @return The list of skateboard saves
     * @since 1.0
     */
    public static List<Skateboard> loadSaves()
    {
        // Attempt to load the saves configuration file
        try
        {
            List<Skateboard> skateboards = SaveManager.parse(Crispin.getApplicationContext().
                    openFileInput("saves.xml"));
            return skateboards;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Logger.error(TAG, "Failed to load the current saves from the saves.xml config");
            return new ArrayList<>();
        }
    }

    /**
     * Write the skateboard saves to the saves.xml configuration file
     *
     * @param skateboards   The list of skateboards to write to the config file
     * @since 1.0
     */
    public static void writeSave(List<Skateboard> skateboards)
    {
        // Attempt to write to the saves config
        try
        {
            FileOutputStream fileOutputStream = Crispin.getApplicationContext().openFileOutput(
                    "saves.xml", Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "saves");

            // Write each skateboard to the XML file
            for(Skateboard s : skateboards)
            {
                serializer.startTag("", "skateboard");
                serializer.attribute("", "deck", Integer.toString(s.getDeck()));
                serializer.attribute("", "grip", Integer.toString(s.getGrip()));
                serializer.attribute("", "trucks", Integer.toString(s.getTrucks()));
                serializer.attribute("", "bearings", Integer.toString(s.getBearings()));
                serializer.attribute("", "wheels", Integer.toString(s.getWheels()));
                serializer.attribute("", "design", Integer.toString(s.getDesign()));
                serializer.attribute("", "name", s.getName());
                serializer.endTag("", "skateboard");
            }

            serializer.endTag("", "saves");
            serializer.endDocument();
            serializer.flush();
            fileOutputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Logger.error(TAG, "Failed to write saves to the saves.xml config");
        }
    }

    /**
     * Parse a configuration
     *
     * @param in    The input stream with the configuration to pass
     * @since 1.0
     */
    public static List parse(InputStream in) throws XmlPullParserException, IOException
    {
        // Try to parse the input stream config
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readSaves(parser);
        }
        finally
        {
            in.close();
        }
    }

    /**
     * Read a save XML configuration format
     *
     * @param parser    The XML pull parser to read the save configuration format
     * @since 1.0
     */
    private static List readSaves(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // The list of save entries
        List entries = new ArrayList();

        // Require the saves tag
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "saves");

        // While an end tag has not been found, parse
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If a new start tag has not been found, start the next loop cycle
            if(parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            String name = parser.getName();

            // If a new skateboard entry has been found, read the skateboard save
            if(name.equals("skateboard"))
            {
                entries.add(readSkateboardSave(parser));
            }
            else
            {
                skip(parser);
            }
        }
        return entries;
    }

    /**
     * Read a skateboard XML format
     *
     * @param parser    The XML pull parser to read the skateboard configuration format
     * @since 1.0
     */
    private static Skateboard readSkateboardSave(XmlPullParser parser) throws
            XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "skateboard");

        // Load all of the IDs for each component
        int deckId = Integer.parseInt(parser.getAttributeValue(null, "deck"));
        int gripId = Integer.parseInt(parser.getAttributeValue(null, "grip"));
        int trucksId = Integer.parseInt(parser.getAttributeValue(null, "trucks"));
        int bearingsId = Integer.parseInt(parser.getAttributeValue(null,
                "bearings"));
        int wheelsId = Integer.parseInt(parser.getAttributeValue(null, "wheels"));
        int designId = Integer.parseInt(parser.getAttributeValue(null, "design"));
        String name = parser.getAttributeValue(null, "name");

        // While no end tag has been found, parse
        while(parser.next() != XmlPullParser.END_TAG)
        {
            // If a start tag hasn't been found, start next loop cycle
            if(parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            String elementName = parser.getName();
            Logger.info("Unsupported XML element found in saves.xml: " + elementName);
            skip(parser);
        }
        return new Skateboard(name, deckId, gripId, trucksId, bearingsId, wheelsId, designId);
    }

    /**
     * Skip an element from the XML configuration
     *
     * @param parser    The XML pull parser to skip the next element from
     * @since 1.0
     */
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        // If a start tag has not been found then throw an exception because we are not at the
        // start of a new element
        if(parser.getEventType() != XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }

        // Keep track of how many levels we have traversed
        int levels = 1;

        // Whilst we have not finished traversing all levels, go through the element
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
