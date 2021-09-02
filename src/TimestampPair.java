import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Object storing two Timestamp objects. Implements Comparable. Can get each
 * Timestamp. Can get elapsed time. Can export/import to CSV.
 * 
 * @author B. A. Beder
 *
 */
public class TimestampPair implements Comparable<TimestampPair> {

	public static void main(String[] args) {
		ArrayList<TimestampPair> list = new ArrayList<TimestampPair>();

		Timestamp temp = Util.getTimestampNow();
		Util.sleep(500);
		list.add(TimestampPair.fromStartAndNow(temp));

		temp = Util.getTimestampNow();
		Util.sleep(300);
		list.add(TimestampPair.fromStartAndNow(temp));

		Collections.sort(list);

		for (TimestampPair pair : list) {
			System.out.println(pair.toCSV());
		}
	}

	/**
	 * constructs new TimestampPair object from given initial start time Timestamp
	 * and final end time Timestamp
	 * 
	 * @param start initial start time Timestamp
	 * @param end   final end time Timestamp
	 */
	public TimestampPair(final Timestamp start, final Timestamp end) {
		this.start = start;
		this.end = end;
		assertClassInvariant();
	}

	/**
	 * gets and constructs new TimestampPair object from given initial start time
	 * Timestamp and current now Timestamp
	 * 
	 * @param start initial start time Timestamp
	 * @return new constructed TimestampPair object
	 */
	public static TimestampPair fromStartAndNow(final Timestamp start) {
		return new TimestampPair(start, Util.getTimestampNow());
	}

	/**
	 * parses and constructs new TimestampPair object from CSV format: delineates
	 * Timestamp objects with commas
	 * 
	 * @param csv String storing text of TimestampPair objects
	 * @return new constructed TimestampPair object
	 */
	public static TimestampPair fromCSV(final String csv) {
		return TimestampPair.fromCSV(csv, ",");
	}

	/**
	 * parses and constructs new TimestampPair object from CSV format: delineates
	 * Timestamp objects with given Strings
	 * 
	 * @param csv           String storing text of TimestampPair objects
	 * @param timeDelimiter delineates Timestamp objects
	 * @return new constructed TimestampPair object
	 */
	public static TimestampPair fromCSV(final String csv, final String timeDelimiter) {
		try {
			String[] strs = csv.split(timeDelimiter);

			if (strs.length < 2) {
				return null;
			}

			Timestamp start = Timestamp.valueOf(strs[0]);
			Timestamp end = Timestamp.valueOf(strs[1]);

			return new TimestampPair(start, end);

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "Start: " + this.getStartTimestamp() + ", " + "End: " + this.getEndTimestamp();
	}

	/**
	 * converts this TimestampPair object to String CSV format: delineates Timestamp
	 * objects with commas
	 * 
	 * @return String CSV format representation of this TimestampPair object
	 */
	public String toCSV() {
		return this.toCSV(",");
	}

	/**
	 * converts this TimestampPair object to String CSV format: delineates Timestamp
	 * objects with given Strings
	 * 
	 * @param timeDelimiter delineates Timestamp objects
	 * @return String CSV format representation of this TimestampPair object
	 */
	public String toCSV(final String timeDelimiter) {
		return this.getStartTimestamp().toString() + timeDelimiter + this.getEndTimestamp().toString();
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other.getClass() == this.getClass()) {
			return this.equals((TimestampPair) other);
		}
		return false;
	}

	public boolean equals(final TimestampPair other) {
		if (this == other) {
			return true;
		}
		return this.getStartTimestamp().equals(other.getStartTimestamp())
				&& this.getEndTimestamp().equals(other.getEndTimestamp());
	}

	@Override
	public int compareTo(final TimestampPair other) {
		if (this.equals(other)) {
			return 0;
		}
		int cmp = this.getEndTimestamp().compareTo(other.getEndTimestamp());
		if (cmp == 0) {
			cmp = this.getStartTimestamp().compareTo(other.getStartTimestamp());
		}
		return cmp;
	}

	/**
	 * @return Timestamp representing initial start time
	 */
	public Timestamp getStartTimestamp() {
		return this.start;
	}

	/**
	 * @return Timestamp representing final end time
	 */
	public Timestamp getEndTimestamp() {
		return this.end;
	}

	/**
	 * @return elapsed time between start and end times, in milliseconds
	 */
	public long getElapsedTime() {
		return Util.getElapsedTime(this.getStartTimestamp(), this.getEndTimestamp());
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
	 * assert class invariant (things that should be true for any instance before
	 * and after any method)
	 */
	private void assertClassInvariant() {
		if (this.getEndTimestamp().before(this.getStartTimestamp())) {
			throw new IllegalStateException("End before Start");
		}
	}

	/* private fields */

	/**
	 * Timestamp representing initial start time
	 */
	private final Timestamp start;
	/**
	 * Timestamp representing final end time
	 */
	private final Timestamp end;

}
