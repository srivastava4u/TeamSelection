package com.sc.team.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sc.team.model.FootBallPlayerType;
import com.sc.team.model.FootballPlayer;
import com.sc.team.util.TeamUtil;

public class FootballTeamSelection extends AbstractTeamSelection<FootballPlayer> {

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
	 */
	@Override
	protected List<FootballPlayer> selectTeam(List<FootballPlayer> fitPlayers) {

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
	 */
	private List<FootballPlayer> createBestPossibleTeam(List<FootballPlayer> selectedStriker,
			List<FootballPlayer> selectedDefender, List<FootballPlayer> selectedAllRounder) {

		// Striker and Defender number difference
		int difference = selectedStriker.size() - selectedDefender.size();
		System.out.println(String.format("Difference Between Striker and Defender Selected is %d", difference));

		if (difference == 0 && selectedAllRounder.size() % 2 == 0)
			// Striker and Defender are Equal and distribute all rounder equally
			distributeAllRounderEqually(selectedStriker, selectedDefender, selectedAllRounder);
		else if (difference != 0 && selectedAllRounder.size() >= Math.abs(difference)
				&& (selectedAllRounder.size() - difference) % 2 == 0)
			// Striker and Defender are Unequal but all rounder can fill that gap
			distAllRounderToEqualStrikerAndDefender(selectedStriker, selectedDefender, selectedAllRounder, difference);
		else {
			System.out.println("Unable to form Team");
			return List.of();
		}

		return markSelectedPlayer(selectedStriker, selectedDefender);
	}

	/**
	 * Players selected for team should be marked as Striker or Defender depending
	 * upon their profile and team combination
	 * 
	 * @param selectedStriker
	 * @param selectedDefender
	 * @return
	 */
	private List<FootballPlayer> markSelectedPlayer(List<FootballPlayer> selectedStriker,
			List<FootballPlayer> selectedDefender) {

		List<FootballPlayer> finalSelectedTeam = new ArrayList<>();

		selectedStriker.forEach(player -> player.setPlayerType(FootBallPlayerType.Striker).setSelected(Boolean.TRUE));
		selectedDefender.forEach(player -> player.setPlayerType(FootBallPlayerType.Defender).setSelected(Boolean.TRUE));

		finalSelectedTeam.addAll(selectedStriker);
		finalSelectedTeam.addAll(selectedDefender);

		return finalSelectedTeam;
	}

	/**
	 * In case Striker and Defender are not in equal number this method will help in
	 * classifying all rounder accordingly as striker or defender to even out the
	 * number
	 * 
	 * @param selectedStriker
	 * @param selectedDefender
	 * @param selectedAllRounder
	 * @param difference
	 */
	private void distAllRounderToEqualStrikerAndDefender(List<FootballPlayer> selectedStriker,
			List<FootballPlayer> selectedDefender, List<FootballPlayer> selectedAllRounder, int difference) {

		List<FootballPlayer> differencePlayerList = selectedAllRounder.subList(0, Math.abs(difference));

		List<FootballPlayer> additionalAllRounderPlayerList = selectedAllRounder.subList(Math.abs(difference),
				selectedAllRounder.size());

		if (difference < 0)
			selectedStriker.addAll(differencePlayerList);
		else
			selectedDefender.addAll(differencePlayerList);

		distributeAllRounderEqually(selectedStriker, selectedDefender, additionalAllRounderPlayerList);

	}

	/**
	 * Distributes All Rounder equally between Striker and Defender list
	 * 
	 * @param selectedStriker
	 * @param selectedDefender
	 * @param selectedAllRounder
	 */
	private void distributeAllRounderEqually(List<FootballPlayer> selectedStriker,
			List<FootballPlayer> selectedDefender, List<FootballPlayer> selectedAllRounder) {

		for (int i = 0; i < selectedAllRounder.size(); i++) {

			if (i % 2 == 0)
				selectedStriker.add(selectedAllRounder.get(i));
			else
				selectedDefender.add(selectedAllRounder.get(i));

		}

	}

}
