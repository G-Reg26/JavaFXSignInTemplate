package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SignInController extends Controller {

  @FXML
  //actionTarget is used for pop up messages to the user
  private Text actionTarget;
  @FXML
  private TextField userName;
  @FXML
  private PasswordField password;

  @FXML
  public void initialize() {
    //Add text field nodes to textFields
    //This is primarily for textFields to be checked
    textFields.add(userName);
    textFields.add(password);
  }

  //This handles what happens when the sign in button is clicked
  @FXML
  protected void handleSignInButtonAction(ActionEvent event) {
    //IF all text and password textFields are filled
    if (checkFields()) {
      //IF there is an account with the entered username and password
      if (checkUserAndPassword()) {
        //Notify user whose account you're signing into
        try {
          //Go to home page
          changeScene("HomePageController");
        } catch (Exception exception) {
          System.out.println("Exception caught");
        }
      } else {
        //Notify user that they have entered an invalid username or password
        actionTarget.setText("Invalid username or password");
      }
    } else {
      //Notify user to fill all text and password textFields
      actionTarget.setText("Please fill all textFields");
    }
  }

  //This handles what happens when the create account button is clicked
  @FXML
  protected void handleCreateAccountButtonAction(ActionEvent event) {
    try {
      //Go to create account
      changeScene("CreateAccount");
    } catch (Exception exception) {
      System.out.println("Exception caught");
    }
  }

  @FXML
  protected void handleReturnToHomepageButtonAction(ActionEvent event) {
    try {
      //Go to home page
      changeScene("HomePageController");
    } catch (Exception exception) {
      System.out.println("Exception caught");
    }
  }

  public boolean checkUserAndPassword() {
    //IF there are already accounts made
    if (Account.accounts.size() > 0) {
      //Check all accounts
      for (Account account : Account.accounts) {
        //IF there is an account with entered username and password
        if (account.getUsername().equals(userName.getText()) &&
            account.getPassword().equals(password.getText())) {
          //set current user to account being signed in to
          Account.currentUser = account;
          return true;
        }
      }
    }

    return false;
  }

}
