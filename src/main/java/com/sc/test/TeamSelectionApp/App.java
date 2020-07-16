package com.sc.test.TeamSelectionApp;

import java.io.File;
import java.io.IOException;

import com.sc.team.model.FootballPlayer;
import com.sc.team.service.AbstractTeamSelection;
import com.sc.team.service.FootballTeamSelection;

public class App {

	public static void main(String[] args) throws IOException {

		checkArgs(args);

		String filePath = args[0];
		System.out.println(String.format("File Path received as input=%s", filePath));

		File inputFile = new File(filePath);

		if (!inputFile.exists())
			throw new IllegalArgumentException(String.format("File doesn't exists=%s", filePath));

		AbstractTeamSelection<FootballPlayer> footballTeamSelection = new FootballTeamSelection();

		footballTeamSelection.teamSelectionProcess(filePath);

	}

	private static void checkArgs(String[] args) {

		if (args != null && args.length == 1)
			return;

		throw new IllegalArgumentException("Expected 1 argument with value file path");
	}

}
