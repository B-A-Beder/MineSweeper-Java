import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Object storing TimestampPair objects. Implements Comparable. Can get each
 * TimestampPair. Can get elapsed time. Can export/import to CSV.
 * 
 * @author B. A. Beder
 *
 */
public class TimestampPairs implements Comparable<TimestampPairs> {

	public TimestampPairs() {
		this.pairs = new ArrayList<TimestampPair>();
		this.sort();
	}

	/**
	 * scans and constructs new TimestampPairs object from CSV format: delineates
	 * TimestampPair objects with new lines and Timestamp objects with default
	 * TimestampPair delineator
	 * 
	 * @param scan Scanner storing text of TimestampPair objects
	 * @return new constructed TimestampPairs object
	 */
	public static TimestampPairs fromCSV(final Scanner scan) {
		TimestampPairs pairs = new TimestampPairs();
		while (scan.hasNextLine()) {
			String csv = scan.nextLine();
			TimestampPair pair = TimestampPair.fromCSV(csv);
			if (pair == null) {
				break;
			}
			pairs.addPair(pair);
		}
		pairs.sort();
		return pairs;
	}

	/**
	 * scans and constructs new TimestampPairs object from CSV format: delineates
	 * TimestampPair objects and Timestamp objects with given Strings
	 * 
	 * @param scan          Scanner storing text of TimestampPair objects
	 * @param pairDelimiter delineates TimestampPair objects
	 * @param timeDelimiter delineates Timestamp objects
	 * @return new constructed TimestampPairs object
	 */
	public static TimestampPairs fromCSV(final Scanner scan, final String pairDelimiter, final String timeDelimiter) {
		TimestampPairs pairs = new TimestampPairs();

		if (!scan.hasNextLine()) {
			return pairs;
		}
		String line = scan.nextLine();

		String[] csvPairs = line.split(pairDelimiter);

		for (String csvTimes : csvPairs) {
			TimestampPair pair = TimestampPair.fromCSV(csvTimes, timeDelimiter);
			if (pair != null) {
				pairs.addPair(pair);
			}
		}

		pairs.sort();
		return pairs;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + "Number of " + TimestampPair.class.getSimpleName() + ": "
				+ this.getPairs().size();
	}

	/**
	 * prints this TimestampPairs object to CSV format: delineates TimestampPair
	 * objects with new lines and Timestamp objects with default TimestampPair
	 * delineator
	 * 
	 * @param printer target PrintStream to print to
	 */
	public void toCSV(final PrintStream printer) {
		for (TimestampPair pair : this.getPairs()) {
			printer.println(pair.toCSV());
		}
	}

	/**
	 * prints this TimestampPairs object to CSV format: delineates TimestampPair
	 * objects and Timestamp objects with given Strings
	 * 
	 * @param printer       target PrintStream to print to
	 * @param pairDelimiter delineates TimestampPair objects
	 * @param timeDelimiter delineates Timestamp objects
	 */
	public void toCSV(final PrintStream printer, final String pairDelimiter, final String timeDelimiter) {
		for (int pairNum = 0; pairNum < this.getPairs().size() - 1; pairNum++) {
			printer.print(this.getPairs().get(pairNum).toCSV(timeDelimiter));
			printer.print(pairDelimiter);
		}
		printer.print(this.getPairs().get(this.getPairs().size() - 1).toCSV(timeDelimiter));
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other.getClass() == this.getClass()) {
			return this.equals((TimestampPairs) other);
		}
		return false;
	}

	public boolean equals(final TimestampPairs other) {
		if (this == other) {
			return true;
		}
		if (this.getSize() != other.getSize()) {
			return false;
		}
		for (int num = 0; num < this.getSize(); num++) {
			if (!this.get(num).equals(other.get(num))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int compareTo(final TimestampPairs other) {
		this.sort();
		other.sort();
		if (this.equals(other)) {
			return 0;
		}
		int cmp = 0;
		for (int num = 0; num < this.getSize() && num < other.getSize(); num++) {
			cmp = this.get(this.getSize() - num - 1).compareTo(other.get(other.getSize() - num - 1));
			if (cmp != 0) {
				break;
			}
		}
		return cmp;
	}

	/**
	 * sorts elements stored in this TimestampPairs object. sorts by TimestampPair's
	 * compairTo() method.
	 */
	public void sort() {
		this.getPairs().sort((TimestampPair x, TimestampPair y) -> {
			return x.compareTo(y);
		});
	}

	/**
	 * adds given TimestampPair to this TimestampPairs object and re-sorts elements
	 * 
	 * @param pair TimestampPair to add
	 */
	public void addPair(final TimestampPair pair) {
		this.getPairs().add(pair);
		this.sort();
	}

	/**
	 * @return number of elements
	 */
	public int getSize() {
		return this.getPairs().size();
	}

	/**
	 * @return true if contains no elements, false if not
	 */
	public boolean isEmpty() {
		return this.getSize() <= 0;
	}

	/**
	 * @param index index of target TimestampPair
	 * @return TimestampPair at target index
	 */
	public TimestampPair get(final int index) {
		return this.getPairs().get(index);
	}

	/**
	 * @return first TimestampPair object stored in this TimestampPairs object
	 */
	public TimestampPair getFirst() {
		if (this.isEmpty()) {
			return null;
		}
		this.sort();
		return this.get(0);
	}

	/**
	 * @return last TimestampPair object stored in this TimestampPairs object
	 */
	public TimestampPair getLast() {
		if (this.isEmpty()) {
			return null;
		}
		this.sort();
		return this.get(this.getSize() - 1);
	}

	/**
	 * @return elapsed time between start and end times, in milliseconds
	 */
	public long getElapsedTime() {
		long elapsed = 0;
		for (TimestampPair pair : this.getPairs()) {
			elapsed += pair.getElapsedTime();
		}
		return elapsed;
	}

	/**
	 * @return elapsed time between start and end times, as String in format
	 *         "HHHHhr:MMmin:SSsec:FFFFmil"
	 */
	public String getElapsedTimeString() {
		return Util.getElapsedTimeString(this.getElapsedTime());
	}

	/* private methods */

	/**
	 * @return ArrayList of TimestampPair stored in this TimestampPairs object
	 */
	private ArrayList<TimestampPair> getPairs() {
		return this.pairs;
	}

	/* private fields */

	/**
	 * ArrayList of TimestampPair
	 */
	private final ArrayList<TimestampPair> pairs;

}
