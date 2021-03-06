# TeamSelection

Problem Statement :

	Build a application to select football team with equal number of Striker and Defenders in the team with rest player as All Rounder.
	
Input :

	List of players will be received as an input in a csv file in below fixed format
		Player Name,Height,BMI,Goal Scored,Goal Defended
	
Selection Criteria :

	1. Fit player should be only considered for team selection
		BMI < 24 and Height > 5.5
	2. Striker player should have scored at least 50 goals
	3. Defender player should have saved at least 30 goals
	4. Striker and Defender should be even out and rest players should be all rounder
	5. Team size should be matched i.e. 11 in our case
	
Solution :

	1. Used a Template pattern to define skeleton of team selection algorithm.
		AbstractTeamSelection.class
	   Template will help us in extending this code for other sports team selection
	2. Builder pattern has been used for constructing player profile object
	3. Code is compatible with Java 8 version
	
Assumptions :

	1. Input File will have data in correct format and no validation has been put in place.
	   Code can be improved by adding validation once business validation requirement is elaborated
	2. Fitness of player has been assumed a common criteria but can be overridden if required
	3. Output of result has been put in console and can be extended to write the result in a file.
	4. Only players selected in the team is getting printed as it will be too verbose to print all players.
	5. JUnit test case has been written to cover selection criterion. File Parsing and Fitness criteria test should be written for more coverage.
	