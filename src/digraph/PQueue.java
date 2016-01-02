package digraph;

/**
 * PriorityQueue with updateData() and item concept.
 * Makes use of a min heap stored in an ArrayList.
 * 
 * @author Christoph Stamm 
  */

import java.util.*;

public class PQueue<E> {
    //////////////////////////////////////////////////////////////////////
    private class Item extends PQItem {
        private E m_data;						// data element
        private int m_index;					// heap position
        
        private Item(E data, int index) {
            m_data = data;
            m_index = index;
        }
        
        private E getData() {
            return m_data;
        }
        
        private int getIndex() {
            return m_index;
        }

        private void setIndex(int index) {
            m_index = index;
        }
        
    	private boolean checkMembership(PQueue<?> queue) {
    		return queue == PQueue.this;
    	}
    }
    
    //////////////////////////////////////////////////////////////////////
    private Comparator<? super E> m_comp;	// comparator
    private ArrayList<Item> m_array;		// internal data storage for the heap
    
    /**
     * Creates empty priority queue.
     * @param comp comparator
     */
    public PQueue(Comparator<? super E> comp) {
    	m_comp = comp;
        m_array = new ArrayList<Item>();
    }
    
    /**
     * Creates priority queue and adds all elements of collection c to this queue in linear time.
     * O(n), where n is the number of element in the given collection.
     * @param c collection
     * @param comp comparator
     */
    public PQueue(Collection<? extends E> c, Comparator<? super E> comp) {
    	m_comp = comp;
        m_array = new ArrayList<Item>(c.size());
        
        // copy elements
        int j = 0;
        for(E e: c) {
            m_array.add(new Item(e, j++));
        }
      
    	// create heap
    	final int s = m_array.size();
    	int l2 = s/2 - 1;
    	for (int i = l2; i >= 0; i--) {
    	    siftDown(i);
    	}
    }
    
    /**
     * 
     * @return number of elements in this queue
     */
    public int size() {
        return m_array.size();
    }
    
    /**
     * 
     * @return true if this queue is empty
     */
    public boolean isEmpty() {
        return m_array.isEmpty();
    }
    
    /**
     * Adds a data element to this queue in O(log n) time.
     * @param data data element
     * @return item of the added data element
     */
    public PQItem add(E data) {
        int s = size();
        Item item = new Item(data, s);
        m_array.add(item);
        siftUp(s);
        return item;
    }
    
    /**
     * Removes the first element in the ordered sequence of queue elements in O(log n) time.
     * @return data of the removed element
     */
    public E removeMin() {
        int size = size();
        if (size == 0) return null;
        if (size == 1) return m_array.remove(0).getData();
            
		int last = size - 1;
		// swap a[first] with a[last]
		Item t = m_array.get(0); 
        E data = t.getData();
		set(0, m_array.get(last)); 
		//set(last, t);
		// remove last
		m_array.remove(last);
		t.m_index = -1; // set index of removed item to an invalid value
		// heapify
		siftDown(0);
        return data;
    }
    
    /**
     * Clears the queue and makes it the empty queue.
     */
    public void clear() {
        m_array.clear();
    }
 
    /**
     * Gets element data
     * @param item
     * @return element data of given item
     */
	@SuppressWarnings("unchecked")
	public E getData(PQItem item) {
    	assert checkMembership(item) : "wrong item";
    	return ((Item)item).getData();
	}
    
	/**
	 * Gets heap index
	 * @param item
	 * @return heap index of given item
	 */
	@SuppressWarnings("unchecked")
	public int getIndex(PQItem item) {
    	assert checkMembership(item) : "wrong item";
    	return ((Item)item).getIndex();
	}
    
    /**
     * Gets the i-th item of this min-heap. The items are not sequentially ordered in the heap.
     * @param i
     * @return item or null if the given index is not available
     */
    public PQItem getItem(int i) {
        return (i >= 0 && i < size()) ? m_array.get(i) : null;
    }
    
    /**
     * Returns first item. It's the the first element in sequential order.
     * @return first item or null if the heap is empty
     */
    public PQItem getFirstItem() {
        return getItem(0);
    }
    
    /**
     * Returns next item. You can use this method for iterating through the heap.
     * @param item
     * @return next item or null if there is no next item
     */
	public PQItem getNextItem(PQItem item) {
    	assert item == null || checkMembership(item) : "wrong item";
    	
		if (item == null) return null;
        @SuppressWarnings("unchecked")
		int index = ((Item)item).getIndex() + 1;
        return (index < size()) ? m_array.get(index) : null;
    }
    
    /**
     * Updates data of a queue element and updates its heap position in O(log n) time.
     * @param item item of the element to be updated
     * @param data new data value of given item
     * @return true if the data could be updated
     */
    public boolean updateData(PQItem item, E data) {
		assert checkMembership(item) : "wrong item";
    	
		@SuppressWarnings("unchecked")
		Item pqitem = (Item)item;
        int pos = pqitem.getIndex();
        if (pos < 0 || pos >= m_array.size()) return false; // item is not in heap

        pqitem.m_data = data;
        
        if (pos > 0) {
            // check heap condition at parent
            int par = (pos - 1)/2;
    		if (m_comp.compare(m_array.get(par).m_data, m_array.get(pos).m_data) > 0) {
                siftUp(pos);
                return true;
            }
        }
        int son = pos*2 + 1;
        if (son < size()) {
            // check heap condition at son
    		if (m_comp.compare(m_array.get(pos).m_data, m_array.get(son).m_data) > 0) {
                siftDown(pos);
            }        
        }
        return true;
    }
    
    private int set(int pos, Item item) {
        int oldIndex = item.getIndex();
        item.setIndex(pos);
        m_array.set(pos, item);
        return oldIndex;
    }
    
    /**
     * sift down at position pos.
     * O(log n)
     */
    private void siftDown(int pos) {
        final int end = size() - 1;
    	int son = pos*2 + 1;
    	
    	while (son <= end) {
    		// son ist der linke Sohn
    		if (son < end) {
    			// pos hat auch einen rechten Sohn
    			if (m_comp.compare(m_array.get(son).m_data, m_array.get(son + 1).m_data) > 0) son++;
    		}
    		// son ist der groessere Sohn
    		if (m_comp.compare(m_array.get(pos).m_data, m_array.get(son).m_data) > 0) {
	    		// swap a[pos] with a[son]
				Item t = m_array.get(pos); 
				set(pos, m_array.get(son)); 
				set(son, t);
				pos = son;
				son = 2*pos + 1;
    		} else {
    			return;
    		}
    	}
    }
    
    /**
     * sift up at position pos
     * O(log n)
     */
    private void siftUp(int pos) {
        int par = (pos - 1)/2; // parent
        
        while(par >= 0 && m_comp.compare(m_array.get(par).m_data, m_array.get(pos).m_data) > 0) {
	    	// swap a[par] with a[pos]
			Item t = m_array.get(par); 
			set(par, m_array.get(pos)); 
			set(pos, t);
			pos = par;
			par = (pos - 1)/2;
        }
    }
 
	@SuppressWarnings("unchecked")
	private boolean checkMembership(PQItem item) {
		assert item != null && item instanceof PQueue<?>.Item : 
			"wrong item";
		return ((Item)item).checkMembership(this);
	}
    
    /**
     * Sorting of collection c.
     * Uses heap sort with O(n log n) time.
     * @param c collection to be sorted
     * @param comp comparator
     */
    public static <E> void heapSort(Collection<E> c, Comparator<? super E> comp) {
        PQueue<E> pq = new PQueue<E>(c, comp);
        
        c.clear();
        while(!pq.isEmpty()) {
        	c.add(pq.removeMin());
        }
    }
}
