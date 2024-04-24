package com.teamgames.endpoints.vote.obj;

/**
 * @author Nelson Sanchez
 * Represents the details of a reward claimed by a player through the TeamGames Vote API.
 * This class is used to hold the data retrieved from the API response after a player claims a reward.
 */
public class ClaimReward {

    /** The total number of votes the player has made. */
    public String total_votes;

    /** The number of vote points the player has, which can be used to claim rewards. */
    public int vote_points;

    /** The number of votes the player has made in the current month. */
    public int votes_month;

    /** The username of the player who is claiming the reward. */
    public String username;

    /** The amount of the reward given to the player. */
    public int give_amount;

    /** The unique identifier of the reward. */
    public int reward_id;

    /** The name of the reward. */
    public String reward_name;

    /** The amount of the specific reward type to be given. */
    public int reward_amount;

    /** A message associated with the reward, for example a reason if the request was rejected. */
    public String message;

}
