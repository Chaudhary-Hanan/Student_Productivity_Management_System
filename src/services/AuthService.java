package services;

import models.Student;
import java.util.ArrayList;

public class AuthService {

	private static final String USERS_FILE = "users.txt";
	private static Student currentUser = null;

	// Format: username|password|email|studentId|program|semester|points|streak

	public static boolean signup(Student student) {
		if (userExists(student.getUsername())) {
			return false;  // username taken
		}

		String line = student.getUsername() + "|" + student.getPassword() + "|" +
				student.getEmail() + "|" + student.getStudentId() + "|" +
				student.getProgram() + "|" + student.getSemester() + "|" +
				student.getTotalPoints() + "|" + student.getStreakDays();

		FileManager.writeTextLine(USERS_FILE, line);
		return true;
	}

	public static Student login(String username, String password) {
		ArrayList<String> lines = FileManager.readTextLines(USERS_FILE);

		for (String line : lines) {
			String[] parts = line.split("\\|");
			if (parts.length >= 8 && parts[0].equals(username) && parts[1].equals(password)) {
				Student s = new Student(parts[0], parts[1], parts[2],
						parts[3], parts[4], Integer.parseInt(parts[5]));
				s.setTotalPoints(Integer.parseInt(parts[6]));
				s.setStreakDays(Integer.parseInt(parts[7]));
				currentUser = s;
				return s;
			}
		}
		return null;
	}

	public static boolean userExists(String username) {
		ArrayList<String> lines = FileManager.readTextLines(USERS_FILE);
		for (String line : lines) {
			String[] parts = line.split("\\|");
			if (parts.length >= 1 && parts[0].equals(username)) return true;
		}
		return false;
	}

	public static void updateUser(Student student) {
		ArrayList<String> lines = FileManager.readTextLines(USERS_FILE);
		ArrayList<String> updated = new ArrayList<>();

		for (String line : lines) {
			String[] parts = line.split("\\|");
			if (parts.length >= 1 && parts[0].equals(student.getUsername())) {
				String newLine = student.getUsername() + "|" + student.getPassword() + "|" +
						student.getEmail() + "|" + student.getStudentId() + "|" +
						student.getProgram() + "|" + student.getSemester() + "|" +
						student.getTotalPoints() + "|" + student.getStreakDays();
				updated.add(newLine);
			} else {
				updated.add(line);
			}
		}
		FileManager.writeAllTextLines(USERS_FILE, updated);
	}

	public static Student getCurrentUser() { return currentUser; }
	public static void setCurrentUser(Student user) { currentUser = user; }
	public static void logout() { currentUser = null; }
}
