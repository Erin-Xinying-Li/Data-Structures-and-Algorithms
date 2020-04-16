package avl.test;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import avl.AVLTree;
import avl.util.TreeToStrings;
import avl.validate.BSTValidationError;
import avl.validate.BSTValidator;

public class TestExists {
	@Rule
	public FailReporter tvs = new FailReporter();

	private static BSTValidator<Integer> genTree() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		return new BSTValidator<Integer>(tree);
	}


	@Test
	public void testInsertSmallExists() {
		int[] values = {15, 10, 5, 7, 8, 3, 4, 20, 17};
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		for(int i=0; i<values.length; i++) {
			String before = TreeToStrings.toTree(tree);
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(values[i]);
			List<Integer> missing = new ArrayList<>();
			for(int j=0; j<=i; j++) {
				if(!tree.exists(values[j])) {
					missing.add(values[j]);
				}
			}
			try {
				if(missing.size()>0) {
					throw new BSTValidationError("\nAfter inserting "+ values[i] + " your tree is missing " + Arrays.toString(missing.toArray()));
				}
			} catch (Throwable t) {
				String oops = "\nTree before the problem occurred:\n";
				oops += before + "\n";
				oops += "Tree that triggered this problem:" + "\n";
				oops += TreeToStrings.toTree(tree);
				t.printStackTrace();
				throw new BSTValidationError(t + "" + oops);
			}
			verifySize("after Insert", tree, i+1);
			bstv.check();

		}
	}

	@Test
	public void testRemoveExists() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;
		for(int i=0; i<num; i++) {
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(i);
			verifySize("after Insert", tree, i+1);
			bstv.check();
		}
		 Set<Integer> removed = new HashSet<>();
		for (int i=0; i < num/2; ++i) {
			verifySize("before Remove", tree, num - i);
			bstv.check();
			String before = TreeToStrings.toTree(tree);
			tree.remove((i + 7) % num);
			removed.add((i+7)%num);
			List<Integer> missing = new ArrayList<>();
			for(int j=0; j<num; ++j) {
				if(!removed.contains(j)) {
					// this element should still be in the tree
					if(!tree.exists(j)) 
						missing.add(j);
				}
			}
			try {
				if(missing.size()>0) {
					throw new BSTValidationError("\nAfter removing "+ (i+7)%num + " your tree is missing " + Arrays.toString(missing.toArray()));
				}
			} catch (Throwable t) {
				String oops = "\nTree before the problem occurred:\n";
				oops += before + "\n";
				oops += "Tree that triggered this problem:" + "\n";
				oops += TreeToStrings.toTree(tree);
				t.printStackTrace();
				throw new BSTValidationError(t + "" + oops);
			}
			bstv.check();
			verifySize("after Remove", tree, num - i - 1);
		}
	}


	private void verifySize(String event, AVLTree<?> tree, int expectedSize) {
		assertEquals("Expect tree " + event + " to have size " + 
				expectedSize + " but it did not", expectedSize, tree.size);
	}

}
