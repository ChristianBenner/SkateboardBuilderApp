package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

public class Skateboard
{
    public static final int NO_PART = -1;

    private String name;
    private int deck;
    private int grip;
    private int trucks;
    private int bearings;
    private int wheels;
    private int design;

    public Skateboard(String name,
                      int deck,
                      int grip,
                      int trucks,
                      int bearings,
                      int wheels,
                      int design)
    {
        this.name = name;
        this.deck = deck;
        this.grip = grip;
        this.trucks = trucks;
        this.bearings = bearings;
        this.wheels = wheels;
        this.design = design;
    }

    public Skateboard()
    {
        this("Skateboard", NO_PART, NO_PART, NO_PART, NO_PART, NO_PART, NO_PART);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getDeck()
    {
        return deck;
    }

    public void setDeck(int deck)
    {
        this.deck = deck;
    }

    public int getGrip()
    {
        return grip;
    }

    public void setGrip(int grip)
    {
        this.grip = grip;
    }

    public int getTrucks()
    {
        return trucks;
    }

    public void setTrucks(int trucks)
    {
        this.trucks = trucks;
    }

    public int getBearings()
    {
        return bearings;
    }

    public void setBearings(int bearings)
    {
        this.bearings = bearings;
    }

    public int getWheels()
    {
        return wheels;
    }

    public void setWheels(int wheels)
    {
        this.wheels = wheels;
    }

    public void setDesign(int design)
    {
        this.design = design;
    }

    public int getDesign()
    {
        return design;
    }
}
