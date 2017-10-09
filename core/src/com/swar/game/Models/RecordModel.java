package com.swar.game.Models;

/**
 * Created by Koma on 19.09.2017.
 */
public class RecordModel {
    int score;
    float time;
    String gameType;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }
    @Override
    public String toString(){
        String result = "";
        result += " Player " + "Time:" + this.getTime() + " Score:" + this.getScore();

        return result;
    }
}
