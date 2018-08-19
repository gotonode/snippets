package io.github.gotonode.snippets.domain;

import java.sql.Timestamp;

/**
 * An abstract class that both Snippet and SnippetData extend from.
 * This class cannot be used directly, and is always used through some other class.
 */
public abstract class Item {

	private final int id;
	private final Timestamp createTime;

	/**
	 * An abstract class, used by Snippet and SnippetData.
	 *
	 * @param id Either Snippet's ID or SnippetData's ID.
	 * @param createTime The time of creation, a timestamp.
	 */
	public Item(int id, Timestamp createTime) {
		this.id = id;
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	@Override
	public String toString() {
		return "Item{" + "id=" + id + ", createTime=" + createTime + '}';
	}
}
