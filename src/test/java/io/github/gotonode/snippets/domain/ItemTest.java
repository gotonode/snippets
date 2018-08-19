package io.github.gotonode.snippets.domain;

import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

	private class NonAbstractItem extends Item {

		NonAbstractItem(int id, Timestamp createTime) {
			super(id, createTime);
		}
	}

	@Test
	public void idWorks() {
		int id = ThreadLocalRandom.current().nextInt();
		NonAbstractItem nonAbstractItem = new NonAbstractItem(id, new Timestamp(0));
		assertEquals(id, nonAbstractItem.getId());
	}

	@Test
	public void toStringWritesSomething() {
		NonAbstractItem nonAbstractItem = new NonAbstractItem(1, new Timestamp(0));
		assertFalse(nonAbstractItem.toString().isEmpty());
	}
}
