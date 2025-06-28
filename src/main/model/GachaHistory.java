package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import persistence.Writable;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents the collection of multiple GachaPull records.
 * Responsible for adding, removing, and managing all pull data,
 * as well as calculating statistics (like expected value) across multiple
 * pulls.
 */
public class GachaHistory implements Writable {

    private List<GachaPull> pullRecords;

    /**
     * Requires: nothing
     * Modifies: this
     * Effects: initializes an empty GachaHistory (no pull records).
     */
    public GachaHistory() {
        pullRecords = new ArrayList<>();
    }

    /**
     * Adds a new GachaPull record to the history.
     *
     * Requires: nothing
     * Modifies: this
     * Effects: inserts the given pull record into the internal list of pull
     * records.
     */
    public void addPull(GachaPull pull) {
        pullRecords.add(pull);
        EventLog.getInstance().logEvent(new Event("Added pull record: Pull #"
                + pull.getPullIndex()
                + ", Desired 5-star: " + pull.isDesired5Star()
                + ", 4-star count: " + pull.getNumberOf4Stars()
                + ", Total draws: " + pull.getDrawCount()));
    }

    /**
     * Removes a GachaPull record from the history.
     *
     * Requires: nothing
     * Modifies: this
     * Effects: if pull is found in this history, removes it; otherwise does
     * nothing.
     */
    public void removePull(GachaPull pull) {
        pullRecords.remove(pull);
    }

    /**
     * Removes a GachaPull record from the history by index.
     *
     * Requires: nothing
     * Modifies: this
     * Effects: if pull at index is found in this history, removes it; otherwise
     * does nothing.
     */
    public void removePull(int index) {
        if (index >= 0 && index < pullRecords.size()) {
            GachaPull removed = pullRecords.remove(index);
            EventLog.getInstance().logEvent(new Event("Removed pull record: Pull #"
                    + removed.getPullIndex()));
        }
    }

    /**
     * Retrieves all pull records in this history as an unmodifiable list.
     *
     * Requires: nothing
     * Modifies: nothing
     * Effects: returns a list of all GachaPull objects contained in this
     * GachaHistory.
     */
    public List<GachaPull> getAllPulls() {
        return Collections.unmodifiableList(pullRecords);
    }

    /**
     * Calculates the probability (or expected value) of obtaining a 5-star item
     * across all recorded pulls, including how many were desired vs undesired.
     *
     * Requires: there is at least one pull in the history
     * Modifies: nothing
     * Effects: returns a double representing the fraction of pulls containing
     * 5-star items,
     * or 0.0 if no records exist.
     */
    public double calculateFiveStarRate() {
        if (pullRecords.isEmpty()) {
            return 0.0;
        }
        int totalDesired = 0;
        int totalDraws = 0;
        for (GachaPull pull : pullRecords) {
            // Each pull yields one 5-star; count it if it's desired.
            if (pull.isDesired5Star()) {
                totalDesired++;
            }
            totalDraws += pull.getDrawCount();
        }
        return (double) totalDesired / totalDraws;
    }

    /**
     * Calculates the expected number of 4-star items per pull based on recorded
     * pulls.
     *
     * Requires: there is at least one pull in the history
     * Modifies: nothing
     * Effects: returns a double representing the average number of 4-star items.
     */
    public double calculateFourStarAvg() {
        if (pullRecords.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (GachaPull pull : pullRecords) {
            total += pull.getNumberOf4Stars();
        }
        return (double) total / pullRecords.size();
    }

    /**
     * Checks the rate of desired 5-star items against a given threshold.
     *
     * Requires: threshold >= 0
     * Modifies: nothing
     * Effects: returns true if the actual desired 5-star rate is above the
     * threshold,
     * false otherwise.
     */
    public boolean isAboveThreshold(double threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException(
                    "Threshold cannot be negative");
        }
        return calculateFiveStarRate() > threshold;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray pullArray = new JSONArray();
        for (GachaPull pull : pullRecords) {
            pullArray.put(pull.toJson());
        }
        json.put("pullRecords", pullArray);
        return json;
    }

}
