package io.github.gotonode.snippets.controller;

import java.sql.Timestamp;

/**
 * This class is used to house items placed on the history table.
 */
public class HistoryItem {
	private final int id;
	private final Timestamp timestamp;
	private final String comment;

	/**
	 * Construct a new HistoryItem.
	 *
	 * @param id The ID of the SnippetData.
	 * @param timestamp When it was created (saved to the database).
	 * @param comment A comment describing the changes made.
	 */
	public HistoryItem(int id, Timestamp timestamp, String comment) {
		this.id = id;
		this.timestamp = timestamp;
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String getComment() {
		return comment;
	}
}