package io.github.gotonode.snippets.app;

import io.github.gotonode.snippets.database.Database;
import io.github.gotonode.snippets.ui.MessageClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This is Snippets, a simple snippet management utility.
 * Hereafter all snippets are referenced with an uppercase 'S'.
 *
 * @author github.com/gotonode
 */
public class Main extends Application {

	private static final Logger LOG = Logger.getLogger(Main.class.getName());

	/**
	 * The entry point for this application.
	 *
	 * @param args Arguments for the application (these are not used).
	 */
	public static void main(String[] args) {
		LOG.info("Snippets is starting...");

		Properties properties = new Properties();

		File configFile = new File("config.properties");
		File databaseFile;

		if (configFile.exists()) {
			try {
				properties.load(new FileInputStream(configFile.getAbsoluteFile()));
			} catch (IOException e) {
				MessageClass.showError("Could not process the configuration file.", e);
			}
			databaseFile = new File(properties.getProperty("database"));
		} else {
			databaseFile = new File(System.getProperty("user.home") + "/snippets.db");
		}
		LOG.info("Database assigned: '" + databaseFile.getAbsolutePath() + "'");

		Database database = new Database(databaseFile);

		StaticClass.createController(database);

		launch(args);
	}

	/**
	 * This will start the JavaFX application.
	 *
	 * @param stage The Stage is passed automatically.
	 * @throws Exception Can throw many exceptions.
	 */
	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));

		Scene scene = new Scene(root);

		stage.setTitle("Snippets");
		stage.setMinWidth(600);
		stage.setMinHeight(300);
		stage.setScene(scene);

		stage.show();
	}
}
