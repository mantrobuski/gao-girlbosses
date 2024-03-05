package ubc.cosc322;

import java.util.HashSet;

public class GameNode 
{
	
	GameState state;
	HashSet<GameNode> children;
	HashSet<GameNode> parents;
	
	public GameNode(GameState state, GameNode parent)
	{
		this.state = state;
		children = new HashSet<GameNode>();
		parents = new HashSet<GameNode>();
		parents.add(parent); //there may be more parents, but there is always at least one
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
