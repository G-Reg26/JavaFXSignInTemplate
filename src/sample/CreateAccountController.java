/**
 * The CreateAccountController class is the controller for the create account fxml file. This
 * controller handles all the nodes and behaviors in the JavaFX scene. Users can create their
 * account in this scene by filling in all text fields with the appropriate information.
 *
 * @author Gregorio Lozada
 * @version 1.0
 * @since 10/18/2018
 */

package sample;

import static sample.Account.AccountType.MANAGER;
import static sample.Account.AccountType.PLAYER;
import static sample.Account.AccountType.SPECTATOR;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import sample.Account.AccountType;

public class CreateAccountController extends Controller {

  public static boolean editing = false;

  public static String prevSceneKey;

  @FXML
  private Text actionTarget;

  @FXML
  private TextField name;

  @FXML
  private TextField userName;

  @FXML
  private PasswordField password;

  @FXML
  private PasswordField confirmPassword;

  @FXML
  private TextField teamName;

  @FXML
  private MenuButton menuButton;

  @FXML
  private ImageView profilePic;

  private Image image;

  private File profilePicFile;

  private AccountType accountType;

  @FXML
  public void initialize() {
    textFields.add(name);
    textFields.add(userName);
    textFields.add(password);
    textFields.add(confirmPassword);
    textFields.add(teamName);

    if (!editing) {
      menuButton.setText("Spectator");
      accountType = SPECTATOR;
    } else {
      name.setPromptText("Change name");
      userName.setPromptText("Change username");
      password.setPromptText("Change password");
      confirmPassword.setPromptText("Confirm password");
      teamName.setPromptText("Change team name");

      name.setText(Account.currentUser.getName());
      userName.setText(Account.currentUser.getUsername());

      switch (Account.currentUser.getAccountType()) {
        case SPECTATOR:
          menuButton.setText("Spectator");
          accountType = SPECTATOR;
          break;
        case PLAYER:
          menuButton.setText("Player");
          accountType = PLAYER;
          break;
        case MANAGER:
          menuButton.setText("Manager");
          accountType = MANAGER;
          teamName.setText(((Manager) Account.currentUser).getTeam().getName());
          break;
      }

      image = SwingFXUtils.toFXImage(Account.currentUser.getProfilePic(), null);
      profilePic.setImage(image);
    }
  }

  /**
   * This handles what happens when the spectator menu item is clicked
   */
  @FXML
  private void onSpectatorMenuItemClicked() {
    menuItemSelected("Spectator", SPECTATOR, true);
  }

  /**
   * This handles what happens when the player menu item is clicked
   */
  @FXML
  private void onPlayerMenuItemClicked() {
    menuItemSelected("Player", AccountType.PLAYER, true);
  }

  /**
   * This handles what happens when the manager menu item is clicked
   */
  @FXML
  private void onManagerMenuItemClicked() {
    boolean setTeamNameDisabled;

    if (!editing || (editing && !(Account.currentUser instanceof Manager))) {
      setTeamNameDisabled = false;
    } else {
      setTeamNameDisabled = true;
    }

    menuItemSelected("Manager", AccountType.MANAGER, setTeamNameDisabled);
  }

  /**
   * This handles what happens when the back to sign in button is clicked
   */
  @FXML
  private void onBackToSignInButtonClicked() {
    changeScene(prevSceneKey);
  }

  /**
   * This handles what happens when the submit button is clicked
   */
  @FXML
  private void onSubmitButtonClicked() {
    // If not editing a profile
    if (!editing) {
      // If all text and password textFields are filled
      if (checkFields()) {
        // If account does not exist
        if (accountDoesNotExist()) {
          // If password and confirm password match
          if (confirmPasswords()) {
            // Create new account
            createAccount();
          } else {
            // Notify user that password and confirm password do not match
            actionTarget.setText("Passwords do not match");
          }
        }
      } else {
        // Notify user to fill all text and password textFields
        actionTarget.setText("Please fill all textFields");
      }
    } else {
      // If all required fields pass checks
      if (fieldsPass()) {
        // If account being edited is changing types
        if (Account.currentUser.getAccountType() != accountType) {
          // If account is a manager account
          if (Account.currentUser instanceof Manager) {
            // Delete manager's team and set manager's team to null
            ((Manager) Account.currentUser).getTeam().deleteTeam();
            ((Manager) Account.currentUser).setTeam(null);
          }
          // If account is a player account
          else if (Account.currentUser instanceof Player) {
            // Change account tier
            ((Player) Account.currentUser).changeTier();
          }

          // Create temp account
          Account tempAccount;

          // Depending on the account type set
          switch (accountType) {
            case SPECTATOR:
              tempAccount = new Account(Account.currentUser.getName(),
                  Account.currentUser.getUsername(),
                  Account.currentUser.getPassword(),
                  accountType);
              break;
            case PLAYER:
              tempAccount = new Player(Account.currentUser.getName(),
                  Account.currentUser.getUsername(),
                  Account.currentUser.getPassword(),
                  accountType);
              break;
            case MANAGER:
              tempAccount = new Manager(Account.currentUser.getName(),
                  Account.currentUser.getUsername(),
                  Account.currentUser.getPassword(),
                  accountType,
                  null);
              break;
            default:
              tempAccount = null;
              break;
          }

          // Transfer followed teams from previous account to new account
          tempAccount.getTeamsFollowed().addAll(Account.currentUser.getTeamsFollowed());

          // Remove previous account from the accounts list and add new account to accounts list
          Account.accounts.remove(Account.currentUser);
          Account.accounts.add(tempAccount);

          // Set current user to new account
          Account.currentUser = tempAccount;

          // If teamName textfield is not disabled
          if (!teamName.isDisabled()) {
            // Create team using the team name text field text
            Team team = new Team((Manager) tempAccount, teamName.getText());
            // Add team to team list
            Team.teams.add(team);

            // Set new account's team and disable team name text field
            ((Manager) Account.currentUser).setTeam(team);
            teamName.setDisable(true);
          }
        }

        // If name textfield is not empty and not set to current account's name
        if (!name.getText().equals("") ||
            !name.getText().equals(Account.currentUser.getName())) {
          // Set account's name to text in name textfield
          Account.currentUser.setName(name.getText());
        }

        // If username textfield is not empty and not set to current account's username
        if (!userName.getText().equals("") ||
            !userName.getText().equals(Account.currentUser.getUsername())) {
          // Set account's username to text in username textfield
          Account.currentUser.setUsername(userName.getText());

          // Rename profile pic file to new file name
          File newFile = new File
              (".\\src\\sample\\Images\\AccountProfilePics\\" + Account.currentUser.getUsername()
                  + ".png");
          Account.currentUser.getProfilePicFile().renameTo(newFile);
        }

        // If password textfield is not empty
        if (!password.getText().equals("")) {
          // Set account's password to text in password textfield
          Account.currentUser.setPassword(password.getText());
        }

        setProfilePic();

        actionTarget.setText("Account has been edited");
      }
    }
  }

  /**
   * This handles what happens when the change profile pic button is clicked
   */
  @FXML
  private void onChangeProfilePicButtonClicked() {
    // Set profile pic file to the file chosen in file chooser
    profilePicFile = fileChooser.showOpenDialog(popUpStage);

    try {
      // If profile pic is not null and is a png file
      if (supportedImageType(profilePicFile.getName())) {
        // Set profile pic to png file
        image = SwingFXUtils.toFXImage(ImageIO.read(profilePicFile), null);
        profilePic.setImage(image);
      } else {
        actionTarget.setText("File chosen is the wrong file type");
      }
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /**
   * This method sets the text of the menu button, the type of account to be created, and disables/
   * enables the team name text field
   *
   * @param menuButtonText the text that the menu button text will be set to
   * @param accountType the type of account to be created
   * @param teamNameDisabled whether or not the team name text field should be enabled
   */
  private void menuItemSelected(String menuButtonText, AccountType accountType,
      boolean teamNameDisabled) {
    //Change text of menu button
    menuButton.setText(menuButtonText);
    //Set accountType to manager
    this.accountType = accountType;
    //Enable team name textfield
    teamName.setDisable(teamNameDisabled);
  }

  /**
   * When editing, this method checks all required text fields
   *
   * @return true if all text fields pass false if otherwise
   */
  private boolean fieldsPass() {
    // If the username is already taken
    if (Account.findUserByName(userName.getText()) != null &&
        Account.findUserByName(userName.getText()) != Account.currentUser) {
      actionTarget.setText("Username is already taken");
      return false;
    }

    // If password textfield is not empty
    if (!password.getText().equals("")) {
      // If confirmPassword textfield is empty
      if (confirmPassword.getText().equals("")) {
        actionTarget.setText("Please confirm password");
        return false;
      } else {
        // If confirmPassword text field text does not equal password textfield
        if (!confirmPassword.getText().equals(password.getText())) {
          actionTarget.setText("Passwords do not match");
          return false;
        }
      }
    }

    // If teamName text field is not disabled
    if (!teamName.isDisabled()) {
      // If textfield is empty
      if (teamName.getText().equals("")) {
        actionTarget.setText("Please enter team name or change account type");
        return false;
      } else {
        // If current user is a manager profile
        if (Account.currentUser instanceof Manager) {
          // If team name is already taken
          if (Team.findTeamByName(teamName.getText()) != null &&
              Team.findTeamByName(teamName.getText()) != ((Manager) Account.currentUser)
                  .getTeam()) {
            actionTarget.setText("Team name is already taken");
            return false;
          }
        } else {
          // If team name is already taken
          if (Team.findTeamByName(teamName.getText()) != null) {
            actionTarget.setText("Team name is already taken");
            return false;
          }
        }
      }
    }

    // If all fields checked pass
    return true;
  }

  /**
   * Checks every account in the account list to see if the current text in the username textfield
   * matches an already taken username
   *
   * @return false if the username is already taken, and true if the username is not taken
   */
  public boolean accountDoesNotExist() {
    // If account exists
    if (Account.findUserByName(userName.getText()) != null) {
      actionTarget.setText("Username is already in use");
      return false;
    }

    // If user is a manager
    if (accountType == AccountType.MANAGER) {
      // If team exists
      if (Team.findTeamByName(teamName.getText()) != null) {
        actionTarget.setText("Team name is already in use");
        return false;
      }
    }

    return true;
  }

  /**
   * confirms that the reentered password matches the original password textfield
   *
   * @return true if the text in password and confirm password match, and false if they do not match
   */
  public boolean confirmPasswords() {
    return password.getText().equals(confirmPassword.getText());
  }

  /**
   * Set current user's profile pic to the file chosen from file chooser
   */
  private void setProfilePic() {
    try {
      // If profile pic is not null
      if (profilePicFile != null) {
        // Copy contents of file to current user's profile pic file
        FileManager.copyToFile(profilePicFile, Account.currentUser.getProfilePicFile());
        // Set profile pic image
        Account.currentUser.setProfilePic(ImageIO.read(Account.currentUser.getProfilePicFile()));
      }
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /**
   * This method creates an account and, if account type is MANAGER, a team using the text from the
   * text fields in this scene. Created teams and accounts are then added to their respective lists
   */
  public void createAccount() {
    // Get text from text textFields and create a new account with them
    // Check account type
    switch (accountType) {
      case SPECTATOR:
        Account.currentUser =
            new Account(name.getText(), userName.getText(), password.getText(), accountType);
        break;
      case PLAYER:
        Account.currentUser =
            new Player(name.getText(), userName.getText(), password.getText(), accountType);
        break;
      case MANAGER:
        // Create team using the team name text field text
        Team team = new Team(teamName.getText());
        // Add team to team list
        Team.teams.add(team);

        Account.currentUser =
            new Manager(name.getText(), userName.getText(), password.getText(), accountType, team);

        team.setManager((Manager) Account.currentUser);

        // Create and add a new news object to news list
        News tempNews = new News(
            userName.getText() + " has just made a new team, the " + teamName.getText() + ".",
            new Date());
        tempNews.addTeamInvolved(team);
        News.newsList.add(tempNews);
        break;
    }

    setProfilePic();

    // Add account to account list
    Account.accounts.add(Account.currentUser);

    changeScene("HomePageController");
  }
}
