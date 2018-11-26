package sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CreateEventController extends Controller {

  public static boolean editing;

  private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM dd, yyyy h:mm a");

  private ObservableList<Team> teamsList = FXCollections.observableArrayList();

  @FXML
  private ComboBox<Team> teamsComboBox;

  @FXML
  private Text messageNode;

  @FXML
  private MenuButton startMenuButton;

  @FXML
  private MenuButton endMenuButton;

  @FXML
  private MenuItem startPMMenuItem;

  @FXML
  private MenuItem startAMMenuItem;

  @FXML
  private MenuItem endPMMenuItem;

  @FXML
  private MenuItem endAMMenuItem;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private TextField eventNameTextField;

  @FXML
  private TextField locationTextField;

  @FXML
  private TextField startTimeHourTextField;

  @FXML
  private TextField startTimeMinuteTextField;

  @FXML
  private TextField endTimeHourTextField;

  @FXML
  private TextField endTimeMinuteTextField;

  @FXML
  private TextArea descriptionTextArea;

  @FXML
  private void initialize() {
    // Add text field nodes to textFields
    textFields.add(eventNameTextField);
    textFields.add(locationTextField);
    textFields.add(startTimeHourTextField);
    textFields.add(startTimeMinuteTextField);
    textFields.add(endTimeHourTextField);
    textFields.add(endTimeMinuteTextField);

    // Set on action to all menu items
    startAMMenuItem.setOnAction(event -> startMenuButton.setText(startAMMenuItem.getText()));

    startPMMenuItem.setOnAction(event -> startMenuButton.setText(startPMMenuItem.getText()));

    endAMMenuItem.setOnAction(event -> endMenuButton.setText(endAMMenuItem.getText()));

    endPMMenuItem.setOnAction(event -> endMenuButton.setText(endPMMenuItem.getText()));

    // Add all teams to team list except the current user's team
    teamsList.addAll(Team.teams);
    teamsList.remove(((Manager) Account.currentUser).getTeam());

    // Set items of the combo box
    teamsComboBox.setItems(teamsList);

    // If event is being edited
    if (editing) {
      // Set fields to the current event's values
      eventNameTextField.setText(Event.currentEvent.getName());
      locationTextField.setText(Event.currentEvent.getLocation());

      Date startDate = Event.currentEvent.getStartDateDate();

      startDatePicker.setValue
          (LocalDate.of(startDate.getYear() + 1900, startDate.getMonth() + 1, startDate.getDate()));

      setHourTextField(startDate.getHours(), startTimeHourTextField, startMenuButton);
      startTimeMinuteTextField.setText("" + startDate.getMinutes());

      Date endDate = Event.currentEvent.getEndDateDate();

      endDatePicker.setValue
          (LocalDate.of(endDate.getYear() + 1900, endDate.getMonth() + 1, endDate.getDate()));

      setHourTextField(endDate.getHours(), endTimeHourTextField, endMenuButton);
      endTimeMinuteTextField.setText("" + endDate.getMinutes());

      teamsComboBox.setValue(Event.currentEvent.getTeamsInvolved()[1]);
      descriptionTextArea.setText(Event.currentEvent.getDescription());
    }
  }

  /**
   * This handles what happens when the create event button is clicked
   */
  @FXML
  private void onCreateEventButtonClicked() {
    // If all fields filled in
    if (checkNodes()) {
      // If hour and minutes text fields have the appropriate values
      if (checkHoursAndMinutes()) {
        // Set start date and end date
        Date startDate = getDate(startDatePicker,
            startTimeHourTextField.getText(),
            startTimeMinuteTextField.getText(),
            startMenuButton);

        Date endDate = getDate(endDatePicker,
            endTimeHourTextField.getText(),
            endTimeMinuteTextField.getText(),
            endMenuButton);

        // Make sure start date occurs before end date
        if (startDate.before(endDate)) {
          // Make sure event name is not already taken
          if (checkEventName(eventNameTextField.getText())) {
            // Make sure start date does not overlap with another event that one of teams is
            // involved with
            if (!dateOverlaps(startDate)) {
              // If event is being edited
              if (editing) {
                messageNode.setText("Even edited");

                // Set current event's new values
                Event.currentEvent.setName(eventNameTextField.getText());
                Event.currentEvent.setLocation(locationTextField.getText());
                Event.currentEvent.setStartDate(startDate);
                Event.currentEvent.setEndDate(endDate);
                Event.currentEvent.getTeamsInvolved()[1] = teamsComboBox.getValue();
                Event.currentEvent.setDescription(descriptionTextArea.getText());
              } else {
                messageNode.setText("Event created");
                // Set up events and set the teams involved
                Event tempEvent = new Event((Manager) Account.currentUser,
                    eventNameTextField.getText(),
                    locationTextField.getText(),
                    startDate,
                    endDate,
                    descriptionTextArea.getText());

                tempEvent.setTeamsInvolved(((Manager) Account.currentUser).getTeam());
                tempEvent.setTeamsInvolved(teamsComboBox.getValue());

                // Add event to list
                Event.events.add(tempEvent);
                ((Manager) Account.currentUser).getEventsOrganized().add(tempEvent);

                changeScene("EditTeamPage");
              }
            } else {
              messageNode.setText
                  ("Event overlaps with another event that one of the teams is involved with");
            }
          } else {
            messageNode.setText("Event name is already taken");
          }
        }
      }
    } else {
      messageNode.setText("Please fill in all fields");
    }
  }

  /**
   * This handles what happens when the back to edit team button is clicked
   */
  @FXML
  private void onBackToEditTeamButtonClicked() {
    editing = false;
    changeScene("EditTeamPage");
  }

  /**
   * Returns date using JavaFX date picker, the text from the hour and minute text fields, and
   * whether its AM or PM
   *
   * @param datePicker used to get day, month, and year
   * @param timeHour used to get hour
   * @param timeMinute used to get minute
   * @param menuButton used to get AM or PM
   * @return the date constructed by using the parameters
   */
  private Date getDate
  (DatePicker datePicker, String timeHour, String timeMinute, MenuButton menuButton) {
    try {
      // Get integer values of year, month, and day
      int year = datePicker.getValue().getYear();
      int month = datePicker.getValue().getMonthValue();
      int day = datePicker.getValue().getDayOfMonth();

      // Set date string that will be used to create date object
      String dateString = "" + month +
          " " + day +
          ", " + year +
          " " + timeHour +
          ":" + timeMinute +
          " " + menuButton.getText();

      // Return date object
      return dateFormatter.parse(dateString);
    } catch (ParseException parseException) {
      System.out.println("Parse exception caught!");
      return null;
    }
  }

  /**
   * Makes sure that hours and minutes text fields are set to their correct values
   *
   * @return false if value of at least one text field is incorrect true if they all have correct
   * values
   */
  private boolean checkHoursAndMinutes() {
    try {
      // If hour is not in between 1 and 12
      if (Integer.parseInt(startTimeHourTextField.getText()) < 1 ||
          Integer.parseInt(startTimeHourTextField.getText()) > 12) {
        messageNode.setText("Your start time hour is set to an incorrect value, please enter a" +
            " value between 1 and 12");
        return false;
      }

      if (Integer.parseInt(endTimeHourTextField.getText()) < 1 ||
          Integer.parseInt(endTimeHourTextField.getText()) > 12) {
        messageNode.setText("Your end time hour is set to an incorrect value, please enter a" +
            " value between 1 and 12");
        return false;
      }

      // If minute is not in between 0 and 59
      if (Integer.parseInt(startTimeMinuteTextField.getText()) < 0 ||
          Integer.parseInt(startTimeMinuteTextField.getText()) > 59) {
        messageNode.setText("Your start time minute is set to an incorrect value, please enter a" +
            " value between 0 and 59");
        return false;
      }

      if (Integer.parseInt(endTimeMinuteTextField.getText()) < 0 ||
          Integer.parseInt(endTimeMinuteTextField.getText()) > 59) {
        messageNode.setText("Your end time minute is set to an incorrect value, please enter a" +
            " value between 0 and 59");
        return false;
      }

      return true;
    } catch (NumberFormatException imException) {
      messageNode.setText("Only enter numbers in hour and minute text fields");
      return false;
    }
  }

  /**
   * This first checks if start date overlaps with another event's start and end date then, if date
   * does overlap, will check if either teams involved are also involved in this event
   *
   * @param date date to check
   * @return true if date overlaps with another event and said event contains either of the teams
   * involved
   */
  private boolean dateOverlaps(Date date) {
    // For every event
    for (Event event : Event.events) {
      // If date overlaps with event's start and end date and event is not the current event
      if (event.overlapDate(date) && event != Event.currentEvent) {
        // If one of the teams involved is also involved in event
        if (event.teamIsInvolved(((Manager) Account.currentUser).getTeam()) ||
            event.teamIsInvolved(teamsComboBox.getValue())) {
          return true;
        }
      }
    }

    // No overlap
    return false;
  }

  /**
   * This makes sure that event name is not already in use by another event
   *
   * @param name event name to check
   * @return true if event name is not taken false if otherwise
   */
  private boolean checkEventName(String name) {
    Event event = Event.getEventByName(name);
    return (event == null || (event == Event.currentEvent && editing));
  }

  /**
   * When editing event and filling in event text fields make sure that event hours are the
   * appropriate value
   *
   * @param hour integer value of hour to check
   * @param textField text field to set
   * @param menuButton menu button to set
   */
  private void setHourTextField(int hour, TextField textField, MenuButton menuButton) {
    if (hour == 0) {
      textField.setText("" + (hour + 12));
      menuButton.setText("AM");
    } else if (hour > 0 && hour < 12) {
      textField.setText("" + hour);
      menuButton.setText("AM");
    } else if (hour == 12) {
      textField.setText("" + hour);
      menuButton.setText("PM");
    } else {
      textField.setText("" + (hour - 12));
      menuButton.setText("PM");
    }
  }

  /**
   * Checks if all required nodes are filled in
   *
   * @return true if all fields are filled and false if otherwise
   */
  private boolean checkNodes() {
    return checkFields() &&
        teamsComboBox.getValue() != null &&
        startDatePicker.getValue() != null &&
        endDatePicker.getValue() != null &&
        !descriptionTextArea.getText().equals("");
  }
}
