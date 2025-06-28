package ui;

/**
 * The main entry point for running the Gacha Statistics Tracker in console mode.
 * Contains only a short main method to instantiate and run the GachaApp.
 */
public class Main {

    /**
     * Requires: nothing
     * Modifies: none
     * Effects:  creates and runs the console-based GachaApp.
     */
    public static void main(String[] args) {
        new GachaApp().runGachaApp();
    }
}
