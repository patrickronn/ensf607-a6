import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * Reads music record info from a text file then serializes each record as a MusicRecord object.
 * Objects are written to a specified file name.
 *
 * For Exercise 3.
 */
public class WriteRecord {

	ObjectOutputStream objectOut = null;
	MusicRecord record = null;
	Scanner stdin = null;
	Scanner textFileIn = null;

	/**
	 * Creates an blank MusicRecord object
	 */
	public WriteRecord() {
		record = new MusicRecord();
	}

	/**
	 * Initializes the data fields of a record object
	 * @param year - year that song was purchased
	 * @param songName - name of the song
	 * @param singerName - singer's name
	 * @param price - CD price
	 */
	public void setRecord(int year, String songName, String singerName,
                                                                 double price) {
		record.setSongName(songName);
		record.setSingerName(singerName);
		record.setYear(year);
		record.setPrice(price);
	}

	/**
	 * Opens a file input stream, using the data field textFileIn
	 * @param textFileName name of text file to open
	 */
	public void openFileInputStream(String textFileName) {
		try {
			textFileIn = new Scanner(new FileInputStream(textFileName));
		}
		catch (IOException e) {
			System.err.println("Error opening file.");
			e.printStackTrace();
		}
	}

	/**
	 * Opens an ObjectOutputStream using objectOut data field
	 * @param objectFileName name of the object file to be created
	 */
	public void openObjectOutputStream(String objectFileName) {
		try {
			objectOut = new ObjectOutputStream(new FileOutputStream(objectFileName));
		}
		catch (IOException e) {
			System.err.println("Error creating file.");
			e.printStackTrace();
		}
	}

	/**
	 * Reads records from given text file, fills the blank MusicRecord
	 * created by the constructor with the existing data in the text
	 * file and serializes each record object into a binary file
	 */
	public void createObjectFile() {

		while (textFileIn.hasNext()) // loop until end of text file is reached
		{
			int year = Integer.parseInt(textFileIn.nextLine());
			System.out.print(year + "  ");       // echo data read from text file

			String songName = textFileIn.nextLine();
			System.out.print(songName + "  ");  // echo data read from text file

			String singerName = textFileIn.nextLine();
			System.out.print(singerName + "  "); // echo data read from text file

			double price = Double.parseDouble(textFileIn.nextLine());
			System.out.println(price + "  ");    // echo data read from text file

			setRecord(year, songName, singerName, price);
			textFileIn.nextLine();   // read the dashed lines and do nothing

            // Serialize/write object to file then instantiate a new empty record
			try {
				objectOut.writeObject(record);
				record = new MusicRecord();
			}
			catch (IOException e) {
				System.err.println("Error writing record to file.");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {

		WriteRecord d = new WriteRecord();

		String textFileName = "someSongs.txt"; // Name of a text file that contains
                                               // song records

		String objectFileName = "mySongs.ser"; // Name of the binary file to
                                               // serialize record objects

		d.openFileInputStream(textFileName);   // open the text file to read from

		d.openObjectOutputStream(objectFileName); // open the object file to
                                                  // write music records into it

		d.createObjectFile();   // read records from opened text file, and write
                                // them into the object file.
	}
}
