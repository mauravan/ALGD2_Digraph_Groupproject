package digraph;

import java.util.AbstractList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DLinkedList<E> extends AbstractList<E> {

	private ListItem first;
	private ListItem last;
	private int size = 0;
	
	/**
	 * Tells if the argument is the index of an existing element.
	 * @param index to test
	 * @return true or false
	 */
	private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }
	
	/**
	 * Gets ListItem by index.
	 * Precondition: valid index for this list
	 * @param index to get
	 * @return ListItem at that index
	 */
	private ListItem getListItemByIndex(int index) {

		assert isElementIndex(index) : "Index must be a valid index for this list.";
		
		if (index < (size >> 1)) {
			// start at the head			
			ListItem x = this.first;
			for(int i = 0; i < index; i++) {
				x = x.getNext();
			}
			return x;
		} else {
			// start at the tail
			ListItem x = this.last;
            for (int i = size - 1; i > index; i--) {
            	x = x.getPrev();
            }
            return x;
		}
		
	}
	
	/**
     * Returns the number of elements in this list.
     * @return the number of elements in this list
     */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Adds a list item at the front.
	 * Precondition: item must not be in a list
	 * @param item to link in front
	 * @return void
	 */
	private void linkInFront(ListItem item) {

		assert item != null : "Item must not be null.";
		assert !checkOwnership(item) : "Item must not be in a list.";
		
		item.setOwner(this);
		item.setPrev(null);
		item.setNext(first);
		
		if(last == null) {
			// list is empty
			first = item;
			last = item;
		} else {
			// create link from old first
			final ListItem oldFirst = first;
			oldFirst.setPrev(item);
			first = item;
		}
		
		size++;
		modCount++;
	}
	
	/**
	 * Adds a list item at the tail.
	 * Precondition: item must not be in a list
	 * @param item to link in back
	 * @return void
	 */
	private void linkInBack(ListItem item) {

		assert item != null : "Item must not be null.";
		assert !checkOwnership(item) : "Item must not be in a list.";
		
		final ListItem oldLast = last;
		
		item.setOwner(this);
		item.setPrev(oldLast);
		item.setNext(null);
		
		if(oldLast == null) {
			// list is empty
			first = item;
			last = item;
 		} else {
 			// create a link form old last
 			oldLast.setNext(item);
			last = item;
 		}

		size++;
		modCount++;
	}

	/**
	 * Unlinks a list item, sets prev and next to null and removes owner.
	 * Precondition: item must be in this list
	 * @param item unlinks this
	 * @return void
	 */
	private void unlink(ListItem item) {
		
		assert item != null : "Item must not be null.";
		assert checkMembership(item) : "Item must be in this list.";

		final ListItem prev = item.getPrev();
		final ListItem next = item.getNext();
		
		if (prev == null) {
			// item is first item
            first = next;
        } else {
        	// item is not first item
            prev.setNext(next);
        }

        if (next == null) {
        	// item is last item
            last = prev;
        } else {
        	// item is not last item
            next.setPrev(prev);
        }
		
		item.setNext(null);
		item.setPrev(null);
		item.setOwner(null);
		
		size--;
		modCount++;
	}

	/**
	 * Inserts all of the elements in the specified collection into this list, 
	 * starting at the specified position. Shifts the element currently at that position 
	 * (if any) and any subsequent elements to the right (increases their indices). 
	 * The new elements will appear in the list in the order that they are returned 
	 * by the specified collection's iterator.
	 * @param index after which die the elements have to be added
	 * @param c collection of elements which have to be added
	 * @return boolean if this list was modified
	 * @throws IndexOutOfBoundsException if index out of bounds
	 * @throws IllegalArgumentException if given collection is null
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		
		if(c == null) throw new IllegalArgumentException("Collection c must not be null.");
		if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index \"" + index + "\" is out of bounds.");
		
		int numNew = c.size();
		if(numNew == 0) return false;

		ListItem pred, succ;
        if (index == size) {
            succ = null;
            pred = last;
        } else {
            succ = getListItemByIndex(index);
            pred = succ.getPrev();
        }
        
        for (E e : c) {
        	ListItem newNode = new ListItem(e, pred, null, this);
            if (pred == null)
                first = newNode;
            else
                pred.setNext(newNode);
            pred = newNode;
        }
        
        if (succ == null) {
            last = pred;
        } else {
            pred.setNext(succ);
            succ.setPrev(pred);
        }
        
        size += numNew;
        modCount++;
        return true;
	}

	/**
     * Returns the data from the item at given index
     * @throws IndexOutOfBoundsException if index out of bounds
     * @return the data at the specified position in this list
     */
	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if(!isElementIndex(index)) throw new IndexOutOfBoundsException("Index \"" + index + "\" is out of bounds.");
		return (E) (getListItemByIndex(index)).getData();
	}

	/**
     * Set the data of the item at given index
     * @throws IndexOutOfBoundsException if index out of bounds
     * @return the data previously at the specified position
     */
	@Override
	public E set(int index, E element) {
		if(!isElementIndex(index)) throw new IndexOutOfBoundsException("Index \"" + index + "\" is out of bounds.");
		final ListItem item = getListItemByIndex(index);
		@SuppressWarnings("unchecked")
		E prevVal = (E) item.getData();
		item.setData(element);
		return prevVal;
	}

	/**
	 * Appends the specified element to the end of this list.
	 * @param element data to enter the list
	 */
	@Override
	public boolean add(E element) {
		final ListItem oldLast = last;
		final ListItem newItem = new ListItem(element, oldLast, null, this);
		
		last = newItem;
		
		if(oldLast == null) {
			// single item
			first = newItem;
		} else {
			oldLast.setNext(newItem);
		}
		
		size++;
		modCount++;
		return true;
	}
	
	/**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     * 
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
	@Override
	public void add(int index, E element) {
		if (index == size)
            addTail(element);
        else
        	if(!this.isElementIndex(index)) throw new IndexOutOfBoundsException("Index \"" + index + "\" is out of bounds.");
            addBefore(getListItemByIndex(index), element);
	}

	/**
     * Removes the item at the given index.
     * @param index index at which the specified element is to be deleted
     * @throws IndexOutOfBoundsException
     * @return data of the removed item
     */
	@SuppressWarnings("unchecked")
	@Override
	public E remove(int index) {
		if(!isElementIndex(index)) throw new IndexOutOfBoundsException("Index \"" + index + "\" is out of bounds.");
		ListItem toRemove = getListItemByIndex(index);
		unlink(toRemove);
		return (E) toRemove.getData();
	}

	/**
     * Returns an iterator over the elements in this list in proper sequence.
     * @return an iterator over the elements in this list in proper sequence
     */
    public Iterator<E> iterator() {
    	return new Itr();
    }
	
    /**
     * Returns a list-iterator of the elements in this list (in proper
     * sequence), starting at the head of the list.
     * @return a ListIterator of the elements in this list (in proper
     *         sequence), starting at the head of the list
     */
	@Override
	public ListIterator<E> listIterator() {
		return new ListItr();
	}

	/**
     * Returns a list-iterator of the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * @param index index of the first element to be returned from the
     *              list-iterator
     * @return a ListIterator of the elements in this list (in proper
     *         sequence), starting at the specified position in the list
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
	@Override
	public ListIterator<E> listIterator(int index) {
		return new ListItr(index);
	}

	/**
	 * Returns whether the given item is in this list or not
	 * @param item item to check
	 * @return true if item is a member of this list
	 */
	public boolean checkMembership(ListItem item) {
		return item.getOwner() == this;
	}
	
	/**
	 * Returns whether the given item is in a list or not
	 * @param item item to check
	 * @return true if item is member of a list
	 */
	private boolean checkOwnership(ListItem item) {
		return item.getOwner() != null;
	}

	/**
	 * Returns the first item or null if the list is empty
	 * @return the first item or null if the list is empty
	 */
	public ListItem head() {
		return first;
	}

	/**
	 * Returns the last item or null if the list is empty
	 * @return the last item or null if the list is empty
	 */
	public ListItem tail() {
		return last;
	}

	/**
	 * Returns the successor item of a given item
	 * @param item
	 * @return the successor of item or null if item is the last
	 * @throws IllegalArgumentException if given item is null or not in this list
	 */
	public ListItem next(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		return item.getNext();
	}

	/**
	 * Returns the predecessor item of a given item
	 * @param item
	 * @return the predecessor of item or null if item is the first
	 * @throws IllegalArgumentException if given item is null or not in this list
	 */
	public ListItem previous(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		return item.getPrev();		
	}

	/**
	 * Returns the cyclic successor item of a given item
	 * @param item
	 * @return the cyclic successor of item, if item is the last then the first
	 * @throws IllegalArgumentException if given item is null or not in this list
	 */
	public ListItem cyclicNext(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		
		ListItem next = item.getNext();
		
		if(next == null) {
			return this.first;
		} else {
			return next;
		}
	}

	/**
	 * Returns the cyclic predecessor item of a given item
	 * @param item
	 * @return the cyclic predecessor of item, if item is the first then the last
	 * @throws IllegalArgumentException if given item is null or not in this list
	 */
	public ListItem cyclicPrevious(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		
		ListItem prev = item.getPrev();
		
		if(prev == null) {
			return this.last;
		} else {
			return prev;
		}
	}

	/**
	 * Deletes item while iterating.
	 * If next = true: returns successor of item or null if item is the last.
	 * If next = false: returns predecessor of item or null if item is the first.
	 * @param item to be deleted
	 * @param next controls direction of iteration
	 * @return successor of item while iterating or null
	 */
	public ListItem delete(ListItem item, boolean next) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		
		ListItem returnValue = (next) ? item.getNext() : item.getPrev();
		unlink(item);
		return returnValue;
	}

	/**
	 * Deletes item while iterating.
	 * If next = true: returns cyclic successor of item or null if item is the only list item.
	 * If next = false: returns cyclic predecessor of item or null if item is the only list item.
	 * @param item to be deleted
	 * @param next controls direction of cyclic iteration
	 * @return successor of item while cyclic iterating or null if list becomes empty
	 */
	public ListItem cyclicDelete(ListItem item, boolean next) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		
		ListItem returnValue = (next) ? item.getNext() : item.getPrev();
		
		if(size != 1) {
			returnValue = (next && returnValue == null) ? this.first : returnValue;
			returnValue = (!next && returnValue == null) ? this.last : returnValue;
		}
		
		unlink(item);
		return returnValue;
	}

	/**
	 * Returns the data of a given item
	 * @param item
	 * @return the contents of item
	 * @throws IllegalArgumentException if given item is null or not in this list
	 */
	@SuppressWarnings("unchecked")
	public E get(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		return (E) item.getData();
	}

	/**
	 * Makes data the contents of item.
	 * @param item
	 * @param data
	 * @throws IllegalArgumentException if given item is null or not in this list
	 */
	public void set(ListItem item, E data) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		item.setData(data);
	}

	/**
	 * Deletes the item and returns its contents.
	 * @param item
	 * @return contents of the removed item
	 * @throws IllegalArgumentException if given item is null or not in this list
	 */
	@SuppressWarnings("unchecked")
	public E remove(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		unlink(item);
		return (E) item.getData();
	}

	/**
	 * Adds a new list item with given data at the front and returns it.
	 * @param data
	 * @return new item
	 */
	public ListItem addHead(E data) {
		ListItem newItem = new ListItem(data, null, null, null);
		linkInFront(newItem);
		return newItem;
	}

	/**
	 * Appends a new list item with given data and returns it.
	 * @param data
	 * @return appended item
	 */
	public ListItem addTail(E data) {
		ListItem newItem = new ListItem(data, null, null, null);
		linkInBack(newItem);
		return newItem;
	}

	/**
	 * Inserts a new list item with given data after item and returns the new item.
	 * @param item can be null
	 * @param data
	 * @return inserted item
	 * @throws IllegalArgumentException if given item is not null and is not in this list
	 */
	public ListItem addAfter(ListItem item, E data) {
		if(item == null) return this.addHead(data);
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");

		ListItem tmpNextItem = item.getNext();
		ListItem newItem = new ListItem(data, item, tmpNextItem, this);
		item.setNext(newItem);
		if(tmpNextItem == null) {
			last = newItem;
		} else {
			tmpNextItem.setPrev(newItem);
		}
		
		modCount++;
		size++;
		
		return newItem;
	}

	/**
	 * Inserts a new list item with given data before item and returns the new item.
	 * @param item can be null
	 * @param data
	 * @return inserted item
	 * @throws IllegalArgumentException if given item is not null and is not in this list
	 */
	public ListItem addBefore(ListItem item, E data) {
		if(item == null) return this.addTail(data);
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		
		ListItem tmpPrevItem = item.getPrev();
		ListItem newItem = new ListItem(data, tmpPrevItem, item, this);
		item.setPrev(newItem);
		if(tmpPrevItem == null) {
			first = newItem;
		} else {
			tmpPrevItem.setNext(newItem);
		}
		
		modCount++;
		size++;
		
		return newItem;
	}

	/**
	 * Moves item to front. item becomes new head.
	 * @param item to move to front
	 * @throws IllegalArgumentException if given item is not null and is not in this list
	 */
	public void moveToHead(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		
		if(item != first){
			unlink(item);
			linkInFront(item);
			modCount--; //because unlink and linkInFront both increase the modCount
		}
	}

	/**
	 * Moves item to back. item becomes new tail.
	 * @param item to move to back
	 * @throws IllegalArgumentException if given item is not null and is not in this list
	 */
	public void moveToTail(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		
		if(item != last) {
			unlink(item);
			linkInBack(item);
			modCount--; //because unlink and linkInBack both increase the modCount
		}
	}

	/**
	 * Rotates the list until item is the new head in O(1) time.
	 * @param item new head after rotation
	 * @throws IllegalArgumentException if given item is not null and is not in this list
	 */
	public void rotate(ListItem item) {
		if(item == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item)) throw new IllegalArgumentException("Item is not in this list.");
		
		if(item == tail()) {			
			// just move tail to head
			moveToHead(item);
		} else if(item != head()) {
			final ListItem currentHead = head();
			final ListItem currentTail = tail();
			final ListItem newTail = item.getPrev();			
			newTail.setNext(null);
			item.setPrev(null);			
			currentHead.setPrev(currentTail);
			currentTail.setNext(currentHead);			
			first = item;
			last = newTail;
			modCount++;
		}
	}

	/**
	 * Swaps two list items. The content of the list items doesn't change.
	 * @param item1
	 * @param item2
	 * @throws IllegalArgumentException if given items are null or not in this list.
	 */
	public void swap(ListItem item1, ListItem item2) {
		if(item1 == null) throw new IllegalArgumentException("No valid item.");
		if(item2 == null) throw new IllegalArgumentException("No valid item.");
		if(!checkMembership(item1)) throw new IllegalArgumentException("Item1 is not in this list.");
		if(!checkMembership(item2)) throw new IllegalArgumentException("Item2 is not in this list.");
		
		if(item1 != item2) {
			final ListItem tempNext = item1.getNext();
			final ListItem tempPrev = item1.getPrev();
			
			// Change own pointers
			if(item1.prev == item2) {  
				// direct neighbor before
				item1.setNext(item2);
				item1.setPrev(item2.getPrev());				
				item2.setNext(tempNext);
				item2.setPrev(item1);
			} else if(item1.getNext() == item2 ) {	
				// direct neighbor after				
				item1.setNext(item2.getNext());
				item1.setPrev(item2);				
				item2.setNext(item1);
				item2.setPrev(tempPrev);
			} else {	
				// No direct neighbors				
				item1.setNext(item2.getNext());
				item1.setPrev(item2.getPrev());				
				item2.setNext(tempNext);
				item2.setPrev(tempPrev);
			}
			
			// Change neighbors pointers
			if(item1.getNext() != null)
				(item1.getNext()).setPrev(item1);
			else 
				last = item1;
			
			if(item1.getPrev() != null)
				(item1.getPrev()).setNext(item1);
			else
				first = item1;
			
			if(item2.getNext() != null)
				(item2.getNext()).setPrev(item2);
			else 
				last = item2;
			
			if(item2.getPrev() != null)
				(item2.getPrev()).setNext(item2);
			else 
				first = item2;
			
			modCount++;
		}
	}

	/**
	 * Reverses the order of all list items. Head becomes tail and vice versa.
	 * Replaces Collections.reverse(), because that method needs either random access
	 * or it just replaces swap data objects and changes data of list items.
	 * Takes O(n) time, where n is the size of the this list. 
	 */
	public void reverse() {
		if(size == 0 || size == 1) return;

		ListItem item1 = head();
		ListItem item2 = tail();
		
		ListItem tempItem;
		
		int half = size/2;

		for(int i = 0; i < half; i++) {
			swap(item1, item2);
			tempItem = item1;
			item1 = item2.getNext();
			item2 = tempItem.getPrev();
		}
	}

	/**
	 * Inserts list after item in O(n) time, where n is the size of list.
	 * Makes list the empty list;
	 * @param item can be null
	 */
	public void addAfter(ListItem item, List<E> list) {
		if ((item != null && !checkMembership(item)))
			throw new IllegalArgumentException("Item must be a member of this list or null.");
		if (list == null)
			throw new IllegalArgumentException("List must not be null");
		if (list == this)
			throw new IllegalArgumentException("List must be a different list.");
		
		if (item == null) {
			conc(list, false);
		} else {
			Iterator<E> itr = list.iterator();
			while (itr.hasNext()) {
				addAfter(item, itr.next());
				itr.remove();
				item = item.getNext();
			}
		}
	}

	/**
	 * Inserts list before item in O(n) time, where n is the size of list.
	 * Makes list the empty list;
	 * @param item can be null
	 */
	public void addBefore(ListItem item, List<E> list) {
		if (item != null && !checkMembership(item))
			throw new IllegalArgumentException("Item must be a member of this list or null");
		if (list == null)
			throw new IllegalArgumentException("List must not be null.");
		if (list == this)
			throw new IllegalArgumentException("List must be a different list.");
		
		if (item == null) {
			conc(list, true);
		} else {
			Iterator<E> itr = list.iterator();
			while (itr.hasNext()) {
				addBefore(item, itr.next());
				itr.remove();
			}
		}
	}

	/**
	 * Appends (after = true) or prepends (after = false) list to this list in O(n) time 
	 * and makes list the empty list. n is the size of list.
	 * @param list
	 * @param after
	 */
	public void conc(List<E> list, boolean after) {
		if (list == null)
			throw new IllegalArgumentException("List must not be null.");
		if (list == this)
			throw new IllegalArgumentException("List must be another list than this.");

		if (after) {
			Iterator<E> itr = list.iterator();
			while (itr.hasNext()) {
				addTail(itr.next());
				itr.remove();
			}
		} else {
			Iterator<E> itr = list.iterator();
			if (itr.hasNext()) {
				ListItem tmpItem = addHead(itr.next());
				itr.remove();
				while (itr.hasNext()) {
					tmpItem = addAfter(tmpItem, itr.next());
					itr.remove();
				}
			}
		}
	}

	/**
	 * Removes a (cyclic extended) sublist in O(n) time, where n is the size of the returned list.
	 * If startInclusive = endExclusive then this list becomes the empty list.
	 * @param startInclusive first item to be removed
	 * @param endExclusive the item cyclic succeeding the last item to be removed
	 * @return list with removed items with head = startInclusive and tail = last removed
	 */
	public DLinkedList<E> remove(ListItem startInclusive, ListItem endExclusive) {
		if (startInclusive == null || endExclusive == null)
			throw new IllegalArgumentException("Both items must not be null.");
		if (!checkMembership(startInclusive))
			throw new IllegalArgumentException("Start has to be a member of this list.");
		if (!checkMembership(endExclusive))
			throw new IllegalArgumentException("End has to be a member of this list.");
		
		DLinkedList<E> returned = new DLinkedList<>();
		
		if(size == 1) {
			unlink(startInclusive);
			returned.linkInFront(startInclusive);
			return returned;
		}

		ListItem endInclusive = this.cyclicPrevious(endExclusive);
			
		if(endInclusive == startInclusive) {
			unlink(startInclusive);
			returned.linkInFront(startInclusive);
			return returned;
		}
		
		ListItem currentItem;
		ListItem nextItem = startInclusive;
		while(nextItem != null) {
			currentItem = nextItem;
			if(currentItem == endInclusive) {
				nextItem = null;
			} else {
				nextItem = currentItem.getNext();
			}
			unlink(currentItem);
			returned.linkInBack(currentItem);
		}
		return returned;
	}
	
	public static class ListItem {
		
		private Object data;
		private ListItem prev;
		private ListItem next;
		@SuppressWarnings("rawtypes")
		private DLinkedList owner;
		
		private ListItem() {}
		
		@SuppressWarnings("rawtypes")
		private ListItem(Object data, ListItem prev, ListItem next, DLinkedList owner) {
			this.data = data;
			this.prev = prev;
			this.next = next;
			this.owner = owner;
		}

		private Object getData() {
			return data;
		}
		
		private void setData(Object data) {
			this.data = data;
		}
		
		public ListItem getPrev() {
			return prev;
		}
		
		private void setPrev(ListItem prev) {
			this.prev = prev;
		}
		
		public ListItem getNext() {
			return next;
		}
		
		private void setNext(ListItem next) {
			this.next = next;
		}
		
		@SuppressWarnings("rawtypes")
		public DLinkedList getOwner() {
			return owner;
		}
		
		@SuppressWarnings("rawtypes")
		private void setOwner(DLinkedList owner) {
			this.owner = owner;
		}
	}
	
	private class Itr implements Iterator<E> {
		
		private ListItem next = head();
		private ListItem returned = null;
		private int currentModCount = modCount;

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			if (currentModCount != modCount)
				throw new ConcurrentModificationException();

			if (hasNext()) {
				returned = next;
				next = next.getNext();
				return (E) returned.getData();
			} else {
				throw new NoSuchElementException();
			}
		}
		
		@Override
		public void remove() {
			if (currentModCount != modCount)
				throw new ConcurrentModificationException();
			if (returned == null)
				throw new IllegalStateException();
			
			unlink(returned);
			returned = null;
			currentModCount++;
		}
	}
	
	private class ListItr implements ListIterator<E> {
		
		private ListItem next;
		private ListItem previous;		
		private ListItem returned;
		
		private int currentModCount;
		private int index;
		
		private ListItr() {
			this.next = head();
			this.previous = null;			
			this.currentModCount = modCount;
			this.index = 0;
		}
		
		private ListItr(int index) {
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException();
			
			if (index == size) {
				this.next = null;
				this.previous = tail();
			} else {
				this.next = getListItemByIndex(index);
				this.previous = this.next.getPrev();
			}
			
			this.currentModCount = modCount;
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			if (currentModCount != modCount)
				throw new ConcurrentModificationException();

			if (hasNext()) {
				returned = next;
				previous = next;
				next = next.getNext();				
				index++;
				return (E) returned.getData();
			} else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public boolean hasPrevious() {
			return previous != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E previous() {
			if (currentModCount != modCount)
				throw new ConcurrentModificationException();

			if (hasPrevious()) {
				returned = previous;				
				next = previous;
				previous = previous.getPrev();
				index--;
				return (E) returned.getData();
			} else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void remove() {
			if (currentModCount != modCount)
				throw new ConcurrentModificationException();
			if (returned == null)
				throw new IllegalStateException();
				
			if(returned == next) {
				ListItem newNext = next.getNext();
				unlink(next);
				next = newNext;
			} else if(returned == previous) {
				ListItem newPrev = previous.getPrev();
				unlink(previous);
				previous = newPrev;
				index--;
			} else {
				throw new IllegalStateException();
			}
			
			returned = null;
			currentModCount++;
		}

		@Override
		public void set(E data) {
			if (returned == null) {
				throw new IllegalStateException();
			} else {
				returned.setData(data);
			}
		}

		@Override
		public void add(E data) {			
			if (previous == null) {
				previous = addHead(data);
			} else {
				previous = addAfter(previous, data);
			}
			index++;
			currentModCount++;
		}

		public ListItem getVisited() {
			if (returned == null)
				throw new IllegalStateException();
			
			return returned;
		}
	}
}
