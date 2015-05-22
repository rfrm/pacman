package com.rfrodriguez.pacman.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Timer;
import com.rfrodriguez.pacman.Ghost;
import com.rfrodriguez.pacman.Ghost.GhostState;

class StateTransition {
	public int duration;
	public GhostState state;

	public StateTransition(int d, GhostState s){
		duration = d;
		state = s;
	}
}

public class StateManager {
	private Ghost ghost;
	private StateTransition currentTransition;
	private Timer.Task changeTofrightenedEnding, changeToNormal, transitionChange;
	private GhostState currentState;
	
	private StateTransition lastTransition = new StateTransition(Integer.MAX_VALUE, GhostState.chase);
	
	private StateTransition[] stateTransitions = {
		new StateTransition(7, GhostState.scatter),
		new StateTransition(20, GhostState.chase),
		new StateTransition(7, GhostState.scatter),
		new StateTransition(20, GhostState.chase),
		new StateTransition(5, GhostState.scatter),
		new StateTransition(20, GhostState.chase),
		new StateTransition(5, GhostState.scatter),
		lastTransition
	};
	
	private ArrayList<StateTransition> stateTransitionList = new ArrayList<StateTransition> (
		Arrays.asList(stateTransitions)
	);
	
	private Iterator<StateTransition> stateIterator;
	
	public StateManager(Ghost g){
		ghost = g;
		currentState = GhostState.stoped;
		stateIterator = stateTransitionList.iterator();
	}
	
	public void init(){
		updateTransition();
		changeTofrightenedEnding = new Timer.Task() {
			@Override
			public void run() {}
		};
		changeToNormal = new Timer.Task() {
			@Override
			public void run() {}
		};
	}
	
	public void updateTransition(){
		if(stateIterator.hasNext()){
			currentTransition = stateIterator.next();
			currentState = currentTransition.state;			
			setTimer();
		}
		else{
			currentTransition = lastTransition;
			currentState = currentTransition.state;
		}
	}
	
	public void setTimer(){
		transitionChange = Timer.schedule(new Timer.Task() {		
			@Override
			public void run() {
				updateTransition();
			}
		}, currentTransition.duration);
	}
	
	public GhostState currentState(){
		return currentState;
	}	
	
	public void kill(){
		currentState = GhostState.dead;
	}
	
	public void notifyHome(){
		if(currentState == GhostState.dead){
			ghost.penalize();			
		}		
	}
	
	public void penalize(){
		currentState = GhostState.penalized;
		Timer.schedule(new Timer.Task() {			
			@Override
			public void run() {
				updateTransition();				
			}
		}, 5);
	}
	
	public void setFrightened(){
		if(ghost.isNormal() || ghost.isFrightened()){
			cancellTaks();
			currentState = GhostState.frightened;
			changeTofrightenedEnding = Timer.schedule(new Timer.Task() {
				@Override
				public void run() {				 
					setFrightenedEnding();
				}
			}, 12);
		}
	}
	
	private void setFrightenedEnding(){
		if(ghost.isFrightened()){
			cancellTaks();
			currentState = GhostState.frightened_ending;
			changeToNormal = Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					updateTransition();
				}
			}, 3);
		}
	}
	
	private void cancellTaks(){
		changeToNormal.cancel();
		transitionChange.cancel();
		changeTofrightenedEnding.cancel();
	}
}
