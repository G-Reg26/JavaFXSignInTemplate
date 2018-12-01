/**
 * The Comment class is the blueprint used for user submitted comments left on team pages.
 * This class holds the comment text as a String and the date it was posted as a Date object.
 *
 * @author Jordan Sasek
 * @version 1.0
 * @since 11/8/2018
 */

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

