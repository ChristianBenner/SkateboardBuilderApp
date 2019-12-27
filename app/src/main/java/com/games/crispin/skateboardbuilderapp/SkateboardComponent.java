package com.games.crispin.skateboardbuilderapp;

import java.util.HashMap;

// immutable
public final class SkateboardComponent
{
    public static HashMap<Integer, Integer> components = new HashMap<>();

    public static final int DECK_8_125_ID = 0x01;
    public static final int DECK_8_5_ID =   0x02;

    private static final SkateboardComponent DECK_8_125 =
            new SkateboardComponent(DECK_8_125_ID, R.raw.deck8_125_uv_test_2);
    private static final SkateboardComponent DECK_8_5 =
            new SkateboardComponent(DECK_8_5_ID, R.raw.board_8_5_test_2);

    public static int getComponent(int id)
    {
        if(components.containsKey(id))
        {
            return components.get(id).intValue();
        }

        return -1;
    }

    private SkateboardComponent(int id, int modelResourceId)
    {
        components.put(id, modelResourceId);
    }
}