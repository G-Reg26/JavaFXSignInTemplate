/**
 * The Event class is the blueprint used for events, containing information such as:
 * Organizer: The manager account that created the event
 * Name: Name of the event
 * Location: Location of event
 * Start & End Date: Time range of event
 * Description: Description of event (Admission costs, etc.)
 * Event Score: Score of the event
 * Teams Involved: Teams involved in the event
 * Active: Status of event. When event is active scores can be edited
 * As well as implements the StoredInformation interface, which is used for storing event info
 * into a file.
 *
 * @author Jordan Moses
 * @modified Gregorio Lozada
 * @version 1.0
 * @since 10/21/2018
 */

package sample;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Event implements StoredInformation, Comparable<Event> {

  public static ArrayList<Event> events = new ArrayList<>();

  public static Event currentEvent;

  private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy h:mm a");

  private Manager organizer;
  private String name;
  private String location;
  private Date startDate;
  private Date endDate;
  private String description;
  private EventScore eventScore;
  private Team[] teamsInvolved = new Team[2];
  private boolean active;

  public Event() {
    active = true;
    organizer = null;
    name = "";
    location = "";
    startDate = null;
    endDate = null;
    description = "";

    eventScore = new EventScore();
  }

  public Event(Manager teamOrganizer, String eventName, String eventLocation, Date newEventDate,
      Date end,
      String desc) {
    active = true;
    organizer = teamOrganizer;
    name = eventName;
    location = eventLocation;
    startDate = newEventDate;
    endDate = end;
    description = desc;

    eventScore = new EventScore();
  }

  public Manager getOrganizer() {
    return organizer;
  }

  public String getName() {
    return name;
  }

  public String getLocation() {
    return location;
  }

  public Date getStartDateDate() {
    return startDate;
  }

  public Date getEndDateDate() {
    return endDate;
  }

  public String getDescription() {
    return description;
  }

  public EventScore getEventScore() {
    return eventScore;
  }

  public Team[] getTeamsInvolved() {
    return teamsInvolved;
  }

  public boolean isActive() {
    return active;
  }

  public void setOrganizer(Manager manager) {
    organizer = manager;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLocation(String eventLocation) {
    location = eventLocation;
  }

  public void setStartDate(Date date) {
    startDate = date;
  }

  public void setEndDate(Date date) {
    endDate = date;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEventScore(EventScore score) {
    eventScore = score;
  }

  public void setTeamsInvolved(Team team) {
    if (teamsInvolved[0] == null) {
      teamsInvolved[0] = team;
    } else {
      teamsInvolved[1] = team;
    }
  }

  public void setActive(boolean activate) {
    active = activate;
  }

  /**
   * Checks if the team passed is involved in event
   *
   * @param team team to check
   * @return true if team is involved false if otherwise
   */
  public boolean teamIsInvolved(Team team) {
    return (team == teamsInvolved[0] || team == teamsInvolved[1]);
  }

  /**
   * Find event using string passed as a parameter
   *
   * @param eventName name of event to find
   * @return event with the name matching eventName or null if nothing is found
   */
  public static Event getEventByName(String eventName) {
    // For every event in events list
    for (Event event : events) {
      // If event name matches eventName
      if (event.getName().equals(eventName)) {
        return event;
      }
    }

    // Nothing was found
    return null;
  }

  /**
   * Checks if date passed overlaps with start and end date
   *
   * @param dateToCheck date to check
   * @return true if date overlaps false if otherwise
   */
  public boolean overlapDate(Date dateToCheck) {
    return startDate.compareTo(dateToCheck) * dateToCheck.compareTo(endDate) >= 0;
  }

  /**
   * Checks which team has the highest score and returns event winner
   * @return team that won event or null if a draw
   */
  public Team getEventWinner() {
    if (eventScore.getScore(0) > eventScore.getScore(1)) {
      return teamsInvolved[0];
    } else if (eventScore.getScore(0) < eventScore.getScore(1)) {
      return teamsInvolved[1];
    } else {
      return null;
    }
  }

  /**
   * Adds string objects of each attribute of an instance of event to an array list
   *
   * @param list the list that string objects will be added to
   */
  public void getObjectData(ArrayList<String> list) {
    list.add("Organizer:" + organizer);
    list.add("Name:" + name);
    list.add("Location:" + location);
    list.add("StartDate:" + startDate);
    list.add("EndDate:" + endDate);
    list.add("Description:" + description);
    list.add("Team1:" + teamsInvolved[0]);
    list.add("Team2:" + teamsInvolved[1]);
    list.add("Team1Score:" + eventScore.getScore(0));
    list.add("Team2Score:" + eventScore.getScore(1));
    list.add("Active:" + active);
    list.add("event end");
  }

  public int compareTo(Event event) {
    return startDate.compareTo(event.getStartDateDate());
  }

  public String toString() {
    Date today = new Date();

    if (today.before(startDate)) {
      return name + ": " + dateFormatter.format(startDate) + " [Upcoming]";
    } else if (today.after(startDate) && today.before(endDate)) {
      return name + ": " + dateFormatter.format(startDate) + " [Ongoing]";
    } else if (today.after(endDate)) {
      if (getEventWinner() != null) {
        return
            name + ": " + dateFormatter.format(startDate) + " [Winner: " + getEventWinner() + "]";
      } else {
        return
            name + ": " + dateFormatter.format(startDate) + " [Draw]";
      }
    } else {
      return "";
    }
  }
}