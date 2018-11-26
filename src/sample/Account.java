/**
 * The Account class is the blueprint used for user accounts, containing information such as:
 * Account Type: The type of the account (Spectator, player, or manager) Name: user's name Username:
 * used to identify a user's account Password: user's password required to access their account
 * Teams Followed: The teams the user follows As well as implements the StoredInformation interface,
 * which is used for storing account info into a file.
 *
 * @author Gregorio Lozada
 * @version 1.0
 * @since 10/18/2018
 */

package sample;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Account implements StoredInformation {

  public enum AccountType {
    SPECTATOR,
    PLAYER,
    MANAGER,
  }

  public static ArrayList<Account> accounts = new ArrayList<>();

  public static Account currentUser;

  public static File defaultProfilePicFile;

  public static BufferedImage defaultProfilePic;

  private ArrayList<Team> teamsFollowed = new ArrayList<>();

  private AccountType type;
  private String name;
  private String username;
  private String password;

  private File profilePicFile;
  private BufferedImage profilePic = defaultProfilePic;

  public Account() {
    name = "";
    username = "";
    password = "";
    type = AccountType.SPECTATOR;
  }

  public Account(String name, String username, String password, AccountType type) {
    this.name = name;
    this.username = username;
    this.password = password;
    this.type = type;

    initializeProfilePic();
  }

  public ArrayList<Team> getTeamsFollowed() {
    return teamsFollowed;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public AccountType getAccountType() {
    return type;
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

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setType(AccountType type) {
    this.type = type;
  }

  public void setProfilePic(BufferedImage image) {
    profilePic = image;
  }

  /**
   * Adds specific team to the teams followed list
   *
   * @param teamToFollow the team that will be added to the teams followed list
   */
  public void followTeam(Team teamToFollow) {
    teamsFollowed.add(teamToFollow);
  }

  /**
   * Removes specific team from the teams followed list
   *
   * @param teamToUnfollow the team that will be removed from the teams followed list
   */
  public void unfollowTeam(Team teamToUnfollow) {
    teamsFollowed.remove(teamToUnfollow);
  }

  /**
   * Find account using string passed as a parameter
   *
   * @param username username of team to find
   * @return account with the name matching username or null if nothing is found
   */
  public static Account findUserByName(String username) {
    // For every account in account list
    for (Account account : accounts) {
      // If account username matches username passed
      if (account.getUsername().equals(username)) {
        return account;
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
    profilePicFile =
        new File(".\\src\\sample\\Images\\AccountProfilePics\\" + username + ".png");

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
   * Adds string objects of each attribute of an instance of account to an array list
   *
   * @param list the list that string objects will be added to
   */
  public void getObjectData(ArrayList<String> list) {
    list.add("Type:" + type.toString());
    list.add("User:" + username);
    list.add("Name:" + name);
    list.add("Password:" + password);

    if (teamsFollowed.size() > 0) {
      list.add("Teams followed:");
      for (Team team : teamsFollowed) {
        list.add(team.getName());
      }
      list.add("Teams followed end");
    }
  }

  public String toString() {
    return username;
  }
}
