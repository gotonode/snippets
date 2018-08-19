package io.github.gotonode.snippets.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * An interface for the DAO's, implemented by SnippetDao and SnippetDataDao.
 *
 * @param <T> Either SnippetDao or SnippetDataDao.
 * @param <Integer> The integer-based key to use with the function.
 */
public interface Dao<T, Integer> {

	/**
	 * Finds one (1) of the specified type.
	 *
	 * @param id The ID of the Snippet or SnippetData that you want.
	 * @return The item  in question; null otherwise.
	 * @throws SQLException Be careful.
	 */
	T findOne(Integer id) throws SQLException;

	/**
	 * Finds all of the specified type.
	 *
	 * @return All of them  or an empty list.
	 * @throws SQLException This can happen.
	 */
	List<T> findAll() throws SQLException;

	/**
	 * Saves the Snippet or SnippetData to the database. This always ignores the item's ID and timestamp.
	 *
	 * @param object Either a Snippet or SnippetData.
	 * @throws SQLException If the item fails to be added, this is thrown.
	 */
	void save(T object) throws SQLException;

	/**
	 * Deletes the specified Snippet or SnippetData. Targets the ID of the Snippet specifically.
	 *
	 * @param key The ID, either "id" in Snippet or "snippet_id" in SnippetData.
	 * @throws SQLException On unsuccessful removal.
	 */
	void delete(Integer key) throws SQLException;
}
