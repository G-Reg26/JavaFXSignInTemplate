/**
 * The EventScore class is the blueprint used for event scores. The class sets scores for the
 * individual teams involved and increments/decrements them.
 *
 * @author Jordan Moses
 * @version 1.0
 * @since 10/21/2018
 */

package sample;

public class EventScore {

  private int[] scoreArray = new int[2];

  public void incrementScore(int teamIndex) {
    scoreArray[teamIndex]++;
  }

  public void decrementScore(int teamIndex) {
    scoreArray[teamIndex]--;
  }

  public void setScore(int teamIndex, int score) {
    scoreArray[teamIndex] = score;
  }

  public int getScore(int teamIndex) {
    return scoreArray[teamIndex];
  }
}
