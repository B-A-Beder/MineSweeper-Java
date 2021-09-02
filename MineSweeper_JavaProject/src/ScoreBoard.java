import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreBoard {

	public static void main(String[] args) throws IOException {

		ScoreBoard scores = new ScoreBoard("Scores/ScoreBoard_Test.csv");

		scores.displayScores(System.out, 200);

	}

	/**
	 * constructs new ScoreBoard object: with target File
	 * 
	 * @param file target File
	 */
	public ScoreBoard(final File file) {
		this.file = file;
		Util.createFile(this.getFile());
	}

	/**
	 * constructs new ScoreBoard object: with File with target file name
	 * 
	 * @param fileName target file name of File
	 */
	public ScoreBoard(final String fileName) {
		this(new File(fileName));
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.getFile();
	}

	/**
	 * @return file to store this ScoreBoard in
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * gets Scores from File associated with this ScoreBoard, as ArrayList of Scores
	 * 
	 * @return ArrayList of Scores from File (can have size of zero if no Scores can
	 *         be parsed)
	 * @throws FileNotFoundException if File associated with this ScoreBoard cannot
	 *                               be found
	 */
	public ArrayList<Score> getScores() throws FileNotFoundException {
		Util.createFile(this.getFile());

		Scanner scan = null;
		scan = new Scanner(this.getFile());

		ArrayList<Score> scores = new ArrayList<Score>();

		while (scan.hasNextLine()) {
			String scoreStr = scan.nextLine();
			Score nextScore = Score.fromCSV(scoreStr);
			if (nextScore != null) {
				scores.add(nextScore);
			}
		}
		scan.close();
		return scores;
	}

	/**
	 * prints with sleep Scores from File associated with this ScoreBoard at target
	 * PrintStream, or prints appropriate messages if encounters file errors or if
	 * number of saved scores is zero
	 * 
	 * @param printer target PrintStream
	 * @param msec    time to sleep between printing each line, in milliseconds
	 */
	public void displayScores(final PrintStream printer, final long msec) {
		Util.createFile(this.getFile());
		ArrayList<Score> scores = null;
		try {
			scores = this.getScores();
		} catch (FileNotFoundException e) {
			Util.printlnWithSleep(printer, "SCORES READ ENCOUNTERED FILE ERRORS", msec);
			return;
		}

		if (scores.size() <= 0) {
			Util.printlnWithSleep(printer, "NO SAVED SCORES YET", msec);
			return;
		}

		for (int num = 0; num < scores.size(); num++) {
			String text = "";
			text += (num + 1) + ":\t";
			text += scores.get(num).toString().replace("\n", "\n\t\t");
			Util.printlnWithSleep(printer, text, msec);
			printer.println();
		}
	}

	/**
	 * saves target Score by printing it to end of File associated with this
	 * ScoreBoard
	 * 
	 * @param score target Score
	 * @throws FileNotFoundException if File associated with this ScoreBoard cannot
	 *                               be accessed
	 */
	public void printSaveScore(final Score score) throws FileNotFoundException {
		Util.createFile(this.getFile());

		PrintStream printer = null;
		FileOutputStream fos = null;
		fos = new FileOutputStream(this.getFile(), true);
		printer = new PrintStream(fos);

		score.toCSV(printer);
		printer.println();
		printer.close();
	}

	/* private fields */

	/**
	 * File to store this ScoreBoard in
	 */
	private final File file;

}
