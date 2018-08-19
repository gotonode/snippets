/* This file contains the database structure used by Snippets. */

/* Please DO NOT change the CREATE TABLE statements below. */

CREATE TABLE IF NOT EXISTS "Snippet" (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(64) CHECK (name <> "") UNIQUE NOT NULL,
	/* Ideally, languages would be in a different table, but that would complicate things further. */
	language VARCHAR(16) CHECK (language <> "") NOT NULL,
	createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS "SnippetData" (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	snippet_id INTEGER NOT NULL,
	code VARCHAR(4096) CHECK (code <> "") NOT NULL,
	comment VARCHAR(128) CHECK (comment <> "") NOT NULL,
	createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY ("snippet_id") REFERENCES "Snippet" ("id")
);