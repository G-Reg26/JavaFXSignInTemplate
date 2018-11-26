package sample;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SignInController extends Controller {

  @FXML
  private Text actionTarget;

  @FXML
  private TextField userName;

  @FXML
  private PasswordField password;

  @FXML
  private void initialize() {
    textFields.add(userName);
    textFields.add(password);
  }

  /**
   * This handles what happens when the sign in button is clicked
   */
  @FXML
  private void handleSignInButtonAction() {
    // If all text and password textFields are filled
    if (checkFields()) {
      // If there is an account with the entered username and password
      if (checkUserAndPassword()) {
        // Notify user whose account you're signing into
        changeScene("HomePageController");
      } else {
        actionTarget.setText("Invalid username or password");
      }
    } else {
      actionTarget.setText("Please fill all textFields");
    }
  }

  /**
   * This handles what happens when the create account button is clicked
   */
  @FXML
  private void handleCreateAccountButtonAction() {
    CreateAccountController.editing = false;
    CreateAccountController.prevSceneKey = "Login";
    changeScene("CreateAccount");
  }

  /**
   * This handles what happens when the return to home page button is clicked
   */
  @FXML
  private void handleReturnToHomepageButtonAction() {
    changeScene("HomePageController");
  }

  /**
   * Check if username and password entered are valid
   *
   * @return true if username and password are valid false if otherwise
   */
  private boolean checkUserAndPassword() {
    // If there are already accounts made
    if (Account.accounts.size() > 0) {
      // Check all accounts
      for (Account account : Account.accounts) {
        // If there is an account with entered username and password
        if (account.getUsername().equals(userName.getText()) &&
            account.getPassword().equals(password.getText())) {
          // Set current user to account being signed in to
          Account.currentUser = account;
          return true;
        }
      }
    }

    return false;
  }

}
