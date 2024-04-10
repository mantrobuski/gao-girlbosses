package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameTree 
{
	private GameNode root;
	boolean white; //are we white or black, true for white
	public boolean timerInterrupt = false;
	
	public GameTree(GameNode root)
	{
		this.root = root;
		//nodes = new Hashtable<GameNode, GameNode>(); 
	}
	
	public GameNode getRoot()
	{
		return root;
	}
	
	public void setRoot(GameNode node)
	{
		this.root = node;
	}
	
	//populate all children of a node
	public void popNode(GameNode node)
	{
		ArrayList<Move> moves = node.state.getMoves();
		if(moves == null) return;
		for(Move move : moves)
		{
			GameState newState = node.state.makeMove(move);
			GameNode newNode = new GameNode(newState);
			
			this.addNode(newNode, node, move);
		}
	}
	
	//returns the node that was just added (or the existing one)
	public GameNode addNode(GameNode node, GameNode parent, Move move)
	{
		node.addRoute(parent, move);
		
		parent.children.add(node);
		//nodes.put(node, node);
		
		return node;
		
	}
	
	public void setColour(boolean white)
	{
		this.white = white;
	}	
	
	public Move iterativeDeepeningAlphaBeta(int maxDepth)
	{
		
		Move solution = null;
		
		for (int depth = 1; depth <= maxDepth; depth++) {
			// Initial alpha and beta
			System.out.println("Evaluating depth: " + depth);
			short MAX = 32767;
			short MIN = -32768;
	
			//we want to use maximizing player if we're white, we want the minimizing player if we're black
			MoveVal result = minimax(0, root, white, MIN, MAX, depth);
			
			if (result != null) { 
				solution = result.move; 
				} 
			
			
			// TODO: Elana's Timer
			if (this.timerInterrupt) {
					return result.move;
			 }
			
			}
		
		if(solution == null)
		{
			 System.err.println("NO MOVES IN POSITION (according to minimax)");
			 ArrayList<Move> moves = this.root.state.getMoves();
			 if(moves == null) System.out.println("WE LOSE");
			 else
			 {
				 solution = moves.get(new Random().nextInt(moves.size()));
			 }
		}
		
		
		
		return solution;
	}


	public MoveVal minimax(int depth, GameNode node, 
					   Boolean maximizingPlayer, short alpha,
					   short beta, int maxDepth)
	{

		// Terminating condition
		if (depth > maxDepth)
			return new MoveVal(null, node.state.evaluate()); //there is no move at the leafs
		
		Move bestMove = null;
		short bestVal = (short) (maximizingPlayer ? -32768 : 32767); //set the worst possible best value depending on if max or min player

		// Recurse on children
		if(node.children.isEmpty())
		{
			//children not in tree
			
			//populate them
			ArrayList<Move> moves = node.state.getMoves();
			if(moves != null)
			{
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
			
		}
		
		//if children is STILL empty, this node is a terminal node, evaluate will give +- infinity depending on who wins
		if(node.children.isEmpty()) return new MoveVal(null, node.state.evaluate());
		
		//now children will be populated (or be empty because there are no possible moves, SHOULD only occur at the end of the game)
		
		//System.out.println("Evaluating " + node.children.size() + " children");
		for(GameNode child : node.children)
		{
			Move move = child.route.get(node); //get the move that results in the child from the parent
			int val = minimax(depth + 1, child, !maximizingPlayer, alpha, beta, maxDepth).val;
			
			
			if (maximizingPlayer)
			{
				if(val > bestVal) bestMove = move;
				
	            bestVal = (short) Math.max(val, bestVal);
	            alpha = (short) Math.max(alpha, bestVal);
			}
			else
			{
				if(val < bestVal) bestMove = move;
				
				bestVal = (short) Math.min(val, bestVal); //really it's worst val here
	            beta = (short) Math.min(beta, bestVal);
			}
			
			//bail early if we're out of time
			if (this.timerInterrupt) {
				//System.out.println("INTERUPT, VAL: " + bestVal);
				return new MoveVal(bestMove, bestVal);
			}
			
			// Alpha Beta Pruning
            if (beta <= alpha)
                break;
            
		}
		
		
		return new MoveVal(bestMove, bestVal);
	}
}



