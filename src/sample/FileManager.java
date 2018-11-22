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
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.TemporalAccessor;
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

  public FileManager() throws IOException {
    checkFiles(teamsFile, FileContents.TEAMS);
    checkFiles(accountsFile, FileContents.ACCOUNTS);
    checkFiles(eventFile, FileContents.EVENTS);
    checkFiles(newsFile, FileContents.NEWS);
  }

  /***
   * This method checks to see if a certain file exists. If the file does exist it will be read,
   * if not the file will be created.
   *
   * @param fileToCheck the file that will be checked
   * @param contents the contents the file holds
   * @throws IOException
   */
  private void checkFiles(File fileToCheck, FileContents contents) throws IOException {
    if (!fileToCheck.exists()) {
      fileToCheck.createNewFile();
    } else {
      readFiles(fileToCheck, contents);
    }
  }

  /***
   * Writes to all necessary files
   *
   * @throws IOException
   */
  public void writeFiles() throws IOException {
    writeFile(Team.teams, "teams");
    writeFile(Account.accounts, "accounts");
    writeFile(Event.events, "events");
    writeFile(News.newsList, "news");
  }

  /***
   * This method writes the information of certain objects being stored in a list to a text file
   *
   * @param list objects that will be written to a file
   * @param fileName name of the file to write to
   * @param <T>  the data type of the objects being stored in list
   * @throws IOException
   */
  private <T> void writeFile(ArrayList<T> list, String fileName) throws IOException {
    //This list will hold the lines of the file it will be writing to
    ArrayList<String> fileLines = new ArrayList<>();
    //If there is at least one object in list
    if (list.size() > 0) {
      //For each object in list
      for (T listObject : list) {
        //If the object implements the stored information interface
        if (listObject instanceof StoredInformation) {
          //Add the objects data to the list of file lines
          ((StoredInformation) listObject).getObjectData(fileLines);
          //If the object is an account object
          if (listObject instanceof Account) {
            //Add an indicator that'll show there's no more user info to be read
            fileLines.add(((Account) listObject).getUsername() + " user end");
          }
        }
      }

      //Write all the info in file lines to file
      Path file = Paths.get(fileName);
      Files.write(file, fileLines, Charset.forName("UTF-8"));

      fileLines.clear();
    }
  }

  private void readFiles(File fileToRead, FileContents contents) throws IOException {
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
  }

  /***
   * This method reads a list of team names from a file and creates a team with that name
   * The team will have no manager and no members, they will be added as accounts are read
   *
   * @param br the buffered reader that will read each line in file
   * @throws IOException
   */
  private void readTeamsFile(BufferedReader br) throws IOException {
    String st;

    Team tempTeam = null;

    while ((st = br.readLine()) != null) {
      if (st.equals("Comments:")) {
        Comments tempComment = null;
        while (!(st = br.readLine()).equals("Comments end")) {
          if (st.contains("Comment:")) {
            tempComment = new Comments();
            tempComment.setText(st.substring(st.indexOf(":") + 1));
          } else if (st.contains("Date:")) {
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

  }

  /***
   * This method reads a list of account information from a file and uses it to create an account
   *
   * @param br the buffered reader that will read each line in file
   * @throws IOException
   */
  private void readAccountsFile(BufferedReader br) throws IOException {
    Account tempAccount = null;

    String st;

    while ((st = br.readLine()) != null) {
      if (st.contains("Type:")) {
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
      } else if (st.contains("User:")) {
        tempAccount.setUsername(st.substring(st.indexOf(":") + 1));
      } else if (st.contains("Name:")) {
        tempAccount.setName(st.substring(st.indexOf(":") + 1));
      } else if (st.contains("Password:")) {
        tempAccount.setPassword(st.substring(st.indexOf(":") + 1));
      } else if (st.contains("Team:")) {
        ((Contributor) tempAccount).setTeam(Team.findTeamByName(st.substring(st.indexOf(":") + 1)));

        if (tempAccount instanceof Player) {
          ((Player) tempAccount).getTeam().addPlayer((Player) tempAccount);
        } else if (tempAccount instanceof Manager) {
          ((Manager) tempAccount).getTeam().setManager((Manager) tempAccount);
        }
      } else if (st.contains("TeamRequested:")) {
        Team.findTeamByName(st.substring(st.indexOf(":") + 1))
            .addToRequestList((Player) tempAccount);
      } else if (st.equals("Teams followed:")) {
        while (!(st = br.readLine()).equals("Teams followed end")) {
          tempAccount.followTeam(Team.findTeamByName(st));
        }
      } else if (st.contains("user end")) {
        Account.accounts.add(tempAccount);
      }
    }

  }

  private void readEventFile(BufferedReader br) throws IOException {
    Event tempEvent = null;

    String st;

    while ((st = br.readLine()) != null) {
      if (st.contains("Organizer:")) {
        tempEvent = new Event();
        tempEvent.setOrganizer((Manager) Account.findUserByName(st.substring(st.indexOf(":") + 1)));
        ((Manager) Account.findUserByName((st.substring(st.indexOf(":") + 1)))).
            getEventsOrganized().add(tempEvent);
      } else if (st.contains("Name:")) {
        tempEvent.setName(st.substring(st.indexOf(":") + 1));
      } else if (st.contains("Location:")) {
        tempEvent.setLocation(st.substring(st.indexOf(":") + 1));
      } else if (st.contains("StartDate:")) {
        tempEvent.setStartDate(new Date(st.substring(st.indexOf(":") + 1)));
      } else if (st.contains("EndDate:")) {
        tempEvent.setEndDate(new Date(st.substring(st.indexOf(":") + 1)));
      } else if (st.contains("Description:")) {
        tempEvent.setDescription(st.substring(st.indexOf(":") + 1));
      } else if (st.contains("Team1:") || st.contains("Team2:")) {
        tempEvent.setTeamsInvolved(Team.findTeamByName(st.substring(st.indexOf(":") + 1)));
      } else if (st.contains("Team1Score:")) {
        tempEvent.getEventScore().
            setScore(0, Integer.parseInt(st.substring(st.indexOf(":") + 1)));
      } else if (st.contains("Team2Score:")) {
        tempEvent.getEventScore().
            setScore(1, Integer.parseInt(st.substring(st.indexOf(":") + 1)));
      } else if (st.contains("event end")) {
        Event.events.add(tempEvent);
      }
    }
  }

  /***
   * This method reads a list of news information from a file and uses it to create a news object
   *
   * @param br the buffered reader that will read each line in file
   * @throws IOException
   */
  private void readNewsFile(BufferedReader br) throws IOException {
    News tempNews = null;

    String st;

    while ((st = br.readLine()) != null) {
      if (st.contains("News:")) {
        tempNews = new News();
        tempNews.setNews(st.substring(st.indexOf(":") + 1));
      } else if (st.contains("Date:")) {
        tempNews.setDate(new Date(st.substring(st.indexOf(":") + 1)));
      } else if (st.equals("Team:")) {
        while (!(st = br.readLine()).equals("Team end")) {
          tempNews.addTeamInvolved(Team.findTeamByName(st));
        }
      } else if (st.equals("end")) {
        News.newsList.add(tempNews);
      }
    }
  }
}
