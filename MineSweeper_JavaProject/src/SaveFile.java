import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class SaveFile implements Comparable<SaveFile> {

	public SaveFile(File file) {
		this.file = file;
		Util.createFile(file);
	}

	public static SaveFile saveGame(MSGame game, File file) {
		SaveFile save = new SaveFile(file);
		Util.createFile(save.getFile());
		PrintStream printer = null;
		try {
			printer = new PrintStream(save.getFile());

			printer.println(SaveParts.TIMESTAMPPAIRS.fileLineIndicator());
			game.printTimePairs_CSV(printer);

			printer.println(SaveParts.MOVES.fileLineIndicator());
			printer.println(game.getMoves());

			printer.println(SaveParts.BOARD.fileLineIndicator());
			game.printBoard_CSV(printer);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			printer.close();
		} catch (Exception e) {
		}
		return save;
	}

	public String toString() {
		final String[][] scoreParts = {
				{ "Last Played", this.getTimestampPairs().getLast().getEndTimestamp().toString() },
				{ "Total Time", this.getTimestampPairs().getElapsedTimeString() },
				{ "Sessions Played", String.valueOf(this.getTimestampPairs().getSize()) },
				{ "Moves Played", String.valueOf(this.getMoves()) } };

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
		// Util.padStringArrayRight(values);

		String ret = "";
		ret += this.getFile().getName().substring(0,
				this.getFile().getName().length() - this.getFileExtension().length());
		ret += "\n";

		for (int num = 0; num < scoreParts.length; num++) {
			ret += labels[num];
			ret += "\t";
			ret += values[num];
			ret += "\n";
		}

		return ret;
	}

	public static boolean saveFileExists(File file) {
		return file.exists();
	}

	public File getFile() {
		return this.file;
	}

	public String getFileExtension() {
		int idx = this.getFile().getName().lastIndexOf(".");
		if (idx < 0) {
			return "";
		}
		return this.getFile().getName().substring(idx);
	}

	public TimestampPairs getTimestampPairs() {
		Util.createFile(this.getFile());
		Scanner scan = null;
		try {
			scan = new Scanner(this.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.equals(SaveParts.TIMESTAMPPAIRS.fileLineIndicator())) {
				break;
			}
		}

		TimestampPairs pairs = TimestampPairs.fromCSV(scan);

		scan.close();
		return pairs;
	}

	public int getMoves() {
		Util.createFile(this.getFile());
		Scanner scan = null;
		try {
			scan = new Scanner(this.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.equals(SaveParts.MOVES.fileLineIndicator())) {
				break;
			}
		}

		int moves = -1;
		try {
			moves = Integer.parseInt(scan.nextLine());
		} catch (Exception e) {
		}

		scan.close();
		return moves;
	}

	public Board getBoard() {
		Util.createFile(this.getFile());
		Scanner scan = null;
		try {
			scan = new Scanner(this.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.equals(SaveParts.BOARD.fileLineIndicator())) {
				break;
			}
		}

		Board board = Board.fromCSV(scan);

		scan.close();
		return board;
	}

	public MSGame getGame(PrintStream printer) {
		Util.createFile(this.getFile());

		try {

			TimestampPairs times = this.getTimestampPairs();
			if (times == null) {
				return null;
			}
			int moves = this.getMoves();
			if (moves < 0) {
				return null;
			}
			Board board = this.getBoard();
			if (board == null) {
				return null;
			}

			return new MSGame(printer, times, moves, board);

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int compareTo(SaveFile other) {
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
			cmp = this.getFile().compareTo(other.getFile());
		}

		return cmp;
	}

	/* private fields */

	private enum SaveParts {
		TIMESTAMPPAIRS, MOVES, BOARD;

		public String fileLineIndicator() {
			return "~~" + this.toString() + "~~";
		}
	}

	private final File file;

}
