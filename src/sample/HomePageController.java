package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomePageController extends Controller {

  @FXML
  private Label userName;

  @FXML
  private Button customizationButton;

  @FXML
  private Button followingButton;

  @FXML
  private Button edit_joinTeam;

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

  private Stage popUpStage;

  private String[] news = new String[4];

  private String[] events = new String[3];

  private ArrayList<News> specificNews = new ArrayList<>();

  private ArrayList<Event> specificEvents = new ArrayList<>();

  /***
   * This method initializes a pop up stage and enables/disables certain home page buttons depending
   * on the current user's account type
   */
  @FXML
  protected void initialize() {
    //Initialize pop up stage
    popUpStage = new Stage();
    popUpStage.initModality(Modality.APPLICATION_MODAL);

    //If user has yet to sign in
    if (Account.currentUser == null) {
      if (News.newsList.size() > 0) {
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

      if (Event.events.size() > 0) {
        Collections.sort(Event.events, Collections.reverseOrder());

        for (int i = 0; i < events.length; i++) {
          if (i < Event.events.size()) {
            if (i < News.newsList.size()) {
              events[i] = Event.events.get(i).toString();
            } else {
              events[i] = "";
            }
          }
        }
      } else {
        events[0] = "No upcoming events";
      }

      //Set username text as guest and disable the following buttons:
      userName.setText("Guest");
      customizationButton.setDisable(true);
      customizationButton.setVisible(false);

      followingButton.setDisable(true);
      followingButton.setVisible(false);

      edit_joinTeam.setDisable(true);
      edit_joinTeam.setVisible(false);
    } else {
      if (Account.currentUser instanceof Contributor) {
        for (News news : News.newsList) {
          if (news.teamIsInvolved(((Contributor) Account.currentUser).getTeam())) {
            specificNews.add(news);
          }
        }
      }

      if (News.newsList.size() > 0) {
        for (News news : News.newsList) {
          for (Team team : Account.currentUser.getTeamsFollowed()) {
            if (news.teamIsInvolved(team)) {
              specificNews.add(news);
            }
          }
        }

        if (specificNews.size() > 0) {
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

      if (Event.events.size() > 0) {
        for (Event event : Event.events) {
          for (Team team : Account.currentUser.getTeamsFollowed()) {
            if (event.teamIsInvolved(team)) {
              specificEvents.add(event);
            }
          }
        }

        if (specificEvents.size() > 0) {
          Collections.sort(specificEvents, Collections.reverseOrder());

          for (int i = 0; i < events.length; i++) {
            if (i < specificEvents.size()) {
              events[i] = specificEvents.get(i).toString();
            } else {
              events[i] = "";
            }
          }
        } else {
          events[0] = "No upcoming events in follow list";
        }
      }

      //If a user has signed in set username label to the username of the currently signed in
      //account
      userName.setText(Account.currentUser.getUsername());

      //Depending on the current user's account type disable the following buttons:
      switch (Account.currentUser.getAccountType()) {
        case SPECTATOR:
          edit_joinTeam.setDisable(true);
          edit_joinTeam.setVisible(false);
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
  }

  @FXML
  protected void onSearchButtonClicked() {
    for (Team team : Team.teams) {
      if (team.getName().toUpperCase().equals(teamSearchBar.getText().toUpperCase())) {
        Team.currentTeam = team;
        try {
          changeScene("TeamPage");
        } catch (Exception e) {
          System.out.println(team);
        }
        break;
      }
    }
  }

  /***
   * This handles what happens when the sign in button is clicked
   */
  @FXML
  protected void handleLoginButtonAction() {
    try {
      //Go to login scene
      changeScene("Login");
    } catch (Exception e) {
      System.out.println("Exception caught");
    }
  }

  /***
   * This handles what happens when the following button is clicked
   *
   * @throws IOException
   */
  @FXML
  protected void handleFollowingButtonAction() throws IOException {
    loadPopUpScene("FXMLDocs/TeamsFollowedList.fxml", "Following");
  }

  /***
   * This handles what happens when the following button is clicked
   *
   * @throws IOException
   */
  @FXML
  protected void handleEditAndJoinTeamButtonAction() throws IOException {
    //Depending on the account type of the current user difference scenes will pop up
    switch (Account.currentUser.getAccountType()) {
      case PLAYER:
        if (edit_joinTeam.getText().equals("Team")) {
          Team.currentTeam = ((Player) Account.currentUser).getTeam();
          try {
            changeScene("TeamPage");
          } catch (Exception e) {
            System.out.println("Exception caught");
          }
        } else {
          loadPopUpScene("FXMLDocs/JoinTeam.fxml", "Enter Team Name");
        }
        break;
      case MANAGER:
        Team.currentTeam = ((Manager) Account.currentUser).getTeam();
        try {
          changeScene("TeamPage");
        } catch (Exception e) {
          System.out.println("Exception caught");
        }
        break;
      default:
        break;
    }
  }

  /***
   * This method loads in a pop up scene
   *
   * @param fxmlURL the URL of the fxml file that will be used for the scene
   * @param sceneTitle title of the scene
   * @throws IOException
   */
  private void loadPopUpScene(String fxmlURL, String sceneTitle) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource(fxmlURL));

    Scene popUpScene = new Scene(root);

    popUpStage.setScene(popUpScene);
    popUpStage.setTitle(sceneTitle);
    popUpStage.setResizable(false);

    popUpStage.show();
  }

  public void updateJoinTeamButton() {
    if (Account.currentUser instanceof Player) {
      if (((Player) Account.currentUser).getTeam() != null) {
        edit_joinTeam.setDisable(false);
        edit_joinTeam.setText("Team");
      } else {
        if (((Player) Account.currentUser).getTeamRequested() != null) {
          edit_joinTeam.setDisable(true);
          edit_joinTeam.setText("Request Pending");
        } else {
          edit_joinTeam.setDisable(false);
          edit_joinTeam.setText("Join Team");
        }
      }

      edit_joinTeam.setVisible(true);
    }
  }
}
