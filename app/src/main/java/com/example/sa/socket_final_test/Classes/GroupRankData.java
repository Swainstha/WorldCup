package com.example.sa.socket_final_test.Classes;

/**
 * Created by swainstha on 5/29/18.
 */

public class GroupRankData {

    private String country;
    private String pts;

    private String group;
    private String rank;

    private String mp;
    private String win;
    private String draw;
    private String lose;

    public GroupRankData(String group) {
        this.group = group;
    }

    public GroupRankData(String rank, String country, String pts, String mp, String win, String draw, String lose) {
        this.rank = rank;
        this.country = country;
        this.pts = pts;
        this.mp = mp;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPts() {
        return pts;
    }

    public void setPts(String pts) {
        this.pts = pts;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }
}
