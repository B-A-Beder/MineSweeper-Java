import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {

	public static void main(String[] args) {

		Board b1 = new Board(30, 20, 50);

		game: for (int row = 0; row < b1.getRows(); row++) {
			for (int col = 0; col < b1.getCols(); col++) {
				/*
				 * if (!b.uncoverTile(new BoardLocation(row, col))) { break game; }
				 */
				b1.uncoverTile(new BoardLocation(row, col));
			}
		}

		// b.uncoverTileAll();

		System.out.println(b1.toDisplay());
		System.out.println("Num of Bombs: " + b1.getBombs());

		File f = new File("Board_test.csv");
		PrintStream ps = null;
		try {
			ps = new PrintStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		b1.toCSV(ps);

		ps.close();

		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Board b2 = Board.fromCSV(scan);
		scan.close();

		System.out.println(b2.toDisplay());
		System.out.println("Num of Bombs: " + b2.getBombs());

	}

	/**
	 * constructs new Board: no Bombs
	 * 
	 * @param rows number of rows in this Board
	 * @param cols number of columns in this Board
	 */
	public Board(int rows, int cols) {
		this.board = new Tile[rows][cols];
		// this.bombs = 0;

		// construct Tiles
		for (int row = 0; row < this.getRows(); row++) {
			for (int col = 0; col < this.getCols(); col++) {
				this.board[row][col] = new Tile();
			}
		}

		// construct axis labels
		this.axisRows = new String[this.getRows()];
		for (int row = this.axisRows.length - 1; 0 <= row; row--) {
			this.axisRows[row] = Util.numberToString(row + 1);
		}
		Util.padStringArrayLeft(this.axisRows);

		this.axisCols = new String[this.getCols()];
		for (int col = this.axisCols.length - 1; 0 <= col; col--) {
			this.axisCols[col] = String.valueOf(col + 1);
		}
		Util.padStringArrayLeft(this.axisCols);

		assertClassInvariant();
	}

	/**
	 * constructs new Board: includes target number of Bombs, allocated randomly in
	 * this Board
	 * 
	 * @param rows  number of rows in this Board
	 * @param cols  number of columns in this Board
	 * @param bombs number of Bombs in this Board
	 */
	public Board(int rows, int cols, int bombs) {
		this(rows, cols);
		int bombCount = bombs;

		// add bombs
		if (bombCount <= 0) {
			bombCount = 0;
		} else if (this.getRows() * this.getCols() <= bombCount) {
			bombCount = this.getRows() * this.getCols();
		}

		while (this.getBombs() < bombCount && this.getBombs() < this.getRows() * this.getCols()) {
			final int bombRow = (int) Math.floor(Math.random() * this.getRows());
			final int bombCol = (int) Math.floor(Math.random() * this.getCols());
			Location bombLoc = new BoardLocation(bombRow, bombCol);

			this.addBomb(bombLoc);
		}

		assertClassInvariant();
	}

	/**
	 * constructs new Board: includes Bombs at target Locations
	 * 
	 * @param rows     number of rows in this Board
	 * @param cols     number of columns in this Board
	 * @param bombLocs Location Array of Locations of Bombs
	 */
	public Board(int rows, int cols, Location[] bombLocs) {
		this(rows, cols);

		for (Location bombLoc : bombLocs) {

			this.addBomb(bombLoc);
		}
	}

	/**
	 * construct new Board: includes Bombs at target Locations
	 * 
	 * @param rows     number of rows in this Board
	 * @param cols     number of columns in this Board
	 * @param bombLocs ArrayList of Locations of Locations of Bombs
	 */
	public Board(int rows, int cols, ArrayList<Location> bombLocs) {
		this(rows, cols, bombLocs.toArray(new Location[bombLocs.size()]));
	}

	/**
	 * gets new constructed reset Board, with dimensions and num of Bombs as old
	 * Board
	 * 
	 * @param board old Board to pull from
	 * @return new Board
	 */
	public static Board getNewResetBoard(Board board) {
		return new Board(board.getRows(), board.getCols(), board.getBombs());
	}

	/**
	 * gets new constructed Board, with Tile states based on String save file
	 * section in proper CSV format
	 * 
	 * @param str String containing save info on each Tile
	 * @return new Board
	 */
	public static Board fromCSV(String str) {
		Scanner strScan = new Scanner(str);
		Board board = Board.fromCSV(strScan);
		strScan.close();
		return board;
	}

	/**
	 * gets new constructed Board, with Tile states based on Scanner save file
	 * section in proper CSV format
	 * 
	 * @param scan Scanner containing save info on each Tile
	 * @return new Board
	 */
	public static Board fromCSV(Scanner scan) {

		if (!scan.hasNextLine()) {
			return null;
		}

		ArrayList<Location> bombs = new ArrayList<Location>();
		ArrayList<Location> uncovereds = new ArrayList<Location>();
		ArrayList<Location> flags = new ArrayList<Location>();

		int row = 0;

		String line = scan.nextLine();
		if (line.isEmpty()) {
			return null;
		}
		String[] rowTiles = line.split(",");
		final int cols = rowTiles.length;

		for (int col = 0; col < rowTiles.length; col++) {
			Location loc = new BoardLocation(row, col);
			try {
				Tile.parseCSVTileStates(rowTiles[col], loc, bombs, uncovereds, flags);
			} catch (IOException e) {
				return null;
			}
		}

		while (scan.hasNextLine()) {
			row++;
			line = scan.nextLine();
			if (line.isEmpty()) {
				break;
			}
			rowTiles = line.split(",");
			if (rowTiles.length != cols) {
				return null;
			}

			for (int col = 0; col < rowTiles.length; col++) {
				Location loc = new BoardLocation(row, col);
				try {
					Tile.parseCSVTileStates(rowTiles[col], loc, bombs, uncovereds, flags);
				} catch (IOException io) {
					return null;
				}
			}
		}

		Board board = new Board(row + 1, cols, bombs);

		for (Location loc : uncovereds) {
			board.uncoverTile(loc);
		}

		for (Location loc : flags) {
			board.flag(loc);
		}

		return board;

	}

	public String toString() {
		String ret = "";

		for (int row = 0; row < this.getRows(); row++) {
			for (int col = 0; col < this.getCols(); col++) {
				ret += this.getTile(row, col).tileDisplay();
			}
			ret += "\n";
		}

		assertClassInvariant();
		return ret;
	}

	public void toCSV(PrintStream printer) {
		for (int row = 0; row < this.getRows(); row++) {
			for (int col = 0; col < this.getCols(); col++) {
				String tileCSV = this.getTile(row, col).toCSV();
				printer.print(tileCSV);
				if (col < this.getCols() - 1) {
					printer.print(",");
				}
			}
			printer.println();
		}
	}

	/**
	 * @return String representation of this Board: includes row and col axes
	 */
	public String toDisplay() {
		String ret = "";

		// columns labels
		for (int digit = 0; digit < this.axisCols[this.getCols() - 1].length(); digit++) {
			// whitespace before board
			for (int letter = 0; letter < this.axisRows[this.getRows() - 1].length(); letter++) {
				ret += " ";
			}

			// boarder
			ret += Graphics.BOARDER_VERTICAL.graphic();

			// current digit of each column label
			for (int col = 0; col < this.getCols(); col++) {
				ret += String.valueOf(this.axisCols[col].charAt(digit));
			}

			ret += "\n";
		}

		// boarder
		for (int letter = 0; letter < this.axisRows[this.getRows() - 1].length(); letter++) {
			ret += Graphics.BOARDER_HORIZONTAL.graphic();
		}
		ret += Graphics.BOARDER_INTERSECTION_PLUS.graphic();
		for (int col = 0; col < this.getCols(); col++) {
			ret += Graphics.BOARDER_HORIZONTAL.graphic();
		}

		ret += "\n";

		for (int row = 0; row < this.getRows(); row++) {
			ret += this.axisRows[row];

			// boarder
			ret += Graphics.BOARDER_VERTICAL.graphic();

			for (int col = 0; col < this.getCols(); col++) {
				ret += this.getTile(row, col).tileDisplay();
			}

			ret += "\n";
		}

		assertClassInvariant();
		return ret;
	}

	public String toDisplayStats() {
		String ret = "";

		ret += "Tiles:\t" + this.getRows() * this.getCols();
		ret += "\t";
		ret += "Uncovered:\t" + this.getUncovered();
		ret += "\n";

		ret += "Bombs:\t" + this.getBombs();
		ret += "\t";
		ret += "Flags:\t" + this.getFlags();
		ret += "\n";

		return ret;
	}

	/**
	 * uncovers target Tile
	 * 
	 * @param loc target Tile
	 * @return true if uncovered Bomb, false if not
	 */
	public boolean uncoverTile(Location loc) throws ArrayIndexOutOfBoundsException {
		Tile tile = this.getTile(loc);
		tile.uncover();
		if (tile.isBomb()) {
			Bomb bomb = (Bomb) tile;
			bomb.lose();
			assertClassInvariant();
			return true;
		}
		assertClassInvariant();
		return false;
	}

	/**
	 * uncovers all Tiles
	 */
	public void uncoverTileAll() {
		for (int row = 0; row < this.getRows(); row++) {
			for (int col = 0; col < this.getCols(); col++) {
				try {
					if (!this.getTile(row, col).isFlag()) {
						this.getTile(row, col).uncover();
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}
		assertClassInvariant();
	}

	/**
	 * flags target Tile
	 * 
	 * @param loc target Tile
	 */
	public void flag(Location loc) throws ArrayIndexOutOfBoundsException {
		this.getTile(loc).flag();
		assertClassInvariant();
	}

	/**
	 * @return total rows of this Board
	 */
	public int getRows() {
		return this.board.length;
	}

	/**
	 * @return total cols of this Board
	 */
	public int getCols() {
		return this.board[0].length;
	}

	/**
	 * @return total num of bombs in this Board
	 */
	public int getBombs() {
		// return this.bombs;

		int bombs = 0;

		for (int row = 0; row < this.getRows(); row++) {
			for (int col = 0; col < this.getCols(); col++) {

				if (this.getTile(row, col).isBomb()) {
					bombs++;
				}
			}
		}

		return bombs;
	}

	public boolean isTileBomb(Location loc) {
		return this.getTile(loc).isBomb();
	}

	public boolean isTileBomb(int row, int col) {
		return this.getTile(row, col).isBomb();
	}

	public int getUncovered() {
		int uncovered = 0;
		for (int row = 0; row < this.getRows(); row++) {
			for (int col = 0; col < this.getCols(); col++) {
				if (this.getTile(row, col).isUncovered()) {
					uncovered++;
				}
			}
		}
		return uncovered;
	}

	public boolean isTileUncovered(Location loc) {
		return this.getTile(loc).isUncovered();
	}

	public int getFlags() {
		int flags = 0;
		for (int row = 0; row < this.getRows(); row++) {
			for (int col = 0; col < this.getCols(); col++) {
				if (this.getTile(row, col).isFlag()) {
					flags++;
				}
			}
		}
		return flags;
	}

	public boolean isTileFlagged(Location loc) {
		return this.getTile(loc).isFlag();
	}

	public int getTileAdjBombs(Location loc) {
		return this.getTile(loc).getAdjBombs();
	}

	/* private methods */

	private boolean addBomb(Location bombLoc) {
		if (!(this.getTile(bombLoc).isBomb())) {
			this.board[bombLoc.getRow()][bombLoc.getCol()] = new Bomb(this.getTile(bombLoc));
			this.tilesIncrAdjBombs(bombLoc);
			return true;
		}
		return false;
	}

	/**
	 * increments the num of adjacent Bomb to all Tile adjacent to target Tile
	 * 
	 * @param loc location of target Tile in this Board
	 */
	private void tilesIncrAdjBombs(Location loc) {
		try {
			this.getTile(loc.getRow() - 1, loc.getCol() - 1).incrAdjBombs();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			this.getTile(loc.getRow(), loc.getCol() - 1).incrAdjBombs();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			this.getTile(loc.getRow() + 1, loc.getCol() - 1).incrAdjBombs();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			this.getTile(loc.getRow() - 1, loc.getCol()).incrAdjBombs();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			this.getTile(loc.getRow() + 1, loc.getCol()).incrAdjBombs();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			this.getTile(loc.getRow() - 1, loc.getCol() + 1).incrAdjBombs();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			this.getTile(loc.getRow(), loc.getCol() + 1).incrAdjBombs();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			this.getTile(loc.getRow() + 1, loc.getCol() + 1).incrAdjBombs();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		assertClassInvariant();
	}

	/**
	 * @param loc target Location in this Board
	 * @return Tile at target loc stored in this Board
	 * @throws ArrayIndexOutOfBoundsException
	 */
	private Tile getTile(Location loc) throws ArrayIndexOutOfBoundsException {
		return this.getTile(loc.getRow(), loc.getCol());
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	private Tile getTile(int row, int col) throws ArrayIndexOutOfBoundsException {
		return this.board[row][col];
	}

	/**
	 * assert class invariant (things that should be true for any instance before
	 * and after any method)
	 */
	private void assertClassInvariant() {
		// tiles
		if (this.getRows() * this.getCols() < 0) {
			throw new IllegalStateException("Illegal Tiles State: Less than 0");
		}
		if (Board.maxTiles < this.getRows() * this.getCols()) {
			throw new IllegalStateException("Illegal Bombs State: Greater than max Tiles");
		}

		// bombs
		if (this.getBombs() < 0) {
			throw new IllegalStateException("Illegal Bombs State: Less than 0");
		}
		if (this.getRows() * this.getCols() < this.getBombs()) {
			throw new IllegalStateException("Illegal Bombs State: Greater than total Tiles");
		}

		// uncovered
		if (this.getUncovered() < 0) {
			throw new IllegalStateException("Illegal Uncovered State: Less than 0");
		}
		if (this.getRows() * this.getCols() < this.getUncovered()) {
			throw new IllegalStateException("Illegal Uncovered State: Greater than total Tiles");
		}

		// flags
		if (this.getFlags() < 0) {
			throw new IllegalStateException("Illegal Flags State: Less than 0");
		}
		if (this.getRows() * this.getCols() < this.getFlags()) {
			throw new IllegalStateException("Illegal Flags State: Greater than total Tiles");
		}

		// uncovered + flags
		if (this.getRows() * this.getCols() < this.getUncovered() + this.getFlags()) {
			throw new IllegalStateException("Illegal Uncovered and Flags State: Greater than total Tiles");
		}

		// axes
		if (this.axisRows.length != this.getRows()) {
			throw new IllegalStateException("Illegal Rows Axis State: Not equal to Board Rows");
		}
		if (this.axisCols.length != this.getCols()) {
			throw new IllegalStateException("Illegal Cols Axis State: Not equal to Board Cols");
		}
	}

	/* public fields */
	/**
	 * max num of Tiles per Board
	 */
	public final static int maxTiles = 100 * 100;

	/* private fields */

	/**
	 * 2D array of Tiles
	 */
	private Tile[][] board;
	/**
	 * num of total bombs
	 */
	// private int bombs;
	/**
	 * Constructor parameter for percent Tiles are Bombs
	 */
	// private double bombPercent;

	// board labels
	/**
	 * row axis labels
	 */
	private String[] axisRows;
	/**
	 * cols axis labels
	 */
	private String[] axisCols;

}
