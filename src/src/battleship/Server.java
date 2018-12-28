/* 	- Connect to the server
   	- Notify the player of the connection and game state
	- Communicate the player's move to the server
	- Receive the other player's move from the server
	- Update the game with the state received from the server
*/

//campus.murraystate.edu/academic/faculty/wlyle/325/Chapter27.pdf

package src.battleship;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import javafx.scene.Parent;
import javafx.stage.Stage;
/*import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
*/

public class Server{
	
	private ServerSocket server; //server sockets to connect with clients
	private int currentPlayer; //keeps track of player with current move
	private final static int player_1 = 0; //first player
	private final static int player_2 = 1; //second player
	private ExecutorService runGame; //to run players
	private Lock gameLock; //lock game for synchronization
	private Condition otherPlayerConnected; //wait for other player
	private Condition otherPlayerTurn; //wait for other player's turn
	private String[] player1Board = new String[100]; //player one's board
	private String[] player2Board = new String[100]; //player two's board
	private Player[] players; //array of players
	
	//server setup
	public Server() {
		//super("BATTLESHIP SERVER"); //title of window
		
		//ExecutorService with a thread for each player
		runGame = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock(); //lock for game
		
		//condition variable for both players being connected
		otherPlayerConnected = gameLock.newCondition();
		
		//condition variable for the other player's turn
		otherPlayerTurn = gameLock.newCondition();
		//create board1
		for(int i = 0; i < 100; i++) {
			player1Board[i] = new String("");
		}
		//create board2
		for(int i = 0; i < 100; i++) {
			player2Board[i] = new String("");
		}
		players = new Player[2]; //create array of players
		currentPlayer = player_1; //set current player to player 1
		
		//setup ServerSocket
		try {
			server = new ServerSocket(3000, 2);
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Server awaiting connections\n");
	}
	
	//Waiting for two connections before game begins
	public void execute() {
		//wait for each client to connect
		for(int i = 0; i < players.length; i++) {
			//wait for connection, create player, start runnable
			try {
				players[i] = new Player(server.accept(), i);
				runGame.execute(players[i]); //execute player runnable
			}
			catch(IOException ioException)
			{
				ioException.printStackTrace();
				System.exit(1);
			}
		}//end for
		
		gameLock.lock(); //lock game to signal player1's thread
		
		try {
			players[player_1].setSuspended(false); //resume player 1
			otherPlayerConnected.signal(); //wake up player 1's thread
		}
		finally {
			gameLock.unlock(); //unlock game after signaling player 1
		}
	}//end execute function
	
	//display function
	private void displayMessage(final String message) {
			System.out.println(message);
	}//end display function
	
	//determine if move is valid
	public boolean validateAndMove(int location, int player) {
		//while not current player, must wait for turn
		while(player != currentPlayer) {
			gameLock.lock(); //lock game to wait for other player to play turn
			try {
				otherPlayerTurn.await(); //wait for player's turn
			}
			catch(InterruptedException exception) {
				exception.printStackTrace();
			}
			finally {
				gameLock.unlock(); //unlock game after waiting
			}
		}//end while
		
		if(!isOccupied(location)) {
			//if not occupied mark location
			//player1Board[location] = 
			currentPlayer = (currentPlayer + 1) % 2; //change player
			
			//let new current player know that move occurred
			players[currentPlayer].otherPlayerMoved(location);
			
			gameLock.lock(); //lock game to signal other player's turn
			
			try {
				otherPlayerTurn.signal(); //signal other player to continue
			}
			finally {
				gameLock.unlock(); //unlock game after signaling
			}
			return true; //notify that move was valid
		}//end if
		else return false; //move was invalid
	}//end validateAndMove
	
	//determine if location is occupied
	public boolean isOccupied(int location) {
		/*if(player1Board[location].equals())
		 * 
		 */
		return true; //location is occupied
		
		
		  //else return false; //location is not occupied
		 
	}//end isOccupied
	
	//Determines if game is over
	public boolean isGameOver() {
		/*implement this*/
		return false; 
	}
	
	//this class manages each player
	private class Player implements Runnable{
		private Socket connection; //connection to client
		private Scanner input; //input from client
		private Formatter output; //output to client
		private int playerNumber; //tracks which player this is
		//private String mark; //this is probably color
		private boolean suspended = true; //whether thread is suspended
		
		//set up Player thread
		public Player(Socket socket, int number) {
			playerNumber = number; //store player's number
			/* add something here */
			connection = socket; //store socket for client
			
			//obtain streams from socket
			try {
				input = new Scanner(connection.getInputStream());
				output = new Formatter(connection.getOutputStream());
			}
			catch(IOException ioException) {
				ioException.printStackTrace();
				System.exit(1);
			}
		}//end Player constructor
		
		public void otherPlayerMoved(int location) {
			output.format("Opponent moved\n");
			output.format("%d\n", location); //send location of move
			output.flush(); //flush output
		}
		
		//control thread's execution
		public void run() {
			//send client cells clicked, process messages from client
			try {
				displayMessage("Player 1 connected");
				/*add extra stuff*/
				output.format("%s\n"/* ,args*/);
				output.flush();
				
				//if player one, wait for another player to arrive
				if(playerNumber == player_1) {
					output.format("%s\n%s", "Player 1 connected", "Waiting for another player\n");
					output.flush();
					
					gameLock.lock(); //lock game to wait for second player
					
					try {
						while(suspended) {
							otherPlayerConnected.await(); //wait for other player
						}
					}
					catch(InterruptedException exception){
						exception.printStackTrace();
					}
					finally {
						gameLock.unlock(); //unlock game after second player
					}
					
					//send message that other player connected
					output.format("Other player connected. Your move.\n");
					output.flush();
				}//end if
				else {
					output.format("Player 2 connected, Please wait\n");
					output.flush();
				}
				
				while(!isGameOver()) {
					int location = 0; //initialize move location
					
					if(input.hasNext()) {
						location = input.nextInt(); //get move location
					}
					
					//validate move
					if(validateAndMove(location, playerNumber)) {
						displayMessage("\nlocation: "+ location);
						output.format("Valid move.\n"); //notify client
						output.flush();
					}
					else {
						output.format("Invalid move, try again\n");
						output.flush();
					}
				}//end while
			}
			finally {
				try {
					connection.close(); //close connection
				}
				catch(IOException ioException) {
					ioException.printStackTrace();
					System.exit(1);
				}
			}
		}//end run function
		
		//set whether or not thread is suspended
		public void setSuspended(boolean status) {
			suspended = status; //set value of suspended
		}
	}//end player class

}
