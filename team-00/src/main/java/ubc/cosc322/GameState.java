package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

public class GameState 
{
	
	int[] board; //1D

	boolean whiteTurn; //true if white to move, false if black to move
	
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
					tempIndex = yxToIndex(yQueen, xQueen);
	
					if (this.board[tempIndex] == 0) { // SLOW fix later
						int[] array = {yxToIndex(queenXY[1], queenXY[0]), tempIndex};
						tempMoves.add(array);
					}
					else
						break;

				}	
			}
		}

		//do same as above but for arrows with all moves in tempMoves
		int xArrow = 0;
		int yArrow = 0;

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
