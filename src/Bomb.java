/**
 * Subclass of Tile. 
 * 
 * @author B. A. Beder
 *
 */
public class Bomb extends Tile {

	/**
	 * constructs new Bomb: sets adjacent Bombs to at least target number of
	 * adjacent bombs
	 * 
	 * @param adjBombs target number of adjacent bombs
	 */
	public Bomb(int adjBombs) {
		this();
		while (this.getAdjBombs() < adjBombs) {
			this.incrAdjBombs();
		}
		assertClassInvariant();
	}

	/**
	 * constructs new Bomb: sets adjacent Bombs to at least number of adjacent bombs
	 * of target Tile
	 * 
	 * @param target target Tile
	 */
	public Bomb(Tile target) {
		this(target.getAdjBombs());
		assertClassInvariant();
	}

	public String toString() {
		return super.toString() + ", " + "Caused Lose?: " + this.getLost();
	}

	/**
	 * @return true if this Bomb caused game loss, false if now
	 */
	public boolean getLost() {
		return this.lost;
	}

	/**
	 * causes this Bomb to save that this Bomb caused game loss
	 */
	public void lose() {
		this.lost = true;
		assertClassInvariant();
	}

	/* private methods */

	/**
	 * constructs new Bomb: assumes zero adjacent Bombs
	 */
	private Bomb() {
		super();
		this.lost = false;
		assertClassInvariant();
	}

	/**
	 * @return how to display this Tile on the Board: when uncovered: when
	 *         appropriate, displays Graphics for if this Bomb caused the player to
	 *         lose or Graphics for if this Bomb did not cause the player to lose
	 */
	protected char tileDisplay_uncovered() {
		char ret = '0';
		if (this.getLost()) {
			ret = Graphics.BOMB_LOST.graphic();
		} else {
			ret = Graphics.BOMB_REMAINING.graphic();
		}
		assertClassInvariant();
		return ret;
	}

	/* private fields */

	/**
	 * if this Bomb caused player to lose
	 */
	private boolean lost;

}
