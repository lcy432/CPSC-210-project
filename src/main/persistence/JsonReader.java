package persistence;

import model.GachaHistory;
import model.GachaPull;
import org.json.JSONArray;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Represents a reader that reads GachaHistory data from JSON file.
 */
public class JsonReader {
    private String source;

    /**
     * REQUIRES: source is a valid file path pointing to a JSON file
     *           that structurally matches GachaHistory's format
     * MODIFIES: none
     * EFFECTS:  constructs a reader to read from source file
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /**
     * REQUIRES: the file at source must exist and contain valid JSON data
     *           structured for GachaHistory
     * MODIFIES: none
     * EFFECTS:
     *  - reads JSON data from the specified file (source);
     *  - if reading/parsing is successful, returns a new GachaHistory object
     *    that reproduces the previously saved state (i.e., includes all GachaPulls
     *    that were in the JSON file);
     *  - throws IOException if an error occurs reading data from the file
     *    (e.g. file not found, no access permission);
     *  - throws JSONException if JSON is badly formed (missing keys, wrong types, etc.).
     */
    public GachaHistory read() throws IOException {
        // 1) 读取文件为字符串
        String jsonData = Files.readString(Paths.get(source));

        // 2) 解析成 JSON 对象
        JSONObject jsonObject = new JSONObject(jsonData);

        // 3) 构造并返回 GachaHistory
        return parseGachaHistory(jsonObject);
    }

    // EFFECTS: parses GachaHistory from given JSON object, 
    //          if no "pullRecords" array is present, returns an empty GachaHistory.
    private GachaHistory parseGachaHistory(JSONObject jsonObject) {
        GachaHistory history = new GachaHistory();
        JSONArray pullsArray = jsonObject.optJSONArray("pullRecords");
        if (pullsArray != null) {
            for (Object obj : pullsArray) {
                JSONObject pullJson = (JSONObject) obj;
                // 若 pullJson 中字段缺失或非法，将抛出 JSONException / IllegalArgumentException
                GachaPull pull = parseGachaPull(pullJson);
                history.addPull(pull);
            }
        }
        return history;
    }

    // EFFECTS: parses a single GachaPull object from the JSON,
    //          expects { "desired5Star": bool, "numberOf4Stars": int, "pullIndex": int, "drawCount": int }
    private GachaPull parseGachaPull(JSONObject pullJson) {
        boolean desired    = pullJson.getBoolean("desired5Star");
        int numberOf4Stars = pullJson.getInt("numberOf4Stars");
        int pullIndex      = pullJson.getInt("pullIndex");
        int drawCount      = pullJson.getInt("drawCount");

        // GachaPull 构造器若发现负数或 0 会抛出 IllegalArgumentException
        return new GachaPull(desired, numberOf4Stars, pullIndex, drawCount);
    }
}