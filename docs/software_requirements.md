# Software requirements specification

This document contains the software requirements specifications for the Snippets Java app.

The Snippets app is a simple code snippet manager. You can add your own Snippets of code into it, edit your previous Snippets and remove unwanted Snippets. From this point forth, all snippets in the app are referred to with a capital 'S'.

What's special about this app is that it stores all the revisions of a specific code Snippet into its database. This means that if you update a Snippet, the older version will remain in the database and is still accessible. In a sense, it works like Git's history. However, the scope of this app doesn't include any "diff" features, i.e. features that allow you to see what has changed between any two given versions of a Snippet.

## Users

This app only has one user type (regular user), but each user will get their own database on the operating system.

## Data storage

Snippets uses an SQLite database. The database is located in the root of the user's home directory, in Windows this is `C:\Users\gotonode\snippets.db`. The home directory is detected and set automatically by Java, depending on the OS. It will also be possible to manually specify the path to the database, via the `config.properties` configuration file.

## Functions

Snippets lists all stored Snippets based on their language. In reality, the language column should've been a table of its own, but for simplicity, it is directly added to the specific Snippet. The user first chooses the language, (ex. Java) and then chooses a specific Java Snippet to be viewed, updated or deleted (full CRUD is available here). When saving a new Snippet into the database, the user enters the name of the the Snippet and the language the Snippet is written in along with a comment describing the code (or changes to the code).

Snippets will be done in Java, and the UI will be entirely made with JavaFX. A keen eye will be kept in regards to spelling errors. Usability and accessibility are key issues.

Since this is running on Java and deployed as a Java JAR -file, it can be run anywhere Java 8 (version 1.8) is installed. The dependencies, such as SQLite, are linked into the JAR -file.

### Operation

Once Snippets is running, it has already loaded the user's personal database from their home directory (or, alternatively, to what is pointed in the `config.properties` file).

Now the user can start writing or copy a paste some code. Then, he/she can write a comment for the code and hit 'Save' or 'Save As' to save the Snippet. He/she will then be prompted for a name for the Snippet as well as the language in which it was written in. All of these are mandatory fields.

The new Snippet is created and visible in the app. In the TreeView element on the left is visible only the language that Snippet was written in. Expanding that node will show the Snippet's actual name. Double-clicking on it will open it up.

Changes can be written on top of the existing code, and a comment given. Then, by pressing 'Save', the new changes are pushed on top of the previous one in the database. The previous data is always kept.

When viewing an existing Snippet, the latest code is shown first.

The user can delete his/her Snippet, or all Snippets within a specified language. But individual history (code commit) of a Snippet is not possible.

## Current planned features

These features will go into the initial release version:

- add a Snippet
- view a Snippet
- browse Snippets
- make a new version of Snippet (update it)
- view the history of a Snippet
- remove a Snippet and all of its history
- remove all Snippets within a specific language
- manually define the database path

And additionally, these actions can be taken:

- expand/collapse all nodes on the language TreeView
- open up Snippet's manual on GitHub
- open up Snippet's main project page on GitHub
- toggle code wrap on/off
- open the directory containing the database

## Future development

Snippets will be updated semi-regularly. Some of these might be included in a future release:

- fancier GUI with some modern icon elements
- ability to change the database path within the app
- create a new database table, "Language", to house the language data
- hide specific Snippets (and unhide them)
- database backup functionality (perhaps to an online service)