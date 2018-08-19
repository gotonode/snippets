package io.github.gotonode.snippets.app;

import _generic.TestBase;
import io.github.gotonode.snippets.controller.Controller;
import io.github.gotonode.snippets.controller.HistoryItem;
import io.github.gotonode.snippets.domain.Snippet;
import javafx.collections.ObservableList;
import org.junit.*;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class ControllerTest {

	private Controller controller;

	@Before
	public void before() {
		TestBase testBase = new TestBase();
		controller = new Controller(testBase.getDatabase());
	}

	@Test
	public void testAllProperties() {
		// Checks that the property values can be fetched properly. Add new property values here.
		String s = controller.getPropertyValue("home");
		assertFalse(s.isEmpty());
		s = controller.getPropertyValue("manual");
		assertFalse(s.isEmpty());
	}

	@Test
	public void testLatestSnippetCode() {
		String code = controller.getLatestSnippetCode(1);
		assertFalse(code.isEmpty());
	}

	@Test
	public void testAllSnippets() {
		List<Snippet> snippets = controller.getAllSnippets();
		assertTrue(snippets.size() > 0);
	}

	@Test
	public void latestSnippetCodeWorks() {
		String code = controller.getLatestSnippetCode(1);
		assertFalse(code.isEmpty());
	}

	@Test
	public void getDatabaseFolderTest() {
		File file = controller.getDatabaseFolder();
		assertNotNull(file);
	}

	@Test
	public void getLatestSnippetCodeTest() {
		String latestCode = controller.getLatestSnippetCode(1);
		assertEquals("TEST CODE B", latestCode);
	}

	@Test
	public void getSnippetCodeByIdTest() {
		String code = controller.getSnippetCodeById(1);
		assertEquals("TEST CODE A", code);
	}

	@Test
	public void getSnippetsInMapTest() {
		Map<String, List<Snippet>> snippetsInMap = controller.getSnippetsInMap();
		assertNotNull(snippetsInMap);
	}

	@Test
	public void getSnippetIdByName() {
		int id = controller.getSnippetIdByName("TEST SNIPPET");
		assertEquals(1, id);
	}

	@Test
	public void findIdByNameWithUnknownName() {
		String s = String.valueOf(ThreadLocalRandom.current().nextInt());
		int id = controller.getSnippetIdByName(s);
		assertEquals(-1, id);
	}

	@Test
	public void getHistoryOfSnippet() {
		ObservableList<HistoryItem> list = controller.getHistoryOfSnippet(1);
		assertEquals(2, list.size());
	}

	@Test
	public void addNewCodeToExistingSnippet() {
		String newCode = "CODE";
		controller.addNewCodeToExistingSnippet(1, newCode, "COMMENT");
		String code = controller.getLatestSnippetCode(1);
		assertEquals(newCode, code);
	}

	@Test
	public void getSnippetIdByNameWithBadValue() {
		String bad = String.valueOf(ThreadLocalRandom.current().nextInt());
		int id = controller.getSnippetIdByName(bad);
		assertEquals(-1, id);
	}

	@Test
	public void deleteById() {
		controller.deleteById(1);
		ObservableList<HistoryItem> list = controller.getHistoryOfSnippet(1);
		assertTrue(list.isEmpty());
	}

	@Test
	public void deleteByLanguage() {
		String language = "MOCK LANGUAGE";
		controller.deleteByLanguage(language);
		List<Snippet> snippets = controller.getAllSnippets();
		for (Snippet snippet : snippets) {
			if (snippet.getLanguage().equals(language)) {
				fail();
			}
		}
		assertTrue(true);
	}

	@Test
	public void nameAlreadyExistsTrue() {
		assertTrue(controller.nameAlreadyExists("TEST SNIPPET"));
	}

	@Test
	public void nameDoesNotAlreadyExistsWithUnknownValue() {
		String unknown = String.valueOf(ThreadLocalRandom.current().nextInt());
		assertFalse(controller.nameAlreadyExists(unknown));
	}

	@Test
	public void addNewSnippet() {
		controller.addNewSnippet("NAME", "LANGUAGE", "CODE", "COMMENT");
		int id = controller.getSnippetIdByName("NAME");
		String code = controller.getLatestSnippetCode(id);
		assertEquals("CODE", code);
	}
}