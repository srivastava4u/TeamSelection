package com.sc.team.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.sc.team.model.Player;

/**
 * Template Pattern
 * 
 * @author Sandeep
 *
 */
public abstract class AbstractTeamSelection<T extends Player> {

	public void teamSelectionProcess(String filePath) throws IOException {

		// Parse CSV File
		List<String> allRecords = parseFile(filePath);

		// Convert into Domain Object
		List<T> allPlayer = convertIntoPlayer(allRecords);

		// Filter unfit players
		List<T> fitPlayers = filterFitPlayer(allPlayer);

		// Select Team from the list of fit players
		List<T> selectedPlayer = selectTeam(fitPlayers);

		// Print Selected Team on console
		printSelectedTeam(selectedPlayer);

	}

	private void printSelectedTeam(List<T> selectedPlayer) {
		selectedPlayer.forEach(input -> System.out.println(input));
	}

	protected List<String> parseFile(String filePath) throws IOException {

		Path path = Paths.get(filePath);

		List<String> readAllLines = Files.readAllLines(path);

		System.out.println(String.format("Lines Read from file=%d", readAllLines.size()));

		return readAllLines.subList(1, readAllLines.size());

	}

	protected abstract List<T> convertIntoPlayer(List<String> allRecords);

	protected abstract List<T> selectTeam(List<T> fitPlayers);

	protected abstract List<T> filterFitPlayer(List<T> allPlayer);

}
