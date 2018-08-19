package io.github.gotonode.snippets.domain;

import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Before;

public class SnippetDataTest {

	// No need to test for ID and timestamp, those tests are handled by ItemTest.

	SnippetData snippetData;

	@Before
	public void before() {
		snippetData = new SnippetData(1, new Timestamp(0), 0, "", "");
	}

	@Test
	public void toStringWritesSomething() {
		assertFalse(snippetData.toString().isEmpty());
	}

	@Test
	public void codeWorks() {
		int rnd = ThreadLocalRandom.current().nextInt();
		String code = ("code-".concat(String.valueOf(rnd)));
		snippetData = new SnippetData(1, new Timestamp(0), 0, code, "");
		assertEquals(code, snippetData.getCode());
	}

	@Test
	public void commentWorks() {
		int rnd = ThreadLocalRandom.current().nextInt();
		String comment = ("comment-".concat(String.valueOf(rnd)));
		snippetData = new SnippetData(1, new Timestamp(0), 0, "", comment);
		assertEquals(comment, snippetData.getComment());
	}

	@Test
	public void snippetIdWorks() {
		int id = ThreadLocalRandom.current().nextInt();
		snippetData = new SnippetData(1, new Timestamp(0), id, "", "");
		assertEquals(id, snippetData.getSnippetId());
	}
}
