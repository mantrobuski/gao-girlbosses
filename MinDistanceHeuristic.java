package ubc.cosc322;
import java.util.LinkedList;
import java.util.Queue;

public class MinDistanceHeuristic {
	
	int board[]; // game board array indexed 1-100
	
	/* min distance heuristic function that decides which player
	 * evaluates a player's move in Game of the Amazons
	 * 
	 * Checks which player can reach a square faster with a queen
	 * Ignores arrow shots
	 * 
	 */
	
	public int playerWhiteOwns =0;
	public int playerBlackOwns=0;
	
	/* board= null
	 * b1=null
	 * queens= new Queen[8]; // instantiate an array of queens?
	 */
	
	/*
	 * @param b: the game rules board
	 */
	
	public void evaluate(GameRules b) {
		b1=b;
		board=b1.board;
		
		for(int j=0; j<8; j++) {
			if(j<4) {
				
			}
		}
		
	}
	
	/*
	 * Finds the nearest queen
	 */
	public void findQueen(int index) {
		Queue<Object> queens= new LinkedList<>();
		
		boolean [] check= new boolean[100];
		check[index]=true;
		boolean found= false;
		
		queens= possibleMoves(queens, index, check);
		
		while(!found) {
			Queue<Object> qPrime= new LinkedList<>();
			int i= queens.size();
			// if this evaluates to true, then the player is blocked in
			if(i==0) {
				found=true;
				break;
				
			}
			
			// now we neeeeeeed to check every possible move from current tile 'object'
			if((board[index] != null) &&(board[index])= Queen){// check if current index is not null and a queen
				found=true;
			}
	}
	
	public Queue<Object> possibleMoves(Queue<Object> queens, int index, boolean[] check){
		for (int i=1; index<100 ;i++) { // are we indexing up to 100?
			
			
		}
			
		}
		
		return queens;
	}
	
}
