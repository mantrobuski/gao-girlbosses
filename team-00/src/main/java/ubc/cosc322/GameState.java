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
		
		//to create a move
		Move sample = new Move(0, 1, 2); //these are in index notation
		output.add(sample);
		
		
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
