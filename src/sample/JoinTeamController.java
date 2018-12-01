/**
 * The JoinTeam class is the controller for the JoinTeam fxml file. This
 * controller handles all the nodes and behaviors in the JavaFX scene. Players can send join
 * requests to teams.
 *
 * @author Jake Sherman
 * @modified Gregorio Lozada
 * @version 1.0
 * @since 11/1/2018
 */

package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
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

  private float xOffset;

  private float yOffset;

  /**
   * Initialize join team text field by adding a listener for when user types in it as well as menu
   * items for when user clicks on them
   */
  @FXML
  private void initialize() {
    xOffset = 15.0f;
    yOffset = 75.0f;

    setTeamSearchBarDropDownMenu(joinTeamTextField, contextMenu, xOffset, yOffset);

    messageTextNode.wrappingWidthProperty().bind(joinTeamTextField.widthProperty());
  }

  /**
   * Show context menu at specific coordinates, this is so the context menu stays at the same
   * position regardless of where user right clicks the textfield
   */
  @FXML
  private void onShowingContextMenu() {
    contextMenu.setAnchorX(joinTeamTextField.getScene().getWindow().getX() + xOffset);
    contextMenu.setAnchorY(joinTeamTextField.getScene().getWindow().getY() + yOffset);
  }

  /**
   * This handles what happens when the send join request button is clicked
   */
  @FXML
  private void onSendJoinRequestButtonClicked() {
    try {
      // Add player to team's request list
      Team team = Team.findTeamByName(joinTeamTextField.getText());
      team.addToRequestList((Player) Account.currentUser);

      // Close stage
      Stage stage = (Stage) joinTeamTextField.getScene().getWindow();
      stage.close();

      // Update home page
      ((HomePageController) Main.currentController.getController()).updateJoinTeamButton();

    } catch (Exception e) {
      messageTextNode.setText("Team does not exist");
    }
  }
}
