/* Kia Porter and Chukwubuikem Okafo
 * COSC 330: OO Design Pattern, GUI and Event-driven Programming
 * Project #1: Battleship Game
 * Due October 5, 2018
*/
//Tile2 for the opponent board
package src.battleship;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile2 extends Rectangle{
	
	public static final int TILE_SIZE = 35;
	
	private String shipType;
	private boolean alreadyHit;
	public boolean shipHere;
	public boolean clicked = false;
	public static boolean test;
	
	
	public Tile2(int x, int y) {
		
		this.setX(x);
		this.setY(y);
		this.setShipType("null"); // N for null
		this.setAlreadyHit(false);
		this.setShipHere(false);
		
		//set size of tile
		setWidth(TILE_SIZE);
		setHeight(TILE_SIZE);
		
		relocate(x * TILE_SIZE, y * TILE_SIZE);// idk what this does
		
		//tile color
		setFill(Color.BLACK);
		setStroke(Color.GREEN);
		
		
		this.setOnMouseClicked((MouseEvent event) -> {
			if(this.clicked != true) {
				//if click is a hit do
				if(Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY()).isShipHere() == true) {
					Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY()).setFill(Color.RED);
					test = true; //set test = true. ship hit
					Main.endGame--; //subtract one from total num of ship pieces
					Ship.pop("SHIP HIT", 1); //POP up window with int 1 to make it red
				}else {
					test = false; //set test = false. no ship hit
					Ship.pop("MISSED", 0); //POP UP  window to not turn it red
				}
				//Send this coordinate to the server
				//then get hit or miss value from server
				this.clicked = true;
				System.out.println("Clicked here: " + this.getX() + ", " + this.getY());
				Main.setCoorX((int)this.getX());
				Main.setCoorY((int)this.getY());
				recieveFromServer(test, Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY()).getShipType());
			}
			else {
				Ship.pop("SPOT ALREADY CLICKED\nCLICK ANOTHER SPOT", 1); //POP up window with int 1 to make it red
			}if(Main.endGame == 0) {
				Ship.pop("  YOU WIN\nGAME OVER", 2); //display game over displayed in blue
				//hide original window
			 	((Node)(event.getSource())).getScene().getWindow().hide();
			}
			
			
			event.consume();
		});
	}
	
	//function to receive from server
		public void recieveFromServer(boolean p, String shipType) {
			System.out.println("ShipType: " + shipType);
			
			//for bomb sound
			String bombPath = new File("src/media/BombSound.mp3").getAbsolutePath();
			Media bombSound = new Media(new File(bombPath).toURI().toString());
			MediaPlayer bombPlayer = new MediaPlayer(bombSound);
			
			//for splash sound
			String splashPath = new File("src/media/SplashSound.mp3").getAbsolutePath();
			Media splashSound = new Media(new File(splashPath).toURI().toString());
			MediaPlayer splashPlayer = new MediaPlayer(splashSound);
			
			if(p) {
				this.setFill(Color.RED);
				bombPlayer.play();
			}
			else {
				this.setFill(Color.BLUE);
				splashPlayer.play();
			}
			//Subtract value from ship after hit
			if(shipType == "CARRIER") {
				int size = Main.player1.getShipI(0).getRemainingPieces() - 1 ;
				System.out.println("Size -" + size);
				Main.carrierStatus = new Text("Carrier:  " + size + " pieces left");
				Main.player1.getShipI(0).setRemainingPieces(size);
				Main.carrierStatus.setFont(new Font(15));
				Main.carrierStatus.setFill(Color.GOLD);
			}
			else if(shipType == "CRUISER") {
				int size = Main.player1.getShipI(2).getRemainingPieces() - 1 ;
				System.out.println("Size -" + size);
				Main.cruiserStatus = new Text("Cruiser:  " + size + " pieces left");
				Main.player1.getShipI(2).setRemainingPieces(size);
				Main.cruiserStatus.setFont(new Font(15));
				Main.cruiserStatus.setFill(Color.DARKBLUE);
			}
			else if(shipType == "SUBMARINE") {
				int size = Main.player1.getShipI(3).getRemainingPieces() - 1 ;
				System.out.println("Size -" + size);
				Main.submarineStatus = new Text("Submarine:  " + size + " pieces left");
				Main.player1.getShipI(3).setRemainingPieces(size);
				Main.submarineStatus.setFont(new Font(15));
				Main.submarineStatus.setFill(Color.BROWN);
			}
			else if(shipType == "BATTLESHIP") {
				int size = Main.player1.getShipI(1).getRemainingPieces() - 1 ;
				System.out.println("Size -" + size);
				Main.battleshipStatus = new Text("Battleship:  " + size + " pieces left");
				Main.player1.getShipI(1).setRemainingPieces(size);
				Main.battleshipStatus.setFont(new Font(15));
				Main.battleshipStatus.setFill(Color.GREEN);
			}
			else if(shipType == "DESTROYER") {
				int size = Main.player1.getShipI(4).getRemainingPieces() - 1 ;
				System.out.println("Size -" + size);
				Main.destroyerStatus = new Text("Destroyer:  " + size + " pieces left");
				Main.player1.getShipI(4).setRemainingPieces(size);
				Main.destroyerStatus.setFont(new Font(15));
				Main.destroyerStatus.setFill(Color.DARKVIOLET);
			}
			
			//update the gamePane ship info
			Main.shipStatus = new VBox(10, Main.opponentStatus, Main.yourShips, Main.carrierStatus, Main.battleshipStatus, Main.cruiserStatus, Main.submarineStatus, Main.destroyerStatus);// add ship status
			Main.shipStatus.setAlignment(Pos.CENTER);
			Main.gamePane.setLeft(Main.line);
			Main.gamePane.setRight(Main.shipStatus);
		}
	

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String ship) {
		this.shipType = ship;
	}

	public boolean isAlreadyHit() {
		return alreadyHit;
	}

	public void setAlreadyHit(boolean alreadyHit) {
		this.alreadyHit = alreadyHit;
	}

	public boolean isShipHere() {
		return shipHere;
	}

	public void setShipHere(boolean shipHere) {
		this.shipHere = shipHere;
	}
	
	//getters and setters for x and y are in Coordinates class 

}

