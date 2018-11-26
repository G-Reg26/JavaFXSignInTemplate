package sample;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class EditTeamController extends Controller {

  @FXML
  private ListView<Event> eventListView;

  @FXML
  private ListView<Player> teamListView;

  @FXML
  private ListView<Player> requestListView;

  @FXML
  private TextField changeNameTextField;

  @FXML
  private ImageView profilePic;

  private Image image;

  private ObservableList eventsObsList = FXCollections.observableArrayList();

  private ObservableList requestObsList = FXCollections.observableArrayList();

  private ObservableList teamObsList = FXCollections.observableArrayList();

  private Player selectedPlayer;

  private Event selectedEvent;

  @FXML
  private void initialize() {
    // Initialize pop up stage
    popUpStage = new Stage();
    popUpStage.initModality(Modality.APPLICATION_MODAL);

    updateLists();

    image = SwingFXUtils.toFXImage(Team.currentTeam.getProfilePic(), null);

    profilePic.setImage(image);

    teamListView.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> selectedPlayer = newValue);

    requestListView.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> selectedPlayer = newValue);

    eventListView.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> selectedEvent = newValue);
  }

  /**
   * This handles what happens when tab is changed
   */
  @FXML
  private void tabChanged() {
    selectedPlayer = null;
  }

  /**
   * This handles what happens when the team page button is clicked
   */
  @FXML
  private void onTeamPageButtonClicked() {
    changeScene("TeamPage");
  }

  /**
   * This handles what happens when the accept button is clicked
   */
  @FXML
  private void onAcceptButtonClicked() {
    // If selected player is not null
    if (selectedPlayer != null) {
      // Add selected player to members list
      Team.currentTeam.addPlayer(selectedPlayer);
      // Set selected player's team to current user's team
      selectedPlayer.setTeam(Team.currentTeam);

      createNewsObject(selectedPlayer + " has joined the " + Team.currentTeam);

      updateLists();
    }
  }

  /**
   * This handles what happens when the deny button is clicked
   */
  @FXML
  private void onDenyButtonClicked() {
    // If selected player is not null
    if (selectedPlayer != null) {
      // Remove player from requests list
      Team.currentTeam.getJoinRequestsList().remove(selectedPlayer);
      // Set selected player's requested team to null
      selectedPlayer.setTeamRequested(null);

      updateLists();
    }
  }

  /**
   * This handles what happens when the remove button is clicked
   */
  @FXML
  private void onRemoveButtonClicked() {
    // If selected player is not null
    if (selectedPlayer != null) {
      // Remove player from members list
      Team.currentTeam.removePlayer(selectedPlayer);
      // Set player's team to null
      selectedPlayer.setTeam(null);

      createNewsObject(selectedPlayer + " has left the " + Team.currentTeam);

      updateLists();
    }
  }

  /**
   * This handles what happens when the create button is clicked
   */
  @FXML
  private void onCreateButtonClicked() {
    CreateEventController.editing = false;
    changeScene("CreateEvent");
  }

  /**
   * This handles what happens when the edit button is clicked
   */
  @FXML
  private void onEditButtonClicked() {
    // If selected event is not null
    if (selectedEvent != null) {
      // Set current event to selected event
      Event.currentEvent = selectedEvent;
      // Set event is being edited
      CreateEventController.editing = true;
      changeScene("CreateEvent");
    }
  }

  /**
   * This handles what happens when the cancel button is clicked
   */
  @FXML
  private void onCancelButtonClicked() {
    // If selected event is not null
    if (selectedEvent != null) {
      // Remove event from current user's events organized list
      ((Manager) Account.currentUser).getEventsOrganized().remove(selectedEvent);
      // Remove event from events list
      Event.events.remove(selectedEvent);

      createNewsObject(selectedEvent.getName() + " has been cancelled");

      updateLists();
    }
  }

  /**
   * This handles what happens when the change name button is clicked
   */
  @FXML
  private void onChangeNameButtonClicked() {
    // If change name text field is not empty
    if (!changeNameTextField.getText().equals("")) {
      Team tempTeam = Team.findTeamByName(changeNameTextField.getText());

      // If team name is not taken
      if (tempTeam == null || tempTeam == Team.currentTeam) {
        // Set new team name
        Team.currentTeam.setName(changeNameTextField.getText());
        // Rename team profile pic file
        File newFile = new File
            (".\\src\\sample\\Images\\TeamProfilePics\\" + Team.currentTeam.getName() + ".png");
        Team.currentTeam.getProfilePicFile().renameTo(newFile);
      } else {
        MessageBoxController.messageString = "Team name is already taken.";
        changeNameTextField.setText("");
        loadPopUpScene("FXMLDocs/MessageBox.fxml", "");
      }
    }
  }

  /**
   * This handles what happens when the edit team image view button is clicked
   */
  @FXML
  private void editTeamImageView() {
    File file;
    // Set file to file selected from file chooser
    file = fileChooser.showOpenDialog(popUpStage);

    try {
      // If file is not null and file type is a supported type (at the moment only png)
      if (supportedImageType(file.getName())) {
        // Copy contents of file to current team's profile pic file
        FileManager.copyToFile(file, Team.currentTeam.getProfilePicFile());

        // Set profile pic using profile pic file
        Team.currentTeam.setProfilePic(ImageIO.read(Team.currentTeam.getProfilePicFile()));

        // Set the profile pic image view
        image = SwingFXUtils.toFXImage(Team.currentTeam.getProfilePic(), null);

        profilePic.setImage(image);
      } else {
        MessageBoxController.messageString = "Wrong file type.";
        changeNameTextField.setText("");
        loadPopUpScene("FXMLDocs/MessageBox.fxml", "");
      }
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /**
   * Create a news object
   *
   * @param news text of the news object created
   */
  private void createNewsObject(String news) {
    News tempNews = new News(news, new Date());
    tempNews.addTeamInvolved(((Manager) Account.currentUser).getTeam());

    News.newsList.add(tempNews);
  }

  /**
   * Update list views
   */
  private void updateLists() {
    selectedPlayer = null;
    selectedEvent = null;

    // Clear observable lists
    eventsObsList.clear();
    requestObsList.clear();
    teamObsList.clear();

    // Clear list views
    eventListView.getItems().clear();
    requestListView.getItems().clear();
    teamListView.getItems().clear();

    // Set list views
    if (((Manager) Account.currentUser).getEventsOrganized().size() > 0) {
      eventsObsList.addAll(((Manager) Account.currentUser).getEventsOrganized());
      eventListView.setItems(eventsObsList);
    }

    if (((Manager) Account.currentUser).getTeam().getJoinRequestsList().size() > 0) {
      requestObsList.addAll(((Manager) Account.currentUser).getTeam().getJoinRequestsList());
      requestListView.setItems(requestObsList);
    }

    if (((Manager) Account.currentUser).getTeam().getMembers().size() > 0) {
      teamObsList.addAll(((Manager) Account.currentUser).getTeam().getMembers());
      teamListView.setItems(teamObsList);
    }
  }
}
