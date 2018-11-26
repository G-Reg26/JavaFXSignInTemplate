package sample;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;

public class Team implements StoredInformation {


  public static ArrayList<Team> teams = new ArrayList<>();

  public static Team currentTeam;

  public static File defaultProfilePicFile;

  public static BufferedImage defaultProfilePic;

  private ArrayList<Player> members = new ArrayList<>();
  private ArrayList<Player> joinRequests = new ArrayList<>();
  private ArrayList<Comments> comments = new ArrayList<>();

  private Manager manager;
  private String name;

  private File profilePicFile;
  private BufferedImage profilePic;

  public Team(String name) {
    this.name = name;
    initializeProfilePic();
  }

  public Team(Manager manager, String name) {
    this.manager = manager;
    this.name = name;
    initializeProfilePic();
  }

  public ArrayList<Player> getMembers() {
    return members;
  }

  public ArrayList<Player> getJoinRequestsList() {
    return joinRequests;
  }

  public ArrayList<Comments> getComments() {
    return comments;
  }

  public Manager getManager() {
    return manager;
  }

  public String getName() {
    return name;
  }

  public File getProfilePicFile() {
    return profilePicFile;
  }

  public BufferedImage getProfilePic() {
    return profilePic;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  public void setProfilePic(BufferedImage bufferedImage) {
    profilePic = bufferedImage;
  }

  /**
   * Add player to team
   *
   * @param playerToAdd player to add to team
   */
  public void addPlayer(Player playerToAdd) {
    // Add player to members list
    members.add(playerToAdd);

    // If player is in join requests list
    if (joinRequests.contains(playerToAdd)) {
      // Remove player from requests list
      joinRequests.remove(playerToAdd);
      playerToAdd.setTeamRequested(null);
    }
  }

  /**
   * Remove player from team
   *
   * @param playerToRemove player to remove from team
   */
  public void removePlayer(Player playerToRemove) {
    members.remove(playerToRemove);
  }

  /**
   * Add player to requests list
   *
   * @param playerRequest
   */
  public void addToRequestList(Player playerRequest) {
    joinRequests.add(playerRequest);
    playerRequest.setTeamRequested(this);
  }

  /**
   * Find team using string passed as a parameter
   *
   * @param teamName name of team to find
   * @return team with the name matching teamName or null if nothing is found
   */
  public static Team findTeamByName(String teamName) {
    // For every team in teams list
    for (Team team : teams) {
      // If team name matches teamName
      if (team.getName().equals(teamName)) {
        return team;
      }
    }

    // Nothing was found
    return null;
  }

  /**
   * Initialize team profile picture
   */
  public void initializeProfilePic() {
    // Create file object
    profilePicFile = new File(".\\src\\sample\\Images\\TeamProfilePics\\" + name + ".png");

    try {
      // If file exists
      if (!profilePicFile.exists()) {
        // Create file
        profilePicFile.createNewFile();
        // Set profile pic to the default profile pic
        profilePic = defaultProfilePic;
      } else {
        // If profile pic is empty
        if (profilePicFile.length() == 0) {
          // Set profile pic to the default profile pic
          profilePic = defaultProfilePic;
        } else {
          // Set profile pic to the png file in the profile pic file path
          profilePic = ImageIO.read(profilePicFile);
        }
      }
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /**
   * Delete team. Remove team from teams list and anything connected to it such as, events, news,
   * and remove members from team
   */
  public void deleteTeam() {
    // For every account
    for (Account account : Account.accounts) {
      // Remove team from account's follow list if account is following this team
      if (account.getTeamsFollowed().contains(this)) {
        account.unfollowTeam(this);
      }

      // If account is a player account
      if (account instanceof Player) {
        // If player requested to join this team delete request
        if (((Player) account).getTeamRequested() == this) {
          joinRequests.remove(account);
          ((Player) account).setTeamRequested(null);
        }
        // If player is a member of the team remove player from team
        else if (((Player) account).getTeam() == this) {
          members.remove(account);
          ((Player) account).setTeam(null);
        }
      }
    }

    // For every event
    for (Iterator<Event> iterator = Event.events.iterator(); iterator.hasNext(); ) {
      Event event = iterator.next();
      // If team is involved in event delete event
      if (event.teamIsInvolved(this)) {
        iterator.remove();
      }
    }

    // For every news
    for (Iterator<News> iterator = News.newsList.iterator(); iterator.hasNext(); ) {
      News news = iterator.next();
      // If team is part of news
      if (news.teamIsInvolved(this)) {
        // If this team is the only team involved delete team
        if (news.getTeamsInvolved().size() == 1) {
          iterator.remove();
        }
        // Else remove team from news' teamsInvolved list
        else {
          news.getTeamsInvolved().remove(this);
        }
      }
    }

    // Clear events organized list
    manager.getEventsOrganized().clear();
    // Set manager to null
    manager = null;
    // Delete profile pic file
    profilePicFile.delete();
    // Remove this team from the teams list
    Team.teams.remove(this);
  }

  /**
   * Adds string objects of each attribute of an instance of team to an array list
   *
   * @param list the list that string objects will be added to
   */
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
