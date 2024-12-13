package Estruturas;

import Estruturas.Exceptions.*;
import Estruturas.Interfaces.BinaryTreeADT;
import java.util.Iterator;

/**
 * ArrayBinaryTree implements the BinaryTreeADT interface
 * 
 * @param <T> the element type
 */
public class ArrayBinaryTree<T> implements BinaryTreeADT<T> {
    protected int count;
    protected T[] tree;
    private final int CAPACITY = 50;

    /**
     * Creates an empty binary tree.
     */
    public ArrayBinaryTree() {
        // initialize the tree array and other necessary fields
        tree = (T[]) new Object[10]; // example initialization
        tree = (T[]) new Object[CAPACITY];
        count = 0;
    }

    /**
     * Creates a new binary tree with the specified element as its root
     * 
     * @param element the element that will become the root of the new tree
     */
    public ArrayBinaryTree(T element) {
        tree = (T[]) new Object[CAPACITY];
        tree[0] = element;
        count = 1;
    }

    /**
     * Creates a new binary tree with the specified element as its root and the
     * given trees as its left child and right child
     */
    @Override
    public boolean contains(T targetElement) {
        try {
            find(targetElement);
            return true;
        } catch (ElementNotFoundException e) {
            return false;
        }
    }

    /**
     * Performs a recursive search on the binary tree to find the target element
     */
    @Override
    public Iterator<T> iteratorPostOrder() {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<T>();
        postorder(0, templist);
        return templist.iterator();
    }

    /**
     * Performs a recursive postorder traversal.
     *
     * @param node the node to be used as the root for this traversal
     * @param templist the temporary list for use in this traversal
     */
    protected void postorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length)
            if (tree[node] != null) {
                postorder(node * 2 + 1, templist);
                postorder((node + 1) * 2, templist);
                templist.addToRear(tree[node]);
            }
    }

    /**
     * Finds the specified target element in this binary tree.
     * 
     * @param targetElement the element to be added to this tree
     * @throws ElementNotFoundException if the target element is not found
     */
    @Override
    public T find(T targetElement) throws ElementNotFoundException {
        T temp = null;
        boolean found = false;

        for (int ct = 0; ct < count && !found; ct++)
            if (targetElement.equals(tree[ct])) {
                found = true;
                temp = tree[ct];
            }
        if (!found)
            throw new ElementNotFoundException("binary tree");
        return temp;
    }

    /**
     * Returns the size of this binary tree.
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Returns true if this binary tree is empty and false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Performs a recursive search on the binary tree to find the target element
     */
    @Override
    public Iterator<T> iteratorPreOrder() {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<T>();
        preorder(0, templist);
        return templist.iterator();
    }

    /**
     * Performs a recursive preorder traversal.
     *
     * @param node the node to be used as the root for this traversal
     * @param templist the temporary list for use in this traversal
     */
    protected void preorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length)
            if (tree[node] != null) {
                templist.addToRear(tree[node]);
                preorder(node * 2 + 1, templist);
                preorder((node + 1) * 2, templist);
            }
    }

    /**
     * Performs an inorder traversal on this binary tree by
     * calling an overloaded, recursive inorder method
     * that starts with the root.
     * 
     * @return an iterator over the binary tree
     * 
     */
    @Override
    public Iterator<T> iteratorInOrder() {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<T>();
        inorder(0, templist);
        return templist.iterator();
    }

    /**
     * Performs a recursive inorder traversal.
     *
     * @param node     the node used in the traversal
     * @param templist the temporary list used in the traversal
     */
    protected void inorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length)
            if (tree[node] != null) {
                inorder(node * 2 + 1, templist);
                templist.addToRear(tree[node]);
                inorder((node + 1) * 2, templist);
            }
    }

    /**
     * Returns a reference to the element at the root
     */
    @Override
    public T getRoot() {
        if (isEmpty()) {
            return null; // or throw an exception if preferred
        }
        return tree[0];
    }

    /**
     * Performs a recursive search on the binary tree to find the target element
     */
    @Override
    public Iterator<T> iteratorLevelOrder() {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<T>();
        for (int i = 0; i < count; i++) {
            templist.addToRear(tree[i]);
        }
        return templist.iterator();
    }
}