/*
 * Kia Porter and Chukwubuikem Okafo
 * COSC 330: OO Design Pattern, GUI and Event-driven Programming
 * Project #1: Battleship Game
 * Due October 5, 2018
*/

package src.battleship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player2 {
		
	public static final int NUMBEROFSHIPS = 5;
	
	//member variables
	private Ship[] playerShips;
	private int numShipsAlive;
	//private Grid opponentBoard;
	private Grid2 playerBoard;
	private boolean won;
	private boolean playerTurn;
	
	public Player2(boolean turn) {
		
		playerShips = new Ship[NUMBEROFSHIPS];
		createShips();
		setNumShipsAlive(NUMBEROFSHIPS);
		//setOpponentBoard(new Grid());
		playerBoard = new Grid2();
		playerBoard.setVisible(true);
		setWon(false);
		setPlayerTurn(turn);
	}

	private void createShips() {
		
		//create ships
		playerShips[0] = new Ship(Ship.CARRIER);
		playerShips[1] = new Ship(Ship.CRUISER);
		playerShips[2] = new Ship(Ship.SUBMARINE);
		playerShips[3] = new Ship(Ship.DESTROYER);
		playerShips[4] = new Ship(Ship.BATTLESHIP);
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
/*
	public Grid getOpponentBoard() {
		return opponentBoard;
	}

	public void setOpponentBoard(Grid opponentBoard) {
		this.opponentBoard = opponentBoard;
	}*/

	public Grid2 getPlayerBoard() {
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

