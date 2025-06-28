package persistence;

import model.GachaHistory;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Represents a writer that writes JSON representation of GachaHistory to file.
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    /**
     * REQUIRES: destination is a valid file path where data can be written
     * MODIFIES: this
     * EFFECTS:  constructs a writer to write to destination file
     */
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    /**
     * REQUIRES: destination file must be writable
     * MODIFIES: this
     * EFFECTS:  opens writer; throws FileNotFoundException if destination file cannot
     *           be opened for writing
     */
    public void open() throws FileNotFoundException {
        File file = new File(destination);
    
        // 删除已有的文件，以确保数据写入不会被旧数据干扰
        if (file.exists()) {
            file.delete();
        }
        try {
            writer = new PrintWriter(file, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException("UTF-8 encoding not supported", e);  
        }
    }

    /**
     * REQUIRES: writer is open
     * MODIFIES: this
     * EFFECTS:  writes JSON representation of gachaHistory to file
     */
    public void write(GachaHistory gachaHistory) {
        // 1) 通过 gachaHistory.toJson() 得到其 JSON 对象
        JSONObject json = gachaHistory.toJson();
        // 2) 将 JSON 对象转换成带缩进的字符串并写入文件
        saveToFile(json.toString(TAB));
    }

    /**
     * MODIFIES: this
     * EFFECTS:  closes writer
     */
    public void close() {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    // EFFECTS: writes string to file (low-level) and flushes the writer
    private void saveToFile(String json) {
        writer.print(json);
        writer.flush();
    }
}
