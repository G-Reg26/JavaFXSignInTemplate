package sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CreateEventController extends Controller {

  private static boolean editing;

  private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM dd, yyyy h:mm a");

  private ObservableList<Team> teamsList = FXCollections.observableArrayList();

  @FXML
  private ComboBox<Team> teamsComboBox;

  @FXML
  private Text messageNode;

  @FXML
  private TextField eventNameTextField;

  @FXML
  private TextField locationTextField;

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
  void initialize() {
    //Add text field nodes to textFields
    textFields.add(eventNameTextField);
    textFields.add(locationTextField);
    textFields.add(startTimeHourTextField);
    textFields.add(startTimeMinuteTextField);
    textFields.add(endTimeHourTextField);
    textFields.add(endTimeMinuteTextField);

    //Set on action to all menu items
    startAMMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        startMenuButton.setText(startAMMenuItem.getText());
      }
    });

    startPMMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        startMenuButton.setText(startPMMenuItem.getText());
      }
    });

    endAMMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        endMenuButton.setText(endAMMenuItem.getText());
      }
    });

    endPMMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        endMenuButton.setText(endPMMenuItem.getText());
      }
    });

    //Add all teams to team list except the current user's team
    teamsList.addAll(Team.teams);
    teamsList.remove(((Manager) Account.currentUser).getTeam());

    //Set items of the combo box
    teamsComboBox.setItems(teamsList);

    if (editing) {
      eventNameTextField.setText(Event.currentEvent.getName());
      locationTextField.setText(Event.currentEvent.getLocation());

      startDatePicker.setValue(
          LocalDate.of(Event.currentEvent.getStartDateDate().getYear() + 1900,
              Event.currentEvent.getStartDateDate().getMonth() + 1,
              Event.currentEvent.getStartDateDate().getDate()));

      setHourTextField(Event.currentEvent.getStartDateDate().getHours(),
          startTimeHourTextField,
          startMenuButton);
      startTimeMinuteTextField.setText("" + Event.currentEvent.getStartDateDate().getMinutes());

      endDatePicker.setValue(
          LocalDate.of(Event.currentEvent.getEndDateDate().getYear() + 1900,
              Event.currentEvent.getEndDateDate().getMonth() + 1,
              Event.currentEvent.getEndDateDate().getDate()));

      setHourTextField(Event.currentEvent.getEndDateDate().getHours(),
          endTimeHourTextField,
          endMenuButton);
      endTimeMinuteTextField.setText("" + Event.currentEvent.getEndDateDate().getMinutes());

      teamsComboBox.setValue(Event.currentEvent.getTeamsInvolved()[1]);
      descriptionTextArea.setText(Event.currentEvent.getDescription());
    }
  }

  /**
   * This handles what happens when the create event button is clicked
   */
  @FXML
  void onCreateEventButtonClicked() throws ParseException {
    if (checkNodes()) {
      //Create a start date and end dates using date picker and time text fields
      if (checkHoursAndMinutes()) {
        Date startDate = getDate(startDatePicker,
            startTimeHourTextField.getText(),
            startTimeMinuteTextField.getText(),
            startMenuButton);

        Date endDate = getDate(endDatePicker,
            endTimeHourTextField.getText(),
            endTimeMinuteTextField.getText(),
            endMenuButton);

        //Make sure start date occurs before end date
        if (checkDate(startDate, endDate)) {
          //Make sure event name is not already taken
          if (Event.getEventByName(eventNameTextField.getText()) == null ||
              (Event.getEventByName(eventNameTextField.getText()) == Event.currentEvent &&
                  editing)) {
            //Make sure start date does not overlap with another event that one of teams is
            //involved with
            if (!dateOverlaps(startDate)) {
              if (editing) {
                messageNode.setText("Even edited");

                Event.currentEvent.setName(eventNameTextField.getText());
                Event.currentEvent.setLocation(locationTextField.getText());
                Event.currentEvent.setStartDate(startDate);
                Event.currentEvent.setEndDate(endDate);
                Event.currentEvent.getTeamsInvolved()[1] = teamsComboBox.getValue();
                Event.currentEvent.setDescription(descriptionTextArea.getText());
              } else {
                messageNode.setText("Event created");
                //Set up events and set the teams involved
                Event tempEvent = new Event((Manager) Account.currentUser,
                    eventNameTextField.getText(),
                    locationTextField.getText(), startDate, endDate, descriptionTextArea.getText());

                tempEvent.setTeamsInvolved(((Manager) Account.currentUser).getTeam());
                tempEvent.setTeamsInvolved(teamsComboBox.getValue());

                //Add event to list
                Event.events.add(tempEvent);
                ((Manager) Account.currentUser).getEventsOrganized().add(tempEvent);

                try {
                  changeScene("EditTeamPage");
                } catch (Exception e) {
                  System.out.println("Exception caught");
                }
              }
            } else {
              messageNode.
                  setText(
                      "Event overlaps with another event that one of the teams is involved with");
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

  @FXML
  void onBackToEditTeamButtonClicked() {
    editing = false;

    try {
      changeScene("EditTeamPage");
    } catch (Exception e) {
      System.out.println("Exception caught");
    }
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
  (DatePicker datePicker, String timeHour, String timeMinute, MenuButton menuButton)
      throws ParseException {
    int year = datePicker.getValue().getYear();
    int month = datePicker.getValue().getMonthValue();
    int day = datePicker.getValue().getDayOfMonth();

    String dateString =
        "" + month + " " + day + ", " + year + " " + timeHour + ":" + timeMinute + " "
            + menuButton.getText();
    return dateFormatter.parse(dateString);
  }

  private boolean checkHoursAndMinutes() {
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
  }

  /**
   * Checks if the start date occurs before the end date
   *
   * @param startDate start date
   * @param endDate end date
   * @return true if start date occurs before end date and false if otherwise
   */
  private boolean checkDate(Date startDate, Date endDate) {
    if (startDate.compareTo(new Date()) != 1) {
      messageNode.setText("Start date has already passed");
      return false;
    }

    if (endDate.compareTo(startDate) != 1) {
      messageNode.setText("End date occurs at the same time or before start date");
      return false;
    }

    return true;
  }

  private boolean dateOverlaps(Date date) {
    if (Event.events.size() > 0) {
      for (Event event : Event.events) {
        if (event.overlapDate(date) && event != Event.currentEvent) {
          if (event.teamIsInvolved(((Manager) Account.currentUser).getTeam()) ||
              (event.teamIsInvolved(teamsComboBox.getValue()))) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public void setHourTextField(int hour, TextField textField, MenuButton menuButton) {
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

  public static void isEditiing(boolean beingEdited) {
    editing = beingEdited;
  }
}
