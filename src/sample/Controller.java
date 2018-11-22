package sample;

import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public abstract class Controller {

  //All text fields in scene to be checked
  protected ArrayList<TextField> textFields = new ArrayList<>();

  /***
   * Loads and sets the scene of the main stage
   *
   * @param sceneName used as a key to get the fxml URL of the desired scene to load
   * @throws Exception
   */
  protected void changeScene(String sceneName) throws Exception {
    //Load fxml file using the scene url in the scenes map in main using its key scene name
    FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.scenes.get(sceneName)));

    Main.currentController = loader;

    Parent root = loader.load();
    //Load scene using root
    Main.loadScene(root);
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
}
