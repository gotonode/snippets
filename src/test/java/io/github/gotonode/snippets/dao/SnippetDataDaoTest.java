package io.github.gotonode.snippets.dao;

import _generic.TestBase;
import io.github.gotonode.snippets.domain.SnippetData;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

import org.junit.Before;

public class SnippetDataDaoTest {

	TestBase testBase;
	SnippetDataDao snippetDataDao;

	@Before
	public void before() {
		testBase = new TestBase();
		snippetDataDao = new SnippetDataDao(testBase.getDatabase());
	}

	@Test
	public void findOneReturnsOne() throws Exception {
		SnippetData snippetData = snippetDataDao.findOne(1);
		assertNotNull(snippetData);
	}

	@Test
	public void findOneReturnsNullOnBadId() throws Exception {
		SnippetData snippetData = snippetDataDao.findOne(-1);
		assertNull(snippetData);
	}

	@Test
	public void snippetIdReturnsPositiveInteger() throws Exception {
		SnippetData snippetData = snippetDataDao.findOne(1);
		assertTrue(snippetData.getSnippetId() > 0);
	}

	@Test
	public void codeReturnsSomething() throws Exception {
		SnippetData snippetData = snippetDataDao.findOne(1);
		assertFalse(snippetData.getCode().isEmpty());
	}

	@Test
	public void commentReturnsSomethingOrNull() throws Exception {
		SnippetData snippetData = snippetDataDao.findOne(1);
		String comment = snippetData.getComment();
		assertTrue(true); // If the previous line doesn't cause an error, this test passes.
	}

	@Test
	public void createTimeWorks() throws Exception {
		SnippetData snippetData = snippetDataDao.findOne(1);
		Timestamp timestamp = snippetData.getCreateTime();
		assertNotNull(timestamp);
	}

	@Test
	public void findAllFindsAll() throws SQLException {
		List<SnippetData> snippetData = snippetDataDao.findAll();
		assertFalse(snippetData.isEmpty());
	}

	@Test
	public void newSnippetDataInsertsCorrectly() throws SQLException {

		String testCode = "CODE";
		String testComment = "COMMENT";

		SnippetData newSnippetData = new SnippetData(Integer.MAX_VALUE, new Timestamp(0), 1, testCode, testComment);
		snippetDataDao.save(newSnippetData);
		List<SnippetData> snippetData = snippetDataDao.findAll();

		SnippetData lastSnippetData = snippetData.get(snippetData.size() - 1);

		String code = lastSnippetData.getCode();
		String comment = lastSnippetData.getComment();

		assertEquals(testCode, code);
		assertEquals(testComment, comment);
	}

	@Test
	public void deleteTest() throws SQLException {
		snippetDataDao.delete(1);
		assertEquals(0, snippetDataDao.findAll().size());
	}
}
