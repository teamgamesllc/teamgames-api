package com.teamgames.endpoints.vote.obj;

public class ClaimReward {

	public String total_votes;
	public int vote_points;
	public int votes_month;
	public String username;
	public int give_amount;
	public int reward_id;
	public String reward_name;
	public int reward_amount;
	public String message;

	public String getTotal_votes() {
		return total_votes;
	}

	public void setTotal_votes(String total_votes) {
		this.total_votes = total_votes;
	}

	public int getVote_points() {
		return vote_points;
	}

	public void setVote_points(int vote_points) {
		this.vote_points = vote_points;
	}

	public int getVotes_month() {
		return votes_month;
	}

	public void setVotes_month(int votes_month) {
		this.votes_month = votes_month;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGive_amount() {
		return give_amount;
	}

	public void setGive_amount(int give_amount) {
		this.give_amount = give_amount;
	}

	public int getReward_id() {
		return reward_id;
	}

	public void setReward_id(int reward_id) {
		this.reward_id = reward_id;
	}

	public String getReward_name() {
		return reward_name;
	}

	public void setReward_name(String reward_name) {
		this.reward_name = reward_name;
	}

	public int getReward_amount() {
		return reward_amount;
	}

	public void setReward_amount(int reward_amount) {
		this.reward_amount = reward_amount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
