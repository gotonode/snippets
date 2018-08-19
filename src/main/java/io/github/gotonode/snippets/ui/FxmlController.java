package io.github.gotonode.snippets.ui;

import io.github.gotonode.snippets.app.StaticClass;
import io.github.gotonode.snippets.controller.Controller;
import io.github.gotonode.snippets.controller.HistoryItem;
import io.github.gotonode.snippets.domain.Snippet;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

// This class is exempt from JaCoCo coverage reports.

public class FxmlController implements Initializable {

	private Controller controller; // Handles most things here, except the UI stuff.

	private int currentSnippetId = -1;

	@FXML
	private TextArea fieldCode;

	@FXML
	private TextField fieldComment;

	@FXML
	private TreeView treeSnippets;

	@FXML
	private TableView tableHistory;

	@FXML
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		controller = StaticClass.controller;

		EventHandler<MouseEvent> mouseEventEventHandler = (MouseEvent event) -> {
			eventMouseClicked(event);
		};

		treeSnippets.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
		treeSnippets.setRoot(new TreeItem("root"));

		MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				TreeItem treeItem = (TreeItem) treeSnippets.getSelectionModel().getSelectedItem();

				if (treeItem.getChildren().size() == 0) {
					// It's a child node, containing the name of the Snippet.
					String name = treeItem.getValue().toString();
					int id = controller.getSnippetIdByName(name);
					boolean ok = showConfirmationAlert("Snippet: '" + name + "'", "Are you sure you want to delete this Snippet and all of its history? This action cannot be undone.");
					if (ok) {
						if (id == currentSnippetId) {
							unloadSnippet();
						}
						controller.deleteById(id);
						loadSnippetItems();
					}
				} else if (treeItem.getChildren().size() > 0) {
					// It's a root node (containing the language used).
					String lang = treeItem.getValue().toString();
					boolean ok = showConfirmationAlert("Language: '" + lang + "'", "Are you sure you want to delete all Snippets within this language? This action cannot be undone.");
					if (ok) {
						for (Object o : treeItem.getChildren()) {
							TreeItem treeItemLang = (TreeItem) o;
							if (currentSnippetId == controller.getSnippetIdByName(treeItemLang.getValue().toString())) {
								unloadSnippet();
							}
						}
						controller.deleteByLanguage(lang);
						loadSnippetItems();
					}
				}
			}
		});

		treeSnippets.setContextMenu(new ContextMenu(deleteItem));

		TableColumn<HistoryItem, Timestamp> timestampColumn = new TableColumn<>("Timestamp");
		timestampColumn.setPrefWidth(150);
		timestampColumn.setMinWidth(timestampColumn.getPrefWidth());
		timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		timestampColumn.setResizable(true);
		timestampColumn.setEditable(false);

		TableColumn<HistoryItem, Timestamp> commentColumn = new TableColumn<>("Comment");
		commentColumn.setPrefWidth(600);
		commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
		commentColumn.setResizable(true);
		timestampColumn.setEditable(false);

		tableHistory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue instanceof HistoryItem) {
				HistoryItem historyItem = (HistoryItem) newValue;
				int id = historyItem.getId();

				fieldCode.setText(controller.getSnippetCodeById(id));
			}
		});

		tableHistory.getColumns().addAll(timestampColumn, commentColumn);

		loadSnippetItems();
	}

	@FXML
	private void viewOnGitHub() {
		String uri = controller.getPropertyValue("home");
		browseTo(uri);
	}

	@FXML
	private void howToUse() {
		String uri = controller.getPropertyValue("manual");
		browseTo(uri);
	}

	private void browseTo(String uri) {
		try {
			if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(new URI(uri));
				} catch (Exception e) {
					MessageClass.showError("Error opening the Web URL.", e);
					System.exit(1);
				}
			} else {
				// The formal way of writing Web (as in WWW) is with an uppercase 'W'.
				MessageClass.showWarning("Your operating system doesn't seem to support opening Web links.");
			}
		} catch (Exception e) {
			MessageClass.showWarning("An unknown error occurred while trying to open the link.");
		}
	}

	@FXML
	private void changeCodeWrap(ActionEvent event) {
		CheckMenuItem checkMenuItem = (CheckMenuItem) event.getSource();
		fieldCode.setWrapText(checkMenuItem.isSelected());
	}

	private void addCodeToExistingSnippet() {

		if (!validateCodeAndComment()) {
			return;
		}

		controller.addNewCodeToExistingSnippet(currentSnippetId, fieldCode.getText(), fieldComment.getText());

		refreshHistoryTable();

		fieldComment.setText("");
	}

	private void showAlert(Alert.AlertType alertType, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private boolean showConfirmationAlert(String header, String content) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText(header);
		alert.setContentText(content);
		Optional<ButtonType> result = alert.showAndWait();
		return ((result.isPresent()) && (result.get() == ButtonType.OK));
	}

	private void saveNewSnippet() {

		if (!validateCodeAndComment()) {
			return;
		}

		ArrayList<String> arrayList = promptForNameAndLanguage();
		if (arrayList == null) {
			return; // The user canceled the save.
		}

		// Gather all the needed data here.

		String name = arrayList.get(0).trim();
		if (name.isEmpty()) {
			showAlert(Alert.AlertType.INFORMATION, null, "Please enter a name for your Snippet.");
			return;
		} else if (controller.nameAlreadyExists(name)) {
			showAlert(Alert.AlertType.INFORMATION, null, "A Snippet with this name already exists.");
			return;
		}

		String language = arrayList.get(1).trim();
		if (language.isEmpty()) {
			showAlert(Alert.AlertType.INFORMATION, null, "Please tell which language your Snippet is written in.");
			return;
		}

		String code = fieldCode.getText().trim();
		String comment = fieldComment.getText().trim();

		// Push the save.
		controller.addNewSnippet(name, language, code, comment);

		// Reload the Snippets on the TreeView.
		loadSnippetItems();

		// Clear this field.
		fieldComment.setText("");

		// Search for the correct item, expand its parent and select it.
		TreeItem root = treeSnippets.getRoot();
		root.setExpanded(true);
		for (Object snippetLang : root.getChildren()) {
			TreeItem langItem = (TreeItem) snippetLang;
			for (Object snippetName : langItem.getChildren()) {
				TreeItem nameItem = (TreeItem) snippetName;
				String value = nameItem.getValue().toString();
				if (value.equals(name)) {
					nameItem.getParent().setExpanded(true);
					treeSnippets.getSelectionModel().select(nameItem);
				}
			}
		}

		loadSnippetHistory(name);
	}

	private boolean validateCodeAndComment() {
		if (fieldCode.getText().trim().isEmpty()) {
			showAlert(Alert.AlertType.INFORMATION, null, "Please enter some code first.");
			fieldCode.requestFocus();
			return false;
		} else if (fieldComment.getText().trim().isEmpty()) {
			showAlert(Alert.AlertType.INFORMATION, null, "A comment for your code (or code changes) is mandatory.");
			fieldComment.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * This fun little function creates a JavaFX dialog and prompts for a name and language.
	 *
	 * @return Returns the name (index 0) and language (index 1); null if the user canceled.
	 */
	@Nullable
	private ArrayList<String> promptForNameAndLanguage() {
		Dialog<ArrayList> dialog = new Dialog();
		dialog.setTitle("Create a new Snippet");
		dialog.setHeaderText("Please enter a name (must be unique) and language for your Snippet.");

		DialogPane dialogPane = dialog.getDialogPane();
		TextField fieldName = new TextField();
		fieldName.setPromptText("Name");
		TextField fieldLanguage = new TextField();
		fieldLanguage.setPromptText("Language");
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setContent(new VBox(8, fieldName, fieldLanguage));
		Platform.runLater(fieldName::requestFocus);
		dialog.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK) {
				ArrayList<String> results = new ArrayList<>();
				results.add(fieldName.getText());
				results.add(fieldLanguage.getText());
				return results;
			}

			return null;
		});

		Optional<ArrayList> result = dialog.showAndWait();

		if (result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}

	@FXML
	private void newSnippet(ActionEvent event) {
		unloadSnippet();
	}

	/**
	 * Unloads everything, and clears all text fields.
	 */
	private void unloadSnippet() {

		// Set to an ID that cannot possibly be assigned to anything (my database uses unsigned integers).
		currentSnippetId = -1;

		// First we clear the history table.
		while (tableHistory.getItems().size() > 0) {
			tableHistory.getItems().remove(0);
		}

		// Clear the code field.
		fieldCode.setText("");

		// Clear the comment field.
		fieldComment.setText("");

		// Deselect the selected item from the TreeView.
		treeSnippets.getSelectionModel().clearSelection();
	}

	@FXML
	private void save(ActionEvent event) {

		if (currentSnippetId == -1) {
			saveAs(event);
		} else {
			addCodeToExistingSnippet();
		}
	}

	private void expandOrCollapseAll(boolean expand) {
		TreeItem root = treeSnippets.getRoot();
		for (Object child : root.getChildren()) {
			TreeItem childItem = (TreeItem) child;
			childItem.setExpanded(expand);
		}
	}

	@FXML
	private void expandAll(ActionEvent event) {
		expandOrCollapseAll(true);
	}

	@FXML
	private void collapseAll(ActionEvent event) {
		expandOrCollapseAll(false);
	}

	@FXML
	private void locateDatabase(ActionEvent event) {
		try {
			Desktop.getDesktop().open(controller.getDatabaseFolder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void saveAs(ActionEvent event) {
		saveNewSnippet();
	}

	/**
	 * Reverts any changes the user has made by forcing a reload of the current history item.
	 *
	 * @param event The system will send this one.
	 */
	@FXML
	private void revert(ActionEvent event) {
		if (currentSnippetId == -1) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cannot revert when a Snippet hasn't been loaded.");
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}

		Object o = tableHistory.getSelectionModel().getSelectedItem();
		tableHistory.getSelectionModel().clearSelection();
		tableHistory.getSelectionModel().select(o);
	}

	@FXML
	private void exit(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	private void refreshSnippets(ActionEvent event) {
		loadSnippetItems();
	}

	private void eventMouseClicked(MouseEvent event) {

		if (event.getClickCount() != 2 || event.getButton() != MouseButton.PRIMARY) {
			return;
		}

		Object selectedItem = treeSnippets.getSelectionModel().getSelectedItem();

		if (selectedItem instanceof TreeItem) {
			TreeItem treeItem = (TreeItem) selectedItem;
			if (treeItem.getChildren().size() == 0) {
				loadSnippetHistory(treeItem.getValue().toString());
			}
		}
	}

	private void loadSnippetItems() {

		while (treeSnippets.getRoot().getChildren().size() > 0) {
			treeSnippets.getRoot().getChildren().remove(0);
		}

		Map<String, List<Snippet>> map = controller.getSnippetsInMap();

		Iterator iterator = map.entrySet().iterator();

		// Iterate through the Map, create the TreeView nodes and add them in place.
		while (iterator.hasNext()) {
			Map.Entry keyValue = (Map.Entry) iterator.next();
			iterator.remove();
			TreeItem lang = new TreeItem(keyValue.getKey());
			List<Snippet> snippets = (List<Snippet>) keyValue.getValue();
			for (Snippet snippet : snippets) {
				TreeItem name = new TreeItem(snippet.getName());
				lang.getChildren().add(name);
			}
			treeSnippets.getRoot().getChildren().add(lang);
		}
	}

	private void loadSnippetHistory(String name) {
		currentSnippetId = controller.getSnippetIdByName(name);

		refreshHistoryTable();
	}

	private void refreshHistoryTable() {
		System.out.println("Loading Snippet #" + currentSnippetId);

		// Get the history of this Snippet and display it in the table.
		ObservableList<HistoryItem> observableList = controller.getHistoryOfSnippet(currentSnippetId);
		tableHistory.setItems(observableList);

		// Choose the first row of data. This will trigger loading that history item as well.
		tableHistory.getSelectionModel().selectFirst();
	}
}
