package com.example.minesweeper;

public class PlayerInfo implements Comparable<PlayerInfo>{
    String name;
    int score;

    public PlayerInfo(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(PlayerInfo otherPlayer) {
        if(score>otherPlayer.score){
            return 1;
        }
        if(score<otherPlayer.score){
            return -1;
        }
        return 0;
    }
}
