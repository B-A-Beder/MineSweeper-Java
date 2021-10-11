import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Object record of game Score stats
 * 
 * @author B. A. Beder
 *
 */
public class Score implements Comparable<Score> {

	public Score(final WinState win, final int moves, final TimestampPairs timePairs, final int rows, final int cols,
			final int bombs, final int uncovered) {
		this.win = win;
		this.moves = moves;
		this.timePairs = timePairs;
		this.rows = rows;
		this.cols = cols;
		this.bombs = bombs;
		this.uncovered = uncovered;
	}

	/**
	 * gets a constructed Score based on comma separated values representation of a
	 * Score
	 * 
	 * @param str String representation of a Score, as comma separated values:
	 * @return constructed Score, or null if the CSV cannot be parsed to a Score
	 */
	public static Score fromCSV(final String str) {
		String[] vars = str.split(",");
		if (vars.length < 7) {
			return null;
		}

		try {
			WinState win = WinState.valueOf(vars[0]);
			int moves = Integer.parseInt(vars[1]);
			TimestampPairs timePairs = TimestampPairs.fromCSV(new Scanner(vars[2]), pairDelimiter, timeDelimiter);
			int rows = Integer.parseInt(vars[3]);
			int cols = Integer.parseInt(vars[4]);
			int bombs = Integer.parseInt(vars[5]);
			int uncovered = Integer.parseInt(vars[6]);

			return new Score(win, moves, timePairs, rows, cols, bombs, uncovered);

		} catch (Exception e) {
			return null;
		}
	}

	// TODO convert to array system with standard col size
	/*
	 * public String toString() { String ret = "";
	 * 
	 * ret += this.getWinState(); ret += "\n"; ret += "Moves:\t" + this.getMoves();
	 * ret += "\n"; ret += "Time:\t" + this.getElapsedTimeString(); ret += "\n"; //
	 * TODO fix tabs ret += "Started:\t" + this.getStartTimestampString(); ret +=
	 * "\n"; ret += "Ended:\t\t" + this.getEndTimestampString(); ret += "\n"; ret +=
	 * "Sessions:\t" + this.getSessions(); ret += "\n"; ret += "Rows:\t" +
	 * this.getRows(); ret += "\n"; ret += "Cols:\t" + this.getCols(); ret += "\n";
	 * ret += "Tiles:\t" + this.getNumTiles(); ret += "\n"; ret += "Bombs:\t" +
	 * this.getBombs(); ret += "\n"; ret += "Percent Bombs:\t" +
	 * String.format("%.3f%%", (100.0 * this.getBombs() / this.getNumTiles())); ret
	 * += "\n"; ret += "Uncovered Tiles:\t" + this.getUncovered(); ret += "\n"; ret
	 * += "Percent Uncovered:\t" + String.format("%.3f%%", (100.0 *
	 * this.getUncovered() / this.getNumTiles()));
	 * 
	 * return ret; }
	 */

	@Override
	public String toString() {
		final String[][] scoreParts = { //
				{ "Moves Played", String.valueOf(this.getMoves()) }, //
				{ "Total Time", this.getElapsedTimeString() }, //
				{ "Start Time", this.getStartTimestampString() }, //
				{ "End Time", this.getEndTimestampString() }, //
				{ "Sessions Played", String.valueOf(this.getSessions()) }, //
				{ "Rows", String.valueOf(this.getRows()) }, //
				{ "Columns", String.valueOf(this.getCols()) }, //
				{ "Tiles", String.valueOf(this.getNumTiles()) }, //
				{ "# Bombs", String.valueOf(this.getBombs()) }, //
				{ "% Bombs", String.format("%.3f%%", (100.0 * this.getBombs() / this.getNumTiles())) }, //
				{ "# Uncovered Tiles", String.valueOf(this.getUncovered()) }, //
				{ "% Uncovered Tiles", String.format("%.3f%%", (100.0 * this.getUncovered() / this.getNumTiles())) } //
			};

		for (int num = 0; num < scoreParts.length; num++) {
			if (scoreParts[num].length != 2) {
				throw new IllegalStateException("Invalid Entry Size");
			}
		}

		final String[] labels = new String[scoreParts.length];
		for (int num = 0; num < labels.length; num++) {
			labels[num] = scoreParts[num][0];
			labels[num] = labels[num] + ":";
		}
		Util.padStringArrayRight(labels);

		final String[] values = new String[scoreParts.length];
		for (int num = 0; num < values.length; num++) {
			values[num] = scoreParts[num][1];
		}

		String ret = "";
		ret += this.getWinState();
		ret += "\n";

		for (int num = 0; num < scoreParts.length; num++) {
			ret += labels[num];
			ret += "\t";
			ret += values[num];
			ret += "\n";
		}

		
		return ret;
	}

	/**
	 * @return String representation of this Score, as comma separated values:
	 */
	public void toCSV(final PrintStream printer) {
		printer.print(this.getWinState());
		printer.print(",");
		printer.print(this.getMoves());
		printer.print(",");
		this.getTimestampPairs().toCSV(printer, pairDelimiter, timeDelimiter);
		printer.print(",");
		printer.print(this.getRows());
		printer.print(",");
		printer.print(this.getCols());
		printer.print(",");
		printer.print(this.getBombs());
		printer.print(",");
		printer.print(this.getUncovered());
	}

	@Override
	public boolean equals(final Object other) {
		if (other.getClass() == this.getClass()) {
			return this.equals((Score) other);
		}
		return false;
	}

	public boolean equals(final Score other) {
		return this.getRows() == other.getRows() && this.getCols() == other.getCols()
				&& this.getBombs() == other.getBombs() && this.getUncovered() == other.getUncovered()
				&& this.getMoves() == other.getMoves() && this.getWinState().equals(other.getWinState())
				&& this.getTimestampPairs().equals(other.getTimestampPairs());
	}

	@Override
	public int compareTo(Score other) {
		if (this.equals(other)) {
			return 0;
		}
		TimestampPairs timePairs = this.getTimestampPairs();
		if (timePairs == null) {
			return -1;
		}
		TimestampPairs timePairsOther = this.getTimestampPairs();
		if (timePairsOther == null) {
			return 1;
		}
		int cmp = timePairs.compareTo(timePairsOther);
		if (cmp == 0) {
			cmp = this.getWinState().compareTo(other.getWinState());
		}
		return cmp;
	}

	/**
	 * @return win state
	 */
	public WinState getWinState() {
		return this.win;
	}

	/**
	 * @return number of moves
	 */
	public int getMoves() {
		return this.moves;
	}

	/**
	 * @return Timestamp object representing start time
	 */
	public Timestamp getStartTimestamp() {
		// return this.startTimestamp;
		return this.getTimestampPairs().getFirst().getStartTimestamp();
	}

	/**
	 * @return toString() of start time Timestamp object
	 */
	public String getStartTimestampString() {
		return this.getStartTimestamp().toString();
	}

	/**
	 * @return Timestamp object representing end time
	 */
	public Timestamp getEndTimestamp() {
		// return this.endTimestamp;
		return this.getTimestampPairs().getLast().getEndTimestamp();
	}

	/**
	 * @return toString() of end time Timestamp object
	 */
	public String getEndTimestampString() {
		return this.getEndTimestamp().toString();
	}

	/**
	 * @return TimestampPairs object representing set of session TimestampPair
	 */
	public TimestampPairs getTimestampPairs() {
		return this.timePairs;
	}

	/**
	 * @return elapsed time between start and end times, in milliseconds
	 */
	public long getElapsedTime() {
		// return Util.getElapsedTime(this.getStartTimestamp(), this.getEndTimestamp());
		return this.getTimestampPairs().getElapsedTime();
	}

	/**
	 * @return elapsed time between start and end times, as String in format
	 *         "HHHHhr:MMmin:SSsec:FFFFmil"
	 */
	public String getElapsedTimeString() {
		return Util.getElapsedTimeString(this.getElapsedTime());
	}

	public int getSessions() {
		return this.getTimestampPairs().getSize();
	}

	/**
	 * @return number of rows
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * @return number of columns
	 */
	public int getCols() {
		return this.cols;
	}

	public int getNumTiles() {
		return this.getRows() * this.getCols();
	}

	/**
	 * @return number of Bombs
	 */
	public int getBombs() {
		return this.bombs;
	}

	/**
	 * @return number of uncovered Tiles
	 */
	public int getUncovered() {
		return this.uncovered;
	}

	/**
	 * Enum set of Win States for a game. To be used as part of a Score.
	 * 
	 * @author B. A. Beder
	 *
	 */
	public enum WinState {
		/**
		 * Record that game was a Win. Counts as a ScoreBoard record.
		 */
		WIN(true),
		/**
		 * Record that game was a Loss. Counts as a ScoreBoard record.
		 */
		LOSS(true),
		/**
		 * Record that game was Quit. Counts as a ScoreBoard record.
		 */
		QUIT(true),
		/**
		 * Record that game was a Saved. Does Not count as a ScoreBoard record.
		 */
		SAVED(false);

		/**
		 * if this WinState counts as a ScoreBoard record
		 */
		private final boolean scoreBoardRecord;

		private WinState(final boolean scoreBoardRecord) {
			this.scoreBoardRecord = scoreBoardRecord;
		}

		/**
		 * parses WinState based on target ordinal in enum list
		 * 
		 * @param ordinal target ordinal
		 * @return WinState corresponding to target ordinal, null if not in enum set
		 */
		public static WinState valueOf(final int ordinal) {
			if (ordinal < 0 || WinState.values().length <= ordinal) {
				return null;
			}
			for (WinState state : WinState.values()) {
				if (ordinal == state.ordinal()) {
					return state;
				}
			}
			return null;
		}

		/**
		 * @return true if this WinState counts as a ScoreBoard record, false if not
		 */
		public boolean isScoreBoardRecord() {
			return this.scoreBoardRecord;
		}
	}

	/* private fields */

	// private final int win;
	private final WinState win;

	private final int moves;

	// Timestamp
	private final TimestampPairs timePairs;

	private static final String pairDelimiter = ";";
	private static final String timeDelimiter = "~";

	// MineSweeperGame Board stats
	private final int rows;
	private final int cols;
	private final int bombs;
	private final int uncovered;

}
