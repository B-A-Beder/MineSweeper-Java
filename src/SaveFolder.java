import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class SaveFolder {

	public static void main(String[] args) {
		File f = new File("Saves/SavesTests");

		SaveFolder sf = new SaveFolder(f, "save");

		sf.displaySaves(System.out, 200);

		MSGame game = null;

		try {
			// game = MSGame.fromFile(System.out, sf.getSaveFile(0));
			game = sf.getSaveFile(0).getGame(System.out);
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		game.display(System.out);

		Scanner console = new Scanner(System.in);

		while (game.gameRunning()) {
			game.command(console.nextLine());
			game.display(System.out);
		}
		System.out.println(game.getScore());

	}

	public SaveFolder(File folder, String extension) {
		this.folder = folder;
		Util.createDirectories(this.getFolder());
		this.extension = extension;
	}

	public SaveFolder(String fileName, String extension) {
		this(new File(fileName), extension);
	}

	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.getFolder();
	}

	public File getFolder() {
		return this.folder;
	}

	public String getExtension() {
		return this.extension;
	}

	public static String getExtensionDelimiter() {
		return extDelimiter;
	}

	// TODO change files to SaveFile system

	/*
	 * public ArrayList<File> readFolder() throws FileNotFoundException {
	 * Util.createDirectories(this.getFolder());
	 * 
	 * ArrayList<File> files = new ArrayList<File>();
	 * 
	 * try (DirectoryStream<Path> stream =
	 * Files.newDirectoryStream(this.getFolder().toPath(), "*" + this.extension)) {
	 * for (Path entry : stream) { files.add(entry.toFile()); } } catch (IOException
	 * e) { e.printStackTrace(); }
	 * 
	 * return files; }
	 */

	public ArrayList<SaveFile> readFolder() throws FileNotFoundException {
		Util.createDirectories(this.getFolder());

		ArrayList<SaveFile> saves = new ArrayList<SaveFile>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.getFolder().toPath(),
				"*" + getExtensionDelimiter() + this.getExtension())) {
			for (Path entry : stream) {
				saves.add(new SaveFile(entry.toFile()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		saves.sort((SaveFile x, SaveFile y) -> {
			return x.compareTo(y);
		});

		return saves;
	}

	/*
	 * public String displaySaves() { ArrayList<SaveFile> saves = null; try { saves
	 * = this.readFolder(); } catch (FileNotFoundException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * String ret = "";
	 * 
	 * if (saves.size() <= 0) { ret += "NO SAVED GAMES YET"; return ret; }
	 * 
	 * for (int num = 0; num < saves.size(); num++) { ret += (num + 1) + ":\t";
	 * 
	 * File file = saves.get(num).getFile(); String fileName = file.getName();
	 * fileName = fileName.substring(0, fileName.length() -
	 * this.extension.length());
	 * 
	 * ret += fileName;
	 * 
	 * ret += "\n"; ret += "\n"; ret += "\n";
	 * 
	 * } return ret; }
	 */

	public void displaySaves(PrintStream printer, long msec) {
		ArrayList<SaveFile> saves = null;
		try {
			saves = this.readFolder();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (saves.size() <= 0) {
			Util.printlnWithSleep(printer, "NO SAVED GAMES YET", msec);
			return;
		}

		for (int num = 0; num < saves.size(); num++) {
			String text = "";

			text += (num + 1) + ":\t";

			text += saves.get(num).toString().replace("\n", "\n\t\t");

			/*
			 * text += "\n"; text += "\n"; text += "\n";
			 */
			Util.printlnWithSleep(printer, text, msec);

			printer.println();

		}
	}

	public boolean saveFileExists(String saveFileName) {
		File saveFile = new File(this.getFolder(), saveFileName + getExtensionDelimiter() + this.getExtension());
		return SaveFile.saveFileExists(saveFile);
	}

	public boolean saveFileExists(File saveFile) {
		return this.saveFileExists(saveFile.toString());
	}

	/*
	 * public File getSaveFile(int saveNum) throws ArrayIndexOutOfBoundsException,
	 * IllegalArgumentException { ArrayList<SaveFile> saves = null; try { saves =
	 * this.readFolder(); } catch (FileNotFoundException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * return saves.get(saveNum).getFile();
	 * 
	 * 
	 * try { return MSGame.fromFile(file); } catch (FileNotFoundException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * // return null; }
	 */

	public SaveFile getSaveFile(int saveNum) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
		ArrayList<SaveFile> saves = null;
		try {
			saves = this.readFolder();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return saves.get(saveNum);
	}

	/*
	 * public void saveGame(MSGame game, String saveFile) { File file = new
	 * File(this.getFolder(), saveFile + this.extension); Util.createFile(file);
	 * game.toFile(file); }
	 */

	// TODO implement this
	public void saveGame(MSGame game, String saveFileName) {
		File file = new File(this.getFolder(), saveFileName + getExtensionDelimiter() + this.getExtension());
		Util.createFile(file);
		SaveFile save = SaveFile.saveGame(game, file);
	}

	public void saveGame(MSGame game, File saveFile) {
		this.saveGame(game, saveFile.toString());
	}

	public boolean deleteFile(int saveNum) {
		try {
			return deleteFile(readFolder().get(saveNum).getFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteFile(File file) {
		if (file.exists()) {
			return file.getAbsoluteFile().delete();
		}
		return false;
	}

	/* private fields */

	private final File folder;
	private final String extension;

	private final static String extDelimiter = ".";

}
