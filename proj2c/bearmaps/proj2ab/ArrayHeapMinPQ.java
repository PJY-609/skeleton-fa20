package bearmaps.proj2ab;

import java.util.*;


public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    class Node {
        private final T item;

        private int index;

        private double priority;

        Node(T itm, int i, double p) {
            item = itm;
            index = i;
            priority = p;
        }

    }

    private final ArrayList<Node> nodes;

    private final HashMap<T, Integer> items;

    private int size;

    public ArrayHeapMinPQ() {
        nodes = new ArrayList<>();
        items = new HashMap<>();

        // leave one empty spot at the beginning of the array to simplify computation.
        nodes.add(null);

        size = 0;
    }

    private int getParent(int index) {
        return index / 2;
    }

    private int getLeftChild(int index) {
        return index * 2;
    }

    private int getRightChild(int index) {
        return index * 2 + 1;
    }

    @Override
    public void add(T item, double priority) {
        if (items.containsKey(item)) {
            throw new IllegalArgumentException();
        }

        size++;

        Node node = new Node(item, size, priority);
        nodes.add(node);
        swim(size);

        items.put(item, node.index);
    }

    private void swap(int index1, int index2) {
        Node tempNode = nodes.get(index1);
        nodes.set(index1, nodes.get(index2));
        nodes.set(index2, tempNode);

        int tempIndex = nodes.get(index1).index;
        nodes.get(index1).index = nodes.get(index2).index;
        nodes.get(index2).index = tempIndex;

        items.put(nodes.get(index1).item, nodes.get(index1).index);
        items.put(nodes.get(index2).item, nodes.get(index2).index);
    }

    private void swim(int index) {
        Node node = nodes.get(index);
        Node parentNode = nodes.get(getParent(index));

        if (node == null || parentNode == null) {
            return;
        }

        if (node.priority < parentNode.priority) {
            swap(index, getParent(index));
            swim(getParent(index));
        }
    }

    @Override
    public boolean contains(T item) {
        return items.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (items.isEmpty()) {
            throw new NoSuchElementException();
        }

        return nodes.get(1).item;
    }

    @Override
    public T removeSmallest() {
        if (items.isEmpty()) {
            throw new NoSuchElementException();
        }

        swap(1, size);

        Node rmNode = nodes.remove(size);
        items.remove(rmNode.item);
        size--;

        if (size == 0) {
            return rmNode.item;
        }

        sink(1);

        return rmNode.item;
    }

    private void sink(int index) {
        if (1 > index && index > size) {
            return;
        }
        Node node = nodes.get(index);

        Node leftNode = null;
        int leftIndex = getLeftChild(index);
        if (1 <= leftIndex && leftIndex <= size) {
            leftNode = nodes.get(leftIndex);
        }

        Node rightNode = null;
        int rightIndex = getRightChild(index);
        if (1 <= rightIndex && rightIndex <= size) {
            rightNode = nodes.get(rightIndex);
        }

        if (leftNode != null && rightNode != null && node.priority > leftNode.priority && rightNode.priority > leftNode.priority) {
            swap(index, leftIndex);
            sink(leftIndex);
        } else if (leftNode != null && rightNode != null && node.priority > rightNode.priority && leftNode.priority > rightNode.priority) {
            swap(index, rightIndex);
            sink(rightIndex);
        } else if (leftNode != null && node.priority > leftNode.priority) {
            swap(index, leftIndex);
            sink(leftIndex);
        } else if (rightNode != null && node.priority > rightNode.priority) {
            swap(index, rightIndex);
            sink(rightIndex);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void changePriority(T item, double priority) {
        int index = items.get(item);
        Node node = nodes.get(index);
        node.priority = priority;

        Node parentNode = null;
        int parentIndex = getParent(index);
        if (1 <= parentIndex && parentIndex <= size) {
            parentNode = nodes.get(parentIndex);
        }

        Node leftNode = null;
        int leftIndex = getLeftChild(index);
        if (1 <= leftIndex && leftIndex <= size) {
            leftNode = nodes.get(leftIndex);
        }

        Node rightNode = null;
        int rightIndex = getRightChild(index);
        if (1 <= rightIndex && rightIndex <= size) {
            rightNode = nodes.get(rightIndex);
        }

        if (parentNode != null && node.priority < parentNode.priority) {
            swim(index);
        } else if (leftNode != null && node.priority > leftNode.priority) {
            sink(index);
        } else if (rightNode != null && node.priority > rightNode.priority) {
            sink(index);
        }
    }

    public Object[] toArray() {
        Object[] arr = new Object[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) == null) {
                arr[i] = 0;
            } else {
                arr[i] = nodes.get(i).item;
            }
        }
        return arr;
    }
}
