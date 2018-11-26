package sample;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public abstract class Controller {

  protected ArrayList<TextField> textFields = new ArrayList<>();

  protected Stage popUpStage;

  protected FileChooser fileChooser = new FileChooser();

  /***
   * Loads and sets the scene of the main stage
   *
   * @param sceneName used as a key to get the fxml URL of the desired scene to load
   */
  protected void changeScene(String sceneName) {
    // Load fxml file using the scene url in the scenes map in main using its key scene name
    FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.scenes.get(sceneName)));

    // Set current controller to loader
    Main.currentController = loader;

    try {
      Parent root = loader.load();
      // Load scene using root
      Main.loadScene(root);
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /***
   * Check to see all text textFields in scene are all filled
   *
   * @return true if all fields are filled in, false if there is at least on empty field
   */
  protected boolean checkFields() {
    for (TextField node : textFields) {
      //If a textfield node is empty and enabled
      if (node.getText().equals("") && !node.isDisabled()) {
        return false;
      }
    }

    return true;
  }

  /***
   * This method loads in a pop up scene
   *
   * @param fxmlURL the URL of the fxml file that will be used for the scene
   * @param sceneTitle title of the scene
   */
  protected void loadPopUpScene(String fxmlURL, String sceneTitle) {
    Parent root = null;

    try {
      root = FXMLLoader.load(getClass().getResource(fxmlURL));
    } catch (IOException ioException){
      System.out.println("Input/Output exception caught");
    }

    Scene popUpScene = new Scene(root);

    popUpStage.setScene(popUpScene);
    popUpStage.setTitle(sceneTitle);
    popUpStage.setResizable(false);

    popUpStage.show();
  }

  /**
   * This method takes in two strings converts them to all uppercase and checks if they match
   *
   * @param st1 string to be compared
   * @param st2 string to be compared
   * @return true if strings match, false if they do not
   */
  protected boolean stringsMatch(String st1, String st2) {
    return st1.toUpperCase().equals(st2.toUpperCase());
  }

  /**
   * As the user types in the text field passed a drop down menu will come up to auto fill the text
   * field
   *
   * @param textField text field that is being filled in
   * @param contextMenu drop down menu
   * @param xOffset x offset location of drop down menu
   * @param yOffset y offset location of drop down menu
   */
  protected void setTeamSearchBarDropDownMenu(TextField textField, ContextMenu contextMenu,
      float xOffset,
      float yOffset){

    // This handles whenever the user types in the text field
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      // If there is text in the text field
      if (!textField.getText().equals("")) {
        // Clear the context menu
        contextMenu.getItems().clear();

        // Check if current text in text field is similar to any team's name
        for (Team team : Team.teams) {
          // If the text field text length is less than or equal to the specific team name and
          // text matches team name substring
          if (textField.getText().length() <= team.getName().length() &&
              stringsMatch(textField.getText(),
                  team.getName().substring(0, textField.getText().length()))) {
            // Create new menu item
            MenuItem menuItem = new MenuItem(team.getName());
            // Set menu item onAction handler
            menuItem.setOnAction(event -> {
              // When menu item is clicked change text field text to menu item text
              textField.setText(menuItem.getText());
            });
            // Add menu item to context menu
            contextMenu.getItems().add(menuItem);
          }
        }

        // Show item menu under text field
        contextMenu.show(textField.getScene().getWindow(),
            textField.getScene().getWindow().getX() + xOffset,
            textField.getScene().getWindow().getY() + yOffset);
      } else {
        // If there is no text in text field hide context menu
        contextMenu.hide();
      }
    });
  }
}
