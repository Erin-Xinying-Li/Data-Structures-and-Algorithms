package heaps.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Rule;
import org.junit.Test;

import heaps.Decreaser;
import heaps.HeapSort;
import heaps.MinHeap;
import heaps.validate.MinHeapValidator;
import timing.InputSpec;
import timing.Ticker;
import timing.utils.IntArrayGenerator;

public class TestInsert extends TestUtil{

	@Test
	public void testEmptyHeap() {
		MinHeap<Integer> pq = genHeap(0).pq;	
		assertTrue("The heap after construction should be empty, but was not:  check your isEmpty() method", pq.isEmpty());
		assertEquals("The heap after construction should have 0 elements, but did not: check your size() method", 0, pq.size());
		verifySizeCapacity(pq);
	}

	@Test
	public void testInsert() {
		MinHeapValidator<Integer> mhv = genHeap(200);
		MinHeap<Integer> pq = mhv.pq;	
		int num = r.nextInt(100) + 16;
		for (int i=0; i < num; ++i) {
			verifySize("before insert", pq, i);
			verifySizeCapacity(pq);
			mhv.check();
			pq.insert(i);
			mhv.check();
			verifySize("after insert", pq, i+1);
			assertTrue("Heap should not be empty now: check your isEmpty() method", !pq.isEmpty());
		}
	}
	
	@Test
	public void testLocUpdatedDecrease() {
		//
		// Test that decrease updates the .loc field
		//
		MinHeapValidator<Integer> mhv = genHeap(5);
		MinHeap<Integer> pq = mhv.pq;
		mhv.check();
		Decreaser<Integer> d131 = pq.insert(131);
		mhv.check();
		String before = d131.toString();
		mhv.check();
		pq.insert(1); // should cause 131 to change locations
		mhv.check();
		String after = d131.toString();
		assertTrue("You did not update the .loc field of affected heap elements in decrease",
				!before.equals(after));
	}
	
}
