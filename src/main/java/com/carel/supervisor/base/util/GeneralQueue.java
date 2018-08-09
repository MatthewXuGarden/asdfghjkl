package com.carel.supervisor.base.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.carel.supervisor.controller.priority.PriorityMgr;

	public class GeneralQueue<Item> implements Iterable<Item> 
	{
	    private int N;         // number of elements on queue
	    private Node first;    // beginning of queue
	    private Node last;     // end of queue

	    private int DEFAULT_PRY = 100; //di default uso priority generica
	    
	    // helper linked list class
	    protected class Node 
	    {
	        private Item item;
	        private Node next;
	        private int priority;
	    }

	    // create an empty queue
	    public GeneralQueue() 
	    {
	        first = null;
	        last  = null;
	        N = 0;
	        DEFAULT_PRY = PriorityMgr.getInstance().getDefaultPriority();
	    }

	    // is the queue empty?
	    public boolean isEmpty() 
	    { 
	    	return first == null; 
	    }
	    
	    public int length()      
	    { 
	    	return N;             
	    }
	    
	    public int size()        
	    { 
	    	return N;             
	    }

		// OLD: add an item to the queue
	    /*
	    public void enqueue(Item item) 
	    {
	        Node x = new Node();
	        x.item = item;
	        if (isEmpty()) 
	        { 
	        	first = x;     
	        	last = x; 
	        }
	        else           
	        { 
	        	last.next = x; 
	        	last = x; 
	        }
	        N++;
	    }
	    */
	    
	    // add an item to the queue without priority
	    public void enqueue(Item item) 
	    {
	        enqueue(item, DEFAULT_PRY);
	    }

	    // add an item to the queue by priority
	    public void enqueue(Item item, int priority)
	    {
	    	Node x = new Node();
	        x.item = item;
	        x.priority = priority;
	        
	        if (isEmpty()) //inserimento primo item della coda
	        { 
	        	first = x;
	        	last = x;
	        	x.next = null;
	        }
	        else           
	        {
	        	if ((priority < DEFAULT_PRY)) //set prioritario da ordinare
	        	{
	        		if (priority < first.priority) //nessun set prioritario presente: inserimento in testa
	        		{
	        			x.next = first;
	        			first = x;
	        		}
	        		else //set prioritario presente: inserimento a ricerca di posizione
	        		{
	        			Node tmpi = first; //precedente
	        			Node tmpc = first.next; //corrente
	        			
	        			while ((tmpc != null) && (tmpc.priority <= priority))
	        			{
	        				tmpi = tmpc;
	        				tmpc = tmpc.next;
	        			}
	        			
	        			tmpi.next = x;
	        			x.next = tmpc;
	        			
	        			if (tmpc == null)
	        				last = x; //ho inserito alla fine della coda
	        		}
	        	}
	        	else //altro set (non prioritario), alla fine della coda in ordine di arrivo
	        	{
	        		last.next = x; 
	        		last = x;
	        		x.next = null;
	        	}
	        }
	        
	        N++;
	    }
	    
	    // OLD: remove and return the least recently added item
	    // NEW: remove the top most position item
	    public Item dequeue() 
	    {
	        if (isEmpty()) throw new RuntimeException("Queue underflow");
	        Item item = first.item;
	        first = first.next;
	        N--;
	        return item;
	    }

	    // estrae dalla coda il primo nodo con una data priority (ossia il primo set di un particolare plugin)
	    public Item dequeueByPriority(int priority)
	    {
	    	Item item = null;
	    	
	    	if (!isEmpty()) //se coda non vuota
	    	{
	    		Node tmpi = first;
	    		Node tmpf = first;
	    		
	    		while ((tmpf != null) && (tmpf.priority != priority))
	    		{
	    			tmpi = tmpf;
	    			tmpf = tmpf.next;
	    		}
	    		
	    		if (tmpf != null)
	    		{
	    			item = tmpf.item;
	    			tmpi.next = tmpf.next;
	    			
	    			if (tmpf == last)
	    			{
	    				last = tmpi; //estratto ultimo nodo
	    				tmpi.next = null;
	    				emptyNode(tmpf);
	    			}
	    			
	    			if (tmpi == first)
	    			{
	    				first = tmpi.next; //estratto primo nodo
	    				tmpi.next = null;
	    				emptyNode(tmpi);
	    			}
	    		}
	    	}
	    	
	    	return item;
	    }
	    
	    // elimina dalla coda tutti i nodi con una data priority (ossia tutti i set di un particolare plugin)
	    public void removeAllByPriority(int priorityValue)
	    {
	    	if (!isEmpty()) //se coda non vuota
	    	{
	    		Node tmpi = first;
	    		Node tmpf = first;
	    		
	    		while (tmpf != null)
	    		{
	    			if (tmpf.priority == priorityValue)
	    			{
	    				if (tmpi != first)
	    				{
	    					tmpi.next = tmpf.next;
	    					emptyNode(tmpf);
	    					tmpf = tmpi.next;
	    				}
	    				else
	    				{
	    					first = first.next; //ho eliminato primo nodo
	    					tmpi = tmpi.next;
	    					emptyNode(tmpf);
	    					tmpf = tmpi;
	    				}
	    				
	    				if (tmpf == null)
    						last = tmpi; //ho eliminato ultimo nodo
	    			}
	    			else
	    			{
	    				tmpi = tmpf;
	    				tmpf = tmpf.next;
	    			}
	    		}
	    	}
	    	
	    	return;
	    }
	    
	    public void emptyNode(Node node)
	    {
	    	//System.out.println(((Float)node.item).floatValue() + " - " + node.priority);
	    	node.next = null;
	    	node.item = null;
	    }
	    
	    // list the elements in the queue
	    public String toString() 
	    {
	        String s = "";
	        
	        for (Node x = first; x != null; x = x.next)
	            s += x.item.toString() + " - (p." + x.priority + ") ";
	        
	        return s;
	    }

	    public Iterator<Item> iterator()  
	    { 
	    	return new QueueIterator();  
	    }

	    // an iterator, doesn't implement remove() since it's optional
	    private class QueueIterator implements Iterator<Item> 
	    {
	        private Node current = first;

	        public boolean hasNext()  
	        { 
	        	return current != null;                     
	        }
	        
	        public void remove()      
	        { 
	        	throw new UnsupportedOperationException();  
	        }

	        public Item next() 
	        {
	            if (!hasNext()) throw new NoSuchElementException();
	            Item item = current.item;
	            current = current.next; 
	            return item;
	        }
	    }



	    // a test client
	    public static void main(String[] args) {

	       /***********************************************
	        *  A queue of strings
	        ***********************************************/
	    	GeneralQueue<String> q1 = new GeneralQueue<String>();
	        q1.enqueue("Vertigo");
	        q1.enqueue("Just Lose It");
	        q1.enqueue("Pieces of Me");
	        System.out.println(q1.dequeue());
	        q1.enqueue("Drop It Like It's Hot");
	        while (!q1.isEmpty())
	            System.out.println(q1.dequeue());
	        System.out.println();


	       /*********************************************************
	        *  A queue of integers. Illustrates autoboxing and
	        *  auto-unboxing.
	        *********************************************************/
	        GeneralQueue<Integer> q2 = new GeneralQueue<Integer>();
	        for (int i = 0; i < 10; i++)
	            q2.enqueue(i);

	        // test out iterator
	        for (int i : q2)
	        	System.out.print(i + " ");
	        System.out.println();

	        // test out dequeue and enqueue
	        while (q2.size() >= 2) {
	            int a = q2.dequeue();
	            int b = q2.dequeue();
	            int c = a + b;
	            System.out.println(c);
	            q2.enqueue(a + b);
	        }
	        
	        
	        System.out.println();
	        
	        /***********************************************
	         *  A priority queue
	         ***********************************************/
	    	 GeneralQueue<Float> q0 = new GeneralQueue<Float>();
	    	 
	    	 q0.enqueue(new Float(4.2), 150);
	    	 q0.enqueue(new Float(5.5), 100);
	    	 q0.enqueue(new Float(15.5), 15);
	    	 q0.enqueue(new Float(25.5), 100);
	    	 q0.enqueue(new Float(440.2), 150);
	    	 q0.enqueue(new Float(0.5), 10); //inserimento 1mo elem a priority max
	    	 q0.enqueue(new Float(17.5), 15);
	    	 q0.enqueue(new Float(144.2), 150);
	    	 q0.enqueue(new Float(37.5), 100);
	    	 q0.enqueue(new Float(27.5), 100);
	    	 q0.enqueue(new Float(0.3), 10); //inserimento 2ndo elem a priority max
	    	 q0.enqueue(new Float(13.5), 15);
	    	 q0.enqueue(new Float(44.2), 10); //inserimento 3rzo elem a priority max
	    	 q0.enqueue(new Float(442), 150);
	    	 
	    	 /*
	    	 Float flo = q0.dequeueByPriority(20); //elimina primo elem di priority = 20
	    	 if (flo != null)
	    		 System.out.println(" ---> " + flo.floatValue() + " // eliminato dalla coda");
	    	 
	    	 q0.removeAllByPriority(10); //elimina tutti gli elem di priority = 10
	    	 
	    	 flo = q0.dequeueByPriority(40); //elimina primo elem di priority = 40
	    	 if (flo != null)
	    		 System.out.println(" ---> " + flo.floatValue() + " // eliminato dalla coda");
	    	 
	    	 q0.removeAllByPriority(0); //NON elimina gli elem di priority = 0  perchè non permesso
	    	 
	    	 flo = q0.dequeueByPriority(0); //NON elimina primo elem di priority = 0  perchè non permesso
	    	 if (flo != null)
	    		 System.out.println(" ---> " + flo.floatValue() + " // eliminato dalla coda");
	    	 
	    	 flo = q0.dequeue(); //scodo 1mo elem
	    	 if (flo != null)
	    		 System.out.println(" " + flo.floatValue() + " // scodato e ri-accodato con priority = " + (flo.intValue()/10)*10);
	    	 
	    	 q0.enqueue(flo, (flo.intValue()/10)*10); //ri-accodo ultimo elem appena scodato
	    	 */
	    	 
	    	 int priorityTest = 150;
	    	 System.out.println("Remove by priority = "+priorityTest+" :");
	    	 q0.removeAllByPriority(priorityTest);
	    	 
	    	 System.out.println("Dequeue by priority :");
	    	 while (!q0.isEmpty())
	            System.out.println(q0.dequeue().floatValue());
	    }
	}
