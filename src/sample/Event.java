package sample;

import java.util.ArrayList;
import java.util.Date;


public class Event implements StoredInformation, Comparable<Event> {

  public static ArrayList<Event> events = new ArrayList<>();
  public static Event currentEvent;

  private Manager organizer;
  private String name;
  private String location;
  private Date startDate;
  private Date endDate;
  private String description;
  private EventScore eventScore;
  private Team[] teamsInvolved = new Team[2];

  public Event() {
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
    organizer = teamOrganizer;
    name = eventName;
    location = eventLocation;
    startDate = newEventDate;
    endDate = end;
    description = desc;

    eventScore = new EventScore();
  }

  public void setOrganizer(Manager manager) {
    organizer = manager;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStartDate(Date date) {
    startDate = date;
  }

  public void setEndDate(Date date) {
    endDate = date;
  }

  public void setLocation(String eventLocation) {
    location = eventLocation;
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

  public Manager getOrganizer() {
    return organizer;
  }

  public Date getStartDateDate() {
    return startDate;
  }

  public Date getEndDateDate() {
    return endDate;
  }

  public String getLocation() {
    return location;
  }

  public String getName() {
    return name;
  }

  public EventScore getEventScore() {
    return eventScore;
  }

  public String getDescription() {
    return description;
  }

  public Team[] getTeamsInvolved() {
    return teamsInvolved;
  }

  public boolean teamIsInvolved(Team team) {
    return (team == teamsInvolved[0] || team == teamsInvolved[1]);
  }

  public static Event getEventByName(String eventName) {
    if (events.size() > 0) {
      for (Event event : events) {
        if (event.getName().equals(eventName)) {
          return event;
        }
      }
    }

    return null;
  }

  public boolean overlapDate(Date dateToCheck) {
    return startDate.compareTo(dateToCheck) * dateToCheck.compareTo(endDate) >= 0;
  }

  public Team getEventWinner() {
    if (eventScore.getScore(0) > eventScore.getScore(1))
      return teamsInvolved[0];
    else if (eventScore.getScore(0) < eventScore.getScore(1))
      return teamsInvolved[1];
    else
      return null;
  }

  public String toString() {
    return name + ": " + startDate;
  }

  public int compareTo(Event event) {
    return startDate.compareTo(event.getStartDateDate());
  }

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
    list.add("event end");
  }
}