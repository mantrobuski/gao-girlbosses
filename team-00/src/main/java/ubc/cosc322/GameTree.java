package ubc.cosc322;

import java.util.Hashtable;

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
	
	public void addNode(GameNode node, GameNode parent)
	{
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

}
