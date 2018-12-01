/**
 * The JoinTeam class is the controller for the JoinTeam fxml file. This
 * controller handles all the nodes and behaviors in the JavaFX scene. Players can send join
 * requests to teams.
 *
 * @author Jake Sherman
 * @modified Gregorio Lozada
 * @version 1.0
 * @since 11/26/2018
 */

package sample;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessageBoxController extends Controller {

  public static String messageString;

  @FXML
  private Text message;

  @FXML
  private void initialize() {
    message.setText(messageString);
  }

  @FXML
  void onOKButtonClicked() {
    // Close stage
    Stage stage = (Stage) message.getScene().getWindow();
    stage.close();
  }

}
