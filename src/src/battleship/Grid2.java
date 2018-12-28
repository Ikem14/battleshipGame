/*
 * Kia Porter and Chukwubuikem Okafo
 * COSC 330: OO Design Pattern, GUI and Event-driven Programming
 * Project #1: Battleship Game
 * Due October 5, 2018
*/

package src.battleship;

import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grid2 extends Rectangle{
	
	public static final int BOARDSIZE = 10;
	
	public Tile2[][] board; //change to private
	
	//constructor
	public Grid2() {
		//color board light blue or grey
		Tile2 t;
		board = new Tile2[BOARDSIZE][BOARDSIZE];
		
		for(int i = 0; i < BOARDSIZE; i++) {
			for(int j = 0; j < BOARDSIZE; j++) {
				t = new Tile2(i, j);
				board[i][j] = t;
			}
		}
	}
	
	public void relocate(Ship ship, double x, double y) {
		ship.relocate(x, y);
		//board[x][y].set(ship);
	}
	
	public int getBoardSize() {
		 return BOARDSIZE;
	}
	
	public Tile2 getBoardTile(int x, int y) {
		return this.board[x][y];
	}
	
	public void addShip(ImageView ship, Coordinates here) { //how to call? player1.playerBoard(player1.playerShips[i].get, moveHere)
		//change this?
		if(isEmpty(here)) { // if the cell is empty
			
			//mark tile with this ship
			/*this.board[here.getX()][here.getY()].setShipType(ship);
			this.board[here.getX()][here.getY()].setShipHere(true); 
			*/
			this.board[here.getX()][here.getY()].setShipType(ship.getId());
			this.board[here.getX()][here.getY()].setShipHere(true);
		}
		
		//if cells are not empty do not add ship to board
	}
	
	public boolean isEmpty(Coordinates here) {
		
		if (this.board[here.getX()][here.getY()].isShipHere() == false)
		{
			return true;
		}
		
		return false;		
	}
	
	public boolean hitOrMiss(Ship[] ships, Coordinates bomb) { // how to call?: opponent.playerBoard.hitOrMiss(opponent.playerShips, bomb);
		
		if(this.board[bomb.getX()][bomb.getY()].isAlreadyHit() == false) {
			this.board[bomb.getX()][bomb.getY()].setAlreadyHit(true);
			
			for(int i = 0; i < ships.length; i++){
				if(ships[i].wasIHit(bomb)){
					this.board[bomb.getX()][bomb.getY()].setAlreadyHit(true);
					//ship was hit, cell becomes red
					return true;
				}
			}
			// no ships were hit, cell becomes white
			return false;
		}else {
			//print message choose a different space
			return true;
		}
	}
	
	
	/* Functions Needed:
	 *  void placeShip(Ship[] ships)
	 * 		player gives a list of ships, and this function places each ship
	 * 		for(int i = 0; i < ships.length; i++)
	 * 			if(newShip.wouldIcollide(ship[i])
	 * 				then cannot place ship
	 * 			else
	 * 				ship[i] = newShip;
	 * 		
	 * 	void moveShip(Ship[] ships, Coordinates here)
	 * 		for(int i = 0; i < ships.length; i++)
	 * 			if(ship == ships[i])
	 * 				if(ship[i].wouldICollide(new coordinates))
	 * 				say "You can't do that!"
	 * 				ships[i].move(new coordinates)
	 * 
	 *
	 *		
	 */
}
