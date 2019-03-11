# Snippets
*Snippets - a simple code snippet manager (student project)*

| Travis CI |
| :-------: |
| [![Build Status](https://travis-ci.org/gotonode/snippets.svg?branch=master)](https://travis-ci.org/gotonode/snippets) |

![Snippets running](https://github.com/gotonode/snippets/blob/master/docs/images/app_02.png)

A code snippet manager built with **Java** and tested with **JUnit**. GUI by **JavaFX**. Database functionality by **SQLite**. Code coverage by **JaCoCo**. Style enforcement by **Checkstyle**. Documentation by **JavaDoc**.

IDE used is **JetBrains IntelliJ IDEA Professional**.

## How to run

Download a release [here](https://github.com/gotonode/snippets/releases/tag/v1).

Double-click the downloaded JAR -file. If that doesn't work, use this command:

```
java -jar Snippets.jar
```

## Documentation

- [Manual](https://github.com/gotonode/snippets/blob/master/docs/manual.md)
- [Software requirements](https://github.com/gotonode/snippets/blob/master/docs/software_requirements.md)
- [Architecture](https://github.com/gotonode/snippets/blob/master/docs/architecture.md)
- [Testing](https://github.com/gotonode/snippets/blob/master/docs/testing.md)
- [Hours spent on project](https://github.com/gotonode/snippets/blob/master/docs/hours_spent.md)

## Commands

Before you start:

1. Please make sure that your OS properly detects Java
2. Maven must be installed also, and your OS must be able to find it
3. Build the project (see instructions below) 

### Building the project

This will also generate a runnable JAR -file.

```text
mvn clean package
```

The optional *clean* argument ensures no collisions happen with existing files, but building will take a bit longer.

> Output: "*target/snippets-1.0.jar*" (do not open the one that says "original" in it)

### Tests

##### JUnit tests

```text
mvn test
```

> (output will be printed to console)

##### Code-coverage report with JaCoCo

```text
mvn jacoco:report
```

> Output: "*target/site/jacoco/index.html*"

### Generate JavaDoc

JavaDoc has been written for all publicly-visible members.

```text
mvn javadoc:javadoc
```

> Output: "*target/site/apidocs/index.html*"

### Generate Checkstyle

Checkstyle checks can be manually created with this command:

```text
mvn jxr:jxr checkstyle:checkstyle
```

> Output: "*target/site/checkstyle.xml*"
