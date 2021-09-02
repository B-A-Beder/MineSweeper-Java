import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Scanner;

public class MSGame {

	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		PrintStream printer = System.out;

		MSGame game1 = new MSGame(printer, console);
		/*
		 * game1.command("u d4"); game1.display(printer);
		 * 
		 * game1.command("f a1"); game1.display(printer);
		 * 
		 * game1.command("f a1"); game1.display(printer);
		 * 
		 * File f = new File("Saves/SavesTests/GameSaveFile_test2.save");
		 * 
		 * game1.toFile(f);
		 * 
		 * MSGame game2 = null;
		 * 
		 * System.out.println("Change to New File");
		 * 
		 * try { game2 = MSGame.fromFile(f); } catch (FileNotFoundException |
		 * IllegalArgumentException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } game2.display(printer);
		 */
		game1.display(printer);

		while (game1.gameRunning()) {
			game1.command(console.nextLine());
			game1.display(printer);
		}
		// System.out.println(game.getScore());
		Util.printlnWithSleep(System.out, game1.getScore().toString().replace("\n", "\n\t"), 300);

	}

	public MSGame(PrintStream printer, Scanner console) {
		this.printer = printer;

		// rows
		int rows = -1;
		while (true) {
			printer.println("Please Enter Number of Rows for the Board");
			String input = console.next();
			console.nextLine();
			try {
				rows = Integer.parseInt(input);
			} catch (Exception e) {
				printer.println("Invalid Input: Invalid Type: " + input);
				printer.println("\t(This includes values greater than the max Java int, "
						+ "\n\twhich are way too large anyways!)");
				continue;
			}
			if (rows <= 0) {
				printer.println("Invalid Input: Rows Too Low: Less Than or Equal to Zero: " + rows);
				continue;
			}
			if (Board.maxTiles < rows) {
				printer.println("Invalid Input: Rows Too High: Greater Than Max Number of Tiles " + "(" + Board.maxTiles
						+ ")" + ": " + rows);
				continue;
			}
			break;
		}

		// cols
		int cols = -1;
		if (Board.maxTiles / 2 < rows) {
			cols = 1;
			printer.println("Number of Columns: " + cols);
		} else {
			while (true) {
				printer.println("Please Enter Number of Columns for the Board");
				String input = console.next();
				console.nextLine();
				try {
					cols = Integer.parseInt(input);
				} catch (Exception e) {
					printer.println("Invalid Input: Invalid Type: " + input);
					printer.println("\t(This includes values greater than the max Java int, "
							+ "\n\twhich are way too large anyways!)");
					continue;
				}
				if (cols <= 0) {
					printer.println("Invalid Input: Columns Too Low: Less Than or Equal to Zero: " + cols);
					continue;
				}
				if (Board.maxTiles / rows < cols) {
					printer.println("Invalid Input: Columns Too High: Area Greater Than Max Number of Tiles " + "("
							+ Board.maxTiles + ")" + ": " + cols);
					continue;
				}
				break;
			}
		}

		// bomb percent
		double bombPercent = -1;
		while (true) {
			printer.println("Please Enter Percentage of Bombs per Tiles for the Field");
			String input = console.next();
			console.nextLine();
			try {
				bombPercent = Double.parseDouble(input);
			} catch (Exception e) {
				printer.println("Invalid Input: Invalid Type: " + input);
				continue;
			}
			if (bombPercent < 0) {
				printer.println("Invalid Input: Value Too Low: " + bombPercent + "%");
				continue;
			}
			if (100 < bombPercent) {
				printer.println("Invalid Input: Value Too High: " + bombPercent + "%");
				continue;
			}
			break;
		}
		int bombs = (int) Math.ceil(bombPercent / 100 * rows * cols);

		this.score = null;
		this.timePairs = new TimestampPairs();
		this.startTime = Util.getTimestampNow();
		this.gameRunning = true;
		this.moves = 0;
		this.board = new Board(rows, cols, bombs);

		this.checkWin();
	}

	public MSGame(PrintStream printer, TimestampPairs times, int moves, Board board) {
		this.printer = printer;

		this.score = null;
		this.timePairs = times;
		this.startTime = Util.getTimestampNow();
		this.gameRunning = true;
		this.moves = moves;
		this.board = board;

		this.checkWin();
	}

	// TODO from save File
	/*public static MSGame fromFile(PrintStream printer, File file)
			throws FileNotFoundException, IllegalArgumentException {
		Scanner scan = new Scanner(file);
		MSGame game = MSGame.fromFile(printer, scan);
		scan.close();
		return game;
	}*/

	// TODO move file stuff to SaveFile class

	// TODO change save files to store TimestampPairs

	/*public static MSGame fromFile(PrintStream printer, Scanner scan) throws IllegalArgumentException {

		if (scan.hasNextLine()) {
			try {
				// start = Timestamp.valueOf(scan.nextLine());
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid Start Timestamp");
			}
		}

		int moves = -1;

		if (scan.hasNextLine()) {
			try {
				moves = Integer.valueOf(scan.nextLine());
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid Moves Integer");
			}

			if (moves < 0) {
				throw new IllegalArgumentException("Invalid Moves Value: Less Than Zero");
			}
		}

		Board board = Board.fromCSV(scan);

		if (board == null) {
			throw new IllegalArgumentException("Invalid Board");
		}

		// return new MSGame(printer, times, moves, board);
		return null;
	}*/

	// TODO change save files to store TimestampPairs

	/*public void toFile(File file) {
		Util.createFile(file);
		PrintStream printer = null;
		try {
			printer = new PrintStream(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		printer.println(this.getStartTime());
		printer.println(this.getMoves());
		this.getBoard().toCSV(printer);
		printer.close();
	}*/

	public PrintStream getPrinter() {
		return this.printer;
	}

	/*
	 * public void command(String str) { String command = str.toUpperCase();
	 * 
	 * if (!this.gameRunning) {
	 * printer.println("Game Has Ended, Cannot Take New Commands");
	 * printer.println("\n\n"); printer.println(this.getBoard().toDisplay()); }
	 * 
	 * if (command.isEmpty()) { return; }
	 * 
	 * if (command.equalsIgnoreCase("QUIT") || command.equalsIgnoreCase("Q")) {
	 * this.quit(); return; }
	 * 
	 * if (command.equalsIgnoreCase("SAVE") || command.equalsIgnoreCase("S")) {
	 * this.save(); return; }
	 * 
	 * if (command.equalsIgnoreCase("TEST") || command.equalsIgnoreCase("T")) {
	 * this.test(); } else {
	 * 
	 * String[] commandParts = command.split(" ");
	 * 
	 * if (commandParts.length < 2) {
	 * 
	 * printer.println("Invalid Command: Too Short: " + command); return; }
	 * 
	 * Location loc = BoardLocation.parseBoardLocation(commandParts[1]); if
	 * (!BoardLocation.inBounds(loc, this.getBoard())) {
	 * printer.println("Invalid Location: " + commandParts[1]); return; }
	 * 
	 * if (0 <= loc.getRow())
	 * 
	 * switch (commandParts[0]) { // uncover Tile case "UNCOVER": case "U":
	 * this.uncover(loc); break; // flag Tile case "FLAG": case "F": this.flag(loc);
	 * break; // invalid command default: printer.println("Invalid Command: " +
	 * command); return; } }
	 * 
	 * this.display(printer); this.checkWin(); }
	 */

	public enum Command {

		QUIT((game, loc) -> {

			game.quit();

		}, false, "Quit Current Game"), SAVE((game, loc) -> {

			game.save();

		}, false, "Save Current Game"), UNCOVER((game, loc) -> {

			game.uncover(loc);
			game.display(game.getPrinter());
			game.checkWin();

		}, true, "Uncover Tile"), FLAG((game, loc) -> {

			game.flag(loc);
			game.display(game.getPrinter());
			game.checkWin();

		}, true, "Flag Tile"), TEST((game, loc) -> {

			game.test();
			game.display(game.getPrinter());
			game.checkWin();

		}, false, "For Every Tile with Adjacent Flags " + "\n\t Greater Than or Equal to Number of Adjacent Bombs, "
				+ "\n\t Uncovers All Non-Flagged Adjacent Tiles" + "\n\t\t Can Result in Loss of Game");

		private final Lambda_Void_MSGameLocation command;
		private final boolean reqsLoc;
		private final String description;

		private Command(Lambda_Void_MSGameLocation command, boolean reqsLoc, String description) {
			this.command = command;
			this.reqsLoc = reqsLoc;
			this.description = description;
		}

		private void command(MSGame game, Location loc) {
			this.command.function(game, loc);
		}

		public boolean getReqsLoc() {
			return this.reqsLoc;
		}

		public String getDescription() {
			return this.description;
		}

		public String getAbrev() {
			return this.toString().substring(0, 1);
		}

		public static Command getCommand(String arg) {
			Command command = null;
			try {
				command = Command.valueOf(arg);
			} catch (Exception e) {
				for (Command curCommand : Command.values()) {
					if (curCommand.getAbrev().equalsIgnoreCase(arg)) {
						command = curCommand;
						break;
					}
					if (curCommand.toString().equalsIgnoreCase(arg)) {
						command = curCommand;
						break;
					}
				}
			}
			return command;
		}

	}

	// with enums
	public void command(final String str) {

		if (!this.gameRunning) {
			printer.println("Game Has Ended, Cannot Take New Commands");
			printer.println("\n\n");
			printer.println(this.getBoard().toDisplay());
		}

		if (str.isEmpty()) {
			return;
		}

		String[] commandParts = str.split(" ");

		Command command = Command.getCommand(commandParts[0]);

		if (command == null) {
			printer.println("Invalid Command: " + str);
			return;
		}

		Location loc = null;

		if (command.getReqsLoc()) {
			if (commandParts.length < 2) {

				printer.println("Invalid Command: Too Short: " + command);
				return;
			}

			loc = BoardLocation.parseBoardLocation(commandParts[1]);
			if (loc == null || !BoardLocation.inBounds(loc, this.getBoard())) {
				printer.println("Invalid Location: " + commandParts[1]);
				return;
			}
		}

		command.command(this, loc);

	}

	public void display(PrintStream printer) {
		printer.println("\n\n");
		printer.println(this.getBoard().toDisplay());
		printer.println();
		printer.println(this.getBoard().toDisplayStats());
		printer.println(this.toDisplayStatsTimes());
		printer.println();
	}

	public String toDisplayStatsTimes() {
		String ret = "";

		// TODO fix tabs
		ret += "Session:\t" + this.getSessionElapsedTimeString();
		ret += "\n";
		ret += "Total:\t\t" + this.getTotalElapsedTimeString();
		ret += "\n";

		return ret;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}

	public TimestampPair getSessionTimestampPair() {
		return TimestampPair.fromStartAndNow(this.getStartTime());
	}

	/**
	 * @return elapsed time between start and end times, in milliseconds
	 */
	public long getSessionElapsedTime() {
		// return Util.getElapsedTime(this.getStartTimestamp(), this.getEndTimestamp());
		return this.getSessionTimestampPair().getElapsedTime();
	}

	/**
	 * @return elapsed time between start and end times, as String in format
	 *         "HHHHhr:MMmin:SSsec:FFFFmil"
	 */
	public String getSessionElapsedTimeString() {
		return Util.getElapsedTimeString(this.getSessionElapsedTime());
	}

	/**
	 * @return elapsed time between start and end times, in milliseconds
	 */
	public long getTotalElapsedTime() {
		// return Util.getElapsedTime(this.getStartTimestamp(), this.getEndTimestamp());
		return this.getTimePairs().getElapsedTime() + getSessionElapsedTime();
	}

	/**
	 * @return elapsed time between start and end times, as String in format
	 *         "HHHHhr:MMmin:SSsec:FFFFmil"
	 */
	public String getTotalElapsedTimeString() {
		return Util.getElapsedTimeString(this.getTotalElapsedTime());
	}

	public void printTimePairs_CSV(PrintStream printer) {
		this.getTimePairs().toCSV(printer);
	}

	public int getMoves() {
		return this.moves;
	}

	public void printBoard_CSV(PrintStream printer) {
		this.getBoard().toCSV(printer);
	}

	public boolean gameRunning() {
		return this.gameRunning;
	}

	public Score getScore() {
		return this.score;
	}

	public Score endGame(Score.WinState winState) {
		this.getTimePairs().addPair(this.getSessionTimestampPair());
		this.gameRunning = false;
		return new Score(winState, this.getMoves(), this.getTimePairs(), this.getBoard().getRows(),
				this.getBoard().getCols(), this.getBoard().getBombs(), this.getBoard().getUncovered());
	}

	/* private methods */

	private void uncover(Location loc) {
		try {
			if (moves <= 0) {
				this.replaceBoardFirstMove(loc);
			}
			this.moves++;

			// uncover Tile
			// checks if lose
			boolean lost = this.getBoard().uncoverTile(loc);
			uncoverWhiteSpaceAll();

			if (lost) {
				this.score = this.endGame(Score.WinState.LOSS);
				this.getBoard().uncoverTileAll();
				return;
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			printer.println("Invalid Location: " + loc);
		}
	}

	private void uncoverWhiteSpaceAll() {
		boolean change = true;
		while (change) {
			change = false;
			for (int row = 0; row < this.getBoard().getRows(); row++) {
				for (int col = 0; col < this.getBoard().getCols(); col++) {
					Location loc = new BoardLocation(row, col);
					if (this.getBoard().isTileUncovered(loc) && this.getBoard().getTileAdjBombs(loc) == 0
							&& !this.getBoard().isTileFlagged(loc)) {
						Location adj = null;
						try {
							adj = new BoardLocation(loc.getRow() - 1, loc.getCol() - 1);
							if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
								this.getBoard().uncoverTile(adj);
								change = true;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow(), loc.getCol() - 1);
							if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
								this.getBoard().uncoverTile(adj);
								change = true;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() + 1, loc.getCol() - 1);
							if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
								this.getBoard().uncoverTile(adj);
								change = true;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() - 1, loc.getCol());
							if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
								this.getBoard().uncoverTile(adj);
								change = true;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() + 1, loc.getCol());
							if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
								this.getBoard().uncoverTile(adj);
								change = true;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() - 1, loc.getCol() + 1);
							if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
								this.getBoard().uncoverTile(adj);
								change = true;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow(), loc.getCol() + 1);
							if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
								this.getBoard().uncoverTile(adj);
								change = true;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() + 1, loc.getCol() + 1);
							if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
								this.getBoard().uncoverTile(adj);
								change = true;
							}
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	private void test() {
		boolean change = true;
		while (change) {
			change = false;
			for (int row = 0; row < this.getBoard().getRows(); row++) {
				for (int col = 0; col < this.getBoard().getCols(); col++) {
					Location loc = new BoardLocation(row, col);
					if (this.getBoard().isTileUncovered(loc) && !this.getBoard().isTileFlagged(loc)) {
						int adjFlags = 0;
						Location adj = null;
						try {
							adj = new BoardLocation(loc.getRow() - 1, loc.getCol() - 1);
							if (this.getBoard().isTileFlagged(adj)) {
								adjFlags++;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow(), loc.getCol() - 1);
							if (this.getBoard().isTileFlagged(adj)) {
								adjFlags++;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() + 1, loc.getCol() - 1);
							if (this.getBoard().isTileFlagged(adj)) {
								adjFlags++;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() - 1, loc.getCol());
							if (this.getBoard().isTileFlagged(adj)) {
								adjFlags++;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() + 1, loc.getCol());
							if (this.getBoard().isTileFlagged(adj)) {
								adjFlags++;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() - 1, loc.getCol() + 1);
							if (this.getBoard().isTileFlagged(adj)) {
								adjFlags++;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow(), loc.getCol() + 1);
							if (this.getBoard().isTileFlagged(adj)) {
								adjFlags++;
							}
						} catch (Exception e) {
						}
						try {
							adj = new BoardLocation(loc.getRow() + 1, loc.getCol() + 1);
							if (this.getBoard().isTileFlagged(adj)) {
								adjFlags++;
							}
						} catch (Exception e) {
						}

						if (this.getBoard().getTileAdjBombs(loc) <= adjFlags) {
							try {
								adj = new BoardLocation(loc.getRow() - 1, loc.getCol() - 1);
								if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
									uncover(adj);
									change = true;
								}
							} catch (Exception e) {
							}
							try {
								adj = new BoardLocation(loc.getRow(), loc.getCol() - 1);
								if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
									uncover(adj);
									change = true;
								}
							} catch (Exception e) {
							}
							try {
								adj = new BoardLocation(loc.getRow() + 1, loc.getCol() - 1);
								if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
									uncover(adj);
									change = true;
								}
							} catch (Exception e) {
							}
							try {
								adj = new BoardLocation(loc.getRow() - 1, loc.getCol());
								if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
									uncover(adj);
									change = true;
								}
							} catch (Exception e) {
							}
							try {
								adj = new BoardLocation(loc.getRow() + 1, loc.getCol());
								if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
									uncover(adj);
									change = true;
								}
							} catch (Exception e) {
							}
							try {
								adj = new BoardLocation(loc.getRow() - 1, loc.getCol() + 1);
								if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
									uncover(adj);
									change = true;
								}
							} catch (Exception e) {
							}
							try {
								adj = new BoardLocation(loc.getRow(), loc.getCol() + 1);
								if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
									uncover(adj);
									change = true;
								}
							} catch (Exception e) {
							}
							try {
								adj = new BoardLocation(loc.getRow() + 1, loc.getCol() + 1);
								if (!this.getBoard().isTileUncovered(adj) && !this.getBoard().isTileFlagged(adj)) {
									uncover(adj);
									change = true;
								}
							} catch (Exception e) {
							}
						}
					}
				}
			}
		}
	}

	private void flag(Location loc) {
		try {
			this.moves++;
			this.getBoard().flag(loc);
		} catch (ArrayIndexOutOfBoundsException e) {
			printer.println("Invalid Location: " + loc);
		}
	}

	private void quit() {
		this.score = this.endGame(Score.WinState.QUIT);
	}

	private void save() {
		this.score = this.endGame(Score.WinState.SAVED);
	}

	private void checkWin() {
		if (this.getBoard().getUncovered() == (this.getBoard().getRows() * this.getBoard().getCols())
				- this.getBoard().getBombs()) {
			this.score = this.endGame(Score.WinState.WIN);
		}
	}

	private Board getBoard() {
		return this.board;
	}

	private TimestampPairs getTimePairs() {
		return this.timePairs;
	}

	private void replaceBoardFirstMove(Location loc) {
		if ((this.getBoard().getRows() * this.getBoard().getCols()) <= this.getBoard().getBombs()) {
			return;
		}
		if (!this.getBoard().isTileBomb(loc) && this.getBoard().getTileAdjBombs(loc) == 0) {
			return;
		}

		printer.println();

		do {
			printer.println("RESETTING BOARD...");

			replaceBoardFirstMove_adjBombs(loc);
		} while (this.getBoard().isTileBomb(loc));

		printer.println("BOARD RESET");

	}

	private void replaceBoardFirstMove_adjBombs(Location loc) {
		if ((this.getBoard().getRows() * this.getBoard().getCols()) <= this.getBoard().getBombs()) {
			return;
		}

		this.board = Board.getNewResetBoard(this.getBoard());
		final int maxTests = 20;
		for (int maxAdjBombs = 0; maxAdjBombs <= 8; maxAdjBombs++) {

			printer.println("\tINCREASING TOLERANCES...");

			Timestamp initialTime_tests = Util.getTimestampNow();

			for (int test = 0; test < maxTests; test++) {
				if (this.getBoard().isTileBomb(loc) || maxAdjBombs < this.getBoard().getTileAdjBombs(loc)) {

					if (Util.isPastInitialTimeByEllapsed(initialTime_tests, 500)) {
						printer.println("\t\tREBUILDING BOARD...");
						initialTime_tests = Util.getTimestampNow();
					}

					this.board = Board.getNewResetBoard(this.getBoard());
				} else {
					return;
				}
			}
		}
	}

	/* private fields */

	private final PrintStream printer;

	/**
	 * set of TimestampPairs from sessions played
	 */
	private final TimestampPairs timePairs;

	/**
	 * current session start time
	 */
	private final Timestamp startTime;

	/**
	 * number of moves played
	 */
	private int moves;

	/**
	 * the Board
	 */
	private Board board;

	/**
	 * describes if this MSGame is running or ended
	 */
	private boolean gameRunning;

	/**
	 * Score object to describe this MSGame stats
	 */
	private Score score;

}
