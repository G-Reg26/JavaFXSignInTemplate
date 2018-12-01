/**
 * The FileManager class manages reading and writing text files that will store user, team, event,
 * and news information.
 *
 * @author Gregorio Lozada
 * @version 1.0
 * @since Created: 11/5/2018
 */

package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import sample.Account.AccountType;

public class FileManager {

  private enum FileContents {
    TEAMS,
    ACCOUNTS,
    EVENTS,
    NEWS
  }

  private File teamsFile = new File("teams");
  private File accountsFile = new File("accounts");
  private File eventFile = new File("events");
  private File newsFile = new File("news");

  public FileManager() {
    checkFiles(teamsFile, FileContents.TEAMS);
    checkFiles(accountsFile, FileContents.ACCOUNTS);
    checkFiles(eventFile, FileContents.EVENTS);
    checkFiles(newsFile, FileContents.NEWS);
  }

  /**
   * This method checks to see if a certain file exists. If the file does exist it will be read, if
   * not the file will be created.
   *
   * @param fileToCheck the file that will be checked
   * @param contents the contents the file holds
   */
  private void checkFiles(File fileToCheck, FileContents contents) {
    try {
      if (!fileToCheck.exists()) {
        fileToCheck.createNewFile();
      } else {
        readFiles(fileToCheck, contents);
      }
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /**
   * Writes to all necessary files
   */
  public void writeFiles() {
    writeFile(Team.teams, "teams");
    writeFile(Account.accounts, "accounts");
    writeFile(Event.events, "events");
    writeFile(News.newsList, "news");
  }

  /**
   * This method writes the information of certain objects being stored in a list to a text file
   *
   * @param list objects that will be written to a file
   * @param fileName name of the file to write to
   * @param <T> the data type of the objects being stored in list
   */
  private <T> void writeFile(ArrayList<T> list, String fileName) {
    // This list will hold the lines of the file it will be writing to
    ArrayList<String> fileLines = new ArrayList<>();
    // If there is at least one object in list
    if (list.size() > 0) {
      // For each object in list
      for (T listObject : list) {
        // If the object implements the stored information interface
        if (listObject instanceof StoredInformation) {
          // Add the objects data to the list of file lines
          ((StoredInformation) listObject).getObjectData(fileLines);
          // If the object is an account object
          if (listObject instanceof Account) {
            // Add an indicator that'll show there's no more user info to be read
            fileLines.add("user end");
          }
        }
      }

      try {
        // Write all the info in file lines to file
        Path file = Paths.get(fileName);
        Files.write(file, fileLines, Charset.forName("UTF-8"));
      } catch (IOException ioException) {
        System.out.println("Input/Output exception caught");
      }

      fileLines.clear();
    }
  }

  /**
   * Depending on what type of content is in the file to read read the file through specific
   * methods
   *
   * @param fileToRead file to read
   * @param contents contents of the file to read
   */
  private void readFiles(File fileToRead, FileContents contents) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileToRead));

      switch (contents) {
        case TEAMS:
          readTeamsFile(br);
        case ACCOUNTS:
          readAccountsFile(br);
        case EVENTS:
          readEventFile(br);
        case NEWS:
          readNewsFile(br);
      }
    } catch (FileNotFoundException fileNotFoundException) {
      System.out.println("File not found exception caught");
    }
  }

  /**
   * This method reads a list of team names from a file and creates a team with that name The team
   * will have no manager and no members, they will be added as accounts are read
   *
   * @param br the buffered reader that will read each line in file
   */
  private void readTeamsFile(BufferedReader br) {
    String st;

    Team tempTeam = null;

    try {
      while ((st = br.readLine()) != null) {
        if (stringCheck(st, "Comments:")) {
          Comments tempComment = null;
          while (!(st = br.readLine()).equals("Comments end")) {
            if (stringCheck(st, "Comment:")) {
              tempComment = new Comments();
              tempComment.setText(st.substring(st.indexOf(":") + 1));
            } else if (stringCheck(st, "Date:")) {
              tempComment.setDate(new Date(st.substring(st.indexOf(":") + 1)));
              tempTeam.getComments().add(tempComment);
            }
          }
        } else if (st.equals("Team end")) {
          Team.teams.add(tempTeam);
        } else {
          tempTeam = new Team(st);
        }
      }
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /**
   * This method reads a list of account information from a file and uses it to create an account
   *
   * @param br the buffered reader that will read each line in file
   */
  private void readAccountsFile(BufferedReader br) {
    Account tempAccount = null;

    String st;

    try {
      while ((st = br.readLine()) != null) {
        if (stringCheck(st, "Type:")) {
          String accountType = st.substring(st.indexOf(":") + 1);
          switch (accountType) {
            case "SPECTATOR":
              tempAccount = new Account();
              tempAccount.setType(AccountType.SPECTATOR);
              break;
            case "PLAYER":
              tempAccount = new Player();
              tempAccount.setType(AccountType.PLAYER);
              break;
            case "MANAGER":
              tempAccount = new Manager();
              tempAccount.setType(AccountType.MANAGER);
              break;
          }
        } else if (stringCheck(st, "User:")) {
          tempAccount.setUsername(st.substring(st.indexOf(":") + 1));
        } else if (stringCheck(st, "Name:")) {
          tempAccount.setName(st.substring(st.indexOf(":") + 1));
        } else if (stringCheck(st, "Password:")) {
          tempAccount.setPassword(st.substring(st.indexOf(":") + 1));
        } else if (stringCheck(st, "Team:")) {
          ((Contributor) tempAccount)
              .setTeam(Team.findTeamByName(st.substring(st.indexOf(":") + 1)));

          if (tempAccount instanceof Player) {
            ((Player) tempAccount).getTeam().addPlayer((Player) tempAccount);
          } else if (tempAccount instanceof Manager) {
            ((Manager) tempAccount).getTeam().setManager((Manager) tempAccount);
          }
        } else if (stringCheck(st, "TeamRequested:")) {
          Team.findTeamByName(st.substring(st.indexOf(":") + 1))
              .addToRequestList((Player) tempAccount);
        } else if (st.equals("Teams followed:")) {
          while (!(st = br.readLine()).equals("Teams followed end")) {
            tempAccount.followTeam(Team.findTeamByName(st));
          }
        } else if (st.equals("user end")) {
          tempAccount.initializeProfilePic();
          Account.accounts.add(tempAccount);
        }
      }
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /**
   * This method reads a list of event information from a file and uses it to create an event
   *
   * @param br the buffered reader that will read each line in file
   */
  private void readEventFile(BufferedReader br) {
    Event tempEvent = null;

    String st;

    try {
      while ((st = br.readLine()) != null) {
        if (stringCheck(st, "Organizer:")) {
          tempEvent = new Event();
          tempEvent
              .setOrganizer(
                  (Manager) Account.findUserByUsername(st.substring(st.indexOf(":") + 1)));
          ((Manager) Account.findUserByUsername((st.substring(st.indexOf(":") + 1)))).
              getEventsOrganized().add(tempEvent);
        } else if (stringCheck(st, "Name:")) {
          tempEvent.setName(st.substring(st.indexOf(":") + 1));
        } else if (stringCheck(st, "Location:")) {
          tempEvent.setLocation(st.substring(st.indexOf(":") + 1));
        } else if (stringCheck(st, "StartDate:")) {
          tempEvent.setStartDate(new Date(st.substring(st.indexOf(":") + 1)));
        } else if (stringCheck(st, "EndDate:")) {
          tempEvent.setEndDate(new Date(st.substring(st.indexOf(":") + 1)));
        } else if (stringCheck(st, "Description:")) {
          tempEvent.setDescription(st.substring(st.indexOf(":") + 1));
        } else if (stringCheck(st, "Team1:") || stringCheck(st, "Team2:")) {
          tempEvent.setTeamsInvolved(Team.findTeamByName(st.substring(st.indexOf(":") + 1)));
        } else if (stringCheck(st, "Team1Score:")) {
          tempEvent.getEventScore().
              setScore(0, Integer.parseInt(st.substring(st.indexOf(":") + 1)));
        } else if (stringCheck(st, "Team2Score:")) {
          tempEvent.getEventScore().
              setScore(1, Integer.parseInt(st.substring(st.indexOf(":") + 1)));
        } else if (stringCheck(st, "Active:")) {
          if (st.substring(st.indexOf(":") + 1).equals("true")) {
            tempEvent.setActive(true);
          } else {
            tempEvent.setActive(false);
          }
        } else if (st.equals("event end")) {
          Event.events.add(tempEvent);
        }
      }
    } catch (IOException ioException) {
      System.out.println("Input/Output exception caught");
    }
  }

  /**
   * This method reads a list of news information from a file and uses it to create a news object
   *
   * @param br the buffered reader that will read each line in file
   */
  private void readNewsFile(BufferedReader br) {
    News tempNews = null;

    String st;

    try {
      while ((st = br.readLine()) != null) {
        if (stringCheck(st, "News:")) {
          tempNews = new News();
          tempNews.setNews(st.substring(st.indexOf(":") + 1));
        } else if (stringCheck(st, "Date:")) {
          tempNews.setDate(new Date(st.substring(st.indexOf(":") + 1)));
        } else if (st.equals("Team:")) {
          while (!(st = br.readLine()).equals("Team end")) {
            tempNews.addTeamInvolved(Team.findTeamByName(st));
          }
        } else if (st.equals("end")) {
          News.newsList.add(tempNews);
        }
      }
    } catch (IOException ioException) {
      System.out.println("Failed to read file");
    }
  }

  /**
   * Checks if beginning of string matches the desired string
   *
   * @param stringToBeChecked string that will be checked
   * @param desiredString string desired
   * @return false if the beginning of stringToBeChecked does not match desiredString
   */
  private boolean stringCheck(String stringToBeChecked, String desiredString) {
    if (stringToBeChecked.length() >= desiredString.length()) {
      return stringToBeChecked.substring(0, desiredString.length()).equals(desiredString);
    } else {
      return false;
    }
  }

  /**
   * Copy contents of one file to another
   *
   * @param in file that will have its contents copied
   * @param out file that will be copied to
   */
  public static void copyToFile(File in, File out) {
    FileChannel inStream = null;
    FileChannel outStream = null;

    try {
      inStream = new FileInputStream(in).getChannel();
      outStream = new FileOutputStream(out).getChannel();
      outStream.transferFrom(inStream, 0, inStream.size());
    } catch (Exception e) {
      System.out.println("Exception caught!");
    } finally {
      try {
        if (inStream != null) {
          inStream.close();
        }

        if (outStream != null) {
          outStream.close();
        }
      } catch (IOException ioE) {
        System.out.println("IOException caught!");
      }
    }
  }
}
