package ubc.cosc322;

import java.util.HashSet;

public class GameNode 
{
	
	GameState state;
	HashSet<GameNode> children;
	
	//this creates a root node
	public GameNode()
	{
		GameState begin = new GameState();
		this.state = begin;
		this.children = new HashSet<GameNode>();
	}
	
	public GameNode(GameState state, GameNode parent)
	{
		this.state = state;
		children = new HashSet<GameNode>();
		parent.children.add(this);
	}
	
	@Override
	public boolean equals(Object other)
	{
		GameNode o = (GameNode) other; //cast
		return o.state.equals(this.state); // pass down to the state, just compare the board itself
	}
	
	@Override
	public int hashCode()
	{
		return state.hashCode(); // again pass this down to the state, ignore the parents and children for equality purposes
	}

}
