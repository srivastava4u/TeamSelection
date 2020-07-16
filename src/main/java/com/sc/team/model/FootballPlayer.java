package com.sc.team.model;

public class FootballPlayer extends Player {

	private Integer goalScored;

	private Integer goalDefended;

	private FootBallPlayerType playerType;

	private Boolean isSelected = Boolean.FALSE;

	public Integer getGoalScored() {
		return goalScored;
	}

	public FootballPlayer setGoalScored(Integer goalScored) {
		this.goalScored = goalScored;
		return this;
	}

	public Integer getGoalDefended() {
		return goalDefended;
	}

	public FootballPlayer setGoalDefended(Integer goalDefended) {
		this.goalDefended = goalDefended;
		return this;
	}

	public FootBallPlayerType getPlayerType() {
		return playerType;
	}

	public FootballPlayer setPlayerType(FootBallPlayerType playerType) {
		this.playerType = playerType;
		return this;
	}

	public Boolean isSelected() {
		return isSelected;
	}

	public FootballPlayer setSelected(Boolean isSelected) {
		this.isSelected = isSelected;
		return this;
	}

	@Override
	public String toString() {
		return "[ name=" + name + ", playerType=" + playerType + ", isSelected=" + isSelected + "]";
	}

}
