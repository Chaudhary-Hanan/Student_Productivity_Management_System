package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	private String title;
	private String content;
	private String subject;
	private ArrayList<String> tags;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Note() {
		this.tags = new ArrayList<>();
	}

	public Note(String title, String content, String subject) {
		this.title = title;
		this.content = content;
		this.subject = subject;
		this.tags = new ArrayList<>();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Note(String title, String content, String subject, ArrayList<String> tags) {
		this(title, content, subject);
		this.tags = tags;
	}

	// Getters & Setters
	public String getTitle() { return title; }
	public void setTitle(String title) {
		this.title = title;
		this.updatedAt = LocalDateTime.now();
	}

	public String getContent() { return content; }
	public void setContent(String content) {
		this.content = content;
		this.updatedAt = LocalDateTime.now();
	}

	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }

	public ArrayList<String> getTags() { return tags; }
	public void setTags(ArrayList<String> tags) { this.tags = tags; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

	public void addTag(String tag) {
		if (!tags.contains(tag)) tags.add(tag);
	}

	public void removeTag(String tag) {
		tags.remove(tag);
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public String getTagsAsString() {
		return String.join(", ", tags);
	}

	@Override
	public String toString() {
		return title + " [" + subject + "] - Tags: " + getTagsAsString();
	}
}
