package com.rfrodriguez.pacman;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rfrodriguez.pacman.screen.PlayScreen;

public class MyContactListener implements ContactListener{

	PlayScreen playScreen;
	
	public MyContactListener(PlayScreen playScreen) {
		this.playScreen = playScreen;	
	}

	@Override
	public void beginContact(Contact contact) {
		if(isContactBetween(contact, "pacman", "pac")){
			try {
				playScreen.addToDeletionList(getBody(contact, "pac"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(isContactBetween(contact, "pacman", "energizer")){
			try {
				playScreen.addToDeletionList(getBody(contact, "energizer"));
				playScreen.beginFrightenedMode();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(isContactBetween(contact, "pacman", "ghost")){
			try {
				playScreen.checkGhostPacmanContact();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void endContact(Contact contact) { }
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
	
	private Body getBody(Contact contact, String name) throws Exception{
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		
		if(f1.getUserData().equals(name))
			return f1.getBody();
		else if(f2.getUserData().equals(name))
			return f2.getBody();
		else
			throw (new Exception("This contact has no "+name));		
	}
	
	private boolean isContactBetween(Contact contact, String name1, String name2){
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		
		return ((f1.getUserData().equals(name1) && f2.getUserData().equals(name2)) ||
				(f2.getUserData().equals(name1) && f1.getUserData().equals(name2))); 
		
	}

}
