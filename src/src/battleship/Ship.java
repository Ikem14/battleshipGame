/*
 * Kia Porter and Chukwubuikem Okafo
 * COSC 330: OO Design Pattern, GUI and Event-driven Programming
 * Project #1: Battleship Game
 * Due October 5, 2018
*/

package src.battleship;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Ship extends StackPane{
	
	//ship type constants
	public static final String CARRIER = "CARRIER";
	public static final String CRUISER = "CRUISER";
	public static final String SUBMARINE = "SUBMARINE";
	public static final String BATTLESHIP = "BATTLESHIP";
	public static final String DESTROYER = "DESTROYER";
	
	//ship size constants
	public static final int CARRIER_SIZE = 5;
	public static final int CRUISER_SIZE = 3;
	public static final int SUBMARINE_SIZE = 3;
	public static final int BATTLESHIP_SIZE = 4;
	public static final int DESTROYER_SIZE = 2;
	public static final int tileMultiple = 40;
	
	//member variables
	public String shipType; //cruiser, carrier, battleship, etc.
	private Coordinates[] shipLocation; // ship location on the grid; array of coordinates
	private int shipSize; // 2, 3, 4, or 5
	private int remainingPieces;
	private boolean locationChange; // if the location of the ship has changed
	private boolean isAlive;
	private Image shipImage;
	private ImageView shipImageView;
	private boolean verticalOrientation; //if the ship is vertical or not. Original setup = horizontal
	
	//???
	public static double mouseX, mouseY, deltaX, deltaY;// newTranslateX, newTranslateY;
	
	// Constructor
	public Ship(String type) {
		
		this.shipType = type; //set shipType
		this.setShipSize();  //set shipLenght
		this.setRemainingPieces(shipSize);
		this.shipLocation = new Coordinates[shipSize];
		this.setLocationChange(false);
		this.setAlive(true);
		this.setShipImage();
		this.verticalOrientation = false;
		
		
		this.shipImageView.setOnDragDetected((MouseEvent event) -> {
			Dragboard db = this.shipImageView.startDragAndDrop(TransferMode.MOVE);
			//store node ID in order to know what is dragged
			ClipboardContent content = new ClipboardContent();
			content.putString(shipImageView.getId());
			db.setContent(content);
			System.out.println("OnDragDetected: " + this.shipType);
			event.consume();
		});
		
		this.shipImageView.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle (DragEvent event) {
				if (event.getTransferMode() == TransferMode.MOVE) {
					//transfer
				}
				System.out.println("OnDragDone");
				event.consume();
			}
		});		
		
		this.shipImageView.setOnMouseEntered((MouseEvent event) -> {
			this.shipImageView.setCursor(Cursor.HAND);
		});
		
		this.shipImageView.setOnMousePressed((new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.SECONDARY) {
					double value = shipImageView.getRotate();
					if(verticalOrientation == false) {
						shipImageView.setRotate(value + 90);
						//verticalOrientation = true;
						setVerticalOrientation(true);
					}else {
						shipImageView.setRotate(value + 270);
						//verticalOrientation = false;
						setVerticalOrientation(false);
					}
				}
				System.out.println("mouse Pressed Handler");
			}
				
			}));
		
		this.shipImageView.setOnMouseDragged((new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				deltaX = event.getSceneX() - mouseX;
	    		deltaY = event.getSceneY() - mouseY;
	    		//newTranslateX = orgTranslateX + deltaX;
	    		//newTranslateY = orgTranslateY + deltaY;
	    		
	    		//((ImageView)(event.getSource())).setTranslateX(newTranslateX);
	    		//((ImageView)(event.getSource())).setTranslateY(newTranslateY);
	    		
	    		//System.out.println("NewTranslateX: "+newTranslateX);
	    		//System.out.println("NewTranslateY: "+newTranslateY);
	    		System.out.println("mouse Dragged Handler");
			}
		}));
		
	}
	
	//returns vertical orientation
	public boolean getO() {
		return verticalOrientation;
	}
	
	public void Relocate(ImageView ship, double x, double y) {
		ship.relocate(x, y);
		Main.tileGroup.getChildren().add(ship);
	}
	
	private void setShipImage() {
		shipImageView = new ImageView();
		
		switch (this.shipType) {
			case CARRIER:
				shipImage = new Image("file:///Users/ikemokafo/eclipse-workspace/Battleship2.2/src/CARRIER (1).png");
				shipImageView.setFitWidth(200);
				break;
			case CRUISER: 
				shipImage = new Image("file:///Users/ikemokafo/eclipse-workspace/Battleship2.2/src/CRUISER (1).png");
				shipImageView.setFitWidth(200);
				break;
			case SUBMARINE: 
				shipImage = new Image("file:///Users/ikemokafo/eclipse-workspace/Battleship2.2/src/SUBMARINE (1).png");
				shipImageView.setFitWidth(200);
				break;
			case DESTROYER: 
				shipImage = new Image("file:///Users/ikemokafo/eclipse-workspace/Battleship2.2/src/DESTROYER (1).png");
				shipImageView.setFitWidth(140);
				break;
			case BATTLESHIP: 
				shipImage = new Image("file:///Users/ikemokafo/eclipse-workspace/Battleship2.2/src/BATTLESHIP (1).png");
				shipImageView.setFitWidth(260);
				break;
			default: System.out.println("Invalid ship type. Exiting program...");
				System.exit(1);
		}
	
		shipImageView.setImage(this.shipImage);
        //shipImageView.setFitWidth(shipSize * Tile.TILE_SIZE);
        shipImageView.setPreserveRatio(true);
        shipImageView.setId(this.shipType);
	}
	
	public ImageView getShipImage(Ship ship){
		return ship.shipImageView;
	}
	
	private void setShipSize() {
		
		switch (this.shipType) {
			case CARRIER: 
				this.shipSize = CARRIER_SIZE;
				break;
			case CRUISER: 
				this.shipSize = CRUISER_SIZE;
				break;
			case SUBMARINE: 
				this.shipSize = SUBMARINE_SIZE;
				break;
			case DESTROYER: 
				this.shipSize = DESTROYER_SIZE;
				break;
			case BATTLESHIP: 
				this.shipSize = BATTLESHIP_SIZE;
				break;
			default: System.out.println("Invalid ship type. Exiting program...");
				System.exit(1);
		}
	}
	
	public int getShipSize(){
		return this.shipSize;
	}
	
	public String getShipType() {
		return this.shipType;
	}
	
	public int getRemainingPieces() {
		return remainingPieces;
	}

	public void setRemainingPieces(int remainingPieces) {
		this.remainingPieces = remainingPieces;
	}
	
	/*public void updateLocation(int startX, int startY) {
		
		//check if space is empty in update location function
		if (this.shipDirection == HORIZONTAL) {
			//update horizontal; change Y
			for (int i = 0; i < this.shipLength; i++) {
				this.shipLocation[i].setX(startX);
				this.shipLocation[i].setY(startY + i);
			}
			System.out.println("Updated ship location.");
		}
		
		if (this.shipDirection == VERTICAL) {
			//update vertical; change X
			for (int i = 0; i < this.shipLength; i++) {
				this.shipLocation[i].setX(startX + i);
				this.shipLocation[i].setY(startY);
			}
			System.out.println("Updated ship location.");
		}
	} */
	
	public Coordinates[] getLocation() {
		
		return this.shipLocation;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public boolean isLocationChange() {
		return locationChange;
	}

	public void setLocationChange(boolean locationChange) {
		this.locationChange = locationChange;
	}

	public boolean getVerticalOrientation() {
		return this.verticalOrientation;
	}

	public void setVerticalOrientation(boolean shipDirection) {
		this.verticalOrientation = shipDirection;
	}

	public boolean wasIHit(Coordinates bomb) {
		
		for(int i = 0; i < this.shipSize; i++) {
			if (this.shipLocation[i].getX() == bomb.getX() && this.shipLocation[i].getY() == bomb.getY()) {
				if(this.remainingPieces > 0) {
					this.remainingPieces-=1;
				}else { // hit the last piece, sink the ship
					setAlive(false);
				}
				//mark this coordinate in shipLocation as hit?
				return true;
			}
		}
		return false;
	}
	
	//pop window for hit
	public static void pop(String s, int n) {
		BorderPane pop = new BorderPane();
		pop.setPrefSize(300, 150);
		Text text = new Text(s);
		text.setFont(new Font(20));
		if (n == 1) {
			text.setFill(Color.RED);
		}
		if(n == 2) {
			text.setFill(Color.BLUE);
		}
		/*if(Main.endGame == 0) {
			
			pop.setTop(text);
			pop.setAlignment(text, Pos.TOP_CENTER); //ignore warning
			
			ImageView winImageView = new ImageView();
			String imagePath = new File("src/media/giphy.gif").getAbsolutePath();
			Image winImage = new Image(new File(imagePath).toURI().toString());
			
			winImageView.setFitWidth(100);
			winImageView.setImage(winImage);
	        winImageView.setPreserveRatio(true);
	        
	        pop.setCenter(winImageView);
	             
		}*/
		pop.setCenter(text);
		Scene gameScene = new Scene(pop); // replace with another create content function?
	 	Stage gameStage = new Stage();
	 	gameStage.setTitle("Battleship");
	 	gameStage.setScene(gameScene);
	 	gameStage.show();
	}
	
	
	/* Functions Needed:
	 *  void updateShipLocation(Coordinates here)
	 *  	update ship location
	 *  
	 *  boolean wouldICollide(Ship ship, Coordinate[] here)
	 *  	ask other ship if any of my cells would collide with their cells
	 *  	if so
	 *  		return true
	 */
} // end of class
