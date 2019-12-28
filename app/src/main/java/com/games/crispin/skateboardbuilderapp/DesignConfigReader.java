package com.games.crispin.skateboardbuilderapp;

import android.util.Xml;

import com.games.crispin.crispinmobile.Crispin;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DesignConfigReader
{
    class Design
    {
        public int id;
        public int width;
        public int resourceId;
        public String name;
        public String info;
    }

    private static final String ns = null;

    public static int getResId(String resourceName)
    {
        try
        {
            Field idField = R.drawable.class.getDeclaredField(resourceName);
            return (idField).getInt(idField);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    private List<Design> designs;

    public DesignConfigReader()
    {
        System.out.println("Reading designs configuration");
        loadDesigns();
    }

    public List<Design> getDesigns()
    {
        return designs;
    }

    // Class is to read in all of the design parts specified in the config resource
    private void loadDesigns()
    {
        try
        {
            // Get the design config resource file
            InputStream designConfig = Crispin.getApplicationContext().getResources().
                    openRawResource(R.raw.designs);
            designs = parse(designConfig);

            for(int i = 0; i < designs.size(); i++)
            {
                System.out.println("Design[" + i + "]:\n{");
                System.out.println("\tID: " + designs.get(i).id);
                System.out.println("\tWidth: " + designs.get(i).width);
                System.out.println("\tTextureID: " + designs.get(i).resourceId);
                System.out.println("\tName: " + designs.get(i).name);
                System.out.println("\tInfo: " + designs.get(i).info);
                System.out.println("}");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List parse(InputStream in) throws XmlPullParserException, IOException
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

    private List readSaves(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        List entries = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "designs");
        while(parser.next() != XmlPullParser.END_TAG)
        {
            if(parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            String name = parser.getName();

            if(name.equals("design"))
            {
                entries.add(readDesignSave(parser));
            }
            else
            {
                skip(parser);
            }
        }
        return entries;
    }

    private Design readDesignSave(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "design");

        Design tempDesign = new Design();
        tempDesign.id = Integer.parseInt(parser.getAttributeValue(null, "id"));
        tempDesign.width = Integer.parseInt(parser.getAttributeValue(null, "width"));
        tempDesign.resourceId = getResId(parser.getAttributeValue(null, "texture"));
        tempDesign.name = parser.getAttributeValue(null, "name");
        tempDesign.info = parser.getAttributeValue(null, "info");

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
        return tempDesign;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
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
