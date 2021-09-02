
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MineSweeper {

	public static void main(String[] args) {

		appRunning = true;

		introScreen();

		while (appRunning) {
			command();
		}
	}

	/* private methods */

	/**
	 * prints MineSweeper app intro screen to printer
	 */
	private static void introScreen() {
		ArrayList<String> text = new ArrayList<String>();

		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("--------------------     MINESWEEPER    ------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		// text.add("------------------------------ by Benjamin Beder
		// ---------------------");
		text.add("------------------------------- by B. A. Beder -----------------------");
		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("----------------------------------------------------------------------");
		text.add("");

		// TODO change sleep time
		Util.printlnWithSleep(printer, text, 500);
		text.clear();
	}

	// TODO create command enum + method system

	private enum Command {

		PLAY(() -> {

			playGame(null);

		}, "Play a New MINESWEEPER Game"), LOAD(() -> {

			loadGame();

		}, "Load a Saved MINESWEEPER Game"), SCORES(() -> {

			displayScores();

		}, "Display Recorded Scores"), QUIT(() -> {

			quitApp();

		}, "Quit the MINESWEEPER App");

		private final Lambda_Void_None command;
		private final String description;

		private Command(Lambda_Void_None command, String description) {
			this.command = command;
			this.description = description;
		}

		private void command() {
			this.command.function();
		}

		public String description() {
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

	private static void command() {
		ArrayList<String> text = new ArrayList<String>();

		text.add("");
		text.add("");
		text.add("Enter a Command:");

		text.add("(capitalization is ignored)");

		/*
		 * text.add("\tEnter \"Play\" or \"P\" to Play a New MINESWEEPER Game");
		 * 
		 * text.add("\tEnter \"Load\" or \"L\" to Load a Saved MINESWEEPER Game");
		 * 
		 * text.add("\tEnter \"Scores\" or \"S\" to Display Recorded Scores");
		 * text.add("\tEnter \"Quit\" or \"Q\" to Quit the MINESWEEPER App");
		 */

		for (Command command : Command.values()) {
			text.add("\tEnter \"" + command.toString() + "\" or \"" + command.getAbrev() + "\" to "
					+ command.description());
		}

		text.add("");

		Util.printlnWithSleep(printer, text, 350);
		text.clear();

		while (true) {
			// String input = console.nextLine();
			String input = console.next();
			console.nextLine();

			/*
			 * switch (command.toUpperCase()) {
			 * 
			 * case "PLAY": case "P": playGame(null);
			 * 
			 * 
			 * // Command.PLAY.command(); return; case "LOAD": case "L": loadGame(); return;
			 * case "SCORES": case "S": displayScores(); return; case "QUIT": case "Q":
			 * quitApp(); return;
			 * 
			 * default: text.add(""); text.add("Invalid Command: " + command);
			 * text.add("Please Enter a Valid Command"); text.add("");
			 * Util.printlnWithSleep(printer, text, 200); text.clear(); break; }
			 */

			Command command = Command.getCommand(input.toUpperCase());

			if (command == null) {
				text.add("");
				text.add("Invalid Command: " + input);
				text.add("Please Enter a Valid Command");
				text.add("");
				Util.printlnWithSleep(printer, text, 200);
				text.clear();
			} else {
				command.command();
				return;
			}

		}

	}

	private static void playGame(MSGame gameSave) {
		ArrayList<String> text = new ArrayList<String>();

		text.add("");
		text.add("");
		text.add("MINESWEEPER:");
		text.add("");
		text.add("");
		Util.printlnWithSleep(printer, text, 250);
		text.clear();

		text.add("COMMANDS:");
		text.add("");

		for (final MSGame.Command command : MSGame.Command.values()) {
			String desc = "";
			desc += "\t" + "\"" + command.toString() + "\" / \"" + command.getAbrev() + " ";

			if (command.getReqsLoc()) {
				desc += "<Location>" + " ";
			}

			desc += "=" + " " + command.getDescription();

			if (command.getReqsLoc()) {
				desc += " " + "@ " + "<Location>" + " ";
			}

			text.add(desc);
		}

		text.add("");
		text.add("\t" + "Example Command:");

		final MSGame.Command commandDemo = MSGame.Command.UNCOVER;

		text.add("\t\t" + commandDemo.getAbrev() + " A1");
		text.add("\t\t" + "To " + commandDemo.toString().toUpperCase().substring(0, 1)
				+ commandDemo.toString().toLowerCase().substring(1) + " @ A1");
		text.add("");
		text.add("");

		/*
		 * text.add("Q / Quit = Quit Current Game");
		 * 
		 * text.add("S / Save = Save Current Game");
		 * 
		 * text.add("U / Uncover <Location> = Uncover at <Location>");
		 * text.add("F / Flag <Location> = Flag at <Location>");
		 * text.add("Locations in Format \"A1\"");
		 * text.add("T / Test = For Every Tile with Adjacent Flags " +
		 * "\n\tGreater Than or Equal to Number of Adjacent Bombs, " +
		 * "\n\t Uncovers All Non-Flagged Adjacent Tiles");
		 * text.add("\tCan Result in Loss of Game"); text.add("");
		 * text.add("Example Command:"); text.add("U A1"); text.add("To Uncover at A1");
		 * text.add(""); text.add("");
		 */

		Util.printlnWithSleep(printer, text, 250);
		text.clear();

		MSGame game = gameSave;
		if (game == null) {
			game = new MSGame(printer, console);
		}
		game.display(printer);

		while (game.gameRunning()) {
			game.command(console.nextLine());
			// game.display(printer);
		}

		Score score = game.getScore();

		if (score.getWinState().equals(Score.WinState.SAVED)) {
			String saveName = "";
			while (true) {
				printer.println("Save Name?");
				saveName = console.nextLine();
				if (!saveFolder.saveFileExists(saveName)) {
					break;
				}

				text.add("Save Name Already in Use");
				text.add("Do You Wish to Override Current Save File?");
				text.add("Y/N");
				Util.printlnWithSleep(printer, text, 150);
				text.clear();

				Util.YesNo yn = null;
				while (true) {
					String command = console.next();
					console.nextLine();
					yn = Util.YesNo.getState(command);
					if (yn != null) {
						break;
					}
					text.add("");
					text.add("Invalid Command: " + command);
					text.add("Please Enter a Valid Command");
					text.add("");
					Util.printlnWithSleep(printer, text, 200);
					text.clear();
				}
				if (yn.getBoolean()) {
					break;
				}
			}

			saveFolder.saveGame(game, saveName);
		}

		Util.printlnWithSleep(printer, score.toString().replace("\n", "\n\t"), 250);

		if (score.getWinState().isScoreBoardRecord()) {
			try {
				scoreBoard.printSaveScore(score);
			} catch (FileNotFoundException e) {
				Util.printlnWithSleep(printer, "SCORES SAVE ENCOUNTERED FILE ERRORS", 150);
			}
		}
	}

	private static void loadGame() {
		ArrayList<String> text = new ArrayList<String>();

		text.add("");
		text.add("");
		text.add("SAVE FILES:");
		text.add("");
		Util.printlnWithSleep(printer, text, 250);
		text.clear();

		// TODO timings
		saveFolder.displaySaves(printer, 500);

		ArrayList<SaveFile> saveFiles = null;
		try {
			saveFiles = saveFolder.readFolder();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (saveFiles.size() <= 0) {
			return;
		}

		text.add("");
		text.add("Save #?");
		text.add("");
		Util.printlnWithSleep(printer, text, 150);
		text.clear();

		// File index
		int saveNum = 0;
		while (true) {
			String input = console.next();
			console.nextLine();
			try {
				saveNum = Integer.parseInt(input);
			} catch (Exception e) {
				printer.println("Invalid Input: Invalid Type: " + input);
				continue;
			}
			if (saveNum <= 0) {
				printer.println("Invalid Input: Rows Too Low: Less Than or Equal to Zero: " + saveNum);
				continue;
			}
			if (saveFiles.size() < saveNum) {
				printer.println("Invalid Input: Rows Too High: Greater Than Number of Saves " + "(" + saveFiles.size()
						+ ")" + ": " + saveNum);
				continue;
			}
			break;
		}

		MSGame game = null;

		// File file = saveFolder.getSaveFile(saveNum - 1);

		int saveIdx = saveNum - 1;

		try {
			// game = MSGame.fromFile(printer, file);

			game = saveFolder.getSaveFile(saveIdx).getGame(printer);

		} catch (Exception e) {// catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		text.add("");
		text.add("Delete Save File?");
		text.add("Y/N");
		text.add("");
		Util.printlnWithSleep(printer, text, 150);
		text.clear();

		/*
		 * deleteCheck: while (true) { String command = console.nextLine(); switch
		 * (command.toUpperCase()) {
		 * 
		 * case "YES": case "Y": case "DELETE": case "D": saveFolder.deleteFile(saveNum
		 * - 1); break deleteCheck; case "NO": case "N": case "SAVE": case "S": break
		 * deleteCheck;
		 * 
		 * default: text.add(""); text.add("Invalid Command: " + command);
		 * text.add("Please Enter a Valid Command"); text.add("");
		 * Util.printlnWithSleep(printer, text, 200); text.clear(); break; }
		 * 
		 * }
		 */

		Util.YesNo yn = null;
		while (true) {
			String command = console.next();
			console.nextLine();
			yn = Util.YesNo.getState(command);
			if (yn != null) {
				break;
			}
			text.add("");
			text.add("Invalid Command: " + command);
			text.add("Please Enter a Valid Command");
			text.add("");
			Util.printlnWithSleep(printer, text, 200);
			text.clear();
		}

		if (yn.getBoolean()) {
			saveFolder.deleteFile(saveIdx);
		}

		playGame(game);
	}

	private static void displayScores() {
		ArrayList<String> text = new ArrayList<String>();
		text.add("");
		text.add("");
		text.add("SCORES:");
		text.add("");
		text.add("");
		Util.printlnWithSleep(printer, text, 250);
		text.clear();

		// TODO timings
		// Util.printlnWithSleep(printer, scoreBoard.displayScores(), 500);
		scoreBoard.displayScores(printer, 500);

		text.add("");
		text.add("");
		Util.printlnWithSleep(printer, text, 250);
		text.clear();
	}

	private static void quitApp() {
		appRunning = false;
	}

	/* private fields */

	private static boolean appRunning = true;
	private static final PrintStream printer = System.out;
	private static final Scanner console = new Scanner(System.in);
	private static final ScoreBoard scoreBoard = new ScoreBoard("Score/MineSweeper_ScoreBoard.csv");
	private static final SaveFolder saveFolder = new SaveFolder("Saves", "save");

}
