# Testing

This document contains information on how testing has been done in *Snippet*s.

The tests fall under basic unit tests (testing single classes), integration tests (testing the interoperability of two or more classes), and UI testing which is done manually by hitting all the buttons.

Used testing framework is JUnit, and code coverage is provided by Java Code Coverage (JaCoCo). Line and branch coverage is kept high (at or over 85 %).

## Test coverage

Here's my view of the JaCoCo report (2018-05-01):

![Coverage](https://github.com/gotonode/snippets/blob/master/docs/images/coverage_01.png)

100 % method and line coverage has been reached for the following classes:
- `/controller/HistoryItem`
- `/dao/SnippetDao`
- `/dao/SnippetDataDao`
- `/domain/Item`
- `/domain/Snippet`
- `/domain/SnippetData`

Currently, tests for instances that lead to errors aren't tested. Errors always display a friendly message to the user and then shut down the app. All other instances have been tested in the following classes:
- `/controller/Controller`
- `/database/Database`

In IntelliJ IDEA, my view of the (internal) test coverage results per class (2018-05-08):

![Coverage](https://github.com/gotonode/snippets/blob/master/docs/images/coverage_02.png)

## Logic testing

##### Package "domain"

Classes in the "domain" package are tested with simple unit tests. Within each test, a Snippet or a SnippetData is created, populated with test data and then read that the data is returned/processed correctly.

Since Snippet and SnippetData classes mainly just house their respective data, testing is very simple.

##### Package "dao"

The DAO classes are tested with the database. New instances of Snippet and SnippetData are created and then added to the database.

##### Package "ui"

No automated UI testing is done. UI is tested manually.

##### Package "app"

Since the Main class only handles the UI, it is not covered by tests.

StaticClass, a work-around helper class, is also tested here.

##### Package "controller"

The bulk of the testing is done in ControllerTest. These are integration tests.

Controller (class) talks with SnippetDao and SnippetDataDao and also understands about Snippet and SnippetData. All public-facing methods are tested with both valid and invalid values.

##### Package "database"

Please see the section on database testing in this document.

## Database testing

When the automated tests are run, a temporary folder is created by JUnit (the Java testing framework). This folder goes into the OS's temp folder.

Here's an example location on Windows:

```text
C:\Users\gotonode\AppData\Local\Temp\junit424626313747906588
```

Replace "gotonode" with your own username. The final digits are chosen pseudorandomly.

After the temporary directory has been created, a testing database is assigned there. Before each database test, this database is destroyed, recreated, and one sample Snippet with a history list containing two items added.

Tests, such as adding a Snippet (or SnippetData) into the database and then reading the latest added entry are done.

On the testing database, the database schema is exactly the same as on the production database. It is created with the same CREATE TABLE commands.

The full path, on Windows, now becomes:

```text
C:\Users\gotonode\AppData\Local\Temp\junit424626313747906588\snippets_TEST.db
```

Please feel free to delete these databases and/or the created directories at your leisure, but make sure no tests are running before you do.

## Shortcomings

Currently, the tests do cover all methods but line coverage could be better.

Especially, cases where a try/catch block is run, the case where an exception occurs aren't tested.

At present, the database tests are done with a database created into a temporary folder. These tests would be much faster if performed on a test-wide persistent in-memory database.

Finally, the names for the tests could be better.
