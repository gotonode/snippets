package _generic;

import io.github.gotonode.snippets.database.Database;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class TestBase {

	private static final Logger LOG = Logger.getLogger(TestBase.class.getName());

	@Rule
	public static TemporaryFolder tempFolder;
	private static File databaseFile;
	private Database database;

	public TestBase() {

		if (tempFolder == null) {
			try {
				tempFolder = new TemporaryFolder();
				tempFolder.create();
			} catch (IOException e) {
				LOG.severe("Failed to create the needed temporary folder.");
				System.exit(1);
			} finally {
				LOG.info("Temporary folder created: '" + tempFolder.getRoot() + "'");
			}
		}

		if (databaseFile == null) {
			databaseFile = new File(tempFolder.getRoot() + "/snippets_TEST.db");
			LOG.info("Database assigned: '" + databaseFile.getAbsolutePath() + "'");
		} else if (databaseFile.exists()) {
			databaseFile.delete();
		}

		database = new Database(databaseFile);

		database.insertTestData();
	}

	public Database getDatabase() {
		return database;
	}
}
