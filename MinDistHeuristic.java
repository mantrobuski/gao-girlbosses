import java.util.ArrayList;

public class MinDistHeuristic {
	public int boardSize=10;
	
	// defining the players (to help with my implementation)
	public char pW='W'; // player white
	public char pB='B'; // player black
	
	public char[][]board= { // sample board
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', '1', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
            
	};
	
	public int evaluation(char player) {// datatype may need to be changed
		int distTotal=0;
		
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				if(board[i][j]==player) {
					int distance= minOpponentDistance(player,i,j);
					distTotal += distance;
					
				}
			}
		}
		
		
		return distTotal;
		
		
	}
	
	public int minOpponentDistance(char player, int row, int col) {
		int minDist= Integer.MAX_VALUE;
		
		// iterating through the board to find opponents
		for(int i=0; i< boardSize; i++) {
			for(int j=0; j< boardSize; j++) {
				if(board[i][j] != player && board[i][j]!=' ') {
					int distance= Math.abs(row-i)+Math.abs(col-j);
					if(distance<minDist) {
						minDist=distance;
					}
				}
			}
		}
		return minDist;
	}
}
