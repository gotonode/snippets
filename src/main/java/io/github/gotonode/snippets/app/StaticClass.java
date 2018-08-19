package io.github.gotonode.snippets.app;

import io.github.gotonode.snippets.controller.Controller;
import io.github.gotonode.snippets.database.Database;

/**
 * A static class for handling the Controller.
 */
public class StaticClass {

	public static Controller controller;

	/**
	 * Creates the static Controller that is usable by the GUI, using a pre-created Database.
	 *
	 * @param database The database to be used (production or testing).
	 */
	public static void createController(Database database) {
		controller = new Controller(database);
	}
}
