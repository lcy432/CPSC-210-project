package persistence;

import org.json.JSONObject;

/**
 * Represents an object that can be serialized to JSON.
 * 
 * Effects: Classes implementing this interface can convert their data to a JSONObject,
 *          enabling standardized JSON serialization for persistence.
 */
public interface Writable {
    /**
     * Converts this object to a JSON representation.
     * 
     * Effects: Returns a JSONObject containing the data of this object.
     */
    JSONObject toJson();
}