package heaps.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Rule;
import org.junit.Test;

import heaps.MinHeap;
import heaps.Decreaser;
import heaps.validate.MinHeapValidator;
import timing.Ticker;

public class TestExtractMin extends TestUtil{
	
	@Test
	public void testExtractMinUniq() {
		for (int i=0; i < 10; ++i) {
			System.out.println("Testing with unique vals size " + i);
			sortTest(genUniqueInts(i), false);
			System.out.println("done");
		}
        int i=50;
        System.out.println("Testing with unique vals size " + i);
        sortTest(genUniqueInts(i), false);
    }

	@Test
	public void testExtractMinDups() {
		for (int i=0; i < 10; ++i) {
			System.out.println("Testing with duplicates size " + i);
			sortTest(genDupInts(i), false);
			System.out.println("done");
		}
        int i=50;
        System.out.println("Testing with duplicates size " + i);
        sortTest(genUniqueInts(i), false);
	}

	@Test
	public void testLocUpdatedHeapify() {
		//
		// Test that decrease updates the .loc field
		//
		MinHeapValidator<Integer> mhv = genHeap(10);
		MinHeap<Integer> pq = mhv.pq;
		for (int i=1; i <= 6; ++i) {
			mhv.check();
			pq.insert(i);
			mhv.check();
		}
		Decreaser<Integer> d131 = pq.insert(131);
		String before = d131.toString();
		mhv.check();
		pq.extractMin(); // should cause 131 to change locations
		mhv.check();
		String after = d131.toString();
		assertTrue("You did not update the .loc field of affected heap elements in heapify",
				!before.equals(after));
	}

}
