package io.github.gotonode.snippets.dao;

import io.github.gotonode.snippets.database.Database;
import io.github.gotonode.snippets.domain.SnippetData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SnippetDataDao implements Dao<SnippetData, Integer> {

	private final Database database;

	/**
	 * A DAO object for SnippetData. It basically "maps" SQLite to Java.
	 *
	 * @param database The connection String.
	 */
	public SnippetDataDao(Database database) {
		this.database = database;
	}

	/**
	 * Find one SnippetData (which contains the Snippet's code and possibly a comment).
	 *
	 * @param id SnippetData's ID.
	 * @return A single SnippetData, null if none is found.
	 * @throws SQLException An SQL exception.
	 */
	@Override
	public SnippetData findOne(Integer id) throws SQLException {
		try (Connection connection = database.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SnippetData WHERE id=?");
			preparedStatement.setInt(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				SnippetData snippetData = constructSnippetDataFromResults(resultSet);

				resultSet.close();

				return snippetData;
			}
		}

		return null;
	}

	/**
	 * Get all the SnippetData from the database.
	 *
	 * @return A List of SnippetData, or an empty List.
	 * @throws SQLException Be cautious.
	 */
	@Override
	public List<SnippetData> findAll() throws SQLException {
		try (Connection connection = database.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SnippetData");

			ResultSet resultSet = preparedStatement.executeQuery();

			List<SnippetData> output = new ArrayList<>();

			while (resultSet.next()) {
				SnippetData snippetData = constructSnippetDataFromResults(resultSet);
				output.add(snippetData);
			}

			resultSet.close();

			return output;
		}
	}

	/**
	 * Finds all SnippetData that belongs to a specific Snippet. Latest Snippet is first.
	 *
	 * @param id Snippet's ID.
	 * @return Returns all the SnippetData.
	 * @throws SQLException Beware of this.
	 */
	public List<SnippetData> findAllBySnippetId(int id) throws SQLException {
		try (Connection connection = database.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SnippetData WHERE snippet_id = ? ORDER BY createTime DESC");
			preparedStatement.setInt(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();

			List<SnippetData> output = new ArrayList<>();

			while (resultSet.next()) {
				SnippetData snippetData = constructSnippetDataFromResults(resultSet);
				output.add(snippetData);
			}

			resultSet.close();

			return output;
		}
	}

	/**
	 * Saves a SnippetData into the database. This function ignores the SnippetData's own ID and timestamp.
	 *
	 * @param snippetData The newly-created SnippetData to be saved.
	 */
	@Override
	public void save(SnippetData snippetData) throws SQLException {
		try (Connection connection = database.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO SnippetData (snippet_id, code, comment) VALUES (?, ?, ?)");

			preparedStatement.setInt(1, snippetData.getSnippetId());
			preparedStatement.setString(2, snippetData.getCode());
			preparedStatement.setString(3, snippetData.getComment());

			preparedStatement.executeUpdate();
		}
	}

	/**
	 * Deletes the SnippetData from the database, that belongs to the specified Snippet ID.
	 *
	 * @param id The ID of the Snippet to remove SnippetData from.
	 * @throws SQLException If it doesn't work.
	 */
	@Override
	public void delete(Integer id) throws SQLException {
		try (Connection connection = database.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM SnippetData WHERE snippet_id = ?");

			preparedStatement.setInt(1, id);

			preparedStatement.execute();
		}
	}

	private SnippetData constructSnippetDataFromResults(ResultSet resultSet) throws SQLException {
		SnippetData snippetData = new SnippetData(
				resultSet.getInt("id"),
				Database.parseTimestamp(resultSet.getString("createTime")),
				resultSet.getInt("snippet_id"),
				resultSet.getString("code"),
				resultSet.getString("comment")
		);

		return snippetData;
	}
}
