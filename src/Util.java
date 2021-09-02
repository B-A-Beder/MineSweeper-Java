import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

// Github

/**
 * Class of utility methods
 * 
 * @author B. A. Beder
 *
 */
public class Util {

	/**
	 * test code: confirms number conversion
	 */
	public static void main(String[] args) {
		System.out.println(stringToNumber("AA"));
		System.out.println(numberToString(27));
	}

	// Number representation

	/**
	 * parses String representation of a number to an int (ex: "AA" -> 27)
	 * 
	 * @param str String representation of number (ex: "AA")
	 * @return int representation of number (ex: 27)
	 * @author Akanksha Rai (Abby_akku)
	 */
	public static int stringToNumber(String str) {
		// This process is similar to
		// binary-to-decimal conversion
		int result = 0;
		for (int i = 0; i < str.length(); i++) {
			result *= 26;
			result += str.charAt(i) - 'A' + 1;
		}
		return result;
	}

	/**
	 * converts target int to a String representation of target number (ex: 27 ->
	 * "AA")
	 * 
	 * @param num int representation of number (ex: 27)
	 * @return String representation of number (ex: "AA")
	 * @author Harikrishnan Rajan
	 */
	public static String numberToString(int num) {
		// To store result (Excel column name)
		StringBuilder columnName = new StringBuilder();

		while (num > 0) {
			// Find remainder
			int rem = num % 26;

			// If remainder is 0, then a
			// 'Z' must be there in output
			if (rem == 0) {
				columnName.append("Z");
				num = (num / 26) - 1;
			} else // If remainder is non-zero
			{
				columnName.append((char) ((rem - 1) + 'A'));
				num = num / 26;
			}
		}

		return columnName.reverse().toString();
	}

	// Sleep

	/**
	 * causes Thread to sleep for target time, avoids Exceptions
	 * 
	 * @param msec time to sleep, in milliseconds
	 */
	public static void sleep(final long msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * println each element of target array, sleeping between each println
	 * 
	 * @param printer PrintStream to print to
	 * @param text    String array to print
	 * @param msec    time to sleep between printing each array element, in
	 *                milliseconds
	 */
	public static void printlnWithSleep(final PrintStream printer, final String[] text, final long msec) {
		for (String line : text) {
			printer.println(line);
			Util.sleep(msec);
		}
	}

	/**
	 * println each element of target ArrayList<String>, sleeping between each
	 * println
	 * 
	 * @param printer PrintStream to print to
	 * @param text    ArrayList<String> to print
	 * @param msec    time to sleep between printing each ArrayList<String> element,
	 *                in milliseconds
	 */
	public static void printlnWithSleep(final PrintStream printer, final ArrayList<String> text, final long msec) {
		for (String line : text) {
			printer.println(line);
			Util.sleep(msec);
		}
	}

	/**
	 * println each line of target String (using '\n' delimiter), sleeping between
	 * each println
	 * 
	 * @param printer PrintStream to print to
	 * @param text    String to print
	 * @param msec    time to sleep between printing each line, in milliseconds
	 */
	public static void printlnWithSleep(final PrintStream printer, final String text, final long msec) {
		Util.printlnWithSleep(printer, text.split("\n"), msec);
	}

	// Timestamp

	/**
	 * @return current (now) Timestamp
	 */
	public static Timestamp getTimestampNow() {
		return Timestamp.from(Instant.now());
	}

	/**
	 * @param start initial start time Timestamp
	 * @param end   final end time Timestamp
	 * @return elapsed time between start and end times, in milliseconds
	 */
	public static long getElapsedTime(final Timestamp start, final Timestamp end) {
		return end.getTime() - start.getTime();
	}

	/**
	 * @param elapsed elapsed time, in milliseconds
	 * @return elapsed time between start and end times, as String in format
	 *         "HHHHhr:MMmin:SSsec:FFFFmil"
	 */
	public static String getElapsedTimeString(final long elapsed) {
		long mil = elapsed % 1000;
		long sec = (elapsed / (1000)) % 60;
		long min = (elapsed / (1000 * 60)) % 60;
		long hr = (elapsed / (1000 * 60 * 60));

		return hr + "hr:" + min + "min:" + sec + "sec:" + mil + "mil";
	}

	/**
	 * checks if elapsed time between current (now) Timestamp and target initial
	 * Timestamp is greater than target time
	 * 
	 * @param initial initial target Timestamp checked
	 * @param msec    target minimum time elapsed, in milliseconds
	 * @return true if elapsed time is greater than target time, false if not
	 */
	public static boolean isPastInitialTimeByEllapsed(final Timestamp initial, final long msec) {
		return (msec < getElapsedTime(initial, getTimestampNow()));
	}

	// Files

	/**
	 * creates target directories, avoids Exceptions
	 * 
	 * @param directories target directories
	 */
	public static void createDirectories(final File directories) {
		try {
			Files.createDirectories(directories.toPath());
		} catch (IOException e) {
		}
	}

	/**
	 * creates target file, including any parent directories needed, avoids
	 * Exceptions
	 * 
	 * @param file target file path, can include parent directories
	 */
	public static void createFile(final File file) {
		try {
			Util.createDirectories(file.getParentFile());
		} catch (NullPointerException e) {
		}

		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Padding

	/**
	 * pads all elements with spaces so all elements are the same length: pads with
	 * spaces to the left
	 * 
	 * @param arr target String array
	 * @return modified version of target String array
	 */
	public static String[] padStringArrayLeft(final String[] arr) {
		return padStringArrayMargin(arr, false);
	}

	/**
	 * pads all elements with spaces so all elements are the same length: pads with
	 * spaces to the right
	 * 
	 * @param arr target String array
	 * @return modified version of target String array
	 */
	public static String[] padStringArrayRight(final String[] arr) {
		return padStringArrayMargin(arr, true);
	}

	/**
	 * pads all elements with spaces so all elements are the same length: pads with
	 * spaces to direction dependent on boolean parameter
	 * 
	 * @param arr      target String array
	 * @param padRight boolean controlling pad direction: true -> pad right, false
	 *                 -> pad left
	 * @return modified version of target String array
	 */
	private static String[] padStringArrayMargin(final String[] arr, final boolean padRight) {
		final int dir;
		if (padRight) {
			dir = -1;
		} else {
			dir = 1;
		}

		int longest = 0;
		for (int num = 0; num < arr.length; num++) {
			if (arr[longest].length() < arr[num].length()) {
				longest = num;
			}
		}

		for (int num = 0; num < arr.length; num++) {
			arr[num] = String.format("%" + (dir * arr[longest].length()) + "s", arr[num]);
		}

		return arr;
	}

	// Yes No Boolean

	/**
	 * Enum set to simulate booleans, using Yes/No input
	 * 
	 * @author B. A. Beder
	 *
	 */
	public enum YesNo {

		/**
		 * input representing "YES", representing true
		 */
		YES(true),
		/**
		 * input representing "NO", representing false
		 */
		NO(false);

		/**
		 * corresponding boolean value
		 */
		private final boolean bool;

		private YesNo(final boolean bool) {
			this.bool = bool;
		}

		/**
		 * @return corresponding boolean value
		 */
		public boolean getBoolean() {
			return this.bool;
		}

		/**
		 * @return abbreviated form of this enum's name
		 */
		public String getAbrev() {
			return this.toString().substring(0, 1);
		}

		/**
		 * parses target String to corresponding YesNo enum
		 * 
		 * @param arg target String
		 * @return corresponding YesNo enum if can be properly parsed, null if not
		 */
		public static YesNo getState(final String arg) {
			YesNo yn = null;
			try {
				yn = YesNo.valueOf(arg);
			} catch (Exception e) {
				for (YesNo curEnum : YesNo.values()) {
					if (curEnum.getAbrev().equalsIgnoreCase(arg)) {
						yn = curEnum;
						break;
					}
					if (curEnum.toString().equalsIgnoreCase(arg)) {
						yn = curEnum;
						break;
					}
				}
			}
			return yn;
		}

	}

}
