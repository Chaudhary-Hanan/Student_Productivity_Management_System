package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	protected String title;
	protected String description;
	protected LocalDate deadline;
	protected String priority;  // HIGH, MEDIUM, LOW
	protected boolean completed;
	protected String subject;

	public Task() {}

	public Task(String title, String description, LocalDate deadline,
				String priority, String subject) {
		this.title = title;
		this.description = description;
		this.deadline = deadline;
		this.priority = priority;
		this.subject = subject;
		this.completed = false;
	}

	// Getters & Setters
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public LocalDate getDeadline() { return deadline; }
	public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

	public String getPriority() { return priority; }
	public void setPriority(String priority) { this.priority = priority; }

	public boolean isCompleted() { return completed; }
	public void setCompleted(boolean completed) { this.completed = completed; }

	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }

	public long getDaysLeft() {
		return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
	}

	// Smart Deadline Alert (escalating)
	public String getAlertLevel() {
		long days = getDaysLeft();
		if (days < 0) return "OVERDUE";
		else if (days == 0) return "URGENT - DUE TODAY";
		else if (days <= 1) return "CRITICAL";
		else if (days <= 3) return "HIGH";
		else if (days <= 7) return "MEDIUM";
		else return "LOW";
	}

	public String displayInfo() {
		return title + " | " + subject + " | Due: " + deadline + " | " + priority;
	}

	@Override
	public String toString() {
		return displayInfo();
	}
}
