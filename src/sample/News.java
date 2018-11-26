package sample;

import java.util.ArrayList;
import java.util.Date;

public class News implements Comparable<News>, StoredInformation {

  public static ArrayList<News> newsList = new ArrayList<>();

  private ArrayList<Team> teamsInvolved = new ArrayList<>();

  private String news;
  private Date date;

  public News() {
    news = "";
    date = null;
  }

  public News(String news, Date date) {
    this.date = date;
    this.news = news;
  }

  public ArrayList<Team> getTeamsInvolved() {
    return teamsInvolved;
  }

  public String getNews() {
    return news;
  }

  public Date getDate() {
    return date;
  }

  public void setNews(String theNews) {
    news = theNews;
  }

  public void setDate(Date theDate) {
    date = theDate;
  }

  /**
   * Add team to teams involved list
   *
   * @param team team to add to list
   */
  public void addTeamInvolved(Team team) {
    teamsInvolved.add(team);
  }

  /**
   * Check if team is involved in the specific news object
   *
   * @param team team to check if it's involved
   * @return team that is involved
   */
  public boolean teamIsInvolved(Team team) {
    return teamsInvolved.contains(team);
  }

  /**
   * Adds string objects of each attribute of an instance of news to an array list
   *
   * @param list the list that string objects will be added to
   */
  public void getObjectData(ArrayList<String> list) {
    list.add("News:" + news);
    list.add("Date:" + date.toString());
    if (teamsInvolved.size() > 0) {
      list.add("Team:");
      for (Team team : teamsInvolved) {
        list.add(team.toString());
      }
      list.add("Team end");
    }
    list.add("end");
  }

  public int compareTo(News news) {
    return date.compareTo(news.getDate());
  }

  public String toString() {
    return news;
  }
}
