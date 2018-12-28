package src.battleship;

import java.util.Random;

/*
 * Kia Porter and Chukwubuikem Okafo
 * COSC 330: OO Design Pattern, GUI and Event-driven Programming
 * Project #1: Battleship Game
 * Due October 5, 2018
*/
public class Coordinates {
	
	//member variables
	protected int x;
	protected int y;
	
	//constructor
	public Coordinates() {
		this.x = -1;
		this.y = -1;
	}
	public void setX(int X) {
		this.x = X;
	}

	public void setY(int Y) {
		this.y = Y;
	}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	//RANDOM PLACEMENT FUNCTION
	public static int randPlace() {
		Random rand = new Random();
		int coordinate = rand.nextInt(10);
		//System.out.println("randPlace - " + coordinate);
		return coordinate;
	}
	
	//random orientation function
	public static int randOrien() {
		Random rand = new Random();
		int orientation = rand.nextInt(3);
		//System.out.println("randOrient - " + orientation);
		return orientation;
	}
}
