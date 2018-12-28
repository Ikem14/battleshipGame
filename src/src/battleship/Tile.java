/*
 * Kia Porter and Chukwubuikem Okafo
 * COSC 330: OO Design Pattern, GUI and Event-driven Programming
 * Project #1: Battleship Game
 * Due October 5, 2018
*/

package src.battleship;

import java.io.File;
import java.util.EventObject;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile extends Rectangle{
	
	public static final int TILE_SIZE = 25;
	public static final int tileMultiple = 40;
	public static final Text ERROR_MESSAGE = new Text("CAN'T PLACE SHIP HERE");
	
	private String shipType;
	private boolean alreadyHit;
	public boolean shipHere;
	double orgSceneX;
	double orgSceneY;
	double orgTranslateX;
	double orgTranslateY; 
	double offsetX;
	double offsetY;
	double newTranslateX;
	double newTranslateY;
	boolean test = false; //for on drag exited
	int totalTest = 0; //for testing before dropping
	
	public Tile(int x, int y) {
		ERROR_MESSAGE.setFill(Color.RED);
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
		setFill(Color.LIGHTBLUE); // or SKYBLUE
		setStroke(Color.BLACK);
		
		
		//specifies which object accepts the data
		this.setOnDragOver((DragEvent event) -> {
			if (event.getGestureSource() != this && event.getDragboard().hasString()) {
				event.acceptTransferModes(TransferMode.MOVE);
				System.out.println("OnDragOver");
			}
			event.consume();
		});
		
		/*
		//SET ON DRAG ENTERED
		this.setOnDragEntered((DragEvent event) -> {
			//the drag and drop gesture entered the target
			//show user where item will be placed
			if(event.getGestureSource()!= this && event.getDragboard().hasString()) {
				if(this.test == false && this.isShipHere() == false) {
					this.setFill(Color.DARKORANGE);
					System.out.println("OnDragEntered Location: " + this.getX() + ", " + this.getY());
					System.out.println("Ship here: " + this.isShipHere());
				}
			}
			event.consume();
		});
		
		
		//RESET AFTER DRAG EXITED
		this.setOnDragExited((DragEvent event) -> {
			if(this.test == false && this.isShipHere() == false) {
				this.setFill(Color.LIGHTBLUE);
				event.consume();
			}
		});
		*/
		
		
		
		//when the mouse is released on the tiles
		this.setOnDragDropped((DragEvent event)-> {
			Dragboard db = event.getDragboard();
			//get item id here, which is stored when the drag started.
			boolean success = false;
			//if this is a meaningful drop...
			if(db.hasString() && db != event.getGestureSource() ) {
				//get ship ID
				String nodeID = db.getString();
				System.out.println("Actual ship: " + nodeID);
				//search for item in shipset
				ImageView ship = (ImageView) Main.shipSet.lookup("#" + nodeID);
				Ship actualShip = Player.getShipByType(nodeID);
				
				// MOVE SHIP TO BOARD DEPENDING ON SHIP TYPE	
				if(this.isShipHere() == false) {
					//IF CARRIER DO THIS
					if(actualShip.getShipType() == "CARRIER") {
						if(actualShip.getVerticalOrientation() == false) {
							//test if you have enough space to place the ship
							if(this.horTest(Ship.CARRIER_SIZE) == false) {
							
								//CHECK IF SHIP IS HORIZONTAL. IF IT IS DO THIS
								for(int i = 0; i < Ship.CARRIER_SIZE; i++) { //place ship
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setFill(Color.GOLD);
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).test = true;
										//set ship here to true
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipHere(true); 
										//this.setShipType(nodeID);
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipType("CARRIER");
									//}
								}
								//OTHER STUFF AFTER SUCCESS
								Main.shipSet.getChildren().remove(ship);
								Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
								//Main.tileGroup.getChildren().add(ship);
								event.setDropCompleted(success);
								Main.numShips--;
								Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left						Main.text2.setFont(new Font(20));
								Main.text2.setFill(Color.BLACK);
								//update player's board and number of ships left to boards
								Main.boards = new VBox(50, Main.tileGroup, Main.text2);
								Main.root.setCenter(Main.boards);
								ding();
								event.consume();
								
							}else {
								Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
								Main.root.setCenter(Main.boards);
							}
							
						}else { //IF VERTICAL DO THIS
							if(this.verTest(Ship.CARRIER_SIZE) == false){
								for(int i = 0; i < Ship.CARRIER_SIZE; i++) { //place ship
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setFill(Color.GOLD);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).test = true;
									//set ship here to true
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipHere(true); 
									//this.setShipType(nodeID);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipType("CARRIER");
									//}
								}
								//OTHER STUFF AFTER SUCCESS
								Main.shipSet.getChildren().remove(ship);
								Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
								//Main.tileGroup.getChildren().add(ship);
								event.setDropCompleted(success);
								Main.numShips--;
								Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left						Main.text2.setFont(new Font(20));
								Main.text2.setFill(Color.BLACK);
								//update player's board and number of ships left to boards
								Main.boards = new VBox(50, Main.tileGroup, Main.text2);
								Main.root.setCenter(Main.boards);
								ding();
								event.consume();
								
							}else {
								Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
								Main.root.setCenter(Main.boards);
							}
						}
					}
				
					
				
				
				/*
				 * 
				 * 
				 */
				//IF BATTLESHIP DO THIS
				else if(actualShip.getShipType() == "BATTLESHIP") {
					if(actualShip.getVerticalOrientation() == false) {
						//test if you have enough space to place the ship
						if(this.horTest(Ship.BATTLESHIP_SIZE) == false) {
						
							//CHECK IF SHIP IS HORIZONTAL. IF IT IS DO THIS
							for(int i = 0; i < Ship.BATTLESHIP_SIZE; i++) { //place ship
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setFill(Color.GREEN);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).test = true;
									//set ship here to true
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipHere(true); 
									//this.setShipType(nodeID);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipType("BATTLESHIP");
								//}
							}
							//OTHER STUFF AFTER SUCCESS
							Main.shipSet.getChildren().remove(ship);
							Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
							//Main.tileGroup.getChildren().add(ship);
							event.setDropCompleted(success);
							Main.numShips--;
							Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left						Main.text2.setFont(new Font(20));
							Main.text2.setFill(Color.BLACK);
							//update player's board and number of ships left to boards
							Main.boards = new VBox(50, Main.tileGroup, Main.text2);
							Main.root.setCenter(Main.boards);
							ding();
							event.consume();
							
						}else {
							Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
							Main.root.setCenter(Main.boards);
						}
						
					}else { //IF VERTICAL DO THIS
						if(this.verTest(Ship.BATTLESHIP_SIZE) == false){
							for(int i = 0; i < Ship.BATTLESHIP_SIZE; i++) { //place ship
								Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setFill(Color.GREEN);
								Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).test = true;
								//set ship here to true
								Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipHere(true);
								//this.setShipType(nodeID);
								Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipType("BATTLESHIP");
								//}
							}
							//OTHER STUFF AFTER SUCCESS
							Main.shipSet.getChildren().remove(ship);
							Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
							//Main.tileGroup.getChildren().add(ship);
							event.setDropCompleted(success);
							Main.numShips--;
							Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left						Main.text2.setFont(new Font(20));
							Main.text2.setFill(Color.BLACK);
							//update player's board and number of ships left to boards
							Main.boards = new VBox(50, Main.tileGroup, Main.text2);
							Main.root.setCenter(Main.boards);
							ding();
							event.consume();
							
						}else {
							Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
							Main.root.setCenter(Main.boards);
						}
					}
				}
					
					/*
					 * 
					 * 
					 */
					//IF CRUISER DO THIS
					else if(actualShip.getShipType() == "CRUISER") {
						if(actualShip.getVerticalOrientation() == false) {
							//test if you have enough space to place the ship
							if(this.horTest(Ship.CRUISER_SIZE) == false) {
							
								//CHECK IF SHIP IS HORIZONTAL. IF IT IS DO THIS
								for(int i = 0; i < Ship.CRUISER_SIZE; i++) { //place ship
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setFill(Color.DARKBLUE);
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).test = true;
										//set ship here to true
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipHere(true); 
										//this.setShipType(nodeID);
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipType("CRUISER");
									//}
								}
								//OTHER STUFF AFTER SUCCESS
								Main.shipSet.getChildren().remove(ship);
								Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
								//Main.tileGroup.getChildren().add(ship);
								event.setDropCompleted(success);
								Main.numShips--;
								Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left						Main.text2.setFont(new Font(20));
								Main.text2.setFill(Color.BLACK);
								//update player's board and number of ships left to boards
								Main.boards = new VBox(50, Main.tileGroup, Main.text2);
								Main.root.setCenter(Main.boards);
								ding();
								event.consume();
								
							}else {
								Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
								Main.root.setCenter(Main.boards);
							}
							
						}else { //IF VERTICAL DO THIS
							if(this.verTest(Ship.CRUISER_SIZE) == false){
								for(int i = 0; i < Ship.CRUISER_SIZE; i++) { //place ship
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setFill(Color.DARKBLUE);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).test = true;
									//set ship here to true
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipHere(true); 
									//this.setShipType(nodeID);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipType("CRUISER");
									//}
								}
								//OTHER STUFF AFTER SUCCESS
								Main.shipSet.getChildren().remove(ship);
								Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
								//Main.tileGroup.getChildren().add(ship);
								event.setDropCompleted(success);
								Main.numShips--;
								Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left						Main.text2.setFont(new Font(20));
								Main.text2.setFill(Color.BLACK);
								//update player's board and number of ships left to boards
								Main.boards = new VBox(50, Main.tileGroup, Main.text2);
								Main.root.setCenter(Main.boards);
								ding();
								event.consume();
								
							}
						}
					}
					
					/*
					 * 
					 * 
					 */
					//IF SUBMARINE DO THIS
					else if(actualShip.getShipType() == "SUBMARINE") {
						if(actualShip.getVerticalOrientation() == false) {
							//test if you have enough space to place the ship
							if(this.horTest(Ship.SUBMARINE_SIZE) == false) {
							
								//CHECK IF SHIP IS HORIZONTAL. IF IT IS DO THIS
								for(int i = 0; i < Ship.SUBMARINE_SIZE; i++) { //place ship
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setFill(Color.BROWN);
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).test = true;
										//set ship here to true
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipHere(true); 
										//this.setShipType(nodeID);
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipType("SUBMARINE");
									//}
								}
								//OTHER STUFF AFTER SUCCESS
								Main.shipSet.getChildren().remove(ship);
								Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
								//Main.tileGroup.getChildren().add(ship);
								event.setDropCompleted(success);
								Main.numShips--;
								Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left
								Main.text2.setFill(Color.BLACK);
								//update player's board and number of ships left to boards
								Main.boards = new VBox(50, Main.tileGroup, Main.text2);
								Main.root.setCenter(Main.boards);
								ding();
								event.consume();
								
							}else {
								Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
								Main.root.setCenter(Main.boards);
							}
							
						}else { //IF VERTICAL DO THIS
							if(this.verTest(Ship.SUBMARINE_SIZE) == false){
								for(int i = 0; i < Ship.SUBMARINE_SIZE; i++) { //place ship
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setFill(Color.BROWN);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).test = true;
									//set ship here to true
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipHere(true); 
									//this.setShipType(nodeID);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipType("SUBMARINE");
									//}
								}
								//OTHER STUFF AFTER SUCCESS
								Main.shipSet.getChildren().remove(ship);
								Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
								//Main.tileGroup.getChildren().add(ship);
								event.setDropCompleted(success);
								Main.numShips--;
								Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left						Main.text2.setFont(new Font(20));
								Main.text2.setFill(Color.BLACK);
								//update player's board and number of ships left to boards
								Main.boards = new VBox(50, Main.tileGroup, Main.text2);
								Main.root.setCenter(Main.boards);
								ding();
								event.consume();
								
							}
							else {
								Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
								Main.root.setCenter(Main.boards);
							}
						}
					}
					/*
					 * 
					 * 
					 */
					//IF DESTROYER DO THIS
					else if(actualShip.getShipType() == "DESTROYER") {
						if(actualShip.getVerticalOrientation() == false) {
							//test if you have enough space to place the ship
							if(this.horTest(Ship.DESTROYER_SIZE) == false) {
							
								//CHECK IF SHIP IS HORIZONTAL. IF IT IS DO THIS
								for(int i = 0; i < Ship.DESTROYER_SIZE; i++) { //place ship
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setFill(Color.DARKVIOLET);
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).test = true;
										//set ship here to true
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipHere(true); 
										//this.setShipType(nodeID);
										Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + i, (int)this.getY()).setShipType("DESTROYER");
									//}
								}
								//OTHER STUFF AFTER SUCCESS
								Main.shipSet.getChildren().remove(ship);
								Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
								//Main.tileGroup.getChildren().add(ship);
								event.setDropCompleted(success);
								Main.numShips--;
								Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left
								Main.text2.setFill(Color.BLACK);
								//update player's board and number of ships left to boards
								Main.boards = new VBox(50, Main.tileGroup, Main.text2);
								Main.root.setCenter(Main.boards);
								ding();
								event.consume();
								
							}else {								
								Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
								Main.root.setCenter(Main.boards);
							}
							
						}else { //IF VERTICAL DO THIS
							if(this.verTest(Ship.DESTROYER_SIZE) == false){
								for(int i = 0; i < Ship.DESTROYER_SIZE; i++) { //place ship
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setFill(Color.DARKVIOLET);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).test = true;
									//set ship here to true
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipHere(true); 
									//this.setShipType(nodeID);
									Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + i).setShipType("DESTROYER");
									//}
								}
								//OTHER STUFF AFTER SUCCESS
								Main.shipSet.getChildren().remove(ship);
								Main.player1.getPlayerBoard().getBoardTile((int) this.getX(), (int)this.getY());
								//Main.tileGroup.getChildren().add(ship);
								event.setDropCompleted(success);
								Main.numShips--;
								Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left
								Main.text2.setFill(Color.BLACK);
								//update player's board and number of ships left to boards
								Main.boards = new VBox(50, Main.tileGroup, Main.text2);
								Main.root.setCenter(Main.boards);
								ding();
								event.consume();
								
							}else {
								Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
								Main.root.setCenter(Main.boards);
							}
						}
					}
				
				}else {
						Main.boards = new VBox(50, Main.tileGroup, Main.text2, ERROR_MESSAGE);
						Main.root.setCenter(Main.boards);
					}
					
					//this.setVisible(false);
					Ship.mouseX = event.getSceneX();
					Ship.mouseY = event. getSceneY();
					this.setShipType(nodeID);
					this.setShipHere(true);
					success = true;
				}
			
				System.out.println("OnDraggedDropped Location: " + this.getX() + ", " + this.getY());
				System.out.println("Ship here: " + this.isShipHere());
			
		});
	}

	
	/**************************
	 * 
	 * 
	 * RANDOM PLACEMENT
	 **************************/
	
	public static void autoPlace() {
			int shipNum = 5;
			int x, y, orient; //for coordinates
			boolean vertical; //for orientation
			//get ship images
			ImageView carrierI = (ImageView) Main.shipSet.lookup("#" + "CARRIER");
			ImageView battleshipI = (ImageView) Main.shipSet.lookup("#" + "BATTLESHIP");
			ImageView submarineI = (ImageView) Main.shipSet.lookup("#" + "SUBMARINE");
			ImageView cruiserI = (ImageView) Main.shipSet.lookup("#" + "CRUISER");
			ImageView destroyerI = (ImageView) Main.shipSet.lookup("#" + "DESTROYER");
			//GET SHIPS
			Ship carrier = Player.getShipByType("CARRIER");
			Ship battleship = Player.getShipByType("BATTLESHIP");
			Ship submarine = Player.getShipByType("SUBMARINE");
			Ship cruiser = Player.getShipByType("CRUISER");
			Ship destroyer = Player.getShipByType("DESTROYER");
			boolean carrier_placed = false;
			boolean battleship_placed = false;
			boolean submarine_placed = false;
			boolean cruiser_placed = false;
			boolean destroyer_placed = false;
		
			
		while(shipNum > 0) {
			//place carrier
			while(carrier_placed == false) {
				x = Coordinates.randPlace();
				y = Coordinates.randPlace();
				orient = Coordinates.randOrien();
				//get orientation
				if(orient == 1) {
					vertical = false;
				}else {
					vertical = true;
				}
				
				//HANDLE HORIZONTAL
				if(vertical == false && Main.player1.getPlayerBoard().getBoardTile(x, y).horTest(carrier.getShipSize()) == false) {
					for(int i = 0; i < Ship.CARRIER_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setFill(Color.GOLD);
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setShipHere(true);
						Main.player1.getPlayerBoard().getBoardTile((int)x + i, (int)y).setShipType("CARRIER");
					//}
					}
					carrier_placed = true;
				}else if(vertical == true && Main.player1.getPlayerBoard().getBoardTile(x, y).verTest(carrier.getShipSize()) == false){ //handle vertical
					for(int i = 0; i < Ship.CARRIER_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setFill(Color.GOLD);
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y + i).setShipType("CARRIER");
						//}
					}
					carrier_placed = true;
				} //handle vertical end
				if(carrier_placed == true) {
					shipNum--; //subtract ship num
					//OTHER STUFF AFTER placements
					Main.shipSet.getChildren().remove(carrierI);
					Main.player1.getPlayerBoard().getBoardTile(x, y);
					//Main.tileGroup.getChildren().add(ship);
					Main.numShips--;
					Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left
					Main.text2.setFill(Color.BLACK);
					//update player's board and number of ships left to boards
					Main.boards = new VBox(50, Main.tileGroup, Main.text2);
					Main.root.setCenter(Main.boards);
				}
			
			}//place carrier end
			
			/*
			 * 
			 * 
			 * place battleship
			 */
			while(battleship_placed == false) {
				x = Coordinates.randPlace();
				y = Coordinates.randPlace();
				orient = Coordinates.randOrien();
				//get orientation
				if(orient == 1) {
					vertical = false;
				}else {
					vertical = true;
				}
				
				//HANDLE HORIZONTAL
				if(vertical == false && Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y).horTest(battleship.getShipSize()) == false) {
					for(int i = 0; i < Ship.BATTLESHIP_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setFill(Color.GREEN);
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x + i, (int)y).setShipType("BATTLESHIP");
					//}
					}
					battleship_placed = true;
				}else if(vertical == true && Main.player1.getPlayerBoard().getBoardTile(x, y).verTest(battleship.getShipSize()) == false){ //handle vertical
					for(int i = 0; i < Ship.BATTLESHIP_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setFill(Color.GREEN);
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y + i).setShipType("BATTLESHIP");
						//}
					}
					battleship_placed = true;
				} //handle vertical end
				if(battleship_placed == true) {
					//OTHER STUFF AFTER placements
					shipNum--; //subtract ship num
					Main.shipSet.getChildren().remove(battleshipI);
					Main.player1.getPlayerBoard().getBoardTile(x, y);
					//Main.tileGroup.getChildren().add(ship);
					Main.numShips--;
					Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left
					Main.text2.setFill(Color.BLACK);
					//update player's board and number of ships left to boards
					Main.boards = new VBox(50, Main.tileGroup, Main.text2);
					Main.root.setCenter(Main.boards);
				}
			
			}//END PLACE BATTLESHIP
			
			/*
			 * 
			 * 
			 * PLACE CRUISER
			 */
			while(cruiser_placed == false) {
				x = Coordinates.randPlace();
				y = Coordinates.randPlace();
				orient = Coordinates.randOrien();
				//get orientation
				if(orient == 1) {
					vertical = false;
				}else {
					vertical = true;
				}
				
				//HANDLE HORIZONTAL
				if(vertical == false && Main.player1.getPlayerBoard().getBoardTile(x, y).horTest(cruiser.getShipSize()) == false) {
					for(int i = 0; i < Ship.CRUISER_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setFill(Color.DARKBLUE);
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x + i, (int)y).setShipType("CRUISER");
					//}
					}
					cruiser_placed = true;
				}else if(vertical == true && Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y).verTest(cruiser.getShipSize()) == false){ //handle vertical
					for(int i = 0; i < Ship.CRUISER_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setFill(Color.DARKBLUE);
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y + i).setShipType("CRUISER");
						//}
					}
					cruiser_placed = true;
				} //handle vertical end
				if(cruiser_placed == true) {
					shipNum--; //subtract ship num
					//OTHER STUFF AFTER placements
					Main.shipSet.getChildren().remove(cruiserI);
					Main.player1.getPlayerBoard().getBoardTile(x, y);
					//Main.tileGroup.getChildren().add(ship);
					Main.numShips--;
					Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left
					Main.text2.setFill(Color.BLACK);
					//update player's board and number of ships left to boards
					Main.boards = new VBox(50, Main.tileGroup, Main.text2);
					Main.root.setCenter(Main.boards);
				}
			
			}
			/*
			 * 
			 * 
			 *Place submarine
			 */
			while(submarine_placed == false) {
				x = Coordinates.randPlace();
				y = Coordinates.randPlace();
				orient = Coordinates.randOrien();
				//get orientation
				if(orient == 1) {
					vertical = false;
				}else {
					vertical = true;
				}
				
				//HANDLE HORIZONTAL
				if(vertical == false && Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y).horTest(submarine.getShipSize()) == false) {
					for(int i = 0; i < Ship.SUBMARINE_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setFill(Color.BROWN);
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x + i, (int)y).setShipType("SUBMARINE");
					//}
					}
					submarine_placed = true;
				}else if(vertical == true && Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y).verTest(submarine.getShipSize()) == false){ //handle vertical
					for(int i = 0; i < Ship.SUBMARINE_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setFill(Color.BROWN);
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y + i).setShipType("SUBMARINE");
						//}
					}
					submarine_placed = true;
				} //handle vertical end
				if(submarine_placed == true) {
					shipNum--; //subtract ship num
					//OTHER STUFF AFTER placements
					Main.shipSet.getChildren().remove(submarineI);
					Main.player1.getPlayerBoard().getBoardTile(x, y);
					//Main.tileGroup.getChildren().add(ship);
					Main.numShips--;
					Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left
					Main.text2.setFill(Color.BLACK);
					//update player's board and number of ships left to boards
					Main.boards = new VBox(50, Main.tileGroup, Main.text2);
					Main.root.setCenter(Main.boards);
				}
			
			}
			/*
			 * 
			 * 
			 * PLACE DESTROYER
			 */
			while(destroyer_placed == false) {
				x = Coordinates.randPlace();
				y = Coordinates.randPlace();
				orient = Coordinates.randOrien();
				//get orientation
				if(orient == 1) {
					vertical = false;
				}else {
					vertical = true;
				}
				
				//HANDLE HORIZONTAL
				if(vertical == false && Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y).horTest(destroyer.getShipSize()) == false) {
					for(int i = 0; i < Ship.DESTROYER_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setFill(Color.DARKVIOLET);
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile(x + i, y).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x + i, (int)y).setShipType("DESTROYER");
					//}
					}
					destroyer_placed = true;
				}else if(vertical == true && Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y).verTest(destroyer.getShipSize()) == false){ //handle vertical
					for(int i = 0; i < Ship.DESTROYER_SIZE; i++) { //place ship
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).setFill(Color.DARKVIOLET);
						Main.player1.getPlayerBoard().getBoardTile(x, y + i).test = true;
						//set ship here to true
						Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y + i).setShipHere(true); 
						Main.player1.getPlayerBoard().getBoardTile((int)x, (int)y + i).setShipType("DESTROYER");
						//}
					}
					destroyer_placed = true;
				} //handle vertical end
				if(destroyer_placed == true) {
					shipNum--; //subtract ship num
					//OTHER STUFF AFTER placements
					Main.shipSet.getChildren().remove(destroyerI);
					Main.player1.getPlayerBoard().getBoardTile(x, y);
					//Main.tileGroup.getChildren().add(ship);
					Main.numShips--;
					Main.text2 = new Text("Number of Ships Left: "+ Main.numShips + "   "); //display num of ships left
					Main.text2.setFill(Color.BLACK);
					//update player's board and number of ships left to boards
					Main.boards = new VBox(50, Main.tileGroup, Main.text2);
					Main.root.setCenter(Main.boards);
				}
			
			}
		}
		Main.text = new Text(" CLICK THE START GAME BUTTON TO START GAME");
		Main.text.setFill(Color.BLUE);
		Main.text.setFont(new Font(20));
		VBox box = new VBox(5, new Text("  "), Main.text, Main.startGame, new Text(" "));
		box.setAlignment(Pos.CENTER);
		Main.boards.getChildren().remove(Main.text2); //remove num of ships text
		Main.root.setTop(box);
			
	}//end auto place
	
	//RUNS WHILE LOOP TO SEE IF SHIP LENGTH IS VALID TO BE PLACED for Horizontal placement
	//returns false if ship is not on tile
	public boolean horTest(int size) {
		boolean test = false;
		int testNum = 0;
		int total;
		while(testNum < size && test == false) {
			total = testNum + (int)this.getX();
			if(total > 9) {
				test = true;
			}
			else if(Main.player1.getPlayerBoard().getBoardTile((int)this.getX() + testNum, (int)this.getY()).isShipHere() == false) {
				testNum++;
			}else {
				test = true;
			}
		}
		return test;
	}
	
	//RUNS WHILE LOOP TO SEE IF SHIP LENGTH IS VALID TO BE PLACED for vertical placement
	//returns false if ship is not on tile
	public boolean verTest(int size){
		boolean test = false;
		int testNum = 0;
		int total;
		while(testNum < size && test == false) {
			total = testNum + (int)this.getY();
			if(total > 9) {
				test = true;
			}
			else if(Main.player1.getPlayerBoard().getBoardTile((int)this.getX(), (int)this.getY() + testNum).isShipHere() == false){
				testNum++;
			}else {
				test = true;
			}
		}
		return test;
	}
	
	public void ding() {
		String dingPath = new File("src/media/ElevatorDing.mp3").getAbsolutePath();
		Media dingSound = new Media(new File(dingPath).toURI().toString());
		MediaPlayer dingPlayer = new MediaPlayer(dingSound);
		dingPlayer.play();
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
