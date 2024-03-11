
package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
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
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	//COSC322Test player = new COSC322Test(args[0], args[1]);
    	HumanPlayer player = new HumanPlayer();
    	
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
    		this.takeTurn(null);
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
	
	public void takeTurn(ArrayList<Integer> state)
	{
		//rn this is a skeleton
		//decideMove(state) //heuristic function makes a decision and returns an object
		//(queen x,y move to x,y  shoot arrow, x,y queenToMove.x, queenMove.x, arrow.x
		
		ArrayList<Integer>[] move = getMove(state);
		
		gameClient.sendMoveMessage(move[0], move[1], move[2]);
	}
	
	public ArrayList<Integer>[] getMove(ArrayList<Integer> state)
	{
		//this function will use heuristics to make a move [<x, y>, <x, y> <x,y>] queen to move, square move to, arrow shoot location
		ArrayList<Integer>[] move = (ArrayList<Integer>[]) new ArrayList[3];
		
		//this is hard coded, this will be computed in the future.
		move[0] = new ArrayList<Integer>();
		move[0].add(0); //x
		move[0].add(6); //y
		
		move[1] = new ArrayList<Integer>();
		move[1].add(5); //x
		move[1].add(1);
		
		move[2] = new ArrayList<Integer>();
		move[2].add(3); //x
		move[2].add(1);
		
		return move;
		
	}

 
}//end of class
