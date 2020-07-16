package com.sc.team.model;

/**
 * Player POJO
 */
public class Player {

	protected String name;

	protected Double height;

	protected Double bmi;

	public Player setName(String name) {
		this.name = name;
		return this;
	}

	public Player setHeight(Double height) {
		this.height = height;
		return this;
	}

	public Player setBmi(Double bmi) {
		this.bmi = bmi;
		return this;
	}

	public String getName() {
		return name;
	}

	public Double getHeight() {
		return height;
	}

	public Double getBmi() {
		return bmi;
	}

}