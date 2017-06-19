package edu.fjnu.birdie.notepad2.Data;

import java.io.Serializable;

/**
 * Created by TOSHIBA on 2017/6/19.
 */
public class Text implements Serializable {

    String id;
    String title;
    String contain;
    String date;
    String dirid;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContain() {
        return contain;
    }
    public void setContain(String contain) {
        this.contain = contain;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDirid() {
        return dirid;
    }
    public void setDirid(String dirid) {
        this.dirid = dirid;
    }
}
