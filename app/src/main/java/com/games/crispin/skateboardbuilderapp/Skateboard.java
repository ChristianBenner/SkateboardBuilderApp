package com.games.crispin.skateboardbuilderapp;

public class Skateboard
{
    private String name;
    private int deck;
    private int grip;
    private int trucks;
    private int bearings;
    private int wheels;

    public Skateboard(String name, int deck, int grip, int trucks, int bearings, int wheels)
    {
        this.name = name;
        this.deck = deck;
        this.grip = grip;
        this.trucks = trucks;
        this.bearings = bearings;
        this.wheels = wheels;
    }

    public Skateboard()
    {
        name = "Skateboard";
        deck = 0;
        grip = 0;
        trucks = 0;
        bearings = 0;
        wheels = 0;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
