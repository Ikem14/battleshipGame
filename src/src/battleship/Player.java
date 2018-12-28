/*
 * Kia Porter and Chukwubuikem Okafo
 * COSC 330: OO Design Pattern, GUI and Event-driven Programming
 * Project #1: Battleship Game
 * Due October 5, 2018
*/

package src.battleship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Player {
	
	public static final int NUMBEROFSHIPS = 5;
	
	//member variables
	private static Ship[] playerShips;
	private int numShipsAlive;
	static public Grid playerBoard;
	private boolean won;
	private boolean playerTurn;
	
	public Player(boolean turn) {
		
		playerShips = new Ship[NUMBEROFSHIPS];
		createShips();
		setNumShipsAlive(NUMBEROFSHIPS);
		playerBoard = new Grid();
		playerBoard.setVisible(true);
		setWon(false);
		setPlayerTurn(turn);
	}

	private void createShips() {
		
		//create ships
		playerShips[0] = new Ship(Ship.CARRIER);
		playerShips[1] = new Ship(Ship.BATTLESHIP);
		playerShips[2] = new Ship(Ship.CRUISER);
		playerShips[3] = new Ship(Ship.SUBMARINE);
		playerShips[4] = new Ship(Ship.DESTROYER);
	}
	
	public static Ship getShipI(int i) {
		
		return playerShips[i];
	}
	
	public static Ship getShipByType(String type) {
		
		switch (type) {
		
		case Ship.CARRIER:
			return playerShips[0];
		case Ship.BATTLESHIP:
			return playerShips[1];
		case Ship.CRUISER:
			return playerShips[2];
		case Ship.SUBMARINE:
			return playerShips[3];
		case Ship.DESTROYER:
			return playerShips[4];
		
		
		}
		
		return null;
	}
	
	public ImageView getPlayerShip(int i) {
		return playerShips[i].getShipImage(playerShips[i]);
	}

	public int getNumShipsAlive() {
		int count = 0;
		for(int i = 0; i < NUMBEROFSHIPS; i++) {
			
			if(this.playerShips[i].isAlive()) {
				count++;
			}
		}
		setNumShipsAlive(count);
		return numShipsAlive;
	}

	public void setNumShipsAlive(int numShipsAlive) {
		this.numShipsAlive = numShipsAlive;
	}

	public Grid getPlayerBoard() {
		return playerBoard;
	}

	public boolean isPlayerWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public boolean isPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(boolean playerTurn) {
		this.playerTurn = playerTurn;
	}
	
}

