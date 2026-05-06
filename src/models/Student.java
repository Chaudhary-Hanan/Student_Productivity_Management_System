package models;

public class Student extends User {
	private static final long serialVersionUID = 1L;

	private String studentId;
	private String program;
	private int semester;
	private int totalPoints;  // for gamification
	private int streakDays;

	public Student() { super(); }

	public Student(String username, String password, String email,
				   String studentId, String program, int semester) {
		super(username, password, email);
		this.studentId = studentId;
		this.program = program;
		this.semester = semester;
		this.totalPoints = 0;
		this.streakDays = 0;
	}

	// Getters & Setters
	public String getStudentId() { return studentId; }
	public void setStudentId(String studentId) { this.studentId = studentId; }

	public String getProgram() { return program; }
	public void setProgram(String program) { this.program = program; }

	public int getSemester() { return semester; }
	public void setSemester(int semester) { this.semester = semester; }

	public int getTotalPoints() { return totalPoints; }
	public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }

	public int getStreakDays() { return streakDays; }
	public void setStreakDays(int streakDays) { this.streakDays = streakDays; }

	public void addPoints(int points) {
		this.totalPoints += points;
	}

	@Override  // Polymorphism
	public void displayInfo() {
		super.displayInfo();
		System.out.println("Student ID: " + studentId + ", Program: " + program);
	}
}
