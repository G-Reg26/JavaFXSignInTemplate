package sample;

import java.util.ArrayList;

public class Team implements StoredInformation {

  public static ArrayList<Team> teams = new ArrayList<>();
  public static Team currentTeam;

  private ArrayList<Player> members = new ArrayList<>();
  private ArrayList<Player> joinRequests = new ArrayList<>();
  private ArrayList<Comments> comments = new ArrayList<>();

  private Manager manager;
  private String name;

  public Team(String name) {
    this.name = name;
  }

  public Team(Manager manager, String name) {
    this.manager = manager;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public ArrayList<Comments> getComments() {
    return comments;
  }

  public ArrayList<Player> getJoinRequestsList() {
    return joinRequests;
  }

  public ArrayList<Player> getMembers() {
    return members;
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  public Manager getManager() {
    return manager;
  }

  public void addPlayer(Player playerToAdd) {
    members.add(playerToAdd);

    if (joinRequests.contains(playerToAdd)) {
      joinRequests.remove(playerToAdd);
      playerToAdd.setTeamRequested(null);
    }
  }

  public void removePlayer(Player playerToRemove) {
    members.remove(playerToRemove);
  }

  public String addToRequestList(Player playerRequest) {
    if (joinRequests.contains(playerRequest)) {
      return "Player has already sent a request";
    } else if (members.contains(playerRequest)) {
      return "Player is already a member of this team";
    } else {
      joinRequests.add(playerRequest);
      playerRequest.setTeamRequested(this);
      return "Request has been sent";
    }
  }

  public static Team findTeamByName(String teamName) {
    if (teams.size() > 0) {
      for (Team team : teams) {
        if (team.getName().equals(teamName)) {
          return team;
        }
      }
    }

    return null;
  }

  public void getObjectData(ArrayList<String> list) {
    list.add(name);
    if (comments.size() > 0) {
      list.add("Comments:");
      for (Comments comment : comments) {
        list.add("Comment:" + comment.getText());
        list.add("Date:" + comment.getDate());
      }
      list.add("Comments end");
    }
    list.add("Team end");
  }

  public String toString() {
    return name;
  }
}
