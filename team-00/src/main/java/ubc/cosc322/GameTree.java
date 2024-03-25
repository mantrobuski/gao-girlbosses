package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

public class GameTree 
{
	//this is a table where key == value, this is because you can get from the table by object
	//hashset does not have this property
	//doing this because we have ovverride the equals of GameNode to only look at the state of the board and to ignore the parents and children when comparing because we do it in this class instead
	Hashtable<GameNode, GameNode> nodes;
	private GameNode root;
	
	private boolean white; //are we white or black, true for white
	
	public GameTree(GameNode root)
	{
		this.root = root;
		nodes = new Hashtable<GameNode, GameNode>(); 
	}
	
	public GameNode getRoot()
	{
		return root;
	}
	
	public void setRoot(GameNode node)
	{
		this.root = node;
	}
	
	//returns the node that was just added (or the existing one)
	public GameNode addNode(GameNode node, GameNode parent, Move move)
	{
		node.addRoute(parent, move);
		
		//node is in the table already
		if(nodes.containsKey(node))
		{
			//marks the existing node as a child of the desired parent
			GameNode existingNode = nodes.get(node);
			parent.children.add(existingNode);
			return existingNode;
		}
		
		//we have to do the above juggling so that we don't reference the wrong node object if one already exists
		parent.children.add(node);
		nodes.put(node, node);
		
		return node;
		
	}
	
	public void setColour(boolean white)
	{
		this.white = white;
	}
	
	/*
	public Move selectMove()
	{
		//run UCB on each child
		double bestScore = -1;
		Move bestMove = null;
		
		for(GameNode child : root.children)
		{
			double eval = UCB(child);
			if(eval > bestScore)
			{
				bestScore = eval;
				bestMove = child.route.get(root); //get the move that routes from root -> child
			}
		}
		
		//this code should NEVER RUN, but it's a fallback in case there is a bug somewhere
		if(bestMove == null)
		{
			//if we couldn't select a move, pick one at random
			ArrayList<Move> moves = root.state.getMoves();
			bestMove = moves.get(new Random().nextInt(moves.size()));
		}
		
		return bestMove;
		
	}
	
	//this can probably be optimzed at the cost of accuracy
	public double UCB(GameNode node)
	{
		double whiteWinPercentage = 0.5;
		if(node.playouts != 0)whiteWinPercentage = node.whiteWins / node.playouts;
		
		double xbar = (white ? whiteWinPercentage : 1 - whiteWinPercentage); //flipped odds if we are black
		
		double xbarSquared = xbar * xbar;
		double logRoot = Math.log(root.playouts);
		
		double confidence = (logRoot / node.playouts) * Math.min(0.25, xbar - xbarSquared + Math.sqrt((2 * logRoot) / node.playouts));
		
		return xbar + Math.sqrt(confidence);
	}
	*/
	
	//runs count # of playouts
	public void runPlayouts(GameNode start, int count)
	{
		for(int i = 0; i < count; i++)
		{
			playout(start);
		}
	}
	
	//simulate full game
	public void playout(GameNode start)
	{
		
		GameNode cursor = start;
		ArrayList<Move> moves = cursor.state.getMoves();
		
		//hold the visited moves, this will get updated after the result is determined
		HashSet<GameNode> visited = new HashSet<GameNode>();
		
		//moves is null when the game is over
		while(moves != null)
		{
			visited.add(cursor);
			
			//randomly select a move
			Move move = moves.get(new Random().nextInt(moves.size()));
			
			//get resulting state from move
			GameState newState = cursor.state.makeMove(move);
			
			//create node using the state
			GameNode newNode = new GameNode(newState);
			
			//add it to the tree
			this.addNode(newNode, cursor, move);
			
			//move cursor to this new node
			cursor = newNode;
			
			//get new possible moves
			moves = cursor.state.getMoves();
		}
		
		visited.add(cursor);
		
		//determine result
		//the player whos turn it is loses (at end state)
		boolean result = !cursor.state.whiteTurn; //true for white win
		
		//back propigate
		for(GameNode node : visited)
		{
			node.playouts += 1;
			if(result) node.whiteWins += 1;
			//no need to do this for black, white win % is whiteWins / playouts, black win % is 1 - white win %
		}
		
	}
			
	
	public Move iterativeDeepeningAlphaBeta(int maxDepth)
	{

		Move solution = null;
		
		for (int depth = 1; depth < maxDepth; depth++) {
			// Initial alpha and beta
			System.out.println("Evaluating depth: " + depth);
			int MAX = Integer.MAX_VALUE;
			int MIN = -1 * Integer.MAX_VALUE;
	
			//we want to use maximizing player if we're white, we want the minimizing player if we're black
			MoveVal result = minimax(0, root, white, MIN, MAX, depth);
			
			if (result != null) { 
				solution = result.move; 
				} 
			
			// TODO: Elana's Timer
			// if (timerInterupt) {
			//		break;
			// }
			//
			}
		
		if(solution == null) System.err.println("NO MOVES IN POSITION (according to minimax)");
		
		return solution;
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
