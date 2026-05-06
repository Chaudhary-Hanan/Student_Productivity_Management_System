package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudySession implements Serializable {
	private static final long serialVersionUID = 1L;

	private String subject;
	private LocalDate date;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int durationMinutes;
	private boolean completed;
	private String notes;

	public StudySession() {}

	public StudySession(String subject, int durationMinutes) {
		this.subject = subject;
		this.durationMinutes = durationMinutes;
		this.date = LocalDate.now();
		this.startTime = LocalDateTime.now();
		this.completed = false;
	}

	// Getters & Setters
	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }

	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }

	public LocalDateTime getStartTime() { return startTime; }
	public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

	public LocalDateTime getEndTime() { return endTime; }
	public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

	public int getDurationMinutes() { return durationMinutes; }
	public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

	public boolean isCompleted() { return completed; }
	public void setCompleted(boolean completed) {
		this.completed = completed;
		if (completed) this.endTime = LocalDateTime.now();
	}

	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }

	@Override
	public String toString() {
		return date + " | " + subject + " | " + durationMinutes + " min | " +
				(completed ? "✓" : "✗");
	}
}
