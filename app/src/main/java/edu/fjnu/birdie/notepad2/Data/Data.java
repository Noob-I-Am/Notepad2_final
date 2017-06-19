package edu.fjnu.birdie.notepad2.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TOSHIBA on 2017/6/19.
 */
public class Data implements Serializable {
    private String info;
    private String id;
    private ArrayList<Text> list;
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public ArrayList<Text> getList() {
        return list;
    }
    public void setList(ArrayList<Text> list) {
        this.list = list;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


}
