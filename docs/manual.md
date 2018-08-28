# Snippets Manual

Snippets is fun and easy to use! It has been designed with accessibility in mind.

## Getting Snippets

If you haven't already, download a release [here](https://github.com/gotonode/snippets/releases) (choose the most recent version).

## Running it

Double-click the downloaded JAR -file. If that doesn't work, use this command from the directory you downloaded the JAR into:

```text
java -jar Snippets.jar
```

The app will set all necessary configurations automatically.

## Using it

Once Snippets starts, you'll be greeted with this view:

![Snippets running](https://github.com/gotonode/snippets/blob/master/docs/images/app_01.png)

From the left, you can double-click on the name of a programming/scripting/markup/query language to view all the Snippets within that language. In our example image above, JavaScript has been opened and we're viewing the code of a single JavaScript Snippet.

You'll see the code in the big text field, and this is where you'll do your edits also.

At the top of the app, you'll find a table containing the full history of the opened Snippet. Currently, there are two commits to this Snippet.

Clicking on an entry in the table will display the Snippet's code at that moment in time. Snippets will retain the full history of all Snippets, no matter how many times you update it.

##### Creating a new Snippet

From the main menu, choose 'File' and then 'New Snippet'. This will clear all the fields and unload the currently loaded Snippet (if any). Now just write (or copy & paste) your code into the code view, enter a comment describing your changes and click 'Save'. You'll then be prompted for a name and language for your new Snippet.

##### Editing a Snippet

If you already have a Snippet open, simple write your changes into the code view. Once you're done, enter a short comment describing the changes you've made. Then hit 'Save'. You can always go back to the previous version(s) of the code.

##### Removing a Snippet

Right-click on the Snippet's name on the tree (left-hand side). Then choose 'Delete' and confirm your action. If you want to delete all Snippets within a specific language, right-click on the language and follow the previous steps. All deletions are final.

##### Using 'Save As'

Having a Snippet open, you can click 'Save As' to save the current code into a completely new Snippet. How useful is that! You'll be prompted for a name and language of the new Snippet to be created.

##### Configuration

If you'd like to specify a database location of your own, here's how.

Create an empty file and name it "`config.properties`". Inside, write the following:

```text
database=C:\path_to_database\databaseFile.db
```

Replace the path to the database file with your own path.

This feature is especially useful if you want all users on the computer to share a single database. Currently, Snippets creates (and uses) a different database for each user in their home directories.

##### Additional features

From the main menu, you can choose 'Tree' and then either 'Expand All' or 'Collapse All'. These will either open up (or close) all the nodes in the tree on the very left.

Snippets, by default, wraps long lines of code. To toggle this off, choose 'Code' from the main menu and then 'Wrap Code'.

If you'd like to view the database Snippets is using, navigate to 'Tools' in the main menu and then choose 'View Database Location'.

You can find Snippets on GitHub by opening 'Help' from the main menu, and then choosing 'View on GitHub'. Choosing 'How to use Snippets?' will open this manual.

If, for some reason, the loaded tree (on the left) of Snippets goes out of sync, you can open 'File' and then choose 'Refresh Snippets'. This will force a reload of all the stored data in the database (in case you chose to manually edit that). 
