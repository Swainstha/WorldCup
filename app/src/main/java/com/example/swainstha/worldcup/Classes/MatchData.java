package com.example.swainstha.worldcup.Classes;

/**
 * Created by swainstha on 5/22/18.
 */

public class MatchData {

    private String team1;
    private String team2;
    private String score1;
    private String score2;
    private String group;
    private String date;
    private String time;

    public MatchData(String team1, String score1, String score2, String team2, String date, String time) {
        this.team1 = team1;
        this.team2 = team2;
        this.score1 = score1;
        this.score2 = score2;
        this.date = date;
        this.time = time;
    }

    public MatchData(String group) {
        this.group = group;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
