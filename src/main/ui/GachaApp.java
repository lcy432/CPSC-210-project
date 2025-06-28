package ui;

import model.GachaHistory;
import model.GachaPull;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the console-based user interface for the Gacha Statistics Tracker.
 * Handles user input and output, allowing users to add, view, remove, and analyze
 * gacha pulls.
 *
 * NOTE: This class will contain System.in/System.out calls once implemented, 
 * but no direct input/output should occur in the model package.
 */
public class GachaApp {
    private GachaHistory history;
    private Scanner scanner;
    private static final String DATA_FILE = "./data/gachaHistory.json";
    /**
     * Requires: nothing
     * Modifies: this
     * Effects:  constructs a new GachaApp and initializes any necessary UI components 
     *           (e.g., prompting user, displaying main menu, etc.).
     */

    public GachaApp() {
        history = new GachaHistory();
        scanner = new Scanner(System.in);
        
    }

    /**
     * Runs the console-based UI, providing menu options for user actions:
     * - Add a new gacha pull
     * - View all pull records
     * - Remove or modify a pull record
     * - Calculate desired statistics (expected values, threshold checks)
     *
     * Requires: nothing
     * Modifies: this
     * Effects:  continuously reads user input, processes commands, and prints results until quit.
     */
    public void runGachaApp() {
        boolean exit = false;
        while (!exit) {
            displayMenu();
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "add":
                    addNewPull();
                    break;
                case "view":
                    viewPulls();
                    break;
                case "remove":
                    removePull();
                    break;
                case "stats":
                    displayStats();
                    break;
                case "save":
                    saveData();
                    break;
                case "load":
                    loadData();
                    break;
                case "quit":
                    exit = true;
                    System.out.println("Exiting Gacha Statistics Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    /**
     * Displays the main menu of commands for the user.
     *
     * Requires: nothing
     * Modifies: nothing
     * Effects:  prints out a list of possible commands (e.g., "add", "view", "remove", "quit").
     */
    public void displayMenu() {
        System.out.println("\nGacha Statistics Tracker Menu:");
        System.out.println(" - add    : Add a new gacha pull record");
        System.out.println(" - view   : View all recorded gacha pulls");
        System.out.println(" - remove : Remove a gacha pull record");
        System.out.println(" - stats  : Display statistics");
        System.out.println(" - save   : Save gacha history to file");
        System.out.println(" - load   : Load gacha history from file");
        System.out.println(" - quit   : Exit the application");
    }

    /**
     * Requires: valid integer inputs for pull index and number of 4-star items.
     * Modifies: this, history
     * Effects:  Prompts the user for pull details and adds a new pull record.
     */
    private void addNewPull() {
        try {
            System.out.print("Enter pull index (integer >= 1): ");
            int pullIndex = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Was the 5-star item desired? (yes/no): ");
            String desiredInput = scanner.nextLine().trim().toLowerCase();
            boolean desired5Star = desiredInput.equals("yes") || desiredInput.equals("y");
            System.out.print("Enter number of 4-star items obtained: ");
            int numberOf4Stars = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter total number of draws in this pull: ");
            int drawCount = Integer.parseInt(scanner.nextLine().trim());

            GachaPull pull = new GachaPull(desired5Star, numberOf4Stars, pullIndex, drawCount);
            history.addPull(pull);
            System.out.println("Gacha pull record added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Enter valid integers for pull index, 4-star count, and draw count.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Requires: nothing
     * Modifies: nothing
     * Effects:  Displays all recorded gacha pull records.
     */
    private void viewPulls() {
        List<GachaPull> pulls = history.getAllPulls();
        if (pulls.isEmpty()) {
            System.out.println("No gacha pull records available.");
        } else {
            System.out.println("Gacha Pull Records:");
            for (GachaPull pull : pulls) {
                System.out.println("Pull " + pull.getPullIndex() + ": " 
                        + "Desired 5-star: " + pull.isDesired5Star() 
                        + ", 4-star count: " + pull.getNumberOf4Stars() 
                        + ", Draw count: " + pull.getDrawCount());
            }
        }
    }

    /**
     * Requires: valid integer input for pull index.
     * Modifies: this, history
     * Effects:  Prompts the user to remove a record by pull index.
     */
    private void removePull() {
        System.out.print("Enter the pull index of the record to remove: ");
        try {
            int indexToRemove = Integer.parseInt(scanner.nextLine().trim());
            GachaPull toRemove = null;
            for (GachaPull pull : history.getAllPulls()) {
                if (pull.getPullIndex() == indexToRemove) {
                    toRemove = pull;
                    break;
                }
            }
            if (toRemove != null) {
                history.removePull(toRemove);
                System.out.println("Record removed successfully.");
            } else {
                System.out.println("No record found with pull index: " + indexToRemove);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer for pull index.");
        }
    }

    /**
     * Requires: nothing
     * Modifies: nothing
     * Effects:  Displays the desired 5-star rate and average 4-star count,
     *           and checks if the desired 5-star rate is above a user-specified threshold.
     */
    private void displayStats() {
        double desiredRate = history.calculateFiveStarRate();
        double fourStarAvg = history.calculateFourStarAvg();
        System.out.println("Desired 5-star rate: " + String.format("%.2f", desiredRate));
        System.out.println("Average number of 4-star items per pull: " + String.format("%.2f", fourStarAvg));

        System.out.print("Enter threshold for desired 5-star rate: ");
        try {
            double threshold = Double.parseDouble(scanner.nextLine().trim());
            if (history.isAboveThreshold(threshold)) {
                System.out.println("Actual desired 5-star rate is above the threshold.");
            } else {
                System.out.println("Actual desired 5-star rate is not above the threshold.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid threshold input. Please enter a valid number.");
        }
    }

    /**
     * Saves the current GachaHistory to a JSON file.
     * Uses JsonWriter to write data to the default DATA_FILE.
     * Effects: Persists current state and prints success or error messages.
     */
    private void saveData() {
        JsonWriter writer = new JsonWriter(DATA_FILE);
        try {
            writer.open();
            writer.write(history);
            writer.close();
            System.out.println("Gacha history saved successfully to " + DATA_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to open file for writing: " + e.getMessage());
        }
    }

    /**
     * Loads GachaHistory from a JSON file.
     * Uses JsonReader to read data from the default DATA_FILE.
     * Effects: Replaces current history with the loaded history and prints success or error messages.
     */
    private void loadData() {
        JsonReader reader = new JsonReader(DATA_FILE);
        try {
            history = reader.read();
            System.out.println("Gacha history loaded successfully from " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error: Unable to read from file: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error: Invalid JSON data: " + e.getMessage());
        }
    }
    
}


