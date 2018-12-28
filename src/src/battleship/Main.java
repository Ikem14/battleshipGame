package src.battleship;
	
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;



public class Main extends Application {
	
	//Network variables
	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	private String message = ""; // message from server
	private String battleShipServer; // host server for this application
	private Socket client; // socket to communicate with server
	private static int coorX; //X coordinate from server
	private static int coorY; //y coordinate from server
	public static void setCoorX(int x) {
		coorX = x;
	}
	
	public static void setCoorY(int y) {
		coorY = y;
	}
	
	public static int getCoorX() {
		return coorX;
	}
	
	public static int getCoorY() {
		return coorY;
	}
	
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


	 //GAME CONTENT VARIABLES
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int WIDTH2 = 700;
	private static final int HEIGHT2 = 700;

	//create player board
	public static Player player1 = new Player(false);
	public static Player2 player2 = new Player2(false);
	public static Text line = new Text(" ---------------------------------- ");
	public ImageView [] Ships = new ImageView[5];
	public static Group tileGroup = new Group(); //for player board
	public static Group tileGroup2 = new Group(); //for opponent board
	public static FlowPane flowPane = new FlowPane(); //before game starts
	public static FlowPane flowPane2 = new FlowPane(); //after game starts
	public static VBox shipSet = new VBox(new Text("                                 ")); //shipSet
	public static VBox boards; //player board
	public static VBox boards2; //after game starts
	public static VBox shipStatus; // player's ships status
	public static int numShips = Player.NUMBEROFSHIPS; //number of ships
	public static Text text, text2, carrierStatus, cruiserStatus, submarineStatus, destroyerStatus, battleshipStatus, opponentStatus, yourShips;
	public static BorderPane root = new BorderPane(); //before game starts
	public static BorderPane gamePane = new BorderPane(); //after game starts
	final static Button startGame = new Button("Start Game");
	final static Button autoPlace = new Button("AUTO PLACEMENT"); //for randomly dropping the ships
	public static int endGame = 17; //determines if game is over or not
	//public static VBox oppBoard = new VBox(); //opponent board
	public static String turn;
	
	private Parent createContent(String host) {
			line.setFill(Color.DARKGRAY);
			battleShipServer = host;
			//root.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #dc143c, #661a33)");
			root.setStyle("-fx-background-color: DARKGRAY");
			root.setPrefSize(WIDTH, HEIGHT);
			
			//add title
			text = new Text("\nDrag and Drop ships onto your board\n OR use AUTO PLACEMENT");
			text.setFont(new Font(20));
			text.setFill(Color.BLACK);
			
			//add button
			
			//opens new window when button is clicked
			startGame.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					//do this when all ships are placed 
					try {
						if(numShips != 0) {
							Text error = new Text("MUST PLACE ALL SHIPS ON BOARD\nBEFORE CLICKING THE START BUTTON");
							error.setFill(Color.RED);
							boards = new VBox(50, Main.tileGroup, text2, error);
							root.setCenter(Main.boards);
							
						}else {
						
							Scene gameScene = new Scene(createGameContent()); // replace with another create content function?
						 	Stage gameStage = new Stage();
						 	gameStage.setTitle("Battleship");
						 	gameStage.setScene(gameScene);
						 
						 	gameStage.show();
						 	//hide original window
						 	((Node)(event.getSource())).getScene().getWindow().hide();
						 	runClient();
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					//else print out message
					
				}
			});
			
			//AUTO PLACEMENT HANDLER
			autoPlace.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					//do this when all ships are placed 
					try {
						if(numShips != 5) {
							//display error
							Text error = new Text("DRAG AND DROP STARTED\nPROCEED WITHOUT AUTO PLACEMENT");
							error.setFill(Color.RED);
							boards = new VBox(50, Main.tileGroup, text2, error);
							root.setCenter(Main.boards);
							
						}else {
							Tile.autoPlace(); //call auto place function
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					//else print out message
					
				}
			});
			
			//COLOR CODE FOR USER TO IDENTIFY CARRIER SHIP
			Text carrier = new Text ("CARRIER");
			carrier.setFont(new Font(20));
			carrier.setFill(Color.GOLD);

			//COLOR CODE FOR USER TO IDENTIFY battleship SHIP
			Text battleship = new Text ("BATTLESHIP");
			battleship.setFont(new Font(20));
			battleship.setFill(Color.GREEN);
			
			//COLOR CODE FOR USER TO IDENTIFY CRUSER SHIP
			Text cruiser = new Text ("CRUISER");
			cruiser.setFont(new Font(20));
			cruiser.setFill(Color.DARKBLUE);
			
			//COLOR CODE FOR USER TO IDENTIFY SUBMARINE SHIP
			Text submarine = new Text ("SUBMARINE");
			submarine.setFont(new Font(20));
			submarine.setFill(Color.BROWN);
			
			//COLOR CODE FOR USER TO IDENTIFY DESTROYER SHIP
			Text destroyer = new Text ("DESTROYER");
			destroyer.setFont(new Font(20));
			destroyer.setFill(Color.DARKVIOLET);
			
			VBox colorCode = new VBox(5, carrier, battleship, cruiser, submarine, destroyer);
			//PLACE ALL ITEMS INT VBOX
			VBox box = new VBox(5, text, startGame, autoPlace, new Text(" "));
			box.setAlignment(Pos.CENTER);
			root.setTop(box);
			
			//add each tile to tileGroup; displays tiles
			for(int x = 0; x < Grid.BOARDSIZE; x++) {
				for(int y = 0; y < Grid.BOARDSIZE; y++) {
					tileGroup.getChildren().add(player1.getPlayerBoard().getBoardTile(x, y));
				}
			}
			
			text2 = new Text("Number of Ships Left: "+ numShips + "   "); //display num of ships left
			text2.setFont(new Font(20));
			text2.setFill(Color.BLACK);
			
			//add player's board and number of ships left to boards
			boards = new VBox(50, tileGroup, text2);
					

			//flowPane.getChildren().add(boards); //add player board to flowPane
			//flowPane.setAlignment(Pos.CENTER);
			root.setCenter(boards);
	
			//get player ships		
			for(int i = 0; i < 5; i++) {
				shipSet.getChildren().add(player1.getPlayerShip(i));
			}
			
			HBox box3 = new HBox(new Text("       "), colorCode, new Text("       "));
			shipSet.getChildren().add(line);
			shipSet.setAlignment(Pos.CENTER);
			root.setLeft(shipSet);
			root.setRight(box3);
			return root;
			
		}
		
		
		public Parent createGameContent() {
			
			Text line = new Text(" ---------------------------------- ");
			line.setFill(Color.DARKGRAY);
			gamePane.setStyle("-fx-background-color: DARKGRAY");
			gamePane.setPrefSize(WIDTH2, HEIGHT2);
			
			Text text4 = new Text("OPPONENT BOARD");
			text4.setFont(new Font(15));
			text4.setFill(Color.RED);
			
			//add each tile2 to tileGroup2; displays tiles
			for(int x = 0; x < Grid.BOARDSIZE; x++) {
				for(int y = 0; y < Grid.BOARDSIZE; y++) {
					tileGroup2.getChildren().add(player2.getPlayerBoard().getBoardTile(x, y));
				}
			}
			
			boards2 = new VBox(10, new Text(" "), tileGroup2, text4); //add boards to vbox
			boards2.setAlignment(Pos.CENTER);
			
			flowPane2.getChildren().add(boards);
			flowPane2.getChildren().add(boards2); //add all boards to flowPane2
			flowPane2.setAlignment(Pos.CENTER);
			
			gamePane.setCenter(flowPane2);
			
			//for ship status
			
			opponentStatus = new Text("Opponent has " + player2.getNumShipsAlive() + " ships alive");
			opponentStatus.setFont(new Font(20));
			opponentStatus.setFill(Color.BLACK);
			
			yourShips = new Text("Your Ships Remaining:");
			yourShips.setFont(new Font(15));
			yourShips.setFill(Color.BLACK);
			
			carrierStatus = new Text("Carrier:  " + Player.getShipI(0).getRemainingPieces() + " pieces left");
			carrierStatus.setFont(new Font(15));
			carrierStatus.setFill(Color.GOLD);
			
			cruiserStatus = new Text("Cruiser:  " + Player.getShipI(2).getRemainingPieces() + " pieces left");
			cruiserStatus.setFont(new Font(15));
			cruiserStatus.setFill(Color.DARKBLUE);
			
			submarineStatus = new Text("Submarine:  " + Player.getShipI(3).getRemainingPieces() + " pieces left");
			submarineStatus.setFont(new Font(15));
			submarineStatus.setFill(Color.BROWN);
			
			destroyerStatus = new Text("Destroyer:  " + Player.getShipI(4).getRemainingPieces() + " pieces left");
			destroyerStatus.setFont(new Font(15));
			destroyerStatus.setFill(Color.DARKVIOLET);
			
			battleshipStatus = new Text("Battleship:  " + Player.getShipI(1).getRemainingPieces() + " pieces left");
			battleshipStatus.setFont(new Font(15));
			battleshipStatus.setFill(Color.GREEN);
			
			shipStatus = new VBox(10, opponentStatus, yourShips, carrierStatus, battleshipStatus, cruiserStatus, submarineStatus, destroyerStatus);// add ship status
			shipStatus.setAlignment(Pos.CENTER);
			gamePane.setLeft(line);
			gamePane.setRight(shipStatus);
			boards.getChildren().remove(text2);
			
			return gamePane;
		}
		
		/*
		 * 
		 * NETWORK CODE
		 * 
		 */
		
		public void runClient() 
		   {
		      try // connect to server, get streams, process connection
		      {
		         connectToServer(); // create a Socket to make connection
		         getStreams(); // get the input and output streams
		         processConnection(); // process connection
		      } // end try
		      catch ( EOFException eofException ) 
		      {
		    	  Ship.pop("Client terminated connection", 1);
		      } // end catch
		      catch ( IOException ioException ) 
		      {
		         ioException.printStackTrace();
		      } // end catch
		      finally 
		      {
		         closeConnection(); // close connection
		      } // end finally
		   } // end method runClient
		
		
		//connect to server
		 private void connectToServer() throws IOException
		 {      
			 Ship.pop("Attempting connection", 2);

		    // create Socket to make connection to server
		    client = new Socket( InetAddress.getByName( battleShipServer ), 12345 );

		    // display connection information
		    Ship.pop("Connected to: " + 
		    client.getInetAddress().getHostName(), 2);
		  } // end method connectToServer
		 
		//get streams to send and receive data
		  private void getStreams() throws IOException
		  {
	      // set up output stream for objects
			  output = new ObjectOutputStream( client.getOutputStream() );      
			  output.flush(); // flush output buffer to send header information

		      // set up input stream for objects
		      input = new ObjectInputStream( client.getInputStream() );
		      Ship.pop("Got I/O streams", 2);
		   } // end method getStreams

		  
		//process connection with server
		   private void processConnection() throws IOException
		   {

		      do // process messages sent from server
		      {
		         try // read message and display it
		         {
		        	 //message = (String) input.readObject(); // read new message
		             coorX = (int) input.readObject(); // read new message
		             //sendData("Game Connected to server");
		         } // end try
		         catch ( ClassNotFoundException classNotFoundException ) 
		         {
		        	 Ship.pop("Unknown object type received", 1);
		         } // end catch

		      } while (!message.equals( "SERVER>>> TERMINATE" ));
		   } // end method processConnection
		   
		   
		//send message to server
		   private void sendData( String message )
		   {
		      try // send object to client
		      {
		         output.writeObject(message);
		         output.flush(); // flush output to client
		         //Ship.pop("\nSERVER>>> " + message );
		         Ship.pop(message, 2);
		      } // end try
		      catch (IOException ioException) 
		      {
		    	  Ship.pop("Error writing object", 1);
		      } // end catch
		   } // end method sendData
		   
		// close streams and socket
		   private void closeConnection() 
		   {
			   Ship.pop("Terminating connection", 1);
		      try 
		      {
		         output.close(); // close output stream
		         input.close(); // close input stream
		         client.close(); // close socket
		      } // end try
		      catch ( IOException ioException ) 
		      {
		         ioException.printStackTrace();
		      } // end catch
		   } // end method closeConnection
	   
	@Override
	public void start(Stage primaryStage) { // code for JavaFX application goes in here
		try {
			//create scene
			Scene scene = new Scene(createContent("localhost"));
			
			//title of stage
			primaryStage.setTitle("COSC 330 Battleship Game");
			
			//add scene to stage
			primaryStage.setScene(scene);
			
			primaryStage.setResizable(true);
			
			//display stage contents
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args); //launch application
	}
}