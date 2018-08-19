package io.github.gotonode.snippets.controller;

import org.junit.Test;

import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class HistoryItemTest {

	@Test
	public void idWorks() {
		int id = ThreadLocalRandom.current().nextInt();
		HistoryItem historyItem = new HistoryItem(id, new Timestamp(0), "");
		assertEquals(id, historyItem.getId());
	}

	@Test
	public void timestampWorks() {
		int time = ThreadLocalRandom.current().nextInt();
		Timestamp timestamp = new Timestamp(time);
		HistoryItem historyItem = new HistoryItem(1, timestamp, "");
		assertEquals(timestamp, historyItem.getTimestamp());
	}

	@Test
	public void commentWorks() {
		int rnd = ThreadLocalRandom.current().nextInt();
		String comment = ("comment-".concat(String.valueOf(rnd)));
		HistoryItem historyItem = new HistoryItem(1, new Timestamp(0), comment);
		assertEquals(comment, historyItem.getComment());
	}
}