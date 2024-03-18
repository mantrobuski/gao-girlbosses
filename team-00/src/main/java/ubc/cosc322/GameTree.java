package ubc.cosc322;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class GameTree 
{
	//this is a table where key == value, this is because you can get from the table by object
	//hashset does not have this property
	//doing this because we have ovverride the equals of GameNode to only look at the state of the board and to ignore the parents and children when comparing because we do it in this class instead
	private Hashtable<GameNode, GameNode> nodes;
	private GameNode root;
	
	public GameTree(GameNode root)
	{
		this.root = root;
		nodes = new Hashtable<GameNode, GameNode>(); 
	}
	
	public GameNode getRoot()
	{
		return root;
	}
	
	public void addNode(GameNode node, GameNode parent, Move move)
	{
		node.addRoute(parent, move);
		
		//node is in the table already
		if(nodes.containsKey(node))
		{
			//marks the existing node as a child of the desired parent
			GameNode existingNode = nodes.get(node);
			parent.children.add(existingNode);
			return;
		}
		
		//we have to do the above juggling so that we don't reference the wrong node object if one already exists
		parent.children.add(node);
		nodes.put(node, node);
		
	}
	
	public Move alphaBeta(GameNode node, int maxDepth)
	{

		// Initial alpha and beta
		int MAX = Integer.MAX_VALUE;
		int MIN = -1 * Integer.MAX_VALUE;

		// Returns optimal GameState
	   // output = minimax(depth, node, whiteTurn, values, MIN, MAX);
		MoveVal solution = minimax(0, node, true, MIN, MAX, maxDepth);
		
		if(solution.move == null) System.err.println("NO MOVES IN POSITION (according to minimax)");
		
		return solution.move;
	}


	public MoveVal minimax(int depth, GameNode node, 
					   Boolean maximizingPlayer, int alpha,
					   int beta, int maxDepth)
	{

		// Terminating condition
		if (depth > maxDepth)
			return new MoveVal(null, node.state.evaluate()); //there is no move at the leafs
		
		Move bestMove = null;
		int bestVal = (maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE); //set the worst possible best value depending on if max or min player

		// Recurse on children
		if(node.children.isEmpty())
		{
			//children not in tree
			
			//populate them
			ArrayList<Move> moves = node.state.getMoves();
			for(Move move : moves)
			{
				//get resulting state from move
				GameState result = node.state.makeMove(move);
				
				//create node using the state
				GameNode newNode = new GameNode(result);
				
				//add it to the tree
				this.addNode(newNode, node, move);
			}
		}
		
		//if children is STILL empty, this node is a terminal node, evaluate will give +- infinity depending on who wins
		if(node.children.isEmpty()) return new MoveVal(null, node.state.evaluate());
		
		//now children will be populated (or be empty because there are no possible moves, SHOULD only occur at the end of the game)
		
		for(GameNode child : node.children)
		{
			Move move = child.route.get(node); //get the move that results in the child from the parent
			int val = minimax(depth + 1, child, !maximizingPlayer, alpha, beta, maxDepth).val;
			
			
			if (maximizingPlayer)
			{
				if(val > bestVal) bestMove = move;
				
	            bestVal = Math.max(val, bestVal);
	            alpha = Math.max(alpha, bestVal);
			}
			else
			{
				if(val < bestVal) bestMove = move;
				
				bestVal = Math.min(val, bestVal); //really it's worst val here
	            beta = Math.min(beta, bestVal);
			}
			
			// Alpha Beta Pruning
            if (beta <= alpha)
                break;
            
		}
		
		
		return new MoveVal(bestMove, bestVal);
	}

}
