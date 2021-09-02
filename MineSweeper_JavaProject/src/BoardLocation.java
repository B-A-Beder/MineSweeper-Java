/**
 * Object representing Location in a Board. Uses zero-based indexing.
 * Constructed from row and col values or alphanumeric String representation
 * (ex: "AA57").
 * 
 * @author B. A. Beder
 *
 */
public class BoardLocation implements Location {

	public static void main(String[] args) {
		System.out.println(BoardLocation.parseBoardLocation("AA57"));
		System.out.println(new BoardLocation(0, 0));
	}

	/**
	 * constructs new BoardLocation based on row (int) and col (int) values
	 * 
	 * @param row row row num in int form (ex: "6")
	 * @param col col num in int form (ex: 25)
	 * @throws IllegalStateException if values out of standard bounds
	 */
	public BoardLocation(int row, int col) throws IllegalStateException {
		this.row = row;
		this.col = col;
		this.assertClassInvariant();
	}

	/**
	 * constructs new BoardLocation based on row (String) and col (int) values
	 * 
	 * @param row row num in String form (ex: "AA")
	 * @param col col num in int form (ex: 25)
	 * @throws IllegalStateException if values out of standard bounds
	 */
	public BoardLocation(String row, int col) throws IllegalStateException {
		this(Util.stringToNumber(row.toUpperCase()) - 1, col - 1);
	}

	/**
	 * gets new constructed BoardLocation based on String representation
	 * 
	 * @param loc String representation (ex: "AA57")
	 * @return new BoardLocation
	 * @throws IllegalStateException if values out of standard bounds
	 */
	public static BoardLocation parseBoardLocation(String loc) throws IllegalStateException {
		// attempts to parse
		// if anything throws Exception, returns null
		try {
			String str = loc.toUpperCase();

			// row
			String row = "";
			int strIdx = 0;
			while (strIdx < str.length()) {
				if ('A' <= str.charAt(strIdx) && str.charAt(strIdx) <= 'Z') {
					row += String.valueOf(str.charAt(strIdx));
					strIdx++;
				} else {
					break;
				}
			}

			// col
			int col = Integer.parseInt(str.substring(strIdx));

			return new BoardLocation(row, col);

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * checks if target Location is in the bounds of target Board
	 * 
	 * @param loc   target Location
	 * @param board target Board
	 * @return false if loc or board are null, false if loc is out of board bounds,
	 *         true if loc is in board bounds
	 */
	public static boolean inBounds(Location loc, Board board) {
		if (loc == null) {
			return false;
		}
		if (board == null) {
			return false;
		}
		if (loc.getRow() < 0) {
			return false;
		}
		if (board.getRows() <= loc.getRow()) {
			return false;
		}
		if (loc.getCol() < 0) {
			return false;
		}
		if (board.getCols() <= loc.getCol()) {
			return false;
		}

		return true;
	}

	public String toString() {
		return Util.numberToString(this.getRow() + 1) + (this.getCol() + 1);
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getCol() {
		return col;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other.getClass() == this.getClass()) {
			return this.equals((BoardLocation) other);
		}
		return false;
	}

	public boolean equals(final BoardLocation other) {
		if (this == other) {
			return true;
		}
		return this.getRow() == other.getRow() && this.getCol() == other.getCol();
	}

	/* private methods */

	/**
	 * assert class invariant (things that should be true for any instance before
	 * and after any method)
	 */
	protected void assertClassInvariant() {
		if (this.getRow() < 0) {
			throw new IllegalStateException("Illegal Row Location: Less than 0");
		}
		if (this.getCol() < 0) {
			throw new IllegalStateException("Illegal Col Location: Less than 0");
		}
	}

	/* private fields */

	private final int row;
	private final int col;

}
