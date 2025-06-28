package model;

import org.json.JSONObject;

import persistence.Writable;

/*
 * Represents a single gacha pull record.
 * Stores information about whether a 5-star item was obtained, and if it was desired, 
 * and how many 4-star items were obtained in this single pull.
 */
public class GachaPull implements Writable {

   
    private boolean desired5Star;
    private int numberOf4Stars;
    private int pullIndex;
    private int drawCount; // Total number of draws in this pull

    /*
     * Requires: desiredFlag indicates if the 5-star item is desired (true) or not (false),
     *           num4Stars >= 0,
     *           pullIndex >= 1 (for tracking the nth pull).
     * Modifies: this
     * Effects:  initializes a new GachaPull with the provided details.
     */
    public GachaPull(boolean desiredFlag, int num4Stars, int pullIndex, int drawCount) {
        if (num4Stars < 0 || pullIndex < 1 || drawCount < 1) {
            throw new IllegalArgumentException(
            "Invalid number of 4-star items, pull index, or draw count.");
        }
        this.desired5Star = desiredFlag;
        this.numberOf4Stars = num4Stars;
        this.pullIndex = pullIndex;
        this.drawCount = drawCount;
    }

    
     /* 
     * Requires: nothing
     * Modifies: nothing
     * Effects:  returns true if the 5-star item obtained is the desired item, 
     * false otherwise.
     */
    public boolean isDesired5Star() {
        return desired5Star; 
    }

    /**
     * Returns the number of 4-star items obtained in this pull.
     * 
     * Requires: nothing
     * Modifies: nothing
     * Effects:  returns the integer count of 4-star items.
     */
    public int getNumberOf4Stars() {
        return numberOf4Stars; 
    }

    /**
     * Returns the pull index (e.g., the nth pull).
     * 
     * Requires: nothing
     * Modifies: nothing
     * Effects:  returns the integer representing which pull this was.
     */
    public int getPullIndex() {
        return pullIndex;
    }

    /**
     * Requires: nothing
     * Modifies: nothing
     * Effects:  Returns the number of draws in this pull.
     */
    public int getDrawCount() {
        return drawCount;
    }

    /**
     * Sets whether this pull included the desired 5-star item.
     * 
     * Requires: none
     * Modifies: this
     * Effects:  updates the desired-flag status for this pull.
     */
    public void setDesired5Star(boolean desiredFlag) {
        this.desired5Star = desiredFlag;
    }

    /**
     * Sets the number of 4-star items obtained in this pull.
     * 
     * Requires: num4Stars >= 0
     * Modifies: this
     * Effects:  updates the stored count of 4-star items.
     */
    public void setNumberOf4Stars(int num4Stars) {
        if (num4Stars < 0) {
            throw new IllegalArgumentException(
            "Number of 4-star items cannot be negative");  
        }
        this.numberOf4Stars = num4Stars;
    }

    /**
     * Requires: drawCount >= 1.
     * Modifies: this
     * Effects:  Sets the number of draws for this pull.
     */
    public void setDrawCount(int drawCount) {
        if (drawCount < 1) {
            throw new IllegalArgumentException(
            "Draw count must be at least 1.");
        }
        this.drawCount = drawCount;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("desired5Star", desired5Star);
        json.put("numberOf4Stars", numberOf4Stars);
        json.put("pullIndex", pullIndex);
        json.put("drawCount", drawCount);
        return json;
    }
}

