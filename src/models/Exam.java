package models;

import java.time.LocalDate;

public class Exam extends Task {
	private static final long serialVersionUID = 1L;

	private String examType;  // Midterm, Final, Quiz
	private String venue;
	private int duration;  // in minutes

	public Exam() { super(); }

	public Exam(String title, String description, LocalDate deadline,
				String priority, String subject, String examType, String venue, int duration) {
		super(title, description, deadline, priority, subject);
		this.examType = examType;
		this.venue = venue;
		this.duration = duration;
	}

	public String getExamType() { return examType; }
	public void setExamType(String examType) { this.examType = examType; }

	public String getVenue() { return venue; }
	public void setVenue(String venue) { this.venue = venue; }

	public int getDuration() { return duration; }
	public void setDuration(int duration) { this.duration = duration; }

	@Override
	public String displayInfo() {
		return "[EXAM] " + super.displayInfo() + " | " + examType + " | Venue: " + venue;
	}
}
