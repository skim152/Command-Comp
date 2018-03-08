import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class Graph <T>
{		
    private LinkedList<Integer>[] adjacencyList; // Will store the directed edges of each vertex
    private HashMap<T,Integer> verticiesHashMap = new HashMap<>(); //Used to associate vertex names with their index
	private ArrayList<T> verticies = new ArrayList<T>(); // Will contain the vertex names at the position of their index  
    private int numOfVerticies; // Will hold the number of unique class names in the file thats read in.  
    private Stack<Integer> stack = new Stack<>(); // Keep track of the vertices being stored, when sorting them in topological order. 
    
	

	public void buildGraph(String fileName)
    {
        try 
        {
        	int index = 0; // will keep track of each vertices index
        	
            @SuppressWarnings("resource")
            // Scan a the file that contains the vertex dependency information
			Scanner sc = new Scanner(new File(fileName));
            
            while(sc.hasNext())
            {
            	// For each line in the txt file
                String line = sc.nextLine();
                // Split the contents of the line into an array, using spaces as the delimiter. 
                String[] vertexName = line.split(" ");
                
                // For each vertex name, add it to the HashMap, along with its associated index number that is contained in the index variable tracker.
                // Storing the vertex indices rather than the names simplifies the depth-first search. The hash map would associate index 0 with ClassA,
                // index 1 with ClassC and so on.
                for( int i = 0; i < vertexName.length; i ++)
                {
                	T vertex = (T)vertexName[i];
                	
                	// Do not allow duplicate vertex names to be added into the HasMap. 
                	if(!verticiesHashMap.containsKey(vertex))
                	{
                		// Make sure that once a new vertex and its associated index have been added, then to increment the index variable, so it can be used to
                		// associate with the next unique vertex name.
                		verticiesHashMap.put(vertex,index++);
                		//System.out.println(className + " at " + index);
                		
                		// For each new vertex name and index added to the HashMap, also add the vertex name into an ArrayList<T>,
                		// as to associate it with their index in the list of vertices. For example, the first vertex read in from the file will be
                		// located at position 0 of the list of vertices. The second unique vertex read in from the file will be placed in index 1, etc...
                		// This will make it easy to retrieve the names of the classes when displaying them in the GUI, after they have been sorted in topological order. 
                		verticies.add(vertex);
                	}
                }
            } 
            // variable to store the size of the HashMap, indicating how many unique vertices are in the graph. 
            numOfVerticies = verticiesHashMap.size();
            
            // Create the adjacency list, which is an array of linked list. The size of the array will equal the number of unique vertices in the graph. 
            // Each index position of the adjacency list array will correspond to the vertex that has that same index number associated with it in the vertexNames HashMap.
            adjacencyList = new LinkedList[numOfVerticies]; 
            
            
            // Will need to scan the file a second time to now create the adjacency list containing the edges of each vertex in the graph. 
            sc = new Scanner(new File(fileName));
            while(sc.hasNext())
            {
                String line = sc.nextLine();
                String[] vertexName = line.split(" ");
                                
                // The first content in each line will always be the source vertex, and the vertices next to it in the same line are dependent upon it.
                T sourceVertex = (T) vertexName[0];
                
                // Create a new linked list, that will hold the edges of the source vertex.
                // This linked list will be created in the array position specified by the HashMap integer for the source vertex. 
            	adjacencyList[verticiesHashMap.get(sourceVertex)] = new LinkedList<>();
            	
            	// All the remaining vertices in the current line of the file, are the edges of the source vertex. 
                for( int i = 1; i < vertexName.length; i ++)
                {
                	T edgeVertex =  (T) vertexName[i];
                	
                	// use the addEdge method to add the edgeVertex into the adjacency list of the source vertex.  
                	addEdge(sourceVertex,edgeVertex);
                }    
            }      
            // If the file is read in, and no exceptions have been thrown, then the graph was built successfully. 
            JOptionPane.showMessageDialog(null,"Graph Built Sucessfully");
        }
        catch (FileNotFoundException ex)
        {
        	// If the file could not be open or doesn't exist, then display appropriate message. 
            JOptionPane.showMessageDialog(null,"File Did Not Open");
        }
    }
      
	/**
	 * This method will create a directed edge pointing from the source vertex to the destination vertex.
	 * edges of each class to another.
	 * 
	 * @param source the source vertex
	 * @param dest the destination vertex  
	 */
    public void addEdge(T source, T destination)
    {
        // can only add an edge if both the source and destination vertices already exist in the graph
       if(verticiesHashMap.containsKey(source) && verticiesHashMap.containsKey(destination))
       {
    	   int sourceVertexIndex = verticiesHashMap.get(source);
    	   
    	   //The destination vertex will be added into the linked list of the source vertex, in the adjacency list array.
    	   adjacencyList[sourceVertexIndex].add(verticiesHashMap.get(destination));
       }
       // else, return error message if either source or destination do not already exist in the graph.
       else
       {
    	   JOptionPane.showMessageDialog(null,"Edge going from " + source + " to " + destination + " cannot be added.");
    	   return;
       }
    }

      
    

    public String topologicalSort(T sourceVertex)
    {
    	//Create boolean arrays, which will be used to determine whether a vertex has been discovered or has finished, when performing the dfs topological sort.
    	// They will be initialized to false, and set to true if they are found to be discovered or the dfs has concluded for that vertex without any cycles being detected.  
    	boolean discovered[]= new boolean[numOfVerticies];
        boolean finished[]= new boolean[numOfVerticies];
        
        //An invalid class name should generate an appropriate error message.
        if(!verticiesHashMap.containsKey(sourceVertex))
        {
     	   JOptionPane.showMessageDialog(null,"Invalid Class Name");
        }
        // else if a valid class or vertex name is entered
        else
        {
	        try
	        {
	        	// call the recursive depthFirstSearch method, which arranges the graph in reverse topological order, using the source vertex index.
				depthFirstSearch(verticiesHashMap.get(sourceVertex), discovered, finished);
				
				// Since the depthFirstSearch method algorithm generates a reverse topological order, then the forward
				// topological order can be ascertained by popping the vertices off the stack 
	
	            String orderOfClass = "";
	            
	            // While the stack is not empty
	            while(!stack.isEmpty())
	            {
	            	// Pop the stack, which will be an int number corresponding to the index of a specific vertex.
	               int pop = stack.pop();
	               
	               // Use the vertices ArrayList<String> to retrieve the name of the vertex at the specified index position of the popped integer. 
	                String sort =  (String) verticies.get(pop);
	                
	                //Add that name into an ArrayList<String> in the sorted order. 
	                orderOfClass += sort + " ";
	            }
	            return orderOfClass;
			}
	        // Will catch an exception, if there is a cycle detected 
	        catch (Exception e)
	        {
	        	//catch cycle detected exception, and display message
	        	JOptionPane.showMessageDialog(null,"Cycle Detected");
	        	
				e.printStackTrace();
			}
        }
        return null;
    }
    
    
	
    private void depthFirstSearch(int sourceIndex, boolean discovered[], boolean finished[]) throws Exception
    {       
    	//if source is discovered
        if(discovered[sourceIndex] == true)
        {
        	// erase any of vertices added to the stack prior to the cycle being found.
        	stack = new Stack<>();
        	
        	//throw cycle detected exception
        	throw new Exception("Cycle Detected");
        }      
        
        //if source is finished, then return
        if(finished[sourceIndex] == true)
        {
            return;
        }
        // mark source as discovered
        discovered[sourceIndex] = true;
    	
        //If the adjacency list for the source is not null
        if(adjacencyList[sourceIndex]!= null)
        {
        	//for all adjacent vertices v
        	for(int v: adjacencyList[sourceIndex])
        	{
        		//recursively do a depthFirstSearch of each of the adjacent vertices
        		depthFirstSearch(v,discovered,finished);
        	}
        }
        //mark source as finished
        finished[sourceIndex] = true;
        
        //push source onto the stack
        stack.push(sourceIndex);
    }
}







