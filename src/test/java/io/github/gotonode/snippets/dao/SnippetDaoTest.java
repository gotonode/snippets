package io.github.gotonode.snippets.dao;

import _generic.TestBase;
import io.github.gotonode.snippets.domain.Snippet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class SnippetDaoTest {

	private TestBase testBase;
	private SnippetDao snippetDao;

	@Before
	public void before() {
		testBase = new TestBase();
		snippetDao = new SnippetDao(testBase.getDatabase());
	}

	@Test
	public void findOneReturnsOne() throws Exception {
		Snippet snippet = snippetDao.findOne(1);
		assertNotNull(snippet);
	}

	@Test
	public void findOneReturnsNullOnBadId() throws Exception {
		Snippet snippet = snippetDao.findOne(-1);
		assertNull(snippet);
	}

	@Test
	public void createTimeWorks() throws Exception {
		Snippet snippet = snippetDao.findOne(1);
		Timestamp timestamp = snippet.getCreateTime();
		assertNotNull(timestamp);
	}

	@Test
	public void nameReturnsSomething() throws Exception {
		Snippet snippet = snippetDao.findOne(1);
		assertFalse(snippet.getName().isEmpty());
	}

	@Test
	public void languageReturnsSomething() throws Exception {
		Snippet snippet = snippetDao.findOne(1);
		assertFalse(snippet.getLanguage().isEmpty());
	}

	@Test
	public void findIdByNameTest() throws SQLException {
		int id = snippetDao.findIdByName("TEST SNIPPET");
		assertEquals(1, id);
	}

	@Test
	public void newSnippetInsertsCorrectly() throws SQLException {

		String testName = "NAME";
		String testLanguage = "LANGUAGE";

		Snippet newSnippet = new Snippet(Integer.MAX_VALUE, new Timestamp(0), testName, testLanguage);

		snippetDao.save(newSnippet);

		int id = snippetDao.findIdByName(testName);
		Snippet snippet = snippetDao.findOne(id);

		String name = snippet.getName();
		String language = snippet.getLanguage();

		assertEquals(testName, name);
		assertEquals(testLanguage, language);
	}

	@Test
	public void findIdByNameWithUnknownValue() throws SQLException {
		String name = "BAD-" + ThreadLocalRandom.current().nextInt();
		int id = snippetDao.findIdByName(name);
		assertEquals(-1, id);
	}

	@Test
	public void findAllTest() throws SQLException {
		List<Snippet> snippets = snippetDao.findAll();
		assertEquals(1, snippets.size());
	}

	@Test
	public void deleteTest() throws SQLException {
		snippetDao.delete(1);
		Assert.assertEquals(0, snippetDao.findAll().size());
	}
}
