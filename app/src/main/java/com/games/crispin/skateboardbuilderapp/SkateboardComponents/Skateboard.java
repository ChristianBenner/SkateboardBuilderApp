package com.games.crispin.skateboardbuilderapp.SkateboardComponents;

import com.games.crispin.crispinmobile.Utilities.Logger;

/**
 * Skateboard class contains the IDs of the different components within a skateboard save. The IDs
 * match with individual <code>ComponentBase</code> types and can be used with their associated
 * configuration reader to fetch information such as name, model ID and price.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Skateboard
{
    // The ID when no part is present
    public static final int NO_PART = -1;

    // The name of the skateboard
    private String name;

    // The ID of the deck component
    private int deck;

    // The ID of the grip component
    private int grip;

    // The ID of the truck component
    private int trucks;

    // The ID of the bearing component
    private int bearings;

    // The ID of the wheel component
    private int wheels;

    // The ID of the design component
    private int design;

    /**
     * Create the skateboard object with all of the IDs of the components
     *
     * @param name      The name of the skateboard design
     * @param deck      The ID of the deck component
     * @param grip      The ID of the grip component
     * @param trucks    The ID of the trucks component
     * @param bearings  The ID of the bearings component
     * @param wheels    The ID of the wheels component
     * @param design    The ID of the design component
     * @since   1.0
     */
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

    /**
     * Create the skateboard object with default values for of the different component IDs
     * @since   1.0
     */
    public Skateboard()
    {
        this("Skateboard", NO_PART, NO_PART, NO_PART, NO_PART, NO_PART, NO_PART);
    }

    /**
     * Get the skateboard name
     *
     * @return The name of the skateboard
     * @since 1.0
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the skateboard name
     *
     * @param name  The new name of the skateboard
     * @since 1.0
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the deck component ID
     *
     * @return The ID of the deck component
     * @since 1.0
     */
    public int getDeck()
    {
        return deck;
    }

    /**
     * Set the deck component ID
     *
     * @param deck  The new deck component ID
     * @since 1.0
     */
    public void setDeck(int deck)
    {
        this.deck = deck;
    }

    /**
     * Get the grip component ID
     *
     * @return The ID of the grip component
     * @since 1.0
     */
    public int getGrip()
    {
        return grip;
    }

    /**
     * Set the grip component ID
     *
     * @param grip  The new grip component ID
     * @since 1.0
     */
    public void setGrip(int grip)
    {
        this.grip = grip;
    }

    /**
     * Get the truck component ID
     *
     * @return The ID of the truck component
     * @since 1.0
     */
    public int getTrucks()
    {
        return trucks;
    }

    /**
     * Set the truck component ID
     *
     * @param trucks    The new truck component ID
     * @since 1.0
     */
    public void setTrucks(int trucks)
    {
        this.trucks = trucks;
    }

    /**
     * Get the bearing component ID
     *
     * @return The ID of the bearing component
     * @since 1.0
     */
    public int getBearings()
    {
        return bearings;
    }

    /**
     * Set the bearing component ID
     *
     * @param bearings  The new bearing component ID
     * @since 1.0
     */
    public void setBearings(int bearings)
    {
        this.bearings = bearings;
    }

    /**
     * Get the wheel component ID
     *
     * @return The ID of the wheel component
     * @since 1.0
     */
    public int getWheels()
    {
        return wheels;
    }

    /**
     * Set the wheel component ID
     *
     * @param wheels    The new wheel component ID
     * @since 1.0
     */
    public void setWheels(int wheels)
    {
        this.wheels = wheels;
    }

    /**
     * Get the design component ID
     *
     * @return The ID of the design component
     * @since 1.0
     */
    public int getDesign()
    {
        return design;
    }

    /**
     * Set the design component ID
     *
     * @param design    The new design component ID
     * @since 1.0
     */
    public void setDesign(int design)
    {
        this.design = design;
    }

    /**
     * Print some information about the skateboard such as its name and component IDs
     *
     * @since 1.0
     */
    public void print()
    {
        Logger.info("Skateboard {");
        Logger.info("\tName: " + name);
        Logger.info("\tDeck: " + deck);
        Logger.info("\tGrip: " + grip);
        Logger.info("\tTrucks: " + trucks);
        Logger.info("\tBearings: " + bearings);
        Logger.info("\tWheels: " + wheels);
        Logger.info("\tDesign: " + design);
        Logger.info("}");
    }
}
