package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
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

  private ArrayList<News> teamNewsList = new ArrayList<>();

  private ObservableList commentsObsList = FXCollections.observableArrayList();

  private ObservableList memebersObsList = FXCollections.observableArrayList();

  private ObservableList newsObsList = FXCollections.observableArrayList();

  private ObservableList eventsObsList = FXCollections.observableArrayList();

  @FXML
  protected void initialize() {
    if (Account.currentUser != null) {
      if (Account.currentUser instanceof Contributor &&
          ((Contributor) Account.currentUser).getTeam() == Team.currentTeam) {
        if (Account.currentUser instanceof Manager) {
          follow_Edit_Button.setText("Edit");
        } else {
          follow_Edit_Button.setDisable(true);
          follow_Edit_Button.setVisible(false);
        }
      } else {
        if (Account.currentUser.getTeamsFollowed().contains(Team.currentTeam)) {
          follow_Edit_Button.setText("Unfollow");
        } else {
          follow_Edit_Button.setText("Follow");
        }
      }
    } else {
      follow_Edit_Button.setDisable(true);
      follow_Edit_Button.setVisible(false);
    }

    teamName.setText(Team.currentTeam.getName());

    for (News news : News.newsList) {
      if (news.teamIsInvolved(Team.currentTeam)) {
        teamNewsList.add(news);
      }
    }

    Collections.sort(teamNewsList, Collections.reverseOrder());
    Collections.sort(Team.currentTeam.getManager().getEventsOrganized());

    commentsObsList.addAll(Team.currentTeam.getComments());
    commentsListView.setItems(commentsObsList);

    commentsListView.setCellFactory(new Callback<ListView<Comments>, ListCell<Comments>>() {
      @Override
      public ListCell<Comments> call(ListView<Comments> param) {
        return new ListCell<Comments>() {
          {
            //Set item width property to the width of the  list view minus an offest
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

    memebersObsList.add(Team.currentTeam.getManager());
    memebersObsList.addAll(Team.currentTeam.getMembers());

    membersListView.setItems(memebersObsList);

    newsObsList.addAll(teamNewsList);
    newsListView.setItems(newsObsList);

    eventsObsList.addAll(Team.currentTeam.getManager().getEventsOrganized());
    eventsListView.setItems(eventsObsList);

    eventsListView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<Event>() {
          @Override
          public void changed(ObservableValue<? extends Event> observable, Event oldValue,
              Event newValue) {
            Event.currentEvent = newValue;

            try {
              EventPageController.prevSceneKey = "TeamPage";
              changeScene("EventPage");
            } catch (Exception e){
              e.printStackTrace();
            }
          }
        });
  }

  @FXML
  protected void onFollow_Edit_ButtonClicked() {
    if (follow_Edit_Button.getText().equals("Edit")) {
      try {
        changeScene("EditTeamPage");
      } catch (Exception e) {
        System.out.println("Exception Caught");
      }
    } else {
      if (Account.currentUser.getTeamsFollowed().contains(Team.currentTeam)) {
        Account.currentUser.unfollowTeam(Team.currentTeam);
        follow_Edit_Button.setText("Follow");
      } else {
        Account.currentUser.followTeam(Team.currentTeam);
        follow_Edit_Button.setText("Unfollow");
      }
    }
  }

  @FXML
  protected void onPostButtonClicked() {
    if (!commentBox.getText().equals("")) {
      String comment;

      if (Account.currentUser == null) {
        comment = "[Guest]: " + commentBox.getText();
      } else {
        comment = "[" + Account.currentUser.getUsername() + "]: " + commentBox.getText();
      }

      Team.currentTeam.getComments().add(new Comments(comment, new Date()));

      commentsObsList.add
          (Team.currentTeam.getComments().get(Team.currentTeam.getComments().size() - 1));

      commentsListView.setItems(commentsObsList);

      commentBox.setText("");
    }
  }

  @FXML
  protected void onHomePageButtonClicked() {
    try {
      changeScene("HomePageController");
    } catch (Exception e) {
      System.out.println("Exception Caught");
    }
  }
}
