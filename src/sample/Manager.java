/**
 * The Manager class is the blueprint used for user accounts that contribute to teams and events,
 * containing general and contributor account information
 *
 * @author Gregorio Lozada
 * @version 1.0
 * @since 10/21/2018
 */

package sample;

import java.util.ArrayList;

public class Manager extends Contributor {

  private ArrayList<Event> eventsOrganized = new ArrayList<>();

  public Manager() {
    super();
  }

  public Manager(String name, String userName, String passWord, AccountType type, Team team) {
    super(name, userName, passWord, type, team);
  }

  public ArrayList<Event> getEventsOrganized() {
    return eventsOrganized;
  }
}
