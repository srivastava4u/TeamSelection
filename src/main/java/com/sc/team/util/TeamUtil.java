package com.sc.team.util;

import java.util.ArrayList;
import java.util.List;

import com.sc.team.model.FootballPlayer;
import com.sc.team.model.Player;

/**
 * Util Class
 * 
 * @author Sandeep
 *
 */
public class TeamUtil {

	public static final String FIELD_SEPERATOR = ",";
	private static final Integer NUMBER_OF_FIELDS_EXPECTED = 5;

	public static Player convertIntoPlayer(String inputLine) {
		if (inputLine == null || inputLine.isEmpty())
			return null;

		String[] fields = inputLine.split(FIELD_SEPERATOR);

		if (fields.length != NUMBER_OF_FIELDS_EXPECTED)
			return null;

		Player player = new FootballPlayer().setGoalDefended(Integer.valueOf(fields[4]))
				.setGoalScored(Integer.valueOf(fields[3])).setName(fields[0]).setHeight(Double.valueOf(fields[1]))
				.setBmi(Double.valueOf(fields[2]));

		return player;

	}

	public static List<FootballPlayer> convertIntoPlayer(List<String> inputLine) {
		final List<FootballPlayer> playerList = new ArrayList<>();

		inputLine.forEach(input -> {
			Player player = convertIntoPlayer(input);
			if (player != null)
				playerList.add((FootballPlayer) player);
		});

		return playerList;
	}

}
