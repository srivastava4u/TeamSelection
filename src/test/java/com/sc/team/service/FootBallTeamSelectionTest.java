package com.sc.team.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sc.team.exception.FootBallSelectionexception;
import com.sc.team.model.FootBallPlayerType;
import com.sc.team.model.FootballPlayer;

public class FootBallTeamSelectionTest {

	FootballTeamSelection teamSelection = new FootballTeamSelection();

	@Test
	public void test_withOnlyAllRounder() throws FootBallSelectionexception {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(30).setName("A"),
				(FootballPlayer) new FootballPlayer().setGoalScored(70).setGoalDefended(35).setName("B"),
				(FootballPlayer) new FootballPlayer().setGoalScored(60).setGoalDefended(35).setName("C"));

		teamSelection.setTeamSize(3);

		List<FootballPlayer> selectedTeam = teamSelection.selectTeam(playersList);
		assertTrue(selectedTeam.size() == 3);
		assertTrue(selectedTeam.stream().filter(player -> player.getPlayerType() == FootBallPlayerType.Allrounder)
				.count() == 3);

	}

	@Test(expected = FootBallSelectionexception.class)
	public void test_withNoAllRounder() throws FootBallSelectionexception {

		teamSelection.setTeamSize(3);
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(10).setName("A"),
				(FootballPlayer) new FootballPlayer().setGoalScored(10).setGoalDefended(50).setName("B"),
				(FootballPlayer) new FootballPlayer().setGoalScored(51).setGoalDefended(0).setName("C"),
				(FootballPlayer) new FootballPlayer().setGoalScored(10).setGoalDefended(30).setName("D"));

		teamSelection.selectTeam(playersList);
	}

	@Test
	public void test_withEmptyPlayerList() throws FootBallSelectionexception {
		List<FootballPlayer> playersList = new ArrayList<FootballPlayer>();

		assertTrue(teamSelection.selectTeam(playersList).isEmpty());

	}

	@Test(expected = FootBallSelectionexception.class)
	public void test_withIncorrectCombinationofPlayerList() throws FootBallSelectionexception {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(10).setName("A"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(10).setGoalDefended(50).setName("B"), // Defender
				(FootballPlayer) new FootballPlayer().setGoalScored(51).setGoalDefended(0).setName("C"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(10).setGoalDefended(10).setName("D")); // Bad Player

		teamSelection.setTeamSize(3);

		teamSelection.selectTeam(playersList);

	}

	@Test
	public void test_withMoreStrikerCombinationofPlayerList() throws FootBallSelectionexception {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(10).setName("A"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(55).setGoalDefended(10).setName("B"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(51).setGoalDefended(60).setName("C"), // All Rounder
				(FootballPlayer) new FootballPlayer().setGoalScored(49).setGoalDefended(35).setName("D")); // Defender

		teamSelection.setTeamSize(3);
		assertTrue(teamSelection.selectTeam(playersList).size() == 3);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Striker).count() == 1);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Defender).count() == 1);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Allrounder).count() == 1);

	}

	@Test
	public void test_withExtraAllRounderCombinationofPlayerList() throws FootBallSelectionexception {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(10).setName("A"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(55).setGoalDefended(10).setName("B"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(51).setGoalDefended(60).setName("C"), // All Rounder
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(30).setName("D"), // AllRounder
				(FootballPlayer) new FootballPlayer().setGoalScored(52).setGoalDefended(35).setName("E"), // All Rounder
				(FootballPlayer) new FootballPlayer().setGoalScored(49).setGoalDefended(35).setName("F")); // Defender

		teamSelection.setTeamSize(4);
		assertTrue(teamSelection.selectTeam(playersList).size() == 4);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Striker).count() == 1);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Defender).count() == 1);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Allrounder).count() == 2);
	}

}
