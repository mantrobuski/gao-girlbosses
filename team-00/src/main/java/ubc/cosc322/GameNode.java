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


	public GameState alphaBeta(GameNode node, int depth, int maxDepth){
    
		// Initial alpha and beta
		int MAX = Integer.MAX_VALUE;
		int MIN = -1 * Integer.MAX_VALUE;
	
		// Returns optimal GameState
	   // output = minimax(depth, node, whiteTurn, values, MIN, MAX);
		GameState output = minimax(0, node, true, MIN, MAX, maxDepth);
	
		return output;
	}
	
	
	public GameState minimax(int depth, GameNode node, 
					   Boolean maximizingPlayer, int alpha,
					   int beta, int maxDepth)
	{
	
		// Terminating condition
		if (depth == maxDepth)
	
			return node.state;
	 
		if (maximizingPlayer)
		{
			int best =  -1 * Integer.MAX_VALUE;
	
			// evaluate children
			for (GameNode child: children)
			{
				GameState newState = minimax(depth + 1, child,
								  false, alpha, beta, maxDepth);
				//int val = newState.getHeuristic(); // TODO: make sure this links properly
				int val = -99;
				best = Math.max(best, val);
				alpha = Math.max(alpha, best);
	 
				// Alpha Beta Pruning
				if (beta <= alpha)
					break;
			}
			return node.state; 
		}
		else
		{
			int best = Integer.MAX_VALUE;
	 
			// evaluate children
			for (GameNode child: children)
			{
				GameState newState = minimax(depth + 1, child,
								  true, alpha, beta, maxDepth);
				//int val = newState.getHeuristic();   // TODO: make sure this links properly
				int val = -99;
				best = Math.min(best, val);
				beta = Math.min(beta, best);
	 
				// Alpha Beta Pruning
				if (beta <= alpha)
					break;
			}
			return node.state;
		}
	}

}
