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

  //Sets getScore
  public int getScore(int teamIndex) {
    return scoreArray[teamIndex];
  }
}
