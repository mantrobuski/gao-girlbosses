package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	
	private int territoryHeuristic()
	{
		//we are going to count the total number of tiles each queen sees, black counts for negative
		int territory = 0;
		
		//create two array lists containing the location of each queen (index notation)
		ArrayList<Integer> whiteQueens = new ArrayList<Integer>();
		ArrayList<Integer> blackQueens = new ArrayList<Integer>();
		for(int i = 0; i < this.board.length; i++)
		{
			//add the index of the queens we care about
			if(board[i] == 1) whiteQueens.add(i);
			if(board[i] == -1) blackQueens.add(i);
		}
		
		//for each white queen
		for(int queen : whiteQueens)
		{
			//check 4 directions
			
			//up
			int cursor = queen;
			cursor++;
			do
			{
				if(cursor > 99 || cursor < 0) break;
				else if(board[cursor] == 0)
				{
					territory += 1;
				}
				else break;
				cursor++;
			}while(cursor % 10 != 0);
			
			//down
			cursor = queen;
			cursor--;
			do
			{
				if(cursor > 99 || cursor < 0) break;
				else if(board[cursor] == 0)
				{
					territory += 1;
				}
				else break;
				cursor--;
			}while(cursor % 10 != 9);
			
			//right
			cursor = queen;
			cursor += 10;
			do
			{
				if(cursor > 99 || cursor < 0) break;
				else if(board[cursor] == 0)
				{
					territory += 1;
				}
				else break;
				cursor += 10;
			}while(cursor >  9);
			
			//left
			cursor = queen;
			cursor -= 10;
			do
			{
				if(cursor > 99 || cursor < 0) break;
				else if(board[cursor] == 0)
				{
					territory += 1;
				}
				else break;
				cursor -= 10;
			}while(cursor < 90);
		}
		
		//for each black queen
		for(int queen : blackQueens)
		{
			//check 4 directions
			
			//up
			int cursor = queen;
			cursor++;
			do
			{
				if(cursor > 99 || cursor < 0) break;
				else if(board[cursor] == 0)
				{
					territory -= 1;
				}
				else break;
				cursor++;
			}while(cursor % 10 != 0);
			
			//down
			cursor = queen;
			cursor--;
			do
			{
				if(cursor > 99 || cursor < 0) break;
				else if(board[cursor] == 0)
				{
					territory -= 1;
				}
				else break;
				cursor--;
			}while(cursor % 10 != 9);
			
			//right
			cursor = queen;
			cursor += 10;
			do
			{
				if(cursor > 99 || cursor < 0) break;
				else if(board[cursor] == 0)
				{
					territory -= 1;
				}
				else break;
				cursor += 10;
			}while(cursor >  9);
			
			//left
			cursor = queen;
			cursor -= 10;
			do
			{
				if(cursor > 99 || cursor < 0) break;
				else if(board[cursor] == 0)
				{
					territory -= 1;
				}
				else break;
				cursor -= 10;
			}while(cursor < 90);
		}
		
		
		return territory;
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
