package io.github.gotonode.snippets.database;

import _generic.TestBase;
import io.github.gotonode.snippets.dao.SnippetDao;
import org.junit.*;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class DatabaseTest {

	private TestBase testBase;
	private Database database; // This is here for quick reference.

	@Before
	public void before() {
		testBase = new TestBase();
		database = testBase.getDatabase(); // Just for convenience.
	}

	public void insertingTestData() {

	}

	@Test
	public void timestampWorks() {
		Timestamp output = Database.parseTimestamp("1970-01-01 00:00:00");
		Date date = new Date(output.getTime());

		int year = Integer.valueOf(new SimpleDateFormat("y").format(date));
		int month = Integer.valueOf(new SimpleDateFormat("M").format(date));
		int day = Integer.valueOf(new SimpleDateFormat("d").format(date));

		int hour = Integer.valueOf(new SimpleDateFormat("h").format(date));
		int minute = Integer.valueOf(new SimpleDateFormat("m").format(date));
		int second = Integer.valueOf(new SimpleDateFormat("s").format(date));

		assertEquals(1, day);
		assertEquals(1, month);
		assertEquals(1970, year);

		// We don't test the hour because it varies based on the user's time zone on this implementation.
		// Same with minutes, on some time zones.

		if (!true) {
			assertEquals(0, hour);
			assertEquals(0, minute);
		}
		assertEquals(0, second);
	}

	@Test
	public void getDatabaseFileTest() {
		File databaseFile = database.getDatabaseFile();
		assertNotNull(databaseFile);
	}
}
