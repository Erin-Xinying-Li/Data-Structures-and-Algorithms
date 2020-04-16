package avl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import avl.AVLTree;
import org.junit.Rule;
import org.junit.Test;

import avl.util.TreeToStrings;
import avl.validate.BSTValidator;

/**
 *
 * @author Fernando Rojo
 *
 */
public class TestBalancedTree {

	@Rule
	public FailReporter tvs = new FailReporter();

	private static BSTValidator<Integer> genTree() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		return new BSTValidator<Integer>(tree);
	}

	@Test
	public void testEmptyTree() {
		AVLTree<Integer> bst = genTree().tree;
		assertTrue("The tree after creation should be empty, but your size is > 0.", bst.size == 0);
	}

	@Test
	public void testInsertSmallAscending() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;
		for (int i=0; i < num; ++i) {
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(i);
			bstv.check();
			verifySize("after Insert", tree, i+1);
		}
		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}

	@Test
	public void testInsertLargeAscending() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 200000;
		for (int i=0; i < num; ++i) {
			tree.insert(i);
		}
		bstv.check();
		verifySize("after Insert", tree, num);
	}

	@Test
	public void testInsertSmallDescending() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;
		for (int i= 0; i < num; ++i) {
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(num - i - 1);
			bstv.check();
			verifySize("after Insert", tree, i+1);
		}
		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}

	@Test
	public void testInsertLargeDescending() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 200000;
		for (int i=0; i < num; ++i) {
			tree.insert(num - i - 1);
		}
		bstv.check();
		verifySize("after Insert", tree, num);
	}

	@Test
	public void testInsertSmallRandom() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;

		ArrayList<Integer> ints = genUniqueInts(num);
		for (int i= 0; i < num; ++i) {
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(ints.get(i));
			bstv.check();
			verifySize("after Insert", tree, i+1);
		}
		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}

	@Test
	public void testInsertLargeRandom() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 200000;

		ArrayList<Integer> ints = genUniqueInts(num);
		for (int i=0; i < num; ++i) {
			tree.insert(ints.get(i));
		}
		bstv.check();
		verifySize("after Insert", tree, num);
	}

	@Test
	public void testInsertDuplicates() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;

		ArrayList<Integer> ints = genUniqueInts(num);
		for (int i= 0; i < num; ++i) {
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(ints.get(i));
			bstv.check();
			verifySize("after Insert", tree, i+1);
			tree.insert(ints.get(i));
			bstv.check();
			verifySize("after Insert", tree, i+1);
		}
		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}

	@Test
	public void testRemoveSmall() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;

		for (int i=0; i < num; ++i) {
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(i);
			bstv.check();
			verifySize("after Insert", tree, i+1);
		}

		for (int i=0; i < num/2; ++i) {
			verifySize("before Remove", tree, num - i);
			bstv.check();
			tree.remove((i + 7) % num);
			bstv.check();
			verifySize("after Remove", tree, num - i - 1);
		}

		for (int i=0; i < num; ++i) {
			bstv.check();
			tree.insert(i);
			bstv.check();
		}
		verifySize("after Remove", tree, num);

		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}

	@Test
	public void testRemoveLarge() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 200000;

		for (int i=0; i < num; ++i) {
			tree.insert(i);
		}
		bstv.check();

		for (int i=0; i < num/2; ++i) {
			tree.remove((i + 7) % num);
		}
		bstv.check();

		for (int i=0; i < num; ++i) {
			tree.insert(i);
		}
		bstv.check();
		verifySize("after Remove", tree, num);
	}

	private void verifySize(String event, AVLTree<?> tree, int expectedSize) {
		assertEquals("Expect tree " + event + " to have size " + 
				expectedSize + " but it did not", expectedSize, tree.size);
	}

	@Test
	public void testRebalanceSmall() {
		// left-heavy tree
		BSTValidator<Integer> bstvL = genTree();
		AVLTree<Integer> treeL = bstvL.tree;

		//test left-left
		bstvL.check();
		treeL.insert(15);
		bstvL.check();
		treeL.insert(10);
		bstvL.check();
		treeL.insert(5);  // right-rotate here
		bstvL.check();

		//test left-right
		treeL.insert(6);
		bstvL.check();
		treeL.insert(7);  //left-rotate here
		bstvL.check();
		treeL.insert(8);  //double-rotate here (L then R)
		bstvL.check();

		// right-heavy tree
		BSTValidator<Integer> bstvR = genTree();
		AVLTree<Integer> treeR = bstvR.tree;

		//test right-right
		bstvR.check();
		treeR.insert(5);
		bstvR.check();
		treeR.insert(10);
		bstvR.check();
		treeR.insert(15);  // left-rotate here
		bstvR.check();

		//test left-right
		treeR.insert(14);
		bstvR.check();
		treeR.insert(13);  //right-rotate here
		bstvR.check();
		treeR.insert(12);  //double-rotate here (R then L)
		bstvR.check();

	}


	private ArrayList<Integer> genUniqueInts(int num) {
		ArrayList<Integer> ans = new ArrayList<Integer>(num);
		for (int i=0; i < num; ++i) {
			ans.add(i,i);
		}
		Collections.shuffle(ans);
		return ans;
	}

}
