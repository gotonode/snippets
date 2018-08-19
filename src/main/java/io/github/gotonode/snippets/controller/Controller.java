package io.github.gotonode.snippets.controller;

import io.github.gotonode.snippets.dao.SnippetDao;
import io.github.gotonode.snippets.dao.SnippetDataDao;
import io.github.gotonode.snippets.database.Database;
import io.github.gotonode.snippets.domain.Snippet;
import io.github.gotonode.snippets.domain.SnippetData;
import io.github.gotonode.snippets.ui.MessageClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**
 * A controller to handle Snippet(s) and SnippetData(s). Mainly used with the JavaFX GUI.
 *
 * SQLExceptions here will show a friendly message via the MessageClass class,
 * and then shut down the app.
 */
public class Controller {

	private SnippetDao snippetDao;
	private SnippetDataDao snippetDataDao;
	private Database database;
	private Properties properties;

	/**
	 * Construct a new Controller.
	 *
	 * @param database Initialize a Database and pass it here (either production or testing).
	 */
	public Controller(Database database) {
		this.database = database;

		snippetDao = new SnippetDao(database);
		snippetDataDao = new SnippetDataDao(database);

		properties = new Properties();

		URL url = ClassLoader.getSystemResource("properties/static.properties");

		try {
			properties.load(url.openStream());
		} catch (IOException e) {
			MessageClass.showError("Could not load the 'static.properties' file.", e);
			System.exit(1);
		}
	}

	/**
	 * Get the property value from 'static.properties' file.
	 *
	 * @param key The key for which you want the value for.
	 * @return Returns the value for the provided key.
	 */
	public String getPropertyValue(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Get the directory the database resides in.
	 *
	 * @return A Java File, which is actually a directory.
	 */
	public File getDatabaseFolder() {
		return new File(database.getDatabaseFile().getParent());
	}

	/**
	 * This will get the latest code (from SnippetData) based on the Snippet's ID.
	 *
	 * @param id The Snippet's ID.
	 * @return Returns the code in a String format.
	 */
	public String getLatestSnippetCode(int id) {

		List<SnippetData> snippetData = new ArrayList<>();

		try {
			snippetData = snippetDataDao.findAllBySnippetId(id);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}

		return snippetData.get(0).getCode();
	}

	/**
	 * Gets the code associated with the SnippetData.
	 *
	 * @param id The SnippetData's ID.
	 * @return A String containing the code.
	 */
	public String getSnippetCodeById(int id) {
		SnippetData snippetData = null;

		try {
			snippetData = snippetDataDao.findOne(id);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}

		assert snippetData != null;
		return snippetData.getCode();
	}

	/**
	 * Get all of the Snippet(s). Not to be confused with SnippetData(s); this doesn't return those.
	 *
	 * @return A List. Easy to iterate through.
	 */
	public List<Snippet> getAllSnippets() {
		List<Snippet> snippets = new ArrayList<>();
		try {
			snippets = snippetDao.findAll();
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}
		return snippets;
	}

	/**
	 * Returns a Map containing the languages as keys and a List of Snippets that have that language.
	 *
	 * @return A Map, to be used with the TreeView item of the GUI.
	 */
	public Map<String, List<Snippet>> getSnippetsInMap() {

		Map<String, List<Snippet>> output = new HashMap<>();

		List<Snippet> snippets = new ArrayList<>();
		try {
			snippets = snippetDao.findAll();
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}

		for (Snippet snippet : snippets) {
			String lang = snippet.getLanguage();

			if (!output.containsKey(lang)) {
				output.put(lang, new ArrayList<>());
			}

			output.get(lang).add(snippet);
		}

		return output;
	}

	/**
	 * Provide a name, and get the ID of a Snippet that has that name.
	 *
	 * @param name The Snippet's name.
	 * @return The Snippet's ID will be returned; -1 if not found.
	 */
	public int getSnippetIdByName(String name) {

		int id = -1;

		try {
			id = snippetDao.findIdByName(name);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}

		return id;
	}

	/**
	 * Returns an ObservableList to be used with the history table.
	 *
	 * @param id The Snippet's ID to gather history data for.
	 * @return An ObservableList containing the history data.
	 */
	public ObservableList<HistoryItem> getHistoryOfSnippet(int id) {

		ObservableList<HistoryItem> output = FXCollections.observableArrayList();

		List<SnippetData> snippetData = new ArrayList<>();

		try {
			snippetData = snippetDataDao.findAllBySnippetId(id);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}

		for (SnippetData sd : snippetData) {
			output.add(new HistoryItem(sd.getId(), sd.getCreateTime(), sd.getComment()));
		}

		return output;
	}

	/**
	 * To a specific Snippet, adds the code and comment given.
	 *
	 * @param snippetId Must reference an existing Snippet.
	 * @param newCode New code to be added into SnippetData.
	 * @param comment The comment describing the changes.
	 */
	public void addNewCodeToExistingSnippet(int snippetId, String newCode, String comment) {

		SnippetData snippetData = new SnippetData(0, null, snippetId, newCode, comment);

		try {
			snippetDataDao.save(snippetData);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}
	}

	/**
	 * Deletes the specified Snippet and all its code and history.
	 *
	 * @param id The ID of the Snippet in question.
	 */
	public void deleteById(int id) {
		try {
			snippetDataDao.delete(id); // First we delete the SnippetData.
			snippetDao.delete(id); // And then the actual Snippet.
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}
	}

	/**
	 * Creates a new Snippet and a SnippetData based on the given parameters, and adds them to the database.
	 *
	 * @param name Name for the Snippet.
	 * @param language The language used.
	 * @param code The actual "data" part of the Snippet.
	 * @param comment A mandatory comment, describing the changes.
	 */
	public void addNewSnippet(String name, String language, String code, String comment) {

		Snippet snippet = new Snippet(0, null, name, language);

		try {
			snippetDao.save(snippet);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}

		int newSnippetId = -1;

		try {
			newSnippetId = snippetDao.findIdByName(name);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}

		SnippetData snippetData = new SnippetData(0, null, newSnippetId, code, comment);

		try {
			snippetDataDao.save(snippetData);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}
	}

	/**
	 * Checks the database if a Snippet with the specified name already exists.
	 *
	 * @param name The name to be checked.
	 * @return True if it exists; false otherwise.
	 */
	public boolean nameAlreadyExists(String name) {

		int id = -1;

		try {
			id = snippetDao.findIdByName(name);
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}

		return id != -1;
	}

	/**
	 * Deletes all SnippetData(s) and Snippet(s) that have the specified language.
	 *
	 * @param lang A String of the language.
	 */
	public void deleteByLanguage(String lang) {

		List<Snippet> snippets;
		try {
			snippets = snippetDao.findAll();

			for (Snippet snippet : snippets) {
				if (snippet.getLanguage().equals(lang)) {
					List<SnippetData> snippetData = snippetDataDao.findAllBySnippetId(snippet.getId());
					for (SnippetData snippetDataItem : snippetData) {
						snippetDataDao.delete(snippetDataItem.getId());
					}
					snippetDao.delete(snippet.getId());
				}
			}
		} catch (SQLException e) {
			showDatabaseErrorAndExit(e);
		}
	}

	/**
	 * Redirects the exception to the MessageClass, shows the message and exits the app.
	 *
	 * @param exception Pass the SQLException here.
	 */
	private void showDatabaseErrorAndExit(SQLException exception) {
		MessageClass.showDatabaseError(exception);
		System.exit(1);
	}
}