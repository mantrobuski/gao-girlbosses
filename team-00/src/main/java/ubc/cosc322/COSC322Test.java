
package ubc.cosc322;

import java.io.FileWriter;
import java.io.IOException;
//import java.io.FileWriter;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    	
    	//benchmark();
    	
    	//run as many playouts right now as we can get away with
    	int initialPlayouts = 2000;
    	this.tree.runPlayouts(root, initialPlayouts);
    	
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {
    	
    	List<Room> rooms = gameClient.getRoomList();
    	
    	gameClient.joinRoom("Kalamalka Lake");
    	
    	
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
    		System.out.println("recieved opponent move");
    		this.getGameGUI().updateGameState(msgDetails);
    		ArrayList<Integer> qCur = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
    		ArrayList<Integer> qMove = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
    		ArrayList<Integer> arrow = (ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.ARROW_POS);
    		
    		Move opMove = new Move(GameState.yxToIndex(qCur.get(0), qCur.get(1)), GameState.yxToIndex(qMove.get(0), qMove.get(1)), GameState.yxToIndex(arrow.get(0), arrow.get(1)));
    		opponentTurn(opMove);
    		takeTurn();
    	}
    	
    	else if(messageType.equals(GameMessage.GAME_ACTION_MOVE))
    	{
    		this.getGameGUI().updateGameState(msgDetails);
    		System.out.println(msgDetails);
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
		//rn this is a skeleton
		//decideMove(state) //heuristic function makes a decision and returns an object
		//(queen x,y move to x,y  shoot arrow, x,y queenToMove.x, queenMove.x, arrow.x
		
		//MAKE AS MANY PLAYOUTS AS POSSIBLE
		this.tree.runPlayouts(this.tree.getRoot(), 1000);
		Move move = getMove();
		
		//STALL AND RUN EVEN MORE PLAYOUTS HERE RIGHT UP TO 28 SECONDS BEFORE SENDING MOVE
		
		gameClient.sendMoveMessage(move.getQCurCoords(), move.getQMoveCoords(), move.getArrowCoords());
		this.getGameGUI().updateGameState(move.getQCurCoords(), move.getQMoveCoords(), move.getArrowCoords());
		GameState newState = tree.getRoot().state.makeMove(move);
		GameNode newRoot = new GameNode(newState); //this is fake
		this.tree.setRoot(newRoot);
	}
	
	public void opponentTurn(Move move)
	{
		GameState newState = tree.getRoot().state.makeMove(move);
		GameNode newRoot = new GameNode(newState);
		
		this.tree.addNode(newRoot, tree.getRoot(), move);
		this.tree.setRoot(newRoot);
	}
	
	public Move getMove()
	{
		//this function will use heuristics to make a move [<x, y>, <x, y> <x,y>] queen to move, square move to, arrow shoot location
		return tree.selectMove();
	}
	
	//true if we are white, false if we are black
	private void initialize(boolean white) 
	{
		this.tree.setColour(white);
		
		//do more here
	}
	
	public void benchmark()
	{
		try 
    	{
	      FileWriter myWriter = new FileWriter("bench.txt");
	      
	      long start = System.currentTimeMillis();
	      int playouts = 1000;
	      
	      //benchmark here
	      this.tree.runPlayouts(this.tree.getRoot(), playouts);
	      
	      long stop = System.currentTimeMillis();
	      
	      myWriter.write(playouts + " playouts in " +  (int)(stop - start) + "ms");
	    		  

	      myWriter.close();
	      System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) 
	    {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}

 
}//end of class
