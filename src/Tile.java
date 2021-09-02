import java.io.IOException;
import java.util.ArrayList;

/**
 * Object representing a Tile of a MSGame Board
 * 
 * @author B. A. Beder
 */
public class Tile {

	/**
	 * testing code
	 */
	public static void main(String[] args) {
		Tile t = new Tile();
		t.incrAdjBombs();
		t.incrAdjBombs();
		System.out.println(t.tileDisplay());

		Tile b = new Bomb(0);
		b.incrAdjBombs();
		System.out.println(b);
	}

	public Tile() {
		this.adjBombs = 0;
		this.uncovered = false;
		this.flagged = false;
	}

	/**
	 * Enum list of Tile States
	 * 
	 * @author B. A. Beder
	 *
	 */
	private enum TileState {
		/**
		 * represents a Tile (useful because pads between commas in CSV)
		 */
		TILE,
		/**
		 * represents a Bomb
		 */
		BOMB,
		/**
		 * represents an uncovered Tile
		 */
		UNCOVERED,
		/**
		 * represents a flagged Tile
		 */
		FLAGGED;

		/**
		 * @return abbreviated form of this enum's name
		 */
		public String getAbrev() {
			return this.toString().substring(0, 1);
		}
	}

	/**
	 * @return String CSV representation of this Tile
	 */
	public String toCSV() {
		String tileCSV = TileState.TILE.getAbrev();
		if (this.isBomb()) {
			tileCSV += TileState.BOMB.getAbrev();
		}
		if (this.isUncovered()) {
			tileCSV += TileState.UNCOVERED.getAbrev();
		}
		if (this.isFlag()) {
			tileCSV += TileState.FLAGGED.getAbrev();
		}
		return tileCSV;
	}

	/**
	 * parses String CSV representation of a Tile at target Location. Adds these
	 * tile states to corresponding ArrayLists.
	 * 
	 * @param tile       String CSV representation of a Tile
	 * @param loc        Location of that Tile
	 * @param bombs      ArrayList of Locations of Bombs
	 * @param uncovereds ArrayList of Locations of Uncovered Tiles
	 * @param flags      ArrayList of Locations of Flagged Tiles
	 * @throws IOException if that Tile is simultaneously uncovered and a Bomb or
	 *                     uncovered and flagged
	 */
	public static void parseCSVTileStates(final String tile, final Location loc, final ArrayList<Location> bombs,
			final ArrayList<Location> uncovereds, final ArrayList<Location> flags) throws IOException {

		boolean bomb = tile.contains(TileState.BOMB.getAbrev());
		boolean uncovered = tile.contains(TileState.UNCOVERED.getAbrev());
		boolean flag = tile.contains(TileState.FLAGGED.getAbrev());

		if (uncovered && (bomb || flag)) {
			throw new IOException();
		}

		if (bomb) {
			bombs.add(loc);
		}
		if (uncovered) {
			uncovereds.add(loc);
		}
		if (flag) {
			flags.add(loc);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + "Adjacent Bombs: " + this.getAdjBombs() + ", " + "Uncovered?: "
				+ this.isUncovered() + ", " + "Flagged?: " + this.isFlag();
	}

	/**
	 * @return char to display this Tile on the Board
	 */
	public char tileDisplay() {
		if (this.isUncovered()) {
			return this.tileDisplay_uncovered();
		}
		if (this.isFlag()) {
			return this.tileDisplay_flagged();
		}
		return this.tileDisplay_covered();
	}

	/**
	 * @return num of adjacent bombs
	 */
	public int getAdjBombs() {
		return this.adjBombs;
	}

	/**
	 * increments num of adjacent bombs
	 */
	public void incrAdjBombs() {
		this.adjBombs++;
		assertClassInvariant();
	}

	/**
	 * @return true if this Tile has been uncovered, false if not
	 */
	public boolean isUncovered() {
		return this.uncovered;
	}

	/**
	 * @return true if this Tile is a Bomb, false if not
	 */
	public boolean isBomb() {
		return this instanceof Bomb;
	}

	/**
	 * uncovers this Tile
	 */
	public void uncover() {
		this.uncovered = true;
		this.flagged = false;
		assertClassInvariant();
	}

	/**
	 * @return true if this Tile has been flagged, false if not
	 */
	public boolean isFlag() {
		return this.flagged;
	}

	/**
	 * alternates this Tile flagged status
	 * 
	 * @return true if this Tile is now flagged, false if unflagged
	 */
	public void flag() {
		this.flagged = !this.isFlag();
		this.uncovered = false;
		assertClassInvariant();
	}

	/* private methods */

	/**
	 * @return how to display this Tile on the Board: when uncovered: no adjacent
	 *         Bomb Graphics, or single digit number of adjacent Bomb
	 */
	protected char tileDisplay_uncovered() {
		char ret = '0';
		if (this.getAdjBombs() == 0) {
			ret = Graphics.TILE_ZERO_ADJ_BOMBS.graphic();
		} else {
			ret = String.valueOf(this.getAdjBombs()).charAt(0);
		}
		assertClassInvariant();
		return ret;
	}

	/**
	 * @return how to display this Tile on the Board: when flagged: displays a flag
	 *         Graphics
	 */
	protected char tileDisplay_flagged() {
		char ret = '0';
		if (this.isFlag()) {
			ret = Graphics.FLAG.graphic();
		}
		assertClassInvariant();
		return ret;
	}

	/**
	 * @return how to display this Tile on the Board: when still covered
	 */
	protected char tileDisplay_covered() {
		assertClassInvariant();
		return Graphics.TILE_COVERED.graphic();
	}

	/**
	 * assert class invariant (things that should be true for any instance before
	 * and after any method)
	 */
	protected void assertClassInvariant() {
		if (this.getAdjBombs() < 0) {
			throw new IllegalStateException("Illegal Adjacent Bombs State: Less than 0");
		}
		if (8 < this.getAdjBombs()) {
			throw new IllegalStateException("Illegal Adjacent Bombs State: Greater than 8");
		}
		if ((this.isUncovered() && this.isFlag())) {
			throw new IllegalStateException("Illegal Tile State: Simultaneously Uncovered and Flagged");
		}
	}

	/* private fields */

	/**
	 * num of adjacent bombs
	 */
	private int adjBombs;
	/**
	 * if this Tile has been uncovered yet
	 */
	private boolean uncovered;
	/**
	 * if this Tile has been flagged
	 */
	private boolean flagged;

}
