package sample;

import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class EventsListController extends Controller {

  private ObservableList list = FXCollections.observableArrayList();

  @FXML
  private ListView<Event> listView;

  @FXML
  protected void initialize() {
    // If current team is null
    if (Team.currentTeam == null) {
      // If current user is null
      if (Account.currentUser == null) {
        // Add all events to list
        list.addAll(Event.events);
      } else {
        // For every event in events list
        for (Event event : Event.events) {
          // For every team in current account's follow list
          for (Team team : Account.currentUser.getTeamsFollowed()) {
            // If team is involved in event add events to list
            if (event.teamIsInvolved(team)) {
              list.add(event);
            }
          }
        }

        // If current user is a contributor
        if (Account.currentUser instanceof Contributor) {
          // For every event in events list
          for (Event event : Event.events) {
            // If current user's team is involved in event add events to list
            if (event.teamIsInvolved(((Contributor) Account.currentUser).getTeam())) {
              list.add(event);
            }
          }
        }
      }
    } else {
      // For every event in events list
      for (Event event : Event.events) {
        // If current team is involved in event add event to events list
        if (event.teamIsInvolved(Team.currentTeam)) {
          list.add(event);
        }
      }
    }

    Collections.sort(list);

    // Set items in list view to the items in the observable list
    listView.setItems(list);
    // When an item in the list is clicked
    listView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          //Set current team to the
          Event.currentEvent = newValue;

          changeScene("EventPage");

          Stage stage = (Stage) listView.getScene().getWindow();
          stage.close();
        });
  }
}
