package ubc.cosc322;

//this class only exists because Java doesn't have Tuples
public class MoveVal 
{
	
	public final Move move;
	public final int val;
	
	public MoveVal(Move move, int val)
	{
		this.move = move;
		this.val = val;
	}

}
