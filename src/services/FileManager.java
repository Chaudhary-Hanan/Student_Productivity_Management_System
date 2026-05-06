package services;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

	private static final String DATA_FOLDER = "data/";

	// Ensure data folder exists
	static {
		File folder = new File(DATA_FOLDER);
		if (!folder.exists()) folder.mkdir();
	}

	// ===== SERIALIZATION (for object lists) =====

	public static <T> void saveObjects(ArrayList<T> list, String filename) {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(DATA_FOLDER + filename))) {
			oos.writeObject(list);
		} catch (IOException e) {
			System.err.println("Error saving " + filename + ": " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> loadObjects(String filename) {
		File file = new File(DATA_FOLDER + filename);
		if (!file.exists()) return new ArrayList<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			return (ArrayList<T>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error loading " + filename + ": " + e.getMessage());
			return new ArrayList<>();
		}
	}

	// ===== TEXT FILES (for users — readable format) =====

	public static void writeTextLine(String filename, String line) {
		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter(DATA_FOLDER + filename, true))) {
			bw.write(line);
			bw.newLine();
		} catch (IOException e) {
			System.err.println("Error writing to " + filename + ": " + e.getMessage());
		}
	}

	public static ArrayList<String> readTextLines(String filename) {
		ArrayList<String> lines = new ArrayList<>();
		File file = new File(DATA_FOLDER + filename);
		if (!file.exists()) return lines;

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.trim().isEmpty()) lines.add(line);
			}
		} catch (IOException e) {
			System.err.println("Error reading " + filename + ": " + e.getMessage());
		}
		return lines;
	}

	public static void writeAllTextLines(String filename, ArrayList<String> lines) {
		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter(DATA_FOLDER + filename, false))) {
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			System.err.println("Error writing to " + filename + ": " + e.getMessage());
		}
	}
}
