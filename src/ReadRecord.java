import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Reads objects from a file via deserialization. For Exercise 3.
 */
public class ReadRecord {

    /**
     * Stream to file to read objects from.
     */
    private ObjectInputStream input;

    /**
     * opens an ObjectInputStream using a FileInputStream
     */
    private void readObjectsFromFile(String name) {
        MusicRecord record;

        try {
            input = new ObjectInputStream(new FileInputStream(name));
        }
        catch (IOException ioException) {
            System.err.println("Error opening file.");
        }

        /* The following loop is supposed to use readObject method of
         * ObjectInputSteam to read a MusicRecord object from a binary file that
         * contains several records.
         * Loop should terminate when an EOFException is thrown.
         */
        System.out.println("\nObjects read from " + name + ":");
            try {
                while (true) {
                    // Read and deserialize object
                    record = (MusicRecord) input.readObject();

                    // Display record info to std out
                    System.out.println(record.getYear() + "  " +
                            record.getSongName() + "  " +
                            record.getSingerName() + "  " +
                            record.getPurchasePrice());
                }
            }
            catch (EOFException ignored) {
                // EOF has been reached, so terminate loop
            }
            catch (IOException e) {
                System.err.println("Error while trying to read from file");
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                System.err.println("Class of a serialized object cannot be found");
                e.printStackTrace();
            }
        }

    public static void main(String[] args) {
        ReadRecord d = new ReadRecord();
        d.readObjectsFromFile("mySongs.ser");
        d.readObjectsFromFile("allSongs.ser");
    }
}