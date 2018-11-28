package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TeamPageController extends Controller {

  @FXML
  private ListView<Comments> commentsListView;

  @FXML
  private ListView<Contributor> membersListView;

  @FXML
  private ListView<Event> eventsListView;

  @FXML
  private ListView<News> newsListView;

  @FXML
  private TextArea commentBox;

  @FXML
  private Text teamName;

  @FXML
  private Button follow_Edit_Button;

  @FXML
  private Circle profilePic;

  private Image image;

  private ArrayList<News> teamNewsList = new ArrayList<>();

  private ObservableList commentsObsList = FXCollections.observableArrayList();

  private ObservableList memebersObsList = FXCollections.observableArrayList();

  private ObservableList newsObsList = FXCollections.observableArrayList();

  private ObservableList eventsObsList = FXCollections.observableArrayList();

  @FXML
  private void initialize() {
    // Initialize pop up stage
    popUpStage = new Stage();
    popUpStage.initModality(Modality.APPLICATION_MODAL);

    // Set image view profile pic
    image = SwingFXUtils.toFXImage(Team.currentTeam.getProfilePic(), null);
    profilePic.setFill(new ImagePattern(image));

    // If current user is not null
    if (Account.currentUser != null) {
      // If current user is a contributor and account's team is the current team
      if (Account.currentUser instanceof Contributor &&
          ((Contributor) Account.currentUser).getTeam() == Team.currentTeam) {
        // If current account is a manager
        if (Account.currentUser instanceof Manager) {
          // Set text of follow/edit button to edit
          follow_Edit_Button.setText("Edit");
        } else {
          // Disable follow/edit button and set it to invisible
          follow_Edit_Button.setDisable(true);
          follow_Edit_Button.setVisible(false);
        }
      } else {
        // If current user follows the current team
        if (Account.currentUser.getTeamsFollowed().contains(Team.currentTeam)) {
          // Set text of follow/edit button to unfollow
          follow_Edit_Button.setText("Unfollow");
        } else {
          // Set text of follow/edit button to follow
          follow_Edit_Button.setText("Follow");
        }
      }
    } else {
      // Disable follow/edit button and set it to invisible
      follow_Edit_Button.setDisable(true);
      follow_Edit_Button.setVisible(false);
    }

    // Set team name text to current team's name
    teamName.setText(Team.currentTeam.getName());

    // For every news in news list
    for (News news : News.newsList) {
      // If current teams is involved in team add news to team news list
      if (news.teamIsInvolved(Team.currentTeam)) {
        teamNewsList.add(news);
      }
    }

    //Sort team news list in reverse order
    Collections.sort(teamNewsList, Collections.reverseOrder());

    // Set comments list view
    commentsObsList.addAll(Team.currentTeam.getComments());
    commentsListView.setItems(commentsObsList);

    commentsListView.setCellFactory(new Callback<ListView<Comments>, ListCell<Comments>>() {
      @Override
      public ListCell<Comments> call(ListView<Comments> param) {
        return new ListCell<Comments>() {
          {
            //Set item width property to the width of the  list view minus an offset
            prefWidthProperty().bind(commentsListView.widthProperty().subtract(20));
          }

          @Override
          protected void updateItem(Comments item, boolean empty) {
            if (item != null && !empty) {
              this.setWrapText(true);
              setText(item.getText());
            } else {
              setText(null);
            }
          }
        };
      }
    });

    // Set members list view
    memebersObsList.add(Team.currentTeam.getManager());
    memebersObsList.addAll(Team.currentTeam.getMembers());

    membersListView.setItems(memebersObsList);

    // Set news list view
    newsObsList.addAll(teamNewsList);

    Collections.sort(newsObsList, Collections.reverseOrder());

    newsListView.setItems(newsObsList);

    Date today = new Date();

    // For every event in events list
    for (Event event : Event.events) {
      // If current team is involved in event and starts after current date and time
      if (event.teamIsInvolved(Team.currentTeam) && event.getEndDateDate().after(today)) {
        // Add event to observable list
        eventsObsList.add(event);
      }
    }

    // Set events list view
    Collections.sort(eventsObsList);

    eventsListView.setItems(eventsObsList);

    // When events list view item is selected
    eventsListView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          Event.currentEvent = newValue;

          EventPageController.prevSceneKey = "TeamPage";
          changeScene("EventPage");
        });
  }

  /**
   * This handles what happens when the follow/edit button is clicked
   */
  @FXML
  private void onFollow_Edit_ButtonClicked() {
    // If button's text is set to Edit
    if (follow_Edit_Button.getText().equals("Edit")) {
      changeScene("EditTeamPage");
    } else {
      // If current user follows current team
      if (Account.currentUser.getTeamsFollowed().contains(Team.currentTeam)) {
        Account.currentUser.unfollowTeam(Team.currentTeam);
        // Set text to Follow
        follow_Edit_Button.setText("Follow");
      } else {
        // Add current team to current user's follow list
        Account.currentUser.followTeam(Team.currentTeam);
        // Set text to Unfollow
        follow_Edit_Button.setText("Unfollow");
      }
    }
  }

  /**
   * This handles what happens when the post button is clicked
   */
  @FXML
  private void onPostButtonClicked() {
    // If comment box is not empty
    if (!commentBox.getText().equals("")) {
      String comment;

      // If current user is a guest
      if (Account.currentUser == null) {
        comment = "[Guest]: " + commentBox.getText();
      } else {
        comment = "[" + Account.currentUser.getUsername() + "]: " + commentBox.getText();
      }

      // Add comment to current team's comments list
      Team.currentTeam.getComments().add(new Comments(comment, new Date()));

      // Set comments list view
      commentsObsList.add
          (Team.currentTeam.getComments().get(Team.currentTeam.getComments().size() - 1));

      commentsListView.setItems(commentsObsList);

      // Empty the comment box
      commentBox.setText("");
    }
  }

  /**
   * This handles what happens when the home page button is clicked
   */
  @FXML
  private void onHomePageButtonClicked() {
    changeScene("HomePageController");
  }

  /**
   * This handles what happens when the view all events button is clicked
   */
  @FXML
  private void onViewAllEventsButtonClicked() {
    loadPopUpScene("FXMLDocs/EventsList.fxml", "Events");
    EventPageController.prevSceneKey = "TeamPage";
  }
}
