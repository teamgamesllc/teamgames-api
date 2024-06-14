package com.teamgames.endpoints.vote.obj;

import com.teamgames.lib.gson.SerializedName;

/**
 * Represents the details of a reward claimed by a player through the TeamGames Vote API.
 * This class is designed to be thread-safe to allow concurrent access to the reward details.
 */
public class ClaimReward {
    private final String username; // Immutable field does not need synchronization
    private volatile int rewardAmount;

    @SerializedName("message")
    public volatile String message;
    
    @SerializedName("total_points")
    public volatile String totalVotes; 
    @SerializedName("vote_points")
    public volatile int votePoints;
    @SerializedName("votes_month")
    public volatile int votesMonth;
    @SerializedName("give_amount")
    public volatile int giveAmount;
    @SerializedName("reward_id")
    public volatile int rewardId;
    @SerializedName("reward_name")
    public volatile String rewardName; 

    // Constructor
    public ClaimReward(String username, String rewardName) {
        this.username = username;
        this.rewardName = rewardName;
    }

    // Thread-safe getter and setter for totalVotes
    public synchronized String getTotalVotes() {
        return totalVotes;
    }

    public synchronized void setTotalVotes(String totalVotes) {
        this.totalVotes = totalVotes;
    }

    // Thread-safe getter and setter for votePoints
    public synchronized int getVotePoints() {
        return votePoints;
    }

    public synchronized void setVotePoints(int votePoints) {
        this.votePoints = votePoints;
    }

    // Thread-safe getter and setter for votesMonth
    public synchronized int getVotesMonth() {
        return votesMonth;
    }

    public synchronized void setVotesMonth(int votesMonth) {
        this.votesMonth = votesMonth;
    }

    // Immutable getter for username
    public String getUsername() {
        return username;
    }

    // Thread-safe getter and setter for giveAmount
    public synchronized int getGiveAmount() {
        return giveAmount;
    }

    public synchronized void setGiveAmount(int giveAmount) {
        this.giveAmount = giveAmount;
    }

    // Thread-safe getter and setter for rewardId
    public synchronized int getRewardId() {
        return rewardId;
    }

    public synchronized void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    // Immutable getter for rewardName
    public String getRewardName() {
        return rewardName;
    }

    // Thread-safe getter and setter for rewardAmount
    public synchronized int getRewardAmount() {
        return rewardAmount;
    }

    public synchronized void setRewardAmount(int rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    // Thread-safe getter and setter for message
    public synchronized String getMessage() {
        return message;
    }

    public synchronized void setMessage(String message) {
        this.message = message;
    }
}
