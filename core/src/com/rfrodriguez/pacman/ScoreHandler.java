package com.rfrodriguez.pacman;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreHandler {
	public ArrayList<Score> bestScores;
	
	public ScoreHandler(){
		bestScores = new ArrayList<Score>();
	}
	
	public boolean isBestScore(int score){		
		if(bestScores.size()==0){
			return true;
		} else {
			Collections.sort(bestScores);
			Score min = bestScores.get(bestScores.size()-1);
			if(min.playerScore < score)
				return true;
		}
		return false;
	}
	
	public void addScore(Score score){
		bestScores.add(score);
		Collections.sort(bestScores);
	}
	
	public void persist(){
		ScoreService.persist(this);
	}
}
