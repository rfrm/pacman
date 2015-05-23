package com.rfrodriguez.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class ScoreService {
	
	private static final String SCORES_DATA_FILE = "data/scores.json";
	
	public static void persist(ScoreHandler scoreHandler){
		Json json = new Json();
		FileHandle scoresFile = Gdx.files.local(SCORES_DATA_FILE);
		String scores = json.toJson(scoreHandler);
		String scoresEncoded = Base64Coder.encodeString(scores);
		scoresFile.writeString(scoresEncoded, false);
	}
	
	public static ScoreHandler loadScoreHandler(){
		Json json = new Json();
		ScoreHandler scoreHandler = null;
		//System.out.println("Working Directory = " + System.getProperty("user.dir"));
		FileHandle scoresFile = Gdx.files.local(SCORES_DATA_FILE);
		if(scoresFile.exists()){
			try{
				String scoresEncoded = scoresFile.readString();
				String scores = Base64Coder.decodeString(scoresEncoded);
				scoreHandler = json.fromJson(ScoreHandler.class, scores);
			} catch (Exception e){
				System.out.println("Unable to parse existing profile data file" + e);
				return new ScoreHandler();
			}
		}		
		else{
			scoreHandler = new ScoreHandler();
			persist(scoreHandler);			
		}
		return scoreHandler;
	}
}
