package sample;

import java.io.BufferedReader;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EventPageController extends Controller{

  public static String prevSceneKey;

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
  private Button decrementScore1Button;

  @FXML
  private Button decrementScore2Button;

  @FXML
  private Button incrementScore1Button;

  @FXML
  private Button incrementScore2Button;

  @FXML
  void initialize() {
    if (Account.currentUser != Event.currentEvent.getOrganizer()) {
      decrementScore1Button.setDisable(true);
      decrementScore2Button.setDisable(true);
      incrementScore1Button.setDisable(true);
      incrementScore2Button.setDisable(true);

      decrementScore1Button.setVisible(false);
      decrementScore2Button.setVisible(false);
    }
    eventName.setText(Event.currentEvent.getName());
    eventAttributesLabel.setText(Event.currentEvent.getLocation() + ", " +
        Event.currentEvent.getStartDateDate() + ", ");

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
  }

  @FXML
  void backButtonClicked() {
    try {
      changeScene(prevSceneKey);
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  @FXML
  void onTeam1IncrementScore() {
    Event.currentEvent.getEventScore().incrementScore(0);
    team1Score.setText("" + Event.currentEvent.getEventScore().getScore(0));
  }

  @FXML
  void onTeam2IncrementScore() {
    Event.currentEvent.getEventScore().incrementScore(1);
    team2Score.setText("" + Event.currentEvent.getEventScore().getScore(1));
  }

  @FXML
  void onDecrementScore1ButtonClicked(){
    Event.currentEvent.getEventScore().decrementScore(0);
    team1Score.setText("" + Event.currentEvent.getEventScore().getScore(0));
  }

  @FXML
  void onDecrementScore2ButtonClicked(){
    Event.currentEvent.getEventScore().decrementScore(1);
    team2Score.setText("" + Event.currentEvent.getEventScore().getScore(1));
  }

}
