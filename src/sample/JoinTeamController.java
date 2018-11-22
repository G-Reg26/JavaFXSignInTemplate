package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JoinTeamController extends Controller {

  @FXML
  private Text messageTextNode;

  @FXML
  private TextField joinTeamTextField;

  @FXML
  private ContextMenu contextMenu;

  /***
   * Initialize join team text field by adding a listener for when user types in it as well as menu
   * items for when user clicks on them
   */
  @FXML
  protected void initialize() {
    if (((Player) Account.currentUser).getTeamRequested() != null ||
        ((Player) Account.currentUser).getTeam() != null) {
      joinTeamTextField.setDisable(true);
      joinTeamTextField.setPromptText("You have already sent request");
    }

    //This handles whenever the user types in the text field
    joinTeamTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      //If there is text in the text field
      if (!joinTeamTextField.getText().equals("")) {
        //Clear the context menu
        contextMenu.getItems().clear();

        //Check if current text in text field is similar to any team's name
        for (Team team : Team.teams) {
          //If the text field text length is less than or equal to the specific team name AND
          //text matches team name substring
          if (joinTeamTextField.getText().length() <= team.getName().length() &&
              stringsMatch(joinTeamTextField.getText(),
                  team.getName().substring(0, joinTeamTextField.getText().length()))) {
            //Create new menu item
            MenuItem menuItem = new MenuItem(team.getName());
            //Set menu item onAction handler
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
                //When menu item is clicked change text field text to menu item text
                joinTeamTextField.setText(menuItem.getText());
              }
            });
            //Add menu item to context menu
            contextMenu.getItems().add(menuItem);
          }
        }

        //Show item menu under text field
        contextMenu.show(joinTeamTextField.getScene().getWindow(),
            joinTeamTextField.getScene().getWindow().getX() + 15.0f,
            joinTeamTextField.getScene().getWindow().getY() + 75.0f);
      } else {
        //If there is no text in text field hide context menu
        contextMenu.hide();
      }
    });

    messageTextNode.wrappingWidthProperty().bind(joinTeamTextField.widthProperty());
  }

  /***
   * Show context menu at specific coordinates, this is so the context menu stays at the same
   * position regardless of where user right clicks the textfield
   */
  @FXML
  protected void onShowingContextMenu() {
    contextMenu.setAnchorX(joinTeamTextField.getScene().getWindow().getX() + 15.0f);
    contextMenu.setAnchorY(joinTeamTextField.getScene().getWindow().getY() + 75.0f);
  }

  @FXML
  protected void onSendJoinRequestButtonClicked() {
    try {
      messageTextNode.setText(Team.findTeamByName(joinTeamTextField.getText())
          .addToRequestList((Player) Account.currentUser));
      Stage stage = (Stage) joinTeamTextField.getScene().getWindow();
      stage.close();

      ((HomePageController) Main.currentController.getController()).updateJoinTeamButton();

    } catch (Exception e) {
      messageTextNode.setText("Team does not exist");
    }
  }

  /***
   * This method takes in two strings converts them to all uppercase and checks if they match
   *
   * @param st1 string to be compared
   * @param st2 string to be compared
   * @return true if strings match, false if they do not
   */
  private boolean stringsMatch(String st1, String st2) {
    return st1.toUpperCase().equals(st2.toUpperCase());
  }
}
