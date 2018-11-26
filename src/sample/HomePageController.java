package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomePageController extends Controller {

  @FXML
  private Label userName;

  @FXML
  private Button followingButton;

  @FXML
  private Button team_joinTeamButton;

  @FXML
  private Button editProfileButton;

  @FXML
  private Label currentEventName;

  @FXML
  private Label team1Score;

  @FXML
  private Label team2Score;

  @FXML
  private Text news1;

  @FXML
  private Text news2;

  @FXML
  private Text news3;

  @FXML
  private Text news4;

  @FXML
  private Text upcomingEvent1;

  @FXML
  private Text upcomingEvent2;

  @FXML
  private Text upcomingEvent3;

  @FXML
  private TextField teamSearchBar;

  @FXML
  private ContextMenu contextMenu;

  @FXML
  private ImageView profilePic;

  private Image image;

  private String[] news = new String[4];

  private String[] events = new String[3];

  private ArrayList<News> specificNews = new ArrayList<>();

  private ArrayList<Event> upcomingEvents = new ArrayList<>();

  private Date today;

  private float xOffset;

  private float yOffset;

  @FXML
  private void initialize() {
    // Initialize pop up stage
    popUpStage = new Stage();
    popUpStage.initModality(Modality.APPLICATION_MODAL);

    today = new Date();

    // If user has yet to sign in
    if (Account.currentUser == null) {
      // If news list is not empty
      if (News.newsList.size() > 0) {
        // Fill news text nodes with the five latest news headlines
        Collections.sort(News.newsList, Collections.reverseOrder());

        for (int i = 0; i < news.length; i++) {
          if (i < News.newsList.size()) {
            news[i] = News.newsList.get(i).getNews();
          } else {
            news[i] = "";
          }
        }
      } else {
        news[0] = "No recent news";
      }

      // If there's at least one event
      if (Event.events.size() > 0) {
        // Fill list of upcoming events with events that occur after today
        Collections.sort(Event.events);

        for (Event event : Event.events) {
          if (event.getEndDateDate().after(today)) {
            upcomingEvents.add(event);
          }
        }

        // Fill events text nodes with the five latest upcoming events
        for (int i = 0; i < events.length; i++) {
          if (i < upcomingEvents.size()) {
            events[i] = upcomingEvents.get(i).toString();
          } else {
            events[i] = "";
          }
        }

        // If upcoming events list is not empty
        if (upcomingEvents.size() > 0) {
          currentEventName.setText(upcomingEvents.get(0).getName());
          team1Score.setText("T1:" + upcomingEvents.get(0).getEventScore().getScore(0));
          team2Score.setText("T2:" + upcomingEvents.get(0).getEventScore().getScore(1));
        }
      } else {
        events[0] = "No upcoming events";
        currentEventName.setText("No upcoming events");
        team1Score.setText("");
        team2Score.setText("");
      }

      // Set username text as guest and disable the following buttons:
      userName.setText("Guest");

      followingButton.setDisable(true);
      followingButton.setVisible(false);

      team_joinTeamButton.setDisable(true);
      team_joinTeamButton.setVisible(false);

      editProfileButton.setDisable(true);
      editProfileButton.setVisible(false);

    } else {
      // Set profile pic image view
      image = SwingFXUtils.toFXImage(Account.currentUser.getProfilePic(), null);
      profilePic.setImage(image);

      // If news list is not empty
      if (News.newsList.size() > 0) {
        // If current user is a contributor
        if (Account.currentUser instanceof Contributor) {
          // Add any news that contributor's team is involved with to the specific news list
          for (News news : News.newsList) {
            if (news.teamIsInvolved(((Contributor) Account.currentUser).getTeam())) {
              specificNews.add(news);
            }
          }
        }

        // For all news in news list
        for (News news : News.newsList) {
          // For all teams in current user's follow list
          for (Team team : Account.currentUser.getTeamsFollowed()) {
            // If team is involved in news add it to the specific news list
            if (news.teamIsInvolved(team)) {
              specificNews.add(news);
            }
          }
        }

        // If specific news is not empty
        if (specificNews.size() > 0) {
          // Fill news text nodes with the five latest news headlines in specific news list
          Collections.sort(specificNews, Collections.reverseOrder());

          for (int i = 0; i < news.length; i++) {
            if (i < specificNews.size()) {
              news[i] = specificNews.get(i).getNews();
            } else {
              news[i] = "";
            }
          }
        } else {
          news[0] = "No recent news from follow list";
        }
      }

      // If events list is not empty
      if (Event.events.size() > 0) {
        // If current user is a contributor
        if (Account.currentUser instanceof Contributor) {
          // For all events in events list
          for (Event event : Event.events) {
            // If contributor's team is involved in event and event occurs after today
            if (event.teamIsInvolved(((Contributor) Account.currentUser).getTeam()) &&
                event.getEndDateDate().after(today)) {
              // Add event to upcoming events list
              upcomingEvents.add(event);
            }
          }
        }

        // For all events in events list
        for (Event event : Event.events) {
          // For all teams in current user's follow list
          for (Team team : Account.currentUser.getTeamsFollowed()) {
            // If team is involved in event and occurs after today add event to upcoming events list
            if (event.teamIsInvolved(team) && event.getEndDateDate().after(today)) {
              upcomingEvents.add(event);
            }
          }
        }

        // If upcoming events is not empty
        if (upcomingEvents.size() > 0) {
          // Fill events text nodes with the five latest upcoming events
          Collections.sort(upcomingEvents);

          for (int i = 0; i < events.length; i++) {
            if (i < upcomingEvents.size()) {
              events[i] = upcomingEvents.get(i).toString();
            } else {
              events[i] = "";
            }
          }

          currentEventName.setText(upcomingEvents.get(0).getName());
          team1Score.setText("T1:" + upcomingEvents.get(0).getEventScore().getScore(0));
          team2Score.setText("T2:" + upcomingEvents.get(0).getEventScore().getScore(1));
        } else {
          events[0] = "No upcoming events in follow list";
          currentEventName.setText("No upcoming events");
          team1Score.setText("");
          team2Score.setText("");
        }
      } else {
        events[0] = "No upcoming events in follow list";
        currentEventName.setText("No upcoming events");
        team1Score.setText("");
        team2Score.setText("");
      }

      // If a user has signed in set username label to the username of the currently signed in
      // account
      userName.setText(Account.currentUser.getUsername());

      // Depending on the current user's account type disable the following buttons:
      switch (Account.currentUser.getAccountType()) {
        case SPECTATOR:
          team_joinTeamButton.setDisable(true);
          team_joinTeamButton.setVisible(false);
          break;
        case PLAYER:
          updateJoinTeamButton();
          break;
        case MANAGER:
          break;
      }
    }

    news1.setText(news[0]);
    news2.setText(news[1]);
    news3.setText(news[2]);
    news4.setText(news[3]);

    upcomingEvent1.setText(events[0]);
    upcomingEvent2.setText(events[1]);
    upcomingEvent3.setText(events[2]);

    xOffset = 450.0f;
    yOffset = 65.0f;

    setTeamSearchBarDropDownMenu(teamSearchBar, contextMenu, xOffset, yOffset);
  }

  /**
   * This handles what happens when the search button is clicked
   */
  @FXML
  private void onSearchButtonClicked() {
    // For every team in teams list
    for (Team team : Team.teams) {
      // If team name equals the text in team search bar
      if (team.getName().toUpperCase().equals(teamSearchBar.getText().toUpperCase())) {
        Team.currentTeam = team;
        changeScene("TeamPage");
        break;
      }
    }
  }

  /**
   * This handles what happens when the sign in button is clicked
   */
  @FXML
  private void onLoginButtonClicked() {
    changeScene("Login");
  }

  /**
   * This handles what happens when the following button is clicked
   */
  @FXML
  private void onFollowingButtonClicked() {
    loadPopUpScene("FXMLDocs/TeamsFollowedList.fxml", "Following");
  }

  /**
   * This handles what happens when the team/join team button is clicked
   */
  @FXML
  private void onTeamAndJoinTeamButtonClicked() {
    //Depending on the account type of the current user difference scenes will pop up
    switch (Account.currentUser.getAccountType()) {
      case PLAYER:
        // If current user is part of team
        if (team_joinTeamButton.getText().equals("Team")) {
          Team.currentTeam = ((Player) Account.currentUser).getTeam();
          changeScene("TeamPage");
        } else {
          loadPopUpScene("FXMLDocs/JoinTeam.fxml", "Enter Team Name");
        }
        break;
      case MANAGER:
        Team.currentTeam = ((Manager) Account.currentUser).getTeam();
        changeScene("TeamPage");
        break;
      default:
        break;
    }
  }

  /**
   * This handles what happens when the edit profile button is clicked
   */
  @FXML
  private void onEditProfileButtonClicked() {
    // Profile is being edited
    CreateAccountController.editing = true;
    CreateAccountController.prevSceneKey = "HomePageController";
    changeScene("CreateAccount");
  }

  /**
   * This handles what happens when the view all events button is clicked
   */
  @FXML
  private void onViewAllEventsButtonClicked() {
    loadPopUpScene("FXMLDocs/EventsList.fxml", "Events");
    EventPageController.prevSceneKey = "HomePageController";
  }

  /**
   * Show context menu at specific coordinates, this is so the context menu stays at the same
   * position regardless of where user right clicks the textfield
   */
  @FXML
  private void onShowingContextMenu() {
    contextMenu.setAnchorX(teamSearchBar.getScene().getWindow().getX() + xOffset);
    contextMenu.setAnchorY(teamSearchBar.getScene().getWindow().getY() + yOffset);
  }

  /**
   * Set text and visibility of the team/join team button
   */
  protected void updateJoinTeamButton() {
    if (Account.currentUser instanceof Player) {
      if (((Player) Account.currentUser).getTeam() != null) {
        team_joinTeamButton.setDisable(false);
        team_joinTeamButton.setText("Team");
      } else {
        if (((Player) Account.currentUser).getTeamRequested() != null) {
          team_joinTeamButton.setDisable(true);
          team_joinTeamButton.setText("Request Pending");
        } else {
          team_joinTeamButton.setDisable(false);
          team_joinTeamButton.setText("Join Team");
        }
      }

      team_joinTeamButton.setVisible(true);
    }
  }
}
