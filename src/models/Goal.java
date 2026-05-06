package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Goal implements Serializable {
	private static final long serialVersionUID = 1L;

	private String title;
	private String description;
	private String type;  // WEEKLY, MONTHLY
	private LocalDate startDate;
	private LocalDate endDate;
	private int targetHours;  // study hours target
	private int completedHours;
	private boolean achieved;

	public Goal() {}

	public Goal(String title, String description, String type,
				LocalDate startDate, LocalDate endDate, int targetHours) {
		this.title = title;
		this.description = description;
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.targetHours = targetHours;
		this.completedHours = 0;
		this.achieved = false;
	}

	// Getters & Setters
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	public LocalDate getStartDate() { return startDate; }
	public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

	public LocalDate getEndDate() { return endDate; }
	public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

	public int getTargetHours() { return targetHours; }
	public void setTargetHours(int targetHours) { this.targetHours = targetHours; }

	public int getCompletedHours() { return completedHours; }
	public void setCompletedHours(int completedHours) {
		this.completedHours = completedHours;
		if (completedHours >= targetHours) this.achieved = true;
	}

	public boolean isAchieved() { return achieved; }
	public void setAchieved(boolean achieved) { this.achieved = achieved; }

	public double getProgress() {
		if (targetHours == 0) return 0;
		return Math.min(100.0, (completedHours * 100.0) / targetHours);
	}

	public void addHours(int hours) {
		this.completedHours += hours;
		if (completedHours >= targetHours) this.achieved = true;
	}

	@Override
	public String toString() {
		return title + " (" + type + ") - " + completedHours + "/" + targetHours + " hrs";
	}
}
