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

  Team teamRequested;

  public Player() {
    super();
  }

  public Player(String name, String userName, String passWord, AccountType type) {
    super(name, userName, passWord, type, null);
  }

  public void setTeamRequested(Team teamRequested) {
    this.teamRequested = teamRequested;
  }

  public Team getTeamRequested() {
    return teamRequested;
  }

  public void getObjectData(ArrayList<String> list) {
    super.getObjectData(list);
    if (teamRequested != null) {
      list.add("TeamRequested:" + teamRequested.getName());
    }
  }
}
