package io.github.gotonode.snippets.domain;

import java.sql.Timestamp;

/**
 * The actual Snippet, when mapped with SnippetDao.
 */
public class Snippet extends Item {

	private final String name;
	private final String language;

	/**
	 * A Snippet, containing the name and language used. This doesn't include the actual code.
	 *
	 * @param id Snippet's ID.
	 * @param createTime The timestamp when this was created.
	 * @param name The Snippet's name.
	 * @param language The language used (might be a different database table in the future).
	 */
	public Snippet(int id, Timestamp createTime, String name, String language) {
		super(id, createTime);
		this.name = name;
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
