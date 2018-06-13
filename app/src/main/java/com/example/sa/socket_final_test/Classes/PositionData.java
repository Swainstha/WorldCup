package com.example.sa.socket_final_test.Classes;

/**
 * Created by swainstha on 5/22/18.
 */

public class PositionData {

    private String position;
    private String name;
    private String score;

    public PositionData(String position, String name, String score) {
        this.position = position;
        this.name = name;
        this.score = score;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
