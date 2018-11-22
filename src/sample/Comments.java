package sample;

import java.util.Date;

public class Comments {

  private String text;
  private Date date;

  public Comments() {
    text = "";
    date = null;
  }

  public Comments(String text, Date date) {
    this.text = text;
    this.date = date;
  }

  public String getText() {
    return text;
  }

  public Date getDate() {
    return date;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String toString() {
    return date.toString() + " " + text;
  }
}

