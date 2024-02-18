package com.example.myapplication;

import com.example.myapplication.internal.RBTree;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RBTreeTest {

    private final static int TREE_SIZE = 5000; // how many nodes in the tree
    private final static int MAX_VALUE = 1000; // max value of the node

    private RBTree<Integer, String> tree;
    private Map<Integer, LinkedList<String>> pool;
    private Random rnd;

    @Before
    public void init() {
        tree = new RBTree<>();

        // randomly generate node
        rnd = new Random();
        pool = new HashMap<>();
        for (int i = 0; i < TREE_SIZE; i++) {
            int key = rnd.nextInt(MAX_VALUE);
            if (pool.containsKey(key)) {
                Objects.requireNonNull(pool.get(key)).addLast(String.valueOf(key - rnd.nextInt(MAX_VALUE)));
            } else {
                LinkedList<String> values = new LinkedList<>();
                values.addLast(String.valueOf(key - rnd.nextInt(MAX_VALUE)));
                pool.put(key, values);
            }
        }

        // or, manually generate node
//        pool.add(2);
//        pool.add(89);
//        pool.add(53);
//        pool.add(36);
//        pool.add(47);

        // ... and insert these nodes into the tree
        pool.forEach((k, v) -> tree.insertAll(k, (LinkedList<String>) v.clone())); // use clone() to pass-by-value
        System.out.println(pool.toString());
    }

    @Test
    public void testInsert() {
        // Test whether the inserted node is searchable
        pool.forEach((k, v) -> assertEquals(tree.search(k), v.pollLast()));
    }

    @Test
    public void testInsertAppend() {
        pool.forEach((k, v) -> assertEquals(tree.searchAll(k), v));

        Map<Integer, String> randPairs = new HashMap<>();
        pool.forEach((k, v) -> randPairs.put(k, v.getLast()));

        // overwrite values to which existing keys maps
        randPairs.forEach((k, v) -> tree.insert(k, v));

        // check if the correctness of overwriting
        randPairs.forEach((k, v) -> assertEquals(tree.search(k), v));
    }

    @Test
    public void testSearchNonExisting() {
        // Test searching for non-existing node
        for (int i = 0; i < TREE_SIZE; i++) {
            int x = (int) (MAX_VALUE + rnd.nextInt(1000)); // we plus MAX_VALUE so that the nodes never appear
            assertNull(tree.search(x));
        }
    }

    @Test
    public void testDelete() {
        pool.forEach((k, v) -> {
            String deleted = tree.delete(k);
            String expectedDeleted = v.pollLast();
            assertEquals(deleted, expectedDeleted);
        });
    }

    @Test
    public void testDeleteAll() {
        pool.forEach((k, v) -> {
            tree.deleteAll(k);
            assertNull(tree.search(k));
            assertNull(tree.searchAll(k));
        });
    }

    @Test
    public void testSubTree() {
        int lo = 0; // lower bound of the node in the sub tree
        int hi = (int) (MAX_VALUE * 0.5); // higher bound of the node in the sub tree

        Set<Integer> expectedSet = new HashSet<Integer>() {{
            pool.forEach((k, v) -> {
                if (lo <= k && k <= hi) {
                    add(k);
                }
            });
        }};
        Set<Integer> actualSet = new HashSet<Integer>() {{
            tree.subTree(lo, hi).forEachAscendingAll((k, v) -> add(k));
        }};

        assertEquals(actualSet, expectedSet);
    }

    @Test
    public void testContainsValue() {
        pool.forEach((k, v) -> v.forEach(vv ->assertTrue(tree.containsValue(vv))));
    }

    @Test
    public void testContainsKey() {
        pool.forEach((k, v) -> assertTrue(tree.containsKey(k)));
    }

    @Test
    public void testForEachAscending() {
        AtomicInteger prevKey = new AtomicInteger(tree.min().getKey());
        tree.forEachAscending((k, v) -> {
            assertTrue(k.compareTo(prevKey.get()) >= 0);
            prevKey.set(k);
        });
    }

    @Test
    public void testForEachDescending() {
        AtomicInteger prevKey = new AtomicInteger(tree.max().getKey());
        tree.forEachDescending((k, v) -> {
            assertTrue(k.compareTo(prevKey.get()) <= 0);
            prevKey.set(k);
        });
    }

    @Test
    public void testForEachDescendingAll() {
        AtomicInteger prevKey = new AtomicInteger(tree.max().getKey());
        tree.forEachDescendingAll((k, v) -> {
            assertTrue(k.compareTo(prevKey.get()) <= 0);
            prevKey.set(k);
        });
    }

    @Test
    public void testClear() {
        tree.clear();
    }

}

