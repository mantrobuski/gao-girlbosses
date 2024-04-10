
package ubc.cosc322;

import java.io.FileWriter;
import java.io.IOException;
//import java.io.FileWriter;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
    
    private GameTree tree;
    
    private int turnCount = 0;
    private int depth = 1;
	//private ArrayList<Move> possibleOpponentMoves = new ArrayList<Move>();
 
    private SearchTimer Timer;
    

    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {	
    	
    	COSC322Test player = new COSC322Test(args[0], args[1]);
    	//HumanPlayer player = new HumanPlayer();
    	
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd 
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	
    	GameNode root = new GameNode();
    	this.tree = new GameTree(root);
    	this.Timer = new SearchTimer(this.tree);
    	
    	this.tree.popNode(root);
    	for(GameNode node : root.children)
    	{
    		this.tree.popNode(node);
    	}
    	
    	System.out.println("Setup complete");
    	
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {
    	
    	List<Room> rooms = gameClient.getRoomList();
    	
    	gameClient.joinRoom("Echo Lake");
    	
    	
    	userName = gameClient.getUserName();
    	if(gamegui != null) 
    	{
    		gamegui.setRoomInformation(rooms);
    	}
  
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	
    	System.out.println("type: " + messageType);
    	
    	if(messageType.equals(GameMessage.GAME_STATE_BOARD))
    	{
    		this.getGameGUI().setGameState((ArrayList<Integer>) msgDetails.get("game-state"));
    	}
    	
    	else if(messageType.equals(GameMessage.GAME_ACTION_START))
    	{
    		//this.getGameGUI().setGameState((ArrayList<Integer>) msgDetails.get("game-state"));
    		System.out.println("Black: " +  (String)msgDetails.get("player-black") + " vs WHITE: " + (String)msgDetails.get("player-white"));
    		boolean white = ((String) msgDetails.get("player-white")).equals(this.userName);
    		
    		this.initialize(white); //pass what colour we are.
    		
    		//if we are black we go first
    		if(!white)
    		{
    			//go
    			takeTurn();
    		}
    		
    		
    	}
    	
    	else if(messageType.equals(GameMessage.GAME_ACTION_MOVE))
    	{
    		System.out.println("recieved opponent move");
    		this.getGameGUI().updateGameState(msgDetails);
    		ArrayList<Integer> qCur = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
    		ArrayList<Integer> qMove = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
    		ArrayList<Integer> arrow = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.ARROW_POS);
    		
    		Move opMove = new Move(GameState.yxToIndex(qCur.get(0), qCur.get(1)), GameState.yxToIndex(qMove.get(0), qMove.get(1)), GameState.yxToIndex(arrow.get(0), arrow.get(1)));
    		try {
    			this.tree.popNode(this.tree.getRoot()); //make sure the roots children are populated
				opponentTurn(opMove);
			} catch (Exception e) {
				// this happens if they submit an invalid move
				System.err.println(e);
				this.tree.getRoot().state.printBoard();
				System.out.println(opMove.toString());
			}
    		takeTurn();
    	}
    	
    	else
    	{
    		System.out.println("UNHANDLED KEY: " + messageType);
    	}
    	
    	/*
    	for (Map.Entry<String,Object> entry : msgDetails.entrySet())
    	{
    		System.out.println("Key: " + entry.getKey());
    		if(entry.getKey() == "game-state")
        	{
        		System.out.println("setting game state");
        		this.getGameGUI().setGameState((ArrayList<Integer>) entry.getValue());
        	}
    		else if(entry.getKey() == "")
    		{
    			System.out.println("updating game state");
    			this.getGameGUI().updateGameState(null, null, null);
    		}
    		else
    		{
    			System.out.println("UNHANDLED KEY: " + entry.getKey());
    		}
    	}
    	*/
    	
    	
    	
    	    	
    	return true;   	
    }
    
    @Override
    public boolean handleMessage(String msg)
    {
    	System.out.println("Generic msg: " + msg);
    	return true;
    }
    
    
    @Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
		return this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}
	
	public void takeTurn()
	{
		turnCount++;

		Timer.startSearching();
		//decideMove(state) //heuristic function makes a decision and returns an object
		//(queen x,y move to x,y  shoot arrow, x,y queenToMove.x, queenMove.x, arrow.x
		
		long startTime = System.currentTimeMillis();
		
		Move move = getMove();
		Timer.timerCancel();

		long endTime = System.currentTimeMillis();
		System.out.println("Got move in " + (endTime - startTime) + "ms");
		
		//we  make a move in under 7s, add to the depth (and we weren't interuppted)
	
		if(endTime - startTime < 7000 && turnCount > 5 && !this.tree.timerInterrupt)
		{
			depth ++;
		}

		GameState newState = tree.getRoot().state.makeMove(move);
		GameNode newRoot = new GameNode(newState); //this is fake
		//this.tree.nodes.remove(this.tree.getRoot());
		this.tree.setRoot(newRoot);

		gameClient.sendMoveMessage(move.getQCurCoords(), move.getQMoveCoords(), move.getArrowCoords());
		this.getGameGUI().updateGameState(move.getQCurCoords(), move.getQMoveCoords(), move.getArrowCoords());
		
	}
	
	public void opponentTurn(Move move) throws Exception
	{
		//for some reason contains behaves weirdly, even though equals is override
		//if the root has children
		boolean valid = false;
		if(this.tree.getRoot().children != null && !this.tree.getRoot().children.isEmpty())
		{
			for(GameNode child : this.tree.getRoot().children)
			{
				//see if the route exists from current root to an exisiting child through the move that is passed
				if(child.route.get(this.tree.getRoot()).equals(move)) valid = true;
			}
		}
		else
		{
			throw new Exception("NO VALID MOVES, OPPONENT SHOULD LOSE");
		}
		
		if(!valid) throw new Exception("Opponent submitted invalid move");
		
		GameState newState = tree.getRoot().state.makeMove(move);
		GameNode newRoot = new GameNode(newState);
		//this.tree.nodes.remove(this.tree.getRoot());
		this.tree.addNode(newRoot, tree.getRoot(), move);
		this.tree.setRoot(newRoot);
	}
	
	public Move getMove()
	{
		//random move first turn
		if(this.tree.white && this.turnCount == 1)
		{
			ArrayList<Move> moves = this.tree.getRoot().state.getMoves();
			 if(moves == null) System.out.println("WE LOSE");
			 else
			 {
				 return moves.get(new Random().nextInt(moves.size()));
			 }
		}
		
		//this function will use heuristics to make a move [<x, y>, <x, y> <x,y>] queen to move, square move to, arrow shoot location
		return tree.iterativeDeepeningAlphaBeta(depth);
	}
	
	//true if we are white, false if we are black
	private void initialize(boolean white) 
	{
		this.tree.setColour(white);
		
		//do more here
	}
	
 
}//end of class
