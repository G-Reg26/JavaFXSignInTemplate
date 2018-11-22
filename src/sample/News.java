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

  public void setNews(String theNews) {
    news = theNews;
  }

  public void setDate(Date theDate) {
    date = theDate;
  }

  public String getNews() {
    return news;
  }

  public Date getDate() {
    return date;
  }

  public void addTeamInvolved(Team team) {
    teamsInvolved.add(team);
  }

  public boolean teamIsInvolved(Team team) {
    return teamsInvolved.contains(team);
  }

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

  @Override
  public int compareTo(News news) {
    return date.compareTo(news.getDate());
  }

  public String toString() {
    return news;
  }
}
