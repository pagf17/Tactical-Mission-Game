package Estruturas;

import Estruturas.Interfaces.BinaryTreeADT;
import Estruturas.Exceptions.ElementNotFoundException;

import java.util.Iterator;

public class LinkedBinaryTree<T> implements BinaryTreeADT<T> {


    public LinkedBinaryTree(BinaryTreeNode<T> root) {

        this.root = root;

        this.count = 1;

    }

    @Override
    public boolean contains(T targetElement) {
        try {
            find(targetElement);
            return true;
        } catch (ElementNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns a reference to the specified target element if it is
     * found in this binary tree. Throws a NoSuchElementException if
     * the specified target element is not found in the binary tree.
     *
     * @param targetElement the element being sought in this tree
     * @return a reference to the specified target
     * @throws ElementNotFoundException if an element not found
     *                                  exception occurs
     */
    public T find(T targetElement) throws ElementNotFoundException {
        BinaryTreeNode<T> current = findAgain(targetElement, root);

        if (current == null)
            throw new ElementNotFoundException("binary tree");

        return (current.element);
    }

    @Override
    public Iterator<T> iteratorPostOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        postorder(root, tempList);
        return tempList.iterator();
    }

    /**
     * Performs a recursive postorder traversal.
     *
     * @param node     the node to be used as the root
     *                 for this traversal
     * @param tempList the temporary list for use in this traversal
     */
    protected void postorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tempList) {
        if (node != null) {
            postorder(node.left, tempList);
            postorder(node.right, tempList);
            tempList.addToRear(node.element);
        }
    }

    @Override
    public Iterator<T> iteratorPreOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        preorder(root, tempList);
        return tempList.iterator();
    }

    /**
     * Performs a recursive preorder traversal.
     *
     * @param node     the node to be used as the root
     *                 for this traversal
     * @param tempList the temporary list for use in this traversal
     */
    protected void preorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tempList) {
        if (node != null) {
            tempList.addToRear(node.element);
            preorder(node.left, tempList);
            preorder(node.right, tempList);
        }
    }

    /**
     * Performs an inorder traversal on this binary tree by calling an
     * overloaded, recursive inorder method that starts with
     * the root.
     *
     * @return an in order iterator over this binary tree
     */
    public Iterator<T> iteratorInOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        inorder(root, tempList);

        return tempList.iterator();
    }

    /**
     * Performs a recursive inorder traversal.
     *
     * @param node     the node to be used as the root
     *                 for this traversal
     * @param tempList the temporary list for use in this traversal
     */
    protected void inorder(BinaryTreeNode<T> node,
            ArrayUnorderedList<T> tempList) {
        if (node != null) {
            inorder(node.left, tempList);
            tempList.addToRear(node.element);
            inorder(node.right, tempList);
        }
    }

    @Override
    public T getRoot() {
        return root.element;
    }

    @Override
    public Iterator<T> iteratorLevelOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        levelorder(root, tempList);
        return tempList.iterator();
    }

    /**
     * Performs a levelorder traversal on this binary tree, using a queue.
     *
     * @param node     the node to be used as the root for this traversal
     * @param tempList the temporary list for use in this traversal
     */
    protected void levelorder(BinaryTreeNode<T> node,
    ArrayUnorderedList<T> tempList) {
        int height = height(node);
        for (int i = 1; i <= height; i++) {
            levelOrderHelper(node, i, tempList);
        }
    }

    private void levelOrderHelper(BinaryTreeNode<T> node, int level, ArrayUnorderedList<T> tempList) {
        if (node == null) {
            return;
        }
        if (level == 1) {
            tempList.addToRear(node.element);
        } else if (level > 1) {
            levelOrderHelper(node.left, level - 1, tempList);
            levelOrderHelper(node.right, level - 1, tempList);
        }
    }

    private int height(BinaryTreeNode<T> node) {
        if (node == null) {
            return 0;
        } else {
            int leftHeight = height(node.left);
            int rightHeight = height(node.right);

            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    @Override
    public int size() {
        return count;
    }

    protected int count;
    protected BinaryTreeNode<T> root;

    /**
     * Creates an empty binary tree.
     */
    public LinkedBinaryTree() {
        count = 0;
        root = null;
    }

    /**
     * Creates a binary tree with the specified element as its root.
     *
     * @param element the element that will become the root of the
     *                new binary tree
     */
    public LinkedBinaryTree(T element) {
        count = 1;
        root = new BinaryTreeNode<T>(element);
    }

    /**
     * Returns a reference to the specified target element if it is
     * found in this binary tree.
     *
     * @param targetElement the element being sought in this tree
     * @param next          the element to begin searching from
     */
    private BinaryTreeNode<T> findAgain(T targetElement,
            BinaryTreeNode<T> next) {
        if (next == null)
            return null;

        if (next.element.equals(targetElement))
            return next;

        BinaryTreeNode<T> temp = findAgain(targetElement, next.left);

        if (temp == null)
            temp = findAgain(targetElement, next.right);

        return temp;
    }
}
