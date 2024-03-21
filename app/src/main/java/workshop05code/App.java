package workshop05code;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
//Included for the logging exercise
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author sqlitetutorial.net
 */
public class App {
    // Start code for logging exercise
    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        try {// resources\logging.properties
            LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
    }
    private static final Logger logger = Logger.getLogger(App.class.getName());
    // logger.log(Level.INFO 



    // End code for logging exercise
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SQLiteConnectionManager wordleDatabaseConnection = new SQLiteConnectionManager("words.db");

        wordleDatabaseConnection.createNewDatabase("words.db");
        if (wordleDatabaseConnection.checkIfConnectionDefined()) {
            logger.log(Level.INFO, "Wordle created and connected.");
        } else {
            System.out.println("Not able to connect. Sorry!");
            return;
        }
        if (wordleDatabaseConnection.createWordleTables()) {
            logger.log(Level.INFO, "Wordle structures in place.");
        } else {
            System.out.println("Not able to launch. Sorry!");
            return;
        }

         // let's add some words to valid 4 letter words from the data.txt file (EDITED to make more secure, hopefully is fine)
        int validWordCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("resources/data.txt"))) {
            String line;
            int i = 1;
            while ((line = br.readLine()) != null) {
                if (line.length() == 4) {
                    logger.log(Level.FINE, "Loaded valid word: " + line);  // FINE because provides more detailed information
                    wordleDatabaseConnection.addValidWord(i, line);
                    validWordCount++;
                } else {
                    logger.log(Level.SEVERE, "Ignoring invalid word in data.txt: " + line);
                    // log invalid words encountered in the data file (Severe since data file is under our control)
                }
                i++;
            }
            logger.log(Level.INFO, "Loaded " + validWordCount + " valid words from data.txt"); // successful loading :D
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading data.txt", e);
            System.out.println("Not able to load. Sorry!");
            return;
        }


        // let's get them to enter a word
        // Issue is here

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a 4 letter word for a guess or q to quit: ");
            String guess = scanner.nextLine(); //issue

            while (!guess.equals("q")) {
                
                System.out.println("You've guessed '" + guess+"'.");
                

                if (wordleDatabaseConnection.isValidWord(guess)) { 
                    System.out.println("Success! It is in the the list.\n");
                }else{
                    System.out.println("Sorry. This word is NOT in the the list.\n");
                    logger.log(Level.INFO, "Players wrong guess: " + guess); //log the players guessing if wrong
                }

                System.out.print("Enter a 4 letter word for a guess or q to quit: " );
                guess = scanner.nextLine();
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            logger.log(Level.WARNING, "Error reading user input", e);
            System.out.println("There was an error. Please try again :(");
            e.printStackTrace();
        }

    }
}


// Print only game-related info to the console.
// • Log invalid guesses. Pick an appropriate logging level.
// • Log invalid words read from file data.txt. Log them at a severe level. This is because data.txt is under our control, so invalid
// input could be severe, and should be cleaned up.
// • Log all valid words from file data.txt. Don’t print them.
// • Log all exceptions. You can use logger.log(Level.WARNING, "Your message.", e) to log exception e. Pick a level appropriate to the seriousness of the exception. Do not print traces
// to screen. If you want to inform the user that something went wrong, use a message with generic wording.


/* LOGGING LEVELS:
 * SEVERE (Level.SEVERE)
 * WARNING (Level.WARNING)
 * INFO (Level.INFO)
 * CONFIG (Level.CONFIG)
 * FINE (Level.FINE)
 * FINER (Level.FINER)
 * FINEST (Level.FINEST)
 */