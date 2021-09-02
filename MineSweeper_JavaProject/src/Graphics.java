/**
 * stores MineSweeperGame Graphics to display
 * 
 * @author B. A. Beder
 */
public enum Graphics {

	// TILES

	// BOMBS
	BOMB_LOST('*'), BOMB_REMAINING('^'),
	// FLAGS
	FLAG('@'),
	// BASIC TILES
	TILE_COVERED('#'), TILE_ZERO_ADJ_BOMBS(' '),

	// BOARD UI

	// BOARDER
	BOARDER_HORIZONTAL('-'), BOARDER_VERTICAL('|'), BOARDER_INTERSECTION_PLUS('+');

	private final char graphic;

	private Graphics(char graphic) {
		this.graphic = graphic;
	}

	public char graphic() {
		return this.graphic;
	}

}
