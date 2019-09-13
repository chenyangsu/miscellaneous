

import java.io.*;

public class Game {
	
	public static int play(InputStreamReader input){
		BufferedReader keyboard = new BufferedReader(input);
		Configuration c = new Configuration();
		int columnPlayed = 3; int player;
		
		// first move for player 1 (played by computer) : in the middle of the grid
		c.addDisk(firstMovePlayer1(), 1);
		int nbTurn = 1;
		
		while (nbTurn < 42){ // maximum of turns allowed by the size of the grid
			player = nbTurn %2 + 1;
			if (player == 2){
				columnPlayed = getNextMove(keyboard, c, 2);
			}
			if (player == 1){
				columnPlayed = movePlayer1(columnPlayed, c);
			}
			System.out.println(columnPlayed);
			c.addDisk(columnPlayed, player);
			if (c.isWinning(columnPlayed, player)){
				c.print();
				System.out.println("Congrats to player " + player + " !");
				return(player);
			}
			nbTurn++;
		}
		return -1;
	}
	
	public static int getNextMove(BufferedReader keyboard, Configuration c, int player){
		
		try{
			System.out.println("The AI has made its move:");
			c.print();
			System.out.println("Your turn. Please enter a column");
			String move= keyboard.readLine();
			//If the user input is not an String between 0 and 6(inclusive), 
			// keep asking for a new input
			while(!move.equals("0") && !move.equals("1") 
					&& !move.equals("2") && !move.equals("3") 
					&& !move.equals("4") && !move.equals("5") 
					&& !move.equals("6")) {
				System.out.println("Please pick a value between 0 and 6 (including)");
				move = keyboard.readLine();
			}
			while(c.available[Integer.parseInt(move)]>5) {
				System.out.println("That column is full already. Choose another column.");
				move = keyboard.readLine();
				while(!move.equals("0") && !move.equals("1") && !move.equals("2") && !move.equals("3") && !move.equals("4") && !move.equals("5") && !move.equals("6")) {
					System.out.println("Please pick a value between 0 and 6 (including)");
					move = keyboard.readLine();
				}
			}
			//convert input of user to int
			int moveColumn = Integer.parseInt(move);
			return moveColumn;
			
			} catch(IOException e) {//catch any exceptions and returning -1
				System.out.println("Error caught!");
				return -1;
			}
		
	}
	
	public static int firstMovePlayer1 (){
		return 3;
	}
	
	public static int movePlayer1 (int columnPlayed2, Configuration c){
		
		int col1 = c.canWinNextRound(1);
		int col2 = c.canWinTwoTurns(1);
		if(col1!=-1) {//if column is not -1(there is a column to win next), return that column
			return col1;
		} else if(col2!=-1) {//if column is not -1(there is a column where player 1 can win in two turns), return that column
			return col2;
		} else { 
			if(c.available[columnPlayed2]<6) { //if possible, player1 adds to the same column as player 2
				return columnPlayed2; 
			} else { //last column played by player 2 is full
				for(int i=1; i<7; i++) { 
					//Starting at 1, running through the possible increments(decrements)
					if(columnPlayed2 - i>=0 && c.available[columnPlayed2-i]<6) { //if adding at columnPlayed2-i is possible, returning that column
						return columnPlayed2-i;
					} 
					if(columnPlayed2+i<=6 && c.available[columnPlayed2+i]<6) { //if adding at columnPlayed2+i is possible, returning that column
						return columnPlayed2+i;
					}
				}
			}
		}
		//is no where on the board to add a disk
		return -1; 
	
}
