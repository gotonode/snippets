/* This file contains TEST data for Snippets. Only for Java tests. */

/* Please DO NOT change the values here. */

INSERT INTO Snippet (createTime, name, language) VALUES
	("1970-01-01 00:00:00", "TEST SNIPPET", "MOCK LANGUAGE");

INSERT INTO SnippetData (snippet_id, code, comment, createTime) VALUES
	(1, "TEST CODE A", "COMMENT A", "1970-01-01 00:00:00"),
	(1, "TEST CODE B", "COMMENT B", "1970-01-01 00:00:01");