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

public class SaveManager
{
    private static final String TAG = "SaveManager";

    private static final String ns = null;

    public static Skateboard loadCurrentSave()
    {
        Logger.info(TAG + ": Reading current save");

        try
        {
            List<Skateboard> skateboards = SaveManager.parse(Crispin.getApplicationContext().
                    openFileInput("currentsave.xml"));

            for(int i = 0; i < skateboards.size(); i++)
            {
                System.out.println("Skateboard: " + skateboards.get(i).getDeck());
            }
            if(skateboards.isEmpty())
            {
                System.err.println("Error: Current save empty");
                return null;
            }
            else
            {
                return skateboards.get(0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeCurrentSave(Skateboard currentSkateboard)
    {
        try
        {
            FileOutputStream fileOutputStream = Crispin.getApplicationContext().openFileOutput("currentsave.xml", Context.MODE_PRIVATE);
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
            e.printStackTrace();
        }
    }

    public static List<Skateboard> loadSaves()
    {
        try
        {
            List<Skateboard> skateboards = SaveManager.parse(Crispin.getApplicationContext().
                    openFileInput("saves.xml"));
            return skateboards;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeSave(List<Skateboard> skateboards)
    {
        try
        {
            FileOutputStream fileOutputStream = Crispin.getApplicationContext().openFileOutput("saves.xml", Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "saves");

            // Write skateboard XML
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
        }
    }

    public static List parse(InputStream in) throws XmlPullParserException, IOException
    {
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

    private static List readSaves(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List entries = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "saves");
        while(parser.next() != XmlPullParser.END_TAG)
        {
            if(parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            String name = parser.getName();

            if(name.equals("skateboard"))
            {
                System.out.println("Found a board");
                entries.add(readSkateboardSave(parser));
            }
            else
            {
                skip(parser);
            }
        }
        return entries;
    }

    private static Skateboard readSkateboardSave(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "skateboard");
        String name = null;
        int deckId = 0;
        int gripId = 0;
        int trucksId = 0;
        int bearingsId = 0;
        int wheelsId = 0;
        int designId = 0;

        deckId = Integer.parseInt(parser.getAttributeValue(null, "deck"));
        gripId = Integer.parseInt(parser.getAttributeValue(null, "grip"));
        trucksId = Integer.parseInt(parser.getAttributeValue(null, "trucks"));
        bearingsId = Integer.parseInt(parser.getAttributeValue(null, "bearings"));
        wheelsId = Integer.parseInt(parser.getAttributeValue(null, "wheels"));
        designId = Integer.parseInt(parser.getAttributeValue(null, "design"));
        name = parser.getAttributeValue(null, "name");

        while(parser.next() != XmlPullParser.END_TAG)
        {
            if(parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            String elementName = parser.getName();
            System.out.println("Unsupported Element: " + elementName);
            skip(parser);
        }
        return new Skateboard(name, deckId, gripId, trucksId, bearingsId, wheelsId, designId);
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        if(parser.getEventType() != XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }

        int levels = 1;
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
