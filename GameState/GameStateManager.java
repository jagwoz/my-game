package GameState;

import Entity.Enemy;
import Entity.Player;
import Equipment.Inventory;
import TileMap.TileMap;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
public class GameStateManager {
	private GameState[] gameStates;
	private int currentState;
	public static final int NUM_GAME_STATES = 10;
	public static final int MENU_STATE = 1;
	public static final int LEVEL1_STATE = 2;
	public static final int ESCAPE = 10;

	public GameStateManager() {
		gameStates = new GameState[NUM_GAME_STATES];
		currentState = MENU_STATE;
		loadState(null, currentState,null,null);
	}

	private void loadState(TileMap tm, int state, Player p, Inventory i,int pau, ArrayList<Enemy> e) {
		if(state == MENU_STATE)
			gameStates[state] = new Menu(tm,this, p, i);
		else if(state == LEVEL1_STATE)
			gameStates[state] = new Level1State(tm,this, p,i,pau,e);
		else if(state == ESCAPE)
			gameStates[state] = new Escape(tm , this, p, i, pau, e);
		else
		switch (state){
			case 3: gameStates[state] = new Missions(tm,this, p,i); break;
			case 4: gameStates[state] = new Armory(tm,this, p,i); break;
			case 5: gameStates[state] = new Jewelry(tm,this, p,i); break;
			case 6: gameStates[state] = new Miscellangeous(tm,this, p,i); break;
			case 7: gameStates[state] = new Blacksmith(tm,this, p,i); break;
			case 8: gameStates[state] = new Gambler(tm,this, p,i); break;
			case 9: gameStates[state] = new WizardState(tm,this, p,i); break;
			default: break;
		}
	}

	private void loadState(TileMap tm, int state, Player p, Inventory i) {
		if(state == MENU_STATE)
			gameStates[state] = new Menu(tm,this, p, i);
		else if(state == LEVEL1_STATE)
			gameStates[state] = new Level1State(tm,this, p,i);
		switch (state){
			case 3: gameStates[state] = new Missions(tm,this, p,i); break;
			case 4: gameStates[state] = new Armory(tm,this, p,i); break;
			case 5: gameStates[state] = new Jewelry(tm,this, p,i); break;
			case 6: gameStates[state] = new Miscellangeous(tm,this, p,i); break;
			case 7: gameStates[state] = new Blacksmith(tm,this, p,i); break;
			case 8: gameStates[state] = new Gambler(tm,this, p,i); break;
			case 9: gameStates[state] = new WizardState(tm,this, p,i); break;
			default: break;
		}
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(TileMap tm, int state, Player p, Inventory i, int pau, ArrayList<Enemy> e) {
		unloadState(currentState);
		currentState = state;
		loadState(tm, currentState,p,i,pau,e);
	}

	public void setState(TileMap tm, int state, Player p, Inventory i) {
		unloadState(currentState);
		currentState = state;
		loadState(tm, currentState,p,i);
	}

	public void update() {
		try {
			gameStates[currentState].update();
		} catch(Exception e) {}
	}

	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch(Exception e) {}
	}

	public void keyPressed(KeyEvent ke, int k) {
		gameStates[currentState].keyPressed(ke, k);
	}
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
	public void mouseClicked(MouseEvent e) { gameStates[currentState].mouseClicked(e); }
	public void mousePressed(MouseEvent e) { gameStates[currentState].mousePressed(e); }
	public void mouseReleased(MouseEvent e) { gameStates[currentState].mouseReleased(e); }
	public void mouseEntered(MouseEvent e) { gameStates[currentState].mouseEntered(e); }
	public void mouseExited(MouseEvent e) { gameStates[currentState].mouseExited(e); }
	public void mouseDragged(MouseEvent e) { gameStates[currentState].mouseDragged(e); }
	public void mouseMoved(MouseEvent e) { gameStates[currentState].mouseMoved(e); }
}