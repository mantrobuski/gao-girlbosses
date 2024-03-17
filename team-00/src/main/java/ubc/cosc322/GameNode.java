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


	public GameState alphaBeta(GameNode node, int depth){
    
		// Initial alpha and beta
		int MAX = 1000;
		int MIN = -1000;   
	
		this.node = node;
	
		// Returns optimal GameState
	   // output = minimax(depth, node, whiteTurn, values, MIN, MAX);
		int output = minimax(0, node, true, MIN, MAX);
	
		return output;
	}
	
	
	public GameState minimax(int depth, int node, 
					   Boolean maximizingPlayer, int alpha,
					   int beta, int maxDepth)
	{
		this.node = node;
	
		// Terminating condition
		if (depth == maxDepth)
	
			return this.node.state;
	 
		if (maximizingPlayer)
		{
			int best = MIN;
	
			// evaluate children
			for (GameNode child: hashSet)
			{
				GameState newState = minimax(depth + 1, child,
								  false, values, alpha, beta);
				int val = newState.getHeuristic(); // TODO: make sure this links properly
				best = Math.max(best, val);
				alpha = Math.max(alpha, best);
	 
				// Alpha Beta Pruning
				if (beta <= alpha)
					break;
			}
			return this.node.state; 
		}
		else
		{
			int best = MAX;
	 
			// evaluate children
			for (GameNode child: hashSet)
			{
				GameState newState = minimax(depth + 1, child,
								  true, values, alpha, beta);
				inct val = newState.getHeuristic();   // TODO: make sure this links properly
				best = Math.min(best, val);
				beta = Math.min(beta, best);
	 
				// Alpha Beta Pruning
				if (beta <= alpha)
					break;
			}
			return this.node.state;
		}
	}

}
