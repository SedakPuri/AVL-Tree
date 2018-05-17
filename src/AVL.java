import java.util.Iterator;


public class AVL<E extends Comparable<E>> implements Set<E> 
{
	private int size;
	private Node<E> root;
	
	public AVL()
	{
		size = 0;
		root = null;
	}

	/**
	 * Find the node that contains v if it exits. If it doesn't exist
	 * then return the node that would have been the parent of a node
	 * containing v.
	 * @param r root of the subtree
	 * @param v value we are searching for
	 * @return Node containing v or node that would have a parent of v
	 */
	private Node<E> find(Node<E> r, E v)
	{
		//Case 1: Tree is empty
		if(r == null)
		{
			return null;
		}
		
		//Case 2: r contains v OR r is a leaf 
		if(r.value.equals(v))
		{
			return r;
		}
		
		//Case 3: r contains a value that is less than v
		if(r.value.compareTo(v) < 0 && r.right != null)
		{
			return find(r.right, v);
		}
		
		//Case 4: r contains a value that is greater than v
		if(r.value.compareTo(v) > 0 && r.left != null)
		{
			return find(r.left, v);
		}
		
		
		//All other cases: I didn't find the value
		return r;
		
	}
	
	/* return the new root of the tree after left rotation
	 * 			x               y
	 * 		   / \             / \ 
	 * 		  T1	  y    =>     x   T3
	 * 		      /\         /\
	 * 		     T2 T3      T1 T2
	 */
	private Node<E> leftRotate(Node<E> x)
	{
		Node<E> y = x.right;
		Node<E> t2 = y.left;
		
		y.left = x;
		x.right = t2;
		
		//Re-calculate height of x BEFORE y because x is lower node
		this.recalculateHeight(x);
		this.recalculateHeight(y);
		
		return y;
		
	}

	
	/* return the new root of the tree after left rotation
	 * 			x               y
	 * 		   / \             / \ 
	 * 		  T1	  y    <=     x   T3
	 * 		      /\         /\
	 * 		     T2 T3      T1 T2
	 */
	private Node<E> rightRotate(Node<E> y)
	{
		Node<E> x = y.left;
		Node<E> t2 = x.right;
		
		x.right = y;
		y.left = t2;
		
		//Re-calculate height of y BEFORE x because y is lower node
		this.recalculateHeight(y);
		this.recalculateHeight(x);
		
		return x;
		
	}

	/*
	 * Rebalance left rebalances the tree rooted at z if the height
	 * of the left subtree - height of the right subtree is greater than 1
	 * returns the new root of the subtree after rebalance
	 */
	private Node<E> rebalanceLeft(Node<E> z)
	{
		if(heightNode(z.left) - heightNode(z.right) > 1)
		{
			Node<E> y = z.left;
			if(heightNode(y.left) >= heightNode(y.right))
			{
				//Left left:
				z = rightRotate(z);
				
			}
			else
			{
				//Left Right:
				z.left = leftRotate(y);
				z = rightRotate(z);
			}
		}
		
		return z;
	}
	
	private Node<E> rebalanceRight(Node<E> z)
	{
		if(heightNode(z.right) - heightNode(z.left) > 1)
		{
			Node<E> y = z.right;
			if(heightNode(y.right) >= heightNode(y.left))
			{
				//Right Right:
				z = leftRotate(z);
				
			}
			else
			{
				//Right left:
				z.right = rightRotate(y);
				z = leftRotate(z);
			}
		}
		
		return z;
	}

	private int heightNode(Node<E> n)
	{
		if(n == null)
		{
			return -1;
		}
		return n.height;
	}
	private void recalculateHeight(Node<E> n)
	{
		n.height = 1 + Math.max(heightNode(n.left), heightNode(n.right));
	}
	
	/**
	 * 
	 * @param k key
	 * @param r root of the subtree in the recursive call
	 * @return root of the subtree after insert is completed
	 */
	private Node<E> add(E k, Node<E> r)
	{
		//If the subtree is empty that's where we insert the new node
		if(r == null)
		{
			size++;
			r = new Node(k);
			return r;			
		}
		
		if(k.equals(r.value))
		{
			return r;
		}
		else if(k.compareTo(r.value) < 0)
		{
			r.left = add(k, r.left);
			recalculateHeight(r);
			r = rebalanceLeft(r);
		}
		else
		{
			r.right = add(k, r.right);			
			recalculateHeight(r);
			r = rebalanceRight(r);

		}
		return r;
			
	}
	/**
	 * Adds a node with the value k if it doesn't exist
	 * @param k value to be inserted
	 * @return true if value was inserted successfully
	 */
	public boolean add(E k) 
	{
		int oldSize = size;
		root = add(k, root);
		return oldSize < size;
	}

	/**
	 * Remove the node with value v and return the root of the subtree with the node removed
	 * @param r root of the subtree 
	 * @param v value to remove
	 * @return root of the new subtree
	 */
	private Node<E> remove(Node<E> r, E v)
	{
		//Case 1: tree is empty
		if(r == null)
		{
			return null;
		}
		if(r.value.compareTo(v) < 0)
		{
			r.right = remove(r.right, v);
			recalculateHeight(r);
			r = rebalanceLeft(r);
		}
		else if(r.value.compareTo(v) > 0)
		{
			r.left = remove(r.left, v);
			recalculateHeight(r);
			r = rebalanceRight(r);
		}
		else
		{
			size--;
			//Case 1: leaf
			if(r.right == null && r.left == null)
			{
				return null;
			}
			//Case 2: single child
			if(r.right == null)
			{
				return r.left;
			}
			if(r.left == null)
			{
				return r.right;
			}
			//Case 3: two children;
			Node<E> pred = r.left;
			while(pred.right != null)
			{
				pred = pred.right;
			}
			r.value = pred.value;
			r.left = remove(r.left, pred.value);
		}
		
		return r;
		
	}
	
	public boolean remove(E k) 
	{
		int oldSize = size;
		root = remove(root, k);
		return oldSize > size;
	}

	
	private void toString(Node<E> r, StringBuilder sb, int level)
	{
		if(r != null)
		{
			//Print the root
			for(int i=0; i < 2 * level; i++)
			{
				sb.append(" ");
			}
			
			//Recursively print the left and right children
			sb.append(r.toString() + "\n");
			
			toString(r.left, sb, level+1);
			toString(r.right, sb, level+1);
		}
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		toString(root, sb, 0);
		String r = sb.toString();
		r += "Tree height: " + root.height; 
		return r;
	}

	
	public boolean contains(E k)
	{
		Node<E> n = find(root, k);
		return n != null && n.value.equals(k);
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAll(Set<E> other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retainAll(Set<E> other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAll(Set<E> other) {
		// TODO Auto-generated method stub
		
	}

	private static class Node<T extends Comparable<T>>
	{
		private T value;
		private Node<T> left;
		private Node<T> right;
		private int height;
		
		public Node(T v)
		{
			value = v;
			left = null;
			right = null;
			height = 0;
		}
		
		public String toString()
		{
			return "(" + height + ")" + value.toString();
		}
	}
}
