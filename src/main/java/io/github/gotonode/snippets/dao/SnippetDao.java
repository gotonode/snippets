package io.github.gotonode.snippets.dao;

import io.github.gotonode.snippets.database.Database;
import io.github.gotonode.snippets.domain.Snippet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SnippetDao implements Dao<Snippet, Integer> {

	private final Database database;

	/**
	 * A DAO object model for Snippet(s).
	 *
	 * @param database The connection String.
	 */
	public SnippetDao(Database database) {
		this.database = database;
	}

	/**
	 * Find a Snippet based on the ID.
	 *
	 * @param id Snippet's ID.
	 * @return Returns a Snippet based on its ID; null if none is found.
	 * @throws SQLException This can be thrown.
	 */
	@Override
	public Snippet findOne(Integer id) throws SQLException {
		try (Connection connection = database.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Snippet WHERE id=?");
			preparedStatement.setInt(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Snippet snippet = constructSnippetFromResults(resultSet);

				resultSet.close();

				return snippet;
			}
		}

		return null;
	}

	/**
	 * Finds the ID of the Snippet that has the specific (globally unique) name.
	 *
	 * @param name The name to search for (should be trimmed).
	 * @return The ID if found, -1 otherwise.
	 * @throws SQLException Yes, this can happen.
	 */
	public int findIdByName(String name) throws SQLException {

		try (Connection connection = database.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM Snippet WHERE name=?");
			preparedStatement.setString(1, name);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int id = resultSet.getInt("id");

				resultSet.close();

				return id;
			}
		}

		return -1;
	}

	/**
	 * Returns all Snippets in the database, ordered by name alphabetically.
	 *
	 * @return All Snippets in the database, or an empty List.
	 * @throws SQLException An SQL exception.
	 */
	@Override
	public List<Snippet> findAll() throws SQLException {
		try (Connection connection = database.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Snippet ORDER BY name ASC;");

			ResultSet resultSet = preparedStatement.executeQuery();

			List<Snippet> output = new ArrayList<>();

			while (resultSet.next()) {
				Snippet snippet = constructSnippetFromResults(resultSet);
				output.add(snippet);
			}

			resultSet.close();

			return output;
		}
	}

	/**
	 * Saves a Snippet into the database. This function ignores the Snippet's ID and timestamp.
	 *
	 * @param snippet The newly-created Snippet to be saved.
	 */
	@Override
	public void save(Snippet snippet) throws SQLException {
		try (Connection connection = database.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Snippet (name, language) VALUES (?, ?)");

			preparedStatement.setString(1, snippet.getName());
			preparedStatement.setString(2, snippet.getLanguage());

			preparedStatement.executeUpdate();
		}
	}

	/**
	 * Deletes the Snippet from the database, with the specified ID.
	 *
	 * @param id The ID of the Snippet to be removed.
	 * @throws SQLException If it doesn't work.
	 */
	@Override
	public void delete(Integer id) throws SQLException {
		try (Connection connection = database.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Snippet WHERE id = ?");

			preparedStatement.setInt(1, id);

			preparedStatement.execute();
		}
	}

	private Snippet constructSnippetFromResults(ResultSet resultSet) throws SQLException {
		Snippet snippet = new Snippet(
				resultSet.getInt("id"),
				Database.parseTimestamp(resultSet.getString("createTime")),
				resultSet.getString("name"),
				resultSet.getString("language"));

		return snippet;
	}
}

