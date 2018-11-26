package sample;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class EventPageController extends Controller {

  public static String prevSceneKey;

  private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy h:mm a");

  ObservableList team1ObsList = FXCollections.observableArrayList();

  ObservableList team2ObsList = FXCollections.observableArrayList();

  @FXML
  private Label team1Label;

  @FXML
  private Label eventName;

  @FXML
  private Label eventAttributesLabel;

  @FXML
  private Label team2Label;

  @FXML
  private Label team1Score;

  @FXML
  private Label team2Score;

  @FXML
  private Label eventDescriptionLabel;

  @FXML
  private Button decrementScore1Button;

  @FXML
  private Button decrementScore2Button;

  @FXML
  private Button incrementScore1Button;

  @FXML
  private Button incrementScore2Button;

  @FXML
  private Button deactivateButton;

  @FXML
  private ListView<Account> team1ListView;

  @FXML
  private ListView<Account> team2ListView;

  @FXML
  private void initialize() {
    // If current user is not event organizer or event is not active
    if (Account.currentUser != Event.currentEvent.getOrganizer() ||
        !Event.currentEvent.isActive()) {
      deactivateButton.setDisable(true);
      deactivateButton.setVisible(false);

      decrementScore1Button.setDisable(true);
      decrementScore2Button.setDisable(true);
      incrementScore1Button.setDisable(true);
      incrementScore2Button.setDisable(true);

      decrementScore1Button.setVisible(false);
      decrementScore2Button.setVisible(false);
      incrementScore1Button.setVisible(false);
      incrementScore2Button.setVisible(false);
    }

    // Set labels to current event attributes
    eventName.setText(Event.currentEvent.getName());
    eventAttributesLabel.setText(Event.currentEvent.getLocation() + ", " +
        dateFormatter.format(Event.currentEvent.getStartDateDate()) + ", ");

    Date today = new Date();

    if (today.before(Event.currentEvent.getStartDateDate())) {
      eventAttributesLabel.setText(eventAttributesLabel.getText() + "Upcoming");
    } else if (today.after(Event.currentEvent.getStartDateDate()) &&
        today.before(Event.currentEvent.getEndDateDate())) {
      eventAttributesLabel.setText(eventAttributesLabel.getText() + "Ongoing");
    } else if (today.after(Event.currentEvent.getEndDateDate())) {
      eventAttributesLabel.setText(eventAttributesLabel.getText() + "Ended");
    }

    team1Label.setText(Event.currentEvent.getTeamsInvolved()[0].getName());
    team2Label.setText(Event.currentEvent.getTeamsInvolved()[1].getName());

    team1Score.setText("" + Event.currentEvent.getEventScore().getScore(0));
    team2Score.setText("" + Event.currentEvent.getEventScore().getScore(1));

    eventDescriptionLabel.setText(Event.currentEvent.getDescription());

    // Set list view for team members
    team1ObsList.addAll(Event.currentEvent.getTeamsInvolved()[0].getMembers());
    team2ObsList.addAll(Event.currentEvent.getTeamsInvolved()[1].getMembers());

    team1ListView.setItems(team1ObsList);
    team2ListView.setItems(team2ObsList);
  }

  /**
   * This handles what happens when the back button is clicked
   */
  @FXML
  private void onBackButtonClicked() {
      changeScene(prevSceneKey);
  }

  /**
   * This handles what happens when the team1 increment button is clicked
   */
  @FXML
  private void onIncrementScore1ButtonClicked() {
    Event.currentEvent.getEventScore().incrementScore(0);
    team1Score.setText("" + Event.currentEvent.getEventScore().getScore(0));
  }

  /**
   * This handles what happens when the team2 increment button is clicked
   */
  @FXML
  private void onIncrementScore2ButtonClicked() {
    Event.currentEvent.getEventScore().incrementScore(1);
    team2Score.setText("" + Event.currentEvent.getEventScore().getScore(1));
  }

  /**
   * This handles what happens when the team1 decrement button is clicked
   */
  @FXML
  private void onDecrementScore1ButtonClicked() {
    if (Event.currentEvent.getEventScore().getScore(0) > 0) {
      Event.currentEvent.getEventScore().decrementScore(0);
      team1Score.setText("" + Event.currentEvent.getEventScore().getScore(0));
    }
  }

  /**
   * This handles what happens when the team2 decrement button is clicked
   */
  @FXML
  void onDecrementScore2ButtonClicked() {
    if (Event.currentEvent.getEventScore().getScore(1) > 0) {
      Event.currentEvent.getEventScore().decrementScore(1);
      team2Score.setText("" + Event.currentEvent.getEventScore().getScore(1));
    }
  }

  /**
   * This handles what happens when the deactivate button is clicked
   */
  @FXML
  private void onDeactivateButtonClicked() {
    Event.currentEvent.setActive(false);

    deactivateButton.setDisable(true);
    deactivateButton.setVisible(false);

    decrementScore1Button.setDisable(true);
    decrementScore2Button.setDisable(true);
    incrementScore1Button.setDisable(true);
    incrementScore2Button.setDisable(true);

    decrementScore1Button.setVisible(false);
    decrementScore2Button.setVisible(false);
    incrementScore1Button.setVisible(false);
    incrementScore2Button.setVisible(false);

    News tempNews = new News(
        "The " + Event.currentEvent.getEventWinner() + " have won the event " + eventName.getText(),
        new Date());

    tempNews.addTeamInvolved(Event.currentEvent.getTeamsInvolved()[0]);
    tempNews.addTeamInvolved(Event.currentEvent.getTeamsInvolved()[1]);

    News.newsList.add(tempNews);
  }
}
