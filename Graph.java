

public class Graph {
	
	boolean[][] adjacency;
	int nbNodes;
	
	public Graph (int nb){
		this.nbNodes = nb;
		this.adjacency = new boolean [nb][nb];
		for (int i = 0; i < nb; i++){
			for (int j = 0; j < nb; j++){
				this.adjacency[i][j] = false;
			}
		}
	}
	
	public void addEdge (int i, int j){
		this.adjacency[i][j] = true;
		this.adjacency[j][i] = true;
		// since the graph is undirected, adjacency[i][j] and adjacency[j][i] are
		// the exact same edge in the graph so must update both 
	}
	
	public void removeEdge (int i, int j){
		this.adjacency[i][j] = false;
		this.adjacency[j][i] = false;
		// since the graph is undirected, adjacency[i][j] and adjacency[j][i] are
		// the exact same edge in the graph so must update both 
		
	}
	
	public int nbEdges(){
		
		// 1)  since the graph is undirected, the matrix is symmetric and we  
		// only need to count [i][j] and [j][i] as one
		// we can do the upper triangle above the main diagonal or the lower triangle
		// 2) since the graph may contain self loops, the main diagonal may
		// have true within it so we must account for this too
		int counter = 0;
		// 1) Upper Triangle
		for (int i=1; i <nbNodes; i++) {
			for (int j=0; j<i; j++) {
				if (this.adjacency[i][j] ==true) {
					counter++;
				}
			}
		}
		//2) 
		for (int i=0; i<nbNodes; i++) {
			if (this.adjacency[i][i]==true) {
				counter++;
			}
		}
		
		return counter;
	
	public boolean cycle(int start){
		int[] nodesVisited = new int[this.nbNodes * this.nbNodes];
		for (int i=0; i<nodesVisited.length; i++) {
			nodesVisited[i] = -1;
		}
		boolean[] visited = new boolean[nbNodes];
		
		
		// now we want to check the neighbours of the node start
		this.dfs(visited, -1, start, nodesVisited);
		
		for (int i=0; i<nodesVisited.length; i++) {
			if (nodesVisited[i] == start) {
				return true; // there is a cycle
			}		
		}
		// there is no cycle
		return false; 
	}
	
	// helper method for dfs
	public int[] dfs(boolean[] visited, int u, int start, int[] nodesVisited) {
		visited[start] = true; // visit the start node so set it to true
		
		int[] neighbours = this.neighbour(start);
		
		for (int w = 0; w<neighbours.length; w++) {
			if (visited[neighbours[w]] == false) {
				this.dfs(visited, start, neighbours[w], nodesVisited);
			}
			// check for cycle (but disregard reverse of edge leading to v)
            else if (neighbours[w] != u && neighbours[w] != start) {
            	this.addElement(nodesVisited, neighbours[w]);
            	this.addElement(nodesVisited, start);
            }
       
		}
		return nodesVisited;
		
	}
	
	// helper method: adds node to array to keep track of the nodes we have visited
	public void addElement(int[] nodesVisited, int toAdd) {
		for(int i=0; i<nodesVisited.length; i++) {
			if(nodesVisited[i]==-1) {
				nodesVisited[i] = toAdd;
				break;
			}
			
		}
		
	}
	// helper method to find number of neighbours a node has
	public int numNeighbours(int node) {
		int num = 0;
		for(int i=0; i<this.nbNodes; i++) {
			if(this.adjacency[node][i]==true) {
				num++;
			}
		}
		return num;
	}
	
	// helper method returns an array with neighbours of node
	public int[] neighbour(int node) {
		int num = this.numNeighbours(node);
		int[] a = new int[num];
				
		int j = 0;
		while (j<a.length) {
			for (int i=0; i <this.nbNodes; i++) {
				if (this.adjacency[node][i] == true) {
					a[j] = i;
					j++;
				}
			}
		}
		return a;
	}
	
	public int shortestPath(int start, int end){
		if (start == end) { // get from node to same node
			// case if is a self loop
			if (this.adjacency[start][end]) { 
				return 1;
				// case when there is no direct edge between the nodes start and end  
				// but we have a cycle at the node start
			} else if (!this.adjacency[start][end] && this.cycle(start)){ 
				int path = this.shortestCycle(start, end); 
				//finding length of this cycle
				if(path==2) {
					path++;
				}
				return path;
			} else { //i.e no self loop exists
				return nbNodes + 1; 
			}
		}
		//if start and end are adjacent or there is a self-loop
		if(adjacency[start][end]==true) {// edge connecting the two nodes
			return 1;
		}
		
	
		boolean[] visited = new boolean[this.nbNodes];
		int[] q = new int[this.nbNodes];
		int[] dQ = new int[this.nbNodes];
		for(int i=0; i<q.length; i++) {
			q[i]=-1; // set values of array to be -1
			dQ[i]=-1;
		}
		
		int[][] nodeParent = new int[nbNodes][2];
		for(int i=0; i<nodeParent.length; i++) {
			for(int j=0; j<nodeParent[i].length; j++) {
				nodeParent[i][j]=-1;
			}
		}

		bfs(start,end,visited, q, dQ, nodeParent);
		
		int endNodeIndex=-1; //set the index of the end node to -1
		//find the end node. We begin at 1 because 0 is the start node
		for(int i=1; i<dQ.length; i++) { 
			if(dQ[i]==end) {
				endNodeIndex=i; //found end node and now update
			}
		}
		if(endNodeIndex == -1) {//the end node was not updated
			return this.nbNodes+1; 
			//there was no path from node start to end
		}
		//shortestP = shortest Path
		int shortestP=0; // call backtrack method to find shortestpath
		shortestP = this.backtrack(nodeParent, shortestP, endNodeIndex);
		
		return shortestP;
	}
	
	//Helper method: check if 2 dimensional array is empty i.e value -1
	//Used in descendingCyclePath and ascendingCyclePath helper methods for shortestPath
	public boolean isEmpty(int[][] a) { // check if node is empty
		for (int i = 0; i < a.length; i++) {
			if (a[i][1] != -1) {
				return false;
			}
		}
		return true; 
	}
	
	//Find the descending path that is a cycle
	public int[][] descendingCyclePath(int start, int end){
		// store all the nodes we have popped
		int[][] pop = new int[nbEdges() * nbEdges()][2]; 
		for (int i = 0; i < pop.length; i++) {
			for (int j = 0; j < pop[i].length; j++) {
				pop[i][j] = -1; 
			}
		}
		// using 2D array to create stack
		// since we know that: each node only appears in the stack once, 
		// the max size must be nbNodes
		int[][] stack = new int[2 * nbEdges()][2]; 
		for (int i = 0; i < stack.length; i++) {
			for (int j = 0; j < stack[i].length; j++) {
				stack[i][j] = -1; 
			}
		}
		boolean[] visited = new boolean[nbNodes]; 
		// a self-loop has path 1; parent of starting node is not important
		stack[0][0] = -1;
		stack[0][1] = start; 
		// i = index of popped array; j = index of stack 
		int i = 0; 
		int j = 0; 
	
		int counter = 0; 
		while (!this.isEmpty(stack)) {
			int parent = stack[j][0];
			int thisNode = stack[j][1]; 
			if (thisNode == end) {
				counter++;
				// pop it
				if (counter == 2) {
					stack[j][0] = -1;	
					stack[j][1] = -1;
					pop[i][0] = parent;
					pop[i][1] = thisNode;  	
					return pop; 
				}
			} 
			// Case where have not reach the end node yet
			if (!visited[thisNode]){
				// pop it
				stack[j][0] = -1;
				stack[j][1] = -1;
				pop[i][0] = parent;
				pop[i][1] = thisNode; 
				i++; 
				visited[thisNode] = true; // visit the node
				// push all neighbours onto stack
				//start iterating from the biggest node to find the neighbours
				for (int k=nbNodes-1; k>=0 ; k--) {
					if (this.adjacency[thisNode][k] == true) {
						if(k!=parent) {
							stack[j][0] = thisNode;
							stack[j][1] = k; 
							j++;  
						} 
					}
				}
			} else {
				stack[j][0] = -1;
				stack[j][1] = -1;
				pop[i][0] = parent;
				pop[i][1] = thisNode; 
				i++; 
			}
			j--; 
		}	
		return pop;	
	}
	
	//helper method: finds the ascending path that is a cycle
	public int[][] ascendingCyclePath(int start, int end){
		int[][] pop = new int[nbEdges() * nbEdges()][2]; 
		for (int i = 0; i < pop.length; i++) {
			for (int j = 0; j < pop[i].length; j++) {
				pop[i][j] = -1; 
			}
		}
		// use 2D array as a Stack
		int[][] stack = new int[2 * nbEdges()][2]; 
		for (int i = 0; i < stack.length; i++) {
			for (int j = 0; j < stack[i].length; j++) {
				stack[i][j] = -1; 
			}
		}
		boolean[] visited = new boolean[nbNodes]; 
		stack[0][0] = -1;
		stack[0][1] = start; 
		// i represents index of popped array; j represents index of stack 
		int i = 0; 
		int j = 0; 
		int counter = 0; 
		while (!this.isEmpty(stack)) {
			int parent = stack[j][0];
			int thisNode = stack[j][1]; 
			if (thisNode == end) {
				counter++;
				// pop
				if (counter == 2) {
					stack[j][0] = -1;	
					stack[j][1] = -1;
					pop[i][0] = parent;
					pop[i][1] = thisNode;  
					return pop; 
				}
			} 
			// Case where have not reached the end node
			if (!visited[thisNode]){
				// pop the current node from stack
				stack[j][0] = -1;
				stack[j][1] = -1;
				pop[i][0] = parent;
				pop[i][1] = thisNode; 
				i++; 
				// visit the node
				visited[thisNode] = true; 
				// put all neighbours onto; start iterating from lowest node to fin
				// the neighbours
				for (int k = 0; k < nbNodes; k++) {
					if (this.adjacency[thisNode][k] == true) {
						if(k!=parent) {
							stack[j][0] = thisNode;
							stack[j][1] = k; 
							j++;  
						} 
					}
				}
			} else {
				stack[j][0] = -1;
				stack[j][1] = -1;
				pop[i][0] = parent;
				pop[i][1] = thisNode; 
				i++; 
			}
			j--; 
		}	
		return pop; 
	}
	//Finds the shortest path that is a cycle
	public int shortestCycle(int start, int end) {
		int[][] p1 = this.ascendingCyclePath(start, end);
		int[][] p2 =  this.descendingCyclePath(start,end);
		
		//initialize the length of pathA and the index
		int length1 = 0; 
		int position1 = -1; 
		for (int i = p1.length - 1; i >= 0; i--) {
			if (p1[i][1] == end) {
				position1 = i; 
				break; 
			}
		} 
		if (position1 != -1) {
			// to find length of the path we backtrack from end back to beginning 
			int thisNode = p1[position1][0]; 
			for (int i = position1; i >= 0; i--) {
				if (p1[i][1] == thisNode) {
					length1++; 
					thisNode = p1[i][0];
				}
			}
		} 
		int length2 = 0; 
		int position2 = -1; 
		for (int i = p2.length - 1; i >= 0; i--) {
			if (p2[i][1] == end) {
				position2 = i; 
				break; 
			}
		} 
		if (position2 != -1) {
			int thisNode = p2[position2][0]; 
			for (int i = position2; i >= 0; i--) {
				if (p2[i][1] == thisNode) {
					length2++; 
					thisNode = p2[i][0];
				}
			}
		}
		if(position1==-1 && position2==-1) { //found no cycle
			return this.nbNodes+1;
		}
		if(length1>length2) {
			return length2; //return the shorter length path
		} 
		return length1; 
	}
	
	//Helper method for the shortestPath method
	/*Input: start and end nodes, 
	 * boolean array to keep track of the nodes that have been visited
	int arrays to store the queued and dequeued elements, 
	and a 2D array to store the visited nodes and its corresponding parents*/
	public int[][] bfs(int start, int end, boolean[] visited, int[] q, int[] dQ, int[][] nodeParent) {
		visited[start] = true;
		
		//enqueue start
		this.enqueue(q,start);
		while(!qIsEmpty(q)) { //check if queue is empty before preceding to dequeue
			int dequeued = this.updateDequeue(q,dQ);
			this.nodeWithParentArray(start,nodeParent,dQ, dequeued);//updating the nodeAndParent array
			int[] neighbours = this.neighbour(dequeued); //array to store the neighbours of the dequeued node
			for(int i=0; i<neighbours.length; i++) {
				
				if(!visited[neighbours[i]]) { //checking if the neighbours have been visited
					visited[neighbours[i]]=true; //if not, set them as visited
					this.enqueue(q, neighbours[i]); //enqueue the neighbours
					
				}
			}
		}
		//returning the complete 2D array of the visited nodes and their corresponding parent
		return nodeParent;
	}
	
	//A helper method for the shortestPath method
		//backtracks from the index of the end node the index of its parent 
		//Updating the shortestPath variable for each backtrack step
	public int backtrack(int[][] nodeParent, int shortestPath, int a) {
		for(int i=a; i>-1; i--) {
			if (nodeParent[i][1] == nodeParent[a][0]) {
				shortestPath++;
				a=i;
			}
		}
		return shortestPath;
	}
	
	//A helper method for the bfs helper method
	//Stores the visited nodes in a 2D array(the first element of each subarray is the node's parent, and the second element is the node)
	public int[][] nodeWithParentArray(int start, int[][] nodeParent, int[] dQ, int node) {
		
		if(node==start) { //initializing the first subarray with -1 as the parent and start as the node
			nodeParent[0][0]=-1;
			nodeParent[0][1]=start;
			return nodeParent;
		}
		//Finding the first parent, that has already been visited, of a particular node
		//storing this parent node into a the variable
		int parent= -1;
		for(int k=0; k<dQ.length; k++) {
			if(dQ[k]!=-1) {
				if(this.adjacency[dQ[k]][node]) {
				parent=dQ[k];
					break;
				}
			}
		}
		//Finding an available subarray(i.e. first one with the node still initialized at -1) to store the node and its parent
		for(int i=0; i<nodeParent.length; i++) {
			if (nodeParent[i][1]==-1) {
				nodeParent[i][0]= parent;
				nodeParent[i][1]= node;
				break;
			
			}
		}
		return nodeParent;
	}
	
	//A helper method for the bfs helper method
	//Adding a node to the front of the queue array by finding the first spot(starting from the front) that is not occupied
	public int[] enqueue(int[] q, int vertex) {
		for(int i=0; i<q.length; i++) {
			if(q[i] == -1) {
				q[i] = vertex;
				break;
			}
		}
		return q;
	}
	
	//Helper method for breadth first search bfs helper method
		//if queue is empty return true
		public boolean qIsEmpty(int[] q) {
			for(int i=0; i<q.length; i++) {
				if(q[i]!=-1) {
					return false;
				}
			}
			return true;
		}
	
	//Helper method for bfs helper method
	//Update dequeue array w/ element at front of queue
	//Removes the first element from the queue array and shifts all the elements forward 
	public int updateDequeue(int[] q, int[] dQ) {
		int dequeued = q[0];
		this.addElement(dQ, dequeued);
		for(int i=0; i<q.length-1; i++) {
			q[i] = q[i+1];
		}
		
		return dequeued;
	}
	
	
	
	
}
