package com.sc.team.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sc.team.exception.FootBallSelectionexception;
import com.sc.team.model.FootBallPlayerType;
import com.sc.team.model.FootballPlayer;
import com.sc.team.util.TeamUtil;

public class FootballTeamSelection extends AbstractTeamSelection<FootballPlayer> {

	private Integer teamSize = 11;

	private static final Double HEIGHT_CUTOFF = 5.5;
	private static final Double BMI_CUTOFF = 24d;
	private static final Integer GOAL_CUTOFF = 50;
	private static final Integer GOAL_SAVED_CUTOFF = 30;

	final Predicate<FootballPlayer> heightFilter = player -> HEIGHT_CUTOFF.compareTo(player.getHeight()) <= 0;

	final Predicate<FootballPlayer> bmiFilter = player -> BMI_CUTOFF.compareTo(player.getBmi()) >= 0;

	final Predicate<FootballPlayer> goalScoredFilter = player -> GOAL_CUTOFF.compareTo(player.getGoalScored()) <= 0;

	final Predicate<FootballPlayer> goalSavedFilter = player -> GOAL_SAVED_CUTOFF
			.compareTo(player.getGoalDefended()) <= 0;

	@Override
	protected List<FootballPlayer> convertIntoPlayer(List<String> allRecords) {
		return TeamUtil.convertIntoPlayer(allRecords);
	}

	@Override
	protected List<FootballPlayer> filterFitPlayer(List<FootballPlayer> allPlayer) {

		List<FootballPlayer> playerFitInHeightAndBmi = allPlayer.stream().filter(heightFilter.and(bmiFilter))
				.collect(Collectors.toList());
		System.out.println(String.format("Number of players Fit = %d out of total = %d", playerFitInHeightAndBmi.size(),
				allPlayer.size()));
		return playerFitInHeightAndBmi;

	}

	/**
	 * Contains main algorithm to select team
	 * 
	 * @throws FootBallSelectionexception
	 */
	@Override
	protected List<FootballPlayer> selectTeam(List<FootballPlayer> fitPlayers) throws FootBallSelectionexception {

		if (fitPlayers.isEmpty())
			return List.of();

		List<FootballPlayer> selectedAllRounder = fitPlayers.stream().filter(goalScoredFilter.and(goalSavedFilter))
				.collect(Collectors.toList());
		System.out.println(String.format("All Rounder players = %d", selectedAllRounder.size()));

		List<FootballPlayer> selectedStriker = fitPlayers.stream()
				.filter(goalScoredFilter.and(goalSavedFilter.negate())).collect(Collectors.toList());
		System.out.println(String.format("Striker players = %d", selectedStriker.size()));

		List<FootballPlayer> selectedDefender = fitPlayers.stream()
				.filter(goalSavedFilter.and(goalScoredFilter.negate())).collect(Collectors.toList());
		System.out.println(String.format("Defender players = %d", selectedDefender.size()));

		List<FootballPlayer> selectedTeam = createBestPossibleTeam(selectedStriker, selectedDefender,
				selectedAllRounder);
		System.out.println(String.format("Players Selected in Team = %d", selectedTeam.size()));

		return selectedTeam;
	}

	/**
	 * Forms best possible team combination from the eligible player list
	 * 
	 * @param selectedStriker
	 * @param selectedDefender
	 * @param selectedAllRounder
	 * @return
	 * @throws FootBallSelectionexception
	 */
	private List<FootballPlayer> createBestPossibleTeam(List<FootballPlayer> selectedStriker,
			List<FootballPlayer> selectedDefender, List<FootballPlayer> selectedAllRounder)
			throws FootBallSelectionexception {

		Map<FootBallPlayerType, List<FootballPlayer>> result = new HashMap<FootBallPlayerType, List<FootballPlayer>>();

		int totalPossiblePlayers = Math.min(selectedStriker.size(), selectedDefender.size()) * 2
				+ selectedAllRounder.size();

		if (totalPossiblePlayers < getTeamSize() || selectedAllRounder.size() == 0)
			throw new FootBallSelectionexception("Unable to form Team as there is no all rounder");

		// Striker and Defender number difference
		int difference = selectedStriker.size() - selectedDefender.size();
		System.out.println(String.format("Difference Between Striker and Defender Selected is %d", difference));

		result = distAllRounderToEqualStrikerAndDefender(selectedStriker, selectedDefender, selectedAllRounder,
				difference);

		List<FootballPlayer> markSelectedPlayer = markSelectedPlayer(result);

		if (markSelectedPlayer.size() != getTeamSize())
			throw new FootBallSelectionexception("Unable to reach team size");

		return markSelectedPlayer;
	}

	/**
	 * Players selected for team should be marked as Striker or Defender depending
	 * upon their profile and team combination
	 * 
	 * @param selectedStriker
	 * @param selectedDefender
	 * @return
	 */
	private List<FootballPlayer> markSelectedPlayer(Map<FootBallPlayerType, List<FootballPlayer>> result) {

		List<FootballPlayer> finalSelectedTeam = new ArrayList<>();

		result.get(FootBallPlayerType.Striker).forEach(player -> finalSelectedTeam
				.add(player.setPlayerType(FootBallPlayerType.Striker).setSelected(Boolean.TRUE)));
		result.get(FootBallPlayerType.Defender).forEach(player -> finalSelectedTeam
				.add(player.setPlayerType(FootBallPlayerType.Defender).setSelected(Boolean.TRUE)));
		result.get(FootBallPlayerType.Allrounder).forEach(player -> finalSelectedTeam
				.add(player.setPlayerType(FootBallPlayerType.Allrounder).setSelected(Boolean.TRUE)));

		return finalSelectedTeam;
	}

	/**
	 * In case Striker and Defender are not in equal number this method will help in
	 * picking equal striker and defender and rest player as all rounder to reach
	 * the team size
	 * 
	 * @param selectedStriker
	 * @param selectedDefender
	 * @param selectedAllRounder
	 * @param difference
	 */
	private Map<FootBallPlayerType, List<FootballPlayer>> distAllRounderToEqualStrikerAndDefender(
			List<FootballPlayer> selectedStriker, List<FootballPlayer> selectedDefender,
			List<FootballPlayer> selectedAllRounder, int difference) {

		Map<FootBallPlayerType, List<FootballPlayer>> result = new HashMap<FootBallPlayerType, List<FootballPlayer>>();
		List<FootballPlayer> resultStriker = new ArrayList<FootballPlayer>();
		List<FootballPlayer> resultDefender = new ArrayList<FootballPlayer>();
		List<FootballPlayer> resultAllRounder = new ArrayList<FootballPlayer>();

		if (difference < 0) {
			resultDefender.addAll(selectedDefender.subList(0, selectedStriker.size()));
			resultStriker.addAll(selectedStriker);
		} else {
			resultStriker.addAll(selectedStriker.subList(0, selectedDefender.size()));
			resultDefender.addAll(selectedDefender);
		}

		System.out.println(String.format("Selected Defender and Striker are %d and %d", resultDefender.size(),
				resultStriker.size()));

		resultAllRounder.addAll(selectedAllRounder.subList(0, getTeamSize() - resultStriker.size() * 2));

		populateMap(result, resultStriker, resultDefender, resultAllRounder);

		return result;

	}

	/**
	 * Object Holder
	 * 
	 * @param result
	 * @param selectedStriker
	 * @param selectedDefender
	 * @param selectedAllRounder
	 */
	private void populateMap(Map<FootBallPlayerType, List<FootballPlayer>> result, List<FootballPlayer> selectedStriker,
			List<FootballPlayer> selectedDefender, List<FootballPlayer> selectedAllRounder) {
		result.put(FootBallPlayerType.Striker, selectedStriker);
		result.put(FootBallPlayerType.Defender, selectedDefender);
		result.put(FootBallPlayerType.Allrounder, selectedAllRounder);

	}

	public Integer getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(Integer teamSize) {
		this.teamSize = teamSize;
	}

}
