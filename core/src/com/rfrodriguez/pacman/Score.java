package com.rfrodriguez.pacman;

public class Score implements Comparable<Score>{
	
	public int playerScore;
	public String playerName, scoreDate;
	
	public Score(){}
	
	public Score(String name, int score, String date){
		playerName = name;
		playerScore = score;
		scoreDate = date;
	}

	@Override
	public int compareTo(Score o) {
		return o.playerScore-playerScore;
	}
}