import java.util.ArrayList;
import javax.swing.JOptionPane;


public class Stack<E>{
	
	private ArrayList<E> elements;  // the data of the stack, which will hold the elements in LIFO order. 
	private int numberOfElements;   // will keep track of the number of elements currently in the ArrayList Stack. 
	private E topElement;           // the most recent element pushed onto the stack 
	
	
	/**
	 * Constructor that initializes the stack
	 */	
	public Stack()
	{
		elements = new ArrayList<E>();
		numberOfElements = 0;
	}
		
	
	public boolean isEmpty()
	{
		if (numberOfElements == 0)
			return true;
		
		else
			return false;
	}			
	

	public int getSize()
	{
		return this.numberOfElements;
	}
	

	public void push(E item)
	{
		elements.add(item);
		
		numberOfElements++;
	}
	
	
	
	public E pop() 
	{
		if (numberOfElements == 0)
		{
			JOptionPane.showMessageDialog(null, "Stack is Empty");
			
			return null;
		}
		
		else
		{
			topElement = elements.get(elements.size() - 1);
			
			elements.remove(elements.size() - 1);
			
			numberOfElements--;
			
			return topElement;	
		}	
	}

	public E peek()
	{
		if (numberOfElements == 0)
		{
			JOptionPane.showMessageDialog(null, "Stack is Empty");
			return null;
		}
		
		else
		{
			topElement = elements.get(elements.size() - 1);
			
			return topElement;	
		}
	}	
}