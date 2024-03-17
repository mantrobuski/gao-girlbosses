package ubc.cosc322;

import java.util.Stack;

public class IterativeDeepening {
	public Stack<Integer> s;
	public boolean reachedGoal=false;
	public int numNodes;
	public int depth;
	public int depthMax;
	
	public IterativeDeepening() {
		s= new Stack<Integer>();
	}
	
	/*
	 * @param m[][]: adjacency matrix
	 */
	public void iterativeDeepening(int m[][],int destination) {
		numNodes= m[1].length-1;
		
		
		while(!reachedGoal) {
			depthLimitedSearch(m,1,destination);
			depthMax++;
			}
		System.out.println(""+depth);
		
		}
	
	public void depthLimitedSearch(int[][]m, int src, int g) {
		int e, destination=1;
		
		int [] nodesVisited= new int[numNodes+1];
		s.push(src);
		depth=0;
		
		while(!s.isEmpty()) {
			e= s.peek();
			while(destination<= numNodes) {
				if(depth<depthMax) {
					if(m[e][destination]==1) {
						s.push(destination);
						nodesVisited[destination]=1;
						depth++;
						
						if(g==destination) {
							reachedGoal=true;
							return;
						}
						e= destination;
						destination=1;
						continue;
					}
				}else {
					break;
				}destination++;
			}
			destination=s.pop()+1;
			depth--;
		}
		
	
		
	}
	
	
}
