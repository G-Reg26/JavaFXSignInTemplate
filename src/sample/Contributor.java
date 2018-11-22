/**
 * The Contributor class is the blueprint used for user accounts that contribute to teams and
 * events, containing general account information along with: Account Type: The type of the account
 * (Spectator, player, or manager) Team: The team the user contributes to
 *
 * @author Gregorio Lozada
 * @version 1.0
 * @since 10/21/2018
 */

package sample;

import java.util.ArrayList;

public class Contributor extends Account {

  protected Team team;

  public Contributor() {
    super();
  }

  public Contributor(String name, String userName, String passWord, AccountType type, Team team) {
    super(name, userName, passWord, type);
    this.team = team;
  }

  /***
   * Getter method for team
   *
   * @return team attribute of an instance of contributor
   */
  public Team getTeam() {
    return team;
  }

  /***
   * Setter method for team
   *
   * @param team the team attribute will be set to this parameter
   */
  public void setTeam(Team team) {
    this.team = team;
  }

  /***
   * Adds string objects of each attribute of an instance of team to an array list
   *
   * @param list the list that string objects will be added to
   */
  public void getObjectData(ArrayList<String> list) {
    super.getObjectData(list);
    if (team != null) {
      list.add("Team:" + team.getName());
    }
  }
}
