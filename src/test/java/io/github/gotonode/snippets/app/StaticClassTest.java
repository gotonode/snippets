package io.github.gotonode.snippets.app;

import io.github.gotonode.snippets.database.Database;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class StaticClassTest {

	@Test
	public void createController() {
		File file = new File(System.getProperty("user.home") + "/snippets.db");

		Database database = new Database(file);

		StaticClass.createController(database);

		assertNotNull(StaticClass.controller);
	}
}