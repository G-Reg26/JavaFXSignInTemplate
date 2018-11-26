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
