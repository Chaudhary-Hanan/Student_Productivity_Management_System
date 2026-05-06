package models;

import java.time.LocalDate;

public class Assignment extends Task {
	private static final long serialVersionUID = 1L;

	private int totalMarks;
	private String submissionType;  // Online, Physical

	public Assignment() { super(); }

	public Assignment(String title, String description, LocalDate deadline,
					  String priority, String subject, int totalMarks, String submissionType) {
		super(title, description, deadline, priority, subject);
		this.totalMarks = totalMarks;
		this.submissionType = submissionType;
	}

	public int getTotalMarks() { return totalMarks; }
	public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }

	public String getSubmissionType() { return submissionType; }
	public void setSubmissionType(String submissionType) { this.submissionType = submissionType; }

	@Override  // Polymorphism
	public String displayInfo() {
		return "[ASSIGNMENT] " + super.displayInfo() + " | Marks: " + totalMarks;
	}
}
