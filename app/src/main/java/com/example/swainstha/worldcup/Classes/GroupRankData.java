package com.example.swainstha.worldcup.Classes;

/**
 * Created by swainstha on 5/29/18.
 */

public class GroupRankData {

    private int id;
    private String teamName;
    private int point;
    private int g_scored;
    private int g_conceded;
    private String group;

    private String rank;
    private String pts;
    private String gs;
    private String gc;

    public GroupRankData(int id, String teamName, int point, int g_scored, int g_conceded) {
        this.id = id;
        this.teamName = teamName;
        this.point = point;
        this.g_scored = g_scored;
        this.g_conceded = g_conceded;
    }

    public GroupRankData(String group) {
        this.group = group;
    }

    public GroupRankData(String rank, String country,String pts, String gs, String gc ) {
        this.rank = rank;
        this.teamName = country;
        this.pts = pts;
        this.gs = gs;
        this.gc = gc;
    }

    public String getPts() {
        return pts;
    }

    public void setPts(String pts) {
        this.pts = pts;
    }

    public String getGs() {
        return gs;
    }

    public void setGs(String gs) {
        this.gs = gs;
    }

    public String getGc() {
        return gc;
    }

    public void setGc(String gc) {
        this.gc = gc;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getG_scored() {
        return g_scored;
    }

    public void setG_scored(int g_scored) {
        this.g_scored = g_scored;
    }

    public int getG_conceded() {
        return g_conceded;
    }

    public void setG_conceded(int g_conceded) {
        this.g_conceded = g_conceded;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
