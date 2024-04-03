package ubc.cosc322;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.IntStream;

public class GameState 
{
	
	int[] board; //1D

	boolean whiteTurn; //true if white to move, false if black to move
	
	int val = 0; //this will be updated by the heuristic function
	boolean evaluated = false;
	
	//this is for initial state which is always the same
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
	
	
	//0 indexed arrays, 1 indexed board notation because ???
	public static int yxToIndex(int y, int x)
	{
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
		GameState newState = new GameState(this.board.clone(), !this.whiteTurn);
		
		int qVal = newState.board[move.qCur]; //get if this is a +1 or -1 queen
		newState.board[move.qCur] = 0; //queen moves off the tile
		newState.board[move.qMove] = qVal; //queen moves to new tile
		newState.board[move.arrow] = -99; //shoot the arrow
		
		return newState;
	}
	
	public ArrayList<Integer> queenMoves(int queenXY) {
		int[] array = new int [2];
		ArrayList<Integer> tempMoves = new ArrayList<>();
		int xQueen = 0;
		int yQueen = 0;
		int tempIndex = 0;
		
		array = indexToYX(queenXY);
		for (int j = 0; j < 8; j++) // 8 possible directions, starts at up, then clockwise
		{
			xQueen = array[1]; //temporary x and y positions. don't want to overwrite initial for next direction 
			yQueen = array[0];
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

				if (this.board[tempIndex] == 0) { // SLOW fix later
					tempMoves.add(tempIndex);
				}
				else
					break;
			}	
		}
		return tempMoves;
	}

	/*
	public ArrayList<Move> arrowMoves(ArrayList<Integer> moves, int queenIndex) {
		int xArrow = 1;
		int yArrow = 1;
		int [] queenXY = new int[2];
		ArrayList<Move> output = new ArrayList<Move>();

		for (int i = 0; i < moves.size(); i++) {
			queenXY = indexToYX(moves.get(i));
			for (int j = 0; j < 8; j++) // 8 possible directions, starts at up, then clockwise
			{
				xArrow = queenXY[1];
				yArrow = queenXY[0];
				while ((xArrow > 0 && xArrow <= 10) && (yArrow > 0 && yArrow <= 10)) {
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
					int tempIndex = yxToIndex(yArrow, xArrow);
	
					//shooting arrow where you originally where
					if(tempIndex == queenIndex)
					{
						Move move = new Move(queenIndex, moves.get(i), tempIndex); //these are in index notation
						output.add(move);
					}
					else if (this.board[tempIndex] == 0 && tempIndex != yxToIndex(queenXY[0], queenXY[1])) {
						Move move = new Move(queenIndex, moves.get(i), tempIndex); //these are in index notation
						output.add(move);
					}
					else
						break;
				}	
			}
		}
		return output;
	}
	*/
	
	public ArrayList<Move> getMoves()
	{
		ArrayList<Move> output = new ArrayList<Move>();
		
		int target = (whiteTurn ? 1 : -1);
		
		//locate the queens
		int[] queenLocations = new int[4];
		int qi = 0;
		
		
		for(int i = 0; i < board.length; i++)
		{
			if(board[i] == target)
			{
				queenLocations[qi] = i;
				qi++;
				if(qi == 4) break; //found all queens
			}
		}
		
		//now for each queen
		for(int queen : queenLocations)
		{
			ArrayList<Integer> sees = new ArrayList<Integer>();
			int cursor = queen;
			
			//this is to reduce the total number of divisions
			//range means how many tiles you can move in a given direction before going out of bounds
			//when we move diagonally the range will the the min of the two cardinal directions
			int upRange = 9 - (cursor % 10); 
			int downRange = -(0 - (cursor % 10));
			int rightRange = 9 - (cursor / 10);
			int leftRange = -(0 - (cursor / 10));
			
			
			//up
			int range = upRange;
			while(range > 0)
			{
				cursor += 1; //move UP one
				
				//valid move
				if(board[cursor] == 0)
				{
					sees.add(cursor);
				}
				else break; //can't see anywhere else in this direction cause it's blocked
				
				range--;
			}
			
			//down
			cursor = queen; //reset cursor
			range = downRange;
			while(range > 0)
			{
				cursor -= 1; //move DOWN one
				
				//valid move
				if(board[cursor] == 0)
				{
					sees.add(cursor);
				}
				else break; //can't see anywhere else in this direction cause it's blocked
				
				range--;
			}
			
			//right
			cursor = queen; //reset cursor
			range = rightRange;
			while(range > 0)
			{
				cursor += 10; //move RIGHT one
				
				//valid move
				if(board[cursor] == 0)
				{
					sees.add(cursor);
				}
				else break; //can't see anywhere else in this direction cause it's blocked
				
				range--;
			}
			
			//left
			cursor = queen; //reset cursor
			range = leftRange;
			while(range > 0)
			{
				cursor -= 10; //move LEFT one
				
				//valid move
				if(board[cursor] == 0)
				{
					sees.add(cursor);
				}
				else break; //can't see anywhere else in this direction cause it's blocked
				
				range--;
			}
			
			//up-right
			cursor = queen; //reset cursor
			range = Math.min(rightRange, upRange);
			while(range > 0)
			{
				cursor += 11; //move UP one and RIGHT one
				
				//valid move
				if(board[cursor] == 0)
				{
					sees.add(cursor);
				}
				else break; //can't see anywhere else in this direction cause it's blocked
				
				range--;
			}
			
			//up-left
			cursor = queen; //reset cursor
			range = Math.min(leftRange, upRange);
			while(range > 0)
			{
				cursor -= 9; //move UP one and LEFT one
				
				//valid move
				if(board[cursor] == 0)
				{
					sees.add(cursor);
				}
				else break; //can't see anywhere else in this direction cause it's blocked
				
				range--;
			}
			
			//down-right
			cursor = queen; //reset cursor
			range = Math.min(rightRange, downRange);
			while(range > 0)
			{
				cursor += 9; //move DOWN one and RIGHT one
				
				//valid move
				if(board[cursor] == 0)
				{
					sees.add(cursor);
				}
				else break; //can't see anywhere else in this direction cause it's blocked
				
				range--;
			}
			
			//down-left
			cursor = queen; //reset cursor
			range = Math.min(leftRange, downRange);
			while(range > 0)
			{
				cursor -= 11; //move DOWN one and LEFT one
				
				//valid move
				if(board[cursor] == 0)
				{
					sees.add(cursor);
				}
				else break; //can't see anywhere else in this direction cause it's blocked
				
				range--;
			}
			
			//now do this again for all the tiles you just discovered for arrow locations
			for(int qMove : sees)
			{
				cursor = qMove;
				
				//this is to reduce the total number of divisions
				//range means how many tiles you can move in a given direction before going out of bounds
				//when we move diagonally the range will the the min of the two cardinal directions
				upRange = 9 - (cursor % 10); 
				downRange = -(0 - (cursor % 10));
				rightRange = 9 - (cursor / 10);
				leftRange = -(0 - (cursor / 10));
				
				
				//up
				range = upRange;
				while(range > 0)
				{
					cursor += 1; //move UP one
					
					//valid move (this time allowing the arrow to go where the queen started)
					if(board[cursor] == 0 || cursor == queen)
					{
						output.add(new Move(queen, qMove, cursor));
					}
					else break; //can't see anywhere else in this direction cause it's blocked
					
					range--;
				}
				
				//down
				cursor = qMove; //reset cursor
				range = downRange;
				while(range > 0)
				{
					cursor -= 1; //move DOWN one
					
					//valid move (this time allowing the arrow to go where the queen started)
					if(board[cursor] == 0 || cursor == queen)
					{
						output.add(new Move(queen, qMove, cursor));
					}
					else break; //can't see anywhere else in this direction cause it's blocked
					
					range--;
				}
				
				//right
				cursor = qMove; //reset cursor
				range = rightRange;
				while(range > 0)
				{
					cursor += 10; //move RIGHT one
					
					//valid move (this time allowing the arrow to go where the queen started)
					if(board[cursor] == 0 || cursor == queen)
					{
						output.add(new Move(queen, qMove, cursor));
					}
					else break; //can't see anywhere else in this direction cause it's blocked
					
					range--;
				}
				
				//left
				cursor = qMove; //reset cursor
				range = leftRange;
				while(range > 0)
				{
					cursor -= 10; //move LEFT one
					
					//valid move (this time allowing the arrow to go where the queen started)
					if(board[cursor] == 0 || cursor == queen)
					{
						output.add(new Move(queen, qMove, cursor));
					}
					else break; //can't see anywhere else in this direction cause it's blocked
					
					range--;
				}
				
				//up-right
				cursor = qMove; //reset cursor
				range = Math.min(rightRange, upRange);
				while(range > 0)
				{
					cursor += 11; //move UP one and RIGHT one
					
					//valid move (this time allowing the arrow to go where the queen started)
					if(board[cursor] == 0 || cursor == queen)
					{
						output.add(new Move(queen, qMove, cursor));
					}
					else break; //can't see anywhere else in this direction cause it's blocked
					
					range--;
				}
				
				//up-left
				cursor = qMove; //reset cursor
				range = Math.min(leftRange, upRange);
				while(range > 0)
				{
					cursor -= 9; //move UP one and LEFT one
					
					//valid move (this time allowing the arrow to go where the queen started)
					if(board[cursor] == 0 || cursor == queen)
					{
						output.add(new Move(queen, qMove, cursor));
					}
					else break; //can't see anywhere else in this direction cause it's blocked
					
					range--;
				}
				
				//down-right
				cursor = qMove; //reset cursor
				range = Math.min(rightRange, downRange);
				while(range > 0)
				{
					cursor += 9; //move DOWN one and RIGHT one
					
					//valid move (this time allowing the arrow to go where the queen started)
					if(board[cursor] == 0 || cursor == queen)
					{
						output.add(new Move(queen, qMove, cursor));
					}
					else break; //can't see anywhere else in this direction cause it's blocked
					
					range--;
				}
				
				//down-left
				cursor = qMove; //reset cursor
				range = Math.min(leftRange, downRange);
				while(range > 0)
				{
					cursor -= 11; //move DOWN one and LEFT one
					
					//valid move (this time allowing the arrow to go where the queen started)
					if(board[cursor] == 0 || cursor == queen)
					{
						output.add(new Move(queen, qMove, cursor));
					}
					else break; //can't see anywhere else in this direction cause it's blocked
					
					range--;
				}
			}
		}
		
		if(output.size() == 0) return null; //NO MOVES, this is an end position, someone has lost
		else return output;
		
	}
		
	//***********************************************************************
	//swap out the function called here to use a different heuristic function
	//***********************************************************************
	public int evaluate() 
	{
		if(this.evaluated) return this.val;
		
		this.evaluated = true;
		
		//first check if this is a won or lost position
		//if white wins, val should be +inf - 1, black win should return -inf + 1
		ArrayList<Move> moves = getMoves();
		if(moves == null)
		{
			//no moves remain
			//the player whos turn it is loses
			val = (whiteTurn ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1);
		}
		
		//otherwise use a heuristic if no winner in position
		else this.val = territoryHeuristic(); //***CHANGE THIS ***//
		
		return this.val;
	}
	
	//it should return a positive value if white has more territory
	//negative for black havnig more territory
	//refer to that one super helpful paper we keep referencing to see the logic/definition of this
	//use this.board as the board you're running on
	//you'll  basically have to check each square once for white and once for black
	public int territoryHeuristic()
	{
		// array lists holding locations of queens
		ArrayList<Integer> blackQueens = new ArrayList<>();
		ArrayList<Integer> whiteQueens = new ArrayList<>();
		
		int territorySum = 0;
		int counter = 0;

		// find queen locations
		for (int i = 0; i < this.board.length; i++) {
			if(this.board[i] == -1)
				blackQueens.add(i);
			else if (this.board[i] == 1)
				whiteQueens.add(i);
		}
		// loop through each cell and find queen closest to the empty cell
		// also finds how close
		HashMap<Integer, ArrayList<Integer>> moveMap = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 0; i < this.board.length; i++) {
			if (this.board[i] == 0) {
				int whiteDepth = 0;
				int blackDepth = 0;
				
				ArrayList<Integer> first = new ArrayList<Integer>();
				first.add(i);
				moveMap.put(0,  first);
				HashSet<Integer> visited = new HashSet<Integer>();
				int depth = 0;
				int maxDepth = 10;
				while(depth <= maxDepth)
				{
					ArrayList<Integer> possibleMoves = new ArrayList<>(); 
					ArrayList<Integer> moves = moveMap.get(depth);
					for(int move : moves) {
						if(move < 0) continue;
						ArrayList<Integer> foundMoves = findQueen(move);
						int[] temp = new int[2];
						if(foundMoves.size() >= 1)temp[0] = foundMoves.get(foundMoves.size() - 1);
						if(foundMoves.size() >= 2)temp[1] = foundMoves.get(foundMoves.size() - 2);
						//System.out.println(temp[0] + ", " + temp[1]);
						//white queen
						if(IntStream.of(temp).anyMatch(x -> x == -1000) && whiteDepth == 0) whiteDepth = depth + 1;
							
						//black
						if(IntStream.of(temp).anyMatch(x -> x == -1001) && blackDepth == 0) blackDepth = depth + 1;
						if(whiteDepth != 0 && blackDepth != 0) break; //break if found both
						
						for(int newMove : foundMoves)
						{
							if(visited.add(newMove))
							{
								possibleMoves.add(newMove); 	
							}
						}
					}
					
					if(whiteDepth != 0 && blackDepth != 0) break;
					
					moveMap.put(depth + 1, possibleMoves);
					depth ++;
				}
				if(whiteDepth == 0) whiteDepth = maxDepth;
				if(blackDepth == 0) blackDepth = maxDepth;
				moveMap = new HashMap<Integer, ArrayList<Integer>>();
				//System.out.println("whitedepth: " + whiteDepth + ", blacck: " + blackDepth);
				territorySum += relTerritoryEvaluation(whiteDepth, blackDepth);
			}
		}
		//System.out.println("Territory Sum: " + territorySum);
		return territorySum; //0 if it is even
	}
	
	public ArrayList<Integer> findQueen(int queenXY) {
		int[] array = new int [2];
		ArrayList<Integer> tempMoves = new ArrayList<>();
		int xQueen = 0;
		int yQueen = 0;
		int tempIndex = 0;
		boolean foundWhite = false;
		boolean foundBlack = false;
		
		array = indexToYX(queenXY);
		for (int j = 0; j < 8; j++) // 8 possible directions, starts at up, then clockwise
		{
			xQueen = array[1]; //temporary x and y positions. don't want to overwrite initial for next direction 
			yQueen = array[0];
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
					tempMoves.add(tempIndex);
				}
				else if(this.board[tempIndex] == 1) {
					//white queen
					foundWhite = true;
					break;
				}
				else if(this.board[tempIndex] == -1) {
					//white queen
					foundBlack = true;
					break;
				}
				
				else
					break;
			}	
		}
		if(foundWhite) tempMoves.add(-1000);
		if(foundBlack) tempMoves.add(-1001);
		
		return tempMoves;
	}

	// evaluates how many tiles the player can reach before the other player
	private int territoryEvaluation(int n, int m) {
		if (n == 10 && m == 10)
			return 0;
		else if (n == m)
			return 1/5; //this is always going to be 0 (int) 1/5 === 0
		else if (n < m)
			return 1;
		else
			return -1;
	}

	// evaluates based on difference between turns it would take for players
	private int relTerritoryEvaluation(int n, int m) {
		if (m == 10 && n < 10)
			return 10;
		else if (n == 10 && m < 10)
			return -10;
		else if (n == 10 && m == 10) {
			return 0;
		}
		else
			return (m - n);
	}
	
	public void printBoard()
	{
		for(int y = 10; y >= 1; y--)
		{
			for(int x = 1; x <= 10; x++)
			{
				System.out.print(board[GameState.yxToIndex(y, x)] + " ");
			}
			
			System.out.println("");
		}
		
		
		System.out.println("");
		System.out.println("----------------");
	}
	
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		for(int y = 10; y >= 1; y--)
		{
			for(int x = 1; x <= 10; x++)
			{
				output.append(board[GameState.yxToIndex(y, x)] + " ");
			}
			
			output.append("\n");
		}
		output.append("\n");
		output.append("----------------\n");
		
		return output.toString();
	}

	public boolean validateMove() {
		boolean valid = false;
		// run while opponent is playing
		
		return valid;
	}
	
	
	@Override
	public boolean equals(Object other)
	{
		GameState otherState = (GameState) other;
		return Arrays.equals(otherState.board, this.board);
	}
	
	@Override
	//TODO: compare this to using Arrays.hashCode(board);
	public int hashCode()
	{
		return Arrays.hashCode(board);
	}

}
