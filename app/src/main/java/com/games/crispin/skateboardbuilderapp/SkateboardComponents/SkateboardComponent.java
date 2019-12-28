package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

import com.games.crispin.skateboardbuilderapp.R;

import java.util.HashMap;

// immutable
public final class SkateboardComponent
{
    public static HashMap<Integer, SkateboardComponent> components = new HashMap<>();

    public static final int DECK_8_125_ID = 0x01;
    public static final int DECK_8_5_ID =   0x02;

    private static final SkateboardComponent DECK_8_125 =
            new SkateboardComponent(
                    DECK_8_125_ID,
                    R.raw.deck_8_125,
                    "8.1\"",
                    "A deck that has a width of 8.1\".");
    private static final SkateboardComponent DECK_8_5 =
            new SkateboardComponent(
                    DECK_8_5_ID,
                    R.raw.deck_8_5,
                    "8.2\"",
                    "A deck that has a width of 8.2\".");


    public static final int DESIGN_JART_NEW_WAVE_ID =               0x1001;
    public static final int DESIGN_PALACE_DECK_ID =                 0x1002;
    public static final int DESIGN_PRIMITIVE_RODRIQUEZ_THORNS_ID =  0x1003;
    public static final int DESIGN_PRIMITIVE_X_RICK_AND_MORTY_ID =  0x1004;
    public static final int DESIGN_REAL_WAIR_FLOODED_ID =           0x1005;

    private static final SkateboardComponent DESIGN_JART_NEW_WAVE =
            new SkateboardComponent(
                    DESIGN_JART_NEW_WAVE_ID,
                    R.drawable.design_jart_new_wave,
                    "Jart New Wave",
                    "This design is called New Wave. It is by Jart.");

    private static final SkateboardComponent DESIGN_PALACE_DECK =
            new SkateboardComponent(
                    DESIGN_PALACE_DECK_ID,
                    R.drawable.design_palace,
                    "Palace",
                    "This design is called X. It is by Palace.");

    private static final SkateboardComponent DESIGN_PRIMITIVE_RODRIQUEZ_THORNS =
            new SkateboardComponent(
                    DESIGN_PRIMITIVE_RODRIQUEZ_THORNS_ID,
                    R.drawable.design_primitive_rodriquez_thorns,
                    "Primitive Rodriquez Thorns",
                    "This design is called Rodriquez Thorns. It is by Primitive.");

    private static final SkateboardComponent DESIGN_PRIMITIVE_X_RICK_AND_MORTY =
            new SkateboardComponent(
                    DESIGN_PRIMITIVE_X_RICK_AND_MORTY_ID,
                    R.drawable.design_primitive_x_rick_and_morty,
                    "Primitive X Rick and Morty",
                    "This design is based on the Rick and Morty TV series. It is by Primitive.");

    private static final SkateboardComponent DESIGN_REAL_WAIR_FLOODED =
            new SkateboardComponent(
                    DESIGN_REAL_WAIR_FLOODED_ID,
                    R.drawable.design_real_wair_flooded,
                    "Real Wair Flooded",
                    "This design is called Flooded. It is by Real Wair.");


    public static SkateboardComponent getComponent(int id)
    {
        if(components.containsKey(id))
        {
            return components.get(id);
        }

        return null;
    }

    private final int modelResourceId;
    private final String name;
    private final String info;

    private SkateboardComponent(int id, int modelResourceId, String name, String info)
    {
        this.modelResourceId = modelResourceId;
        this.name = name;
        this.info = info;
        components.put(id, this);
    }

    public int getModelResourceId()
    {
        return modelResourceId;
    }

    public String getName()
    {
        return name;
    }

    public String getInfo()
    {
        return info;
    }
}