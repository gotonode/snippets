package io.github.gotonode.snippets.domain;

import java.sql.Timestamp;

/**
 * This class holds mapped SnippetData from SnippetDao.
 */
public class SnippetData extends Item {

	private final int snippetId;
	private final String code;
	private final String comment;

	/**
	 * SnippetData holds the actual data, like the code and the optional
	 * comment.
	 *
	 * @param id The SnippetData's ID.
	 * @param createTime When it was created, a Timestamp.
	 * @param snippetId The Snippet's ID this SnippetData associates with.
	 * @param code The actual code.
	 * @param comment And an optional comment (like used with Git).
	 */
	public SnippetData(int id, Timestamp createTime, int snippetId, String code, String comment) {
		super(id, createTime);
		this.snippetId = snippetId;
		this.code = code;
		this.comment = comment;
	}

	public String getCode() {
		return code;
	}

	public String getComment() {
		return comment;
	}

	public int getSnippetId() {
		return snippetId;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
