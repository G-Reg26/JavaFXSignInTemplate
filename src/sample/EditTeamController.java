package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class EditTeamController extends Controller {
  @FXML
  private ListView<Event> eventListView;

  @FXML
  private ListView<Player> teamListView;

  @FXML
  private ListView<Player> requestListView;

  private ObservableList eventsObsList = FXCollections.observableArrayList();

  private ObservableList requestObsList = FXCollections.observableArrayList();

  private ObservableList teamObsList = FXCollections.observableArrayList();

  Player selectedPlayer;

  Event selectedEvent;

  @FXML
  protected void initialize() {
    updateLists();

    teamListView.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<Player>() {
          @Override
          public void changed(ObservableValue<? extends Player> observable, Player oldValue,
              Player newValue) {
            selectedPlayer = newValue;
          }
        });

    requestListView.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<Player>() {
          @Override
          public void changed(ObservableValue<? extends Player> observable, Player oldValue,
              Player newValue) {
            selectedPlayer = newValue;
          }
        });

    eventListView.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<Event>() {
          @Override
          public void changed(ObservableValue<? extends Event> observable, Event oldValue,
              Event newValue) {
            selectedEvent = newValue;
          }
        });
  }

  @FXML
  void onTeamPageButtonClicked() {
    try {
      changeScene("TeamPage");
    } catch (Exception e) {
      System.out.println("Exception Caught");
    }
  }

  @FXML
  void onAcceptButtonClicked() {
    if (selectedPlayer != null) {
      ((Manager) Account.currentUser).getTeam().addPlayer(selectedPlayer);
      selectedPlayer.setTeam(((Manager) Account.currentUser).getTeam());
      updateLists();
    }
  }

  @FXML
  void onDenyButtonClicked() {
    if (selectedPlayer != null) {
      ((Manager) Account.currentUser).getTeam().getJoinRequestsList().remove(selectedPlayer);
      selectedPlayer.setTeamRequested(null);
      updateLists();
    }
  }

  @FXML
  void onRemoveButtonClicked() {
    if (selectedPlayer != null) {
      ((Manager) Account.currentUser).getTeam().removePlayer(selectedPlayer);
      selectedPlayer.setTeam(null);
      updateLists();
    }
  }

  @FXML
  void onCreateButtonClicked() {
    try {
      changeScene("CreateEvent");
    } catch (Exception e) {
      System.out.println("Exception caught");
    }
  }

  @FXML
  void onEditButtonClicked(){
    if (selectedEvent != null) {
      Event.currentEvent = selectedEvent;
      CreateEventController.isEditiing(true);
      try {
        changeScene("CreateEvent");
      } catch (Exception e) {
        System.out.println("Exception caught");
      }
    }
  }

  @FXML
  void tabChanged() {
    selectedPlayer = null;
  }

  public void updateLists() {
    selectedPlayer = null;
    selectedEvent = null;

    eventsObsList.clear();
    requestObsList.clear();
    teamObsList.clear();

    eventListView.getItems().clear();
    requestListView.getItems().clear();
    teamListView.getItems().clear();

    if (((Manager)Account.currentUser).getEventsOrganized().size() > 0) {
      eventsObsList.addAll(((Manager) Account.currentUser).getEventsOrganized());
      eventListView.setItems(eventsObsList);
    }

    if (((Manager)Account.currentUser).getTeam().getJoinRequestsList().size() > 0) {
      requestObsList.addAll(((Manager) Account.currentUser).getTeam().getJoinRequestsList());
      requestListView.setItems(requestObsList);
    }

    if (((Manager)Account.currentUser).getTeam().getMembers().size() > 0) {
      teamObsList.addAll(((Manager) Account.currentUser).getTeam().getMembers());
      teamListView.setItems(teamObsList);
    }
  }
}
