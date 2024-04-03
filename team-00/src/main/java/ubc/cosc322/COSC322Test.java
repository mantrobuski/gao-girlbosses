
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
    	
    	/*
    	int[] board = new int[100];
    	board[9] = -1;
    	board[88] = -1;
    	board[12] = -1;
    	board[62] = -1;
    	
    	board[17] = 1;
    	board[21] = 1;
    	board[75] = 1;
    	board[72] = 1;
    	
    	board[40] = -99;
    	board[51] = -99;
    	board[71] = -99;
    	board[81] = -99;
    	board[2] = -99;
    	board[32] = -99;
    	board[42] = -99;
    	board[52] = -99;
    	board[13] = -99;
    	board[23] = -99;
    	board[33] = -99;
    	board[63] = -99;
    	board[4] = -99;
    	board[24] = -99;
    	board[54] = -99;
    	board[64] = -99;
    	board[74] = -99;
    	board[84] = -99;
    	board[94] = -99;
    	board[15] = -99;
    	board[55] = -99;
    	board[16] = -99;
    	board[26] = -99;
    	board[36] = -99;
    	board[46] = -99;
    	board[56] = -99;
    	board[76] = -99;
    	board[47] = -99;
    	board[57] = -99;
    	board[67] = -99;
    	board[77] = -99;
    	board[97] = -99;
    	board[38] = -99;
    	board[19] = -99;
    	board[59] = -99;
    	
    	GameState test = new GameState(board, true);
    	
    	try 
    	{
	      FileWriter myWriter = new FileWriter("moves.txt");
	      
	      myWriter.write(test.toString());
	      
	      myWriter.write("Territory eval: " + test.territoryHeuristic());
	      myWriter.write("\n");
	      
	      ArrayList<Move> moves = test.getMoves();
	      for(Move move : moves)
	      {
	    	  myWriter.write(move.toString());
	    	  myWriter.write("\n");
	      }
	    		  

	      myWriter.close();
	      System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) 
	    {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
    	
    	GameNode root = new GameNode();
    	GameTree testTree = new GameTree(root);
    	testTree.setColour(false);
    	
    	testTree.iterativeDeepeningAlphaBeta(1);
    	*/
    	
    	
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

		//possibleOpponentMoves = this.tree.getRoot().state.getMoves();
		//if(possibleOpponentMoves != null) System.out.println("Possible Opponent Moves: " + possibleOpponentMoves.size());
		
		//STALL AND RUN EVEN MORE PLAYOUTS HERE RIGHT UP TO 28 SECONDS BEFORE SENDING MOVE
		
		gameClient.sendMoveMessage(move.getQCurCoords(), move.getQMoveCoords(), move.getArrowCoords());
		this.getGameGUI().updateGameState(move.getQCurCoords(), move.getQMoveCoords(), move.getArrowCoords());
		
	}
	
	public void opponentTurn(Move move) throws Exception
	{
		//check if move is in global array
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
	
	/*
	public void benchmark()
	{
		try 
    	{
	      FileWriter myWriter = new FileWriter("bench.txt");
	      
	      long start = System.currentTimeMillis();
	      int playouts = 2000;
	      
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
	*/

 
}//end of class
