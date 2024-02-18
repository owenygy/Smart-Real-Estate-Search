package com.example.myapplication.internal;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BiConsumer;

import static com.example.myapplication.internal.RBTree.Colour.BLACK;
import static com.example.myapplication.internal.RBTree.Colour.RED;

public class RBTree<K extends Comparable<K>, V> {

    private Node<K, V> root; // The root node of the tree

    /**
     * Initialises empty RBTree.
     */
    public RBTree() {
        root = null;
    }

    /**
     * Traverses all the nodes (with duplicate key occurrences) in the RBTree in ascending order of key.
     * In other words, during iteration the same key may appear more than once with different values.
     */
    public void forEachAscending(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Node<K, V> e = min(); e != null; e = successor(e)) {
            for (V v : e.values) {
                action.accept(e.key, v);
            }
        }
    }

    /**
     * Traverses all the nodes (with duplicate key occurrences) in the RBTree in descending order of key.
     * In other words, during iteration the same key may appear more than once with different values.
     */
    public void forEachDescending(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Node<K, V> e = max(); e != null; e = predecessor(e)) {
            for (V v : e.values) {
                action.accept(e.key, v);
            }
        }
    }

    /**
     * Traverses all the nodes (without duplicate key) in the RBTree in ascending order of key.
     * In other words, a key appear only once during the iteration and multi values appear as LinkedList.
     */
    public void forEachAscendingAll(BiConsumer<? super K, ? super LinkedList<V>> action) {
        Objects.requireNonNull(action);
        for (Node<K, V> e = min(); e != null; e = successor(e)) {
            action.accept(e.key, e.values);
        }
    }

    /**
     * Traverses all the nodes (without duplicate key) in the RBTree in descending order of key.
     * In other words, a key appear only once during the iteration and multi values appear as LinkedList.
     */
    public void forEachDescendingAll(BiConsumer<? super K, ? super LinkedList<V>> action) {
        Objects.requireNonNull(action);
        for (Node<K, V> e = max(); e != null; e = predecessor(e)) {
            action.accept(e.key, e.values);
        }
    }

    /**
     * Returns the first node (with the least key) in the RBTree.
     */
    public Node<K, V> min() {
        Node<K, V> t = root;
        if (t != null) {
            while (t.left != null)
                t = t.left;
        }
        return t;
    }

    /**
     * Returns the last node (with the greatest key) in the RBTree.
     */
    public Node<K, V> max() {
        Node<K, V> t = root;
        if (t != null) {
            while (t.right != null)
                t = t.right;
        }
        return t;
    }

    /**
     * Checks whether the RBTree contains the specified value.
     *
     * @return true if the RBTree contains the value, otherwise false.
     */
    public boolean containsValue(V value) {
        for (Node<K, V> e = min(); e != null; e = successor(e)) {
            for (V v : e.values) {
                if (value.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether the RBTree contains the specified key.
     *
     * @return true the RBTree contains the key, otherwise false.
     */
    public boolean containsKey(K key) {
        return searchNode(key) != null;
    }

    /**
     * Inserts node into RBTree.
     *
     * @param key   key of the node
     * @param value value of the node
     * @return old value if the key to which the value is mapped already exists, or null if it does not
     */
    public V insert(K key, V value) {
        LinkedList<V> values = new LinkedList<V>() {{
            addLast(value);
        }};

        if (root == null) {
            root = new Node<>(key, values, null);
            return null;
        }

        int cmp;
        Node<K, V> t = root; // root node is not null
        Node<K, V> parent;
        do {
            parent = t;
            cmp = key.compareTo(t.key);
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {
                t = t.right;
            } else {
                return t.append(value);
            }
        } while (t != null);
        Node<K, V> n = new Node<>(key, values, parent);
        if (cmp < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }

        // Fix tree
        fixInsert(n);
        return null;
    }

    /**
     * Inserts node into RBTree.
     *
     * @param key    key of the node
     * @param values values of the node
     */
    public void insertAll(K key, LinkedList<V> values) {
        if (root == null) {
            root = new Node<>(key, values, null);
            return;
        }

        int cmp;
        Node<K, V> t = root; // root node is not null
        Node<K, V> parent;
        do {
            parent = t;
            cmp = key.compareTo(t.key);
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {
                t = t.right;
            } else {
                t.appendAll(values);
            }
        } while (t != null);
        Node<K, V> n = new Node<>(key, values, parent);
        if (cmp < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }

        // Fix tree
        fixInsert(n);
    }

    /**
     * Deletes the last value of the node to which the specified key maps.
     *
     * @param key the key of the node to be deleted
     * @return old value to which the key maps
     */
    public V delete(K key) {
        Node<K, V> x = searchNode(key);
        if (x == null) {
            return null; // Key does not exist
        }
        if (x.values.size() > 1) {
            return x.values.pollLast();
        }
        V old = x.values.getLast(); // Let gc delete it. We only *get* it.
        deleteNode(x);
        return old;
    }

    /**
     * Deletes the specified value of the node to which the specified key maps.
     *
     * @param key   the key of the value to be deleted
     * @param value the value to be deleted
     * @return old value to which the key maps, or null if the specified value does not exist
     */
    public V delete(K key, V value) {
        Node<K, V> x = searchNode(key);
        if (x == null) {
            return null; // Key does not exist
        }
        V old;
        if (x.values.size() > 1) {
            // #values is >= 2
            Iterator<V> itr = x.values.iterator();
            while (itr.hasNext()) {
                // find the specified value and delete it
                if ((old = itr.next()).equals(value)) {
                    itr.remove();
                    return old;
                }
            }
        } else {
            return delete(key);
        }
        return null;
    }

    /**
     * Deletes the node to which the specified key maps.
     *
     * @param key the key of the node to be deleted
     * @return old value to which the key maps
     */
    public LinkedList<V> deleteAll(K key) {
        Node<K, V> x = searchNode(key);
        if (x == null) {
            return null; // Key does not exist
        }
        LinkedList<V> old = x.values;
        deleteNode(x);
        return old;
    }

    /**
     * Returns the sub tree where all the keys fall in range between fromKey and toKey (inclusive).
     *
     * @param fromKey inclusive, the start point of the range
     * @param toKey   inclusive, the end point of the range
     * @return the sub tree where all the keys fall in range between fromKey and toKey (inclusive)
     */
    public RBTree<K, V> subTree(K fromKey, K toKey) {
        if (fromKey.compareTo(toKey) > 0) {
            throw new IllegalArgumentException("fromKey must be less than or equal to toKey");
        }

        RBTree<K, V> subTree = new RBTree<>();
        Node<K, V> t = root;

        // Morris Traversal - Inorder
        while (t != null) {
            if (t.left == null) {
                if (t.key.compareTo(toKey) <= 0 && t.key.compareTo(fromKey) >= 0) {
                    subTree.insertAll(t.key, t.values);
                }
                t = t.right;
            } else {
                Node<K, V> pre = t.left;
                while (pre.right != null && pre.right != t) {
                    pre = pre.right;
                }
                if (pre.right == null) {
                    pre.right = t;
                    t = t.left;
                } else {
                    pre.right = null;
                    if (t.key.compareTo(toKey) <= 0 && t.key.compareTo(fromKey) >= 0) {
                        subTree.insertAll(t.key, t.values);
                    }
                    t = t.right;
                }
            }
        }
        return subTree;
    }

    /**
     * Returns the sub tree where all the keys fall in range between fromKey and toKey (inclusive).
     *
     * @param fromKey inclusive, the start point of the range
     * @param toKey   inclusive, the end point of the range
     * @param comp    comparator
     * @return the sub tree where all the keys fall in range between fromKey and toKey (inclusive)
     */
    public RBTree<K, V> subTree(K fromKey, K toKey, Comparator<K> comp) {
        if (fromKey.compareTo(toKey) > 0) {
            throw new IllegalArgumentException("fromKey must be less than or equal to toKey");
        }

        RBTree<K, V> subTree = new RBTree<>();
        Node<K, V> t = root;

        // Morris Traversal - Inorder
        while (t != null) {
            if (t.left == null) {
                if (comp.compare(t.key, toKey) <= 0 && comp.compare(t.key, fromKey) >= 0) {
                    subTree.insertAll(t.key, t.values);
                }
                t = t.right;
            } else {
                Node<K, V> pre = t.left;
                while (pre.right != null && pre.right != t) {
                    pre = pre.right;
                }
                if (pre.right == null) {
                    pre.right = t;
                    t = t.left;
                } else {
                    pre.right = null;
                    if (comp.compare(t.key, toKey) <= 0 && comp.compare(t.key, fromKey) >= 0) {
                        subTree.insertAll(t.key, t.values);
                    }
                    t = t.right;
                }
            }
        }
        return subTree;
    }

    /**
     * Returns a corresponding value to which the key maps.
     *
     * @param key the key to search for
     * @return a corresponding value, or null if the key does not exist
     */
    public V search(K key) {
        Node<K, V> x = searchNode(key);
        return x != null ? x.values.getLast() : null;
    }

    /**
     * Returns all the corresponding values to which the key maps.
     *
     * @param key the key to search for
     * @return all the corresponding values, or null if the key does not exist
     */
    public LinkedList<V> searchAll(K key) {
        Node<K, V> x = searchNode(key);
        return x != null ? x.values : null;
    }

    /**
     * Delete all the nodes of the RBTree.
     */
    public void clear() {
        root = null;
    }

    private Node<K, V> searchNode(K key) {
        Node<K, V> x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                return x;
            }
        }
        return null;
    }

    // Rotate the node so it becomes the child of its right branch
    /*
        e.g.
              [x]                    b
             /   \                 /   \
           a       b     == >   [x]     f
          / \     / \           /  \
         c  d    e   f         a    e
                              / \
                             c   d
    */
    private void rotateLeft(Node<K, V> x) {
        if (x != null) {
            Node<K, V> r = x.right;
            x.right = r.left;
            if (r.left != null) {
                r.left.parent = x;
            }
            r.parent = x.parent;
            if (x.parent == null) {
                root = r;
            } else if (x.parent.left == x) {
                x.parent.left = r;
            } else {
                x.parent.right = r;
            }
            r.left = x;
            x.parent = r;
        }
    }

    // Rotate the node so it becomes the child of its left branch
    /*
        e.g.
              [x]                    a
             /   \                 /   \
           a       b     == >     c     [x]
          / \     / \                   /  \
         c  d    e   f                 d    b
                                           / \
                                          e   f
    */
    private void rotateRight(Node<K, V> x) {
        if (x != null) {
            Node<K, V> l = x.left;
            x.left = l.right;
            if (l.right != null) {
                l.right.parent = x;
            }
            l.parent = x.parent;
            if (x.parent == null) {
                root = l;
            } else if (x.parent.right == x) {
                x.parent.right = l;
            } else {
                x.parent.left = l;
            }
            l.right = x;
            x.parent = l;
        }
    }

    private void deleteNode(Node<K, V> x) {
        if (x.left != null && x.right != null) {
            Node<K, V> s = successor(x);
            x.key = s.key;
            x.values = s.values;
            x = s;
        }
        Node<K, V> child = (x.left != null ? x.left : x.right);
        if (child != null) {
            child.parent = x.parent;
            if (x.parent == null) {
                root = child;
            } else if (x == x.parent.left) {
                x.parent.left = child;
            } else {
                x.parent.right = child;
            }
            x.parent = null;
            x.right = null;
            x.left = null;

            if (x.color == BLACK) {
                fixDelete(child);
            }
        } else if (x.parent == null) {
            root = null;
        } else {
            if (x.color == BLACK) {
                fixDelete(x);
            }
            if (x.parent != null) {
                if (x == x.parent.left) {
                    x.parent.left = null;
                } else if (x == x.parent.right) {
                    x.parent.right = null;
                }
                x.parent = null;
            }
        }
    }

    private void fixDelete(Node<K, V> x) {
        while (x != root && color(x) == BLACK) {
            Node<K, V> sib = x.sib();
            if (x == left(parent(x))) {
                if (color(sib) == RED) {
                    // Case 1 : x's sibling w is red
                    setColor(sib, BLACK);
                    setColor(parent(x), RED);
                    rotateLeft(parent(x));
                    sib = right(parent(x));
                }
                if (color(left(sib)) == BLACK && color(right(sib)) == BLACK) {
                    // Case 2 : x's sibling w is black, and both of w's children are black
                    setColor(sib, RED);
                    x = parent(x);
                } else {
                    if (color(right(sib)) == BLACK) {
                        // Case 3 : x's sibling w is black, w's left child is red, and w's right child is black
                        setColor(left(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = right(parent(x));
                    }
                    // Case 4: x's sibling w is black, and w's right child is red
                    setColor(sib, color(parent(x)));
                    setColor(parent(x), BLACK);
                    setColor(right(sib), BLACK);
                    rotateLeft(parent(x));
                    x = root;
                }
            } else {
                // Same as then clause with "right" and "left" exchanged)
                if (color(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parent(x), RED);
                    rotateRight(parent(x));
                    sib = left(parent(x));
                }
                if (color(right(sib)) == BLACK &&
                        color(left(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parent(x);
                } else {
                    if (color(left(sib)) == BLACK) {
                        setColor(right(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = left(parent(x));
                    }
                    setColor(sib, color(parent(x)));
                    setColor(parent(x), BLACK);
                    setColor(left(sib), BLACK);
                    rotateRight(parent(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private void fixInsert(Node<K, V> x) {
        setColor(x, RED);
        while (x != null && x != root && x.parent.color == RED) {
            if (parent(x) == left(parent(parent(x)))) {
                Node<K, V> y = right(parent(parent(x)));
                if (color(y) == RED) {
                    // Case 1 : Recolour
                    setColor(parent(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parent(parent(x)), RED);

                    // Check if violated further up the tree
                    x = parent(parent(x));
                } else {
                    if (x == right(parent(x))) {
                        // Case 2 : Right (uncle is left node) / Left (uncle is right node) Rotation
                        x = parent(x);
                        rotateLeft(x);
                    }
                    // Case 3 : Left (uncle is left node) / Right (uncle is right node) Rotation
                    setColor(parent(x), BLACK);
                    setColor(parent(parent(x)), RED);
                    rotateRight(parent(parent(x)));
                }
            } else {
                // Same as then clause with "right" and "left" exchanged
                Node<K, V> y = left(parent(parent(x)));
                if (color(y) == RED) {
                    setColor(parent(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parent(parent(x)), RED);
                    x = parent(parent(x));
                } else {
                    if (x == left(parent(x))) {
                        x = parent(x);
                        rotateRight(x);
                    }
                    setColor(parent(x), BLACK);
                    setColor(parent(parent(x)), RED);
                    rotateLeft(parent(parent(x)));
                }
            }
        }
        // Ensure property 2 (root and leaves are black) holds
        setColor(root, BLACK);
    }

    enum Colour {
        RED, BLACK
    }

    public static class Node<K, V> {

        K key;
        LinkedList<V> values;
        Node<K, V> left, right, parent;
        Colour color = BLACK;

        Node(K key, LinkedList<V> values, Node<K, V> parent) {
            this.key = key;
            this.values = values;
            this.parent = parent;
        }

        public K getKey() {
            return key;
        }

        public LinkedList<V> getValues() {
            return values;
        }

        public V append(V value) {
            V old = this.values.getLast();
            this.values.addLast(value);
            return old;
        }

        public void appendAll(LinkedList<V> values) {
            this.values.addAll(values);
        }

        public Node<K, V> sib() {
            if (parent == null) return null; // No parent = no sibling
            return (this == parent.left) ? right(parent) : left(parent);
        }

    }

    private static <K, V> Colour color(Node<K, V> x) {
        return (x == null ? BLACK : x.color);
    }

    private static <K, V> Node<K, V> parent(Node<K, V> x) {
        return (x == null ? null : x.parent);
    }

    private static <K, V> void setColor(Node<K, V> x, Colour color) {
        if (x != null) {
            x.color = color;
        }
    }

    private static <K, V> Node<K, V> left(Node<K, V> x) {
        return (x == null) ? null : x.left;
    }

    private static <K, V> Node<K, V> right(Node<K, V> x) {
        return (x == null) ? null : x.right;
    }

    private static <K, V> Node<K, V> successor(Node<K, V> x) {
        if (x == null) {
            return null;
        } else if (x.right != null) {
            return smallestChild(x.right);
        } else {
            Node<K, V> child = x;
            Node<K, V> parent = x.parent;
            while (parent != null && child == parent.right) {
                child = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }

    private static <K, V> Node<K, V> predecessor(Node<K, V> x) {
        if (x == null) {
            return null;
        } else if (x.left != null) {
            return greatestChild(x.left);
        } else {
            Node<K, V> child = x;
            Node<K, V> parent = x.parent;
            while (parent != null && child == parent.left) {
                child = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }

    private static <K, V> Node<K, V> smallestChild(Node<K, V> x) {
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }

    private static <K, V> Node<K, V> greatestChild(Node<K, V> x) {
        while (x.right != null) {
            x = x.right;
        }
        return x;
    }

}
