package heaps;

import java.util.Random;
import java.util.UUID;

import javax.swing.JOptionPane;

import heaps.util.HeapToStrings;
import heaps.validate.MinHeapValidator;
import timing.Ticker;

public class MinHeap<T extends Comparable<T>> implements PriorityQueue<T> {

	private Decreaser<T>[] array;
	private int size;
	private final Ticker ticker;

	/**
	 * I've implemented this for you.  We create an array
	 *   with sufficient space to accommodate maxSize elements.
	 *   Remember that we are not using element 0, so the array has
	 *   to be one larger than usual.
	 * @param maxSize
	 */
	@SuppressWarnings("unchecked")
	public MinHeap(int maxSize, Ticker ticker) {
		this.array = new Decreaser[maxSize+1]; // an array consists of maxsize+1 space to put decreaser 
		                                       // but we don't use the 0 position 
		this.size = 0;
		this.ticker = ticker;
	}
	//constructor initialize

	//
	// Here begin the methods described in lecture
	//
	
	/**
	 * Insert a new thing into the heap.  As discussed in lecture, it
	 *   belongs at the end of objects already in the array.  You can avoid
	 *   doing work in this method by observing, as in lecture, that
	 *   inserting into the heap is reducible to calling decrease on the
	 *   newly inserted element.
	 *   
	 *   This method returns a Decreaser instance, which for the inserted
	 *   thing, tracks the thing itself, the location where the thing lives
	 *   in the heap array, and a reference back to MinHeap so it can call
	 *   decrease(int loc) when necessary.
	 */
	public Decreaser<T> insert(T thing) {
		//
		// Below we create the "handle" through which the value of
		//    the contained item can be decreased.
		// VERY IMPORTANT!
		//    The Decreaser object contains the current location
		//    of the item in the heap array.  Initially it's ++size,
		//    as shown below.  The size is increased by 1, and that's
		//    were you should store ans in the heap array.
		//
		//    If and when the element there changes location in the heap
		//    array, the .loc field of the Decreaser must change to reflect
		//    that.
		//
		Decreaser<T> ans = new Decreaser<T>(thing, this, ++size);
		//
		// You have to now put ans into the heap array
		//   Recall in class we reduced insert to decrease
		//
		//
		//infinity to the new inserted value
		this.array[size] = ans;
		decrease(size);
		ticker.tick(3);
		
		return ans;
	}

	/**
	 * This method responds to an element in the heap decreasing in
	 * value.   As described in lecture, that element might have to swap
	 * its way up the tree so that the heap property is maintained.
	 * 
	 * This method can be called from within this class, in response
	 *   to an insert.  Or it can be called from a Decreaser.
	 *   The information needed to call this method is the current location
	 *   of the heap element (index into the array) whose value has decreased.
	 *   
	 * Really important!   If this method changes the location of elements in
	 *   the array, then the loc field within those elements must be modified 
	 *   too.  For example, if a Decreaser d is currently at location 100,
	 *   then d.loc == 100.  If this method moves that element d to
	 *   location 50, then this method must set d.loc = 50.
	 *   
	 * In my solution, I made sure the above happens by writing a method
	 *    moveItem(int from, int to)
	 * which moves the Decreaser from index "from" to index "to" and, when
	 * done, sets array[to].loc = to
	 *   
	 * This method is missing the "public" keyword so that it
	 *   is only callable within this package.
	 * @param loc position in the array where the element has been
	 *     decreased in value
	 */
	void decrease(int loc) {
		//
		// As described in lecture
		//
		if(loc==1){
		//root, no need to move up
			ticker.tick();
		}
		else if(this.array[loc].getValue().compareTo(this.array[loc/2].getValue())<0){
				// greater than its parent, move up (call moveItem method)
				moveItem(loc,loc/2);
				decrease(loc/2); // recursive call to find its final location
				ticker.tick(3);
			}
		else {
			//smaller than its parent, no need to move up
			ticker.tick();
		}
	}

	
	public void moveItem(int from, int to){
		Decreaser<T> store = this.array[from];
		this.array[from]= this.array[to];
		this.array[to] = store;
		this.array[to].loc=to;
		this.array[from].loc=from;
		ticker.tick(5);
	}
	/**
	 * Described in lecture, this method will return a minimum element from
	 *    the heap.  The hole that is created is handled as described in
	 *    lecture.
	 *    This method should call heapify to make sure the heap property is
	 *    maintained at the root node (index 1 into the array).
	 */
	public T extractMin() {
		if(isEmpty()== false) {
		T ans = array[1].getValue();
		//
		// There is effectively a hole at the root, at location 1 now.
		//    Fix up the heap as described in lecture.
		//    Be sure to store null in an array slot if it is no longer
		//      part of the active heap
		//
		this.array[1]= this.array[size];
		this.array[1].loc = 1; 
		//when move the last item to the root don't forget to set the location in the heap
		this.array[this.size]=null;
		this.size--;
		ticker.tick(7);
		if(this.size > 1) {
			heapify(1);
			ticker.tick(1);
		}
		//
		return ans;
		}
		return null;
	}

	/**
	 * As described in lecture, this method looks at a parent and its two 
	 *   children, imposing the heap property on them by perhaps swapping
	 *   the parent with the lesser of the two children.  The child thus
	 *   affected must be heapified itself by a recursive call.
	 * @param where the index into the array where the parent lives
	 */
	private void heapify(int where) {
		//
		// As described in lecture
		// because left child and right child have different math representation of loc,
		// we need to discuss different scenario
		// we need to consider if it reaches the end of the size after swapping
		// there are two scenario: 
		// we should consider right child reaches the size first, since we are not sure if 
		// where*2+1 also less than size when we use a if statement(where*2<=size) first
		// if where*2+1<=size, where*2 also <=size, then we need to decide the number of children it has
		//when it have two children, where*2+1<=size
		//when it have one children, where*2<=size
		//when it has no children, where*2>size
		ticker.tick();
		if(where*2+1<=size) {
			//the following commented code doesn't even execute
//			//have no child 
//			ticker.tick();
//			if (array[where*2].getValue() == null) {
//				ticker.tick(1);
//				return;
//			}
//			// 2.have one child (left child)
//			else if(array[where*2+1].getValue()==null) {
//				if (array[where*2].getValue().compareTo(array[where].getValue())<0) {
//					moveItem(where,where*2);
//					heapify(where*2);
//					ticker.tick(4);
//					// when value greater than its child, swap recursively
//				}
//			}
			// 3.have two children - a. left child is lesser b. right child is lesser
//			else { 
				//left is lesser
			if(array[where*2].getValue().compareTo(array[where*2+1].getValue())<0){
					if (array[where*2].getValue().compareTo(array[where].getValue())<0) {
						moveItem(where,where*2);
						heapify(where*2);
						ticker.tick(4);
						}
					}
			else{
				//if right child is lesser or equal
				if (array[where*2+1].getValue().compareTo(array[where].getValue())<0) {
						moveItem(where,where*2+1);
						heapify(where*2+1);	
						ticker.tick(4);
						}
					}
				}
		else if (where*2<=size) {//this else if follows after where*2<=size means that 
			                     //1 child
			if(array[where*2].getValue().compareTo(array[where].getValue())<0) {
				moveItem(where,where*2);
				heapify(where*2);	
				ticker.tick(3);
			}
		}
		else {//left child > size, reach the end 
			return;
		}
		
	}

		//
	
	/**
	 * Does the heap contain anything currently?
	 * I implemented this for you.  Really, no need to thank me!
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	//
	// End of methods described in lecture
	//
	
	//
	// The methods that follow are necessary for the debugging
	//   infrastructure.
	//
	/**
	 * This method would normally not be present, but it allows
	 *   our consistency checkers to see if your heap is in good shape.
	 * @param loc the location
	 * @return the value currently stored at the location
	 */
	public T peek(int loc) {
		if (array[loc] == null)
			return null;
		else return array[loc].getValue();
	}

	/**
	 * Return the loc information from the Decreaser stored at loc.  They
	 *   should agree.  This method is used by the heap validator.
	 * @param loc
	 * @return the Decreaser's view of where it is stored
	 */
	public int getLoc(int loc) {
		return array[loc].loc;
	}

	public int size() {
		return this.size;
	}
	
	public int capacity() {
		return this.array.length-1;
	}
	

	/**
	 * The commented out code shows you the contents of the array,
	 *   but the call to HeapToStrings.toTree(this) makes a much nicer
	 *   output.
	 */
	public String toString() {
//		String ans = "";
//		for (int i=1; i <= size; ++i) {
//			ans = ans + i + " " + array[i] + "\n";
//		}
//		return ans;
		return HeapToStrings.toTree(this);
	}

	/**
	 * This is not the unit test, but you can run this as a Java Application
	 * and it will insert and extract 100 elements into the heap, printing
	 * the heap each time it inserts.
	 * @param args
	 */
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "You are welcome to run this, but be sure also to run the TestMinHeap JUnit test");
		MinHeap<Integer> h = new MinHeap<Integer>(500, new Ticker());
		MinHeapValidator<Integer> v = new MinHeapValidator<Integer>(h);
		Random r = new Random();
		for (int i=0; i < 100; ++i) {
			v.check();
			h.insert(r.nextInt(1000));
			v.check();
			System.out.println(HeapToStrings.toTree(h));
			//System.out.println("heap is " + h);
		}
		while (!h.isEmpty()) {
			int next = h.extractMin();
			System.out.println("Got " + next);
		}
	}


}
