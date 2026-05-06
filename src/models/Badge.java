package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Badge implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String icon;  // emoji or text symbol
	private LocalDate earnedDate;
	private int pointsAwarded;

	public Badge() {}

	public Badge(String name, String description, String icon, int pointsAwarded) {
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.pointsAwarded = pointsAwarded;
		this.earnedDate = LocalDate.now();
	}

	// Getters & Setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getIcon() { return icon; }
	public void setIcon(String icon) { this.icon = icon; }

	public LocalDate getEarnedDate() { return earnedDate; }
	public void setEarnedDate(LocalDate earnedDate) { this.earnedDate = earnedDate; }

	public int getPointsAwarded() { return pointsAwarded; }
	public void setPointsAwarded(int pointsAwarded) { this.pointsAwarded = pointsAwarded; }

	@Override
	public String toString() {
		return icon + " " + name + " - " + description;
	}
}
