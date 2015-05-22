package com.rfrodriguez.pacman.ai;

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
	private int currentTransitionIndex;
	private StateTransition currentTransition;
	private Timer.Task changeTofrightenedEnding, changeToNormal, transitionChange;
	private GhostState currentState;
	
	private StateTransition[] stateTransitions = {
			new StateTransition(7, GhostState.scatter),
			new StateTransition(20, GhostState.chase),
			new StateTransition(7, GhostState.scatter),
			new StateTransition(20, GhostState.chase),
			new StateTransition(5, GhostState.scatter),			
			new StateTransition(20, GhostState.chase),
			new StateTransition(5, GhostState.scatter),
			new StateTransition(Integer.MAX_VALUE, GhostState.chase),			
	};
	
	public StateManager(Ghost g){
		ghost = g;		
		currentTransitionIndex = 0;
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
		currentTransition = stateTransitions[currentTransitionIndex++];
		currentState = currentTransition.state;
		
		System.out.println(ghost.name+" current state is "+currentState);
		setTimer();
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
