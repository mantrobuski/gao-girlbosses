package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

public class GameState 
{
	
	int[] board; //1D

	boolean whiteTurn; //true if white to move, false if black to move
	
	//this is for first turn
	public GameState()
	{
		//all 0s
		this.board = new int[100];
		
		//white queens
		board[30] = 1;
		board[60] = 1;
		board[3] = 1;
		board[93] = 1;
		
		//black queens
		board[39] = -1;
		board[69] = -1;
		board[6] = -1;
		board[96] = -1;
		
		whiteTurn = false;
	}
	
	public GameState(ArrayList<Integer> boardIn, boolean whiteTurn)
	{
		//convert arraylist to array because it's faster
		board = new int[boardIn.size()];
        for(int i = 0; i < board.length; i++)
        {
        	board[i] = boardIn.get(i);
        }
		this.whiteTurn = whiteTurn;
	}
	
	public GameState(int[] board, boolean whiteTurn)
	{
		this.board = board;
		this.whiteTurn = whiteTurn;
	}
	
	//white = true means checking white, false means checking black
	//function returns true if the colour being checked has won the game in this state
	public boolean checkWin(boolean white)
	{
		//TODO: implement.
		return false;
	}
	
	//0 indexed arrays, 1 indexed board notation because ???
	public static int yxToIndex(int y, int x)
	{
		System.out.println("Returning: ");
		return (y - 1) + 10* (x - 1);
	}
	
	public static int[] indexToYX(int index)
	{
		int y = (index % 10) + 1;
		int x = (index / 10) + 1;
		
		int[] output = {y, x};
		
		return output;
	}
	
	//there is no validation here, it is assuming you are providing a valid move
	public GameState makeMove(Move move)
	{
		//new state starts the same as the current state, except the turn is flipped
		GameState newState = new GameState(this.board, !this.whiteTurn);
		
		int qVal = newState.board[move.qCur]; //get if this is a +1 or -1 queen
		newState.board[move.qCur] = 0; //queen moves off the tile
		newState.board[move.qMove] = qVal; //queen moves to new tile
		newState.board[move.arrow] = -99; //shoot the arrow
		
		return newState;
	}
	
	public ArrayList<Move> getMoves()
	{
		ArrayList<Move> output = new ArrayList<Move>();
		//look at the Move class to see what the Move object is,
		//it's just the three components in index notation
		
		ArrayList<Integer> queens = new ArrayList<Integer>();
		
		//set which queen symbol we are looking for
		int queenTarget = 0;
		if(this.whiteTurn) queenTarget = 1;
		else queenTarget = -1;
		
		for(int i = 0; i < this.board.length; i++)
		{
			//add the index of the queens we care about
			if(board[i] == queenTarget) queens.add(i);
		}
		
		//one helpful thing
		//if(board[index] != 0) ---> if this condition is true there is something that blocks vision (shot tile, or another queen)
		
		//for each queen figure out every tile they can move to in all 8 directions
		int[] queenXY = new int [2]; //will hold xy index for queens
		ArrayList<int[]> tempMoves = new ArrayList<>(); //holds moves the queen can move to
		int xQueen = 0;
		int yQueen = 0;
		int tempIndex = 0;
		
		// loop through each queen
		for (int i = 0; i < queens.size(); i++) {
			queenXY = indexToYX(queens.get(i));
			for (int j = 0; j < 8; j++) // 8 possible directions, starts at up, then clockwise
			{
				xQueen = queenXY[1]; //temporary x and y positions. don't want to overwrite initial for next direction 
				yQueen = queenXY[0];
				while ((xQueen > 0 && xQueen <= 10) && (yQueen > 0 && yQueen <= 10)) { // while loop for positions within the board
					switch (j) {
						case 0: // UP
							yQueen++;
							break;
						case 1: // UP-RIGHT
							xQueen++;
							yQueen++;
							break;
						case 2: // RIGHT
							xQueen++;
							break;
						case 3: // RIGHT-DOWN
							xQueen++;
							yQueen--;
							break;
						case 4: // DOWN
							yQueen--;
							break;
						case 5: // DOWN-LEFT
							yQueen--;
							xQueen--;
							break;
						case 6: // LEFT
							xQueen--;
							break;
						case 7: // UP-LEFT
							xQueen--;
							yQueen++;
							break;
					}
					
					if(xQueen < 1 || yQueen < 1 || xQueen > 10 || yQueen > 10) break;
					tempIndex = yxToIndex(yQueen, xQueen);
	
					//System.out.println("x:" + xQueen + ", y: " + yQueen);
					if (this.board[tempIndex] == 0) { // SLOW fix later
						int[] array = {yxToIndex(queenXY[0], queenXY[1]), tempIndex};
						tempMoves.add(array);
					}
					else
						break;

				}	
			}
		}

		//do same as above but for arrows with all moves in tempMoves
		int xArrow = 1;
		int yArrow = 1;

		for (int i = 0; i < tempMoves.size(); i++) {
			int[] array = tempMoves.get(i);
			queenXY = indexToYX(array[1]);
			for (int j = 0; j < 8; j++) // 8 possible directions, starts at up, then clockwise
			{
				xArrow = queenXY[1];
				yArrow = queenXY[0];
				while ((xArrow > 0 && xArrow <= 10) && (yArrow > 0 && yArrow <= 10)) { // could make less cases with mod?
					switch (j) {
						case 0: // UP
							yArrow++;
							break;
						case 1: // UP-RIGHT
							xArrow++;
							yArrow++;
							break;
						case 2: // RIGHT
							xArrow++;
							break;
						case 3: // RIGHT-DOWN
							xArrow++;
							yArrow--;
							break;
						case 4: // DOWN
							yArrow--;
							break;
						case 5: // DOWN-LEFT
							yArrow--;
							xArrow--;
							break;
						case 6: // LEFT
							xArrow--;
							break;
						case 7: // UP-LEFT
							xArrow--;
							yArrow++;
							break;
					}
					
					if(xArrow < 1 || yArrow < 1 || xArrow > 10 || yArrow > 10) break;
					tempIndex = yxToIndex(yArrow, xArrow);
	
					if (this.board[tempIndex] == 0 || tempIndex == array[0]) {
						Move move = new Move(array[0], array[1], tempIndex); //these are in index notation
						output.add(move);
					}
					else
						break;
				}	
			}
		}
		if (output.size() == 0)
			return null;
		else 	
			return output;
	}
	
	//THIS SHOULD RETURN A POSITIVE NUMBER FOR WHITE WINNING AND A NEGATIVE ONE FOR BLACK WINNING
	//USE this.board, to reference the board
	
	public int minDistanceHeuristic() {// datatype may need to be changed
		int distTotal=0;
		int boardSize = 10;
		
		//this needs to work regardless of the player
		//so change the logic to use negative for black and positive for white and add together the white and black scores to get a combined evaluation
		
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
//				if(board[i][j]==player) {
//					int distance= minOpponentDistance(player,i,j);
//					distTotal += distance;
//					
//				}
			}
		}
		
		
		return distTotal;
		
		
	}
	
	public int minOpponentDistance(int row, int col) {
		int minDist= Integer.MAX_VALUE;
		int boardSize = 10;
		
		//you can use GameState.indexToYX() to convert from index notation to coordinates
		//GameState.yxToIndex() will go the other way
		
		// iterating through the board to find opponents
		for(int i=0; i< boardSize; i++) {
			for(int j=0; j< boardSize; j++) {
				//0 is empty position, +1 is white queen, -1 is black queen in the board array
//				if(board[i][j] != player && board[i][j]!=' ') {
//					int distance= Math.abs(row-i)+Math.abs(col-j);
//					if(distance<minDist) {
//						minDist=distance;
//					}
//				}
			}
		}
		return minDist;
	}
	
	@Override
	public boolean equals(Object other)
	{
		return Arrays.equals((int[]) other, this.board);
	}
	
	@Override
	//TODO: compare this to using Arrays.hashCode(board);
	public int hashCode()
	{
		//this is dirty but is faster than Arrays.equals()
		//sum up every 5th value
		int hash = 0;
		for(int i = 0; i < board.length; i+=5)
		{
			hash += board[i];
		}
		return hash;
	}

}
