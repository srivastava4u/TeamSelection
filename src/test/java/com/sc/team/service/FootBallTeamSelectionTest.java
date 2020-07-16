package com.sc.team.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sc.team.model.FootBallPlayerType;
import com.sc.team.model.FootballPlayer;

public class FootBallTeamSelectionTest {

	FootballTeamSelection teamSelection = new FootballTeamSelection();

	@Test
	public void test_withOnlyAllRounder() {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(30).setName("A"),
				(FootballPlayer) new FootballPlayer().setGoalScored(60).setGoalDefended(35).setName("B"));

		assertTrue(teamSelection.selectTeam(playersList).size() == 2);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Striker).count() == 1);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Defender).count() == 1);
	}

	@Test
	public void test_withNoAllRounder() {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(10).setName("A"),
				(FootballPlayer) new FootballPlayer().setGoalScored(10).setGoalDefended(50).setName("B"),
				(FootballPlayer) new FootballPlayer().setGoalScored(51).setGoalDefended(0).setName("C"),
				(FootballPlayer) new FootballPlayer().setGoalScored(10).setGoalDefended(30).setName("D"));

		assertTrue(teamSelection.selectTeam(playersList).size() == 4);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Striker).count() == 2);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Defender).count() == 2);
	}

	@Test
	public void test_withEmptyPlayerList() {
		List<FootballPlayer> playersList = new ArrayList<FootballPlayer>();

		assertTrue(teamSelection.selectTeam(playersList).isEmpty());

	}

	@Test
	public void test_withIncorrectCombinationofPlayerList() {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(10).setName("A"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(10).setGoalDefended(50).setName("B"), // Defender
				(FootballPlayer) new FootballPlayer().setGoalScored(51).setGoalDefended(0).setName("C"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(10).setGoalDefended(10).setName("D")); // Bad Player

		assertTrue(teamSelection.selectTeam(playersList).isEmpty());

	}

	@Test
	public void test_withMoreStrikerCombinationofPlayerList() {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(10).setName("A"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(55).setGoalDefended(10).setName("B"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(51).setGoalDefended(60).setName("C"), // All Rounder
				(FootballPlayer) new FootballPlayer().setGoalScored(49).setGoalDefended(35).setName("D")); // Defender

		assertTrue(teamSelection.selectTeam(playersList).size() == 4);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Striker).count() == 2);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Defender).count() == 2);

	}

	@Test
	public void test_withExtraAllRounderCombinationofPlayerList() {
		List<FootballPlayer> playersList = List.of(
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(10).setName("A"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(55).setGoalDefended(10).setName("B"), // Striker
				(FootballPlayer) new FootballPlayer().setGoalScored(51).setGoalDefended(60).setName("C"), // All Rounder
				(FootballPlayer) new FootballPlayer().setGoalScored(50).setGoalDefended(30).setName("D"), // AllRounder
				(FootballPlayer) new FootballPlayer().setGoalScored(52).setGoalDefended(35).setName("E"), // All Rounder
				(FootballPlayer) new FootballPlayer().setGoalScored(49).setGoalDefended(35).setName("F")); // Defender

		assertTrue(teamSelection.selectTeam(playersList).size() == 6);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Striker).count() == 3);
		assertTrue(teamSelection.selectTeam(playersList).stream()
				.filter(player -> player.getPlayerType() == FootBallPlayerType.Defender).count() == 3);

	}

}
