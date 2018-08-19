package io.github.gotonode.snippets.domain;

import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import static org.junit.Assert.*;

public class SnippetTest {

	// No need to test for ID and timestamp, those tests are handled by ItemTest.

	@Test
	public void timestampWorks() {
		int time = ThreadLocalRandom.current().nextInt();
		Timestamp timestamp = new Timestamp(time);
		Snippet snippet = new Snippet(1, timestamp, "", "");
		assertEquals(timestamp, snippet.getCreateTime());
	}

	@Test
	public void toStringWritesSomething() {
		Snippet snippet = new Snippet(1, new Timestamp(0), "", "");
		assertFalse(snippet.toString().isEmpty());
	}

	@Test
	public void languageWorks() {
		int rnd = ThreadLocalRandom.current().nextInt();
		String mockLanguage = ("language-").concat(String.valueOf(rnd));
		Snippet snippet = new Snippet(1, new Timestamp(0), "", mockLanguage);
		assertEquals(mockLanguage, snippet.getLanguage());
	}

	@Test
	public void nameWorks() {
		int rnd = ThreadLocalRandom.current().nextInt();
		String mockName = ("name-").concat(String.valueOf(rnd));
		Snippet snippet = new Snippet(1, new Timestamp(0), mockName, "");
		assertEquals(mockName, snippet.getName());
	}
}
