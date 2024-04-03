package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

public class Move 
{
	
	int qCur; //index notation addressing, can convert with GameState.indexToYX();
	int qMove;
	int arrow;
	
	public Move(int qCur, int qMove, int arrow)
	{
		this.qCur = qCur;
		this.qMove = qMove;
		this.arrow = arrow;
	}
	
	public ArrayList<Integer> getQCurCoords()
	{
		ArrayList<Integer> out = new ArrayList<Integer>();
		int[] move = GameState.indexToYX(qCur);
		out.add(move[0]);
		out.add(move[1]);
		
		return out;
	}
	
	public ArrayList<Integer> getQMoveCoords()
	{
		ArrayList<Integer> out = new ArrayList<Integer>();
		int[] move = GameState.indexToYX(qMove);
		out.add(move[0]);
		out.add(move[1]);
		
		return out;
	}
	
	public ArrayList<Integer> getArrowCoords()
	{
		ArrayList<Integer> out = new ArrayList<Integer>();
		int[] move = GameState.indexToYX(arrow);
		out.add(move[0]);
		out.add(move[1]);
		
		return out;
	}
	
	@Override
	public boolean equals(Object other)
	{
		Move o = (Move) other;
		return this.qCur == o.qCur && this.qMove == o.qMove && this.arrow == o.arrow;
	}
	
	@Override
	public String toString()
	{
		return "Qcur: " + Arrays.toString(GameState.indexToYX(this.qCur)) + ", " + "Qmove: " + Arrays.toString(GameState.indexToYX(this.qMove)) + ", " + "Arrow: " + Arrays.toString(GameState.indexToYX(this.arrow));
	}

}
