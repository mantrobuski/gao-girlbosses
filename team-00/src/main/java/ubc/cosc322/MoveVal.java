package ubc.cosc322;

//this class only exists because Java doesn't have Tuples
public class MoveVal 
{
	
	public final Move move;
	public final short val;
	
	public MoveVal(Move move, short val)
	{
		this.move = move;
		this.val = val;
	}

}
