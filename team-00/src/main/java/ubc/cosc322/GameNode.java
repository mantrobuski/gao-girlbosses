package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameNode 
{
	
	GameState state;
	HashSet<GameNode> children;
	
	HashMap<GameNode, Move> route; //this is a handy index that contains the move that was required to get from a nodes parent to it 
	
	//this creates a root node
	public GameNode()
	{
		GameState begin = new GameState();
		this.state = begin;
		this.children = new HashSet<GameNode>();
		this.route = null;
	}
	
	public GameNode(GameState state, Move move)
	{
		this.state = state;
		children = new HashSet<GameNode>();
	}
	
	//this adds a parent and the move it takes to get from parent to here to the map
	public void addRoute(GameNode parent, Move move)
	{
		route.put(parent, move);
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
