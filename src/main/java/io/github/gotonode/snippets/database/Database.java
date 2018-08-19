package io.github.gotonode.snippets.database;

import io.github.gotonode.snippets.ui.MessageClass;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * This class handles all the database management, but not DAO queries.
 */
public class Database {

	private final String databaseAddress;
	private final File databaseFile;

	/**
	 * Initialize this class with the database file that is given.
	 *
	 * @param file The file that houses the database.
	 */
	public Database(File file) {
		this.databaseFile = file;
		this.databaseAddress = ("jdbc:sqlite:").concat(databaseFile.getAbsolutePath());

		if (!this.databaseFile.exists()) {
			createTables();
		}
	}

	/**
	 * Gets the file the database resides in.
	 *
	 * @return Returns a Java File that points to the database.
	 */
	public File getDatabaseFile() {
		return databaseFile;
	}

	/**
	 * Get a connection.
	 *
	 * @return Get a new connection, which you should close when done.
	 * @throws SQLException Beware!
	 */
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(databaseAddress);
	}

	/**
	 * Formats SQLite's timestamp into something that Java can understand.
	 *
	 * @param timestampString SQLite's version of a timestamp.
	 * @return Java's own Timestamp.
	 */
	public static Timestamp parseTimestamp(String timestampString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");

		Date date = null;

		try {
			date = simpleDateFormat.parse(timestampString);
		} catch (ParseException e) {
			MessageClass.showError("Could not parse timestamp string.", e);
			System.exit(1);
		}

		return new Timestamp(date.getTime());
	}

	/**
	 * Inserts test data. Only used for testing this database.
	 */
	public void insertTestData() {
		try {
			executeTransaction(loadStreamData("/sql/tests.sql"));
		} catch (SQLException e) {
			MessageClass.showError("Could not load SQL test data.", e);
			System.exit(1);
		}
	}

	private void createTables() {
		try {
			executeTransaction(loadStreamData("/sql/structure.sql"));
		} catch (SQLException e) {
			MessageClass.showError("Could not load SQL's CREATE TABLE statements.", e);
			System.exit(1);
		}
	}

	private String loadStreamData(String internalPath) {
		InputStream inputStream = getClass().getResourceAsStream(internalPath);
		return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\r\n"));
	}

	private void executeTransaction(String sql) throws SQLException {

		try (Connection connection = getConnection()) {

			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);

			StringBuilder stringBuilder = new StringBuilder();

			for (String s : sql.split("\r\n")) {
				if (s.trim().equals(("")) || s.trim().startsWith("/*")) {
					continue;
				}
				stringBuilder.append(s);

				if (s.endsWith(";")) {
					statement.addBatch(stringBuilder.toString());
					stringBuilder = new StringBuilder();
				}
			}

			statement.executeBatch();
			connection.commit();

			statement.close();
		}
	}
}
