# Architecture of Snippets

The packages in Snippets have been divided into the following:

- `io.github.gotonode.snippets.app`
- `io.github.gotonode.snippets.ui`
- `io.github.gotonode.snippets.controller`
- `io.github.gotonode.snippets.database`
- `io.github.gotonode.snippets.dao`

In this document we'll go through what classes are housed in each.

## Package diagram

Here you'll find a visual representation of the package structure (in UML form).

![UML diagram](https://github.com/gotonode/snippets/blob/master/docs/images/architecture.png)

## Logic

Snippets has been divided into many packages, each handling a different task.

In "domain" (package), there are three classes: Item (an abstract class), Snippet and SnippetData (who both extend the abstract class).

Snippet stores the name, ID, creation time and the used language of the the specific Snippet.

SnippetData stores entries that always link to a Snippet. SnippetData has the actual code, and each entry also has a comment describing it. A timestamp is also associated with each entry.

With Snippet and SnippetData classes, a Snippet with all of its history can be loaded. If only the recent version of the code is requested, it can be found by looking at all the SnippetData associated with a specific Snippet, and sorting those entries by their creation time such that the newest is first, and then choosing the first item from that list.

The Data Access Object model is in use. An interface class, Dao, is extended by two classes, SnippetDao and SnippetDataDao.

SnippetDao maps the relation between Snippet (a class) and their representation in the database. SnippetDataDao does the same with SnippetData.

The DAO classes work as abstractions between their respective classes and the database.

SnippetDao can be queried with method findOne(int id) to get a Snippet item back. It constructs the Snippet with data from the database. Same with SnippetDataDao and SnippetData. Saving a new Snippet (or SnippetData) works by creating them in code first, and then passing them as arguments to their respective DAO's save -function. Their data is then read and added into the database with INSERT commands.

The required external library (for Maven) is SQLite. It is downloaded and updated via Maven and the version is defined in "pom.xml".

## Example sequence diagram

This is an example sequence diagram when the GUI has been loaded and the user double-clicks on a Snippet (its name) to open it up. All class/method calls are documented.

![Sequence diagram](https://github.com/gotonode/snippets/blob/master/docs/images/sequence_diagram.png)

So when the user wants to load a Snippet, he/she double-clicks on the Snippet's name and then the full history of the Snippet is loaded into the TableView and the most recent one is chosen. Then the most recent version of the code is displayed in the TextArea.

It's worth mentioning that the code is loaded into the TextArea via an event handler attached to the TableView. So only when the TableView's selected item changes is the code loaded into the TextArea. This way no additional code is necessary, since when a Snippet is being loaded, it first populates the TableView, and then chooses the topmost item, triggering the event handler and causing the TextArea to display the most recent code. This is illustrated in the sequence diagram.

## User interface

The user interface in Snippets has been written entirely in JavaFX (an XML dialect) and Java. Segregation of the logic and the components responsible for GUI manipulation is a key issue.

In the "ui" package is a single class called FxmlController. The only FXML element, "Main.fxml", is used with that class.

FxmlController talks only with Controller, a class used to abstract away as much as possible from the DAO's (SnippetDao and SnippetDataDao). FxmlController could've directly communicated with the DAO's, but that would've only strengthened the coupling between the logic and the UI.

For an example, where SnippetDao has a public method of findAll, Controller has a method of getSnippetsInMap. The method getSnippetsInMap uses the findAll method within SnippetDao.

The UI controller class, FxmlController, doesn't need to know anything about SnippetDao and SnippetDataDao.

There's only one Java Scene used in Snippets. Alerts, information and confirmation dialogs are created with native JavaFX calls and are created dynamically.

When changes are made via the GUI, the necessary parts are invalidated and redrawn. Prior to that, the data in question is reloaded completely from the database (this is a consistency issue).

The UI (FXML) as well as the controller code for it are exempt from tests and code coverage. UI testing has been done manually.

Here's a preview of the GUI in Snippets:

![Snippets running](https://github.com/gotonode/snippets/blob/master/docs/images/app_01.png)

The GUI is formed with a VBox at the base. On the top resides a MenuBar. Below the MenuBar, and filling the remaining available space is a SplitPane (horizontal).

On the left of the SplitPane is a TreeView, listing all the languages and the Snippets associated with those languages.

On the right, another SplitPane (vertical). The top part of this second SplitPane houses the TableView which displays the history of the currently selected Snippet.

And on the bottom part of the secondary SplitPane is the actual code editor, a TextArea. Below the TextArea is a ToolBar, containing a TextField for entering a comment and three Buttons ('Save', 'Save As' and 'Revert').

Each major element is stored inside an AnchorPane. This ensures that the elements stay attached to their designated corners when the size of the window is changed. In total, there's at least 6 AnchorPane(s).

## Database

Snippets uses a SQLite database to store all the data. When the app is first launched, the database is created (if it doesn't already exist).

The database is located in the user's home directory. This is OS -dependent, and Java finds this directory automatically. For Windows, the database is located in:

```text
C:\Users\gotonode\snippets.db
```
Replace "gotonode" with your own username.

Please feel free to remove this database at any time. You can also browse its contents with a GUI/non-GUI SQLite browser.

Here are the CREATE TABLE statements for the database:

```sqlite
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
```

Here's the database schema:

![Database schema](https://github.com/gotonode/snippets/blob/master/docs/images/schema.png)

Please note that the exclusion of the "Language" table was a deliberate choice (for simplicity).

## Configuration

Snippets can be manually configured to use a user-specified database.

To use this feature, create an empty file at the root of the JAR -file, and name it "config.properties".

Into that file, write the following:

```text
database=C:\path_to_database\databaseFile.db
```

When Snippets is started, it checks if this file exists, and if it does, reads the database path string from it. It'll then use this path instead of the built-in one.

This feature is especially useful for those who want to share their Snippets (database) among all users of the computer system.

## Architectural shortcomings

These are current issues in Snippets and might be fixed in the future.

The TreeView on the GUI (which lists all the languages and Snippets within that language) doesn't expand properly when certain actions are taken, such as deleting a Snippet or creating a new Snippet. One way to fix this is to create a list of all the expanded nodes before the TreeView's data is recreated, then compare that list with the newly-loaded nodes and expand those that were expanded in the first place.

When the user has written changes to the code, and attempts to navigate away from their changes, the app doesn't prompt the user to save their changes first. It just loads the new content in, replacing the user's code.

Renaming the tests to better describe what they are doing. Test name "toStringWorks" isn't very good.

The enforced Checkstyle rules mandate that no file has a line length greater than 200. Currently, the "controller/Controller" class is about 300 lines.

Refactoring the FxmlController. A lot of lines can certainly be condensed.

Spelling and/or grammatical errors, if any, should be fixed.

The language column in Snippet should, in reality, be in a different table altogether. But for simplicity's sake the language is directly included in each entry. Creating a "Languages" table would help normalize the database but would also increase the need for database queries.

During the mandatory code review part of this course, my code reviewer discovered a bug. She was using NetBeans to run Snippets, and I'm using IntelliJ IDEA. One of the included plugins in "pom.xml" didn't like NetBeans. So I added comments on both the XML file in question as well as the root README.me file of Snippets to indicate this. Of course, writing code that is IDE-independent is good, but this course's deadline is approaching fast so I'll figure out a better solution later.
