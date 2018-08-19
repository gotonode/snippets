package io.github.gotonode.snippets.ui;

import javafx.scene.control.Alert;

/**
 * This static class is used to display messages to the user. It must be run on the JavaFX thread.
 */
public class MessageClass {

	/**
	 * Shows a warning message. The app may continue to run or be shut down after the message has been shown.
	 *
	 * @param message The warning message to show.
	 */
	public static void showWarning(String message) {
		generateAndShowAlert(message, null, Alert.AlertType.WARNING);
	}

	/**
	 * Shows a generic error message. The app must shut down after the message has been shown.
	 *
	 * @param message The error message to show.
	 * @param exception Pass the exception so we can gather data from it.
	 */
	public static void showError(String message, Exception exception) {
		generateAndShowAlert(message, exception, Alert.AlertType.ERROR);
	}

	/**
	 * Shows a database-specific error message. The app must shut down after the message has been shown.
	 *
	 * @param exception Pass the exception so we can gather data from it.
	 */
	public static void showDatabaseError(Exception exception) {
		generateAndShowAlert(null, exception, Alert.AlertType.ERROR);
	}

	private static void generateAndShowAlert(String message, Exception exception, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		switch (alert.getAlertType()) {
			case WARNING:
				alert.setHeaderText(null);
				alert.setContentText(message);
				break;
			case ERROR:
				alert.setHeaderText("An error occurred. Snippets must shut down.");
				alert.setContentText("For the technically savvy, here's the error details:\n\n".concat(exception.getMessage()));
				break;
			default:
				alert.setHeaderText(null);
				alert.setContentText(message);
				break;
		}

		alert.showAndWait();
	}
}
