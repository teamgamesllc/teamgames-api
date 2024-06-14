package com.teamgames.endpoints.leaderboard;

public class PlayerMetric {
    private String name;
    private int level;
    private int experience;

    public PlayerMetric(String name) {
        this.name = name;
    }

    public PlayerMetric setValue(int level) {
        this.level = level;
        return this;
    }

    public PlayerMetric setProgress(int experience) {
        this.experience = experience;
        return this;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return level;
    }

    public int getProgress() {
        return experience;
    }
}
