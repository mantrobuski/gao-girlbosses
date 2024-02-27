package ubc.cosc322;

import java.util.HashSet;

public class GameNode 
{
	
	GameState state;
	HashSet<GameNode> children;
	
	public GameNode(GameState state)
	{
		children = new HashSet<GameNode>();
	}

}
