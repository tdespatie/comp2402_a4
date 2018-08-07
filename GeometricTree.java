package comp2402a4;

import java.util.Random;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashMap;

public class GeometricTree extends BinaryTree<GeometricTreeNode> {

	HashMap<GeometricTreeNode, Integer> map;
	public GeometricTree() {
		super (new GeometricTreeNode());
	  map = new HashMap<>();
	  parentMap = new HashMap<>();
	}

	public void inorderDraw() {
		assignLevels();
		/*GeometricTreeNode current = r, prev = null;
		while (current != null) {
			if (current.left == null) {
					current.position.x = i++;
			   current = current.right;
			} else {
			   prev = current.left;
			   while (prev.right != null && prev.right != current)
			       prev = prev.right;

			   if (prev.right == null) {
			       prev.right = current;
			       current = current.left;
			   } else {
			       prev.right = null;
			       current.position.x = i++;
			       current = current.right;
			   }
			}
		} */
		int i = 0;
		GeometricTreeNode current = firstNode();
		for (; current != null; i++) {
			current.position.x = i;
			current = nextNode(current);
		}
	}


	protected void randomX(GeometricTreeNode u, Random r) {
		if (u == null) return; // Non-existant Node
		u.position.x = r.nextInt(60); // Generate an x coordinate between 0 and 59
		randomX(u.left, r); // Randomize it's left child
		randomX(u.right, r); // Randomize it's right child
	}


	/**
	 * Draw each node so that it's x-coordinate is as small
	 * as possible without intersecting any other node at the same level
	 * the same as its parent's
	 */
	public void leftistDraw() {
		assignLevels();

		GeometricTreeNode prev = r;
		prev.position.x = 0;

		Queue<GeometricTreeNode> queue = new ArrayDeque<>();
		queue.add(r);

		while (!queue.isEmpty()) {
			GeometricTreeNode current = queue.remove();
			if (prev.position.y == current.position.y) {
				current.position.x = prev.position.x + 1;
				prev = current;
			} else {
				current.position.x = 0;
				prev = current;
			}
			if (current.left != null)
				queue.add(current.left);

			if (current.right != null)
				queue.add(current.right);
		}
		r.position.x = 0;
	}

	private GeometricTreeNode largerChild(GeometricTreeNode u) {
		if (map.get(u.right) > map.get(u.left)) // if the right side is larger than the left, return right
			return u.right;
		return u.left; // if the left side is larger than the right, return left
	}

	public int nonRecSize(GeometricTreeNode r) {
		if (r != null) {
				int size = 0; // size of tree
				Queue<GeometricTreeNode> q = new ArrayDeque<>();
				q.add(r);
				while (!q.isEmpty()) {
					GeometricTreeNode x = q.poll();
					size++;
					if(x.left != null)
						q.add(x.left);

					if(x.right!=null)
						q.add(x.right);
				}
				return size;
			}
			return 0;
	}


	public void balancedDraw() {
		int x = 0, y = 0;

		GeometricTreeNode current = firstNode(); // Get's the root node
		while (current != null) { // for each node, set the size of each tree
			current.position.x = 0;
			map.put(current, nonRecSize(current));
			current = nextNode(current); // Go to the next node
		}

		current = r; // Go back to the root
		Queue<GeometricTreeNode> q = new ArrayDeque<>();
		q.add(r);

		do {
			while (current.left != null || current.right != null) { // Continue until we can't go further down
				if (current.right != null && current.left != null) { // Decide which subtree to go down based on who's smaller
					if (map.get(current.left) < map.get(current.right))
						current = current.left;
					else
						current = current.right;
					y++; // Increment the depth
				} else {
					if (current.left != null) // If we can go left, go left
						current = current.left;
					else // If we can't go left, go right
						current = current.right;
					x++; // Increment the distance
				}
				current.position.x = x; // Set the nodes, current position
				current.position.y = y;
				//if (current.position.y == 0)
					//parentMap.put(current.position, current);
				System.out.println(current.position.toString());
			}

			do {
				current = current.parent; // while current node is not null and either the left or right is null or the node hasn't been assigned
				if (current != null)
				System.out.println ("I went up to " + current.position.toString());
			} while (current != null && ((current.left == null || current.right == null) || largerChild(current).position.x > 0));
			if (current == null) break; // if the node is empty, we've had an error
			System.out.println("I finished at " + current.position.toString());

			y = current.position.y;
			if (map.get(current.right) > map.get(current.left)) // if the right side is larger than the left, return right
				current = current.right;
			else
				current = current.left;
			current.position.y = y;
			current.position.x = ++x;
			System.out.println(current.position.toString());
		} while (true);
		System.out.println(parentMap);
	}

	protected void assignLevels() {
		assignLevels(r, 0);
	}

	protected void assignLevels(GeometricTreeNode u, int i) {
		if (u == null) return; // Non-existant Node, return
		u.position.y = i; // Assign current level
		assignLevels(u.left, i+1); // Assign one level below
		assignLevels(u.right, i+1); // Assign one level below
	}

	public static void main(String[] args) {
		GeometricTree t = new GeometricTree();
		galtonWatsonTree(t, 100);
		System.out.println(t);
		t.inorderDraw();
		System.out.println(t);
	}

}
