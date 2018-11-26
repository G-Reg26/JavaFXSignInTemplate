/**
 * The Player class is the blueprint used for user accounts that contribute to teams and events,
 * containing general and contributor account information
 *
 * @author Gregorio Lozada
 * @version 1.0
 * @since 10/21/2018
 */

package sample;

import java.util.ArrayList;

public class Player extends Contributor {

  private Team teamRequested;

  public Player() {
    super();
  }

  public Player(String name, String userName, String passWord, AccountType type) {
    super(name, userName, passWord, type, null);
  }

  public Team getTeamRequested() {
    return teamRequested;
  }

  public void setTeamRequested(Team teamRequested) {
    this.teamRequested = teamRequested;
  }

  /**
   * When player account is changing account type
   */
  public void changeTier(){
    // If user requested to join a team
    if (teamRequested != null) {
      // Remove player from team's requests list and set requested team to null
      teamRequested.getJoinRequestsList().remove(this);
      teamRequested = null;
    }
    // If user is part of a team
    else if (((Player) Account.currentUser).getTeam() != null) {
      // Remove player from team members list and set team to null
      team.getMembers().remove(this);
      team = null;
    }
  }

  /**
   * Adds string objects of each attribute of an instance of player to an array list
   *
   * @param list the list that string objects will be added to
   */
  public void getObjectData(ArrayList<String> list) {
    super.getObjectData(list);
    if (teamRequested != null) {
      list.add("TeamRequested:" + teamRequested.getName());
    }
  }
}
